function [orientation1ZerosPadded,orientation2ZerosPadded,orientation3ZerosPadded] = parallelOrientationsNoParForOptimized(J11,J22,J33,J12,J13,J23,idxMask)

    %build cost matrix for each voxel
    tic;

    numIdxMask = numel(idxMask);

    orientation1 = zeros(numIdxMask, 3);
    orientation2 = zeros(numIdxMask, 3);
    orientation3 = zeros(numIdxMask, 3);

    
    JMat = [reshape(J11(idxMask), [1,1,numIdxMask]) reshape(J12(idxMask), [1,1,numIdxMask]) reshape(J13(idxMask), [1,1,numIdxMask]); ...
            reshape(J12(idxMask), [1,1,numIdxMask]) reshape(J22(idxMask), [1,1,numIdxMask]) reshape(J23(idxMask), [1,1,numIdxMask]); ...
            reshape(J13(idxMask), [1,1,numIdxMask]) reshape(J23(idxMask), [1,1,numIdxMask]) reshape(J33(idxMask), [1,1,numIdxMask])];
        
    for iterVox = 1:numIdxMask
        
        [U, ~] = svd(JMat(:,:,iterVox));

        orientation1(iterVox,:) = U(:,1)';
        orientation2(iterVox,:) = U(:,2)';
        orientation3(iterVox,:) = U(:,3)';
        
    end
    toc;
    
    numElements = numel(J11);
    
    orientation1ZerosPadded = zeros(numElements,3);
    orientation2ZerosPadded = zeros(numElements,3);
    orientation3ZerosPadded = zeros(numElements,3);
    
    orientation1ZerosPadded(idxMask,:) = orientation1(1:numIdxMask,:);
    orientation2ZerosPadded(idxMask,:) = orientation2(1:numIdxMask,:);
    orientation3ZerosPadded(idxMask,:) = orientation3(1:numIdxMask,:);
    
end

