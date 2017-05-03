function [ang, coherency, amplitude, phase, waveNumber, QA, Qwav, rieszConfig] = fullMonogenicAnalysis(A, J, sigma, full)
% FULLMONOGENICANALYSIS perform a complete multiscale monogenic analysis of an image
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A an image to analyze
%
% J the number of scales for the primary wavelet transform
%
% SIGMA regularization parameter. It is the standard deviation of the regularizing
% Gaussian kernel.
%
% FULL Specifies if angles should be restricted to values in [-pi/2,pi/2].
% Optional, default is 0.
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
% AMPLITUDE monogenic amplitude values estimated pointwise in the wavelet bands.
% It consists in a cell of matrices. Each element of the cell corresponds to
% the matrix of amplitude values for a wavelet band.
%
% PHASE monogenic phase values estimated pointwise in the wavelet bands.
% It consists in a cell of matrices. Each element of the cell corresponds to
% the matrix of phase values for a wavelet band.
%
% WAVENUMBER monogenic wave number values estimated pointwise in the wavelet bands.
% It consists in a cell of matrices. Each element of the cell corresponds to
% the matrix of wave nummber values for a wavelet band.
%
% QA Riesz-wavelet coefficients used for the monogenic analysis
%
% QWAV wavelet coefficients used for the monogenic analysis
%
% RIESZCONFIG RieszConfig2D object that specifies the Riesz-wavelet transform.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if nargin < 4,
    full = 0;
end
% configure the Riesz transform of order 1
rieszConfig = RieszConfig2D(size(A), 1, J, 1);
wavConfig = RieszConfig2D(size(A), 0, J, 1);
% compute Riesz-wavelet coefficients
QA = multiscaleRieszAnalysis(A, rieszConfig);

%% monogenic analysis
[ang coherency] = monogenicAnalysisOfRieszCoeff(QA, rieszConfig, sigma, full, A);

%% compute phase
%rotate Riesz coefficients
Qr = rotate2DRieszCoeffs(QA, rieszConfig.RieszOrder, ang);
% compute wavelet coefficients
Qwav = waveletAnalysis(A, wavConfig);

%% compute normalization
noise = zeros(rieszConfig.size);
noise(1) = 1;
Qnoise = multiscaleRieszAnalysis(noise, rieszConfig);
stdNoiseRiesz = ones(length(Qnoise), 1);
for j=1:length(Qnoise)
    tmp = Qnoise{j}(:,:,1);
    stdNoiseRiesz(j) = std(tmp(:));
end
clear Qnoise
%compute wavelet coefficients for noise
Qnoise = multiscaleRieszAnalysis(noise, wavConfig);
stdNoiseWav = ones(length(Qnoise), 1);
for j = 1:length(Qnoise)
    stdNoiseWav(j) = std(Qnoise{j}(:));
end
clear Qnoise

%compute phase and amplitude
phase = cell(1, rieszConfig.numScales);
amplitude = cell(1, rieszConfig.numScales);
for j = 1:rieszConfig.numScales,
    %phase{j} = atan(stdNoiseWav(j)*Qr{j}(:,:,1)./(Qwav{j}*stdNoiseRiesz(j)));
    phase{j} = angle(Qwav{j}/stdNoiseWav(j) + 1j * Qr{j}(:,:,1)/stdNoiseRiesz(j));
    %amplitude{j} = sqrt(QA{j}(:,:,1).^2 + QA{j}(:,:,2).^2 + Q{j}.^2*stdNoiseRiesz{j}(1)/stdNoiseWav(j));
    amplitude{j} = sqrt((Qr{j}(:,:,1)/stdNoiseRiesz(j)).^2 + (Qwav{j}/stdNoiseWav(j)).^2);
end


%% compute the wavenumber
waveNumber = cell(1, rieszConfig.numScales);

% compute the Riesz transform of the gradient image
gA = laplacian(A); %compute the laplacian of the image
QgA = multiscaleRieszAnalysis(gA, rieszConfig);
% compute wavelet coefficients
QwgA = waveletAnalysis(gA, wavConfig);
clear gA;

%form the code of Virginie Ulhmann
correctionFreq = 1;
for j=1:rieszConfig.numScales,
    %	<Rf, psi'_i> = w_(x,i) + jw(y,i).
    w = QgA{j};
    %normalize
    w = w/stdNoiseRiesz(j);
    
    r = QwgA{j};
    r = r/stdNoiseWav(j);
    
    p = Qwav{j};
    p = p/stdNoiseWav(j);
    
    theta = ang{j};
    q = QA{j}(:,:,1).*cos(theta) + QA{j}(:,:,2).*sin(theta);
    q = q/stdNoiseRiesz(j);
    % Numerator of eq. (34) of [1]: -q_i*(w_(x,i)*cos(theta) + w(y_i)*sin(theta)) + w_i*(r_(1x,i) + r_(2y,i)),
    % With the following definitions:
    %		<Rf, psi'_i> = w_(x,i) + jw(y,i),
    %		<f, psi'_i> = -(r_(1x,i) + r(2y_i)),
    % And w_i are the wavelet coefficients.
    nu = q.*(w(:,:,1).*cos(theta) + w(:,:,2).*sin(theta)) + p.*r;
    waveNumber{j} = (nu * correctionFreq )./(abs(p).^2 + abs(q).^2);
end
end
% laplacian like filtering with frequency response ||w||
function gA = laplacian(A)
[sizeY sizeX] = size(A);
gridX = -(floor(sizeX/2)):(floor(sizeX/2)-1);
gridX = 2*pi*repmat(gridX, sizeY, 1)/sizeX;
gridY = -(floor(sizeY/2)):(floor(sizeY/2)-1);
gridY = 2*pi*repmat(gridY', 1, sizeX)/sizeY;
dist = fftshift(sqrt(gridX.^2 + gridY.^2));
gA = ifft2(dist.*fft2(A));
end