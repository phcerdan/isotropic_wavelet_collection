function [ang coherency] = monogenicAnalysis(A, J, sigma, full)
%MONOGENICANALYSIS perform the multiscale monogenic analysis of an image
%
% --------------------------------------------------------------------------
% Input arguments:
%
% A an image to analyze
%
% J the number of scales for the primary wavelet transform
%
% SIGMA regularization parameter. It is the standard deviation of the regularizing
% Gaussian kernel. Optional. Default is 1.5;
%
% FULL Specifies if angles should be restricted to values in [-pi/2,pi/2].
% Optional. Default is 0.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% ANG angles estimated pointwise in the wavelet bands. It consists in a cell
% of matrices. Each element of the cell corresponds to the matrix of angles
% for a wavelet band.
%
% COHERENCY coherency values estimated pointwise in the wavelet bands.
% It consists in a cell of matrices. Each element of the cell corresponds to
% the matrix of coherency values for a wavelet band.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if nargin < 3
    sigma = 1.5;
end
if nargin < 4,
    full = 0;
end

%% configure the Riesz transform of order 1
rieszConfig = RieszConfig2D(size(A), 1, J, 1);

% compute the Riesz-wavelet coefficients
QA = multiscaleRieszAnalysis(A, rieszConfig);

%% monogenic analysis
[ang coherency] = monogenicAnalysisOfRieszCoeff(QA, rieszConfig, sigma, full, A);