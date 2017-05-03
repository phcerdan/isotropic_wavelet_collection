function Q1 = rotate2DRieszCoeffs(Q, order, aci)
%ROTATE2DRIESZCOEFFS steer Riesz-wavelet coefficients
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q Riesz-wavelet coefficients
%
% ORDER the order of the Riesz transform
%
% ACI: angles of rotation for the different scales. It consists in a structure
% of 2D matrices. Each matrix corresponds to the pointwise rotation angles for
% a given wavelet scale.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% Q1 Riesz-wavelet coefficients steered according to the angles ACI
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

Q1 = Q;
%compute each rotation matrix without polynomial coefficients pre-computation
for i=1:length(aci)
    sub=reshape(Q{i},size(Q{i},1)*size(Q{i},2), size(Q{i}, 3));
    ang=aci{i}(:);
    S = computeMultipleRotationMatrixFor2DRiesz(ang, order);
    for j = 1:size(ang, 1)
        sub(j, :) = (S(:,:, j)*sub(j, :)')';
    end
    Q1{i} = reshape(sub, size(Q{i}, 1), size(Q{i}, 2), size(Q{i}, 3));
end


