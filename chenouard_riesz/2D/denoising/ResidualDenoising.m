classdef ResidualDenoising
    %RESIDUALDENOISING enumeration of the avalaible methods for denoising
    % the high-pass residual coefficients from the primary wavelet pyramid
    % --------------------------------------------------------------------------
    %
    % Part of the Generalized Riesz-wavelet toolbox
    %
    % Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
    %
    % Version: Feb. 7, 2012
    
    enumeration
        zeros
        sameAsBands
        none
    end
    methods (Static = true)
        function retVal = getDefaultValue()
            retVal = ResidualDenoising.zeros;
        end
    end
end

