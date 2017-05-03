function mask = getDiskMask(diameter)
% GETDISKMASK compute a 3D disk mask with diameter half of the size of the image
%
% mask = getDiskMask(diameter)
% compute a 3D disk mask with diameter half of the size of the image
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

%compute low pass filtering effect
[~, mask1LP] = simoncelliMask(2*diameter, 2*diameter, 2*diameter);
mask1LP = (mask1LP(1:diameter, :, :) + mask1LP(diameter+1:end, :, :))/2;
mask1LP = (mask1LP(:,1:diameter, :) + mask1LP(:, diameter+1:end, :))/2;
mask1LP = (mask1LP(:, :, 1:diameter) + mask1LP(:, :, diameter+1:end))/2;
%compute high pass filter
[mask2HP ,~] = simoncelliMask(diameter, diameter,diameter);
%combine
mask = mask1LP.*mask2HP;
end