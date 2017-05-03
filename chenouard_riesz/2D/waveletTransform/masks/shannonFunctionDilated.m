function w = shannonFunctionDilated(x, dilationFactor, k)
% SHANNONFUNCTIONDILATED build the radial part of the
% Shannon wavelet function for an arbitrary dilation factor.
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

w = (pi*dilationFactor^(-k) <= abs(x)).*(pi*dilationFactor^(-k+1) > abs(x));
end