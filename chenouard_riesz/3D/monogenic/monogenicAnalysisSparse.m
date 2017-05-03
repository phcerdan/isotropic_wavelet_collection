function orientations = monogenicAnalysisSparse(Q, config, J, sigma, idxMaskStruct, percentToKeep)
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
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox


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
    
    %get a mask of coefficients to process
    if (~isempty(idxMaskStruct))
         idxMask = idxMaskStruct{j};
    else
        idxMaskStruct = cell(1, config.numScales);
        tmp = squeeze(sum(Q{j}.^2,1));
        if (lenght(percentToKeep) < j)
            ratio = percentToKeep(1);
        else
            ratio = percentToKeep(j);
        end
        threshold = quantile(tmp(:), 1-ratio);
        idxMask = find(tmp>threshold);
        clear tmp;
    end
        
    n = size(idxMask,1);
    orientation1 = zeros(n, 3);
    orientation2 = zeros(n, 3);
    orientation3 = zeros(n, 3);
    coordinates = zeros(n, 3);
    coher = zeros(n, 1);
    ang = zeros(n, 2);
    
    %build cost matrix for each pixel
    cnt = 1;
    for it= idxMask',
        it2 = it -1;
        z = floor(it2/(w*h));
        x = floor((it2-z*w*h)/h);
        y = it2-z*w*h-x*h;
        x = x+1;
        y = y+1;
        z = z+1;
            
        j11 = J11(y,x,z);
        j22 = J22(y,x,z);
        j33 = J33(y,x,z);
        j12 = J12(y,x,z);
        j13 = J13(y,x,z);
        j23 = J23(y,x,z);
              
        % svd computation
        JMat = [j11 j12 j13; j12 j22 j23; j13 j23 j33];
        [U, eigvals] = svd(JMat);
        coher(cnt) = real(eigvals(1,1)-(eigvals(2,2)+eigvals(3,3))/2)/(eigvals(1,1)+(eigvals(2,2)+eigvals(3,3))/2);
        ang(cnt, :) = [atan(U(1,1)/U(2,1)), acos(((U(1,1)^2 + U(2,1)^2 + U(3,1)^2)/U(3,1)^2)^(-0.5))];
        orientation1(cnt, :) = U(:,1)';
        orientation2(cnt, :) = U(:,2)';
        orientation3(cnt, :) = U(:,3)';
        coordinates(cnt,:) = [y, x, z];
        cnt = cnt+1;
    end
    
    r = repmat(idxMask, 3, 1);
    c = [ones(n, 1); 2*ones(n,1); 3*ones(n,1)];
    orientation.u1 = sparse(r, c, reshape(orientation1, 3*n, 1));%, numCoeffs, 3);
    orientation.u2 = sparse(r, c, reshape(orientation2, 3*n, 1));%, numCoeffs, 3);
    orientation.u3 = sparse(r, c, reshape(orientation3, 3*n, 1));%, numCoeffs, 3);
    orientation.coordinates = sparse(r, c, reshape(coordinates, 3*n, 1));%, numCoeffs, 3);
    r = repmat(idxMask, 2, 1);
    c = [ones(n, 1); 2*ones(n,1)];
    orientation.ang = sparse(r, c, reshape(ang, 2*n, 1));%, numCoeffs, 2);
    r = idxMask;
    c = ones(n, 1);
    orientation.coherency = sparse(r, c, coher);%, numCoeffs, 1);
    orientations{j} = orientation;
end
end