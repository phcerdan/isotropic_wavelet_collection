function P = getSimoncelliFrame(rieszConfig)
%GETSIMONCELLIFRAME build the generalization matrix corresponding
%to the Simoncelli's frame
%
% P = GETSIMONCELLIFRAME(RIESZCONFIG) build the generalization matrix P
% that corresponds to a Simoncelli's frame with order that is
% specified in RIESZCONFIG. It is obtained by rotating the first Riesz basis
% function and renormalizing it. Rotation angles are equispaced.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% RIESZCONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% --------------------------------------------------------------------------
% Output arguments:
%
% P a generalization matrix that generates a Simoncelli's frame
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

P = cell(1, rieszConfig.numScales+1);
N = rieszConfig.RieszOrder;
K = rieszConfig.RieszChannels;
Ps = zeros(K,K);
for i = 1:K,
    theta = (i-1)*pi/K;
    R = [cos(theta), sin(theta); -sin(theta) cos(theta)];
    rotationMatrix = computeRotationMatrixForRiesz(R, N);
    Ps(:,i) = rotationMatrix(:,1);
end
Ps = Ps* factorial(N)*2^N/(sqrt(K* factorial(2*N)));
for k = 1:length(P)
    P{k} = Ps;
end