function [P ang] = learnGeneralizedRieszFrame(A, rieszConfig, equalizationType, steeringType, learnWithPCA, equiAngular)
%LEARNGENERALIZEDRIESZFRAME Learn a generalized Riesz-wavelet frame
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A image from which to learn the frame
%
% RIESZCONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% EQUALIZATIONTYPE element of the enumeration EqualizationType that specifies
% the method for equalizing Riesz channels
%
% STEERINGTYPE element from the enumeration SteeringType.
% It specifies the coefficient steering method before applying the learning
% technique.
%
% LEARNWITHPCA 1 if principal component analysis is to be used for learning.
% 0 otherwise.
%
% EQUIANGULAR 1 if equiangular generalization is to be used. 0 otherwise.
% The equiangular generalization consists in taking equiangular rotations of
% the first componenent of a Riesz-wavelet frame.
% --------------------------------------------------------------------------
% Output arguments:
%
% P a cell of generalization matrices. There is one matrix per wavelet scale.
%
% ANG the angles that have been used for steering the coefficients before the
% learning step.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% Equalize noise
noise = randn(size(A));
Qnoise = multiscaleRieszAnalysis(noise, rieszConfig);
switch equalizationType,
    case EqualizationType.Equalize,
        P = cell(1, rieszConfig.numScales + 1);
        for i=1:rieszConfig.numScales+1
            q = reshape(Qnoise{i}, size(Qnoise{i},1)*size(Qnoise{i},2), rieszConfig.RieszChannels);
            [eigvect, ~, eigval] = princomp(q);
            P{i} = eigvect*diag(ones(rieszConfig.RieszChannels,1)./sqrt(eigval));
        end
    case EqualizationType.BandByBand,
        P = cell(1, rieszConfig.numScales+1);
        for i=1:rieszConfig.numScales+1
            P{i} = zeros(rieszConfig.RieszChannels, rieszConfig.RieszChannels);
            for j = 1:rieszConfig.RieszChannels,
                P{i}(j,j) = 1/std(reshape(Qnoise{i}(:,:,j), size(Qnoise{i}, 1)*size(Qnoise{i}, 2), 1));
            end
        end
    case EqualizationType.None,
        P = cell(1, rieszConfig.numScales+1);
        for i=1:rieszConfig.numScales+1
            P{i} = eye(rieszConfig.RieszChannels);
        end
    otherwise,
        error('Unkwnown equalization type. See EqualizationType enumeration.')
end
clear Qnoise
clear noise

%% estimate angles and steer coefficients
Q = multiscaleRieszAnalysis(A, rieszConfig);
switch steeringType,
    case SteeringType.monogenic,
        ang = monogenicAnalysis(A, rieszConfig.numScales);
        Qmax = rotate2DRieszCoeffs(Q, rieszConfig.RieszOrder, ang);
    case SteeringType.maxSteer,
        [ang, Qmax] =maxsteer(Q, rieszConfig, 1);
    case SteeringType.none,
        ang = [];
        Qmax = Q;
    otherwise
        error('unknown steering method for riesz frame learning. Available options: monogenic, maxSteer, l1, none')
end

%% Project onto P
Qmax = generalizedRiesz(Qmax, P);

%% frame learning
if learnWithPCA,
    for i=1:rieszConfig.numScales+1
        q=reshape(Qmax{i},size(Qmax{i},1)*size(Qmax{i},2), rieszConfig.RieszChannels);
        [~, ~, eigVect] = svd(q'*q);
        P{i} = P{i}*eigVect;
    end
end

%% equiangular settings
if equiAngular,
    for j=1:rieszConfig.numScales+1,
        Pn = zeros(size(P{j}));
        %Pe = repmat(P{j}(:,1), 1, size(P{j}, 2));
        for i = 1:rieszConfig.RieszChannels,
            theta = (i-1)*pi/(rieszConfig.RieszChannels);
            R = [cos(theta), sin(theta); -sin(theta) cos(theta)];
            rotationMatrix = computeRotationMatrixForRiesz(R, rieszConfig.RieszOrder);
            Ptemp = rotationMatrix*P{j};
            Pn(:,i) = Ptemp(:,1);
        end
        P{j} = Pn;
    end
end