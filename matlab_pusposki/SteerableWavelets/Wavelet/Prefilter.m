function [Ap,res] = Prefilter(A,h)
%
% function [Ap,res] = Prefilter(A,h)
%
%
% prefilter.
%
%
% INPUTS
% ------
%
% A             image to process
%
% h             prefilter (function handle)
%
%
% OUTPUTS
% -------
%
% Ap            filtered image 
%
% res           residue
%
%
% REFERENCE
% ---------
% You are free to use this software for research purposes, but you should 
% not redistribute it without our consent.
%
% In addition, we expect you to include the following citations:
%
% Z. Puspoki and M. Unser, "Template-Free Wavelet-Based Detection of Local
% Symmetries", IEEE Transactions on Image Processing, vol. 24, no. 10, 
% pp. 3009-3018, October 2015
% M. Unser and N. Chenouard, "A Unifying Parametric Framework for 2D Steerable
% Wavelet Transforms", SIAM Journal on Imaging Sciences, vol. 6, no. 1, 
% pp. 102-135, 2013. 
%
%
% AUTHOR
% ------
%
% Z. Puspoki (zsuzsanna.puspoki@epfl.ch)
%
% Biomedical Imaging Group
% Ecole Polytechnique Federale de Lausanne (EPFL)


sA = size(A); 

wmax1 = pi;
wmax2 = pi;
dw1   = 2*pi/sA(1);
dw2   = 2*pi/sA(2);
w1    = -wmax1:dw1:(wmax1-dw1);
w2    = -wmax2:dw2:(wmax2-dw2);

[W1 W2] = ndgrid(w1,w2);

RHO     = sqrt(W1.^2+W2.^2);

Fscaling = h(RHO); 
Fresidue = sqrt(1- h(RHO).^2);

FA = fftshift(fftn(A));

Ap = ifftn(fftshift(Fscaling.*FA));
res = ifftn(fftshift(Fresidue.*FA));
