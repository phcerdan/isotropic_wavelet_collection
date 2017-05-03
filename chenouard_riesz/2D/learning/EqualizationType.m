classdef EqualizationType
    %EQUALIZATIONTYPE Enumeration of methods for equalizing Riesz channels
    % --------------------------------------------------------------------------
    %
    % Part of the Generalized Riesz-wavelet toolbox
    %
    % Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
    %
    % Version: Feb. 7, 2012
    
    enumeration
        Equalize
        BandByBand
        None
    end
    methods (Static = true)
        function retVal = getDefaultValue()
            retVal = EqualizationType.None;
        end
    end
end

