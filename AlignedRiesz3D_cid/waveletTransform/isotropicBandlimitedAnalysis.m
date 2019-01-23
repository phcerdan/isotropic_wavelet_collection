% author: Nicolas Chenouard, nicolas.chenouard@epfl.ch
% part of the monogenicTk

function [LP, HP] = isotropicBandlimitedAnalysis(Im, dim, J, isotropicWaveletType, spatialDomain, downsampling, isotropicWaveletOmega)
% multidimensional isotropic wavelet decomposition
%
% input:
% ------
% Im: multidimensional image to decompose
% dim: dimension of the image. Should be 1, 2 or 3.
% J: number of wavelet scales
% isotropicWaveletType: optional, wavelet function used. See
% IsotropicWaveletType enumeration.
% spatialDomain: optional, image and coefficients in the spatial domain (1, default), or in
% the Fourier domain (0).
%
% output:
% -------
% LP: lowpass filter image
% HP: wavelet coefficients


if (dim>3)
    error('Wavelet transform for 1d, 2d and 3d data only');
end

switch dim,
    case 2,
        if (size(Im,1)~=size(Im, 2))
            error('Isotropic image required for wavelet analysis');
        end
    case 3,
        if (size(Im,1)~=size(Im, 2) || size(Im,1)~=size(Im, 3))
            error('Isotropic volume required for wavelet analysis');
        end
end

if nargin <4
    isotropicWaveletType = IsotropicWaveletType.getDefaultValue();
end
if nargin <5
    spatialDomain = 1;
end

if spatialDomain,
    Im = fftn(Im);
end

HP = cell(1, J);
c3 = size(Im,3);
c2 = size(Im,2);
c1 = size(Im,1);
sizeInit=c1;
for iter =1:J
%     disp(['J=' num2str(iter)]);
    %compute mask
%     maskIsReady = 0;
    if strcmp(isotropicWaveletType,'Simoncelli'),
        [maskHP, maskLP] =  simoncelliMask(c1, c2, c3, downsampling, sizeInit);
    end;
    if strcmp(isotropicWaveletType,'absWav'),
        [maskHP, maskLP] =  absWavMask(c1, c2, c3, downsampling, sizeInit);
    end;
    if strcmp(isotropicWaveletType,'sqrtWav'),
        [maskHP, maskLP] =  sqrtWavMask(c1, c2, c3, downsampling, sizeInit);
    end
    if strcmp(isotropicWaveletType,'tanWav'),
        [maskHP, maskLP] =  tanWavMask(c1, c2, c3, downsampling, sizeInit);
    end
    if strcmp(isotropicWaveletType,'Shannon'),
        if downsampling==0,
            error('Non-decimated transform is not implemented for Shannon!');
        end;
        [maskHP, maskLP] =  halfSizeEllipsoidalMask(c1, c2, c3);
    end
    if strcmp(isotropicWaveletType,'Meyer'),
        if downsampling==0,
            error('Non-decimated transform is not implemented for Meyer!');
        end;
        [maskHP, maskLP] =  meyerMask(c1, c2, c3);
    end
    if strcmp(isotropicWaveletType,'Papadakis'),
        if downsampling==0,
            error('Non-decimated transform is not implemented for Papadakis!');
        end;
        [maskHP, maskLP] =  papadakisMask(c1, c2, c3);
    end
    if strcmp(isotropicWaveletType,'opt310Wav'),
        if downsampling==0,
            error('Non-decimated transform is not implemented for opt310Wav!');
        end;
        [maskHP, maskLP] = opt310Mask(c1, c2, c3);
    
%     plot(1:128,maskLP(:,1,1)); hold on;
%     plot(1:128,maskHP(:,1,1),'r');       
    end
    if strcmp(isotropicWaveletType,'omegaWav'),
        [maskHP, maskLP] = omegaWav(c1, c2, c3,isotropicWaveletOmega,downsampling,sizeInit);
    end

%     plot(1:128,fftshift(maskLP(:,1,1)),'LineWidth',2); hold on;
%     plot(1:128,fftshift(maskHP(:,1,1)),'r','LineWidth',2);


%     if isotropicWaveletType == IsotropicWaveletType.Meyer,
%         [maskHP maskLP] =  meyerMask(size(Im, 1), size(Im,2), size(Im,3));
%         maskIsReady = 1;
%     end
%     if isotropicWaveletType == IsotropicWaveletType.Papadakis,
%         [maskHP maskLP] =  papadakisMask(size(Im, 1), size(Im,2), size(Im,3));
%         maskIsReady = 1;
%     end
%     if isotropicWaveletType == IsotropicWaveletType.Aldroubi,
%         [maskHP maskLP] =  aldroubiMask(size(Im, 1), size(Im,2), size(Im,3));
%         maskIsReady = 1;
%     end
%     if ~maskIsReady,
%         error('unknown wavelet type. See IsotropicWaveletType enumeration.')
%     end   
    
%     if downsampling==0 && iter~=1,
%         factor=1/sqrt(8);
%         maskHP=maskHP.*factor;
%         maskLP=maskLP.*factor;
%     end;

%     if downsampling==0,
%         figure;imshow(maskHP(:,:,1),[]);
%         figure;imshow(maskLP(:,:,1),[]);
%     end;
    
    % high pass image
    fftHP = Im.*maskHP;
    % low pass image
    Im = Im.*maskLP;
    
    switch dim,
        case 1,
            c1 = c1/2;
        case 2,
            c2 = c2/2;
            c1 = c1/2;
        otherwise,
            c3 = c3/2;
            c2 = c2/2;
            c1 = c1/2;
    end    
    
    %downSampling in spatial domain = fold the frequency domain
    %multiple dimensions: cascade of downsampling in each cartesian
    %direction
    if downsampling==1 && iter~=J,
        
        switch dim,
            case 1,
                Im = (Im(1:c1) + Im((1:c1)+c1))/2;
            case 2,
                Im = (Im(1:c1, 1:c2) + Im((1:c1)+c1, 1:c2) + Im((1:c1) + c1, (1:c2) +c2) + Im(1:c1, (1:c2) +c2))/4;
            otherwise,
                temp = zeros(c1,c2,c3);
                for i= 0:1
                    for j=0:1
                        for k= 0:1
                            temp = temp + Im((1:c1) + i*c1, (1:c2) + j*c2, (1:c3) + k*c3);
                        end
                    end
                end
                Im = temp/8;
        end
    end;
    
    if spatialDomain,
        HP{iter} = ifftn(fftHP);
    else
        HP{iter} = fftHP;
    end
%     figure;imshow(HP{iter}(:,:,1),[]);title('high');
%     figure;imshow(Im(:,:,1),[]);title('low');
end
if downsampling==0,
    Im=Im./sqrt(8);
end;
if spatialDomain,
    LP = ifftn(Im);
else
    LP = Im;
end

