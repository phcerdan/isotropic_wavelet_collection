function [] = visualizeRieszFilters(config)
% VISUALIZERIESZFILTERS view the Riesz filters
%
%  visualizeRieszFilters(config, viewType) view the Riesz filters for the
%  configuration object config.
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

for i=1:config.RieszChannels
    figure; vol3d('cdata', abs(fftshift(config.RieszFilters{i})));
    view(-37.5, 30);
    axis image;
    axis vis3d;
    title(sprintf('Modulus of the frequency response of the filter for channel %d', i));
    alphamap('rampup');
    alphamap(0.05*alphamap);
end