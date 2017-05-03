function [A Adenoised Aclean] = demo_basicDenoising(Aclean, denoisingType, residualDenoisingType, numScales, RieszOrder, PCAframe, noiseStd, steeringType, denoisingParameters)
%DEMO_BASICDENOISING perform basic denoising operations in the
%Riesz-wavelet domain.
%
% [A ADENOISED ACLEAN] = DEMO_BASICDENOISING() Perform coefficients
% denoising in a steered generalized Riesz-wavelet frame before
% reconstructing the denoised image. To do so, synthetic white Gaussian noise is
% added to a non-noisy reference image.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% They are all optional.
%
% ACLEAN: input image without noise. If none is provided the method buildDefaultImage is
% called to build one.
%
% DENOISINGTYPE: the type of coefficients denoising method. It has to be an
% element of the enumeration DenoisingTypes. The default is
% DenoisingTypes.getDefaultValue.
%
% RESIDUAL DENOISINGTYPE: the type of denoising method for the residual high-frequency band.
% It has to be an element of the enumeration ResidualDenoising. The default is
% ResidualDenoising.getDefaultValue.
%
% NUMSCALES: number of wavelet scales for the decomposition. Default is
% 3.
% RIESZORDER: order of the Riesz transform. Default is 5.
%
% PCAFRAME: 1 if pca-based learning of a generalized Riesz-wavelet frame is
% to be performed, 0 otherwise. Default is 0.
%
% NOISESTD: standard deviation of the white Gaussian noise to add to the
% clean image. Optional. Default is 20;
%
% STEERINGTYPE:  specifies the coefficient steering method before learning.
% This should an item from the enumeration SteeringType. The default is
% SteeringType.getDefaultValue.
%
% DENOISINGPARAMETERS: parameters used by the denoising algorithms. For the
% hard and soft thresholding methods this should correspond to a vector
% specifying the threshold to be used in each wavelet band. The default is
% [3 2 2 2 ... ] such that the finest band is denoised with a higher
% threshold.
%
% --------------------------------------------------------------------------
%
% Output arguments:
%
% A: the noisy image
%
% Adenoised: the denoised image
%
% ACLEAN: the noiseless image
%
% --------------------------------------------------------------------------
%
% Notes:
%
% IMPORTANT: this routine is NOT intended to provide an effective image
% denoising method. More complex denoising methods and fine tuning
% parameters would be required to do so. The purpose of this routing is to
% demonstrate how to manipulate Riesz-wavelet coefficients and techniques
% such as orientation estimation, steering, frame learning, etc.
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
    Aclean = buildDefaultImage(256);
    figure; imagesc(Aclean); axis image, axis off, colormap gray;
    title('original image without noise')
end

%% default parameters
if nargin < 2,
    denoisingType = DenoisingTypes.getDefaultValue;
end
if nargin < 3,
    residualDenoisingType = ResidualDenoising.getDefaultValue;
end
if nargin < 4,
    numScales = 3;
end
if nargin < 5,
    RieszOrder = 5;
end
if nargin < 6,
    PCAframe = 1;
end
if nargin < 7,
    noiseStd = 20;
end
if nargin < 8,
    steeringType = SteeringType.getDefaultValue;
end
if nargin < 9,
    denoisingParameters = 2*ones(1, numScales);
    denoisingParameters(1) = 1.5*denoisingParameters(1);
end
%% generate noisy data
A = Aclean + noiseStd*randn(size(Aclean));
%% configure the Riesz transform
rieszConfig = RieszConfig2D(size(A), RieszOrder, numScales, 1);
%% learn a new frame
if (PCAframe)
    P = learnGeneralizedRieszFrame(A, rieszConfig, EqualizationType.getDefaultValue, steeringType, 1, 0);
else
    P = [];
end
%% perform denoising
isTightFrame = 0;
Adenoised = denoiseRieszCoefficients(A, rieszConfig, P, isTightFrame, steeringType, denoisingType, residualDenoisingType, noiseStd, denoisingParameters);
%% display results
%figure; imagesc(Aclean); colormap gray; axis off; axis image; title('Original clean image')
figure; imagesc(A); colormap gray; axis off; axis image; title('Noisy image')
figure; imagesc(Adenoised); colormap gray; axis off; axis image; title('Denoised image image')
psnr = computePSNR(Aclean, Adenoised);
display(sprintf('PSNR = %d dB', psnr));