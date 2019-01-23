function RieszFilterSteeringDemo()
%RIESZFILTERSTEERINGDEMO demonstrate the steering of 3D Riesz filters
%
% create movies of the impulse response of Riesz filters which are steered
% in many different directions by applying steering matrices to the
% filters.
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

rieszOrder = 2; % order of the Riesz transform
numWaveletScales = 1;
config = RieszConfig([32 32 32], rieszOrder, numWaveletScales, 1, 1); % create the Riesz transform configuration
viewRieszRotationMovies(config)