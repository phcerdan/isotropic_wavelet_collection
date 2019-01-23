function [] = visualizeRieszFilters(config)
% VISUALIZERIESZFILTERS view the Riesz filters
%
%  visualizeRieszFilters(config, viewType) view the Riesz filters for the
%  configuration object config.
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

for i=1:config.RieszChannels
    figure; vol3d('cdata', abs(fftshift(config.RieszFilters{i})));
    view(-37.5, 30);
    axis image;
    axis vis3d;
%     set(gca, 'box', 'off');
    set(gca, 'xtick', [], 'ytick', [], 'ztick', []);
%     title(sprintf('Modulus of the frequency response of the filter for channel %d', i));
    alphamap('rampup');
    alphamap(0.05*alphamap);
end