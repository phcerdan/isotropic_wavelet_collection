function A = buildDefaultImage( width )
%BUILDDEFAULTIMAGE get a default image to process
%
% A = BUILDDEFAULTIMAGE() build a default image. If a file named
% 'barbara.tif' is present in the matlab path, then this image is loaded.
% Otherwise a default image  of size (256 x 256) that contains a smooth
% disk of radius 32 is built.
%
% A = BUILDDEFAULTIMAGE(width) build a default image. If a file named
% 'barbara.tif' is present in the build path, then this image is loaded.
% Otherwise a default image  of size (WIDTH x WIDTH) that contains a smooth
% disk of radius WIDTH/8 is built.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% WIDTH width of the synthetic image to generate
%
% --------------------------------------------------------------------------
% Output arguments:
%
% A default image
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if (exist('barbara.tif', 'file')>0)
    %% load barbara image
    A = double(imread('barbara.tif'));
else
    if (nargin<1)
        width = 256;
    end
    %% create a disk image
    radius = width/8;
    A = 1:width;
    A = repmat(A, width, 1);
    A = sqrt(((A-width/2).^2 + (A'-width/2).^2));
    A = double(A <= radius);
    %% smooth with a Gaussian kernel
    h = fspecial('gaussian', 24, 3);
    A = 200*imfilter(A, h);
    clear h
end

end

