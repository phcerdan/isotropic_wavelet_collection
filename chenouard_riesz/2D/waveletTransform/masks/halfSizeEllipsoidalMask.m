function [maskHP maskLP] = halfSizeEllipsoidalMask(w,h,d)
% HALFSIZEELLIPSOIDALMASK build ellipsoidal mask that cuts the frequency
% plane at pi/2 for the norm of the frequency vector
%
% --------------------------------------------------------------------------
% Input arguments:
%
% W width of the mask 
% H height of the mask
% D depth of the mask
%
% --------------------------------------------------------------------------
% Output arguments:
%
% MASKHP mask that selects high frequencies
%
% MASKLP mask that selects low frequencies
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

c0 = [ceil((w+1)/2), ceil((h+1)/2), ceil((d+1)/2)];

yramp = [(1:c0(1))-1 (w:-1:(c0(1)+1))-c0(1)].^2;
xramp = [(1:c0(2))-1 (h:-1:(c0(2)+1))-c0(2)].^2;
zramp = [(1:c0(3))-1 (d:-1:(c0(3)+1))-c0(3)].^2;
yramp = repmat(yramp', 1, h);
xramp = repmat(xramp, w,1);
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/2 - 1)^2) + xramp/((c0(2)/2 - 1)^2) + zramp(z)/((c0(3)/2 -1)^2);
end
maskLP = dist<1;
maskHP = 1-maskLP;
end

