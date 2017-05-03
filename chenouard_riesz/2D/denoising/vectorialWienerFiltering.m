function qdenoised = vectorialWienerFiltering(q, noiseCovariance)
%VECTORIALWIENERFILTERING perform vectorial Wiener denoising through Riesz
%channels
%
% QDENOISED = VECTORIALWIENERFILTERING(Q, NOISECOVARIANCE)
% perform vectorial Wiener denoising of the Riesz coefficients Q through
% the Riesz channels (3rd dimension of Q). The noise covariance through
% channels is given in NOISECOVARIANCE
%
%
% QDENOISED denoised Riesz coefficients
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

vectors = reshape(q, size(q,1)*size(q, 2), size(q, 3));
noisySignalCov = vectors'*vectors/size(vectors, 1);
signalCov = noisySignalCov - noiseCovariance;
% correct possible negative eigenvalues (taken from Portilla TIP 2003)
[Q, L] = eig(signalCov);
L = diag(diag(L).*(diag(L)>0))*sum(diag(L))/(sum(diag(L).*(diag(L)>0))+(sum(diag(L).*(diag(L)>0))==0));
signalCov = Q*L*Q';
% filter vectors
vectors = vectors*inv(signalCov + noiseCovariance)*signalCov;
qdenoised = reshape(vectors, size(q, 1), size(q, 2),size(q, 3));
end

