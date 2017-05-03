function [maskLP maskHP] = buildShannonPrefilter(w, h, d)
%BUILDSHANNONPREFILTER build prefilters for the Shannon wavelet function
%
% --------------------------------------------------------------------------
% Input arguments:
%
% W width of the mask
%
% H height of the mask
%
% D depth of the mask
%
% --------------------------------------------------------------------------
% Output arguments:
%
% MASKLP Fourier transform of the filter that selects low-frequency
% components
%
% MASKHP Fourier transform of the filter that selects high-frequency
% components
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

c0 = [ceil((w+1)/2), ceil((h+1)/2), ceil((d+1)/2)];

%rJ = c0/2;
yramp = [(1:c0(1))-1 (w:-1:(c0(1)+1))-c0(1)].^2;
xramp = [(1:c0(2))-1 (h:-1:(c0(2)+1))-c0(2)].^2;
zramp = [(1:c0(3))-1 (d:-1:(c0(3)+1))-c0(3)].^2;
yramp = repmat(yramp', 1, h);
xramp = repmat(xramp, w,1);

%create mask dividing r<pi
dist = zeros(w,h,d);
if d ==1,
    dist(:,:,1) = yramp/((c0(1) - 1)^2) + xramp/((c0(2) - 1)^2);
else
    for z=1:d
        dist(:,:,z) = yramp/((c0(1) - 1)^2) + xramp/((c0(2) - 1)^2) + zramp(z)/((c0(3) -1)^2);
    end
end

mask = (dist<1);


%low pass mask
maskLP = mask;
maskHP = 1-maskLP;
end