function rieszCoeffs = alignInAllComponents(rieszCoeffs, mask, sigma, alignScale)

% rieszCoeffs:  Struct containing (n (scales) + 1) 4D matrices.
% mask:         Mask where to compute the alignment (requires
%               alignSparse = true).
% sigma:        Sigma for the Gaussian kernel.
% alignScale:   0 (default): alignment computed indepentdently for
%               every scale.
%               N value: alignment computed in the N scale and
%               applied to the others.
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Yashin Dicente Cid, yashin.dicente@hevs.ch
%    University of Applied Sciences Western Switzerland
%    University of Geneva, Switzerland
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    Y. Dicente Cid, H. M?ller, A. Platon, P-A. Poletti, A. Depeursinge
% 	 "3-D Solid Texture Classification Using Locally-Oriented Wavelet Transforms",
%    IEEE Transactions on Image Processing, 2017
%
% -------------------------------------------------------------------------


    if ~exist('alignScale', 'var')
        alignScale = 0;
    end

    numScales = size(rieszCoeffs,2) - 1;

    if (alignScale < 0) || (alignScale > numScales)
        warning('Value for alignScale not valid.');
        return;
    end

    % Not working with pyramid!!
    if numScales > 1 && ~isequal(size(rieszCoeffs{1}), size(rieszCoeffs{2}))
        warning('alignInAllDirections does not work with pyramid structure');
        return;
    end

    imageSize = size(rieszCoeffs{1});
    imageSize = imageSize(2:end);

    idxsMask = find(mask);


    if alignScale == 0
        scalesToComputeAlignment = 1:numScales;
    else
        scalesToComputeAlignment = alignScale;
    end


    for scale = scalesToComputeAlignment
        rieszVects = rieszCoeffs{scale}(:,:);

        r = shiftdim(rieszVects,-1);
        rt = permute(r,[2,1,3]);

        J = bsxfun(@times, r , rt);

        % Gaussian:
        sizeJ = size(J);

        K = 4;
        smoothingKernel = createPaddedGaussianKernel(imageSize(1), imageSize(2), imageSize(3), sigma, K);
        smoothingKernel = fftn(smoothingKernel);

        for iJ = 1:sizeJ(1)
            for jJ = 1:sizeJ(2)
                Jij = squeeze(J(iJ,jJ,:));
                Jij = reshape(Jij, imageSize);


                %convolve in Fourier domain
                Jij = fftn(Jij);
                Jij = Jij.*smoothingKernel;
                Jij = real(fftshift(ifftn(Jij)));

                J(iJ,jJ,:) = Jij(:);
            end
        end

        if alignScale == 0
            for idx_vect = idxsMask'
                [rotation, ~] = svd(J(:,:,idx_vect));
                rieszVects(:,idx_vect) = rotation' * rieszVects(:,idx_vect);
            end
            rieszCoeffs{scale}(:,idxsMask) = rieszVects;
        else
            for idx_vect = idxsMask'
                [rotation, ~] = svd(J(:,:,idx_vect));
                for subScale = 1:numScales
                    rieszCoeffs{subScale}(:,idx_vect) = rotation' * rieszCoeffs{subScale}(:,idx_vect);
                end
            end
        end

    end

end
