function q = hardThresholding(q, threshold, noiseStd)
%HARDTHRESHOLDING perform hard thresholding for a set of coefficients
%
% Q = HARDTHRESHOLDING(Q, THRESHOLD, NOISESTD) performs hard-thresholding
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
% Q a matrix of hard-thresholded coefficients
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

    q = q.*(abs(q) > threshold*noiseStd);
end