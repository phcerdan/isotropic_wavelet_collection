% Enumeration of available prefilters

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