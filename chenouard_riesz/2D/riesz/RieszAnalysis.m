function Q = RieszAnalysis(A, config)
% RIESZANALYSIS perform the Riesz tranform of high order
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A image to analyze
%
% CONFIG RieszConfig2D object that specifies the Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% Q Riesz coefficients. It consits in a 3D matrix
% whose 3rd dimension corresponds to Riesz channels.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% perform Riesz transform
if config.RieszOrder>0,
    FA=fft2(A);
    Q = zeros(size(A, 1), size(A, 2), config.RieszChannels);
    if config.realData,
        for iter=1:config.RieszChannels,
            Q(:,:,iter) = real(ifft2(FA.*config.RieszFilters{iter}));
        end;
    else
        for iter=1:config.RieszChannels,
            Q(:,:,iter)=ifft2(FA.*config.RieszFilters{iter});
        end;
    end
else
    Q =A;
end