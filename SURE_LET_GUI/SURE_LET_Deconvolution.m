function SURE_deconvolved_image =   SURE_LET_Deconvolution  ...
    (blurred_noisy_image, filter,  noise_variance, extension, symmetry_type)
wait_bar = waitbar(0, 'Please wait...');
waitbar(1/5, wait_bar);

y = double(blurred_noisy_image); [M,N] = size(y);

% tic;
%% parameter setting
% extension = 'symmetric';  symmetry_type = 'half_point';
lambda = 1*[1e-4 1e-3 1e-2]*noise_variance; it = 3;
% lambda = eps*[1e-4 1e-3 1e-2]; it = 3;

extra_lambda = 1e-5*noise_variance; mu = 5e-2; option = 'DCT';

h = filter; nsigma = sqrt(noise_variance);

[matrix_1, matrix_2, matrix_3, matrix_4, matrix_5, matrix_6, matrix_7, matrix_8] = ...
    design_transform_matrix(M,N,extension,symmetry_type,option);

[H, HH, S, SS, H_f, SS_f] = design_regularization_filter ...
    (h, matrix_1,matrix_2, extension,symmetry_type,option);

%% Tikhonov Regularization
[x_Reg1, x_Reg2, x_Reg3, x_inv,  H_Reg1, H_Reg2, H_Reg3, H_Reg4] = ...
    Tikhonov_Regularization  (y,lambda,extra_lambda, H,HH,SS,  ...
    matrix_3, matrix_4, matrix_5, matrix_6,  extension,symmetry_type,   option);

% toc;
waitbar(2/5, wait_bar);

%% Wavelet Decomposition
% nb is number of highpass subbands
if (it ~= 0)
% tic;
nb = 3*it;
[yL1,yH1] = Haar_rdwt  (x_Reg1,it, extension,symmetry_type);
[yL2,yH2] = Haar_rdwt  (x_Reg2,it, extension,symmetry_type);
[yL3,yH3] = Haar_rdwt  (x_Reg3,it, extension,symmetry_type);
% toc;

%% Wavelet Thresholding
% tic;
var_subband = Subband_variance  (h,lambda,H_Reg1,H_Reg2,H_Reg3, H_f,SS_f,it,extension);
std_subband = sqrt(var_subband);

WW1_1 = zeros(M,N,nb); WW1_2 = zeros(M,N,nb); WW2_1 = zeros(M,N,nb); WW2_2 = zeros(M,N,nb); 
WW3_1 = zeros(M,N,nb); WW3_2 = zeros(M,N,nb); 
div_WW1_1 = zeros(M,N,nb); div_WW1_2 = zeros(M,N,nb); div_WW2_1 = zeros(M,N,nb); div_WW2_2 = zeros(M,N,nb); 
div_WW3_1 = zeros(M,N,nb); div_WW3_2 = zeros(M,N,nb);
a = 6; factor = 1.5;      f = 1/factor^8;  
T1 = a*nsigma*std_subband(1,:); T2 = a*nsigma*std_subband(2,:); T3 = a*nsigma*std_subband(3,:); 

for i = 1:nb
     A = yH1(:,:,i); a1 = factor./T1(i); a1 = A.*a1; a1 = a1.*a1; a1 = a1.*a1; b1 = exp(-a1);
     WW1_1(:,:,i) = A.*(1-b1); div_WW1_1(:,:,i) = 1+(4*a1-1).*b1;
     a1 = f*a1;  b1 = exp(-a1); 
     WW1_2(:,:,i) = A.*(1-b1); div_WW1_2(:,:,i) = 1+(4*a1-1).*b1;

     A = yH2(:,:,i); a1 = factor./T2(i); a1 = A.*a1; a1 = a1.*a1; a1 = a1.*a1; b1 = exp(-a1);
     WW2_1(:,:,i) = A.*(1-b1); div_WW2_1(:,:,i) = 1+(4*a1-1).*b1;
     a1 = f*a1;  b1 = exp(-a1); 
     WW2_2(:,:,i) = A.*(1-b1); div_WW2_2(:,:,i) = 1+(4*a1-1).*b1;

     A = yH3(:,:,i); a1 = factor./T3(i); a1 = A.*a1; a1 = a1.*a1; a1 = a1.*a1; b1 = exp(-a1);
     WW3_1(:,:,i) = A.*(1-b1); div_WW3_1(:,:,i) = 1+(4*a1-1).*b1;
     a1 = f*a1;  b1 = exp(-a1); 
     WW3_2(:,:,i) = A.*(1-b1); div_WW3_2(:,:,i) = 1+(4*a1-1).*b1;

end

clear yH1 yH2 yH3;

div_Aux = cat(3,div_WW1_1,div_WW1_2,div_WW2_1,div_WW2_2, div_WW3_1,div_WW3_2);
clear div_WW1_1 div_WW1_2 div_WW2_1 div_WW2_2  div_WW3_1 div_WW3_2;

waitbar(3/5, wait_bar);
% toc;
%% Wavelet Reconstruction
% tic;
Aux1_1 = zeros(M,N,nb); Aux1_2 = zeros(M,N,nb); Aux2_1 = zeros(M,N,nb); Aux2_2 = zeros(M,N,nb); 
Aux3_1 = zeros(M,N,nb); Aux3_2 = zeros(M,N,nb);

for i = 1:nb
    scale = ceil(i/3);    orientation = mod(i,3);
    Aux1_1(:,:,i) = Haar_irdwt  (WW1_1(:,:,i),orientation,scale,extension,symmetry_type);
    Aux1_2(:,:,i) = Haar_irdwt  (WW1_2(:,:,i),orientation,scale,extension,symmetry_type);

    Aux2_1(:,:,i) = Haar_irdwt  (WW2_1(:,:,i),orientation,scale,extension,symmetry_type);
    Aux2_2(:,:,i) = Haar_irdwt  (WW2_2(:,:,i),orientation,scale,extension,symmetry_type);

    Aux3_1(:,:,i) = Haar_irdwt  (WW3_1(:,:,i),orientation,scale,extension,symmetry_type);
    Aux3_2(:,:,i) = Haar_irdwt  (WW3_2(:,:,i),orientation,scale,extension,symmetry_type);
    
end


Aux_LL1 = Haar_irdwt  (yL1,3,it,extension,symmetry_type);
Aux_LL2 = Haar_irdwt  (yL2,3,it,extension,symmetry_type);
Aux_LL3 = Haar_irdwt  (yL3,3,it,extension,symmetry_type);

clear WW1_1 WW1_2 WW2_1 WW2_2  WW3_1 WW3_2;

Aux = cat(3, Aux1_1,Aux1_2,Aux2_1,Aux2_2,Aux3_1,Aux3_2, Aux_LL1,Aux_LL2, Aux_LL3); 
clear Aux1_1 Aux1_2 Aux2_1 Aux2_2 Aux3_1 Aux3_2   Aux_LL1 Aux_LL2 Aux_LL3;

% toc;

waitbar(4/5, wait_bar);

%% Establishment of linear system of equations Ax = b
% tic;
[F,A,b] = Linear_system_of_equations  (Aux, div_Aux,    x_inv,   H_Reg1,H_Reg2, H_Reg3,H_Reg4,  ...
    noise_variance, it,  matrix_7,matrix_8,   extension,symmetry_type,option);

clear Aux div_Aux;
% toc;

%% solving the equations, using regularization technique
I = eye(size(A));  coef_sure = (A+mu*I)\b;
SURE_deconvolved_image = real(reshape(F*coef_sure,M,N));

waitbar(5/5, wait_bar);

close (wait_bar);

end



%% if necessary ...

% SURE_deconvolved_image = double(uint8(SURE_deconvolved_image));

