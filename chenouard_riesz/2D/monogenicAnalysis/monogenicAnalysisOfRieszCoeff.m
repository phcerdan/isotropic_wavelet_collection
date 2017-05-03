function [ang coherency] = monogenicAnalysisOfRieszCoeff(QA, rieszConfig, sigma, full, A)
%MONOGENICANALYSISOFRIESZCOEFFS perform the multiscale monogenic analysis of given sets
% of Riesz-wavelet coefficients
%
% --------------------------------------------------------------------------
% Input arguments:
%
%
% QA Riesz-wavelet coefficients used for the monogenic analysis
%
% RIESZCONFIG RieszConfig2D object that specifies the Riesz-wavelet
% transform.
%
% SIGMA regularization parameter. It is the standard deviation of the regularizing
% Gaussian kernel.
%
% FULL Specifies if angles should be restricted to values in [-pi/2,pi/2].
% Optional, default is 0.
%
% A an image to analyze. It is required only if full == 1.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% ANG angles estimated pointwise in the wavelet bands. It consists in a cell
% of matrices. Each element of the cell corresponds to the matrix of angles
% for a wavelet band.
%
% COHERENCY coherency values estimated pointwise in the wavelet bands.
% It consists in a cell of matrices. Each element of the cell corresponds to
% the matrix of coherency values for a wavelet band.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if rieszConfig.RieszOrder ~= 1,
    error('The monogenic analysis requires Riesz coefficients of order 1');
end

if nargin>3,
    if (full && nargin==4)
        error('Original image required to compute angles within an full range');
    end
else
    full = 0;
    A = 0;
end
K = 4; % Gaussian smoothing kernel cut = K*sigma

%% compute angles
ang = cell(1, rieszConfig.numScales);
coherency = cell(1, rieszConfig.numScales);
%compute the regularization kernel
c = ceil(K*sigma)+1;
w = 2*ceil(K*sigma)+1;
smoothingKernel = zeros(w,w);
for x1 = 1:w;
    for x2 = 1:w;
        smoothingKernel(x1, x2) = exp(-((x1-c)^2 + (x2-c)^2)/(2*sigma^2));
    end
end
smoothingKernel = smoothingKernel/sum(smoothingKernel(:));

%% full range angle computation
if full, %compute sign of the direction thanks to the gradient of the wavelet coefficients
    %compute first wavelet coefficients
    wavConfig = RieszConfig2D(size(A), 0, rieszConfig.numScales, 1);
    Q = multiscaleRieszAnalysis(A, wavConfig);
    %then smooth the coefficients
    for j=1:rieszConfig.numScales,
        Q{j} = imfilter(Q{j}, smoothingKernel, 'symmetric');
        [FX,FY] = gradient(Q{j});
        %determine sign of the angle from the gradient
        Q{j} = atan2(FY, FX);
    end
end
%loop over the scales
for j=1:rieszConfig.numScales,
    %compute the 4 Jmn maps
    if (size(QA{j},3)==1)
        J11 = real(QA{j}).^2;
        J12 = real(QA{j}).*imag(QA{j});
        J22 = imag(QA{j}).^2;
    else
        J11 = QA{j}(:,:,1).^2;
        J12 = QA{j}(:,:,1).*QA{j}(:,:,2);
        J22 = QA{j}(:,:,2).^2;
    end
    %convolve the maps with the regularization kernel
    J11 = imfilter(J11, smoothingKernel, 'symmetric');
    J12 = imfilter(J12, smoothingKernel, 'symmetric');
    J22 = imfilter(J22, smoothingKernel, 'symmetric');
    %compute coherency
    coherency{j} = sqrt(((J22-J11).^2 + 4*J12.^2))./(J22 + J11+2);
    %compute the first eigenvalue table
    lambda1 = (J22 + J11 +sqrt((J11- J22).^2 + 4 * J12.^2))/2;
    if full, %use the gradient to discriminate angles shifted by pi
        ang{j} = atan((lambda1-J11)./J12) + pi*(Q{j}<0);
    else
        %compute the first eigen vector direction
        ang{j} = atan((lambda1-J11)./J12);
    end
end