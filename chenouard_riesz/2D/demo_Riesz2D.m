function [Q R] = demo_Riesz2D(A, N)
%RIESZ2DDEMO 2D Riesz-wavelet transform decomposition and reconstruction
%
%  [Q R] = DEMO_RIESZ2D() decompose a default image in the Riesz-wavelet
%  frame of order 2 and reconstruct it from the coefficients.
%
%  [Q R] = DEMO_RIESZ2D(A) decompose the image A in the Riesz-wavelet
%  frame of order 2 and reconstruct it from the coefficients.
%
%  [Q R] = DEMO_RIESZ2D(A, N) decompose the image A in the Riesz-wavelet
%  frame of order N and reconstruct it from the coefficients.
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
% M. Unser, N. Chenouard, D. Van De Ville, "Steerable Pyramids and Tight Wavelet Frames in L2(?d),"
% IEEE Transactions on Image Processing, vol. 20, no. 10, pp. 2705-2721, October 2011.
% http://bigwww.epfl.ch/publications/unser1103.html
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% create a default image
if (nargin < 1)
    A = buildDefaultImage(256);
    figure; imagesc(A); axis image, axis off, colormap gray;
    title('original image')
end
%% setup the 2D Riesz transform
if nargin < 2
    rieszOrder = 2;
    display('Riesz transform of order 2 is used')
else
    rieszOrder = N;
end
numWaveletScales = 3;
% setup the Riesz tranform parameters and pre compute the Riesz filters
config = RieszConfig2D(size(A), rieszOrder, numWaveletScales, 1);

%% visualize the Riesz filters in frequency domain
display('Showing Riesz filters')
visualizeRieszFilters(config);
display('press any key to compute Riesz-wavelet coefficients')
pause();

%% compute the Riesz-wavelet coefficients
[Q R] = multiscaleRieszAnalysis(A,config);

%% Visualize coefficients
display('Showing Riesz-wavelet coefficients')
RieszVisualize2D(config, Q)
display('press any key to continue with the synthesis step')
pause();

%% Synthesis step
Arec = multiscaleRieszSynthesis(Q, R, config);

%% Check for perfect reconstruction
fprintf('Maximum absolute value reconstruction error: %e\n',max(abs(double(A(:))-Arec(:))));
fprintf('Root mean square error: %e\n',sqrt(mean(abs(double(A(:))-Arec(:)).^2)));
figure; imagesc(A); axis image, axis off, colormap gray;
title('reconstructed image')
end