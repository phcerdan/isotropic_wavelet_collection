function mask = getDiskMask(diameter,downsampling)
% GETDISKMASK compute a 3D disk mask with diameter half of the size of the image
%
% mask = getDiskMask(diameter)
% compute a 3D disk mask with diameter half of the size of the image
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

%compute low pass filtering effect
[~, mask1LP] = simoncelliMask(2*diameter, 2*diameter, 2*diameter, downsampling, 2*diameter);
mask1LP = (mask1LP(1:diameter, :, :) + mask1LP(diameter+1:end, :, :))/2;
mask1LP = (mask1LP(:,1:diameter, :) + mask1LP(:, diameter+1:end, :))/2;
mask1LP = (mask1LP(:, :, 1:diameter) + mask1LP(:, :, diameter+1:end))/2;
%compute high pass filter
[mask2HP ,~] = simoncelliMask(diameter,diameter,diameter,downsampling,diameter);
%combine
mask = mask1LP.*mask2HP;
end