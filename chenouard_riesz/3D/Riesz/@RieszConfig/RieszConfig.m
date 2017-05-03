% RIESZCONFIG class of objects characterizing 3D Riesz-wavelet transforms
%
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

classdef RieszConfig,
    properties
        dataType; % data type: 'complex' or 'real'
        size; % size of the volume
        spatialDomainStorage; % store coefficients in spatial domain (1), or in Fourier domain (0)
        
        waveletType; % type of wavelet transform: 'isotropic' or 'spline'
        isotropicWaveletType; % type of wavelet transform to use
        bsplineOrder; % order of the bspline wavelet transform (positive integer)
        numScales; % number of scales for the wavelet transform
        
        RieszOrder; % order of the Riesz transform
        RieszChannels; % number of Riesz channels
        RieszFilters; % filters for computing the Riesz transform
        RieszOrders; % orders of the Riesz transform for each channel
        
        prefilterType; % prefiltering operation, see PrefilterType enumeration
        prefilter;
    end
    
    methods
        function config = RieszConfig(dims, RieszOrder, numScales, prepareFilters)
            config.dataType ='real';
            config.waveletType = 'isotropic';
            config.isotropicWaveletType = IsotropicWaveletType.getDefaultValue();
            config.bsplineOrder = 1;
            config.prefilterType = PrefilterType.getDefaultValue();
            config.spatialDomainStorage = 1;
            if nargin>0
                if (dims(1)~=dims(2) || dims(2)~=dims(3) || dims(1)<1)
                    error('The Riesz-wavelet transform works only for isotropic volumes for now')
                end
                config.size = dims;
            else
                config.size = zeros(1,1,1);
            end
            if nargin>1,
                if (RieszOrder <1 || (RieszOrder-floor(RieszOrder)~=0))
                    error('The Riesz transform needs to be a positive integer');
                end
                if (RieszOrder>10)
                    warning('MATLAB:paramAmbiguous', 'Riesz transform order superior to 2 require intensive memory usage');
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
                % at least of size 16x16x16
                config.numScales = max(floor(log(config.size(1)/16)/log(2)), 1);
            end
            if nargin>3 && prepareFilters,
               config = prepareTransform(config); 
            end
        end
        
        function config = prepareTransform(config, dims)
            if nargin>1,
                if (dims(1)~=dims(2) || dims(2)~=dims(3) || dims(1)<1)
                    error('The Riesz-wavelet transform works only for non-empty isotropic volumes for now');
                end
                config.size = dims;
            else
               if config.size(1)<1,
                  error('Specify the volume size before initializing the filters'); 
               end
            end
            config = RieszPrepareTransform(config);
        end
    end
end