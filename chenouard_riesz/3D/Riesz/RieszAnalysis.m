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
    if config.spatialDomainStorage,
        A=fftn(A);
        R = zeros(config.RieszChannels, size(A,1), size(A,2), size(A,3));
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