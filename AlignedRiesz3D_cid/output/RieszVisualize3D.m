function [] = RieszVisualize3D(Q, scales, bands)
%RIESZVISUALIZE3D view the 3D Riesz-wavelet coefficients as a volume
%
% [] = RieszVisualize3D(Q, scales, bands)  view the 3D Riesz-wavelet coefficients
% at scales 'scales' and for Riesz bands 'bands'as a volume.
%
% [] = RieszVisualize3D(Q, scales)  view the 3D Riesz-wavelet coefficients
% at scales 'scales' as a volume.
%
% [] = RieszVisualize3D(Q)  view the 3D Riesz-wavelet coefficients
% at every scale as a volume
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

if nargin<2,
    scales = 1:length(Q)-1;
end

if nargin<3.
   bands = 1:size(Q{1}, 1); 
end

for i = scales,
    for j = bands,
        figure; vol3d('cdata', real(squeeze(Q{i}(j, :, :, :)).^2));
        view(-37.5, 30);
        axis image;
        axis vis3d;
        title(sprintf('Riesz-wavelet coefficients at scale %d for band %d', i, j));
        alphamap('rampup');
        alphamap(0.15*alphamap);
    end
end
end