function viewFilterInSpace(f, mask, epsi)
% VIEWFILTERINSPACE view a spatial version of a Riez filter
%
% viewFilterInSpace(f, mask) view a spatial version of a Riez filter
% specified by its frequency response f. mask is a 3D filter to make the
% filter bandlimited radially.
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

if nargin < 3,
    epsi = 0.0001;
end

%rescale with padding
pad = size(f);
tmp = padarray(fftshift(f.*mask), pad);
tmp = fftshift(tmp);
tmp = real(fftshift(ifftn(tmp)));
[h1, w1, d1] = size(tmp);
f = tmp((pad(1)+1):(h1-pad(1)),(pad(2)+1):(w1-pad(2)), (pad(3)+1):(d1-pad(3)));

%iso surface
V = abs(f);
scaledV = real(f);
fv = isosurface(V, epsi);

p = patch(fv);
isonormals(V, p)
isocolors(1:size(f,1),1:size(f,2),1:size(f,3),scaledV, p);

set(p,'FaceColor','interp','EdgeColor','none');
daspect([1 1 1])
axis off
axis([1 64 1 64 1 64])
view(3);
camlight,
%axis off
lighting phong,
end