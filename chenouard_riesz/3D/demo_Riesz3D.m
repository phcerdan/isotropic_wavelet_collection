function [Q, R] = demo_Riesz3D(A, N)
%DEMO_RIESZ3D demonstrate the 3D Riesz-wavelet transform
%
%  [Q, R] = demo_Riesz3D(A, N) demonstrates the 3D
%  Riesz transform of order N for the 3D image A. Returns the Riesz-wavelet
%  coefficients Q with the high-pass residual R
%
%  [Q, R] = demo_Riesz3D(A) demonstrates the 3D
%  Riesz transform of order 1 for the 3D image A.
%
%  [Q, R] = Riesz3Ddemo() demonstrates the 3D
%  Riesz transform of order 1 for a synthetic image of a smooth sphere.
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

if (nargin < 1)
    % create a 3D sphere
    A = create3Dsphere(64, 12)*200;
    %smooth with a Gaussian kernel
    gKernel = createPaddedGaussianKernel(64,64,64, 1.5, 4);
    A = fftn(A);                                                                                                                                                                                                                           
    gKernel = fftn(gKernel);
    A = real(fftshift(ifftn(gKernel.*A)));
    clear gKernel
    figure; vol3d('cdata', A);
    title('Source volume: smooth 3D sphere')
    display('press any key to continue with the analysis of the volume')
    pause();
end

%% setup the 3D Riesz transform
if nargin < 2
    rieszOrder = 2;
else
    rieszOrder = N;
end
numWaveletScales = 3;
% setup the Riesz tranform parameters and pre compute the Riesz filters
config = RieszConfig(size(A), rieszOrder, numWaveletScales, 1);

%% visualize the Riesz filters in frequency domain
display('Showing Riesz filters')
visualizeRieszFilters(config);
display('press any key to compute Riesz-wavelet coefficients')
pause();

%% compute the Riesz-wavelet coefficients
[Q, R] = multiscaleRieszAnalysis(A,config);

%% Visualize coefficients
display('Showing Riesz-wavelet coefficients')
RieszVisualize3D(Q);
display('press any key to continue with the synthesis step')
pause();

%% Synthesis step
Arec = multiscaleRieszSynthesis(Q, R, config);

%% Check for perfect reconstruction
fprintf('Maximum absolute value reconstruction error: %e\n',max(abs(double(A(:))-Arec(:))));
fprintf('Root mean square error for the reconstruction: %e\n',sqrt(mean(abs(double(A(:))-Arec(:)).^2)));

end