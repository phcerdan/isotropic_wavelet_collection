% RIESZCONFIG class of objects characterizing 3D Riesz-wavelet transforms
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard?epfl.ch
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox


function config = RieszConfig(dims, RieszOrder, numScales, prepareFilters, downsampling, isotropicWaveletType, isotropicWaveletOmega)

    if ~exist('isotropicWaveletType', 'var')
        isotropicWaveletType = 'Simoncelli';
    end
    
    % ydc: adapted to new call
    if ~exist('isotropicWaveletOmega', 'var')
        if strcmp(isotropicWaveletType, 'omegaWav')
            isotropicWaveletOmega = 15/40;
        else
            isotropicWaveletOmega = NaN;
        end
    end
    % end ydc: adapted to new call

    config.downsampling=downsampling;
    config.dataType ='real';
    config.waveletType = 'isotropic';
    config.isotropicWaveletType = isotropicWaveletType;
    config.isotropicWaveletOmega = isotropicWaveletOmega;
    config.bsplineOrder = 1;
    config.prefilterType = 'Simoncelli';
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
        % ydc: Checked that it would work for ordeer = 0
        % if (RieszOrder <1 || (RieszOrder-floor(RieszOrder)~=0))
        %     error('The Riesz transform needs to be a positive integer');
        % end
        %end ydc
%         if (RieszOrder>2)
%             warning('MATLAB:paramAmbiguous', 'Riesz transform order superior to 2 require intensive memory usage');
%         end
        config.RieszOrder=RieszOrder;
    else
        config.RieszOrder=1;
    end
    if nargin>2,
         if (numScales <1 || (numScales-floor(numScales)~=0))
            error('The number of scales needs to be a positive integer');
         end
%         if (2^numScales >= config.size(1))
        if (2^numScales > config.size(1))
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
