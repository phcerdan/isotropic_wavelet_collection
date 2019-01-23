function showOrientations(orientations, sparse, dims, ratio)
% SHOWORIENTATIONS display monogenic orientations as 3D vectors
%
% showOrientations(orientations, sparse, dims)
% show orientations as 3D vectors. set sparse to 1 if orientations have
% been obtained for a subset of coefficients, 0 otherwise. dims is a vector
% indicating the size of each dimension of the data.
%
% warning: if the number of orientations is large this procedure is
% computationally intensive. Use the optional argument ratio in this case
% which is a value  between 0 and 1 that set the ratio of coefficients to
% randomly select for display.
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

if(nargin < 4)
    ratio = 1;
end 

if sparse,
    idxTab = find(orientations.coherency.*(rand(size(orientations.coherency,1), 1)<ratio));
    quiver3(orientations.coordinates(idxTab,1), orientations.coordinates(idxTab,2), orientations.coordinates(idxTab,3), orientations.u1(idxTab,1), orientations.u1(idxTab,2), orientations.u1(idxTab,3));
else
    coordinates = zeros(dims(1), dims(2), dims(3), 3);
    for z = 1:dims(3),
        coordinates(:,:,z, 3) = z;
    end
    for x = 1:dims(2),
        coordinates(:,x,:, 2) = x;
    end
    for y = 1:dims(1),
        coordinates(y,:,:, 1) = y;
    end
    coordinates = reshape(coordinates, dims(1)*dims(2)*dims(3), 3);
    idxTab = find(rand(size(coordinates,1), 1)<ratio);
    quiver3(coordinates(idxTab,1), coordinates(idxTab,2), coordinates(idxTab,3), orientations.u1(idxTab,1), orientations.u1(idxTab,2), orientations.u1(idxTab,3));    
end