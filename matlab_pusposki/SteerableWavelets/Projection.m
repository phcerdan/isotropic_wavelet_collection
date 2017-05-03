function Qu = Projection(Q,U)
%
% function Qu = Projection(Q,U)
%
%
% project wavelet channels using construction matrix U for a tight wavelet
% frame; use U' for inverse. 
%
%
% INPUTS
% ------
%
% Q             cell array of wavelet coefficients per scale
%
% U             construction matrix
%
% OUTPUT
% ------
%
% Q             cell array of projected wavelet coefficients per scale
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

num_scales = length(Q);
sU = size(U);

Qu = cell(num_scales,1);

if isempty(U)
    return;
end

for i =1:num_scales
    
    sC = size(Q{i}.channels);
    % reshape channels to have each channel vectorized as a row in chn
    chn = reshape(Q{i}.channels,[sC(1),sC(2)*sC(3)]);
    
    % apply matrix U
    chn = U*chn;
    
    % reshape back to original shape and save
    Qu{i}.channels = reshape(chn,[size(U,1),sC(2:end)]);
end

% project lowpass
sC = size(Q{end}.channels_approx);
chn = reshape(Q{end}.channels_approx,[sC(1),sC(2)*sC(3)]);
chn = U*chn;
Qu{end}.channels_approx = reshape(chn,[size(U,1),sC(2:end)]);
