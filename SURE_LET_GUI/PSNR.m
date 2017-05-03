function psnr = PSNR(x,y,option)
[M,N] = size(x);
dif = x-y;
MSE = dif(:)'*dif(:)/(M*N);

if option == 255
psnr = 10*log10(255^2/MSE);
end

if option == 1
psnr = 10*log10(1/MSE);
end


if option == 0
x_energy = x(:)'*x(:); x_energy = x_energy/(M*N);
psnr = 10*log10(x_energy/MSE);
end

return;