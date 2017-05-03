function Aeq = demo_monogenicAnalysis_amplitudeEqualization(A, numScales, sigma)
%DEMO_MONOGENICANALYSIS_AMPLITUDEQUALIZATION equalize the monogenic
%amplitude
% AEQ = DEMO_MONOGENICANALYSIS_AMPLITUDEQUALIZATION(A, NUMSCALES, SIGMA)
% perform monogenic analysis with smoothing SIGMA for NUMSCALES scales of
% the image A, equalize the monogenic amplitudes, and reconstruct the image
% AEQ.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% They are all optional.
%
% A: input image. If none is provided the method buildDefaultImage is
% called to build one.
%
% NUMSCALES: number of wavelet scales for the decomposition. Default is
% 3.
%
% SIGMA: standard deviation of the Gaussian window that is used for
% regularizing the monogenic analysis. Default is 1.5;
%
% --------------------------------------------------------------------------
%
% Output arguments:
%
% AEQ: image that is synthetized after equalizing the monogenic amplitudes
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% create a default image
if nargin < 1,
    A = buildDefaultImage();
    figure; imagesc(A); axis image, axis off, colormap gray;
    title('original image')
end

%% default parameters
if nargin < 2,
    numScales = 3;
end
if nargin < 3,
    sigma = 1.5;
end
fullAngles = 0;

[~, ~, amplitude, phase] = fullMonogenicAnalysis(A,  numScales, sigma, fullAngles);

%% display phase and amplitude images
for j = 1:numScales,
    figure;
    imagesc(amplitude{j});
    colormap gray;
    axis image;
    axis off;
    title(sprintf('Amplitude at scale %d', j));
    figure;
    imagesc(phase{j});
    colormap gray;
    axis image;
    axis off;
    title(sprintf('Phase at scale %d', j));
end

%% equalize the amplitude without changing the phase
% configure the Riesz transform of order 1

rieszConfig = RieszConfig2D(size(A), 1, numScales, 1);
[Q residual] = multiscaleRieszAnalysis(A, rieszConfig);

% rescale coefficients to account for amplitude equalization
for j = 1:rieszConfig.numScales,
    map = amplitude{j};
    threshold = quantile(amplitude{j}(:), 0.1); % discard low amplitude coefficients
    Q{j}(:,:,1) = (Q{j}(:,:,1)./map).*(map>threshold);
    Q{j}(:,:,2) = (Q{j}(:,:,2)./map).*(map>threshold);
end
Q{rieszConfig.numScales+1} = zeros(size(Q{rieszConfig.numScales+1}));
residual = zeros(size(residual));
%reconstruct
Aeq = multiscaleRieszSynthesis(Q, residual, rieszConfig);
figure;
imagesc(Aeq),
colormap(gray);
axis image;
axis off;
title('Amplitude equalized image');