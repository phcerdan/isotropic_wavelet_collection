function A = multiscaleRieszSynthesis(Q, R, config)
%MULTISCALERIESZANALYSIS perform the backward Riesz-wavelet transform
%
%  A = multiscaleRieszSynthesis(Q, residual, config) perform the backward
%  Riesz-wavelet transform for the 2D Riesz-wavelet coefficients Q and the
%  residual R. The transform configuration is specified by the RieszConfig
%  object config.
%
% --------------------------------------------------------------------------
% Input arguments:
%
%  Q is a cell structure. Each element is a 4D matrix which corresponds to
%  one scale of the wavelet transform. The first dimension of Q indicates
%  the Riesz band. The last element of Q corresponds to the low-pass
%  residual band of the wavelet transform.
%
%  R 3D matrix that corresponds to a high pass residual image (corners of
%  the 3D frequency space) of the primary wavelet pyramid.
%
%  CONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
%  transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
%  A is a 2D matrix corresponding to the reconstructed image
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%wavelet synthesis
A = waveletSynthesis(Q,config);

%Riesz synthesis
A = RieszSynthesis(A,config);

% Postfilter
A = RieszPostfilter(A, R, config);
end