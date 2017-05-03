function A = create3Dsphere(width, radius)
%CREATE3DSPHERE create a synthetic 3D shpere
%
%  A = create3Dsphere(width, radius) create a width x width x width 3D image
%  A of a sphere of intensity 1 and radius radius.
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

w = width;
d = width;
h = width;

A = zeros(w, h ,d);

c0 = [ceil((w+1)/2), ceil((h+1)/2), ceil((d+1)/2)];

yramp = [(1:c0(1))-1 (w:-1:(c0(1)+1))-c0(1)].^2;
xramp = [(1:c0(2))-1 (h:-1:(c0(2)+1))-c0(2)].^2;
zramp = [(1:c0(3))-1 (d:-1:(c0(3)+1))-c0(3)].^2;
yramp = repmat(yramp', 1, h);
xramp = repmat(xramp, w,1);
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = yramp/(radius^2) + xramp/(radius^2) + zramp(z)/(radius^2);
end

mask1 = (dist<=1);

for z=1:d
    dist(:,:,z) = yramp/((radius-1)^2) + xramp/((radius-1)^2) + zramp(z)/((radius-1)^2);
end

mask2 = (dist>1);

A = fftshift(mask1.*mask2);