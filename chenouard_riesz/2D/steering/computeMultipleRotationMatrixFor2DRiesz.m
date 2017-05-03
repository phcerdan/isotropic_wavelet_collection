function S = computeMultipleRotationMatrixFor2DRiesz(ang, order)
% COMPUTEMULTIPLEROTATIONMATRUXFOR2DRIESZ compute a set of rotation matrices
% for high-order Riesz coefficients
%
% --------------------------------------------------------------------------
% Input arguments:
%
% ANG a 2D matrix of angles
%
% ORDER the order of the Riesz transform
%
% --------------------------------------------------------------------------
% Output arguments:
%
% S rotation matrices concatenated in a single matrix (one matrix per line)
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

npoints = size(ang, 1);
cosTab = cos(ang);
sineTab = sin(ang);

%% build rotation matrices in riesz domain
S = zeros(order+1, order+1, npoints);

for n1 = 0:order
    n2 = order - n1; % since n1 + n2 = order
    nFact = factorial(n1)*factorial(n2);
    %loop over possible orders of derivative along y axis
    %for the second dimension of the steering matrix
    for k11 = 0:n1;
        k12 = n1-k11; % since k11+ k12 = n1
        factK1 = factorial(k11)*factorial(k12);
        for k21 = 0:n2
            k22 = n2 - k21; % since k21 + k22 = n2
            factK2 = factorial(k21)*factorial(k22);
            m1 = k11 + k21;
            S(n1+1, m1+1, :) = squeeze(S(n1+1, m1+1, :)) + ...
                ((-1)^k12)*(cosTab.^(k11+k22)).*((sineTab).^(k12+k21))...
                *nFact/(factK1*factK2);
        end
    end
end

%% renormalize matrices
for n1 = 0:order
    for m1 = 0:order
        S(n1+1, m1+1, :) = S(n1+1, m1+1, :)*sqrt(factorial(m1)*factorial(order-m1)/(factorial(n1)*factorial(order-n1)));
    end
end

end