function C1 = RieszSynthesis(C1, config)
% RIESZSYNTHESIS perform the backward 3D Riesz transform
%
% --------------------------------------------------------------------------
% Input arguments:
%
% C1 Riesz coefficients. It consits in a 3D matrix
% whose 3rd dimension corresponds to Riesz channels.
%
% CONFIG RieszConfig2D object that specifies the Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
%  C1 reconstructed image
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if config.RieszOrder>0,
    C1temp = C1;
    C1=zeros(config.size);
    for iter=1:config.RieszChannels,
        FA = fftn(C1temp(:, :, iter));
        C1=C1+ifftn(FA.*conj(config.RieszFilters{iter}));
    end;
    if config.realData,
        C1 = real(C1);
    end
end