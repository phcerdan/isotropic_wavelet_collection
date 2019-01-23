function [Q R] = Riesz3Ddemo(A, N)
%RIESZ3DDEMO demonstrate the 3D Riesz-wavelet transform
%
%  [Q residual] = Riesz3Ddemo(A, order) demonstrates the 3D
%  Riesz transform of order N for the 3D image A. Returns the Riesz-wavelet
%  coefficients Q with the high-pass residual R
%
%  [Q residual] = Riesz3Ddemo(A) demonstrates the 3D
%  Riesz transform of order 1 for the 3D image A.
%
%  [Q residual] = Riesz3Ddemo() demonstrates the 3D
%  Riesz transform of order 1 for a synthetic image of a smooth sphere.
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard?epfl.ch
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets and Monogenic Analysis
%    for Bioimaging", Proceedings of the Eighth IEEE International
%    Symposium on Biomedical Imaging: From Nano to Macro (ISBI'11), Chicago
%    IL, USA, March 30-April 2, 2011, pp. 2132-2135.
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox
clc;close all;

if (nargin < 1)
    % create a 3D sphere
    A = create3Dsphere(64, 12)*200;
    %smooth with a Gaussian kernel
    gKernel = createPaddedGaussianKernel(64,64,64, 1.5, 4);
    A = fftn(A);                                                                                                                                                                                                                           
    gKernel = fftn(gKernel);
    A = real(fftshift(ifftn(gKernel.*A)));
    clear gKernel
end

%% setup the 3D Riesz transform
if nargin < 2
    rieszOrder = 2;
else
    rieszOrder = N;
end
numWaveletScales = 2;
downsampling = 0;
% setup the Riesz tranform parameters and pre compute the Riesz filters

config = RieszConfig(size(A), rieszOrder, numWaveletScales, 1, downsampling);

%% visualize the Riesz filters in frequency domain
display('Showing Riesz filters')
visualizeRieszFilters(config);
display('press any key to compute Riesz-wavelet coefficients')
% pause();

%% compute the Riesz-wavelet coefficients
[Q R] = multiscaleRieszAnalysis(A,config);

%% Visualize coefficients
display('Showing Riesz-wavelet coefficients')
RieszVisualize3D(Q);
display('press any key to continue with the synthesis step')
% pause();

%% Synthesis step
Arec = multiscaleRieszSynthesis(Q, R, config);

%% Check for perfect reconstruction
fprintf('Maximum absolute value reconstruction error: %e\n',max(abs(double(A(:))-Arec(:))));
fprintf('Root mean square error: %e\n',sqrt(mean(abs(double(A(:))-Arec(:)).^2)));

end