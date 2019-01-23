function C1 = WaveletSynthesis(Q, config)
% WAVELETSYNTHESIS perform an inverse 3D wavelet transform
%
% C1 = WaveletSynthesis(Q, config) perform an inverse 3D wavelet transform
% for the set of Riesz-wavelet coefficients Q (cell of 4D matrices). The
% inverse transform is specified by the RieszConfig object config.
% Returns a 4D matrix C1 corresponding to 3D Riesz coefficients (first
% dimension as the Riesz channel).
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

if config.numScales < 1, %no riesz analysis
    if strcmpi('isotropic', config.waveletType),
        if config.spatialDomainStorage,
            HP = cell(1,config.numScales);
            for j =1:config.numScales,
                HP{j} = Q{j};
            end
            LP = Q{config.numScales+1};
            C1 = isotropicBandlimitedSynthesis(LP, HP, 3, config.isotropicWaveletType, config.spatialDomainStorage, config.downsampling);
        else
            HP = cell(1,config.numScales);
            for j =1:config.numScales,
                HP{j} = fftn(Q{j});
            end
            LP = fftn(Q{config.numScales+1});
            C1 = isotropicBandlimitedSynthesis(LP, HP, 3, config.isotropicWaveletType, config.spatialDomainStorage, config.downsampling);
        end
    else if strcmpi('spline', config.waveletType),
            error('3D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead');
        else
            error('unkwnown wavelet type. Valid options are: isotropic and spline');
        end
    end
else % process riesz-wavelet coefficients
    if strcmpi('isotropic', config.waveletType),
        C1 = zeros(config.RieszChannels, config.size(1), config.size(2), config.size(3));
        if config.spatialDomainStorage,
            for k=1:config.RieszChannels,
                HP = cell(1,config.numScales);
                for j =1:config.numScales,
                    HP{j} = squeeze(Q{j}(k,:,:,:));
                end
                LP = squeeze(Q{config.numScales+1}(k,:,:,:));
                C1(k,:,:,:) = isotropicBandlimitedSynthesis(LP, HP, 3, config.isotropicWaveletType, config.spatialDomainStorage, config.downsampling);
            end
        else
            for k=1:config.RieszChannels,
                HP = cell(1,config.numScales);
                for j =1:config.numScales,
                    HP{j} = fftn(squeeze(Q{j}(k,:,:,:)));
                end
                LP = fftn(squeeze(Q{config.numScales+1}(k,:,:,:)));
                C1(k,:,:,:) = isotropicBandlimitedSynthesis(LP, HP, 3, config.isotropicWaveletType, config.spatialDomainStorage, config.downsampling);
            end
        end
    else if strcmpi('spline', config.waveletType),
            error('3D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead');
        else
            error('unkwnown wavelet type. Valid options are: isotropic and spline');
        end
    end
end

if config.spatialDomainStorage && strcmp(config.dataType, 'real'),
    C1 = real(C1);
end