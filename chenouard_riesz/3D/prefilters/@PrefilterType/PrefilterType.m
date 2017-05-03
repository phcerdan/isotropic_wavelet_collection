% Enumeration of available prefilters
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard@gmail.com
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets in practice",
%   IEEE Transactions on Image Processing,
%   Vol. 21, Num. 11, pp 4522--4533, Nov 2012
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.3, 18.11.2012
%
%  Riesz-3D-light toolbox

classdef(Enumeration) PrefilterType
    enumeration
        Simoncelli
        Shannon
        None
    end
    methods (Static = true)
        function retVal = getDefaultValue()
            retVal = PrefilterType.Simoncelli;
        end
    end
end