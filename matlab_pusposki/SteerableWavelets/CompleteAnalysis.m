function [Q res] = CompleteAnalysis(A,num_scales,ha,hd,harmonics,prefilter)
%
% function [Q res] = CompleteAnalysis(A,num_scales,ha,hd,harmonics,prefilter)
%
%
% steerable wavelet analysis.
%
%
% INPUTS
% ------
%
% A             image to process
%
% num_scales    number of scales
%
% ha            approximation filter (function handle)
%
% hd            detail filter  (function handle)
%
% harmonics     vector of harmonics (corresponding to exp^j*harmonics*angle)
%
% prefilter     whether to prefilter the image (logical, defaults to false)
%
%
% OUTPUTS
% -------
%
% Q             cell array of wavelet coefficients per scale.
%               Q{i}.channels(j,:,:) is the array of coefficients in the jth
%               channel in scale i, with additional field
%               Q{end}.channels_approx for lowpass coefficients.
%               
% res           residue from prefiltering (empty if no prefiltering).
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



if ~exist('prefilter','var')
    prefilter = false;
end

% Initialization
Q = cell(num_scales,1);

% Multiscale Isotropic Wavelet Analysis
[Qi res] = MultiScaleIsoWaveAnalysis(A,num_scales,ha,hd,prefilter);

% Channel Analysis
for i=1:num_scales
      Q{i}.channels = channelAnalysis(Qi{i}.detail,harmonics);
end
 
% Channel Analysis (lowpass)
Q{end}.channels_approx = channelAnalysis(Qi{end}.approx,harmonics);
