% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Yashin Dicente Cid, yashin.dicente@hevs.ch
%    University of Applied Sciences Western Switzerland
%    University of Geneva, Switzerland
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    Y. Dicente Cid, H. M?ller, A. Platon, P-A. Poletti, A. Depeursinge
% 	 "3-D Solid Texture Classification Using Locally-Oriented Wavelet Transforms",
%    IEEE Transactions on Image Processing, 2017
%
% -------------------------------------------------------------------------

%% Create toy image
dim = 128;
dim3D = [dim dim dim];
[X,Y,Z] = meshgrid(1:dim,1:dim,1:dim);
img = sin(X) + sin(Y) + sin(Z);% + rand(dim3D); % 3D checkerboard + noise
img = single(img);

% create toy mask
mask = zeros(dim3D, 'uint8');
mask(dim/4:3*dim/4-1,dim/4:3*dim/4-1,dim/4:3*dim/4-1) = 1;

%% Riesz parameters
rParam.rOrder                   = 3;    % Riesz order
rParam.rNumberOfScales          = min(5, floor(log2(min(size(img))))-1); % number of decomposition levels
rParam.rPrepareFilters          = 1;
rParam.rPyramid                 = 0;    % pyramid wavelet decomposition (subsampling = 1) VS undecimated (= 0)
rParam.rIsotropicWaveletType    = 'omegaWav';
rParam.rIsotropicWaveletOmega   = 15/40;

rParam.alignmentType   = 'Order1Align'; % alignmentTypes = {'NoAlign', 'AlignPure', 'AlignAllInd', 'Order1Align'};
rParam.raAlignScale    = 1;
rParam.raAlignSparse   = 1;
rParam.raSigma         = 1; %05:0.5;4

rSizeVol = size(img);

% rieszCoeffs: not alinged yet:
rConfig         = RieszConfig(rSizeVol, rParam.rOrder, rParam.rNumberOfScales, rParam.rPrepareFilters, rParam.rPyramid, rParam.rIsotropicWaveletType, rParam.rIsotropicWaveletOmega);
rieszCoeffs     = multiscaleRieszAnalysis(img, rConfig);


%% Alignments
if strcmp(rParam.alignmentType, 'AlignPure')
    % Align in pure components
    rieszCoeffs = alignIn3PureComponents(rieszCoeffs, rSizeVol, rParam.rNumberOfScales, rParam.rPyramid, rParam.raSigma, mask, rConfig, rParam.raAlignSparse);

elseif strcmp(rParam.alignmentType, 'AlignAllInd')
    % Align in all components (Independently)
    rieszCoeffs = alignInAllComponents(rieszCoeffs, mask, rParam.raSigma, rParam.raAlignScale);

elseif strcmp(rParam.alignmentType, 'Order1Align')
    % Align with Order 1 Riesz

    % 1st compute Riesz order 1:
    rConfigOrder1       = RieszConfig(rSizeVol, 1, 1, rParam.rPrepareFilters, rParam.rPyramid, rParam.rIsotropicWaveletType, rParam.rIsotropicWaveletOmega);
    rieszCoeffsOrder1   = multiscaleRieszAnalysis(img, rConfigOrder1);

    rieszCoeffs = alignInOrder1(rieszCoeffs, rieszCoeffsOrder1, rSizeVol, rParam.rNumberOfScales, rParam.rPyramid, rParam.raSigma, mask, rConfig, rConfigOrder1, rParam.raAlignSparse);
end

disp('rieszCoeffs computed');