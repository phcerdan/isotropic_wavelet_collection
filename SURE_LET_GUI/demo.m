clear all; close all;


%% experimental setting
% load test images: cameraman  couple coco house crowd lake  bridge mixture
image_name = 'mixture.tif'; clean_image = double(imread(image_name));  
% convolution kernel
s = 3; filter_type = {'Gaussian' s};  
s = 9; filter_type = {'experiment_3' s};
% filter_type = {'experiment_2'};
% Gaussian noise level
nsigma = 5; noise_variance = nsigma^2;

% boundary condition: periodic or symmetric
extension = 'periodic'; symmetry_type = 'NA'; % not available for periodic extension
% extension = 'symmetric'; symmetry_type = 'half_point';
% extension = 'symmetric'; symmetry_type = 'whole_point';


%% generate synthesized blurred image --- blurred_noisy_image;
% h is convolution kernel
[blurred_image,filter] = Image_blurring(clean_image, filter_type, extension, symmetry_type);
% add Gaussian noise
blurred_noisy_image = Image_add_noise(blurred_image, noise_variance);


%% SURE-LET deconvolution: restored image --- SURE_deconvolved_image

tic;
SURE_deconvolved_image =   SURE_LET_Deconvolution  (blurred_noisy_image, filter, noise_variance, extension, symmetry_type); 
toc;
fprintf(['=========================\n']);

%% show result
input_psnr = PSNR(blurred_noisy_image, clean_image, 255);    
output_psnr = PSNR(SURE_deconvolved_image, clean_image, 255);

figure; 
subplot(1,2,1); colormap(gray(256)); image(blurred_noisy_image); 
title(['blurred:', num2str(input_psnr), 'dB']); axis image; 
subplot(1,2,2); colormap(gray(256)); image(SURE_deconvolved_image); 
title(['restored:', num2str(output_psnr), 'dB']); axis image;


