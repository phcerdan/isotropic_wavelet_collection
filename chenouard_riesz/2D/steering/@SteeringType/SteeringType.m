classdef SteeringType
    %STEERINGTYPE Types of Riesz coefficients steering techniques
    %
    % --------------------------------------------------------------------------
    %
    % Part of the Generalized Riesz-wavelet toolbox
    %
    % Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
    %
    % Version: Feb. 7, 2012
    
    enumeration
        none
        monogenic
        maxSteer
    end
    methods (Static = true)
        function retVal = getDefaultValue()
            retVal = SteeringType.monogenic;
        end
    end
end

