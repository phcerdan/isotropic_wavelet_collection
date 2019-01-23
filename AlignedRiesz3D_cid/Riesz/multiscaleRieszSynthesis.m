function A = multiscaleRieszSynthesis(Q, R, config)
% MULTISCALERIESZANALYSIS perform the backward Riesz-wavelet transform
%
%  A = multiscaleRieszSynthesis(Q, residual, config) perform the backward
%  Riesz-wavelet transform for the 3D Riesz-wavelet coefficients Q and the
%  residual R. The transform configuration is specified by the RieszConfig
%  object config.
%
%  Q is a cell structure. Each element is a 4D matrix which corresponds to
%  one scale of the wavelet transform. The first dimension of Q indicates
%  the Riesz band. The last element of Q corresponds to the low-pass
%  residual band of the wavelet transform. R is a 3D matrix which
%  corresponds to a high pass residual image (corners of the 3D frequency
%  space).
%
%  A is a 3D matrix corresponding to the 3D reconstructed image
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


%wavelet synthesis
A = WaveletSynthesis(Q,config);

%Riesz synthesis
A=RieszSynthesis(A,config);

% Postfilter
A=RieszPostfilter(A, R, config);

if ~config.spatialDomainStorage,
    A = ifftn(A); 
    if strcmp(config.dataType, 'real')
        A = real(A); 
    end
end
end

