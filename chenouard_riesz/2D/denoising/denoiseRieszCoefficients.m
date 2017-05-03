function Adenoised = denoiseRieszCoefficients(A, rieszConfig, P, isTightFrame, steeringType, denoisingType, residualDenoisingType, noiseStd, denoisingParameters)
%DENOISERIESZCOEFFICIENTS denoise an image based on Riesz coefficients
%processing
%
%   Perform Riesz-wavelet coefficients denoising. The processing can be
%   soft or hard thresholding, as well as Wiener filtering.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A input noisy image
%
% RIESZCONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% P cell of generalization matrices for the generalized Riesz transform.
%
% ISTIGHTFRAME 1 if the generalization matrix generates a tight frame, 0
% otherwise (inverse is used for synthesis)
%
% STEERINGTYPE item from the SteeringType enumeration that specifies the coefficients steering
% method
%
% DENOISINGTYPE item from the DenoisingTypes enumeration that specifies the
% coefficients denoising method
%
% RESIDUALDENOISINGTYPE item from the ResidualDenoising enumeration that specifies the
% denoising method for the high-pass residual band
%
% NOISESTD standard deviation of the noise in A
%
%  DENOISINGPARAMETERS parameters used by the denoising methods. For the
%  hard and soft thresholding techniques it consists in a table of
%  thresholds for each wavelet scale
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% compute Riesz coefficients
[Q R] = multiscaleRieszAnalysis(A, rieszConfig);

%% steer coefficients
switch steeringType,
    case SteeringType.monogenic,
        ang = monogenicAnalysis(A, rieszConfig.numScales);
        Q = rotate2DRieszCoeffs(Q, rieszConfig.RieszOrder, ang);
    case SteeringType.maxSteer,
        [ang, Q] =maxsteer(Q, rieszConfig, 1);
    case SteeringType.none,
        ang = [];
    otherwise
        error('unknown steering method for riesz frame learning. Available options: monogenic, maxSteer, l1, none')
end

%% project coefficients
if ~isempty(P),
    Q = generalizedRiesz(Q, P);
end

%% compute the noise standard deviation in each band.
switch denoisingType,
    case {DenoisingTypes.hardThresholding, DenoisingTypes.softThresholding, DenoisingTypes.WienerFiltering}
        stdNoiseCell = cell(1, rieszConfig.numScales);
        noise = noiseStd*randn(size(A));
        [Qnoise Rnoise] = multiscaleRieszAnalysis(noise, rieszConfig);
        if ~isempty(P),
            Qnoise = generalizedRiesz(Qnoise, P);
        end
        for j=1:rieszConfig.numScales,
            stdNoiseTab = zeros(1, rieszConfig.RieszChannels);
            for k = 1:rieszConfig.RieszChannels,
                tmp = Qnoise{j}(:, :, k);
                stdNoiseTab(1, k) = std(tmp(:));
                clear tmp
            end
            stdNoiseCell{j} = stdNoiseTab;
            clear stdNoiseTab
        end
        stdNoiseResidual = std(Rnoise(:));
        clear noise
        clear Qnoise
        clear Rnoise
    case DenoisingTypes.vectorialWienerFiltering,
        covNoiseCell = cell(1, rieszConfig.numScales);
        noise = noiseStd*randn(size(A));
        [Qnoise Rnoise] = multiscaleRieszAnalysis(noise, rieszConfig);
        if ~isempty(P),
            Qnoise = generalizedRiesz(Qnoise, P);
        end
        for j=1:rieszConfig.numScales,
            qtmp = reshape(Qnoise{j}, size(Qnoise{j},1)*size(Qnoise{j},2), size(Qnoise{j},3));
            covNoiseCell{j} = qtmp'*qtmp/size(qtmp, 1);
            clear qtmp;
        end
        stdNoiseResidual = std(Rnoise(:));
end

%% denoise coefficients
switch denoisingType,
    case DenoisingTypes.hardThresholding,
        if (size(denoisingParameters(:)) < rieszConfig.numScales)
            error('denoisingParameters needs to be a vector of size numScales')
        end
        for j=1:rieszConfig.numScales,
            for k = 1:rieszConfig.RieszChannels,
                Q{j}(:, :, k) = hardThresholding(Q{j}(:, :, k), denoisingParameters(j), stdNoiseCell{j}(1, k));
            end
        end
    case DenoisingTypes.softThresholding,
        if (size(denoisingParameters(:)) < rieszConfig.numScales)
            error('denoisingParameters needs to be a vector of size numScales')
        end
        for j=1:rieszConfig.numScales,
            for k = 1:rieszConfig.RieszChannels,
                Q{j}(:, :, k) = softThresholding(Q{j}(:, :, k), denoisingParameters(j), stdNoiseCell{j}(1, k));
            end
        end
    case DenoisingTypes.WienerFiltering,
        for j=1:rieszConfig.numScales,
            Q{j} = WienerFiltering(Q{j}, stdNoiseCell{j});
        end
    case DenoisingTypes.vectorialWienerFiltering,
        for j=1:rieszConfig.numScales,
            Q{j} = vectorialWienerFiltering(Q{j}, covNoiseCell{j});
        end
    otherwise
        error('Unkwnown denoising method')
end

%% denoise residual
switch residualDenoisingType,
    case ResidualDenoising.none,
    case ResidualDenoising.sameAsBands,
        switch denoisingType,
            case DenoisingTypes.hardThresholding,
                R = hardThresholding(R, denoisingParameters(1), stdNoiseResidual);
            case DenoisingTypes.softThresholding,
                R = softThresholding(R, denoisingParameters(1), stdNoiseResidual);
            case {DenoisingTypes.WienerFiltering, DenoisingTypes.vectorialWienerFiltering},
                R = WienerFiltering(R, stdNoiseResidual);
            otherwise
                error('Unkwnown denoising method')
        end
    case ResidualDenoising.zeros,
        R = zeros(size(R));
    otherwise
        error('Unknown residual denoising method')
end

%% project back
if ~isempty(P),
    PR = cell(1, length(P));
    if isTightFrame,
        for j = 1:length(P),
            PR{j} = P{j}';
        end
    else
        for j = 1:length(P),
            PR{j} = inv(P{j});
        end
    end
    Q = generalizedRiesz(Q, PR);
end

%% rotate back coefficients
angR = cell(1, length(ang));
for j = 1:length(ang),
    angR{j} = -ang{j};
end
Q = rotate2DRieszCoeffs(Q, rieszConfig.RieszOrder, angR);

%% reconstruct
Adenoised = multiscaleRieszSynthesis(Q, R, rieszConfig);
end

