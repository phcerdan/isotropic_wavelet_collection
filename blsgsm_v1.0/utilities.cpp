/**
 * @file utilities.cpp
 * @brief A set of general functions
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

#include "utilities.h"
#include "mt19937ar.h"
#include "Matrix.h"

//extending noise signal by zero padding
//----> delta: input and output signal
//----> Npy and Npx: new size
void resizedelta(vector< vector< float > >& delta, int Npy, int Npx)
{
    int Ndy = delta.size();
    int Ndx = delta[0].size();

    vector< vector< float > > aux(delta);

    delta.resize(Npy);
    for(int i=0;i<Npy;i++)
    {
        delta[i].resize(Npx);
        for(int j=0;j<Ndx;j++)
        {
            delta[i][j] = 0;
        }
    }

    if(Ndy<=Npy && Ndx<=Npx)
    {
        for(int i=Npy/2-Ndy/2;i<Npy/2+Ndy/2;i++)
            for(int j=Npx/2-Ndx/2;j<Npx/2+Ndx/2;j++)
                delta[i][j] = aux[i-Npy/2+Ndy/2][j-Npx/2+Ndx/2];
    }
    else if(Ndy>Npy && Ndx>Npx)
    {
        for(int i=Npy/2-Ndy/2;i<Npy/2+Ndy/2;i++)
            for(int j=Npx/2-Ndx/2;j<Npx/2+Ndx/2;j++)
                delta[i-Npy/2+Ndy/2][j-Npx/2+Ndx/2] = aux[i][j];
    }
    else if(Ndy<=Npy && Ndx>Npx)
    {
        for(int i=Npy/2-Ndy/2;i<Npy/2+Ndy/2;i++)
            for(int j=Npx/2-Ndx/2;j<Npx/2+Ndx/2;j++)
                delta[i][j-Npx/2+Ndx/2] = aux[i-Npy/2+Ndy/2][j];
    }
    else if(Ndy>Npy && Ndx<=Npx)
    {
        for(int i=Npy/2-Ndy/2;i<Npy/2+Ndy/2;i++)
            for(int j=Npx/2-Ndx/2;j<Npx/2+Ndx/2;j++)
                delta[i-Npy/2+Ndy/2][j] = aux[i][j-Npx/2+Ndx/2];
    }


}

//extending image sizes by mirror reflected rows and cols
//------>img: input image
//------>Bpy and Bpx: new size
//------>flag: 1: extending only from down and right, 0: extending from 4 side
void bound_extension(vector< vector< float > >& img, int Bpy, int Bpx, int flag)
{
    int Ny = img.size();
    int Nx = img[0].size();

    if(flag)
    {
        img.resize(Ny+Bpy);
        for(int i=0;i<Ny+Bpy;i++)
        {
            img[i].resize(Nx+Bpx);
        }

        //down stripe
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Nx+Bpx;j++)
            {
                img[Ny+i][j] = img[Ny-i-1][j];
            }
        }

        //right stripe
        for(int i=0;i<Ny+Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[i][Nx+j] = img[i][Nx-j-1];
            }
        }

        //down-right corner
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[Ny+i][Nx+j] = img[Ny-i-1][Nx-j-1];
            }
        }

    }
    else
    {
        img.resize(Ny+2*Bpy);
        for(int i=0;i<Ny+2*Bpy;i++)
        {
            img[i].resize(Nx+2*Bpx);
        }

        for(int i=Ny+Bpy-1;i>Bpy-1;i--)
        {
            for(int j=Nx+Bpx-1;j>Bpx-1;j--)
            {
                img[i][j] = img[i-Bpy][j-Bpx];
            }
        }

        //up stripe
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Nx+2*Bpx;j++)
            {
                img[i][j] = img[2*Bpy-1-i][j];
            }
        }

        //left stripe
        for(int i=0;i<Ny+2*Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[i][j] = img[i][2*Bpx-1-j];
            }
        }

        //down stripe
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Nx+2*Bpx;j++)
            {
                img[Ny+Bpy+i][j] = img[Ny+Bpy-i-1][j];
            }
        }

        //right stripe
        for(int i=0;i<Ny+2*Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[i][Nx+Bpx+j] = img[i][Nx+Bpx-j-1];
            }
        }

        //up-left corner
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[i][j] = img[2*Bpy-1-i][2*Bpx-1-j];
            }
        }

        //up-right corner
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[i][Nx+Bpx+j] = img[2*Bpy-1-i][Nx+Bpx-j-1];
            }
        }

        //down-left corner
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[Ny+Bpy+i][j] = img[Ny+Bpy-i-1][2*Bpx-1-j];
            }
        }

        //down-right corner
        for(int i=0;i<Bpy;i++)
        {
            for(int j=0;j<Bpx;j++)
            {
                img[Ny+Bpy+i][Nx+Bpx+j] = img[Ny+Bpy-i-1][Nx+Bpx-j-1];
            }
        }
    }
}

//zooming by a factor of 2 using nearest neighborhood method
void expand(vector< vector< float > >& mat)
{
    int my = mat.size();
    int mx = mat[0].size();

    my *= 2;
    mx *= 2;

    vector< vector< float > > aux;
    aux.resize(my);
    for(int i=0;i<my;i++)
    {
        aux[i].resize(mx);
        for(int j=0;j<mx;j++)
        {
            aux[i][j] = 0;
        }
    }

    for(int i=0;i<my;i=i+2)
    {
        for(int j=0;j<mx;j=j+2)
        {
            aux[i][j] = mat[i/2][j/2];
        }
    }

    for(int i=1;i<my;i=i+2)
    {
        for(int j=0;j<mx;j++)
        {
            aux[i][j] = aux[i-1][j];
        }
    }

    for(int i=0;i<my;i++)
    {
        for(int j=1;j<mx;j=j+2)
        {
            aux[i][j] = aux[i][j-1];
        }
    }

    mat = aux;
}


//3 point DCT transform for color decorrelation
//------->img: input color image in rgb format and also the output decorrelated one
//------->width and height: image size
//------->flag: 1:forward 0:backward trasform
void ColorTransform(vector< vector< vector< float > > >& img, int width, int height, int flag)
{
	//some temporary variables
	float tempvar1, tempvar2, tempvar3;
	int i, j;

	// forward transform
	if ( flag == 1 )
	{
        //#pragma omp parallel for
		for (i = 0; i < height; i ++)
			for (j = 0; j < width; j ++)
			{
				tempvar1 =
				(  img[0][i][j] * DCTbasis3x3[0][0]
				 + img[1][i][j] * DCTbasis3x3[0][1]
				 + img[2][i][j] * DCTbasis3x3[0][2] );

				tempvar2 =
				(  img[0][i][j] * DCTbasis3x3[1][0]
				 + img[1][i][j] * DCTbasis3x3[1][1]
				 + img[2][i][j] * DCTbasis3x3[1][2] );

				tempvar3 =
				(  img[0][i][j] * DCTbasis3x3[2][0]
				 + img[1][i][j] * DCTbasis3x3[2][1]
				 + img[2][i][j] * DCTbasis3x3[2][2] );

				 img[0][i][j] = tempvar1;
				 img[1][i][j] = tempvar2;
				 img[2][i][j] = tempvar3;
			}

	}
	// reverse transform
	else if (flag == -1)
	{
        //#pragma omp parallel for
		for (i = 0; i < height; i ++)
			for (j = 0; j < width; j ++)
			{
				tempvar1 =
				(  img[0][i][j] * DCTbasis3x3[0][0]
				 + img[1][i][j] * DCTbasis3x3[1][0]
				 + img[2][i][j] * DCTbasis3x3[2][0] );

				tempvar2 =
				(  img[0][i][j] * DCTbasis3x3[0][1]
				 + img[1][i][j] * DCTbasis3x3[1][1]
				 + img[2][i][j] * DCTbasis3x3[2][1] );

				tempvar3 =
				(  img[0][i][j] * DCTbasis3x3[0][2]
				 + img[1][i][j] * DCTbasis3x3[1][2]
				 + img[2][i][j] * DCTbasis3x3[2][2] );

				 img[0][i][j] = tempvar1;
				 img[1][i][j] = tempvar2;
				 img[2][i][j] = tempvar3;
			}
	}
	else
	{
		printf ("Error: ColorTransform flag should be 1 (forward) or -1 (inverse). \n");
		//exit (1);
	}
}

//applying point wise mean sqr on input matrix
float meansqr(const vector< vector< float > > mat)
{
    float m = 0;
    int Ny = mat.size();
    int Nx = mat[0].size();

    for(int i=0;i<Ny;i++)
        for(int j=0;j<Nx;j++)
            m += mat[i][j]*mat[i][j];

    m /= Nx*Ny;

    return m;
}

//reshap the matrix into array format
void Mat2Array(const vector< vector< float > > mat, float* arr)
{
    int Ny = mat.size();
    int Nx = mat[0].size();

    for(int i=0;i<Ny ;i++)
    {
        for(int j=0;j<Nx;j++)
        {
            arr[i*Nx+j] = mat[i][j];
        }
    }
}

//reshape the array into matrix format
void Array2Mat(const float* arr, vector< vector< float > >& mat)
{
    int Ny = mat.size();
    int Nx = mat[0].size();

    for(int i=0;i<Ny ;i++)
    {
        for(int j=0;j<Nx;j++)
        {
            mat[i][j] = arr[i*Nx+j];
        }
    }
}

// Add Gaussian iid white noise with zero mean
//------>ipixels: input image and also the output noisy one
//------>size: size of image interms of height*width*channel
//------>sigma: noise standard deviation
void addnoise(float* ipixels, int size, float sigma)
{
	mt_init_genrand((unsigned long int) time(NULL));
	//#pragma omp parallel for
	for (int i = 0; i < size; i++)
			ipixels[i] += (float) ((double) sigma
						* sqrt(-2.0 * log(mt_genrand_res53()))
						* cos(2.0 * M_PI * mt_genrand_res53()));

}

//Matrix multiplication C = A * B
//A: N1 by N2
//B: N2 by N3
//C: N1 by N3
void multiply(float** A, float** B, float **C, int N1, int N2, int N3)
{
    for(int i=0;i<N1;i++)
    {
        for(int j=0;j<N3;j++)
        {
            C[i][j] = 0;
            for(int k=0;k<N2;k++)
            {
                C[i][j] += A[i][k] * B[k][j];
            }
        }
    }
}

//matrix transpose
void transp(float** Q, float** QT, int N)
{
    for(int i=0; i<N; i++)
    {
        for(int j=0;j<N;j++)
        {
            QT[i][j] = Q[j][i];
        }
    }
}

//Calculating the covariance matrix
//-----> sub: a subband of image pyramid or noise pyramid
//-----> subp: its parent from coarser scale
//-----> C: covariance matrix
//-----> block: neighborhood size around each coefficient
//-----> prnt: using or not using parent information in neighborhood
void cov(vector< vector< float > > sub, vector< vector< float > > subp, float **C, int block[], int prnt)
{
    int nv = sub.size();
    int nh = sub[0].size();

    int Ly = (block[0]-1)/2;
    int Lx = (block[1]-1)/2;

    int nblv = nv-block[0]+1;
    int nblh = nh-block[1]+1;
    int nexp = nblv*nblh;

    int N = block[0]*block[1] + prnt; //number of elements participating in modeling a noiseless wavelet coefficient neighborhood

    int ind[N-prnt][2];
    int cnt = 0;
    for(int i=Ly;i>=-Ly;i--)
        for(int j=Lx;j>=-Lx;j--)
        {
            ind[cnt][0] = i;
            ind[cnt][1] = j;
            cnt++;
        }
    for(int i=Ly;i<nv-Ly;i++)
    {
        for(int j=Lx;j<nh-Lx;j++)
        {
            for(int n1=0;n1<N-prnt;n1++)
            {
                for(int n2=0;n2<N-prnt;n2++)
                {
                    C[n1][n2] += sub[i+ind[n1][0]][j+ind[n1][1]] * sub[i+ind[n2][0]][j+ind[n2][1]];
                }
                if(prnt)
                {
                    C[n1][N-1] += sub[i+ind[n1][0]][j+ind[n1][1]] * subp[i][j];
                }
            }
            if(prnt)
            {
                C[N-1][N-1] += subp[i][j] * subp[i][j];
            }
        }
    }

    for(int i=0;i<N;i++)
        for(int j=0;j<N;j++)
        {
            C[i][j] /= nexp;
        }

}

// Calculating symmetric sqrt root means mat=S * trans(S)
//------> mat: input matrix
//------> S: symmetrix square root of it
//------> N: mat is N by N
void symroot(float **mat, float **S, int N)
{
    float *dd[N];
    float *aux1[N];

    for(int i=0;i<N;i++)
    {
        dd[i] = new float[N];
        aux1[i] = new float[N];
    }

    eigen(mat, S, dd, N);

    for(int i=0;i<N;i++)
        for(int j=0;j<N;j++)
            dd[i][j] = sqrt(dd[i][j]);

    multiply(S, dd, aux1, N, N, N);

    for(int i=0;i<N;i++)
        for(int j=0;j<N;j++)
            S[i][j] = aux1[i][j];

    for(int i=0;i<N;i++)
    {
        delete[] dd[i];
    }
}

//Force a matrix to be positice semidefinite by eliminating its negative eigen values
void Make_SemiDefinite(float **mat, int N)
{
    float *Q[N];
    float *L[N];
    float *aux1[N];

    for(int i=0;i<N;i++)
    {
        Q[i] = new float[N];
        L[i] = new float[N];
        aux1[i] = new float[N];
    }

    eigen(mat, Q, L, N);

    float sd = 0, sdp = 0;
    for(int i=0;i<N;i++)
    {
        sd += L[i][i];
        sdp += L[i][i] * (L[i][i] > 0);
    }

    if(sdp == 0) sdp = 1;

    for(int i=0;i<N;i++)
    {
        if(L[i][i] < 0)
            L[i][i] = 0; //setting to zero negative eigenvalues
        else
            L[i][i] *= sd/sdp;
    }

    float *QT[N];

    for(int i=0;i<N;i++)
    {
        QT[i] = new float[N];
    }

    transp(Q, QT, N);

    multiply(Q, L, aux1, N, N, N);
    multiply(aux1, QT, mat, N, N, N);

}

//Boundary handling in denoising each subband
//------> y: input noisy subband
//------> p_lz: p(y|z)
//------> zi: sampled z values
//------> block: neighborhood size around each coeffienct at the same subband
//------> nsamp_z: number of z samples
//------> sx2: signal variance in subband (not including parent)
//------> sig2: noise variance in subband (not including parent)
void boundary_handling(vector< vector< float > >& y, float **p_lz, float *zi, int block[], int nsamp_z, float sx2, float sig2)
{
    int nv = y.size();
    int nh = y[0].size();

    int nblv = nv-block[0]+1;
    int nblh = nh-block[1]+1;
    int nexp = nblv*nblh;

    int Ly = (block[0]-1)/2;
    int Lx = (block[1]-1)/2;


    float *z = new float[nexp];

    for(int i=0;i<nexp;i++)
    {
        int ind = 0;

        for(int j=1;j<nsamp_z;j++)
        {
            if(p_lz[i][j] > p_lz[i][ind])
            {
                ind = j;
            }
        }
        z[i] = zi[ind];
    }

    int uv=Ly;
    int lh=Lx;
    int dv=nblv+Ly-1;
    int rh=nblh+Lx-1;

    float **zM = new float*[nv];

    for(int i=0;i<nv;i++)
    {
        zM[i] = new float[nh];
    }


    int cnt = 0;
    for(int j=lh;j<=rh;j++)
    {
        for(int i=uv;i<=dv;i++)
        {
            zM[i][j] = z[cnt++];
        }
    }

    delete[] z;

    for(int i=0;i<=uv;i++)
        for(int j=0;j<=lh;j++)
            zM[i][j] = zM[uv][lh];

    for(int i=0;i<=uv;i++)
        for(int j=rh;j<nh;j++)
            zM[i][j] = zM[uv][rh];

    for(int i=dv;i<nv;i++)
        for(int j=0;j<=lh;j++)
            zM[i][j] = zM[dv][lh];

    for(int i=dv;i<nv;i++)
        for(int j=rh;j<nh;j++)
            zM[i][j] = zM[dv][rh];

    for(int i=0;i<uv;i++)
        for(int j=lh+1;j<=rh-1;j++)
            zM[i][j] = zM[uv][j];

    for(int i=dv+1;i<nv;i++)
        for(int j=lh+1;j<=rh-1;j++)
            zM[i][j] = zM[dv][j];

    for(int i=uv+1;i<=dv-1;i++)
        for(int j=0;j<=lh-1;j++)
            zM[i][j] = zM[i][lh];

    for(int i=uv+1;i<=dv-1;i++)
        for(int j=rh+1;j<nh;j++)
            zM[i][j] = zM[i][rh];

    for(int i=0;i<nv;i++)
    {
        for(int j=0;j<nh;j++)
        {
            y[i][j] *= sx2*zM[i][j]/(sx2*zM[i][j]+sig2);
        }
    }

    //....................End: Boundary handling section...................

    for(int i=0;i<nv;i++)
    {
        delete[] zM[i];
    }
    delete[] zM;
}

//Print PSNR and RMSE in measures.txt file
//------> inputImg: input healthy image
void PrintMeasurements(const float *inputImg, const float *denoisedImg, int N)
{
    float diff = 0;
    for (int i=0; i<N; i++)
        diff += (inputImg[i] - denoisedImg[i]) * (inputImg[i] - denoisedImg[i]);

    float rmse = sqrt(diff / N);
    float psnr = 20.0f * log10f(255.0f / rmse);
    cout<<endl;
    cout<<"PSNR="<<psnr<<endl;
    cout<<"RMSE="<<rmse<<endl;

    ofstream fp("measures.txt", ios::out);
    fp << "BLS-GSM Measurements:" << endl;
    fp << "PSNR= " << psnr << endl;
    fp << "RMSE= " << rmse << endl;
    fp.close();
}

//releasing memory
void clearMatrix(vector< vector< float > >& mat)
{
    for(int i=0;i<mat.size();i++)
        mat[i].clear();
    mat.clear();
}

//releasing memory
void clearMatrix(vector< vector< vector< float > > >& mat)
{
    for(int i=0;i<mat.size();i++)
    {
        for(int j=0;j<mat[i].size();j++)
        {
            mat[i][j].clear();
        }
        mat[i].clear();
    }
    mat.clear();
}
