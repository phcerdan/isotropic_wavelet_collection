function qDenoised = WienerFiltering(q, noiseStdTab)
%WIENERFILTERING perform Wiener filtering of a set of Riesz coefficients
%
% QDENOISED = WIENERFILTERING(Q, NOISESTDTAB) perform Wiener filtering for the
% set of Riesz coefficients Q
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q a matrix of Riesz coefficients to process at a given scale
%
% NOISESTD table for the standard deviation of the noise corrupting Q
%
% --------------------------------------------------------------------------
% Output arguments:
%
% Q a matrix of denoised coefficients
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if size(q, 3)>1,
    vectors = reshape(q, size(q,1)*size(q, 2), size(q, 3));
    for k = 1:size(vectors, 2)
        vectors(:, k) = vectors(:, k) * max(0, var(vectors(:,k))-noiseStdTab(k).^2)/var(vectors(:,k));
    end
    qDenoised = reshape(vectors, size(q, 1), size(q, 2), size(q, 3));
else
    sigVar = var(q(:));
    qDenoised = q * max(0, sigVar-noiseStdTab.^2)/sigVar;
end
end