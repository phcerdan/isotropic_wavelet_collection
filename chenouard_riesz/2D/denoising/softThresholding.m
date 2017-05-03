function q = softThresholding(q, threshold, noiseStd)
%SOFTTHRESHOLDING perform hard thresholding for a set of coefficients
%
% Q = SOFTTHRESHOLDING(Q, THRESHOLD, NOISESTD) performs soft-thresholding
% of the matrix of coefficients Q with the threshold THRESHOLD that is adapted
% to the noise standard deviation NOISESTD
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q a matrix of coefficients to process
%
% THRESHOLD threshold
%
% NOISESTD standard deviation of the noise corrupting Q
%
% --------------------------------------------------------------------------
% Output arguments:
%
% Q a matrix of soft-thresholded coefficients
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

mask = (abs(q) > threshold*noiseStd);
q = (q - threshold*noiseStd*sign(q)).*mask;
end