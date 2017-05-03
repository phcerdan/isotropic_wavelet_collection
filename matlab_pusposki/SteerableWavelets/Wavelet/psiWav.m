function z = psiWav(h,u,harmonics,alpha,dx,xmax,steerangle)
%
% function z = psiWav(h,u,harmonics,alpha,dx,xmax,steerangle)
%
%
% compute wavelet function.
%
%
% INPUTS
% ------
%
% h             isotropic mother wavelet Fourier transform (function handle)
%
% u             vector of Riesz expansion coefficients for given harmonics
%
% harmonics     vector of harmonics (corresponding to exp^j*harmonics*angle)
%
% alpha         scale factor 
%
% dx,xmax       Fourier-domain sampling parameters
%
% steerangle    angle by which to steer the wavelet (optional, defaults to 0)
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



if ~exist('steerangle','var')
    steerangle = 0;
end

N    = length(u);

wmax = pi/dx;
dw   = pi/xmax;
w    = -wmax:dw:(wmax-dw);

[W1 W2] = ndgrid(w,w);

RHO     = sqrt(W1.^2+W2.^2);
PHI     = atan2(W2,W1);
PHI(isnan(PHI)) = 0;

PHI = PHI + steerangle;

Fzrho   = sqrt(alpha) * h(alpha*RHO);

Fzphi   = zeros(size(PHI));
for n = 1:N
    Fzphi = Fzphi + u(n)*exp(-1i*harmonics(n)*PHI);
end

Fz      = Fzrho.*Fzphi;

z       = fftshift(ifftn(fftshift(Fz)));

