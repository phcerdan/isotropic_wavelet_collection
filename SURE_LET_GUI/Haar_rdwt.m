function [yL,yH] = Haar_rdwt (x,it, extension_case,type_of_symmetry)

[M,N] = size(x);

%% periodic boundary condition
if (strcmp(extension_case, 'periodic') == 1)
yH = zeros(M,N,3*it); 
candidate = x;
for level = 1:it
    shift = 2^(level-1);
% LL
xx = [candidate(:,shift+1:end) candidate(:,1:shift)];

D1 = candidate+xx; DD = [D1(shift+1:end,:); D1(1:shift,:)];
D = D1+DD; DA0 = D*0.5;
% LH
D = D1-DD; yH(:,:,(level-1)*3+1) = D*0.5;
% HL
D2 = candidate-xx; DD = [D2(shift+1:end,:); D2(1:shift,:)]; 
D = D2+DD; yH(:,:,(level-1)*3+2) = D*0.5;
% HH
D = D2-DD; yH(:,:,(level-1)*3+3) = D*0.5;

candidate = DA0;
end

yL = DA0;     
    
end



%% symmeteric boundary condition
if (strcmp(extension_case, 'symmetric') == 1)
    
    if (strcmp(type_of_symmetry, 'half_point') == 1)

yH = zeros(M,N,3*it); 
candidate = x;
for level = 1:it
    shift = 2^(level-1);
% LL
xx = [candidate(:,shift+1:end) candidate(:,end:-1:(end-shift+1))];

D1 = candidate+xx; DD = [D1(shift+1:end,:); D1(end:-1:(end-shift+1),:)];
D = D1+DD; DA0 = D*0.5;
% LH
D = D1-DD; yH(:,:,(level-1)*3+1) = D*0.5;
% HL
D2 = candidate-xx; DD = [D2(shift+1:end,:); D2(end:-1:(end-shift+1),:)]; 
D = D2+DD; yH(:,:,(level-1)*3+2) = D*0.5;
% HH
D = D2-DD; yH(:,:,(level-1)*3+3) = D*0.5;

candidate = DA0;
end
yL = DA0;     

    end
    
    
    if (strcmp(type_of_symmetry, 'whole_point') == 1)

yH = zeros(M,N,3*it); 
candidate = x;
for level = 1:it
    shift = 2^(level-1);
% LL
xx = [candidate(:,shift+1:end) candidate(:,end-1:-1:(end-shift))];

D1 = candidate+xx; DD = [D1(shift+1:end,:); D1(end-1:-1:(end-shift),:)];
D = D1+DD; DA0 = D*0.5;
% LH
D = D1-DD; yH(:,:,(level-1)*3+1) = D*0.5;
% HL
D2 = candidate-xx; DD = [D2(shift+1:end,:); D2(end-1:-1:(end-shift),:)]; 
D = D2+DD; yH(:,:,(level-1)*3+2) = D*0.5;
% HH
D = D2-DD; yH(:,:,(level-1)*3+3) = D*0.5;

candidate = DA0;
end
yL = DA0;     

    end
    
end

return;


