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