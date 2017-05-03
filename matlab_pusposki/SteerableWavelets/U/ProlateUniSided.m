function [U,harmonics] = ProlateUniSided(harmonics)
%
% function [U,harmonics] = ProlateUniSided(harmonics)
%
%
% U matrix for uni-sided Prolate design.
%
%
% INPUTS
% ------
%
% harmonics     vector of harmonics (corresponding to exp^j*harmonics*angle)
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


if min(harmonics) < 0
    error('For ProlateUniSided design all harmonics should be non-negative.');
end

lH = length(harmonics);

W = zeros(lH);
for j = 1:lH;
    W(j,:) = harmonics(j)-harmonics;
end
for i = 1:lH
    for j = 1:lH
        if (W(i,j)==0)
            W(i,j) = pi^2/3;
        else
            W(i,j) = (2*pi*W(i,j)*cos(pi*W(i,j)) + sin(pi*W(i,j))*(pi^2*W(i,j)^2-2))/(pi*W(i,j)^3);
        end
        
    end
end

[U, D] = eig(W);
[~,idx] = sort(abs(diag(D)),'ascend');
U = U.';
U = U(idx,:);
U = U/sqrt(trace(U'*U));

end
