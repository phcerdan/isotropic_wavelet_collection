function var_subband = Subband_variance (h,lambda,H_Reg1,H_Reg2,H_Reg3, H_f,SS_f,it,extension_case)

[M,N] = size(h); var_subband = zeros(3,3*it); 

if (strcmp(extension_case, 'symmetric') == 1)
H_Reg1 = real(H_f./(H_f.*H_f+lambda(1)*SS_f)); H_Reg2 = real(H_f./(H_f.*H_f+lambda(2)*SS_f)); 
H_Reg3 = real(H_f./(H_f.*H_f+lambda(3)*SS_f)); 
end

candidate1 = real(H_Reg1.*H_Reg1); candidate2 = real(H_Reg2.*H_Reg2); candidate3 = real(H_Reg3.*H_Reg3); 

for level = 1:it
    
% candidate1 = real(H_Reg1.*H_Reg1); candidate2 = real(H_Reg2.*H_Reg2); candidate3 = real(H_Reg3.*H_Reg3); 
    shift = 2^(level-1);
% LH
d = 1/2*[-1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[1 zeros(1,shift-1) 1 zeros(1,shift-1)];
D = fft2(d,M,N); D = D.*conj(D);
aa = candidate1.*D; var_subband(1,(level-1)*3+1) = sum(aa(:));
aa = candidate2.*D; var_subband(2,(level-1)*3+1) = sum(aa(:));
aa = candidate3.*D; var_subband(3,(level-1)*3+1) = sum(aa(:));
% HL
var_subband(1,(level-1)*3+2) = var_subband(1,(level-1)*3+1); 
var_subband(2,(level-1)*3+2) = var_subband(2,(level-1)*3+1); 
var_subband(3,(level-1)*3+2) = var_subband(3,(level-1)*3+1); 
% HH
d = 1/2*[-1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[-1 zeros(1,shift-1) 1 zeros(1,shift-1)];
D = fft2(d,M,N); D = D.*conj(D);
aa = candidate1.*D; var_subband(1,(level-1)*3+3) = sum(aa(:));
aa = candidate2.*D; var_subband(2,(level-1)*3+3) = sum(aa(:));
aa = candidate3.*D; var_subband(3,(level-1)*3+3) = sum(aa(:));

% transition to next level of decomposition
d_L = 1/2*[1 zeros(1,shift-1) 1 zeros(1,shift-1)]'*[1 zeros(1,shift-1) 1 zeros(1,shift-1)];
D_L = fft2(d_L,M,N); D_L = D_L.*conj(D_L);

candidate1 = candidate1.*D_L; candidate2 = candidate2.*D_L; candidate3 = candidate3.*D_L; 

end

var_subband = (1/(M*N))*real(var_subband);

return;
