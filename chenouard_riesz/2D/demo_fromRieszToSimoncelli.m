function P = demo_fromRieszToSimoncelli(showFilters)
%DEMO_FROMRIESZTOSIMONCELLI demonstrate the construction of Simoncelli's
%pyramid from the Riesz-wavelet frame.
%
% DEMO_FROMRIESZTOSIMONCELLI() demonstrate the construction of Simoncelli's
% pyramid from the Riesz-wavelet frame by designing the proper generalization matrix P.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% They are all optional.
%
% SHOWFILTERS: 1 to display Riesz and Simoncelli's filters, 0 otherwise.
% Default is 1.
%
% --------------------------------------------------------------------------
% Output arguments:
%
%  P: the set of generalization matrices for each wavelet scale that correspond to the Simoncelli's pyramid.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if (nargin<1)
    showFilters = 1;
end

rieszConfig = RieszConfig2D([128 128], 7, 1, 1);
P = getSimoncelliFrame(rieszConfig);
display('transformation matrix from Riesz to Simoncelli:')
P{1}
if showFilters,
    display('showing Riesz filters')
    visualizeRieszFilters(rieszConfig, eye(rieszConfig.RieszChannels));
    display('press any key to show simoncelli filters');
    visualizeRieszFilters(rieszConfig, P{1});
end

end

