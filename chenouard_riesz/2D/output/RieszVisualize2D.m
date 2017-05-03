function RieszVisualize2D(config, Q, grid)
%RIESZVISUALIZE2D display riesz-wavelet coefficients
%
% Display the Riesz-wavelet coefficients Q at each scale
%
% --------------------------------------------------------------------------
% Input arguments:
%
%
% CONFIG RieszConfig2D object that specifies the Riesz-wavelet
% transform.
%
% Q Riesz-wavelet coefficients to display
%
% GRID show the coefficients from the same scale in the same figure
% with a grid layout if 1. Display coefficients in separate images if 0.
% Optional. Default is 1.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if(nargin < 3)
    grid = 1;
end

if grid,
    numCol = ceil(sqrt(config.RieszChannels));
    numRow = ceil(config.RieszChannels/numCol);
    for scale = 1:config.numScales,
        figure;
        for band = 1:config.RieszChannels,
            subplot(numRow, numCol, band),
            imagesc(Q{scale}(:,:,band)); axis image; axis off
            title(sprintf('Scale %d -- Channel %d', scale, band));
        end
    end
    if (length(Q) > config.numScales)
        figure;
        for band = 1:config.RieszChannels,
            subplot(numRow, numCol, band),
            imagesc(Q{config.numScales+1}(:,:,band)); axis image; axis off
            title(sprintf('Coarse scale -- Channel %d',  band));
        end
    end
else
    for scale = 1:config.numScales,
        for band = 1:config.RieszChannels,
            figure; imagesc(Q{scale}(:,:,band)); axis image; axis off
            title(sprintf('Scale %d -- Channel %d', scale, band));
        end
    end
    if (length(Q) > config.numScales)
        for band = 1:config.RieszChannels,
            figure; imagesc(Q{config.numScales+1}(:,:,band)); axis image; axis off
            title(sprintf('Coarse scale -- Channel %d',  band));
        end
    end
    
end

