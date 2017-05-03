function [U,harmonics] = SimoncelliComplete(order)
%
% function [U,harmonics] = SimoncelliComplete(order)
%
%
% U matrix for complete Simoncelli design.
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


harmonics = -order:order;
numChannels = length(harmonics);

%first compute even terms
orderE = order-mod(order, 2);
n = floor(orderE/2);
harmonicsE= -orderE:2:orderE;
numChannelsE = length(harmonicsE);
U = zeros(numChannelsE, numChannelsE);
for j=1:numChannelsE
    thetaj= pi*(j-1)/numChannelsE +pi/2;
    for i=1:numChannelsE
        U(i,j) = (1j/2)^orderE...
            * (factorial(orderE)/(factorial(n+harmonicsE(i)/2)*factorial(n-harmonicsE(i)/2)))...
            *(cos(harmonicsE(i)*thetaj) - 1j*sin(harmonicsE(i)*thetaj));
    end
end
Ueven = U/sqrt(real(trace(U'*U)));
%then compute odd terms
orderO = order - 1 + mod(order, 2);
n = floor(orderO/2);
harmonicsO = -orderO:2:orderO;
numChannelsO = length(harmonicsO);
U = zeros(numChannelsO, numChannelsO);
for j=1:numChannelsO
    thetaj= pi*(j-1)/numChannelsO +pi/2;
    for i=1:length(harmonicsO)
        U(i,j) = (1j/2)^orderO...
            * (factorial(orderO)/(factorial(n+fix(harmonicsO(i)/2))*factorial(n-fix(harmonicsO(i)/2))))...
            *(cos(harmonicsO(i)*thetaj) - 1j*sin(harmonicsO(i)*thetaj));
    end
end
Uodd = U/sqrt(real(trace(U'*U)));
%concatenate even and odd terms
U = zeros(numChannels, numChannels);
if (mod(order, 2)==0)
    for i=1:size(Ueven, 2)
        U(harmonicsE-harmonicsE(1)+1,i*2-1) = Ueven(:,i);
    end
    for i=1:size(Uodd, 2)
        U(harmonicsO-harmonicsE(1)+1,i*2) = Uodd(:,i);
    end
else
    for i=1:size(Ueven, 2)
        U(harmonicsE-harmonicsO(1)+1,i*2) = Ueven(:,i);
    end
    for i=1:size(Uodd, 2)
        U(harmonicsO-harmonicsO(1)+1,i*2-1) = Uodd(:,i);
    end
end
U = U/sqrt(2);

U = U.';
