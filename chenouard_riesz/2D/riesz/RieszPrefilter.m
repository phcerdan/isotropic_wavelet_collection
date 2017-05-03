function [PA residual] = RieszPrefilter(A, config)
% Pre filter the image before applying the isostropic wavelet pyramid
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A image to process
%
% CONFIG RieszConfig2D object that specifies the Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% PA low frequency image obtained by filtering A
% RESIDUAL high frequency image
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if ~isempty(config.prefilter.filterLow),
    FA = fft2(A);
    PA = ifft2(FA.*config.prefilter.filterLow);
    residual = ifft2(FA.*config.prefilter.filterHigh);
else
    PA =A;
    residual = zeros(size(A));
end;