function rieszCoeffs = alignIn3PureComponents(rieszCoeffs, dim3D, J, pyramid, sigma, mask, config, alignSparse)

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

    % Only working for 3D

    scales = 1:J;
    disp('computing orientations: ');

    idxMask = find(mask);
    idxMaskStruct{1} = idxMask;
    orientations = monogenicAnalysisSparseNoParFor(rieszCoeffs, config, 1, sigma, idxMaskStruct, 1);

    [c_x, c_y, c_z] = ind2sub(dim3D, idxMask);
    coordinates = [c_x; c_y; c_z];


    n = numel(idxMask);
    r = repmat(idxMask, 3, 1);
    c = [ones(n, 1); 2*ones(n,1); 3*ones(n,1)];
    numCoeffs = prod(dim3D);
    orientations{1}.coordinates = sparse(r, c, reshape(coordinates, 3*n, 1), numCoeffs, 3);


    orientationsReplicated = cell(1, length(scales));

    sizeCurrentScale = dim3D;
    for j = scales,
        orientationsReplicated{1,j}=orientations{1};

        if pyramid,

            matIdx = reshape(1:prod(sizeCurrentScale),sizeCurrentScale);
            matSubIdx = matIdx(1:2:end,1:2:end, 1:2:end);
            nextScaleIdx = matSubIdx(:);

            orientations{1}.u1 = orientations{1}.u1(nextScaleIdx,:);
            orientations{1}.u2 = orientations{1}.u2(nextScaleIdx,:);
            orientations{1}.u3 = orientations{1}.u3(nextScaleIdx,:);

            % Variables missed:
            orientations{1}.coordinates = orientations{1}.coordinates(nextScaleIdx,:);

            sizeCurrentScale = sizeCurrentScale/2;

        end;
    end;
    disp('aligning riesz coeffs');

    rieszCoeffs = rotate(rieszCoeffs, config, orientationsReplicated, scales, 1, alignSparse);
end
