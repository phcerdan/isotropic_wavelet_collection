------------------------------------------------------------------
Generalized Riesz-wavelet toolbox
------------------------------------------------------------------

Copyright Ecole Polytechnique F?d?rale de Lausanne. 
All rights reserved.
This work should be used for nonprofit purposes only.

Author: Nicolas Chenouard

Contributors: Dimitri Van De Ville

Supervisor: Michael Unser

Website: http://bigwww.epfl.ch/demo/steerable-wavelets/

Version: 1.0; March 7 2012

------------------------------------------------------------------
Content
------------------------------------------------------------------

This toolbox contains Matlab routines for computing the forward and backward
generalized Riesz-wavelet transform of high order. We have included utilities for orientation
computation, coefficients steering, basic denoising, frame learning.

We provide a number of demonstration routines that may serve as tutorials on the use
of the toolbox:

* demo_Riesz2D
    2D Riesz-wavelet transform decomposition and reconstruction

* demo_monogenicAnalysis
    perform monogenic analysis and display estimated orientations and coherency maps

* demo_monogenicAnalysis_amplitudeEqualization
    reconstruct an image with equalized monogenic amplitude

* demo_optimalTemplateSteering
    steer Riesz-wavelet coefficients to maximize the response of a template of Riesz channels

* demo_basicDenoising
    perform basic denoising operations in the Riesz-wavelet domain

* demo_fromRieszToSimoncelli
    demonstrate the construction of a Simoncelli's pyramid from the Riesz-wavelet frame

* demo_generalizedRieszTransformLearning
    learn a generalized Riesz-wavelet frame using principal component analysis

------------------------------------------------------------------
References
------------------------------------------------------------------

Steerable Pyramids and Tight Wavelet Frames in L2(?d)
M. Unser, N. Chenouard, D. Van De Ville
IEEE Transactions on Image Processing, vol. 20, no. 10, pp. 2705-2721, October 2011. 

http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=5746534&isnumber=6019142
http://bigwww.epfl.ch/publications/unser1103.html

If you use this toolbox for reseach purpose, please include this citation in your
manuscripts.

The monogenic analysis that is performed relates to the following work:

Multiresolution Monogenic Signal Analysis Using the Riesz-Laplace Wavelet Transform
M. Unser, D. Sage, D. Van De Ville
IEEE Transactions on Image Processing, vol. 18, no. 11, pp. 2402-2418, November 2009. 

http://bigwww.epfl.ch/publications/unser0907.html

------------------------------------------------------------------
Feedback
------------------------------------------------------------------

If you have any comment, suggestion, or question, please do
contact  Nicolas Chenouard
nicolas #.# chenouard #@# gmail #.# com