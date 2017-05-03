function [orientations coherency ang] = monogenicAnalysis(Q, config, scales, sigma)
% MONOGENICANALYSIS perform multiscale monogenic analysis for Riesz-wavelet coefficients
%
% orientations = monogenicAnalysis(Q, config, scales, sigma)
% 
% input :
% -------
% - Q : Riesz-wavelet coefficients for a 3D Riesz transform of order 1
% - config : RieszConfig object used to generate Q
% - J : set of wavelet scales to analyze (integer array)
% - sigma : standard deviation of the Gaussian kernel used for the
% regularization of the structured tensor
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

orientations = cell(1, length(scales));
if (nargout >1)
   computeCoherency = 1; 
   coherency = cell(1, length(scales));
else
    computeCoherency = 0;
end
if (nargout >2)
   computeAng = 1;
   ang = cell(1, length(scales));
else
    computeAng = 0;
end
    
for j = scales,
    
    %find which Riesz band correspond to each direction hilbert
    %transform
    bandsIdx = ones(1,3);
    for i=1:length(config.riesz.orders)
        band = find(config.riesz.orders{i});
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
    
    if computeAng,
        ang{j} = zeros(size(Q{j},2), size(Q{j},3), size(Q{j},4), 2);
    end
    if computeCoherency,
        coher = zeros(size(Q{j},2), size(Q{j},3), size(Q{j},4));
    end
    
    orientation1 = zeros(h*w*d, 3);
    orientation2 = zeros(h*w*d, 3);
    orientation3 = zeros(h*w*d, 3);
    
    %build cost matrix for each pixel
    cnt = 1;
    for z = 1:d,
        for x = 1:w,
            for y = 1:h,
                j11 = J11(y,x,z);
                j22 = J22(y,x,z);
                j33 = J33(y,x,z);
                j12 = J12(y,x,z);
                j13 = J13(y,x,z);
                j23 = J23(y,x,z);       
                %svd computation
                JMat = [j11 j12 j13; j12 j22 j23; j13 j23 j33];
                [U, ~, eigvals] = princomp(JMat);
                if computeCoherency,
                    coher(y,x,z) = real(eigvals(1)-eigvals(2))/(eigvals(1)+eigvals(2)+eigvals(3));
                end
                if computeAng,
                    ang{j}(y, x, z, 1) = atan(U(1,1)/U(2,1));
                    ang{j}(y, x, z, 2) = acos(((U(1,1)^2 + U(2,1)^2 + U(3,1)^2)/U(3,1)^2)^(-0.5));
                end
                orientation1(cnt, :) = U(:,1)';
                orientation2(cnt, :) = U(:,2)';
                orientation3(cnt, :) = U(:,3)';
                cnt = cnt+1;
            end
        end
    end
    orientation.u1 = orientation1;
    clear orientation1;
    orientation.u2 = orientation2;
    clear orientation2;
    orientation.u3 = orientation3;
    clear orientation3;
    orientations{j} = orientation;
    clear orientation;
    if computeCoherency, 
        coherency{j} = coher;
    end
end
end