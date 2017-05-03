% BLS-GSM image denoising.

# ABOUT

* Author    : Boshra Rajaei <bo.rajaei@gmail.com>
* Copyright : (C) 2013 IPOL Image Processing On Line http://www.ipol.im/
* Licence   : GPL v3+, see GPLv3.txt

# OVERVIEW

This source code provides an implementation of the BLS-GSM image denoising.

# UNIX/LINUX/MAC USER GUIDE

The code is compilable on Unix/Linux and Mac OS. 

- Compilation. 
Automated compilation requires the make program.

- Library. 
This code requires the libpng library.

- Image format. 
Only the PNG format is supported. 
 
-------------------------------------------------------------------------
Usage:
1. Download the code package and extract it. Go to that directory. 

2. Compile the source code (on Unix/Linux/Mac OS). 
There are two ways to compile the code. 
(1) RECOMMENDED, with Open Multi-Processing multithread parallelization 
(http://openmp.org/). Roughly speaking, it accelerates the program using the 
multiple processors in the computer. Run
make OMP=1

OR
(2) If the complier does not support OpenMp, run 
make

3. Run blsgsm image denoising.
./blsgsm InputImage sigma OutputDenoisedImage OutputNoisyImage OutputDifferenceImg ComputePSNR(0 or 1)

Example, run
./blsgsm building1.png 10 denoised.png noisy.png diff.png 1

PSNR/RMSE: 34.62/4.74

# ABOUT THIS FILE

Copyright 2013 IPOL Image Processing On Line http://www.ipol.im/

Copying and distribution of this file, with or without modification,
are permitted in any medium without royalty provided the copyright
notice and this notice are preserved.  This file is offered as-is,
without any warranty.
