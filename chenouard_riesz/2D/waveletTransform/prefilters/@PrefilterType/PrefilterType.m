classdef(Enumeration) PrefilterType
    %PREFILTERTYPE enumeration of the available prefilters
    %
    % --------------------------------------------------------------------------
    %
    % Part of the Generalized Riesz-wavelet toolbox
    %
    % Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
    %
    % Version: Feb. 7, 2012
    
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