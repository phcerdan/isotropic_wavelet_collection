function [maskHP maskLP] =  meyerMask(w,h,d)
% MEYERMASK build ellipsoidal mask that cuts the frequency
% plane at pi/2 for the norm of the frequency vector with a smooth
% transition. It generates Meyer's wavelets.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% W width of the mask 
% H height of the mask
% D depth of the mask
%
% --------------------------------------------------------------------------
% Output arguments:
%
% MASKHP mask that selects high frequencies
%
% MASKLP mask that selects low frequencies
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

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
    maskLP = maskLP + cos(0.5*pi*nu(4*dist-1)).*mask2.*(1-mask4);
    maskHP = sqrt(1-maskLP.^2);
    

function v = nu(t)
v = (t>0).*(t<1).*(t.^4 .*(35 - 84.*t + 70*t.^2 -20*t.^3));
        
