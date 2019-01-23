function viewFilterInSpace(f, mask)
% VIEWFILTERINSPACE view a spatial version of a Riez filter
%
% viewFilterInSpace(f, mask) view a spatial version of a Riez filter
% specified by its frequency response f. mask is a 3D filter to make the
% filter bandlimited radially.
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


%rescale with padding
pad = size(f);
tmp = padarray(fftshift(f.*mask), pad);
tmp = fftshift(tmp);
tmp = real(fftshift(ifftn(tmp)));
[h1 w1 d1] = size(tmp);
f = tmp((pad(1)+1):(h1-pad(1)),(pad(2)+1):(w1-pad(2)), (pad(3)+1):(d1-pad(3)));

%iso surface
V = abs(f);
scaledV = real(f);
fv = isosurface(V,0.00008);
%fv = isosurface(V,0.00001);


p = patch(fv);
isonormals(V, p)
isocolors(1:size(f,1),1:size(f,2),1:size(f,3),scaledV, p);

set(p,'FaceColor','interp','EdgeColor','none');
daspect([1 1 1])
axis off
%view([0,100,0])
%view(3)
axis([1 64 1 64 1 64])
view(3);
camlight,
colormap(autumn)
%axis off
lighting phong,
end