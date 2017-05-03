How to use the code:

This package is self-contained, to implement multi-Wiener SURE-LET deconvolution algorithm. 
The main algorithm file is: SURE_LET_Deconvolution.m


After changing MATLAB into the current directory, you may start to use it in two ways:
[1] run demo.m file, you can change the experiment setting (images, blurs and noise levels);
[2] run SURE_LET_GUI for GUI demo.


Key experimental settings:
[1] you can copy your test image into this folder, change the image_name in demo.m;
[2] you can add new blur types in Image_blurring.m;
[3] you can change noise level in demo.m, where nsigma refers to noise standard deviation. 


Reference
[1] F. Xue, F. Luisier, T. Blu, "SURE-LET image deconvolution using multiple Wiener filters," in IEEE International Conference on Image Processing, Orlando, Florida, USA, OCt. 2012, In press.
[2] F. Xue, F. Luisier, T. Blu, "Multi-Wiener SURE-LET Deconvolution," IEEE Transactions on Image Processing, to appear.

