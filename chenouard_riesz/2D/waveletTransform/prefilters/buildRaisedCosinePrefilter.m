function [maskLP maskHP] = buildRaisedCosinePrefilter(w, h, d)
%BUILDRAISEDCOSINEPREFILTER build prefilters for the Simoncelli wavelet function
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

yramp = [(1:c0(1))-1 (w:-1:(c0(1)+1))-c0(1)].^2;
xramp = [(1:c0(2))-1 (h:-1:(c0(2)+1))-c0(2)].^2;
zramp = [(1:c0(3))-1 (d:-1:(c0(3)+1))-c0(3)].^2;
yramp = repmat(yramp', 1, h);
xramp = repmat(xramp, w,1);

%create mask dividing r<=pi
dist = zeros(w,h,d);
if d ==1,
    dist(:,:,1) = yramp/((c0(1) - 1)^2) + xramp/((c0(2) - 1)^2);
else
    for z=1:d
        dist(:,:,z) = yramp/((c0(1) - 1)^2) + xramp/((c0(2) - 1)^2) + zramp(z)/((c0(3) -1)^2);
    end
end

mask2 = (dist<1);

%create mask selecting r<=pi/2
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/2)^2) + xramp/((c0(2)/2)^2) + zramp(z)/((c0(3)/2)^2);
end
mask4 = (dist<1);


%distance matrix
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = sqrt(yramp/(c0(1)^2) + xramp/(c0(2)^2) + zramp(z)/(c0(3)^2));
end


%low pass mask
maskLP = mask4;
maskLP = maskLP + (1-maskLP).*(mask2).*cos(pi*0.5*log2(2*dist));
maskLP(1) = 1;

maskHP = sqrt(1-maskLP.^2);
end