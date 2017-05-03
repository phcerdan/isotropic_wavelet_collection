function [blurred_image,h] = Image_blurring(image,filter, extension_case,type_of_symmetry)

image = double(image);  option= 'DCT';

switch extension_case
    case 'periodic'
        [blurred_image,h] = Image_distortion_periodic(image,filter);  

    case 'symmetric'
        if (strcmp(type_of_symmetry,'whole_point') == 1)
            [blurred_image,h] = Image_distortion_symmetric(image,filter,'whole_point',option);
        end
        if (strcmp(type_of_symmetry,'half_point') == 1)
            [blurred_image,h] = Image_distortion_symmetric(image,filter,'half_point',option);
        end
end


return;

%% embedded function 1
function [y_blurred,h] = Image_distortion_periodic(image,filter)

x = double(image); [M,N] = size(x);

switch filter{1}
    case 'experiment_1'
        h = zeros(15,15);
    for i=1:15
        for j=1:15
            h(i,j) = (1+(i-8)^2+(j-8)^2)^(-1);
        end
    end
    h = h./sum(sum(h));
    centered_h = centered_kernel_2D(h,M,N); h = centered_h;
    H = fft2(centered_h);
    y_blurred = Image_degradation_periodic(H,x);
    
    case 'experiment_2' 
    h = [1 4 6 4 1]'*[1 4 6 4 1]./256;
    centered_h = centered_kernel_2D(h,M,N); h = centered_h;
    H = fft2(centered_h);
    y_blurred = Image_degradation_periodic(H,x);
    
    case 'experiment_3' 
        blur_size = filter{2};
    h = ones(blur_size, blur_size);    h = h./sum(sum(h));
    centered_h = centered_kernel_2D(h,M,N); h = centered_h;
    H = fft2(centered_h);
    y_blurred = Image_degradation_periodic(H,x);
    
    case 'Gaussian'
        std = filter{2};
%     h = fspecial('gaussian', 25, 1.6);
   h = Gaussian_kernel(55, 55, std);
    centered_h = centered_kernel_2D(h,M,N); h = centered_h;
    H = fft2(centered_h);
    y_blurred = Image_degradation_periodic(H,x);
    
    
    case 'experiment_4'
    h = ones(9,9);    h = h./sum(sum(h));
    centered_h = centered_kernel_2D(h,M,N); h = centered_h;
    H = fft2(centered_h);
    y_blurred = Image_degradation_periodic(H,x);
    
    
    case 'microscopy'
        load 'microscopy_PSF.mat'; h = PSF;
         centered_h = centered_kernel_2D(h,M,N); h = centered_h;
    H = fft2(centered_h);
    y_blurred = Image_degradation_periodic(H,x);
    
end

return;

%% embedded function 2
function y_blurred = Image_degradation_periodic(H,x)

X = fft2(x); Y1 = H.*X; y_blurred = ifft2(Y1);

return;


%% embedded function 3
function [y_blurred,h] = Image_distortion_symmetric(image,filter,type_of_symmetry,option)
%(image,filter,0,'whole_point',0,'DCT'); 

x = double(image); [M,N] = size(x);

switch filter{1}
    case 'experiment_1'
        h = zeros(15,15);
    for i=1:15
        for j=1:15
            h(i,j) = (1+(i-8)^2+(j-8)^2)^(-1);
        end
    end
    h = h./sum(sum(h)); h = centered_kernel_2D(h,M,N);
    y_blurred = Image_degradation_symmetric(h,x,type_of_symmetry,option);

    case 'experiment_4'
    h = ones(9,9);    h = h./sum(sum(h));  h = centered_kernel_2D(h,M,N);
    y_blurred = Image_degradation_symmetric(h,x,type_of_symmetry,option);

    case 'experiment_2' 
    h = [1 4 6 4 1]'*[1 4 6 4 1]./256;  h = centered_kernel_2D(h,M,N);
    y_blurred = Image_degradation_symmetric(h,x,type_of_symmetry,option);

    case 'experiment_3'
    h = ones(5,5);    h = h./sum(sum(h));  h = centered_kernel_2D(h,M,N);
    y_blurred = Image_degradation_symmetric(h,x,type_of_symmetry,option);

    case 'Gaussian'
    std = filter{2};
    h = Gaussian_kernel(45,45,std);  h = centered_kernel_2D(h,M,N);
    y_blurred = Image_degradation_symmetric(h,x,type_of_symmetry,option);
   
end

return;


%% embedded function 4
function y_blurred = Image_degradation_symmetric(h,x,type_of_symmetry,option)
    
[M,N] = size(x);

%%%%%%%%%%%%%%%%% symmetric filtering --- Hx %%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if (strcmp(type_of_symmetry, 'half_point') == 1) && (strcmp(option, 'DFT') == 1)
xx = padarray(x,[M N],'symmetric','post'); XX = fft2(xx);

centered_h = zeros(2*M,2*N); centered_h(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
centered_h(1:ceil(M/2),end-floor(N/2)+1:end) = h(1:ceil(M/2),end-floor(N/2)+1:end); 
centered_h(end-floor(M/2)+1:end,1:ceil(N/2)) = h(end-floor(M/2)+1:end,1:ceil(N/2)); 
centered_h(end-floor(M/2)+1:end,end-floor(N/2)+1:end) = h(end-floor(M/2)+1:end,end-floor(N/2)+1:end); 

H = fft2(centered_h);
y_blurred = real(ifft2(XX.*H)); y_blurred = y_blurred(1:M,1:N);
end


if (strcmp(type_of_symmetry, 'half_point') == 1) && (strcmp(option, 'DCT') == 1)

if (M == N)
[DCT_2_matrix,IDCT_2_matrix] = DCT_type2_real_matrix(N); DCT_2D_X = DCT_2_matrix*x*DCT_2_matrix'; 
[DCT_1_filter_matrix, IDCT_1_filter_matrix] = DCT_type1_matrix(N+1);
hh = zeros(M+1,N+1); hh(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
DCT_2D_H = DCT_1_filter_matrix*hh*DCT_1_filter_matrix'; 
DCT_2D_Y = DCT_2D_H(1:M,1:N).*DCT_2D_X; y_blurred = IDCT_2_matrix*DCT_2D_Y*IDCT_2_matrix'; 
end

if (M ~= N)
[DCT_2_matrix_M,IDCT_2_matrix_M] = DCT_type2_real_matrix(M); 
[DCT_2_matrix_N,IDCT_2_matrix_N] = DCT_type2_real_matrix(N); 
DCT_2D_X = DCT_2_matrix_M*x*DCT_2_matrix_N'; 

[DCT_1_filter_matrix_M, IDCT_1_filter_matrix_M] = DCT_type1_matrix(M+1);
[DCT_1_filter_matrix_N, IDCT_1_filter_matrix_N] = DCT_type1_matrix(N+1);
hh = zeros(M+1,N+1); hh(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
DCT_2D_H = DCT_1_filter_matrix_M*hh*DCT_1_filter_matrix_N'; 
DCT_2D_Y = DCT_2D_H(1:M,1:N).*DCT_2D_X; y_blurred = IDCT_2_matrix_M*DCT_2D_Y*IDCT_2_matrix_N'; 
end

end


if (strcmp(type_of_symmetry, 'whole_point') == 1) && (strcmp(option, 'DFT') == 1)
xx = padarray(x,[M N],'symmetric','post'); 
xx(:,N+1) = []; xx(M+1,:) = []; xx = xx(1:end-1,1:end-1); XX = fft2(xx);

centered_h = zeros(2*M-2,2*N-2); centered_h(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
centered_h(1:ceil(M/2),end-floor(N/2)+1:end) = h(1:ceil(M/2),end-floor(N/2)+1:end); 
centered_h(end-floor(M/2)+1:end,1:ceil(N/2)) = h(end-floor(M/2)+1:end,1:ceil(N/2)); 
centered_h(end-floor(M/2)+1:end,end-floor(N/2)+1:end) = h(end-floor(M/2)+1:end,end-floor(N/2)+1:end); 

H = fft2(centered_h); y_blurred = real(ifft2(XX.*H)); y_blurred = y_blurred(1:M,1:N);
end


if (strcmp(type_of_symmetry, 'whole_point') == 1) && (strcmp(option, 'DCT') == 1)
    
if (M == N)
[DCT_1_matrix,IDCT_1_matrix] = DCT_type1_matrix(N); 
DCT_2D_X = DCT_1_matrix*x*DCT_1_matrix'; 
hh = zeros(M,N); hh(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
DCT_2D_H = DCT_1_matrix*hh*DCT_1_matrix'; 

DCT_2D_Y = DCT_2D_H.*DCT_2D_X;
y_blurred = IDCT_1_matrix*DCT_2D_Y*IDCT_1_matrix';
end

if (M ~= N)
[DCT_1_matrix_M,IDCT_1_matrix_M] = DCT_type1_matrix(M); 
[DCT_1_matrix_N,IDCT_1_matrix_N] = DCT_type1_matrix(N); 
DCT_2D_X = DCT_1_matrix_M*x*DCT_1_matrix_N'; 

hh = zeros(M,N); hh(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
DCT_2D_H = DCT_1_matrix_M*hh*DCT_1_matrix_N';

DCT_2D_Y = DCT_2D_H.*DCT_2D_X;
y_blurred = IDCT_1_matrix_M*DCT_2D_Y*IDCT_1_matrix_N';
end

end

y_blurred = real(y_blurred);

return;

%% embedded function 5
function h = Gaussian_kernel(m,n,sigma)
h = ones(m,n);
center_x = (m+1)/2; center_y = (n+1)/2;
for i = 1:m
    for j = 1:n
        h(i,j) = exp(-((i-center_x)^2+(j-center_y)^2)/(2*sigma^2));
    end
end
h = h./sum(h(:));

return;


%% embedded function 6
function centered_h = centered_kernel_2D(h,M,N)

[m,n] = size(h); size_h = m; centered_h = zeros(M,N);
clear m n;

if mod(size_h,2) == 1
% if size_h is odd ---> whole-point symmetry, often referring to convolution kernel 
% top-left
centered_h(1:(size_h+1)/2,1:(size_h+1)/2) = h((size_h+1)/2:size_h,(size_h+1)/2:size_h);
% bottom-right
centered_h(M-(size_h-1)/2+1:M,N-(size_h-1)/2+1:N) = h(1:(size_h-1)/2,1:(size_h-1)/2);
% top-right
centered_h(1:(size_h+1)/2,N-(size_h-1)/2+1:N) = h((size_h+1)/2:size_h,1:(size_h-1)/2);
% bottom-left
centered_h(M-(size_h-1)/2+1:M,1:(size_h+1)/2) = h(1:(size_h-1)/2,(size_h+1)/2:size_h);
end


if mod(size_h,2) == 0
% if size_h is even ---> half-point symmetry, often referring
% to Haar wavelet, for example.
% top-left of h
centered_h(1:(size_h/2+1),1:(size_h/2+1)) = h(size_h/2:end,size_h/2:end);
% bottom-right of h
centered_h(end-(size_h/2-1)+1:end,end-(size_h/2-1)+1:end) = h(1:size_h/2-1,1:size_h/2-1);
% top-right of h
centered_h(1:size_h/2+1,end-(size_h/2-1)+1:end) = h(size_h/2:end,1:size_h/2-1);
% bottom-left of h
centered_h(end-(size_h/2-1)+1:end,1:size_h/2+1) = h(1:size_h/2-1,end-(size_h/2+1)+1:end);
end

return;

%% embedded function 3
function [DCT_1_matrix, IDCT_1_matrix] = DCT_type1_matrix(N)

%%%%%%%%%%% forward DCT
DCT_1_matrix = zeros(N,N);

for k = 0:N-1
    for n = 0:N-1
        DCT_1_matrix(k+1,n+1) = cos(pi*k*n/(N-1));
    end
end

DCT_1_matrix = 2*DCT_1_matrix;
DCT_1_matrix(:,1) = 1; 
for k = 1:N
    DCT_1_matrix(k,N) = (-1)^(k-1);
end

%%%%%%%%%%%%%% inverse DCT
IDCT_1_matrix = DCT_1_matrix';
IDCT_1_matrix(1,:) = 2*IDCT_1_matrix(1,:); IDCT_1_matrix(N,:) = 2*IDCT_1_matrix(N,:);
IDCT_1_matrix(:,1) = 0.5*IDCT_1_matrix(:,1); IDCT_1_matrix(:,N) = 0.5*IDCT_1_matrix(:,N);
IDCT_1_matrix = IDCT_1_matrix/(2*(N-1));

return;

%% embedded function 4
function [DCT_2_matrix,IDCT_2_matrix] = DCT_type2_matrix(N)

%%%%%%%%% forward DCT
DCT_2_matrix = zeros(N,N);

for k = 0:N-1
    for n = 0:N-1
        DCT_2_matrix(k+1,n+1) = exp(sqrt(-1)*pi*k/(2*N))*cos(pi*k*(n+0.5)/N);
    end
end

DCT_2_matrix = 2*DCT_2_matrix;

%%%%%%%%%%%%%%%% inverse DCT
IDCT_2_matrix = DCT_2_matrix';
IDCT_2_matrix(:,1) = IDCT_2_matrix(:,1)/(4*N);
for i = 2:N
    IDCT_2_matrix(:,i) = IDCT_2_matrix(:,i)/(2*N);
end

return;


%% embedded function 5
function [DCT_type2_matrix,IDCT_type2_matrix] = DCT_type2_real_matrix(N)

%%%%%%%%% forward DCT
DCT_type2_matrix = zeros(N,N);

for k = 0:N-1
    for n = 0:N-1
        DCT_type2_matrix(k+1,n+1) = cos(pi*k*(n+0.5)/N);
    end
end

DCT_type2_matrix = 2*DCT_type2_matrix;

%%%%%%%%%%%%%%%% inverse DCT
IDCT_type2_matrix = DCT_type2_matrix';
IDCT_type2_matrix(:,1) = IDCT_type2_matrix(:,1)/(4*N);
for i = 2:N
    IDCT_type2_matrix(:,i) = IDCT_type2_matrix(:,i)/(2*N);
end

return;

