function orientations = monogenicAnalysisSparseOptParFor(Q, config, J, sigma, idxMaskStruct, percentToKeep)
% MONOGENICANALYSISSPARSE perform multiscale monogenic analysis for a
% subset of coefficients
%
% orientations = monogenicAnalysisSparse(Q, config, J, sigma, idxMaskStruct, kStdThresh)
% 
% input :
% -------
% - Q : Riesz-wavelet coefficients for a 3D Riesz transform of order 1
% - config : RieszConfig object used to generate Q
% - J : set of wavelet scales to analyze (integer array)
% - sigma : standard deviation of the Gaussian kernel used for the
% regularization of the structured tensor
% - idxMaskStruct : cell object that specifies at each scale which
% coefficients to analyze. Optional if percentToKeep is specified
% - percentToKeep : percentage of coefficients to keep at each scale.
% Single value between 0 and 1, or table of values (one for each scale).
% Optional if idxMaskStruct is provided.
%
% output:
% -------
% - orientations : cell object containing the monogenic information for
% each scale
%   * orientations{j}.u1 : first monogenic direction at scale j for each
%   point
%   * orientations{j}.u2 : second monogenic direction at scale j for each
%   point
%   * orientations{j}.u2 : third monogenic direction at scale j for each
%   point
%   * orientations{j}.coherency : coherency value at each point
%   * orientations{j}.coordinates : 3D coordinates of all the analyzed
%   points at scale j
%   * orientations{j}.ang : set of 3 angles that define the 3D monogenic
%   orientation cof all the analyzed points at scale j
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard?epfl.ch
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets and Monogenic Analysis
%    for Bioimaging", Proceedings of the Eighth IEEE International
%    Symposium on Biomedical Imaging: From Nano to Macro (ISBI'11), Chicago
%    IL, USA, March 30-April 2, 2011, pp. 2132-2135.
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox

disp('monogenicAnalysisSparseOptParFor!!!');

orientations = cell(1, length(J));
for j = J,
    %find which Riesz band correspond to each direction hilbert
    %transform with [y x z] convention
    bandsIdx = ones(1,3);
    for i=1:length(config.RieszOrders)
        band = find(config.RieszOrders{i});
        bandsIdx(band) = i;
    end
    
    J11 = squeeze(Q{j}(bandsIdx(1),:,:,:).^2);
    J12 = squeeze(Q{j}(bandsIdx(1),:,:,:).*Q{j}(bandsIdx(2),:,:,:));
    J13 = squeeze(Q{j}(bandsIdx(1),:,:,:).*Q{j}(bandsIdx(3),:,:,:));
    J22 = squeeze(Q{j}(bandsIdx(2),:,:,:).^2);
    J23 = squeeze(Q{j}(bandsIdx(2),:,:,:).*Q{j}(bandsIdx(3),:,:,:));
    J33 = squeeze(Q{j}(bandsIdx(3),:,:,:).^2);
    
    h = size(Q{j},2);
    w = size(Q{j},3);
    d = size(Q{j},4);
    
    %smooth coefficients
    K = 4;
    smoothingKernel = createPaddedGaussianKernel(h,w,d, sigma, K);
    smoothingKernel = fftn(smoothingKernel);
    %convolve in Fourier domain
    J11 = fftn(J11);
    J11 = J11.*smoothingKernel;
    J11 = real(fftshift(ifftn(J11)));
    
    J22 = fftn(J22);
    J22 = J22.*smoothingKernel;
    J22 = real(fftshift(ifftn(J22)));
    
    J33 = fftn(J33);
    J33 = J33.*smoothingKernel;
    J33 = real(fftshift(ifftn(J33)));
    
    J13 = fftn(J13);
    J13 = J13.*smoothingKernel;
    J13 = real(fftshift(ifftn(J13)));
    
    J12 = fftn(J12);
    J12 = J12.*smoothingKernel;
    J12 = real(fftshift(ifftn(J12)));
    
    J23 = fftn(J23);
    J23 = J23.*smoothingKernel;
    J23 = real(fftshift(ifftn(J23)));
    
    
    tmp = squeeze(sum(Q{j}.^2,1));
    tmp2=zeros(size(tmp));
    tmp2(idxMaskStruct{j})=1;
    tmp=tmp.*tmp2;
%     threshold = max(tmp(:))/2;
    threshold = quantile(tmp(idxMaskStruct{j}), 1-percentToKeep);
    idxMask = find(tmp>threshold);
    clear tmp tmp2;

    [orientation1,orientation2,orientation3] = parallelOrientations(J11,J22,J33,J12,J13,J23,idxMask,w,h,d);
    
    orientation.u1 = orientation1;
    clear orientation1;
    orientation.u2 = orientation2;
    clear orientation2;
    orientation.u3 = orientation3;
    clear orientation3;
    orientations{j} = orientation;
    clear orientation;
end
end