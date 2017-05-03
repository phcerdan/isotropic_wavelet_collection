function w = simoncelliFunctionDilated(x, dilationFactor, k)
% SIMONCELLIFUNCTIONDILATED build the radial part of the
% Simoncelli wavelet function for an arbitrary dilation factor.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% X frequency plane
%
% DILATIONFACTOR dilation factor for the wavelet transform
%
% K scale of the wavelet function
%
% --------------------------------------------------------------------------
% Output arguments:
%
% W the wavelet function in Fourier domain for the specified fourier plane
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

w = zeros(size(x));
idx = find((pi*dilationFactor^(-2-k) <= abs(x)).*(pi*dilationFactor^(-k) > abs(x)));
w(idx) = cos(pi * log(dilationFactor^(1+k) *abs(x(idx))/pi)/(2*log(dilationFactor)));
end