function C1= waveletSynthesis(Q, config)
%WAVELETSYNTHESIS perform wavelet reconstruction of Riesz-wavelet coefficients
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q cell of Riesz-wavelet coefficients. Each element of the cell is a 3D
% matrix that corresponds of the Riesz coefficients at a given scale. The
% third dimension of each matrix corresponds to Riesz channels.
%
% CONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% C1 matrix of Riesz coefficients. It consists in a 3D matrix whose 3rd
% dimension corresponds to Riesz channels.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if config.RieszChannels < 2, %no riesz analysis
    switch config.waveletType,
        case WaveletType.isotropic,
            tmp  = cell(1, config.numScales);
            for j=1:config.numScales,
                tmp{j} = Q{j};
            end
            C1 = isotropicBandlimitedSynthesis(Q{config.numScales+1}, tmp, 2, config.isotropicWaveletType, 1);
        case WaveletType.spline
            error('2D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead');
        otherwise,
            [~, s] = enumeration('WaveletType');
            str = 'unkwnown wavelet type. Valid options are: ';
            for k = 1:length(s)
                str = strcat([str, s{k}, ', ']);
            end
            error(str);
    end
else % process riesz-wavelet coefficients
    switch config.waveletType,
        case WaveletType.isotropic,
            C1 = zeros(config.size(1), config.size(2), config.RieszChannels);
            for k=1:config.RieszChannels,
                tmp = cell(1, config.numScales);
                for j = 1:config.numScales,
                    tmp{j} = Q{j}(:,:,k);
                end
                C1(:,:,k) = isotropicBandlimitedSynthesis(Q{config.numScales+1}(:,:,k), tmp, 2, config.isotropicWaveletType, 1);
            end
        case WaveletType.spline
            error('2D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead');
        otherwise,
            [~, s] = enumeration('WaveletType');
            str = 'unkwnown wavelet type. Valid options are: ';
            for k = 1:length(s)
                str = strcat([str, s{k}, ', ']);
            end
            error(str);
    end
end

if config.realData,
    C1 = real(C1);
end