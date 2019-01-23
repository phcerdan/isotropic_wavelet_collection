function C1 = RieszSynthesis(C1, config)
% RIESZSYNTHESIS perform the inverse 3D Riesz transform
%
% C1 = RieszSynthesis(C1, config)
% perform the inverse 3D Riesz transform for the 4D matrix C1 (first
% dimension is the Riesz channel). The transform is characterized by the
% RieszConfig object config.
% Returns a 3D matrix which corresponds to the reconstructed image.
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

if config.RieszOrder>0,
    C1temp = C1;
    C1=zeros(config.size);
    if config.spatialDomainStorage,
        for iter=1:config.RieszChannels,
            FA = fftn(squeeze(C1temp(iter,:,:,:)));
            C1=C1+ifftn(FA.*conj(config.RieszFilters{iter}));
        end;
    else
        for iter=1:config.riesz.channels,
            C1 = C1 + squeeze(C1temp(iter,:,:,:)).*conj(config.RieszFilters{iter});
        end;     
        if strcmp(config.dataType, 'real')
            C1 = real(C1);
        end
    end
end