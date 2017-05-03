/**
 * @file GSM.cpp
 * @brief Main BLSGSM methods
 * @author Boshra Rajaei <bo.rajaei@gmail.com>
 *
 * Copyright (c) 2013, Boshra Rajaei
 * All rights reserved.
 *
 * This program is free software: you can use, modify and/or
 * redistribute it under the terms of the simplified BSD License. You
 * should have received a copy of this license along this program. If
 * not, see <http://www.opensource.org/licenses/bsd-license.html>.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <math.h>
#include <fftw3.h>
#include <vector>
#include <iostream>
#include <fstream>
#include <time.h>
#include <omp.h>

using namespace std;

#include "steerable_pyramid.h"
#include "utilities.h"
#include "Matrix.h"
#include "GSM.h"



//BLS-GSM denoising algorithm
//Inputs:
//-------->img: noisy image (also used as output denoised image)
//-------->sig: Gaussain noise standard deviation
//-------->PS:  power spectral density (white noise in this case)
//-------->blkSize: size of neighborhood around each coefficient at the same scale and orientation for making GSM model
//-------->parent: considering or not considering a coefficient from next coarser scale in GSM
//-------->boundary: boundary mirror extension to avoid boundary artifacts in reconstruction
//-------->Nsc: number of scales in steerable pyramid
//--------> Nor: number of orientations in each scale of pyramid
//Outputs:
//-------->img: denoised image
void denoi_BLS_GSM (vector< vector< float > >& img, float sig, vector< vector< float > > PS, int blSize[], int parent, int boundary, int Nsc, int Nor)
{
    int Ny = img.size();
    int Nx = img[0].size();
    int blSzY = blSize[0];
	int blSzX = blSize[1];

    //for applying steerable pyramid deconposition the image should has dimensions that are integer multiples of 2^(Nsc+1)
    //So Npy and Npx are the closest multiples of 2^(Nsc+1) to image size
    int Npy = ceil(Ny/pow(2,Nsc+1))*pow(2,Nsc+1);
    int Npx = ceil(Nx/pow(2,Nsc+1))*pow(2,Nsc+1);

    //in case Npy and Npx are larger than current size, mirror padding is used
    if(Npy!=Ny || Npx!=Nx)
    {
        int Bpy = Npy-Ny;
        int Bpx = Npx-Nx;

        //extending image only from down and right
        bound_extension(img,Bpy,Bpx,1);
    }

    //ensure power spectrum be in even dimensions
    fftshift(PS);
    int isoddPS_y = (PS.size() != 2*floor(PS.size()/2));
    int isoddPS_x = (PS[0].size() != 2*floor(PS[0].size()/2));
    if(isoddPS_y)
    {
        PS.resize(PS.size()-1);
    }
    if(isoddPS_x)
    {
        for(int i=0;i<PS.size();i++)
            PS[i].resize(PS[i].size()-1);
    }
    fftshift(PS);

    int Ndy = PS.size();
    int Ndx = PS[0].size();

    vector< vector< float > > delta(PS); //noise signal, this will be used for calculating C_w (neighborhood noise covariance) later

    for(int i=0;i<Ndy;i++)
    {
        for(int j=0;j<Ndx;j++)
        {
            PS[i][j] = sqrt(PS[i][j]);
        }
    }

    ifft2(delta, PS);

    fftshift(delta);
    resizedelta(delta, Npy, Npx);

    //By and Bx are size of extensions for boundary handeling depending on neighborhood size around each coefficient
    int By = (blSzY-1)*pow(2,Nsc-2);
    int Bx = (blSzX-1)*pow(2,Nsc-2);


    if(boundary)
    {
        //extending image and also delta for boundary handling and avoiding boundary artifacts
        bound_extension(img, By, Bx, 0);
        resizedelta(delta, Npy+2*By, Npx+2*Bx);
    }
    else
    {
        By = Bx = 0;
    }

    float m = sqrt(meansqr(delta));

    for(int i=0;i<Npy+By;i++)
    {
        for(int j=0;j<Npx+Bx;j++)
        {
            delta[i][j] /= m; //normalizing noise energy
            delta[i][j] *= sig;//applying the input standard deviation to noise
        }
    }

    //this function decomposes the image into steerable pyramid, denoises each coefficient and then reconstructs finally
    decomp_reconst_full(img, Nsc, Nor, blSize, delta, parent);

    clearMatrix(delta);

    //make the image again to its original size, remove the extenstions for boundary handeling and decomposition issues
    for(int i=0;i<Ny;i++)
    {
        for(int j=0;j<Nx;j++)
        {
            img[i][j] = img[i+By][j+Bx];
        }
    }

    img.resize(Ny);
    for(int i=0;i<Ny;i++)
        img[i].resize(Nx);
}

//this function decomposes the image into steerable pyramid, denoises each coefficient and then reconstructs finally
//Inputs:
//-------->img: noisy image (also used as output denoised image)
//-------->Nsc: number of scales in steerable pyramid
//-------->Nor: number of orientations in each scale of pyramid
//-------->block: size of neighborhood around each coefficient at the same scale and orientation for making GSM model
//-------->noise: noise signal, this will be used for calculating C_w (neighborhood noise covariance)
//-------->parent: considering or not considering a coefficient from next coarser scale in GSM
//Outputs:
//-------->img: denoised image
void decomp_reconst_full(vector< vector< float > >& img, int Nsc, int Nor, int block[], vector< vector< float > > noise, int parent)
{

    int Ny = img.size();
    int Nx = img[0].size();

    vector< vector< vector <float > > > pyr; //steerable pyramid corresponding to input noisy image; img variable
    vector< vector< vector <float > > > pyrN; //steerable pyramid corresponding to noise signal; noise variable

    //build steerable pyramid with Nsc scales and Nor oriantations at each scale for noisy image
    buildFullSFpyr2(img, pyr, Nsc, Nor, Ny, Nx);

    //build steerable pyramid with Nsc scales and Nor oriantations at each scale for noisy signal
    buildFullSFpyr2(noise, pyrN, Nsc, Nor, Ny, Nx);

    int nBand = pyr.size(); //total number of subbands in pyramid

    //denoise all subbands except lowpass residual
    #pragma omp parallel for shared(pyrN) schedule(dynamic, 1)
    for(int nb=0;nb<nBand-1;nb++)
    {
        vector< vector< float > > BL(pyr[nb]); //the current subband
        vector< vector< float > > BLn(pyrN[nb]); //the current subband from noise signal

        int Nsy = BL.size();
        int Nsx = BL[0].size();

        //according to Portilla's implementation, due to discaridng 2 coefficients from each dimension
        float factor = sqrt((((float)Nsy-2)*((float)Nsx-2))/((float)Nsy*(float)Nsx));
        for(int i=0;i<Nsy;i++)
        {
            for(int j=0;j<Nsx;j++)
            {
                BLn[i][j] *= factor;
            }
        }

        //is there any parent for current subband?
        int prnt = (parent) & ((nb!=0) & (nb < nBand-Nor));


        if(prnt)
        {
            vector< vector< float > > BLP(pyr[nb+Nor]); //parent subband in the next coarser scale and same orientaiton
            vector< vector< float > > BLPn(pyrN[nb+Nor]); //parent subband for noise

            //resample the parent by 2 to be the same size as the current subband
            expand(BLP);
            expand(BLPn);

            for(int i=0;i<Nsy;i++)
            {
                for(int j=0;j<Nsx;j++)
                {
                    BLPn[i][j] *= factor;
                }
            }

            //denoise the current subband
            denoi_BLS_GSM_band(BL, BLP, block, BLn, BLPn, prnt);


            clearMatrix(BLP);
            clearMatrix(BLPn);
        }
        else
        {
            vector< vector< float > > BLP;
            vector< vector< float > > BLPn;
            denoi_BLS_GSM_band(BL, BLP, block, BLn, BLPn, prnt);

            clearMatrix(BLP);
            clearMatrix(BLPn);
        }


        pyr[nb] = BL;

        clearMatrix(BL);
        clearMatrix(BLn);

        cout<<nb <<" ";
    }

    clearMatrix(pyrN);

    //reconstruct the steerable pyramid
    reconFullSFpyr2(img, pyr, Nsc, Nor, Ny, Nx);

    clearMatrix(pyr);

}

//denoising one subband
//Inputs:
//-------->y: noisy subband (also used as output denoised subband)
//-------->yp: the parent subband
//-------->block: size of neighborhood around each coefficient at the same scale and orientation for making GSM model
//-------->noise: corresponding subband from noise signal
//-------->noisep: the noise parent subband
//-------->prnt: if there is a parent? in case of initial setting to don't consider parent in modeling or for coarsest subband, this will be 0.
//Outputs:
//-------->y: denoised subband
void denoi_BLS_GSM_band(vector< vector< float > >& y, vector< vector< float > > yp, int block[], vector< vector< float > > noise, vector< vector< float > > noisep, int prnt)
{

    int nv = y.size();
    int nh = y[0].size();

    int nblv = nv-block[0]+1;
    int nblh = nh-block[1]+1;
    int nexp = nblv*nblh;
    int N = block[0]*block[1] + prnt; //number of elements participating in modeling a noiseless wavelet coefficient neighborhood

    int Ly = (block[0]-1)/2;
    int Lx = (block[1]-1)/2;

    int cent = floor((block[0]*block[1])/2); //reference element in neighborhood, cenrtal element

    float *C_w[N]; //noise covariance matrix at neighborhood
    for(int i=0;i<N;i++)
    {
        C_w[i] = new float[N];
        for(int j=0;j<N;j++)
            C_w[i][j] = 0;
    }

    float *C_y[N]; //noisy signal covariance matrix at neighborhood
    for(int i=0;i<N;i++)
    {
        C_y[i] = new float[N];
        for(int j=0;j<N;j++)
            C_y[i][j] = 0;
    }

    //calculating noise and noisy signal covariances at whole subband
    cov(noise, noisep, C_w, block, prnt);
    cov(y, yp, C_y, block, prnt);

    clearMatrix(noise);
    clearMatrix(noisep);

    float sig2 = 0; //noise variance in subband (without the coefficient from parent band)
    for(int i=0;i<N-prnt;i++)
        sig2 += C_w[i][i];
    sig2 /= N-prnt;

    float sy2 = 0; //same as last statement but in case of noisy subband y
    for(int i=0;i<N-prnt;i++)
        sy2 += C_y[i][i];
    sy2 /= N-prnt;


    float *S[N];
    float *iS[N];

    for(int i=0;i<N;i++)
    {
        S[i] = new float[N];
        iS[i] = new float[N];
    }

    //calculating S in S*S'= C_w
    symroot(C_w, S, N);

    minv(S, iS, N);

    float *C_x[N];//original signal covariance

    //As a result from independent assumption for noise and signal and y=x+w
    for(int i=0;i<N;i++)
    {
        C_x[i] = new float[N];
        for(int j=0;j<N;j++)
            C_x[i][j] = C_y[i][j] - C_w[i][j];
    }

    for(int i=0;i<N;i++)
    {
        delete[] C_w[i];
        delete[] C_y[i];
    }


    float *Q[N];
    float *L[N];

    for(int i=0;i<N;i++)
    {
        Q[i] = new float[N];
        L[i] = new float[N];
    }

    //eliminate possible negative eigenvalues in C_x without changing the overall variance
    Make_SemiDefinite(C_x, N);

    float sx2 = sy2 - sig2; //signal variance in subband (not parent)
    sx2 = sx2*(sx2>0);

    float *iST[N];
    float *aux2[N];
    float *aux1[N];

    for(int i=0;i<N;i++)
    {
        iST[i] = new float[N];
        aux2[i] = new float[N];
        aux1[i] = new float[N];
    }

    //calculating {Q,L} as eigenvactors/eigenvalues of inv(S)*C_x*transp(inv(S))
    transp(iS, iST, N);
    multiply(iS, C_x, aux1, N, N, N);
    multiply(aux1, iST, aux2, N, N, N);
    eigen(aux2, Q, L, N);

    float la[N]; //positive eigenvalues of inv(S)*C_x*transp(inv(S))
    for(int i=0;i<N;i++)
        la[i] = L[i][i] * (L[i][i] > 0);

    //release memory
    for(int i=0;i<N;i++)
    {
        delete[] iS[i];
        delete[] C_x[i];
        delete[] iST[i];
        delete[] aux1[i];
        delete[] aux2[i];
        delete[] L[i];
    }


    float *M[N];//M=S*Q
    float *V[N];//V=inv(M)*y
    float *iM[N];//inverse M

    for(int i=0;i<N;i++)
    {
        V[i] = new float[nexp];
        M[i] = new float[N];
        iM[i] = new float[N];
    }

    //calculating M=S*Q
    multiply(S, Q, M, N, N, N);

    //calculating V=inv(M)*y
    minv(M, iM, N);

    float *Y[N];

    for(int i=0;i<N;i++)
    {
      Y[i] = new float[nexp];
    }

    int ind[N-prnt][2];
    int cnt = 0;
    for(int i=Ly;i>=-Ly;i--)
        for(int j=Lx;j>=-Lx;j--)
        {
            ind[cnt][0] = i;
            ind[cnt][1] = j;
            cnt++;
        }

    cnt = 0;
    for(int j=Lx;j<nh-Lx;j++)
    {
        for(int i=Ly;i<nv-Ly;i++)
        {
            for(int n1=0;n1<N-prnt;n1++)
            {
                Y[n1][cnt] = y[i+ind[n1][0]][j+ind[n1][1]];

            }
            if(prnt)
            {
                Y[N-1][cnt] = yp[i][j];
            }
            cnt++;
        }
    }

    multiply(iM, Y, V, N, N, nexp);
    //End of calculating V=inv(M)*y

    clearMatrix(yp);

    float m[N];
    for(int i=0;i<N;i++)
        m[i] = M[cent][i];

    //release memory
    for(int i=0;i<N;i++)
    {
        delete[] Y[i];
        delete[] S[i];
        delete[] Q[i];
        delete[] M[i];
    }


    //------- Calculating p(y|z) and E{x|y,z} section---------------

    //these parameters are experimentally obtained for numerical calculation of integrals over z
    float lzmin = -20.5;
    float lzmax = 3.5;
    int step = 2;
    int nsamp_z = 1+ (int)(floor((lzmax-lzmin)/step)); //number of z samples

    float zi[nsamp_z];//z values

    cnt = 0;
    for(float i=lzmin;i<=lzmax;i=i+step)
        zi[cnt++] = exp(i);

    float *laz[N];//lambda*z as in p(y|z) relation

    for(int i=0;i<N;i++)
    {
        laz[i] = new float[nsamp_z];
    }

    for(int i=0;i<N;i++)
        for(int j=0;j<nsamp_z;j++)
            laz[i][j] = la[i] * zi[j];

    float pg1_lz[nsamp_z]; //the denomerator of p(y|z) relation

    //pg1_lz=1/(prod(sqrt(1+z*lambda)))
    for(int i=0;i<nsamp_z;i++)
    {
        pg1_lz[i] = 1;
        for(int j=0;j<N;j++)
            pg1_lz[i] *= 1.0/sqrt(1.0+laz[j][i]);
    }

    float **p_lz = new float*[nexp];//p(y|z)
    float **mu_x = new float*[nexp];//E{x|y,z}

    for(int i=0;i<nexp;i++)
    {
        p_lz[i] = new float[nsamp_z];
        mu_x[i] = new float[nsamp_z];
    }

    //According to equ (14) of Portilla et al paper
    for(int i=0;i<nexp;i++)
    {
        for(int k=0;k<nsamp_z;k++)
        {
            p_lz[i][k] = pg1_lz[k];
            for(int j=0;j<N;j++)
            {
                p_lz[i][k] *= exp(-0.5 * V[j][i]*V[j][i]/(1+laz[j][k]));
            }
        }
    }

    //According to equ (12) of Portilla et al paper
    for(int i=0;i<nexp;i++)
    {
        for(int k=0;k<nsamp_z;k++)
        {
            mu_x[i][k] = 0;
            for(int j=0;j<N;j++)
            {
                mu_x[i][k] +=  V[j][i] * laz[j][k]/(1.0+laz[j][k])*m[j];
            }
        }
    }

    for(int i=0;i<N;i++)
    {
        delete[] V[i];
        delete[] laz[i];
    }

    //------- End of calculating p(y|z) and E{x|y,z} section---------------


    //boundary handling section
    boundary_handling(y, p_lz, zi, block, nsamp_z, sx2, sig2);


    //Jefrrey's perior for p(z)
    float p_z[nsamp_z];
    for(int i=0;i<nsamp_z;i++)
        p_z[i] = 1.0/nsamp_z;


    //Compute p(z|y) from p(y|z) and p(z) (Bayes Rule)
    for(int i=0;i<nexp;i++)
    {
        for(int j=0;j<nsamp_z;j++)
        {
            p_lz[i][j] *= p_z[j]; //in the origin code this is actually the p_lz_y from this point on
        }
    }


    //normalizing p(z|y)
    int maxind;
    for(int i=0;i<nexp;i++)
    {
        maxind = 0;
        for(int j=1;j<nsamp_z;j++)
        {
            if(p_lz[i][j]>p_lz[i][maxind])
                maxind = j;
        }

        for(int j=0;j<nsamp_z;j++)
        {
            p_lz[i][j] = 0;
        }
        p_lz[i][maxind] = 1.0;
    }

    float sum;
    for(int i=0;i<nexp;i++)
    {
        sum = 0;
        for(int j=0;j<nsamp_z;j++)
        {
            sum += p_lz[i][j];
        }

        for(int j=0;j<nsamp_z;j++)
        {
            p_lz[i][j] /= sum;
        }
    }
    //end of normalization section

    int uv=Ly;
    int lh=Lx;
    int dv=nblv+Ly-1;
    int rh=nblh+Lx-1;

    cnt = 0;

    //Calculating E{x|y} equ (8) of Portilla's paper
    for(int j=lh;j<=rh;j++)
    {
        for(int i=uv;i<=dv;i++)
        {
            sum = 0;
            for(int k=0;k<nsamp_z;k++)
            {
                sum += mu_x[cnt][k] * p_lz[cnt][k];
            }
            y[i][j] = sum;
            cnt++;
        }
    }

    for(int i=0;i<nexp;i++)
    {
        delete[] p_lz[i];
        delete[] mu_x[i];
    }


    delete[] p_lz;
    delete[] mu_x;
}

