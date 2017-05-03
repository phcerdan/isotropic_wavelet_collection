%% load image of any format

function [image,im_name]= loadimage_mat()
[filename,pathname] = uigetfile({ '*.tif; *.bmp; *.gif; *.jpg','Image files (*.tif, *.bmp, *.gif, *.jpg)'; ...
    '*.mat','Mat files (*.mat)';  '*.*', 'All files (*.*)'},'Select the Picture');

if isequal(filename,0) | isequal(pathname,0)
    image=-1;
    im_name = ' ';
else
    f_image = fullfile(pathname, filename);

[PATHSTR,NAME,EXT,VERSN] = fileparts(filename);

if EXT =='.mat'
load (f_image);
 es=whos ('-file', f_image);
name_im = es(1).name;
 eval(['im= '  name_im ';']);
 
else
    
im = (imread(f_image));
end

a = size(im,3);
if a == 3
     im = rgb2gray(im);
else
end

image= double(im);
im_name = NAME;
end