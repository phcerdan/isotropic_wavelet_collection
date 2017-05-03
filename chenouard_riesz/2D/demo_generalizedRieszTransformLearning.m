function P = demo_generalizedRieszTransformLearning(A, numScales, order, steeringType, equalizationType,  equiAngular, showFilters)
%DEMO_GENERALIZEDRIESZTRANSFORMLEARNING learn a generalized Riesz-wavelet
%frame using principal component analysis
%
% P = DEMO_GENERALIZEDRIESZTRANSFORMLEARNING()
% Learn a generalized Riesz frame for the image A that is obtained by
% principal component analysis of the Riesz-wavelet bands.
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
% ORDER: order of the Riesz transform. Default is 5.
%
% STEERINGTYPE: specifies the coefficient steering method before learning.
% This should an item from the enumeration SteeringType. The default is
% SteeringType.getDefaultValue.
%
% EQUALIZATIONTYPE: specifies the coefficient equalization method before learning.
% This should an item from the enumeration EqualizationType. The default is
% EqualizationType.getDefaultValue.
%
% EQUIANGULAR: 1 if an equiangular frame has to be built from the first
% learnt Riesz filter. 0 otherwise. Default is 0.
%
% SHOWFILTERS: 1 to display the learnt filters, 0 otherwise. Default is 1.
%
% --------------------------------------------------------------------------
% Output arguments:
%
%  P: the set of generalization matrices for each wavelet scale.
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
    order = 5;
    display(sprintf('using Riesz transform of order %d', order));
end
if nargin  < 4,
    steeringType = SteeringType.getDefaultValue;
end
if nargin < 5
    equalizationType = EqualizationType.getDefaultValue;
end
if nargin < 6
    equiAngular = 0;
end
if nargin < 7
    showFilters = 1;
end

%% prepare the transform
rieszConfig = RieszConfig2D(size(A), order, numScales, 1);

%% learn a generalized version of the transform
learnWithPCA = 1;
P = learnGeneralizedRieszFrame(A, rieszConfig, equalizationType, steeringType, learnWithPCA, equiAngular);
%% display filters
if showFilters,
    for j = 1:rieszConfig.numScales,
        display(sprintf('showing filters at scale %d', j))
        visualizeRieszFilters(rieszConfig, P{j}, 6);
        display('press any key to show next scale filters');
        pause();
    end
end