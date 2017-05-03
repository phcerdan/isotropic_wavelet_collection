function [ang, Qmax] = maxsteer(Q, rieszConfig, channel)
% MAXSTEER steer Riesz coefficients to maximize the response in a given channel
%
% --------------------------------------------------------------------------
% Input arguments:
%
% Q structure of Riesz-wavelet coefficients
%
% CONFIG RieszConfig2D object that specifies the primary Riesz-wavelet
% transform.
%
% CHANNEL Riesz channel for which the response is to be maximized
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
        [ang{i}, Qmax{i}] = RieszAngle(Q{i}, rieszConfig.RieszOrder, channel);
    else
        ang{i} = RieszAngle(Q{i}, rieszConfig.RieszOrder, channel);
    end
end

end
