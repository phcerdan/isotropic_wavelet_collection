% RIESZCONFIG class of objects characterizing 2D Riesz-wavelet transforms
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Nicolas Chenouard. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

classdef RieszConfig2D,
    properties
        realData; % real valued data
        size; % size of the volume
        
        waveletType; % type of wavelet transform. See the WaveletType Enumeration
        isotropicWaveletType; % type of wavelet transform to use
        bsplineOrder; % order of the bspline wavelet transform (positive integer)
        numScales; % number of scales for the wavelet transform
        
        RieszOrder; % order of the Riesz transform
        RieszChannels; % number of Riesz channels
        RieszFilters; % filters for computing the Riesz transform
        %RieszOrders; % orders of the Riesz transform for each channel
        
        prefilterType; % prefiltering operation, see PrefilterType enumeration
        prefilter;
    end
    
    methods
        function config = RieszConfig2D(dims, RieszOrder, numScales, prepareFilters)
            config.realData = 1;
            config.waveletType = WaveletType.getDefaultValue();
            config.isotropicWaveletType = IsotropicWaveletType.getDefaultValue();
            config.bsplineOrder = 1;
            config.prefilterType = PrefilterType.getDefaultValue();
            if nargin>0
                if (dims(1)~=dims(2) || dims(1)<1)
                    error('The Riesz-wavelet transform works only for isotropic volumes for now')
                end
                config.size = dims;
            else
                config.size = zeros(1,1,1);
            end
            if nargin>1,
                if (RieszOrder <0 || (RieszOrder-floor(RieszOrder)~=0))
                    error('The Riesz transform needs to be a positive integer');
                end
                config.RieszOrder=RieszOrder;
            else
                config.RieszOrder=1;
            end
            if nargin>2,
                if (numScales <1 || (numScales-floor(numScales)~=0))
                    error('The number of scales needs to be a positive integer');
                end
                if (2^numScales >= config.size(1))
                    error('The number of scales is too large as compared to the image size');
                end
                config.numScales = numScales;
            else
                % set the number of scales such that the low-pass image is
                % at least of size 16x16
                config.numScales = max(floor(log(config.size(1)/16)/log(2)), 1);
            end
            if nargin>3 && prepareFilters,
                config = prepareTransform(config);
            end
        end
        
        function config = prepareTransform(config, dims)
            if nargin>1,
                if (dims(1)~=dims(2) || dims(1)<1)
                    error('The Riesz-wavelet transform works only for non-empty isotropic volumes for now');
                end
                config.size = dims;
            else
                if config.size(1)<1,
                    error('Specify the volume size before initializing the filters');
                end
            end
            config = RieszPrepareTransform2D(config);
        end
    end
end