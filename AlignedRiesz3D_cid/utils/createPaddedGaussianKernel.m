function smoothingKernel = createPaddedGaussianKernel(h, w, d, sigma, K)
% CREATEPADDEDGAUSSIANKERNEL create a 3D Gaussian kernel
%
%  smoothingKernel = createPaddedGaussianKernel(h, w, d, sigma, K) create a
%  gaussian kernel of standard deviation sigma in a 3D volume of size h x w
%  x d: smoothingKernel. K is a cutoff for low values: K*sigma. 
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard?epfl.ch
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox

cx = (w+1)/2;
cy = (h+1)/2;
cz = (d+1)/2;
halfwdth = ceil(K*sigma);
smoothingKernel = zeros(h,w,d);
for x1=floor(cy-halfwdth):ceil(cy+halfwdth),
    for x2=floor(cx-halfwdth):ceil(cx+halfwdth),
        for x3=floor(cz-halfwdth):ceil(cz+halfwdth),
            smoothingKernel(x1,x2,x3) = exp(-((x1-cy)^2 + (x2-cx)^2+ (x3-cz)^2)/(2*sigma^2));
        end
    end
end
smoothingKernel = smoothingKernel/sum(smoothingKernel(:).^2);