function [P1] = computePSNR(image1, image2, crop)
%COMPUTEPSNR Compute the Peak SNR between two 8-bits images
%
% COMPUTEPSNR(IMAGE1, IMAGE2) compute the Peak SNR in dB between the image
% IMAGE1 and the image IMAGE2. The two images are assumed to be 8-bits
% (data range is 0 to 255).
%
% COMPUTEPSNR(IMAGE1, IMAGE2m CROP) compute the Peak SNR in dB between the image
% IMAGE1 and the image IMAGE2. The two images are assumed to be 8-bits
% (data range is 0 to 255). The computation is restricted to a central part
% that is defined thanks to the vector CROP that defines the size of the
% margins.

if (nargin < 3)
    crop = [0 0];
end
e1 = double(image1) - double(image2);
e1 = e1(crop+1:end-crop, crop+1:end-crop);
VarNoise1 = mean2(e1.^2);
P1 = 10*log10(255^2/VarNoise1);