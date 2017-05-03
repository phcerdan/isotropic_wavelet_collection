/**
* @file steerable_pyramid.cpp
* @brief Source code for Heeger & Bergen texture synthesis.
*
* @version 1.0
* @author Thibaud BRIAND & Jonathan VACHER ; <thibaud.briand@ens-cachan.fr> ; <jvacher@ens-cachan.fr>
*
*
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


#include "steerable_pyramid.h"
#include "utilities.h"

void buildFullSFpyr2(const vector< vector< float > > img, vector< vector< vector <float > > >& pyr, int Nsc, int Nor, int Ny, int Nx)
{

    float *imgArr = new float[Nx*Ny];
    Mat2Array(img, imgArr);


    float *list_sample[2+Nsc*Nor];
    float *list_filters_sample[2+Nsc*Nor];
    float *list_downsample_filters_sample[1+Nsc];
    size_t *size_list_sample_vector = (size_t *) malloc(2*(2+Nsc*Nor)*sizeof(size_t));
    size_t *size_list_downsample_sample_vector = (size_t *) malloc(2*(1+Nsc)*sizeof(size_t));

    size_t *size_list_sample[2+Nsc*Nor];
    for(int k=0;k<2+Nsc*Nor;k++)
        size_list_sample[k]=size_list_sample_vector+2*k;
    size_t *size_list_downsample_sample[1+Nsc];
	for(int k=0;k<1+Nsc;k++)
        size_list_downsample_sample[k]=size_list_downsample_sample_vector+2*k;

    size_filters(size_list_sample, size_list_downsample_sample, Nx, Ny, Nor, Nsc);

      /* allocate memory */
    for(int i=0; i<2+Nsc*Nor;i++)
    {
        list_sample[i]= (float*) malloc(size_list_sample[i][0]*size_list_sample[i][1]*sizeof(float));
        list_filters_sample[i] = (float *) malloc(size_list_sample[i][0]*size_list_sample[i][1]*sizeof(float));
    }

    for(int i=0; i<1+Nsc; i++)
    {
        list_downsample_filters_sample[i] = (float *) malloc(size_list_downsample_sample[i][0]*size_list_downsample_sample[i][1]*sizeof(float));
    }

    filters(list_filters_sample, list_downsample_filters_sample, size_list_sample, size_list_downsample_sample, Nx, Ny, Nor, Nsc);

    analysisHb(list_sample, imgArr, list_filters_sample, list_downsample_filters_sample, size_list_sample, size_list_downsample_sample, Nx, Ny, Nor, Nsc);

    pyr.resize(Nor*Nsc+2);
    for(int sb=0;sb<Nor*Nsc+2;sb++)
    {
        pyr[sb].resize(size_list_sample[sb][1]);
        for(int i=0;i<pyr[sb].size();i++)
        {
            pyr[sb][i].resize(size_list_sample[sb][0]);
            //int p = pyr[sb][i].size();
            for(int j=0;j<pyr[sb][i].size();j++)
            {
                pyr[sb][i][j] = list_sample[sb][i*size_list_sample[sb][0]+j];
            }
        }
    }

    delete[] imgArr;
    free(size_list_sample_vector);
    free(size_list_downsample_sample_vector);

    for(int i=0; i<2+Nsc*Nor;i++)
    {
        free(list_sample[i]);
        free(list_filters_sample[i]);
    }

    for(int i=0; i<1+Nsc; i++)
    {
        free(list_downsample_filters_sample[i]);
    }
    //free(size_list_sample);
    //free(list_sample);
    //free(list_downsample_filters_sample);
    //free(list_filters_sample);
}

void reconFullSFpyr2(vector< vector< float > >& img, const vector< vector< vector <float > > > pyr, int Nsc, int Nor, int Ny, int Nx)
{
    float *imgArr = new float[Nx*Ny];

    float *list_sample[2+Nsc*Nor];
    float *list_filters_sample[2+Nsc*Nor];
    float *list_downsample_filters_sample[1+Nsc];
    size_t *size_list_sample_vector = (size_t *) malloc(2*(2+Nsc*Nor)*sizeof(size_t));
    size_t *size_list_downsample_sample_vector = (size_t *) malloc(2*(1+Nsc)*sizeof(size_t));

    size_t *size_list_sample[2+Nsc*Nor];
    for(int k=0;k<2+Nsc*Nor;k++)
        size_list_sample[k]=size_list_sample_vector+2*k;
    size_t *size_list_downsample_sample[1+Nsc];
	for(int k=0;k<1+Nsc;k++)
        size_list_downsample_sample[k]=size_list_downsample_sample_vector+2*k;

    size_filters(size_list_sample, size_list_downsample_sample, Nx, Ny, Nor, Nsc);

      /* allocate memory */
    for(int i=0; i<2+Nsc*Nor;i++)
    {
        list_sample[i]= (float*) malloc(size_list_sample[i][0]*size_list_sample[i][1]*sizeof(float));
        list_filters_sample[i] = (float *) malloc(size_list_sample[i][0]*size_list_sample[i][1]*sizeof(float));
    }

    for(int i=0; i<1+Nsc; i++)
    {
        list_downsample_filters_sample[i] = (float *) malloc(size_list_downsample_sample[i][0]*size_list_downsample_sample[i][1]*sizeof(float));
    }

    filters(list_filters_sample, list_downsample_filters_sample, size_list_sample, size_list_downsample_sample, Nx, Ny, Nor, Nsc);

    for(int sb=0;sb<Nor*Nsc+2;sb++)
    {
        for(int i=0;i<pyr[sb].size();i++)
        {
            for(int j=0;j<pyr[sb][i].size();j++)
            {
                list_sample[sb][i*size_list_sample[sb][0]+j] = pyr[sb][i][j];
            }
        }
    }

    synthesisHb(imgArr, list_sample, list_filters_sample, list_downsample_filters_sample, size_list_sample, size_list_downsample_sample, Nx, Ny, Nor, Nsc);

    Array2Mat(imgArr, img);

    delete[] imgArr;

    free(size_list_sample_vector);
    free(size_list_downsample_sample_vector);

    for(int i=0; i<2+Nsc*Nor;i++)
    {
        free(list_sample[i]);
        free(list_filters_sample[i]);
    }

    for(int i=0; i<1+Nsc; i++)
    {
        free(list_downsample_filters_sample[i]);
    }

    //free(size_list_sample);
    //free(list_sample);
    //free(list_downsample_filters_sample);
    //free(list_filters_sample);
}

void size_filters(size_t **size_list, size_t **size_list_downsample, size_t nx, size_t ny ,int N_steer, int N_pyr)
{
    /* define the size of the first filters */
    size_list[0][0]= nx;
    size_list[0][1]= ny;
    size_list_downsample[0][0]=nx;
    size_list_downsample[0][1]=ny;

	/* define the size of the filters used during the loop*/
    for(int k=0;k<N_pyr;k++)
    {
      size_list_downsample[k+1][0]= (size_t)  nx/(1<<k);
      size_list_downsample[k+1][1]= (size_t)  ny/(1<<k);

      for(int j=0;j<N_steer;j++)
      {
	size_list[1+N_steer*k+j][0]= (size_t)  nx/(1<<k);
	size_list[1+N_steer*k+j][1]= (size_t)  ny/(1<<k);
      }
    }

	/* define the size of the last filter */
    size_list[1+N_steer*N_pyr][0]= (size_t) nx/(1<<(N_pyr-1));
    size_list[1+N_steer*N_pyr][1]= (size_t) ny/(1<<(N_pyr-1));
}

float compute_alpha(int N_steer)
{
  /* allocation */
  float log_fact_alpha1=0;
  float log_fact_alpha2 =0;
  float log_alpha;
  float alpha_K;

  /* compute the two sums */
  for(int k=2;k<N_steer;k++) log_fact_alpha1 += log(k);
  for(int k=2;k<2*N_steer-1;k++) log_fact_alpha2 += log(k);

  /* compute the log of alpha */
  log_alpha = (N_steer-1)*log(2)+ log_fact_alpha1 - 0.5*(log(N_steer)+log_fact_alpha2);

  /* compute alpha */
  alpha_K = exp(log_alpha);

  return(alpha_K);
}

void filters(float **list_filters, float **list_downsample_filters ,size_t *size_list[2], size_t *size_list_downsample[2],size_t nx,size_t ny ,int N_steer, int N_pyr)
{
  /* Fill the list of filters */
	/* Compute the first and last filters */
	  list_filters[0]=high_filters(nx,ny,2);
	  list_filters[1+N_steer*N_pyr]=low_filters(size_list[1+N_steer*N_pyr][0], size_list[1+N_steer*N_pyr][1], 1);
	  list_downsample_filters[0]=low_filters(nx,ny,2);

	/* compute the others filters */
	  for(int k=0;k<N_pyr;k++)
	  {
		list_downsample_filters[1+k]=low_filters(size_list_downsample[1+k][0],size_list_downsample[1+k][1],1);

		float *high_filters_loop;
		high_filters_loop = (float*) malloc( size_list[1+k*N_steer][0]*size_list[1+k*N_steer][1]*sizeof(float));
		high_filters_loop = high_filters(size_list[1+k*N_steer][0],size_list[1+k*N_steer][1],1);

		for(int j=0;j<N_steer;j++)
		{
		  float *steer_filters_loop;
		  steer_filters_loop = (float*) malloc( size_list[1+k*N_steer+j][0]*size_list[1+k*N_steer+j][1]*sizeof(float));
		  steer_filters_loop = steerable_filters(size_list[1+k*N_steer][0],size_list[1+k*N_steer][1],j,N_steer);

		  for(unsigned int l=0;l<size_list[1+k*N_steer][0]*size_list[1+k*N_steer][1];l++) list_filters[1+k*N_steer+j][l] = steer_filters_loop[l]*high_filters_loop[l];

		  /* free */
		  free(steer_filters_loop);
		}

		/* free */
         free(high_filters_loop);
      }
}

float *high_filters(size_t nx, size_t ny, float factor)
{
  /* memory allocation */
  float *data_out_high_pass = (float*) malloc(nx*ny*sizeof(float));

  /* polar definition of the filter */
  for(unsigned int j=0;j<nx;j++)
  {
    for(unsigned int k=0;k<ny;k++)
    {

      float x=(j<nx/2) ? j*2*M_PI/(nx):j*2*M_PI/(nx)-2*M_PI;
      float y=(k<ny/2) ? k*2*M_PI/(ny):k*2*M_PI/(ny)-2*M_PI;
      float r=hypot(x,y)/factor;

      if(r!=0) {data_out_high_pass[j+k*nx] = ((r>=M_PI/2) + (r>M_PI/4)*(r<M_PI/2)*cos(M_PI/2*(log(2*r/M_PI)/log(2))));}
      else {data_out_high_pass[0]=0;}
    }
  }

  return(data_out_high_pass);
  free(data_out_high_pass);
}

float *low_filters(size_t nx, size_t ny, float factor)
{
  /* memory allocation */
  float *data_out_low_pass = (float*) malloc(nx*ny*sizeof(float));

  /* polar definition of the filter */
  for(unsigned int j=0;j<nx;j++)
  {
    for(unsigned int k=0;k<ny;k++)
    {

      float x=(j<nx/2) ? j*2*M_PI/(nx):j*2*M_PI/(nx)-2*M_PI;
      float y=(k<ny/2) ? k*2*M_PI/(ny):k*2*M_PI/(ny)-2*M_PI;
      float r=hypot(x,y)/factor;

      if(r!=0)
      {data_out_low_pass[j+k*nx] = ((r<=M_PI/4) + (r>M_PI/4)*(r<M_PI/2)*cos(M_PI/2*(log(4*r/M_PI)/log(2))));}
        else {data_out_low_pass[0]=1;}
    }
  }
  return(data_out_low_pass);
  free(data_out_low_pass);
}

float *steerable_filters(size_t nx, size_t ny, int steer, int N_steer)
{
    float alpha = compute_alpha(N_steer);

  /* memory allocation */
  float *data_out_steer_pass = (float*) malloc(nx*ny*sizeof(float));

  /* polar definition of the filter */
  for(unsigned int j=0;j<nx;j++)
  {
    for(unsigned int k=0;k<ny;k++)
    {
        float x=(j<nx/2) ? j*2*M_PI/(nx):j*2*M_PI/(nx)-2*M_PI;
        float y=(k<ny/2) ? k*2*M_PI/(ny):k*2*M_PI/(ny)-2*M_PI;
	float theta=atan2(y,x);
	float factor=cos(theta-M_PI*steer/N_steer);
	float cosinus_theta=1;
	float sign=1;

	for( int p = 1 ; p<N_steer ; p++)
	{
	  cosinus_theta *= factor;
	  sign *= -1;
	}

	data_out_steer_pass[j+k*nx] = (float) (alpha*cosinus_theta*(fabs(fmod(theta+M_PI-M_PI*steer/N_steer,2*M_PI)-M_PI)<M_PI/2)
					  +alpha*sign*cosinus_theta*(fabs(fmod(theta+2*M_PI-M_PI*steer/N_steer,2*M_PI)-M_PI)<M_PI/2));
    }
  }

  return(data_out_steer_pass);
  free(data_out_steer_pass);
}

void analysisHb(float **list_out, float *data_in, float **list_filters, float **list_downsample_filters ,size_t *size_list[2], size_t *size_list_downsample[2],size_t nx,size_t ny ,int N_steer, int N_pyr)
{
    /* allocate memory */
      size_t sx=0;
      size_t sy=0;
      size_t tx,ty;
      fftwf_complex *fft_data_in = (fftwf_complex *) fftwf_malloc( nx*ny * sizeof(fftwf_complex));
      fftwf_complex *fft_data_in_high = (fftwf_complex *) fftwf_malloc( nx*ny * sizeof(fftwf_complex));
      fftwf_complex *fft_data_in_loop = (fftwf_complex *) fftwf_malloc( nx*ny * sizeof(fftwf_complex));

      for(int i=0;i<2+N_pyr*N_steer;i++) list_out[i] = (float*) malloc(nx*ny*sizeof(float));

    /* Start threaded fftw if FFTW_NTHREADS is defined */
      #ifdef FFTW_NTHREADS
      if( 0 == fftwf_init_threads() )
	     return(NULL);
      fftwf_plan_with_nthreads(FFTW_NTHREADS);
      #endif

    /* Compute the dft of the input */
      do_fft( fft_data_in , data_in , nx , ny );

    /* high component */
      pointwise_complexfloat_multiplication( fft_data_in_high , fft_data_in , list_filters[0] , nx*ny );
      do_ifft( fft_data_in_high , list_out[0] , nx , ny );
      fftwf_free(fft_data_in_high);

    /* first low component */
      pointwise_complexfloat_multiplication( fft_data_in_loop , fft_data_in , list_downsample_filters[0] , nx*ny );
      fftwf_free(fft_data_in);

    /* recursive loop  to compute the oriented bandpass images*/
      for(int i=0;i<N_pyr;i++)
      {

		for(int j=0;j<N_steer;j++)
		{
			/* image size */
			sx=size_list[1+i*N_steer+j][0];
			sy=size_list[1+i*N_steer+j][1];

			/* apply the filters */
			fftwf_complex *fft_data_in_loop_loop = (fftwf_complex *) fftwf_malloc( sx*sy* sizeof(fftwf_complex));
			pointwise_complexfloat_multiplication(fft_data_in_loop_loop,fft_data_in_loop, list_filters[1+i*N_steer+j], sx*sy);

			/* iDFT */
			do_ifft( fft_data_in_loop_loop , list_out[1+i*N_steer+j] , sx , sy );
			fftwf_free(fft_data_in_loop_loop);
		}

	/* DFT for the next level */
	   tx=size_list_downsample[i+1][0];
	   ty=size_list_downsample[i+1][1];
	   pointwise_complexfloat_multiplication(fft_data_in_loop,fft_data_in_loop, list_downsample_filters[1+i], tx*ty);

	/* downsample (except for the first iteration) */
	   if(i<N_pyr-1)
	   {
	      fftwf_complex *fft_data_in_loop_downsampled = (fftwf_complex *) fftwf_malloc(tx*ty/4*sizeof( fftwf_complex));

	      for(unsigned int l=0;l< (unsigned int) tx/2;l++)
	      {
			for(unsigned int r=0;r<(unsigned int) ty/2;r++)
			{
				for(int d=0;d<2;d++)
				{
					if((l<tx/4) && (r<ty/4)) fft_data_in_loop_downsampled[l+r*tx/2][d] = fft_data_in_loop[l+r*tx][d];
					else if((l<tx/4) && (r>ty/4-1)) fft_data_in_loop_downsampled[l+r*tx/2][d] = fft_data_in_loop[l+(r+ty/2)*tx][d];
					else if((l>tx/4-1) && (r<ty/4)) fft_data_in_loop_downsampled[l+r*tx/2][d] = fft_data_in_loop[l+tx/2+r*tx][d];
					else fft_data_in_loop_downsampled[l+r*tx/2][d] = fft_data_in_loop[l+tx/2+(r+ty/2)*tx][d];
				}
			}
	      }

	      for(unsigned int k=0;k<tx*ty/4;k++)
	      {
			for(int d=0;d<2;d++)  fft_data_in_loop[k][d] = fft_data_in_loop_downsampled[k][d];
	      }

	      /* free memory */
	      fftwf_free(fft_data_in_loop_downsampled);

		}
      }

    /* Compute the last low component */
       do_ifft( fft_data_in_loop , list_out[1+N_pyr*N_steer] , sx , sy );

    /* free memory */
       fftwf_free(fft_data_in_loop);
}

void synthesisHb( float *data_out, float **list_in, float **list_filters, float **list_upsample_filters ,size_t *size_list[2], size_t *size_list_upsample[2],size_t nx,size_t ny ,int N_steer, int N_pyr)
{
	/* allocate memory */
       size_t sx,sy; /*number of row and columns*/
       fftwf_complex *fft_low_down;
       fftwf_complex *fft_out = (fftwf_complex *) fftwf_malloc( nx*ny * sizeof(fftwf_complex));

	/* Start threaded fftw if FFTW_NTHREADS is defined */
       #ifdef FFTW_NTHREADS
       if( 0 == fftwf_init_threads() )
		return(NULL);
       fftwf_plan_with_nthreads(FFTW_NTHREADS);
       #endif

    /* Compute the DFT of the input */
      sx=size_list[1+N_pyr*N_steer][0];
      sy=size_list[1+N_pyr*N_steer][1];
      fftwf_complex *fft_list_in = (fftwf_complex *) fftwf_malloc(sx*sy*sizeof(fftwf_complex));
      //fftwf_complex *fft_steer   = (fftwf_complex *) fftwf_malloc(sx*sy*sizeof(fftwf_complex));
      do_fft( fft_list_in , list_in[1+N_pyr*N_steer] , sx , sy );

    /* First step of inversion: low pass */
      fft_low_down = (fftwf_complex *) fftwf_malloc(  sx*sy * sizeof(fftwf_complex));
      pointwise_complexfloat_multiplication( fft_low_down , fft_list_in , list_filters[1+N_pyr*N_steer] , sx*sy);
      fftwf_free(fft_list_in);

    /* Second step of inversion: steer pass */
      for(int j=0;j<N_steer;j++)
      {
		/* memory allocation */
		   fftwf_complex *fft_list_in = (fftwf_complex *) fftwf_malloc(sx*sy*sizeof(fftwf_complex));
		   fftwf_complex *fft_steer   = (fftwf_complex *) fftwf_malloc(sx*sy*sizeof(fftwf_complex));

	    do_fft( fft_list_in , list_in[N_pyr*N_steer-j] , sx , sy );

//cout<<fft_list_in[0][0]<<endl;
	    pointwise_complexfloat_multiplication(fft_steer, fft_list_in, list_filters[N_pyr*N_steer-j], sx*sy);

	    for(unsigned int i=0;i<sx*sy;i++) {for(int d=0;d<2;d++) fft_low_down[i][d]+=fft_steer[i][d];}

	    /* free memory */
	       fftwf_free(fft_steer);
	       fftwf_free(fft_list_in);
      }
//fftwf_free(fft_steer);
//	       fftwf_free(fft_list_in);
    /* Loop for inversion */
	for(int i=1;i<N_pyr;i++)
	{
	  /* define size */
	  size_t tx=size_list_upsample[N_pyr-i][0];
	  size_t ty=size_list_upsample[N_pyr-i][1];

	  /* Upsampling */
	     fftwf_complex *fft_low_up =  (fftwf_complex *) fftwf_malloc(tx*ty*sizeof(fftwf_complex));

	  for(unsigned int r=0;r<(unsigned int) ty;r++)
	  {
	    for(unsigned int l=0;l<(unsigned int) tx;l++)
	    {
	      for(int d=0;d<2;d++)
	      {
			if ((l<tx/4) && (r<ty/4))
			  fft_low_up[l+r*tx][d] = fft_low_down[l+r*tx/2][d];
			else if((l<tx/4) && (r>3*ty/4-1))
			  fft_low_up[l+r*tx][d] = fft_low_down[l+(r-ty/2)*tx/2][d];
			else if((l>3*tx/4-1) && (r<ty/4))
			  fft_low_up[l+r*tx][d] = fft_low_down[l-tx/2+r*tx/2][d];
			else if((l>3*tx/4-1) && (r>3*ty/4-1))
			  fft_low_up[l+r*tx][d] = fft_low_down[l-tx/2+(r-ty/2)*tx/2][d];
			else
			  fft_low_up[l+r*tx][d]=0;
	      }
	    }
	  }

	  /* free values from 'fft_low_down' */
	     fftwf_free(fft_low_down);

	  /* First computation: lowpass */
	     pointwise_complexfloat_multiplication( fft_low_up , fft_low_up , list_upsample_filters[N_pyr-i] , tx*ty );

	  /* Computation for steerpass */
	     for(int j=0;j<N_steer;j++)
	     {
		   /* memory allocation */
	          fftwf_complex *fft_list_in = (fftwf_complex *) fftwf_malloc(tx*ty*sizeof(fftwf_complex));
	          fftwf_complex *fft_steer   = (fftwf_complex *) fftwf_malloc(tx*ty*sizeof(fftwf_complex));

		   /* apply the filters and add */
	          do_fft(fft_list_in, list_in[(N_pyr-i)*N_steer-j], tx, ty);
	          pointwise_complexfloat_multiplication(fft_steer, fft_list_in, list_filters[(N_pyr-i)*N_steer-j], tx*ty);

	          for(unsigned int k=0;k<tx*ty;k++) { for(int d=0;d<2;d++) fft_low_up[k][d]+=fft_steer[k][d];}

	  	   /* free memory */
	  	      fftwf_free(fft_list_in);
	  	      fftwf_free(fft_steer);
	     }

	  /* update of 'fft_low_down' */
	     fft_low_down = (fftwf_complex *) fftwf_malloc(  tx*ty * sizeof(fftwf_complex));
	     for(unsigned int k=0;k<tx*ty;k++)
	        for(int d=0;d<2;d++)
	      	   fft_low_down[k][d]=fft_low_up[k][d];

	  /* free memory */
	     fftwf_free(fft_low_up);
	}

	/* last step : high pass and low pass */
       /* low pass */
	      pointwise_complexfloat_multiplication(fft_low_down, fft_low_down, list_upsample_filters[0], nx*ny);

       /* high pass */
          fftwf_complex *fft_high =  (fftwf_complex *) fftwf_malloc(nx*ny*sizeof(fftwf_complex));
          fftwf_complex *fft_list_in_up =  (fftwf_complex *) fftwf_malloc(nx*ny*sizeof(fftwf_complex));

          do_fft(fft_list_in_up, list_in[0], nx, ny);
          pointwise_complexfloat_multiplication(fft_high, fft_list_in_up, list_filters[0], nx*ny);

    for(unsigned int k=0;k<nx*ny;k++) {for(int d=0;d<2;d++) fft_out[k][d]=fft_low_down[k][d]+fft_high[k][d];}

	/* final iDFT */
    do_ifft(fft_out, data_out, nx, ny);

    /* free memory */
       fftwf_free(fft_out);
       fftwf_free(fft_high);
       fftwf_free(fft_low_down);
       fftwf_free(fft_list_in_up);
}

fftwf_complex *pointwise_complexfloat_multiplication(fftwf_complex *comp_out,fftwf_complex *comp_in, float *float_in, size_t N)
{
	fftwf_complex *ptr_comp_in, *ptr_comp_end, *ptr_comp_out;
	float *ptr_float_in;

	/* check allocaton */
	if (NULL == comp_in || NULL == float_in)
		return(NULL);

	ptr_comp_in = comp_in;
	ptr_comp_out = comp_out;
	ptr_float_in = float_in;
	ptr_comp_end = ptr_comp_in + N;
	while( ptr_comp_in < ptr_comp_end )
	{
		(*ptr_comp_out)[0] = (*ptr_float_in)*(*ptr_comp_in)[0];
		(*ptr_comp_out)[1] = (*ptr_float_in)*(*ptr_comp_in)[1];
		ptr_comp_in++;
		ptr_float_in++;
		ptr_comp_out++;
	}
	return(comp_out);
}

void fft2(vector< vector< float > >& fft_coef, const vector< vector< float > > data)
{
    int Ny = data.size();
    int Nx = data[0].size();

    fftwf_complex *fft_data_in = (fftwf_complex *) fftwf_malloc( Nx*Ny * sizeof(fftwf_complex));
    float *dataArr = new float[Nx*Ny];
    Mat2Array(data, dataArr);

    do_fft(fft_data_in, dataArr, Nx , Ny);

    fft_coef.resize(Ny);
    for(int i=0;i<Ny;i++)
    {
        fft_coef[i].resize(Nx);
        for(int j=0;j<Nx;j++)
        {
            fft_coef[i][j] = fft_data_in[i*Nx+j][0];
        }
    }

    delete[] dataArr;
}

void ifft2(vector< vector< float > >& data, const vector< vector< float > > fft_coef)
{
    int Ny = fft_coef.size();
    int Nx = fft_coef[0].size();

    fftwf_complex *fft_in = (fftwf_complex*) fftwf_malloc(Nx*Ny * sizeof(fftwf_complex));

    /* Real --> complex */
    for(int i=0;i<Ny;i++)
    {
        for(int j=0;j<Nx;j++)
        {
            fft_in[i*Nx+j][0]=fft_coef[i][j];
            fft_in[i*Nx+j][1]=0;
        }
    }

    float *dataArr = new float[Nx*Ny];

    do_ifft(fft_in, dataArr, Nx , Ny);
    fftwf_free(fft_in);

    Array2Mat(dataArr, data);

    delete[] dataArr;
}

void fftshift(vector< vector< float > >& mat)
{
    int Ny = mat.size();
    int Nx = mat[0].size();

    int Midy = ceil(Ny/2);
    int Midx = ceil(Nx/2);

    vector< vector< float > > matTemp(mat);
//mat[0][0]=0;
    //first quadrant
    for(int i=0;i<Ny-Midy;i++)
    {
        for(int j=0;j<Nx-Midx;j++)
        {
            mat[i][j] = matTemp[i+Midy][j+Midx];
        }
    }

    //third quadrant
    for(int i=Ny-1;i>Ny-Midy-1;i--)
    {
        for(int j=Nx-1;j>Nx-Midx-1;j--)
        {
            mat[i][j] = matTemp[i-Ny+Midy][j-Nx+Midx];
        }
    }

    //second quadrant
    for(int i=0;i<Ny-Midy;i++)
    {
        for(int j=Nx-1;j>Nx-Midx-1;j--)
        {
            mat[i][j] = matTemp[i+Midy][j-Nx+Midx];
        }
    }

    //forth quadrant
    for(int i=Ny-1;i>Ny-Midy-1;i--)
    {
        for(int j=0;j<Nx-Midx;j++)
        {
            mat[i][j] = matTemp[i-Ny+Midy][j+Midx];
        }
    }

    clearMatrix(matTemp);
}

void do_fft(fftwf_complex *fft_out, float *data_in, size_t nx , size_t ny)
{
  /* memory allocation */
  fftwf_plan plan_r2c;
  fftwf_complex *complex_data_in = (fftwf_complex*) fftwf_malloc(nx*ny * sizeof(fftwf_complex));

  /* Real --> complex */
  for(unsigned int i=0;i<nx*ny;i++)
  {
    complex_data_in[i][0]=data_in[i];
    complex_data_in[i][1]=0;
  }

  /* compute the DFT */
  plan_r2c = fftwf_plan_dft_2d((int) ny ,(int) nx , complex_data_in, fft_out , FFTW_FORWARD, FFTW_ESTIMATE);
  fftwf_execute(plan_r2c);

  /* free */
  fftwf_destroy_plan(plan_r2c);
  fftwf_free(complex_data_in);
  fftwf_cleanup();
}

void do_ifft(fftwf_complex *fft_in, float *data_out, size_t nx , size_t ny)
{
  /* memory allocation */
  fftwf_plan plan_c2r;
  fftwf_complex *complex_data_out = (fftwf_complex *) fftwf_malloc ( sizeof ( fftwf_complex ) * nx * ny );

  /* compute the iDFT */
  plan_c2r = fftwf_plan_dft_2d ( (int) ny, (int) nx , fft_in , complex_data_out , FFTW_BACKWARD, FFTW_ESTIMATE );
  fftwf_execute(plan_c2r);

  /* complex --> real */
  for(unsigned int i=0;i<nx*ny;i++)
    {
      data_out[i]=complex_data_out[i][0]/(nx*ny);
    }

  /* free */
  fftwf_free(complex_data_out);
  fftwf_destroy_plan(plan_c2r);
  fftwf_cleanup();
}

