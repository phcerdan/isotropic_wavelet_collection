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


clear all
close all
clc

addpath U
addpath Wavelet
addpath Wavelet/Radial


%% Initialization 

imfile   = 'cameraman.png';  % image to process
A = double(imread(imfile));
sA = size(A);

htype = 'Simoncelli'; % isotropic wavelet type (string), one of 'Simoncelli', 'Meyer', 'Papadakis' or 'Shannon'

Utype = 'Prolate';  %steerable construction type (string). Available constructions are 
%               'Gradient', 'Monogenic', 'Hessian', 'CircularHarmonic',
%               'Prolate', 'ProlateUniSided', 'Slepian', 'RieszEven',
%               'RieszOdd', 'RieszComplete', 'SimoncelliEven', 'SimoncelliOdd',
%               'SimoncelliComplete', 'UserDefined'.

Uparams.harmonics = -4:2:4; % For 'RieszEven', 'RieszOdd', 'RieszComplete', 'SimoncelliEven', 'SimoncelliOdd', 
                            %'SimoncelliComplete' - wavelet order
                            
Uparams.B = .1;     % For 'Slepian' - bandwidth 
num_scales = 4;     % number of scales
steerangle = pi/3;  % angle by which to steer the wavelet (optional, defaults to 0)

equiangular = false; % whether to construct equiangular wavelets from the first channel of Utype
Mequiangular = 9;   % number of equiangular channels

% init wavelet transform
[hafun,hdfun,hfun,U,harmonics] = Init(htype,Utype,Uparams);

if equiangular
    [U,harmonics] = EquiAng(harmonics,U(1,:),Mequiangular); % Equiangular design
end


% plot wavelets

for j=1:size(U,1)
    PlotWavelet(hfun,U(j,:),harmonics,steerangle);
    suptitle(sprintf('channel %d',j));
end
PlotWaveletScales(hafun,hdfun,hfun,U,harmonics,sA,num_scales,steerangle);
