function  [diagonal_element_H_1,diagonal_element_H_2, diagonal_element_H_3, ...
    diagonal_element_L_1,diagonal_element_L_2,diagonal_element_L_3]  = ...
    Diagonal_element   ...
    (x,H_Reg1,H_Reg2,H_Reg3,H_Reg4, it, matrix_7,matrix_8,      extension_case,type_of_symmetry,option)

%% periodic boundary condition
if (strcmp(extension_case, 'periodic') == 1) 
[M,N] = size(x); a = 1/(M*N);
diagonal_element_H_1 = zeros(1,3*it); diagonal_element_H_2 = zeros(1,3*it); diagonal_element_H_3 = zeros(1,3*it); 
HH_1 = real(H_Reg1.*conj(H_Reg4)); HH_2 = real(H_Reg2.*conj(H_Reg4)); HH_3 = real(H_Reg3.*conj(H_Reg4)); 
D1 = ones(M,N);


for level = 1:it
    shift = 2^(level-1);
% LH    
d = 1/2*[-1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[1 zeros(1,shift-1) 1 zeros(1,shift-1)]; 
    D = fft2(d,M,N); DD = real(D1.*D.*conj(D));
    candidate = (1/4)^(level)*(HH_1.*DD); diagonal_element_H_1((level-1)*3+1) = a*sum(candidate(:));
    candidate = (1/4)^(level)*(HH_2.*DD); diagonal_element_H_2((level-1)*3+1) = a*sum(candidate(:));
    candidate = (1/4)^(level)*(HH_3.*DD); diagonal_element_H_3((level-1)*3+1) = a*sum(candidate(:));
% HL
    diagonal_element_H_1((level-1)*3+2) = diagonal_element_H_1((level-1)*3+1);
    diagonal_element_H_2((level-1)*3+2) = diagonal_element_H_2((level-1)*3+1);
    diagonal_element_H_3((level-1)*3+2) = diagonal_element_H_3((level-1)*3+1);
% HH
d = 1/2*[-1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[-1 zeros(1,shift-1) 1 zeros(1,shift-1)]; 
    D = fft2(d,M,N); DD = real(D1.*D.*conj(D));
    candidate = (1/4)^level*(HH_1.*DD); diagonal_element_H_1((level-1)*3+3) = a*sum(candidate(:));
    candidate = (1/4)^level*(HH_2.*DD); diagonal_element_H_2((level-1)*3+3) = a*sum(candidate(:));
    candidate = (1/4)^level*(HH_3.*DD); diagonal_element_H_3((level-1)*3+3) = a*sum(candidate(:));

    if level == it
        % LL of the last scale
        shift = 2^(level-1);
d = 1/2*[1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[1 zeros(1,shift-1) 1 zeros(1,shift-1)]; 
    D = fft2(d,M,N); DD = real(D1.*D.*conj(D));
    candidate = (1/4)^level*(HH_1.*DD); diagonal_element_L_1 = a*sum(candidate(:));
    candidate = (1/4)^level*(HH_2.*DD); diagonal_element_L_2 = a*sum(candidate(:));
    candidate = (1/4)^level*(HH_3.*DD); diagonal_element_L_3 = a*sum(candidate(:));
    end
    
% D_L of the current level, used for the next level (iteration), while D1 is the previous ones    
d_L = 1/2*[1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[1 zeros(1,shift-1) 1 zeros(1,shift-1)]; 
    D_L = fft2(d_L,M,N); D_L = real(D_L.*conj(D_L)); D1 = real(D_L.*D1);

    
end

end


%% symmetric boundary condition
if (strcmp(extension_case, 'symmetric') == 1) 
    
    
%% half-point symmetry
    if (strcmp(type_of_symmetry, 'half_point') == 1)
        
[M,N] = size(x); 
diagonal_element_H_1 = zeros(M*N,3*it); diagonal_element_H_2 = zeros(M*N,3*it); diagonal_element_H_3 = zeros(M*N,3*it); 
HH_1 = real(H_Reg1.*conj(H_Reg4)); HH_2 = real(H_Reg2.*conj(H_Reg4)); HH_3 = real(H_Reg3.*conj(H_Reg4)); 

switch option
    case 'DFT'
        hh_1 = real(ifft2(HH_1));  hhh_1 = hh_1(1:M+1,1:N+1);
        hh_2 = real(ifft2(HH_2));  hhh_2 = hh_2(1:M+1,1:N+1);
        hh_3 = real(ifft2(HH_3));  hhh_3 = hh_3(1:M+1,1:N+1);
    case 'DCT'
        hhh_1 = matrix_7*HH_1*matrix_8'; hhh_2 = matrix_7*HH_2*matrix_8'; 
        hhh_3 = matrix_7*HH_3*matrix_8'; 
end


for level = 1:it

%%%%%%%%%%%%%%%%%% first level %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
[LH_1, HL_1, HH_1, LL_1] = diagonal_any_size  (hhh_1,'half_point',level);
[LH_2, HL_2, HH_2, LL_2] = diagonal_any_size  (hhh_2,'half_point',level);
[LH_3, HL_3, HH_3, LL_3] = diagonal_any_size  (hhh_3,'half_point',level);

a = (1/16)^level; LH_1 = a*LH_1; HL_1 = a*HL_1; HH_1 = a*HH_1; LL_1 = a*LL_1; 
LH_2 = a*LH_2; HL_2 = a*HL_2; HH_2 = a*HH_2; LL_2 = a*LL_2; 
LH_3 = a*LH_3; HL_3 = a*HL_3; HH_3 = a*HH_3; LL_3 = a*LL_3; 

diagonal_element_H_1(:,(level-1)*3+1) = LH_1(:); diagonal_element_H_1(:,(level-1)*3+2) = HL_1(:);
diagonal_element_H_1(:,(level-1)*3+3) = HH_1(:); diagonal_element_L_1 = LL_1(:);

diagonal_element_H_2(:,(level-1)*3+1) = LH_2(:); diagonal_element_H_2(:,(level-1)*3+2) = HL_2(:);
diagonal_element_H_2(:,(level-1)*3+3) = HH_2(:); diagonal_element_L_2 = LL_2(:);

diagonal_element_H_3(:,(level-1)*3+1) = LH_3(:); diagonal_element_H_3(:,(level-1)*3+2) = HL_3(:);
diagonal_element_H_3(:,(level-1)*3+3) = HH_3(:);diagonal_element_L_3 = LL_3(:);

hhh_1 = level_transition(hhh_1,level); hhh_2 = level_transition(hhh_2,level); hhh_3 = level_transition(hhh_3,level);

end

    end

    
    %% whole-point symmetry
    if (strcmp(type_of_symmetry, 'whole_point') == 1)
        
[M,N] = size(x); 
diagonal_element_H_1 = zeros(M*N,3*it); diagonal_element_H_2 = zeros(M*N,3*it); diagonal_element_H_3 = zeros(M*N,3*it); 
HH_1 = real(H_Reg1.*conj(H_Reg4)); HH_2 = real(H_Reg2.*conj(H_Reg4)); HH_3 = real(H_Reg3.*conj(H_Reg4)); 

switch option
    case 'DFT'
        hh_1 = real(ifft2(HH_1));  hhh_1 = hh_1(1:M+1,1:N+1);
        hh_2 = real(ifft2(HH_2));  hhh_2 = hh_2(1:M+1,1:N+1);
        hh_3 = real(ifft2(HH_3));  hhh_3 = hh_3(1:M+1,1:N+1);
        
    case 'DCT'
        hhh_1 = matrix_7*HH_1*matrix_8'; hhh_2 = matrix_7*HH_2*matrix_8';   hhh_3 = matrix_7*HH_3*matrix_8'; 
        hhh_1 = padarray(hhh_1,[2 2],'symmetric','post'); hhh_1(:,N+1) = []; hhh_1(M+1,:) = [];
        hhh_2 = padarray(hhh_2,[2 2],'symmetric','post'); hhh_2(:,N+1) = []; hhh_2(M+1,:) = [];
        hhh_3 = padarray(hhh_3,[2 2],'symmetric','post'); hhh_3(:,N+1) = []; hhh_3(M+1,:) = [];
end


for level = 1:it

%%%%%%%%%%%%%%%%%% first level %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
[LH_1, HL_1, HH_1, LL_1] = diagonal_any_size  (hhh_1,'whole_point',level);
[LH_2, HL_2, HH_2, LL_2] = diagonal_any_size  (hhh_2,'whole_point',level);
[LH_3, HL_3, HH_3, LL_3] = diagonal_any_size  (hhh_3,'whole_point',level);

a = (1/16)^level; LH_1 = a*LH_1; HL_1 = a*HL_1; HH_1 = a*HH_1; LL_1 = a*LL_1; 
LH_2 = a*LH_2; HL_2 = a*HL_2; HH_2 = a*HH_2; LL_2 = a*LL_2; 
LH_3 = a*LH_3; HL_3 = a*HL_3; HH_3 = a*HH_3; LL_3 = a*LL_3; 

diagonal_element_H_1(:,(level-1)*3+1) = LH_1(:); diagonal_element_H_1(:,(level-1)*3+2) = HL_1(:);
diagonal_element_H_1(:,(level-1)*3+3) = HH_1(:); diagonal_element_L_1 = LL_1(:);

diagonal_element_H_2(:,(level-1)*3+1) = LH_2(:); diagonal_element_H_2(:,(level-1)*3+2) = HL_2(:);
diagonal_element_H_2(:,(level-1)*3+3) = HH_2(:); diagonal_element_L_2 = LL_2(:);

diagonal_element_H_3(:,(level-1)*3+1) = LH_3(:); diagonal_element_H_3(:,(level-1)*3+2) = HL_3(:);
diagonal_element_H_3(:,(level-1)*3+3) = HH_3(:);diagonal_element_L_3 = LL_3(:);

hhh_1 = level_transition(hhh_1,level); hhh_2 = level_transition(hhh_2,level); hhh_3 = level_transition(hhh_3,level);

end

   end
    
    
end

return;



%% embedded function: compute DA0 DA00 DA000
function y = level_transition(x,level_from)
if level_from == 1

[M,N] = size(x); M = M-1; N = N-1;
c1 = [1,1:N]; c2 = c1+1; c3 = [1,3:N+1,N];
x1 = x([1,1:M],:); 
term_1 = x1(:,c1); term_2 = x1(:,c2); term_3 = x1(:,c3);
x2 = x([2,2:M+1],:);
term_4 = x2(:,c1); term_5 = x2(:,c2); term_6 = x2(:,c3); 
x3 = x([1,3:M+1,M],:);
term_7 = x3(:,c1); term_8 = x3(:,c2); term_9 = x3(:,c3);
y = term_1+2*term_2+term_3+2*term_4+4*term_5+2*term_6+term_7+2*term_8+term_9;

end

if level_from > 1
    
[M,N] = size(x); M = M-1; N = N-1;
    shift = 2^(level_from-1);
    m_1 = 1:M+1; m_2 = [shift+1:-1:2   1:M-shift+1]; m_3 = [shift+1:M+1   M:-1:M-shift+1]; 
    n_1  = 1:N+1; n_2  = [shift+1:-1:2   1:N-shift+1]; n_3  = [shift+1:N+1    N:-1:N-shift+1]; 

x1 = x(m_2,:);
term_1 = x1(:,n_2); term_2 = x1(:,n_1); term_3 = x1(:,n_3); 
x2 = x(m_1,:);
term_4 = x2(:,n_2); term_5 = x; term_6 = x2(:,n_3); 
x3 = x(m_3,:);
term_7 = x3(:,n_2); term_8 = x3(:,n_1); term_9 = x3(:,n_3);
y = term_1+2*term_2+term_3+2*term_4+4*term_5+2*term_6+term_7+2*term_8+term_9;
end

return;


