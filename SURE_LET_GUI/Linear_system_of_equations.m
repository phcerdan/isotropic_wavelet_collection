function [F,A,b] = Linear_system_of_equations  (Aux, div_Aux,   x_inv,   H_Reg1,H_Reg2, H_Reg3,H_Reg4,  ...
    noise_variance,it,  matrix_7,matrix_8,   extension_case,type_of_symmetry,option)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% this program is to establish the linear system of equations Ax = b
%%%%%%%%%%%%%% Establishing equations Ax = b %%%%%%%%%%%%%%%%%%%%%

nb = 3*it;

%% periodic boundary condition
if (strcmp(extension_case, 'periodic') == 1)

[M,N,K] = size(Aux); F = zeros(M*N,K); 
for i = 1:K
    F(:,i) = reshape(Aux(:,:,i),M*N,1);
end

first_term = F'*x_inv(:);

%%%%%%%%%%%%%%% omega %%%%%%%%%%%%%%%%%%%%%%
WW = cat(3,div_Aux,ones(M,N,3)); omega = zeros(6*nb+3,1); 
for i = 1:6*nb+3
    omega(i) = sum(sum(WW(:,:,i)));
end


%%%%%%%%%%%%%%% alpha %%%%%%%%%%%%%%%%%%%%%%%%%
[diagonal_element_H_1,diagonal_element_H_2, diagonal_element_H_3, ...
    diagonal_element_L_1,diagonal_element_L_2,diagonal_element_L_3]  = ...
    Diagonal_element ...
    (x_inv,H_Reg1,H_Reg2,H_Reg3,H_Reg4, it, matrix_7,matrix_8,      extension_case,type_of_symmetry,option);

alpha = [diagonal_element_H_1, diagonal_element_H_1, diagonal_element_H_2, diagonal_element_H_2, ...
diagonal_element_H_3, diagonal_element_H_3, ...    
diagonal_element_L_1, diagonal_element_L_2, diagonal_element_L_3]';

second_term = noise_variance*omega.*alpha;

clear alpha omega; 

A = F'*F; b = (first_term-second_term);  
factor = 1/(M*N);   A = factor*A;   b = factor*b; 

end


%% symmetric extension case
if (strcmp(extension_case, 'symmetric') == 1)
    

[M,N,K] = size(Aux); F = zeros(M*N,K); 
for i = 1:K
    F(:,i) = reshape(Aux(:,:,i),M*N,1);
end

first_term = F'*x_inv(:); 
WW = cat(3,div_Aux,ones(M,N,3)); omega = zeros(M*N,6*nb+3);  
for i = 1:6*nb+3
    omega(:,i) = reshape(WW(:,:,i),M*N,1);
end
clear WW;

%%%%%%%%%%%%%%% alpha %%%%%%%%%%%%%%%%%%%%%%%%%
[diagonal_element_H_1,diagonal_element_H_2, diagonal_element_H_3, ...
    diagonal_element_L_1,diagonal_element_L_2,diagonal_element_L_3]  = ...
    Diagonal_element  ...
    (x_inv,H_Reg1,H_Reg2,H_Reg3,H_Reg4, it, matrix_7,matrix_8,      extension_case,type_of_symmetry,option);

alpha = [diagonal_element_H_1 diagonal_element_H_1 diagonal_element_H_2 diagonal_element_H_2 ...
    diagonal_element_H_3 diagonal_element_H_3  ...
    diagonal_element_L_1 diagonal_element_L_2 diagonal_element_L_3 ];

second_term = noise_variance*diag(omega'*alpha);

clear alpha omega;

A = F'*F; b = first_term-second_term;  
factor = 1/(M*N);   A = factor*A;   b = factor*b;

end


return;

