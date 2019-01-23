function [orientation1ZerosPadded,orientation2ZerosPadded,orientation3ZerosPadded] = parallelOrientationsNoParFor(J11,J22,J33,J12,J13,J23,idxMask,w,h,d)

orientation1=[];
orientation2=[];
orientation3=[];

%build cost matrix for each voxel
tic;
JMat=[];

% gJ11=gpuArray(J11);
% gJ22=gpuArray(J22);
% gJ33=gpuArray(J33);
% gJ12=gpuArray(J12);
% gJ13=gpuArray(J13);
% gJ23=gpuArray(J23);
for iterVox=1:size(idxMask,1),
        
    it=idxMask(iterVox);
    it2 = it -1;
    z = floor(it2/(w*h));
    x = floor((it2-z*w*h)/h);
    y = it2-z*w*h-x*h;
    x = x+1;
    y = y+1;
    z = z+1;

    j11 = J11(y,x,z);
    j22 = J22(y,x,z);
    j33 = J33(y,x,z);
    j12 = J12(y,x,z);
    j13 = J13(y,x,z);
    j23 = J23(y,x,z);

    % svd computation
    
    JMat(iterVox).a = [j11 j12 j13; j12 j22 j23; j13 j23 j33];
%     JMat_gpu=gpuArray(JMat);
%     [U, ~] = svd(JMat_gpu);

    [U, ~] = svd(JMat(iterVox).a);
%     disp('SVD');
% 
    orientation1=[orientation1; U(:,1)'];
    orientation2=[orientation2; U(:,2)'];
    orientation3=[orientation3; U(:,3)'];
end;
% test = arrayfun(@(x) svd(x.a), JMat, 'UniformOutput', false);
toc;
orientation1ZerosPadded=zeros(w*h*d,3);
orientation2ZerosPadded=zeros(w*h*d,3);
orientation3ZerosPadded=zeros(w*h*d,3);
for iterVox=1:size(idxMask,1),
%     orientation1ZerosPadded(idxMask(iterVox),:) = gather(orientation1(iterVox,:));
%     orientation2ZerosPadded(idxMask(iterVox),:) = gather(orientation2(iterVox,:));
%     orientation3ZerosPadded(idxMask(iterVox),:) = gather(orientation3(iterVox,:));
    orientation1ZerosPadded(idxMask(iterVox),:) = orientation1(iterVox,:);
    orientation2ZerosPadded(idxMask(iterVox),:) = orientation2(iterVox,:);
    orientation3ZerosPadded(idxMask(iterVox),:) = orientation3(iterVox,:);    
end;
end

