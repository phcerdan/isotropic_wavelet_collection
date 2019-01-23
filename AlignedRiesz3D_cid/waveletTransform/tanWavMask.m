% author: Nicolas Chenouard, nicolas.chenouard@epfl.ch
% part of the monogenicTk

function [maskHP, maskLP] =  tanWavMask(w,h,d,downsampling,sizeInit)

c0 = [ceil((w+1)/2), ceil((h+1)/2), ceil((d+1)/2)];

%rJ = c0/2;
yramp = [(1:c0(1))-1 (w:-1:(c0(1)+1))-c0(1)].^2;
xramp = [(1:c0(2))-1 (h:-1:(c0(2)+1))-c0(2)].^2;
zramp = [(1:c0(3))-1 (d:-1:(c0(3)+1))-c0(3)].^2;
yramp = repmat(yramp', 1, h);
xramp = repmat(xramp, w,1);

xramp=single(xramp);
yramp=single(yramp);
zramp=single(zramp);

%create mask dividing r<pi/2
dist = zeros(w,h,d);
dist=single(dist);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/2 - 1)^2) + xramp/((c0(2)/2 - 1)^2) + zramp(z)/((c0(3)/2 -1)^2);
end

mask2 = (dist<1);

%create mask selecting r<=pi/4
dist = zeros(w,h,d);
dist=single(dist);
for z=1:d
    dist(:,:,z) = yramp/((c0(1)/4)^2) + xramp/((c0(2)/4)^2) + zramp(z)/((c0(3)/4)^2);
end
mask4 = (dist<1);

%distance matrix
dist = zeros(w,h,d);
dist=single(dist);
for z=1:d
    dist(:,:,z) = sqrt(yramp/(c0(1)^2) + xramp/(c0(2)^2) + zramp(z)/(c0(3)^2));
end

% sqrt(log2(4/pi*x(I1)));
% maskHP = sqrt(log2(4*dist)).*(1-mask4).*mask2; % absWav highpass
maskHP = sqrt(0.5+0.5*tan(0.75*(2*log2(2*dist)+1))/tan(0.75)).*(1-mask4).*mask2; % tanWav highpass
maskHP(1) = 0;
maskHP = maskHP + (1-mask2);

% sqrt(1-AbsWav_d(x).^2)
% maskLP = sqrt(1-log2(4*dist)).*(1-mask4).*mask2; % absWav lowpass
maskLP = sqrt(0.5-0.5*tan(0.75*(2*log2(2*dist)+1))/tan(0.75)).*(1-mask4).*mask2; % tanWav lowpass
maskLP(1) = 0;
maskLP = maskLP + mask4;

if downsampling==0, % only works for square masks
    
    sizeDown=w

    maskLP=cat(1,maskLP(1:floor(end/2),:,:),zeros(sizeInit-sizeDown,sizeDown,sizeDown),maskLP(floor(end/2)+1:end,:,:));
    maskLP=cat(2,maskLP(:,1:floor(end/2),:),zeros(sizeInit,sizeInit-sizeDown,sizeDown),maskLP(:,floor(end/2)+1:end,:));
    maskLP=cat(3,maskLP(:,:,1:floor(end/2)),zeros(sizeInit,sizeInit,sizeInit-sizeDown),maskLP(:,:,floor(end/2)+1:end));
    
    maskHP=cat(1,maskHP(1:floor(end/2),:,:),ones(sizeInit-sizeDown,sizeDown,sizeDown),maskHP(floor(end/2)+1:end,:,:));
    maskHP=cat(2,maskHP(:,1:floor(end/2),:),ones(sizeInit,sizeInit-sizeDown,sizeDown),maskHP(:,floor(end/2)+1:end,:));
    maskHP=cat(3,maskHP(:,:,1:floor(end/2)),ones(sizeInit,sizeInit,sizeInit-sizeDown),maskHP(:,:,floor(end/2)+1:end));
end;

end

