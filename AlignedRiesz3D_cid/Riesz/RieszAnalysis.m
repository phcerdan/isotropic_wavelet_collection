function R = RieszAnalysis(A,config)
% RIESZANALYSIS perform the 3D Riesz transform
%
% R = RieszAnalysis(A,config)
% perform the 3D Riesz transform for the 3D image A. The transform is
% characterized by the RieszConfig object config.
% Returns a 4D matrix of coefficients, which first dimension indicates the
% Riesz transform channel.
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
    if config.spatialDomainStorage,
        A=fftn(A);
        R = zeros(config.RieszChannels, size(A,1), size(A,2), size(A,3));
R=single(R);
        for iter=1:config.RieszChannels,
            R(iter, :,:,:)=ifftn(A.*config.RieszFilters{iter});
        end;
        if strcmp(config.dataType, 'real')
            R = real(R);
        end
    else
        R = zeros(config.RieszChannels, size(A,1), size(A,2), size(A,3));
        for iter=1:config.RieszChannels,
            R(iter, :,:,:)=A.*config.RieszFilters{iter};
        end;
    end
else
    R(1, :,:,:)= A;
end