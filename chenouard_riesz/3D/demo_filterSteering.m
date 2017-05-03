%Riesfunction demo_filterSteering()
%DEMO_FILTERSTEERING demonstrate the steering of 3D Riesz filters
%
% create movies of the impulse response of Riesz filters which are steered
% in many different directions by applying steering matrices to the
% filters.
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

% set up the order of the Riesz transform
rieszOrder = 2;
% set up the number of wavelet scales
numWaveletScales = 1;
% set up the size of the volume to analyze
volSize = [32, 32, 32];
% prepare the transform elements
config = RieszConfig(volSize, rieszOrder, numWaveletScales, 1); % create the Riesz transform configuration
% create a movie of rotation filters
viewRieszRotationMovies(config)