function [Q residual] = multiscaleRieszAnalysis(A, config)
% MULTISCALERIESZANALYSIS perform the forward Riesz-wavelet transform
%
%  [Q residual] = multiscaleRieszAnalysis(A, config)  perform the forward
%  Riesz-wavelet transform for the 3D image A. The transform configuration
%  is specified by the RieszConfig object config.
%  Q is a cell structure. Each element is a 4D matrix which corresponds
%  to one scale of the wavelet transform. The first dimension of Q
%  indicates the Riesz band. The last element of Q corresponds to the
%  low-pass residual band of the wavelet transform.
%  residual is a 3D matrix which corresponds to a high pass residual image
%  (corners of the 3D frequency space).
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard@gmail.com
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets in practice",
%   IEEE Transactions on Image Processing,
%   Vol. 21, Num. 11, pp 4522--4533, Nov 2012
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.3, 18.11.2012
%
%  Riesz-3D-light toolbox

if ~config.spatialDomainStorage,
    A = fftn(A);
end
% Prefilter the image to extract the residual that cannot be analyzed with
% the Riesz transform
[Q residual] = RieszPrefilter(A,config);
% Riesz analysis step: get Riesz coefficients for the filtered image
Q=RieszAnalysis(Q,config);
%Wavelet analysis for each Riesz band
Q = WaveletAnalysis(Q, config);