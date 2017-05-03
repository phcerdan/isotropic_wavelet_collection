function S = computeRotationMatrixForRiesz(R, order)
% COMPUTEROTATIONMATRIXFORRIESZ compute Riesz steering
% matrix corresponding to a spatial rotation
%
% --------------------------------------------------------------------------
% Input arguments:
%
% R spatial rotation matrix with [y x z] convention for the spatial domain
%
% ORDER order of the Riesz transform
%
% --------------------------------------------------------------------------
% Output arguments:
%
% S steering matrix for the Riesz-coefficients
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

r11 = R(1,1);
r12 = R(1, 2);
r21 = R(2, 1);
r22 = R(2,2);

%steering matrix
S = zeros(order+1, order+1);

for n1 = 0:order
    n2 = order - n1; % since n1 + n2 = order
    %loop over possible orders of derivative along y axis
    %for the second dimension of the steering matrix
    for k11 = 0:n1;
        k12 = n1-k11; % since k11+ k12 = n1
        for k21 = 0:n2
            k22 = n2 - k21; % since k21 + k22 = n2
            factK1 = factorial(k11)*factorial(k12);
            factK2 = factorial(k21)*factorial(k22);
            nFact = factorial(n1)*factorial(n2);
            r1Pow = (r11^k11)*(r12^k12);
            r2Pow = (r21^k21)*(r22^k22);
            m1 = k11 + k21;
            S(n1+1, m1+1) = S(n1+1, m1+1) + (nFact*r1Pow*r2Pow/(factK1*factK2));
        end
    end
end

for n1 = 0:order
    for m1 = 0:order
        S(n1+1, m1+1) = S(n1+1, m1+1)*sqrt(factorial(m1)*factorial(order-m1)/(factorial(n1)*factorial(order-n1)));
    end
end

end