function [ang coherency] = demo_monogenicAnalysis(A, numScales, sigma, full)
%DEMO_MONOGENICANALYSIS(A, NUMSCALES, SIGMA, FULL) perform monogenic
%analysis
%
% [ANG COHERENCY] = DEMO_MONOGENICANALYSIS(A, NUMSCALES, SIGMA, FULL)
% perform the monogenic analysis described in [1] for the input image A.
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
% FULL: evaluate the angles over the full range [-pi, pi]. Default is 0.
% This yields angles in the range [-pi/2, pi/2]
%
% --------------------------------------------------------------------------
%
% Output arguments:
%
% ANG: angles estimated by the monogenic analysis in the wavelet bands
%
% COHERENCY: estimated coherency in the wavelet bands
%
% --------------------------------------------------------------------------
%
% References:
%
% [1] Multiresolution Monogenic Signal Analysis Using the Riesz-Laplace Wavelet Transform
% M. Unser, D. Sage, D. Van De Ville. IEEE Transactions on Image Processing, vol. 18, no. 11,
% pp. 2402-2418, November 2009.
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
    A = buildDefaultImage(256);
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
if nargin < 4,
    full = 0;
end
[ang coherency] = monogenicAnalysis(A, numScales, sigma, full);

%% display angles and coherency at each scale
for j = 1:numScales,
    %display angles
    figure;
    imagesc(ang{j});
    colormap('hsv');
    axis off;
    axis image;
    title(sprintf('Angle at scale %d', j));
    %display coherency
    figure;
    imagesc(coherency{j});
    colormap('gray');
    axis off;
    axis image;
    title(sprintf('Coherency at scale %d', j));
    %display a composite image
    figure;
    clear hsv;
    hsv = zeros(size(ang{j}, 1), size(ang{j}, 2), 3);
    if full,
        hsv(:,:,1) = (ang{j}+pi)/(2*pi);
    else
        hsv(:,:,1) = (ang{j}+pi/2)/pi;
    end
    hsv(:,:,2) = coherency{j};
    hsv(:,:,3) = ones(size(hsv,1), size(hsv,2));
    rgb = hsv2rgb(hsv);
    imagesc(rgb)
    axis off;
    axis image;
    title(sprintf('Composite scale and coherency at scale %d', j));
end