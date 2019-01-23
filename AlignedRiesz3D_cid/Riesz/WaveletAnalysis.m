function Q = WaveletAnalysis(R, config)
% WAVELETANALYSIS perform a forward wavelet transform
%
% Q = WaveletAnalysis(R, config) perform a forward wavelet transform
% specified by the RieszConfig object config for the 4D set of Riesz
% coeffients R (first dimension of the matrix for Riesz channels).
% Returns a cell object Q with a 4D matrix for each scale of the
% Riesz-wavelet coefficients.
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard?epfl.ch
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets and Monogenic Analysis
%    for Bioimaging", Proceedings of the Eighth IEEE International
%    Symposium on Biomedical Imaging: From Nano to Macro (ISBI'11), Chicago
%    IL, USA, March 30-April 2, 2011, pp. 2132-2135.
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox



if (config.numScales==0)
    iter = 1;
    Q{iter} = R;
else
    if config.RieszOrder < 1 % no riesz analysis
        if strcmpi('isotropic', config.waveletType),
            Q=cell(1,config.numScales+1);
            % ydc: adapted to new call
            [LP HP] = isotropicBandlimitedAnalysis(R, 3, config.numScales, config.isotropicWaveletType, config.spatialDomainStorage, config.downsampling, config.isotropicWaveletOmega);
            % [LP HP] = isotropicBandlimitedAnalysis(R, 3, config.numScales, config.isotropicWaveletType, config.spatialDomainStorage, isotropicWaveletOmega);
            % end ydc
            if config.spatialDomainStorage,
                if strcmpi(config.dataType,'real'),
                    for scale=1:config.numScales,
                        Q{scale} = real(HP{scale});
                    end
                    Q{config.numScales+1} = real(LP);
                else
                    for scale=1:config.numScales
                        Q{scale} = HP{scale};
                    end
                    Q{config.numScales+1} = LP;
                end
            else
                if strcmpi(config.dataType,'real'),
                    for scale=1:config.numScales,
                        Q{scale} = real(ifftn(HP{scale}));
                    end
                    Q{config.numScales+1} = real(ifftn(LP));
                else
                    for scale=1:config.numScales,
                        Q{scale} = ifftn(HP{scale});
                    end
                    Q{config.numScales+1} = ifftn(LP);
                end
            end
        else if strcmpi('spline', config.waveletType)
                error('3D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead');
            else
                error('unkwnown wavelet type. Valid options are isotropic and spline');
            end
        end
    else % process riesz coefficients
        if strcmpi('isotropic', config.waveletType),
            Q=cell(1,config.numScales+1);
            for k=1:size(R,1) % loop on Riesz components
                [LP HP] = isotropicBandlimitedAnalysis(squeeze(R(k ,:,:,:)), 3, config.numScales, config.isotropicWaveletType, config.spatialDomainStorage, config.downsampling,config.isotropicWaveletOmega);
                if config.spatialDomainStorage,
                    if strcmpi(config.dataType,'real'),
                        for scale=1:config.numScales,
                            Q{scale}(k,:,:,:) = real(HP{scale});
                        end
                        Q{config.numScales+1}(k,:,:,:) = real(LP);
                    else
                        for scale=1:config.numScales,
                            Q{scale}(k,:,:,:) = HP{scale};
                        end
                        Q{config.numScales+1}(k,:,:,:) = LP;
                    end
                else
                    if strcmpi(config.dataType,'real'),
                        for scale=1:config.numScales,
                            Q{scale}(k,:,:,:) = real(ifftn(HP{scale}));
                        end
                        Q{config.numScales+1}(k,:,:,:) = real(ifftn(LP));
                    else
                        for scale=1:config.numScales,
                            Q{scale}(k,:,:,:) = ifftn(HP{scale});
                        end
                        Q{config.numScales+1}(k,:,:,:) = ifftn(LP);
                    end
                end
            end
        else if strcmpi('spline', config.waveletType)
                error('3D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead');
            else
                error('unknown wavelet type. Valid options are: istropic and spline')
            end
        end
    end
end
if config.spatialDomainStorage && strcmp(config.dataType, 'real')
    for j =1:length(Q),
        Q{j} = real(Q{j});
    end
end