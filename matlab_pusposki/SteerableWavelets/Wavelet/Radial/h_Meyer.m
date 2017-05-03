function hr = h_Meyer(r)
%
% function hr = h_Meyer(r)
%
%
% for Meyer wavelet.
%
%
% INPUTS
% ------
%
% r             Fourier radius vector
%
%
%
% OUTPUT
% ------
%
% hr            radial Fourier value at r
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

hr = (r<= (pi/2)).*(r> (pi/4)).*(sin((pi/2)*nu((4*r/pi)-1))) + (r <= pi).*(r > (pi/2)).*(cos((pi/2)*nu((2*r/pi)-1)));

function v = nu(t)
v = (t>0).*(t<1).*(t.^4 .*(35 - 84.*t + 70*t.^2 -20*t.^3));
