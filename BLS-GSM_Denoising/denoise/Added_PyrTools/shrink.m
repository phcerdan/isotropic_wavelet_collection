function ts = shrink(t,f)

%	im_shr = shrink(im0, f)
%
% It shrinks (spatially) an image into a factor f
% in each dimension. It does it by cropping the
% Fourier transform of the image.

% JPM, 5/1/95.

% Revised so it can work also with exponents of 3 factors: JPM 5/2003

% odd           = 0     1     2     3     4     5     6
% fftshift(odd) = 4     5     6     0     1     2     3

% even           = 0     1     2     3     4     5     6     7
% fftshift(even) = 4     5     6     7     0     1     2     3

[my,mx] = size(t);
T = fftshift(fft2(t))/f^2;
Ts = zeros(floor(my/f),floor(mx/f));
% outputsize = floor(mx/f)

cy = ceil(my/2);
cx = ceil(mx/2);
evenmy = (my/2==floor(my/2));
evenmx = (mx/2==floor(mx/2));

% if even:
% y1 = my/2 + 2 - outsize/2
% if odd:
% y1 = my/2 + 1 - outsize/2
y1 = cy + 2*evenmy - floor(my/(2*f));
y2 = cy + floor(my/(2*f));
x1 = cx + 2*evenmx - floor(mx/(2*f));
x2 = cx + floor(mx/(2*f));
% Even, f=3, mxOri = 6, cx = 3, mxOut = mxOrig/f = 2
% input:evenmx = 1
% x1 = 3 + 2 - 1 = 4
% x2 = 3     + 1 = 4
% Ts(2:2) = T(4:4)
% if even
% Ts(1,2:2) = 0.5[ T(y1 -1, 4:4) + T(y2+1, 4:4) ]

Ts(1+evenmy:floor(my/f),1+evenmx:floor(mx/f))=T(y1:y2,x1:x2);
% Nyquist frequencies (or largest frequencies if odd) fix:
if evenmy,
    Ts(1+evenmy:my/f,1)=(T(y1:y2,x1-1)+T(y1:y2,x2+1))/2;
end
if evenmx,
    Ts(1,1+evenmx:mx/f)=(T(y1-1,x1:x2)+T(y2+1,x1:x2))/2;
end
if evenmy & evenmx,
    Ts(1,1)=(T(y1-1,x1-1)+T(y1-1,x2+1)+T(y2+1,x1-1)+T(y2+1,x2+1))/4;
end    
Ts = fftshift(Ts);
Ts = shift(Ts, [1 1] - [evenmy evenmx]);
ts = ifft2(Ts);
