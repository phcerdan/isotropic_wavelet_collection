function [ang, coherency, amplitude, phase, waveNumber] = fullMonogenicAnalysisOfRieszCoeff(QA, Qwav, rieszConfig, sigma, full, A)
% FULLMONOGENICANALYSISOFRIESZCOEFFS perform a complete multiscale monogenic
% analysis of given sets of Riesz-wavelet and wavelet coefficients
%
% --------------------------------------------------------------------------
% Input arguments:
%
%
% QA Riesz-wavelet coefficients used for the monogenic analysis
%
% QWAV wavelet coefficients used for the monogenic analysis
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
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if (nargin <5)
    full = 0;
end
if ~exist('A')
    A = zeros(size(Qwav{1},1), size(Qwav{1},2));
end

%% monogenic analysis
[ang coherency] = monogenicAnalysisOfRieszCoeff(QA, rieszConfig, sigma, full, A);

if (nargout>2)
    % compute phase
    %rotate Riesz coefficients
    Qr = rotateRieszCoeffs(QA, rieszConfig.N, ang);
    A = RieszPrefilter(A,rieszConfig); % prefilter image
    Qwav = waveletAnalysis(A, rieszConfig);
    
    %compute normalization
    noise = zeros(rieszConfig.size);
    noise(1) = 1;
    Pnoise = RieszPrefilter(noise, rieszConfig);
    %compute Riesz-wavelet coefficients for noise
    Qnoise = RieszAnalysis(Pnoise, rieszConfig); %apply Riesz transform
    for j=1:length(Qnoise)
        tmp = Qnoise{j}(:,:,1);
        stdNoiseRiesz(j) = std(tmp(:));
    end
    clear Qnoise
    %compute wavelet coefficients for noise
    Qnoise = waveletAnalysis(Pnoise, rieszConfig);
    for j = 1:length(Qnoise)
        stdNoiseWav(j) = std(Qnoise{j}(:));
    end
    clear Qnoise
    
    %compute phase and amplitude
    phase = cell(1, rieszConfig.J);
    amplitude = cell(1, rieszConfig.J);
    for j = 1:rieszConfig.J
        %phase{j} = atan(stdNoiseWav(j)*Qr{j}(:,:,1)./(Qwav{j}*stdNoiseRiesz(j)));
        phase{j} = angle(Qwav{j}/stdNoiseWav(j) + 1j * Qr{j}(:,:,1)/stdNoiseRiesz(j));
        %amplitude{j} = sqrt(QA{j}(:,:,1).^2 + QA{j}(:,:,2).^2 + Q{j}.^2*stdNoiseRiesz{j}(1)/stdNoiseWav(j));
        amplitude{j} = sqrt((Qr{j}(:,:,1)/stdNoiseRiesz(j)).^2 + (Qwav{j}/stdNoiseWav(j)).^2);
    end
end

%% compute the wavenumber
if (nargout>4)
    waveNumber = cell(1, rieszConfig.J);
    
    % compute the Riesz transform of the gradient image
    gA = laplacian(A); %compute the laplacian of the image
    gA =RieszPrefilter(gA,rieszConfig); % prefilter image
    QgA = RieszAnalysis(gA,rieszConfig); %apply Riesz transform
    % compute wavelet coefficients
    QwgA = waveletAnalysis(gA, rieszConfig);
    clear gA;
    
    %from the code of Virginie Ulhmann
    correctionFreq = 1;
    for j=1:J,
        %	<Rf, psi'_i> = w_(x,i) + jw(y,i).
        w = QgA{j};
        %normalize
        w = w/stdNoiseRiesz(j);
        
        % <f, psi'_i> = -(r_(1x,i) + r(2y_i)).
        r = QwgA{j};
        r = r/stdNoiseWav(j);
        
        p = Qwav{j};
        p = p/stdNoiseWav(j);
        
        %q = Qr{j}(:,:,1);
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
end