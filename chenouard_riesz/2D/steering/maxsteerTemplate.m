function [ang, Qmax] = maxsteerTemplate(Q, rieszConfig, template)
%MAXSTEERTEMPLATE steer Riesz coefficients to maximize the response of a template
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q structure of Riesz-wavelet coefficients
%
% RIESZCONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% TEMPLATE coefficients for the linear combination of Riesz channels for which
% the response is to be maximized
%
% --------------------------------------------------------------------------
% Output arguments:
%
% ANG angles estimated pointwise in the wavelet bands. It consists in a cell
% of matrices. Each element of the cell corresponds to the matrix of angles
% for a wavelet band.
%
% QMAX Riesz-wavelet coefficient steered with respect to the angles in ANG
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

ang = cell(1,rieszConfig.numScales);
if nargout >1,
    Qmax = cell(1, rieszConfig.numScales+1);
    Qmax{rieszConfig.numScales+1} = Q{rieszConfig.numScales+1};
end
for i=1:rieszConfig.numScales,
    if nargout >1,
        [ang{i}, Qmax{i}] = RieszAngleTemplate(Q{i}, template, rieszConfig.RieszOrder);
    else
        ang{i} = RieszAngleTemplate(Q{i}, template, rieszConfig.RieszOrder);
    end
end