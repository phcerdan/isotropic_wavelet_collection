function QU=generalizedRiesz(Q, U)
%GENERALIZEDRIESZ project Riesz coefficients onto a generalized Riesz frame
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q Riesz-wavelet coefficients
% U cell of generalization matrices
%
% --------------------------------------------------------------------------
% Output arguments:
%
% QU projected generalized Riesz-wavelet coefficients
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

QU = Q;
for i=1:length(Q)
    s=size(Q{i});
    temp=reshape(Q{i},s(1)*s(2),s(3));
    if iscell(U)
        temp=temp*U{i};
    else
        temp=temp*U;
    end
    QU{i}=reshape(temp,s);
end