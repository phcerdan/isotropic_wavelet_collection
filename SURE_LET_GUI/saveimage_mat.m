
%% salva una imagen .mat o de otro tipo y si cancelas devuelve -1 si no 1

function [value,name]= saveimage_mat(image,default_name)
[filename, pathname, filterindex] = uiputfile( { '*.mat'}, 'Save as',default_name);
if isequal(filename,0) | isequal(pathname,0)
    value=-1;
    name = '';
else

name = strcat(pathname, filename);
[PATHSTR,fname,EXT,VERSN] = fileparts(strcat(pathname, filename));

if(size(EXT,2)~=4)
    switch filterindex
        case 1
            total_name = strcat(name,'.mat');
            save(total_name,'image');
        case 2
            total_name = strcat(name,'.bmp');
            saving=fullfile(pathname, filename);
            imwrite(image, gray(256), saving);
        case 3
            total_name = strcat(name,'.tif');
            saving=fullfile(pathname, filename);
            imwrite(image, gray(256), saving);
        otherwise
            value=-1;
            name = '';
    end
else    
name = strcat(pathname,fname);


if EXT == '.mat'
    save (name ,'image');
else
    saving=fullfile(pathname, filename);
    imwrite(image, gray(256), saving);
end
end
value=1;
end