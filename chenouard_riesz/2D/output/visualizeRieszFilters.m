function filters = visualizeRieszFilters(rieszConfig2D, P, zoomingFactor, sameFig, saveTiff, saveDir)
%VISUALIZERIESZFILTERS View the filters for the specified Riesz transform.
%
% FILTERS = VISUALIZERIESZFILTERS(RIESZCONFIG2D, P, ZOOMINGFACTOR, SAVETIFF,
% SAVEDIR) display the filters for the Riesz transform specified by
% rieszConfig2D with generalization matrix P, zoomed with factor P. Images
% of filters are saved to the saveDir directory
%
% --------------------------------------------------------------------------
% Input arguments:
%
% RIESZCONFIG2D configuration object of type RieszConfig2D for the Riesz
% transform
%
% P generalization matrix. Optional. Default is eye(rieszConfig2D.RieszChannels))
%
% ZOOMINGFACTOR: integer > 1 specifying the level interpolating zoom for
% the filter. Padding in Fourier domain is used for the interpolation.
% Optional. Default is 3.
%
% SAVETIFF: 1 if images of filters need to be save as .tiff files.
% Optional. Default is 0.
%
% SAVEDIR: directory where to save images of filters if SAVETIFF == 1.
% Optional. Default is 'rieszFilters'.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% FILTERS images of the filters
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if (nargin < 2)
    P = eye(rieszConfig2D.RieszChannels);
end
if (nargin<3)
    zoomingFactor = 3;
else
    zoomingFactor = max(1, round(zoomingFactor));
end
if (nargin<4)
    sameFig = 1;
end
if (nargin< 5)
    saveTiff = 0;
end
if (nargin < 6)
    saveDir = 'rieszFilters';
end

mask = rieszConfig2D.prefilter.filterLow;
filters = rieszConfig2D.RieszFilters;
if (nargin>1)
    %use P to recombine filters
    filtersP = cell(1, length(filters));
    for i=1:rieszConfig2D.RieszChannels,
        f = zeros(rieszConfig2D.size);
        for j=1:rieszConfig2D.RieszChannels,
            f = f + rieszConfig2D.RieszFilters{j}*P(j,i);
        end
        filtersP{i}=f;
    end
    filters = filtersP;
    clear filtersP;
end

maxAbs =0;
imList = cell(1, length(filters));
for i=1:length(filters),
    f = filters{i};
    pad = zoomingFactor*rieszConfig2D.size; % use paddidng in the Fourier domain for zooming
    tmp = padarray(fftshift(f.*mask), pad);
    tmp = fftshift(tmp);
    tmp = real(fftshift(ifft2(tmp)));
    
    [h1 w1] = size(tmp);
    tmp = tmp((pad(1)+1):(h1-pad(1)), (pad(2)+1):(w1-pad(2)));
    maxAbs = max(maxAbs, max(abs(tmp(:))));
    imList{i} = tmp;
end

if nargout == 0,
    clear filters;
end

if sameFig,
    epsilon = 10e-7;
    numCol = ceil(sqrt(rieszConfig2D.RieszChannels));
    numRow = ceil(rieszConfig2D.RieszChannels/numCol);
    figure
    for k = 1:rieszConfig2D.RieszChannels,
        subplot(numRow, numCol, k);
        imagesc(imList{k}, [-maxAbs+epsilon maxAbs+epsilon]); colormap(gray); axis image; axis off
        title(sprintf('channel %d', k));
    end
else
    epsilon = 10e-7;
    for i=1:rieszConfig2D.RieszChannels,
        figure; imagesc(imList{i}, [-maxAbs+epsilon maxAbs+epsilon]); colormap(gray); axis image; axis off
        title(sprintf('channel %d', i));
    end
end

if saveTiff,
    mkdir(saveDir);
    for i=1:rieszConfig2D.RieszChannels,
        filename = strcat('filter',num2str(i),'.tif');
        tmp = (imList{i}+maxAbs+epsilon)/(2*maxAbs);
        imwrite(uint8(round(255*tmp)), strcat(saveDir, '/',filename), 'tif');
    end
end
end