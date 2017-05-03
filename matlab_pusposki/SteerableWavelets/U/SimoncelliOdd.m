function [U,harmonics] = SimoncelliOdd(order)
%
% function [U,harmonics] = SimoncelliOdd(order)
%
%
% U matrix for odd Simoncelli design.
%
%
% INPUTS
% ------
%
% order         wavelet order
%
%
% OUTPUTS
% -------
%
% U             construction matrix
%
% harmonics     vector of harmonics (corresponding to exp^j*harmonics*angle)
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


if(mod(order,2)==0)
    error('For SimoncelliOdd design the order should be odd.');
end

harmonics = -order:2:order;

lH = length(harmonics);

Np = floor(order/2);
U = zeros(lH);
for j = 1:lH,
    thetaj = pi*(j-1)/lH + pi/2;
    for i=1:lH
        npp = harmonics(i);
        fact = factorial(2*Np+1)/(factorial(Np + (npp+1)/2)*factorial(Np - (npp-1)/2));
        U(i, j) = ((-1j)^order)*fact*exp(1j*npp*thetaj);
    end
end
U = U/sqrt(real(trace(U*U')));% rescale to be tight frame

U = U.';
