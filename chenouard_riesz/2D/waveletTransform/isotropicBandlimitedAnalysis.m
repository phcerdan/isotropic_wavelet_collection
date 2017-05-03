function [LP HP] = isotropicBandlimitedAnalysis(Im, dim, J, waveletType, spatialDomain)
%ISOTROPICBANDLIMITEDANALYSIS perform multidimensional isotropic wavelet decomposition
%
% --------------------------------------------------------------------------
% Input arguments:
%
% IM multidimensional image to decompose
%
% DIM dimension of the image. Should be 1, 2 or 3.
%
% J number of wavelet scales
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
% LP lowpass filter image.
%
% HP wavelet coefficients. It consists in a cell of matrices, each of
% which is a wavelet band.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

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

if (mod(size(Im, 1), 2^J)~=0)
    error('The size of the image has to be a multiple of 2^J');
end

if nargin <4
    waveletType = IsotropicWaveletType.getDefaultValue;
end
if nargin <5
    spatialDomain = 1;
end

if spatialDomain,
    Im = fftn(Im);
end

HP = cell(1, J);
for iter =1:J
    %compute mask
    switch waveletType,
        case IsotropicWaveletType.Meyer,
            [maskHP maskLP] =  meyerMask(size(Im, 1), size(Im,2), size(Im,3));
        case IsotropicWaveletType.Simoncelli,
            [maskHP maskLP] =  simoncelliMask(size(Im, 1), size(Im,2), size(Im,3));
        case IsotropicWaveletType.Papadakis,
            [maskHP maskLP] =  papadakisMask(size(Im, 1), size(Im,2), size(Im,3));
        case IsotropicWaveletType.Aldroubi,
            [maskHP maskLP] =  aldroubiMask(size(Im, 1), size(Im,2), size(Im,3));
        case IsotropicWaveletType.Shannon,
            [maskHP maskLP] =  halfSizeEllipsoidalMask(size(Im, 1), size(Im,2), size(Im,3));
        case IsotropicWaveletType.Ward,
			error('Ward s wavelet function is not provided in this toolbox');
        otherwise
            error('unknown wavelet type. Valid options are: meyer, simoncelli, papadakis, aldroubi, shannon')
    end
    
    %high pass image
    fftHP = Im.*maskHP;
    %low pass image
    Im = Im.*maskLP;
    
    %downSampling in spatial domain = fold the frequency domain
    %multiple dimensions: cascade of downsampling in each cartesian
    %direction
    switch dim,
        case 1,
            c1 = size(Im,2)/2;
            Im = 0.5*(Im(1:c1) + Im((1:c1)+c1));
        case 2,
            c2 = size(Im,2)/2;
            c1 = size(Im,1)/2;
            Im = 0.25*(Im(1:c1, 1:c2) + Im((1:c1)+c1, 1:c2) + Im((1:c1) + c1, (1:c2) +c2) + Im(1:c1, (1:c2) +c2));
        otherwise,
            c3 = size(Im,3)/2;
            c2 = size(Im,2)/2;
            c1 = size(Im,1)/2;
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
    if spatialDomain,
        HP{iter} = ifftn(fftHP);
    else
        HP{iter} = fftHP;
    end
end
if spatialDomain,
    LP = ifftn(Im);
else
    LP = Im;
end