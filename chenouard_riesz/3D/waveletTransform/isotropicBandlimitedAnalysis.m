function [LP, HP] = isotropicBandlimitedAnalysis(Im, dim, J, isotropicWaveletType, spatialDomain)
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
%
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
for iter =1:J
    %compute mask
    maskIsReady = 0;
    if isotropicWaveletType == IsotropicWaveletType.Simoncelli,
        [maskHP maskLP] =  simoncelliMask(size(Im, 1), size(Im,2), size(Im,3));
        maskIsReady = 1;
    end
    if isotropicWaveletType == IsotropicWaveletType.Meyer,
        [maskHP maskLP] =  meyerMask(size(Im, 1), size(Im,2), size(Im,3));
        maskIsReady = 1;
    end
    if isotropicWaveletType == IsotropicWaveletType.Papadakis,
        [maskHP maskLP] =  papadakisMask(size(Im, 1), size(Im,2), size(Im,3));
        maskIsReady = 1;
    end
    if isotropicWaveletType == IsotropicWaveletType.Aldroubi,
        [maskHP maskLP] =  aldroubiMask(size(Im, 1), size(Im,2), size(Im,3));
        maskIsReady = 1;
    end
    if isotropicWaveletType == IsotropicWaveletType.Shannon,
        [maskHP maskLP] =  halfSizeEllipsoidalMask(size(Im, 1), size(Im,2), size(Im,3));
        maskIsReady = 1;
    end
    if ~maskIsReady,
        error('unknown wavelet type. See IsotropicWaveletType enumeration.')
    end
    
    % high pass image
    fftHP = Im.*maskHP;
    % low pass image
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