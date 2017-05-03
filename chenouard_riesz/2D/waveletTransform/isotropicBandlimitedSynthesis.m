function LP = isotropicBandlimitedSynthesis(LP, HP, dim, waveletType, spatialDomain)
%ISOTROPICBANDLIMITEDSYNTHESIS perform multidimensional isotropic wavelet
%reconstruction
%
% --------------------------------------------------------------------------
% Input arguments:
%
% LP lowpass filter image.
%
% HP wavelet coefficients. It consists in a cell of matrices, each of
% which is a wavelet band.
%
% DIM dimension of the image. Should be 1, 2 or 3.
%
% WAVELETTYPE element of the IsotropicWaveletType enumeration. Specifies
% the radial wavelet function.
% Optional. Default is IsotropicWaveletType.getDefaultValue
%
% SPATIALDOAMAIN 1 if image and coefficients are stored in the spatial domain,
% or in the Fourier domain (0). Optional. Default is 1.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% LP multidimensional image that is reconstructed
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if dim>3,
    error('Wavelet transform for 1d, 2d and 3d data only');
end

if nargin <4
    waveletType = IsotropicWaveletType.getDefaultValue;
end
if nargin <5
    spatialDomain = 1;
end

numBand = size(HP(:));

if spatialDomain,
    fftLP = fftn(LP);
    for iter =numBand:-1:1
        %get highpass image
        FHP = fftn(HP{iter});
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
        %compute filter
        switch waveletType,
            case IsotropicWaveletType.Meyer,
                [maskHP maskLP] =  meyerMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Simoncelli,
                [maskHP maskLP] = simoncelliMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Papadakis,
                [maskHP maskLP] = papadakisMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Aldroubi,
                [maskHP maskLP] = aldroubiMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Ward,
				error('Ward s wavelet function is not provided in this toolbox');
            case IsotropicWaveletType.Shannon,
                [maskHP maskLP] =  halfSizeEllipsoidalMask(size(fftLP, 1), size(fftLP, 2), size(fftLP,3));
            otherwise
                error('unknown wavelet type. Valid options are: meyer, simoncelli, papadakis, aldroubi, shannon')
        end
        %filter highpass and lowpass images
        fftLP = (2^dim)*fftLP.*maskLP + FHP.*maskHP;
    end
    LP = ifftn(fftLP);
else
    for iter =numBand:-1:1
        %upsample in the frequency domain
        switch dim,
            case 3,
                rep = [2 2 2];
            case 2,
                rep = [2 2];
            case 1,
                rep = [1 2];
        end
        fftLP = repmat(LP, rep);
        %compute filter
        switch waveletType,
            case IsotropicWaveletType.Meyer,
                [maskHP maskLP] =  meyerMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Papadakis,
                [maskHP maskLP] =  papadakisMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Aldroubi,
                [maskHP maskLP] =  aldroubiMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Simoncelli,
                [maskHP maskLP] = simoncelliMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Ward,
                [maskHP maskLP] = wardMask(size(fftLP, 1), size(fftLP,2), size(fftLP,3));
            case IsotropicWaveletType.Shannon,
                [maskHP maskLP] =  halfSizeEllipsoidalMask(size(fftLP, 1), size(fftLP, 2), size(fftLP,3));
            otherwise
                error('unknown wavelet type. Valid options are: meyer, simoncelli, papadakis, aldroubi, shannon')
        end
        %filter highpass and lowpass images
        LP = (2^dim)*fftLP.*maskLP + HP{iter}.*maskHP;
    end
end