function hsv = displayCompositeOrientation(orientations, dims, dim, planIdx, orientationDim, intensities)
% DISPLAYCOMPOSITEORIENTATION display the projected orientation onto a
% single plane
%
%  displayCompositeOrientation(orientations, dims, dim, planIdx, orientationDim, intensities)
%  display the projected orientation onto a single plane
%
% input:
% ------
% - orientations : multiscale monogenic orientations
% - dims : size of the original 3D image
% - dim : dimension along which to project orientations (1, 2 or 3)
% - planIdx : index of the plan onto which orientations are projected
% - orientationDim : index of the orientation vector to project, 1 for u1,
%   2 for u2, and 3 for u3
% - intensities : grayscale background image (3D matrix). Optional.
%
% output:
% -------
% hsv: 2d color image of the projected orientations with a hsv look-up
% table
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


switch dim,
    case 1,
        orientationSlice = zeros(dims(2), dims(3));
        coherencySlice = zeros(dims(2), dims(3));
    case 2,
        orientationSlice = zeros(dims(1), dims(3));
        coherencySlice = zeros(dims(1), dims(3));
    case 3,
        orientationSlice = zeros(dims(1), dims(2));
        coherencySlice = zeros(dims(1), dims(2));
    otherwise
        error('dim is the index of the invariant dimension (1, 2, or 3)');
end


idxTab = find(orientations.coordinates(:,1))'; %non zeros orientations
%idx = x1 + (x2-1)*size1 + (x3-1)*size1*size2
idx = 1:(dims(1)*dims(2)*dims(3));
idx = reshape(idx, dims(1), dims(2), dims(3));
switch dim,
    case 1,
        idx = squeeze(idx(planIdx, :, :));
    case 2,
        idx = squeeze(idx(:, planIdx, :));
    case 3,
        idx = squeeze(idx(:, :, planIdx));
end

maxIdx = size(orientations.coherency, 1);
switch orientationDim,
    case 1,
        u2d = orientations.u1(:, [2 3]);
    case 2,
        u2d = orientations.u1(:, [1 3]);
    case 3,
        u2d = orientations.u1(:, [1 2]);
        
end
for m = 1:size(coherencySlice,1)
    for n = 1:size(coherencySlice,2)
        id = idx(m,n);
        if (id <= maxIdx)
            coherencySlice(m,n) = orientations.coherency(id);
            orientationSlice(m,n) = atan2(u2d(id, 2),u2d(id, 1));
        end
    end
end
%figure; imagesc(coherencySlice); colormap(gray); axis image; axis off
%figure; imagesc(orientationSlice); colormap('hsv'); axis image; axis off

hsv(:,:,1) = mod(orientationSlice, pi)/pi;
%hsv(:,:,1) = (orientationSlice+pi)/(2*pi);
hsv(:,:,2) = coherencySlice;

if ~exist('intensities')
    hsv(:,:,3) = ones(size(hsv,1), size(hsv, 2));
else
    switch dim
        case 1,
            hsv(:,:,3) = squeeze(intensities(planIdx, :,:));
        case 2,
            hsv(:,:,3) = squeeze(intensities(:, planIdx, :));
        case 3,
            hsv(:,:,3) = squeeze(intensities(:, :, planIdx));
    end
    hsv(:,:,3) = (hsv(:,:,3) - min(min(hsv(:,:,3))))/(max(max(hsv(:,:,3))) - min(min(hsv(:,:,3))));
end
rgb = hsv2rgb(hsv);
imagesc(rgb)
axis off;
axis image;
title(sprintf('Composite scale and coherency'));

% to save the image call imwrite(hsv2rgb(hsv), 'composite.png', 'PNG');