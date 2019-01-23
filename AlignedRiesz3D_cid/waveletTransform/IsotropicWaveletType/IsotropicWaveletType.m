classdef(Enumeration) IsotropicWaveletType
    enumeration
        Simoncelli
        Shannon
        Aldroubi
        Papadakis
        Meyer
    end
    methods (Static = true)
        function retVal = getDefaultValue()
            retVal = IsotropicWaveletType.Simoncelli;
        end
    end
end