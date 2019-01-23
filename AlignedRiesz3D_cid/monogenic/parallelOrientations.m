function [orientation1ZerosPadded,orientation2ZerosPadded,orientation3ZerosPadded] = parallelOrientations(J11,J22,J33,J12,J13,J23,idxMask,w,h,d)

orientation1=[];
orientation2=[];
orientation3=[];

J11_W=WorkerObjWrapper(J11);
J22_W=WorkerObjWrapper(J22);
J33_W=WorkerObjWrapper(J33);
J12_W=WorkerObjWrapper(J12);
J13_W=WorkerObjWrapper(J13);
J23_W=WorkerObjWrapper(J23);


%build cost matrix for each voxel
%YDC 2015/02/25
% parfor iterVox=1:size(idxMask,1),
parfor (iterVox=1:size(idxMask,1), matlabpool('SIZE'))
%FIN YDC 2015/02/25
        
    it=idxMask(iterVox);
    it2 = it -1;
    z = floor(it2/(w*h));
    x = floor((it2-z*w*h)/h);
    y = it2-z*w*h-x*h;
    x = x+1;
    y = y+1;
    z = z+1;

    j11 = J11_W.Value(y,x,z);
    j22 = J22_W.Value(y,x,z);
    j33 = J33_W.Value(y,x,z);
    j12 = J12_W.Value(y,x,z);
    j13 = J13_W.Value(y,x,z);
    j23 = J23_W.Value(y,x,z);

    % svd computation
    JMat = [j11 j12 j13; j12 j22 j23; j13 j23 j33];
    [U, ~] = princomp(JMat);
    orientation1=[orientation1; U(:,1)'];
    orientation2=[orientation2; U(:,2)'];
    orientation3=[orientation3; U(:,3)'];
end;

orientation1ZerosPadded=zeros(w*h*d,3);
orientation2ZerosPadded=zeros(w*h*d,3);
orientation3ZerosPadded=zeros(w*h*d,3);
for iterVox=1:size(idxMask,1),
    orientation1ZerosPadded(idxMask(iterVox),:) = orientation1(iterVox,:);
    orientation2ZerosPadded(idxMask(iterVox),:) = orientation2(iterVox,:);
    orientation3ZerosPadded(idxMask(iterVox),:) = orientation3(iterVox,:);
end;
end

