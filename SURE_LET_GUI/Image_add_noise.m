function blurred_noisy_image = Image_add_noise(blurred_image,noise_variance)

x = double(blurred_image); [M,N] = size(x);

randn('seed', 0);

noise = randn(M,N); noise = noise/std(noise(:)); 
nsigma = sqrt(noise_variance);
noise = nsigma*noise; blurred_noisy_image = x + noise; 

return;

%%%%% calculate MSE and input PSNR
% MSE = sum(sum((blurred_noisy_image-x).^2))/(M*N);
% Input_PSNR = 10*log10(255^2/MSE);
