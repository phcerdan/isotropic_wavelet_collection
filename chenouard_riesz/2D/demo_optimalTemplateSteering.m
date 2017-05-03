function [ang, Qmax] = demo_optimalTemplateSteering(A, numScales, order, template)
%DEMO_OPTIMALTEMPLATESTEERING steer coefficients to maximize the response of a template
%
%  [ANG, QMAX] = DEMO_OPTIMALTEMPLATESTEERING(A, NUMSCALES, ORDER, TEMPLATE) steer the
% Riesz-wavelet coefficients of order ORDER for the image A in order to maximize the
% response of the template TEMPLATE that is defines a linear combination
% of Riesz channels.
%
% --------------------------------------------------------------------------
% Input arguments:
%
% They are all optional.
%
% A: input image. If none is provided the method buildDefaultImage is
% called to build one.
%
% NUMSCALES: number of wavelet scales for the decomposition. Default is
% 3.
%
% ORDER: order of the Riesz transform. Default is 2.
%
% TEMPLATE: array specifying a linear combination of Riesz channels. The
% method finds at each point the angle that maximizes this linear
% combination. Default is 1 for the first Riesz channels and 0 for the
% other ones.
%
% --------------------------------------------------------------------------
%
% Output arguments:
%
% ANG: angles that yield the maximization of the template
% QMAX : Riesz-wavelet coefficients after optimal steering
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

%% create a default image
if nargin < 1,
    A = buildDefaultImage();
    figure; imagesc(A); axis image, axis off, colormap gray;
    title('original image')
end

%% default parameters
if nargin < 2,
    numScales = 3;
end
if nargin < 3,
    order = 2;
    display('using Riesz transform of order 2')
end
if nargin < 4,
    template = zeros(1, order +1);
    template(1) = 1;
    display('maximizing the response of the first channel')
else
    if (size(template, 1)~=1 && size(template, 2)~= (order+1))
        error('the template needs to be a vector of size (1, order+1)');
    end
end

%% perform a Riesz transform
% prepare the transform
rieszConfig = RieszConfig2D(size(A), order, numScales, 1);
% compute the Riesz-wavelet coefficients
QA = multiscaleRieszAnalysis(A, rieszConfig);

%%  optimal template steering
% check if template corresponds to a single channel
if (length(find(abs(template))) == 1)
    channel = find(abs(template));
    [ang, Qmax] =maxsteer(QA, rieszConfig, channel);
else
    [ang, Qmax] =maxsteerTemplate(QA, rieszConfig, template);
end

%% display angles at each scale
for j = 1:numScales,
    %display angles
    figure;
    imagesc(ang{j});
    colormap('hsv');
    axis off;
    axis image;
    title(sprintf('Template Steering - Angle at scale %d', j));
end

%% display template value after and before steering
for j = 1:numScales,
    %display angles
    figure;
    % compute the original template image
    tmp = zeros(size(QA{j}, 1), size(QA{j}, 2));
    for k = 1:rieszConfig.RieszChannels,
        tmp = tmp + template(k)*QA{j}(:,:,k);
    end
    subplot(1,2,1); imagesc(tmp); axis off; axis image;
    title(sprintf('Original template coefficients at scale %d', j));
    subplot(1,2,2); imagesc(Qmax{j}); axis off; axis image;
    title(sprintf('Steered template coefficients at scale %d', j));
    clear tmp
end

end

