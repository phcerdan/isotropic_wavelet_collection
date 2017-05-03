function [Q,res] = MultiScaleIsoWaveAnalysis(A,num_scales,ha,hd,prefilter)
%
% function [Q,res] = MultiScaleIsoWaveAnalysis(A,num_scales,ha,hd,prefilter)
%
%
% multiscale isotropic wavelet analysis.
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
% prefilter     whether to prefilter the image (logical, defaults to false)
%
%
% OUTPUTS
% -------
%
% Q             cell array of wavelet coefficients per scale; Q{i} has fields
%               .approx and .detail for approximation and detail coefficients
%               at the i-th scale Q{i}.
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

% prefilter
if prefilter
    [A,res] = Prefilter(A,@(r) ha(r/2));
else
    res = [];
end

% Wavelet coefficients for the first scale:
[Q{1}.approx Q{1}.detail] = isoWaveAnalysis(A,ha,hd);
Q{1}.approx = Q{1}.approx(1:2:end,1:2:end); %downsampling

% Wavelet coefficients for the remaining scales
for i=2:num_scales
    
      [Q{i}.approx Q{i}.detail] = isoWaveAnalysis(Q{i-1}.approx,ha,hd);
      Q{i}.approx = Q{i}.approx(1:2:end,1:2:end); %downsampling
      
end
