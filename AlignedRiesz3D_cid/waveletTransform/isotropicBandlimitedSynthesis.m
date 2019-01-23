% author: Nicolas Chenouard, nicolas.chenouard@epfl.ch
% part of the monogenicTk

function LP = isotropicBandlimitedSynthesis(LP, HP, dim, isotropicWaveletType, spatialDomain, downsampling)
% multidimensional isotropic wavelet decomposition
%
% input:
% ------
% LP: low pass coefficients
% HP: wavelet coefficients
% dim: dimension of the image. Should be 1, 2 or 3.
% waveletType: optional, wavelet function used, available options are: 'meyer',
% 'simoncelli', 'papadakis', 'aldroubi', 'shannon'. The default is 'simoncelli'.
% spatialDomain: optional, image and coefficients in the spatial domain (1, default), or in
% the Fourier domain (0).
%
% output:
% -------
% LP: reconstructed image


if (dim>3)
    error('Wavelet transform for 1d, 2d and 3d data only');
end

if nargin <4
    isotropicWaveletType = IsotropicWaveletType.getDefaultValue();
end
if nargin <5
    spatialDomain = 1;
end

numBand = size(HP(:));
sizeInit=size(LP,1);

if spatialDomain,
    if downsampling==0;
        LP = LP/sqrt(8);
    end;
    fftLP = fftn(LP);
    
    for iter=numBand:-1:1
        
        %get highpass image
        FHP = fftn(HP{iter});      
        
        if downsampling==1,
            %upsample in the frequency domain
            switch dim,
                case 3,
                    rep = [2 2 2];
                case 2,
                    rep = [2 2];
                case 1,
                    rep = [1 2];
            end
            fftLP = repmat(fftLP, rep);
            c1 = size(fftLP,1);
            c2 = size(fftLP,2);
            c3 = size(fftLP,3);
        else
            c1 = size(fftLP,1)/(2^(iter)/2);
            c2 = size(fftLP,2)/(2^(iter)/2);
            c3 = size(fftLP,3)/(2^(iter)/2);
        end;
        maskIsReady = 0;
        if isotropicWaveletType == IsotropicWaveletType.Simoncelli,
            [maskHP maskLP] =  simoncelliMask(c1, c2, c3, downsampling, sizeInit);
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Meyer,
            [maskHP maskLP] =  meyerMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Papadakis,
            [maskHP maskLP] =  papadakisMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Aldroubi,
            [maskHP maskLP] =  aldroubiMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Shannon,
            [maskHP maskLP] =  halfSizeEllipsoidalMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if ~maskIsReady,
            error('unknown wavelet type. See IsotropicWaveletType enumeration.')
        end
        
        if downsampling==0 && iter~=1,
            factor=1/sqrt(8);
            maskHP=maskHP*factor;
            maskLP=maskLP*factor;
        end;
        
        %filter highpass and lowpass images
        fftLP = (2^dim)*fftLP.*maskLP + FHP.*maskHP;
    end
    LP = ifftn(fftLP);
else
    if downsampling==0;
        LP = LP/sqrt(8);
    end;
    for iter =numBand:-1:1
        
        if downsampling==1,
            %upsample in the frequency domain
            switch dim,
                case 3,
                    rep = [2 2 2];
                case 2,
                    rep = [2 2];
                case 1,
                    rep = [1 2];
            end
            fftLP = repmat(fftLP, rep);
            c1 = size(fftLP,1);
            c2 = size(fftLP,2);
            c3 = size(fftLP,3);
        else
            c1 = size(fftLP,1)/(2^(iter)/2);
            c2 = size(fftLP,2)/(2^(iter)/2);
            c3 = size(fftLP,3)/(2^(iter)/2);
        end;
        %compute filter
        maskIsReady = 0;
        if isotropicWaveletType == IsotropicWaveletType.Simoncelli,
            [maskHP maskLP] =  simoncelliMask(c1, c2, c3, downsampling, sizeInit);
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Meyer,
            [maskHP maskLP] =  meyerMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Papadakis,
            [maskHP maskLP] =  papadakisMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Aldroubi,
            [maskHP maskLP] =  aldroubiMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if isotropicWaveletType == IsotropicWaveletType.Shannon,
            [maskHP maskLP] =  halfSizeEllipsoidalMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            maskIsReady = 1;
        end
        if ~maskIsReady,
            error('unknown wavelet type. See IsotropicWaveletType enumeration.')
        end
        
        if downsampling==0 && iter~=1,
            factor=1/sqrt(8);
            maskHP=maskHP*factor;
            maskLP=maskLP*factor;
        end;
        
        %filter highpass and lowpass images
        LP = (2^dim)*fftLP.*maskLP + HP{iter}.*maskHP;
    end
end