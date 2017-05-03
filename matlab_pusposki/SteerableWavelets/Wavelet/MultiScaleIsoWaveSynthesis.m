function A = MultiScaleIsoWaveSynthesis(Q,ha,hd,res)
%
% function A = MultiScaleIsoWaveSynthesis(Q,ha,hd,res)
%
%
% multiscale isotropic wavelet synthesis.
%
%
% INPUTS
% ------
%
% Q             cell array of wavelet coefficients per scale; Q{i} has fields
%               .approx and .detail for approximation and detail coefficients
%               at the i-th scale Q{i}. The .approx fields from all but the
%               last scale are not used.
%
% ha            approximation filter (function handle)
%
% hd            detail filter  (function handle)
%               
% res           residue from prefiltering
%
%
% OUTPUTS
% -------
%
% A             resynthesized image
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

num_scales = length(Q);

approx = Q{num_scales}.approx;

for i = num_scales:-1:1
   
    % upsampling
    approxup = zeros(2*size(approx)); 
    approxup(1:2:end,1:2:end) = 4*approx; % 4x 1/4th of the coeffs 
    
    detailup = Q{i}.detail;
    
    approx = isoWaveSynthesis(approxup,detailup,ha,hd);
    
end

A = approx;

if ~isempty(res)
    A = Postfilter(A,res,@(r) ha(r/2));
end
