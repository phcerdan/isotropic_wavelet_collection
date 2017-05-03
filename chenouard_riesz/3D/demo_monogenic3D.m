function [Q, R, orientations] = demo_monogenic3D(A)
%DEMO_MONOGENIC3D demonstrate the 3D monogenic analysis with the Riesz transform
%
%  [Q R orientations] = demo_monogenic3D(A) demonstrates the 3D
%  Riesz transform of order 1 for the 3D matrix A, and the monogenic analysis.
%  Returns the Riesz-wavelet coefficients Q, the high-pass residual R and
%  the orientations estimated with the 3D monogenic signal.
%
%  [Q R orientations] = demo_monogenic3D() demonstrates the 3D
%  Riesz transform of order 1 for a synthetic image of a smooth sphere.
%  Returns the Riesz-wavelet coefficients Q, the high-pass residual R and
%  the orientations estimated with the 3D monogenic signal.
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard@gmail.com
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets in practice",
%   IEEE Transactions on Image Processing,
%   Vol. 21, Num. 11, pp 4522--4533, Nov 2012
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.3, 18.11.2012
%
%  Riesz-3D-light toolbox

if (nargin < 1)
    % create a 3D sphere
    A = create3Dsphere(64, 12)*200;
    %smooth with a Gaussian kernel
    gKernel = createPaddedGaussianKernel(64,64,64, 1.5, 4);
    A = fftn(A);                                                                                                                                                                                                                           
    gKernel = fftn(gKernel);
    A = real(fftshift(ifftn(gKernel.*A)));
    clear gKernel
end

%% setup the 3D Riesz transform
rieszOrder = 1;
numWaveletScales = 3;
% setup the Riesz tranform parameters and pre compute the Riesz filters
config = RieszConfig(size(A), rieszOrder, numWaveletScales, 1);

%% compute the Riesz-wavelet coefficients
[Q, R] = multiscaleRieszAnalysis(A,config);

%% Visualize coefficients
display('Showing coefficients before steering')
RieszVisualize3D(Q);
display('press any key to continue with the monogenic analysis')
pause();

%% compute the monogenic analysis
regularizationSigma = 1.5;
% we proceed only 5 percents of the coefficients as the monogenic analysis
% may be long. The ratio is doubled every scale
ratio = 0.05;
idxMaskStruct = cell(1, config.numScales);
for j = 1:config.numScales,
    tmp = squeeze(sum(Q{j}.^2,1));
    threshold = quantile(tmp(:), 1-ratio*2^(j-1));
    idxMaskStruct{j} = find(tmp>threshold);
    clear tmp;
end
orientations = monogenicAnalysisSparse(Q, config, 1:config.numScales, regularizationSigma, idxMaskStruct);


%% show projected orientations on a single horizontal slice
display('shows a projected orientations on a single horizontal slice')
figure;
axis image;
axis off;
scale = 1;
for slice = 1:size(Q{scale}, 2),
    displayCompositeOrientation(orientations{scale}, [size(Q{scale}, 2), size(Q{scale}, 3), size(Q{scale}, 4)], 1, slice, 1);
    title(sprintf('Composite orientations/coherency projected on slice %d for scale %d', slice, scale))
    pause(0.1);
end

%% show the main orientation as 3D vectors
figure;
for j = 2:config.numScales, % we start at scale 2 since visualizing scale 1 is usually computationally intensive
    idxTab = find(orientations{j}.coherency);
    quiver3(orientations{j}.coordinates(idxTab,1), orientations{j}.coordinates(idxTab,2), orientations{j}.coordinates(idxTab,3), orientations{j}.u1(idxTab,1), orientations{j}.u1(idxTab,2), orientations{j}.u1(idxTab,3));
    axis image;
    axis vis3d;
    title(sprintf('orientations at scale %d', j));
    display(sprintf('orientations at scale %d. Press any key to continue.', j));
    pause();
end

%% rotate Riesz-wavelet coefficients according to the monogenic directions
Q = rotate(Q, config, orientations, 1:config.numScales, 1, 1);
display('Showing coefficients after steering (rotation-invariant coefficients)')
RieszVisualize3D(Q, 1:config.numScales, 3);
display('press any key to continue with 2D representation')
pause();

% show central horizontal slice
for j = 1:config.numScales,
    figure;
    imagesc(squeeze(Q{j}(3,:,:, ceil(size(Q{j}, 4)/2))));
    axis image;
    axis off;
    title(sprintf('central slice for the 3rd band (rotation-invariant) at scale %d', j))
end
display('press any key to continue with the reconstruction')
pause();


%% Synthesis step
Q = rotate(Q, config, orientations, 1:config.numScales, 0, 1);
Arec = multiscaleRieszSynthesis(Q, R, config);

%% Check for perfect reconstruction
display('Image reconstruction after steering back coefficients');
fprintf('Maximum absolute value reconstruction error: %e\n',max(abs(double(A(:))-Arec(:))));
fprintf('Root mean square error: %e\n',sqrt(mean(abs(double(A(:))-Arec(:)).^2)));

end