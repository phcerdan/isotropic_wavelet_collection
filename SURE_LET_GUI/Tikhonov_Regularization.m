function [x_Reg1, x_Reg2, x_Reg3, x_inv,  H_Reg1, H_Reg2, H_Reg3, H_Reg4] = ...
    Tikhonov_Regularization (y,lambda,extra_lambda, H,HH,SS,  ...
    matrix_3, matrix_4, matrix_5, matrix_6, extension_case, type_of_symmetry,option)

if (strcmp(extension_case, 'periodic') == 1)
  [x_Reg1, x_Reg2, x_Reg3, x_inv, H_Reg1, H_Reg2, H_Reg3, H_Reg4] = ...
    Tikhonov_Regularization_periodic  (y,lambda,extra_lambda,H,HH,SS);
end

if (strcmp(extension_case, 'symmetric') == 1)
    [x_Reg1, x_Reg2, x_Reg3, x_inv,  H_Reg1, H_Reg2, H_Reg3, H_Reg4]  = ...
        Tikhonov_Regularization_symmetric ...
    (y,lambda,extra_lambda, H,HH,SS, matrix_3,matrix_4,matrix_5,matrix_6,type_of_symmetry,option);
end

return;


%% embedded function 1
function [x_Reg1, x_Reg2, x_Reg3, x_inv, H_Reg1, H_Reg2, H_Reg3, H_Reg4] = ...
    Tikhonov_Regularization_periodic  (y,lambda,extra_lambda,H,HH,SS)

Y = fft2(y); 
H_Reg1 = conj(H)./(HH+lambda(1)*SS); H_Reg2 = conj(H)./(HH+lambda(2)*SS); 
H_Reg3 = conj(H)./(HH+lambda(3)*SS); H_Reg4 = conj(H)./(HH+extra_lambda*SS); 
x_Reg1 = abs(ifft2(Y.*H_Reg1)); x_Reg2 = abs(ifft2(Y.*H_Reg2));
x_Reg3 = abs(ifft2(Y.*H_Reg3)); x_inv = real(ifft2(Y.*H_Reg4));

return;

%% embedded function 2
function [x_Reg1, x_Reg2, x_Reg3, x_inv, H_Reg1, H_Reg2, H_Reg3, H_Reg4]  = ...
    Tikhonov_Regularization_symmetric ...
    (y,lambda,extra_lambda, H,HH,SS, matrix_3,matrix_4,matrix_5,matrix_6, type_of_symmetry,option)

[M,N] = size(y);  

%%%%%%%%%% half-point symmetric extension of x and Using DFT
if (strcmp(type_of_symmetry, 'half_point') == 1) &&  (strcmp(option, 'DFT') == 1)
yy = padarray(y,[M N],'symmetric','post'); Y = fft2(yy);
H_Reg1 = H./(HH+lambda(1)*SS); x_Reg1 = ifft2(H_Reg1.*Y); x_Reg1 = abs(x_Reg1(1:M,1:N));
H_Reg2 = H./(HH+lambda(2)*SS); x_Reg2 = ifft2(H_Reg2.*Y); x_Reg2 = abs(x_Reg2(1:M,1:N));
H_Reg3 = H./(HH+lambda(3)*SS); x_Reg3 = ifft2(H_Reg3.*Y); x_Reg3 = abs(x_Reg3(1:M,1:N));
H_Reg4 = H./(HH+extra_lambda*SS); x_inv = ifft2(H_Reg4.*Y); x_inv = real(x_inv(1:M,1:N));
end

%%%%%%%%%%% half-point symmetric extension of x and whole-point symmetric h(n)=h(-n)
if (strcmp(type_of_symmetry, 'half_point') == 1) && (strcmp(option, 'DCT') == 1)

if (M == N)
DCT_2D_Y = matrix_3*y*matrix_3'; 

H_Reg1 = H./(HH+lambda(1)*SS); DCT_2D_X_Reg1 = H_Reg1(1:M,1:N).*DCT_2D_Y;
H_Reg2 = H./(HH+lambda(2)*SS); DCT_2D_X_Reg2 = H_Reg2(1:M,1:N).*DCT_2D_Y;
H_Reg3 = H./(HH+lambda(3)*SS); DCT_2D_X_Reg3 = H_Reg3(1:M,1:N).*DCT_2D_Y;
H_Reg4 = H./(HH+extra_lambda*SS); DCT_2D_X_Reg4 = H_Reg4(1:M,1:N).*DCT_2D_Y;

x_Reg1 = abs(matrix_4*DCT_2D_X_Reg1*matrix_4'); x_Reg2 = abs(matrix_4*DCT_2D_X_Reg2*matrix_4');
x_Reg3 = abs(matrix_4*DCT_2D_X_Reg3*matrix_4'); x_inv = real(matrix_4*DCT_2D_X_Reg4*matrix_4');
end


if (M ~= N)
DCT_2D_Y = matrix_3*y*matrix_5'; 
H_Reg1 = H./(HH+lambda(1)*SS); DCT_2D_X_Reg1 = H_Reg1(1:M,1:N).*DCT_2D_Y;
H_Reg2 = H./(HH+lambda(2)*SS); DCT_2D_X_Reg2 = H_Reg2(1:M,1:N).*DCT_2D_Y;
H_Reg3 = H./(HH+lambda(3)*SS); DCT_2D_X_Reg3 = H_Reg3(1:M,1:N).*DCT_2D_Y;
H_Reg4 = H./(HH+extra_lambda*SS); DCT_2D_X_Reg4 = H_Reg4(1:M,1:N).*DCT_2D_Y;

x_Reg1 = abs(matrix_4*DCT_2D_X_Reg1*matrix_6'); x_Reg2 = abs(matrix_4*DCT_2D_X_Reg2*matrix_6');
x_Reg3 = abs(matrix_4*DCT_2D_X_Reg3*matrix_6'); x_inv = real(matrix_4*DCT_2D_X_Reg4*matrix_6');
end

end


%%%%%%%%% whole-point symmetric extension of x and Using DFT
if (strcmp(type_of_symmetry, 'whole_point') == 1) &&  (strcmp(option, 'DFT') == 1)
yy = padarray(y,[M N],'symmetric','post'); 
yy(:,N+1) = []; yy(M+1,:) = []; yy = yy(1:end-1,1:end-1); Y = fft2(yy);

H_Reg1 = H./(HH+lambda(1)*SS); x_Reg1 = abs(ifft2(H_Reg1.*Y)); x_Reg1 = x_Reg1(1:M,1:N);
H_Reg2 = H./(HH+lambda(2)*SS); x_Reg2 = abs(ifft2(H_Reg2.*Y)); x_Reg2 = x_Reg2(1:M,1:N);
H_Reg3 = H./(HH+lambda(3)*SS); x_Reg3 = abs(ifft2(H_Reg3.*Y)); x_Reg3 = x_Reg3(1:M,1:N);
H_Reg4 = H./(HH+extra_lambda*SS); x_inv = real(ifft2(H_Reg4.*Y)); x_inv = x_inv(1:M,1:N);
end


%%%%%%%%%%%%% whole-point symmetric extension of x and whole-point symmetric h(n)=h(-n)
if (strcmp(type_of_symmetry, 'whole_point') == 1) && (strcmp(option, 'DCT') == 1)

if (M == N)
DCT_2D_Y = matrix_3*y*matrix_3'; 
H_Reg1 = H./(HH+lambda(1)*SS); DCT_2D_X_Reg1 = H_Reg1.*DCT_2D_Y;
H_Reg2 = H./(HH+lambda(2)*SS); DCT_2D_X_Reg2 = H_Reg2.*DCT_2D_Y;
H_Reg3 = H./(HH+lambda(3)*SS); DCT_2D_X_Reg3 = H_Reg3.*DCT_2D_Y;
H_Reg4 = H./(HH+extra_lambda*SS); DCT_2D_X_Reg4 = H_Reg4.*DCT_2D_Y;

x_Reg1 = abs(matrix_4*DCT_2D_X_Reg1*matrix_4'); x_Reg2 = abs(matrix_4*DCT_2D_X_Reg2*matrix_4'); 
x_Reg3 = abs(matrix_4*DCT_2D_X_Reg3*matrix_4'); x_inv = real(matrix_4*DCT_2D_X_Reg4*matrix_4');
end

if (M ~= N)
DCT_2D_Y = matrix_3*y*matrix_5';
H_Reg1 = H./(HH+lambda(1)*SS); DCT_2D_X_Reg1 = H_Reg1.*DCT_2D_Y;
H_Reg2 = H./(HH+lambda(2)*SS); DCT_2D_X_Reg2 = H_Reg2.*DCT_2D_Y;
H_Reg3 = H./(HH+lambda(3)*SS); DCT_2D_X_Reg3 = H_Reg3.*DCT_2D_Y;
H_Reg4 = H./(HH+extra_lambda*SS); DCT_2D_X_Reg4 = H_Reg4.*DCT_2D_Y;

x_Reg1 = abs(matrix_4*DCT_2D_X_Reg1*matrix_6'); x_Reg2 = abs(matrix_4*DCT_2D_X_Reg2*matrix_6'); 
x_Reg3 = abs(matrix_4*DCT_2D_X_Reg3*matrix_6'); x_inv = real(matrix_4*DCT_2D_X_Reg4*matrix_6');
end

end

return;


