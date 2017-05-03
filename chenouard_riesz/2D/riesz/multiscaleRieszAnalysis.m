function [Q residual] = multiscaleRieszAnalysis(A, config)
%MULTISCALERIESZANALYSIS performs the Riesz-wavelet decomposition of
% high order for a 2D image
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A image to analyze
%
% CONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% Q structure containing the Riesz-wavelet coefficients. It consists in a
% cell of matrices. Each element of the cell corresponds to a wavelet
% scale. Each matrix is a 3D matrix whose 3rd dimension corresponds to
% Riesz channels.
%
% RESIDUAL high-pass residual band for the primary wavelet pyramid
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% prefilter images
[PA residual] = RieszPrefilter(A,config);
%% Apply the multiscale Riesz transform
Q = RieszAnalysis(PA,config);
%% apply wavelet decomposition
Q = waveletAnalysis(Q, config);
end