function rv = channelSynthesis(channels,harmonics)
%
% function rv = channelSynthesis(channels,harmonics)
%
%
% channel synthesis.
%
%
% INPUTS
% ------
%
% channels      3D array with channels(j,:,:) corresponding to the channel
%               transformed by exp^j*harmonics(j)*Fourier_angle
%
% harmonics     vector of harmonics (corresponding to exp^j*harmonics*angle)
%
%
% OUTPUT
% ------
%
% rv            recombination from applying exp^-j*harmonics*Fourier_angle to
%               the channels followed by summation
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


num_chan = length(harmonics);

sA       = size(channels);
sA       = sA(2:end);

wmax1 = pi;
wmax2 = pi;
dw1   = 2*pi/sA(1);
dw2   = 2*pi/sA(2);
w1    = -wmax1:dw1:(wmax1-dw1);
w2    = -wmax2:dw2:(wmax2-dw2);

[W1, W2] = ndgrid(w1,w2);

PHI = atan2(W2,W1);

rv = zeros(sA);

for iter=1:num_chan,
    Fchan = fftshift(fftn(squeeze(channels(iter,:,:))));
    Fexpj = exp(-1j*harmonics(iter)*PHI);
    rv = rv + ifftn(fftshift((Fexpj.*Fchan)));
end
