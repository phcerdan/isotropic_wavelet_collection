function [maskHP, maskLP] =  simoncelliMask(w,h,d)
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

c0 = [ceil((w+1)/2), ceil((h+1)/2), ceil((d+1)/2)];

yramp = [(1:c0(1))-1 (w:-1:(c0(1)+1))-c0(1)].^2;
xramp = [(1:c0(2))-1 (h:-1:(c0(2)+1))-c0(2)].^2;
zramp = [(1:c0(3))-1 (d:-1:(c0(3)+1))-c0(3)].^2;
yramp = repmat(yramp', 1, h);
xramp = repmat(xramp, w,1);

%create mask dividing r<pi/2
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/2 - 1)^2) + xramp/((c0(2)/2 - 1)^2) + zramp(z)/((c0(3)/2 -1)^2);
end

mask2 = (dist<1);

%create mask selecting r<=pi/4
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/4)^2) + xramp/((c0(2)/4)^2) + zramp(z)/((c0(3)/4)^2);
end
mask4 = (dist<1);

%distance matrix
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = sqrt(yramp/(c0(1)^2) + xramp/(c0(2)^2) + zramp(z)/(c0(3)^2));
end

%low pass mask
maskHP = cos(pi*0.5*log2(2*dist)).*(1-mask4).*mask2;
maskHP(1) = 0;
maskHP = maskHP + (1-mask2);

maskLP = cos(pi*0.5*log2(4*dist)).*(1-mask4).*mask2;
maskLP(1) = 0;
maskLP = maskLP + mask4;

end

