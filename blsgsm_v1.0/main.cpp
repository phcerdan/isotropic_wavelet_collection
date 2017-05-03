/**
 * @file main.cpp
 * @brief Main executable file
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

#include <omp.h>
#include "iostream"
#include <math.h>
#include <stdlib.h>
#include <vector>


#include "GSM.h"
#include "io_png.h"
#include "utilities.h"

using namespace std;

int main(int argc, char **argv)
{

    if(argc<7)
    {
        cout<<"Error! Input parameters are not complete!"<<endl;
        cout<<"Usage:"<<endl;
        cout<<"blsgsm InputImage sigma OutputDenoisedImage OutputNoisyImage OutputDifferenceImg ComputePSNR(0 or 1)"<<endl;
        return 1;
    }

    float *origin; //image holder
    size_t width, height, nchannel; //width and height of image and its number of components
    int size;

    //reading input image
    if((origin = io_png_read_f32(argv[1], &width, &height, &nchannel)) == NULL)
    {
        cout<<"Error! Unable to read input image"<<endl;
        return 1;
    }

    //for gray level images, nchannel = 1 and for color images nchannel = 3
    if(nchannel != 1 && nchannel != 3)
    {
        cout<<"Error! Input image should have 1 or 3 channels"<<endl;
        return 1;
    }

    size = height*width*nchannel;
    int Nsc = ceil(log10(min(width,height))/log10(2) - 4); //Number of scales in steerable pyramid
	int Nor = 4;//Number of orientaions in each scale of steerable pyramid
	float sig = atof(argv[2]); //noise standard deviation
	int blSize[] = {3,3}; //size of neighborhood around each coefficient in the same subband (should be odd)
	int parent = 1; //including or not including a coefficient from the next coarser scale but the same location and orientation in neighborhood
	int boundary = 1; //boundary mirror extension or not, to avoid boundary artifacts
	vector< vector <float > > PS; //power spectral density - here: flat or white noise

    //adding Gaussian noise to input image with zero mean and standard deviation equal to sig
    addnoise(origin, height*width*nchannel, sig);

    //writing noisy image
    if(io_png_write_f32(argv[4], origin, width, height, nchannel)!=0)
    {
        cout<<"Error! Unable to write noisy image"<<endl;
        return 1;
    }

    //image holder same as origin but using vector class
    vector< vector< vector <float > > > img;

    //copying origin into img
    img.resize(nchannel);
    for(int c=0;c<nchannel;c++)
    {
       img[c].resize(height);
       for(int i=0;i<height;i++)
       {
          img[c][i].resize(width);
          for(int j=0;j<width;j++)
          {
            img[c][i][j]=origin[c*height*width+i*width+j];
          }
       }
    }

    free(origin);

	if(nchannel==3)
	{
	    //For color images and to enhance overall performance color decorrelation is done using 3 point DCT transform
	    ColorTransform(img,width,height,1);
	}

    //initial PS and also for white noise set all values to one
	PS.resize(height);
	for(int i=0;i<height;i++)
	{
	    PS[i].resize(width);
	    for(int j=0;j<width;j++)
	    {
	        PS[i][j] = 1;
	    }
	}

    for(int nc=0;nc<nchannel;nc++)
	{
	    //apply BLS-GSM denoising algorithm to each channel, the resulting denoised image is returned in img
	    denoi_BLS_GSM(img[nc], sig, PS, blSize, parent, boundary, Nsc, Nor);
	}

	clearMatrix(PS);

    if(nchannel==3)
	{
	    //inverse color decorrelation
	    ColorTransform(img,width,height,-1);
	}

    //for writing purposed, again transfer image from vector class to pointer form
    float *denoised = new float[size];

	for(int c=0;c<nchannel;c++)
    {
        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                denoised[c*height*width+i*width+j]=img[c][i][j];
            }
        }
    }

    //writing output denoised image
    if(io_png_write_f32(argv[3], denoised, width, height, nchannel)!=0)
    {
        cout<<"Error! Unable to write output denoised image"<<endl;
        return 1;
    }

    //reading input image
    if((origin = io_png_read_f32(argv[1], &width, &height, &nchannel)) == NULL)
    {
        cout<<"Error! Unable to read input image"<<endl;
        return 1;
    }

    float *diffImg = new float[size];
    float tmp;

    for (int i = 0; i<size; i++)
    {
        tmp =  (origin[i] - denoised[i] + sig) * 255.0f / (2.0f * sig);
        diffImg[i] = (tmp < 0.0f ? 0.0f : (tmp > 255.0f ? 255.0f : tmp));
    }

    if(io_png_write_f32(argv[5], diffImg, width, height, nchannel)!=0)
    {
        cout<<"Error! Unable to write output difference image"<<endl;
        return 1;
    }

    if(atof(argv[6])==1)
        PrintMeasurements(origin, denoised, size);

    //release memory
    free(origin);
    delete[] denoised;
    delete[] diffImg;
    clearMatrix(img);


    return 0;
}

