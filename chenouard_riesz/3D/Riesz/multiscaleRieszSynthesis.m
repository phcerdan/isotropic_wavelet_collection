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

