classdef DenoisingTypes
%DENOISINGTYPES enumeration of the methods for denoising the
%high-pass residual band of the wavelet transform
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

 enumeration
        softThresholding
        hardThresholding
        WienerFiltering
        vectorialWienerFiltering
    end
    methods (Static = true)
        function retVal = getDefaultValue()
            retVal = DenoisingTypes.softThresholding;
        end
    end    
end

