% author: Nicolas Chenouard, nicolas.chenouard@epfl.ch
% part of the monogenicTk

function [maskHP maskLP] =  aldroubiMask(w,h,d, order)

if nargin<4
    order = 3;
end

c0 = [ceil((w+1)/2), ceil((h+1)/2), ceil((d+1)/2)];

%rJ = c0/2;
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

mask2 = (dist<=1);

%create mask selecting r<=pi/4
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/4)^2) + xramp/((c0(2)/4)^2) + zramp(z)/((c0(3)/4)^2);
end
mask4 = (dist<=1);

%distance matrix
dist = zeros(w,h,d);
for z=1:d
    dist(:,:,z) = sqrt(yramp/(c0(1)^2) + xramp/(c0(2)^2) + zramp(z)/(c0(3)^2));
end

maskLP = mask4;
if (order==3)
    maskLP = maskLP + sin(2*pi*q3(dist/2)).*mask2.*(1-mask4);
else
    maskLP = maskLP + sin(2*pi*qInf(dist/2)).*mask2.*(1-mask4);
end
maskHP = sqrt(1-maskLP.^2);
end


function v = q3(t)
v = 256*t.^3 - 144*t.^2 + 24*t -1;
end

function v = qInf(t)
v = lambda(4-16*t)./(4*(lambda(4-16*t)+lambda(16*t-2)));
end

function v = lambda(t)
v = (t>0).*exp(-(t.^(-2)));
end