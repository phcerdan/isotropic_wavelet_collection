function est_noise_variance = estimation_noise_variance (y)

[M,N] = size(y);
M_min = 2^(floor(log2(M))); N_min = 2^(floor(log2(N))); N_min = min(M_min,N_min);
y_cut = y(1:N_min,1:N_min);


WaveletFiltType1 = daubcqf(6,'min');  DecompLevels = 3;


% WaveletFiltType2 = daubcqf(2,'min');  ThreshFactor = 3.0; 
 
[tempW, tempS] = mrdwtcycle2D(y_cut,WaveletFiltType1,DecompLevels);
clear tempS;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Noise variance estimation using median estimator
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  temp = abs(tempW(:,:,DecompLevels,3));
  sigma_est = median(temp(:))/.67;

  clear temp tempW
  est_noise_variance = sigma_est^2;

return;

%% embedded function 1: daubcqf
function [h_0,h_1] = daubcqf(N,TYPE)

if(nargin < 2),
  TYPE = 'min';
end;
if(rem(N,2) ~= 0),
  error('No Daubechies filter exists for ODD length');
end;
K = N/2;
a = 1;
p = 1;
q = 1;
h_0 = [1 1];
for j  = 1:K-1,
  a = -a * 0.25 * (j + K - 1)/j;
  h_0 = [0 h_0] + [h_0 0];
  p = [0 -p] + [p 0];
  p = [0 -p] + [p 0];
  q = [0 q 0] + a*p;
end;
q = sort(roots(q));
qt = q(1:K-1);
if TYPE=='mid',
  if rem(K,2)==1,  
    qt = q([1:4:N-2 2:4:N-2]);
  else
    qt = q([1 4:4:K-1 5:4:K-1 N-3:-4:K N-4:-4:K]);
  end;
end;
h_0 = conv(h_0,real(poly(qt)));
h_0 = sqrt(2)*h_0/sum(h_0); 	%Normalize to sqrt(2);
if(TYPE=='max'),
  h_0 = fliplr(h_0);
end;
if(abs(sum(h_0 .^ 2))-1 > 1e-4) 
  error('Numerically unstable for this value of "N".');
end;
h_1 = rot90(h_0,2);
h_1(1:2:N)=-h_1(1:2:N);

return;


%% embedded function 2: mrdwt cycle
function [yw,ys] = mrdwtcycle2D(x, h, L)

[ys,yh] = mrdwt(x, h, L);
N = size(x,1);
yw = zeros(N,N,L,3);

for ll = 1:L
  yw(:,:,L-ll+1,2) = ...
      flipud(fliplr(cshift2(yh(:,(ll-1)*N*3+1:(ll-1)*N*3+N),-1,-1)))';
  yw(:,:,L-ll+1,1) = ...
      flipud(fliplr(cshift2(yh(:,(ll-1)*N*3+N+1:(ll-1)*N*3+2*N),-1,-1)))';
  yw(:,:,L-ll+1,3) = ...
      flipud(fliplr(cshift2(yh(:,(ll-1)*N*3+2*N+1:ll*N*3),-1,-1)))';
% $$$   yw(:,:,L-ll+1,2) = ...
% $$$       cshift2(flipud(fliplr(yh(:,(ll-1)*N*3+1:(ll-1)*N*3+N))),1,1)';
% $$$   yw(:,:,L-ll+1,1) = ...
% $$$       cshift2(flipud(fliplr(yh(:,(ll-1)*N*3+N+1:(ll-1)*N*3+2*N))),1,1)';
% $$$   yw(:,:,L-ll+1,3) = ...
% $$$       cshift2(flipud(fliplr(yh(:,(ll-1)*N*3+2*N+1:ll*N*3))),1,1)';

  
end

return;



