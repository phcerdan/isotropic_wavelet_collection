function [H, HH, S, SS, H_f, SS_f] = design_regularization_filter ...
    (h, matrix_1,matrix_2, extension_case,type_of_symmetry,option)

[M,N] = size(h);

%% design z at first, now z is Hermitian symmetric, s.t. the inverse DFT is also real-valued,
%% for sake of performing filtering.
limit = 1; 
x = linspace(-limit,limit,M); y = linspace(-limit,limit,N); 
[xx,yy] = meshgrid(x,y); z = xx.^2 + yy.^2; z = max(z(:))-z; 

if (mod(M,2) == 0) && (mod(N,2) == 0)
    z = z(1:M/2+1,1:N/2+1);    z = padarray(z,[M/2 N/2],'symmetric','post'); 
    z(M/2+1,:) = []; z(:,N/2+1) = [];
end
if (mod(M,2) == 0) && (mod(N,2) == 1)
    z = z(1:M/2+1,1:(N+1)/2);    z = padarray(z,[M/2 (N-1)/2],'symmetric','post'); 
    z(M/2+1,:) = []; 
end
if (mod(M,2) == 1) && (mod(N,2) == 0)
    z = z(1:(M+1)/2,1:N/2+1);    z = padarray(z,[(M-1)/2 N/2],'symmetric','post'); 
    z(:,N/2+1) = []; 
end
if (mod(M,2) == 1) && (mod(N,2) == 1)
    z = z(1:(M+1)/2,1:(N+1)/2);    z = padarray(z,[(M-1)/2 (N-1)/2],'symmetric','post'); 
end

%% periodic boundary condition
if (strcmp(extension_case, 'periodic') == 1)
    S = z; SS = S.*S; H = fft2(h); HH = H.*conj(H);
    H_f = H; SS_f = SS;
end


%% half-point symmetric boundary condition, with DFT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'half_point') == 1) ...
        &&  (strcmp(option, 'DFT') == 1)
    
    S = z; SS_f = S.*S; H_f = fft2(h); 
    
    z = real(ifft2(z)); % now, z is high-pass filter in spatial domain, of original size M x N
    centered_z = zeros(2*M,2*N); centered_z(1:ceil(M/2),1:ceil(N/2)) = z(1:ceil(M/2),1:ceil(N/2));
    centered_z(1:ceil(M/2),end-floor(N/2)+1:end) = z(1:ceil(M/2),end-floor(N/2)+1:end);
    centered_z(end-floor(M/2)+1:end,1:ceil(N/2)) = z(end-floor(M/2)+1:end,1:ceil(N/2));
    centered_z(end-floor(M/2)+1:end,end-floor(N/2)+1:end) = z(end-floor(M/2)+1:end,end-floor(N/2)+1:end);
    S = fft2(centered_z); SS = S.*conj(S);
    
    centered_h = zeros(2*M,2*N); centered_h(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
    centered_h(1:ceil(M/2),end-floor(N/2)+1:end) = h(1:ceil(M/2),end-floor(N/2)+1:end); 
    centered_h(end-floor(M/2)+1:end,1:ceil(N/2)) = h(end-floor(M/2)+1:end,1:ceil(N/2)); 
    centered_h(end-floor(M/2)+1:end,end-floor(N/2)+1:end) = h(end-floor(M/2)+1:end,end-floor(N/2)+1:end); 
    H = real(fft2(centered_h)); HH = H.*H;
end

%% half-point symmetric boundary condition, with DCT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'half_point') == 1) ...
        &&  (strcmp(option, 'DCT') == 1)
    
    S = z; SS_f = S.*S; H_f = fft2(h); 
    z = real(ifft2(z)); % now, z is high-pass filter in spatial domain, of original size M x N
    zz = zeros(M+1,N+1); zz(1:ceil(M/2),1:ceil(N/2)) = z(1:ceil(M/2),1:ceil(N/2));
    S = matrix_1*zz*matrix_2'; SS = S.*S; % of size (M+1) x (N+1)
    
    hh = zeros(M+1,N+1); hh(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
    H = matrix_1*hh*matrix_2'; HH = H.*H; % of size (M+1) x (N+1)
end


%% whole-point symmetric boundary condition, with DFT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'whole_point') == 1) ...
        &&  (strcmp(option, 'DFT') == 1)
    
    S = z; SS_f = S.*S; H_f = fft2(h); 
    
    z = real(ifft2(z)); 
    centered_z = zeros(2*M-2,2*N-2); centered_z(1:ceil(M/2),1:ceil(N/2)) = z(1:ceil(M/2),1:ceil(N/2));
    centered_z(1:ceil(M/2),end-floor(N/2)+1:end) = z(1:ceil(M/2),end-floor(N/2)+1:end); 
    centered_z(end-floor(M/2)+1:end,1:ceil(N/2)) = z(end-floor(M/2)+1:end,1:ceil(N/2)); 
    centered_z(end-floor(M/2)+1:end,end-floor(N/2)+1:end) = z(end-floor(M/2)+1:end,end-floor(N/2)+1:end); 
    S = fft2(centered_z); SS = S.*conj(S);
    
    centered_h = zeros(2*M-2,2*N-2); centered_h(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
    centered_h(1:ceil(M/2),end-floor(N/2)+1:end) = h(1:ceil(M/2),end-floor(N/2)+1:end); 
    centered_h(end-floor(M/2)+1:end,1:ceil(N/2)) = h(end-floor(M/2)+1:end,1:ceil(N/2)); 
    centered_h(end-floor(M/2)+1:end,end-floor(N/2)+1:end) = h(end-floor(M/2)+1:end,end-floor(N/2)+1:end); 
    H = fft2(centered_h); HH = H.*conj(H);
end


%% whole-point symmetric boundary condition, with DCT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'whole_point') == 1) ...
        &&  (strcmp(option, 'DCT') == 1)
    
    S = z; SS_f = S.*S; H_f = fft2(h); 
    
    z = real(ifft2(z)); % now, z is high-pass filter in spatial domain, of original size M x N
    zz = zeros(M,N); zz(1:ceil(M/2),1:ceil(N/2)) = z(1:ceil(M/2),1:ceil(N/2));
    S = matrix_1*zz*matrix_2'; SS = S.*S;
    
    hh = zeros(M,N); hh(1:ceil(M/2),1:ceil(N/2)) = h(1:ceil(M/2),1:ceil(N/2));
    H = matrix_1*hh*matrix_2'; HH = H.*H;
end



return;










