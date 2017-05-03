function Q = waveletAnalysis(R, config)
%WAVELETANALYSIS perform wavelet decomposition of Riesz coefficients
%
% --------------------------------------------------------------------------
% Input arguments:
%
% R matrix of Riesz coefficients. It consists in a 3D matrix whose 3rd
% dimension corresponds to Riesz channels.
%
% CONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% Q cell of Riesz-wavelet coefficients. Each element of the cell is a 3D
% matrix that corresponds of the Riesz coefficients at a given scale.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if (config.numScales==0)
    iter = 1;
    Q{iter}{1} = R;
else
    if config.RieszOrder < 1 % no riesz analysis
        switch config.waveletType,
            case WaveletType.isotropic,
                [LP Q] = isotropicBandlimitedAnalysis(R, 2, config.numScales, config.isotropicWaveletType, 1);
                Q{config.numScales+1} = LP;
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
    else %process riesz coefficients
        switch config.waveletType,
            case WaveletType.isotropic,
                Q = cell(1, config.numScales+1);
                for k=1:config.RieszChannels,
                    [LP HP] =  isotropicBandlimitedAnalysis(R(:,:,k), 2, config.numScales, config.isotropicWaveletType, 1);
                    for q=1:config.numScales,
                        Q{q}(:,:, k)  = HP{q};
                    end
                    Q{config.numScales+1}(:,:,k) = LP;
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
end