function A = CompleteSynthesis(Q,ha,hd,harmonics,res)
%
% function A = CompleteSynthesis(Q,ha,hd,harmonics,res)
%
%
% steerable wavelet synthesis.
%
%
% INPUTS
% ------
%
% Q             cell array of wavelet coefficients per scale
%
% ha            approximation filter (function handle)
%
% hd            detail filter  (function handle)
%
% harmonics     vector of harmonics (corresponding to exp^j*harmonics*angle)
%
% res           residue from prefiltering (optional)
%
%
% OUTPUTS
% -------
%
% A             synthesized image
%
%
% REFERENCE
% ---------
% You are free to use this software for research purposes, but you should 
% not redistribute it without our consent.
%
% In addition, we expect you to include the following citations:
%
% Z. Puspoki and M. Unser, "Template-Free Wavelet-Based Detection of Local
% Symmetries", IEEE Transactions on Image Processing, vol. 24, no. 10, 
% pp. 3009-3018, October 2015
% M. Unser and N. Chenouard, "A Unifying Parametric Framework for 2D Steerable
% Wavelet Transforms", SIAM Journal on Imaging Sciences, vol. 6, no. 1, 
% pp. 102-135, 2013. 
%
%
% AUTHOR
% ------
%
% Z. Puspoki (zsuzsanna.puspoki@epfl.ch)
%
% Biomedical Imaging Group
% Ecole Polytechnique Federale de Lausanne (EPFL)


if ~exist('res','var')
    res = [];
end

% Initialization
num_scales = length(Q);
Qi = cell(num_scales,1);


% Computation of isotropic wavelt coefficients
for i=1:num_scales
      Qi{i}.detail = channelSynthesis(Q{i}.channels,harmonics);
end

% Computation of isotropic wavelt coefficients (lowpass)
Qi{end}.approx = channelSynthesis(Q{end}.channels_approx,harmonics);

A = MultiScaleIsoWaveSynthesis(Qi,ha,hd,res);
