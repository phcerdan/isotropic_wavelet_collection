function varargout = SURE_LET_GUI(varargin)
% SURE_LET_GUI M-file for SURE_LET_GUI.fig
%      SURE_LET_GUI, by itself, creates a new SURE_LET_GUI or raises the existing
%      singleton*.
%
%      H = SURE_LET_GUI returns the handle to a new SURE_LET_GUI or the handle to
%      the existing singleton*.
%
%      SURE_LET_GUI('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in SURE_LET_GUI.M with the given input arguments.
%
%      SURE_LET_GUI('Property','Value',...) creates a new SURE_LET_GUI or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before SURE_LET_GUI_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to SURE_LET_GUI_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help SURE_LET_GUI

% Last Modified by GUIDE v2.5 03-Dec-2012 16:02:33

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @SURE_LET_GUI_OpeningFcn, ...
                   'gui_OutputFcn',  @SURE_LET_GUI_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before SURE_LET_GUI is made visible.
function SURE_LET_GUI_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to SURE_LET_GUI (see VARARGIN)

% Choose default command line output for SURE_LET_GUI

p = mfilename('fullpath') ;


addpath(p(1:end-12));

set(gcf,'units','normalized');

handles.output = hObject;

% handles.extension = {'periodic', 'half_point'};
% handles.blur_kernel = 1;

global extension; global filter; 
extension = {'periodic', 'half_point'};     filter = {'experiment_1'};

global blur_type; blur_type = 1;
global bsnr; bsnr = 20;

global noise_variance; noise_variance = 1;

global lambda_1;    global lambda_2;    global lambda_3;    
lambda_1 = 0.0001;  lambda_2 = 0.001;  lambda_3 = 0.01;  
global lambda;      lambda = [lambda_1  lambda_2  lambda_3];

global mu; global beta;
mu = 0.05;   beta = 1e-5;

global it;      it = 3;


% Update handles structure
guidata(hObject, handles);

if strcmp(get(hObject,'Visible'),'off')
    initialize_gui(hObject, handles);
end

% UIWAIT makes SURE_LET_GUI wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = SURE_LET_GUI_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;

%%

function initialize_gui(fig_handle, handles)

setappdata(0, 'test_data', '1');

%%

%% load clean image --- x
% --- Executes on button press in load_image.
function load_image_Callback(hObject, eventdata, handles)
% hObject    handle to load_image (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% [filename,path] = uigetfile ({'*.tif';'*.jpg';'*.bmp';'*.png';'*.mat'}, 'load image files'); 

% if ( isequal(filename,0) || isequal(path,0) )
%     errordlg('Error: file not found.'); 
% else

%     file=[path filename];     [a,b, file_format]=fileparts(filename);
%     global x;
% 
%     if ( strcmp(file_format, '.tif') ==1 || strcmp(file_format, '.jpg') ==1 || strcmp(file_format, '.bmp') ==1 ...
%             || strcmp(file_format, '.png') ==1 )
%     x = imread(file);   
%     if ( size(x,3)>1 )
%         x = rgb2gray(x);  
%     end
%         x = double(x);
%     end
% 
%     if ( strcmp(file_format, '.mat') ==1 )
%         x = load(file);   
%     end

global x;  global filter; global extension; global y; global h; global noise_variance;
global y_blurred; global blur_variance;

[x,handles.img_name]=loadimage_mat();
if x==-1
else

        %     set(handles.show_clean,'HandleVisibility','ON');        axes(handles.show_clean);       imshow(x);    handles.img = x; 
    axes(handles.show_clean); colormap(gray(256)); image(x);  axis image;
    guidata(hObject, handles);
%     set(handles.,'String',x);
%     handles.show_clean = x;

%     guidata(hObject, handles); 
% end

blur_type = get (handles.blur_kernel, 'value');
       
switch blur_type
    case 1
        filter = {'experiment_1'};
        set(handles.choose_gaussian_size,'visible','off');      set(handles.slider4,'visible','off');    set(handles.text_blur_size,'visible','off');

    case 2
        filter  = {'experiment_2'};
       set(handles.choose_gaussian_size,'visible','off');      set(handles.slider4,'visible','off');     set(handles.text_blur_size,'visible','off');
       
    case 3
        prompt = 'Specify uniform blur size:';      dlg_title = 'Input the size of uniform blur';
        set(handles.choose_gaussian_size,'visible','off');      set(handles.slider4,'visible','off');    set(handles.text_blur_size,'visible','off');

        num_lines = 1;      def = {'5'};
        answer = inputdlg(prompt,dlg_title,num_lines,def);
        filter  = {'experiment_3', str2double(answer)};
        
    case 4
        set(handles.choose_gaussian_size,'visible','on');      set(handles.slider4,'visible','on');     set(handles.text_blur_size,'visible','on');

        blur_variance = get(handles.slider4,'value');  set(handles.text_blur_size,'String', num2str(blur_variance)); 
        blur_std = sqrt(blur_variance);        filter  = {'Gaussian', blur_std};
        
%         prompt = 'Specify Gaussian blur std:';      dlg_title = 'Input the std of Gaussian blur';
%         num_lines = 1;      def = {'1'};
%         answer = inputdlg(prompt,dlg_title,num_lines,def);
%         filter  = {'Gaussian', str2double(answer)};
        
end

if (isempty(handles.blur_kernel ))
     set(hObject,'String','0')
end


bsnr = get(handles.slider3,'value');  set(handles.text_bsnr, 'String', num2str(bsnr));

extension_case ='periodic';     symmetry_type = 'NA';
% extension_case ='symmetric';     symmetry_type = 'half_point';
% extension_case = extension{1};     symmetry_type = extension{2};

[y_blurred, h] = Image_blurring(x, filter, extension_case, symmetry_type );
[M, N] = size(y_blurred);
y_energy = y_blurred-mean(y_blurred(:)); y_energy = y_energy(:)'*y_energy(:)/(M*N);
noise_variance = y_energy*10^(-bsnr/10);

% set(handles.noise_edit,'String', num2str(noise_variance));

set(handles.text_noise_variance, 'String', num2str(noise_variance));

y = Image_add_noise(y_blurred, noise_variance);
axes(handles.show_blurred); colormap(gray(256)); image(y);  axis image;
c = PSNR(y,x,255); c = num2str(c);   
% set(handles.input_psnr, 'String', c);
set(handles.text_input_psnr, 'String', c);

guidata(hObject, handles); 


end
% return;

%% choose blur kernel --- H
% --- Executes on selection change in blur_kernel.
function blur_kernel_Callback(hObject, eventdata, handles)
% hObject    handle to blur_kernel (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = get(hObject,'String') returns blur_kernel contents as cell array
%        contents{get(hObject,'Value')} returns selected item from blur_kernel
blur_type = get (handles.blur_kernel, 'value');

global filter; global extension; global x; global y; global h; global noise_variance;
global y_blurred; global blur_variance;


switch blur_type
    case 1
        filter = {'experiment_1'};
        set(handles.choose_gaussian_size,'visible','off');      set(handles.slider4,'visible','off');     set(handles.text_blur_size,'visible','off');

    case 2
        filter  = {'experiment_2'};
       set(handles.choose_gaussian_size,'visible','off');      set(handles.slider4,'visible','off');     set(handles.text_blur_size,'visible','off');
       
    case 3
        prompt = 'Specify uniform blur size:';      dlg_title = 'Input the size of uniform blur';
        set(handles.choose_gaussian_size,'visible','off');      set(handles.slider4,'visible','off');     set(handles.text_blur_size,'visible','off');

        num_lines = 1;      def = {'5'};
        answer = inputdlg(prompt,dlg_title,num_lines,def);
        filter  = {'experiment_3', str2double(answer)};
        
    case 4
        set(handles.choose_gaussian_size,'visible','on');      set(handles.slider4,'visible','on');     set(handles.text_blur_size,'visible','on');

        blur_variance = get(handles.slider4,'value');  set(handles.text_blur_size,'String', num2str(blur_variance)); 
        blur_std = sqrt(blur_variance);        filter  = {'Gaussian', blur_std};
        
        
%         prompt = 'Specify Gaussian blur std:';      dlg_title = 'Input the std of Gaussian blur';
%         num_lines = 1;      def = {'1'};
%         answer = inputdlg(prompt,dlg_title,num_lines,def);
%         filter  = {'Gaussian', str2double(answer)};
        
end
        
if (isempty(handles.blur_kernel ))
     set(hObject,'String','0')
end


bsnr = get(handles.slider3,'value');  set(handles.text_bsnr,'String', num2str(bsnr));

[M, N] = size(y_blurred); mean_y = mean(y_blurred(:));
y_energy = y_blurred-mean_y; y_energy = y_energy(:)'*y_energy(:)/(M*N);
noise_variance = y_energy*10^(-bsnr/10);

set(handles.text_noise_variance,'String', num2str(noise_variance));

extension_case ='periodic';     symmetry_type = 'NA';
% extension_case ='symmetric';     symmetry_type = 'half_point';
% extension_case = extension{1};     symmetry_type = extension{2};

[y_blurred, h] = Image_blurring(x, filter, extension_case, symmetry_type );
y = Image_add_noise(y_blurred, noise_variance);
axes(handles.show_blurred); colormap(gray(256)); image(y);  axis image;
c = PSNR(y,x,255); c = num2str(c);   
% set(handles.input_psnr, 'String', c);
set(handles.text_input_psnr, 'String', c);

guidata(hObject, handles); 

return;



% --- Executes during object creation, after setting all properties.
function blur_kernel_CreateFcn(hObject, eventdata, handles)
% hObject    handle to blur_kernel (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end





%% choose noise variance

function noise_edit_Callback(hObject, eventdata, handles)
% hObject    handle to noise_edit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of noise_edit as text
%        str2double(get(hObject,'String')) returns contents of noise_edit as a double

% str=get(handles.slider3,'value'); set(handles.noise_edit,'String',num2str(str));
% noise_edit = str2double( get(handles.noise_edit, 'String') );

% sure_c = PSNR(sure_image,x,255);    
% sure_c = num2str(sure_c);   set(handles.sure_psnr, 'String', sure_c);

% aa=get(handles.slider3,'value');
% set(handles.noise_edit,'String',num2str(aa));
% 
% return;

% --- Executes during object creation, after setting all properties.
function noise_edit_CreateFcn(hObject, eventdata, handles)
% hObject    handle to noise_edit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

%% choose extension case for performing convolution
% --- Executes on selection change in extension.
function extension_Callback(hObject, eventdata, handles)
% hObject    handle to extension (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = get(hObject,'String') returns extension contents as
% cell array
%        contents{get(hObject,'Value')} returns selected item from extension
extension_type = get (handles.extension, 'value');
global extension;

switch extension_type
    case 1
%         set(handles.extension, 'String', {'periodic', 'half_point'});
        extension = {'periodic', 'half_point'};
        
    case 2
%         set(handles.extension, 'String', {'symmetric', 'half_point'});
        extension = {'symmetric', 'half_point'};
        
    case 3
%         set(handles.extension, 'String', {'symmetric', 'whole_point'});
        extension = {'symmetric', 'whole_point'};
        
end
        
if (isempty(handles.extension))
     set(hObject,'String','0')
end

guidata(hObject, handles);

return;



% --- Executes during object creation, after setting all properties.
function extension_CreateFcn(hObject, eventdata, handles)
% hObject    handle to extension (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

%% corrupt button --- perform y=Hx+n
% --- Executes on button press in corrupt.
% function corrupt_Callback(hObject, eventdata, handles)
% % hObject    handle to corrupt (see GCBO)
% % eventdata  reserved - to be defined in a future version of MATLAB
% % handles    structure with handles and user data (see GUIDATA)
% 
% % filter = get(handles.blur_kernel,'String');
% 
% global filter; global extension; global x; global y; global h; global noise_variance; global y_blurred;
% 
% noise_variance = get(handles.slider3,'value');
% 
% aa=get(handles.slider3,'value'); set(handles.noise_edit,'String',num2str(aa));
% 
% % filter = handles.blur_kernel;
% % x = str2double( get(handles.show_clean, 'value')  );
% % x =  get(handles.show_clean) ;
% % x = handles.show_clean;
% % bb = get(handles.extension, 'String');
% % bb = handles.extension;
% 
% extension_case ='periodic';     symmetry_type = 'NA';
% % extension_case ='symmetric';     symmetry_type = 'half_point';
% % extension_case = extension{1};     symmetry_type = extension{2};
% 
% [y_blurred, h] = Image_blurring(x, filter, extension_case, symmetry_type );
% y = Image_add_noise(y_blurred, noise_variance);
% 
% axes(handles.show_blurred); colormap(gray(256)); image(y);  axis image;
% 
% c = PSNR(y,x,255); c = num2str(c);   set(handles.input_psnr, 'String', c);
% 
% guidata(hObject, handles); 
% 
% return;
% 

%% compute input PSNR between y and x

function input_psnr_Callback(hObject, eventdata, handles)
% hObject    handle to input_psnr (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of input_psnr as text
%        str2double(get(hObject,'String')) returns contents of input_psnr as a double


% --- Executes during object creation, after setting all properties.
function input_psnr_CreateFcn(hObject, eventdata, handles)
% hObject    handle to input_psnr (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end




%% perform SURE-LET deconvolution
% --- Executes on button press in start.
function start_Callback(hObject, eventdata, handles)
% hObject    handle to start (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global lambda_1; global lambda_2; global lambda_3;
global beta; global mu; global x; global y; global noise_variance;
global h; global it; global extension;
global sure_image; global mse_image; global running_time;

% global lambda; lambda = [lambda_1  lambda_2  lambda_3] * noise_variance;


extension_case ='periodic';     symmetry_type = 'NA';
% extension_case ='symmetric';     symmetry_type = 'half_point';

if noise_variance == 0
    noise_variance = eps;
end

tic;
sure_image =   SURE_LET_Deconvolution  (y, h,  noise_variance,  extension_case, symmetry_type );
running_time = toc;
running_time = num2str(running_time);   set(handles.text_running_time, 'String', running_time);

axes(handles.show_sure); colormap(gray(256)); image(sure_image);  axis image;
input_c = PSNR(y,x,255); input_c = num2str(input_c);   
% set(handles.input_psnr, 'String', input_c);
set(handles.text_input_psnr, 'String', input_c);

output_c = PSNR(sure_image,x,255);    output_c = num2str(output_c);   
set(handles.text_output_psnr, 'String', output_c);

guidata(hObject, handles); 



function sure_psnr_Callback(hObject, eventdata, handles)
% hObject    handle to sure_psnr (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of sure_psnr as text
%        str2double(get(hObject,'String')) returns contents of sure_psnr as a double


% --- Executes during object creation, after setting all properties.
function sure_psnr_CreateFcn(hObject, eventdata, handles)
% hObject    handle to sure_psnr (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end




% --- Executes on button press in save_observed.
function save_observed_Callback(hObject, eventdata, handles)
% hObject    handle to save_observed (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% global y;
% 
% [filename,pathname]=uiputfile({'*.tif';'*.jpg';'*.bmp';'*.png'},'save image as'); 
% file=[pathname,filename]; 
% [pathstr,name,ext,versn] = fileparts(file);
% 
% if isempty(ext)
% file=[file,'.tif'];
% end
% 
% % I = getframe(gca);  imwrite(I.cdata,file) ;
% 
% I = uint8(y); imwrite(I,file); 
% 


global y;
[filename,pathname]=uiputfile({ '*.tif; *.bmp; *.gif; *.jpg','Image files (*.tif, *.bmp, *.gif, *.jpg)'; ...
    '*.mat','Mat files (*.mat)';  '*.*', 'All files (*.*)'},'Select the Picture');
file=[pathname,filename]; 


if isequal(filename,0) | isequal(pathname,0)
    image=-1;    im_name = ' ';

else
[pathstr,name,ext,versn] = fileparts(file);

if isempty(ext)
file=[file,'.tif'];
end

% I = uint8(sure_image); imwrite(I,file);

y = uint8(y);
if strcmp(ext, '.mat') == 1
    save observed.mat  y;
else
    I = uint8(y); imwrite(I,file);
end

end

return;

% --- Executes on button press in save_sure.
function save_sure_Callback(hObject, eventdata, handles)
% hObject    handle to save_sure (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

global sure_image;
[filename,pathname]=uiputfile({ '*.tif; *.bmp; *.gif; *.jpg','Image files (*.tif, *.bmp, *.gif, *.jpg)'; ...
    '*.mat','Mat files (*.mat)'; '*.*', 'All files (*.*)'},'Select the Picture');
file=[pathname,filename]; 

if isequal(filename,0) | isequal(pathname,0)
    image=-1;    im_name = ' ';

else
[pathstr,name,ext,versn] = fileparts(file);

if isempty(ext)
file=[file,'.tif'];
end

sure_image = uint8(sure_image);
if strcmp(ext, '.mat') == 1
    save restored.mat  sure_image;
else
    I = uint8(sure_image); imwrite(I,file);
end

end

return;


% --- Executes on button press in save_mse.
function save_mse_Callback(hObject, eventdata, handles)
% hObject    handle to save_mse (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global mse_image;

[filename,pathname]=uiputfile({'*.tif';'*.jpg';'*.bmp';'*.png'},'save image as'); 
file=[pathname,filename]; 
[pathstr,name,ext,versn] = fileparts(file);

if isempty(ext)
file=[file,'.tif'];
end

% I = getframe(gca);  imwrite(I.cdata,file) ;

I = uint8(mse_image); imwrite(I,file); 

return;


% --- Executes on slider movement.
function slider3_Callback(hObject, eventdata, handles)
% hObject    handle to slider3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider

global filter; global extension; global x; global y; global h; global noise_variance;
global y_blurred; global blur_variance;

bsnr = get(handles.slider3,'value');   
% set(handles.edit21,'String', num2str(bsnr));
set(handles.text_bsnr,'String', num2str(bsnr));

[M, N] = size(y_blurred); mean_y = mean(y_blurred(:));
y_energy = y_blurred-mean_y; y_energy = y_energy(:)'*y_energy(:)/(M*N);
noise_variance = y_energy*10^(-bsnr/10);

% set(handles.noise_edit,'String',num2str(noise_variance));
set(handles.text_noise_variance, 'String',num2str(noise_variance));

extension_case ='periodic';     symmetry_type = 'NA';
% extension_case ='symmetric';     symmetry_type = 'half_point';
% extension_case = extension{1};     symmetry_type = extension{2};

[y_blurred, h] = Image_blurring(x, filter, extension_case, symmetry_type );
y = Image_add_noise(y_blurred, noise_variance);
axes(handles.show_blurred); colormap(gray(256)); image(y);  axis image;
c = PSNR(y,x,255); c = num2str(c);   
% set(handles.input_psnr, 'String', c);
set(handles.text_input_psnr, 'String', c);

guidata(hObject, handles); 

return;

% --- Executes during object creation, after setting all properties.
function slider3_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on button press in pushbutton8.
function pushbutton8_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton8 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

selection=questdlg(['Are you sure to quit ',get(gcf,'Name'),' program?'], ... 
    ['Close ',get(gcf,'Name'),'...'],'Yes','No','Yes');
if strcmp(selection,'No')
    return;                                    
else
      clc;      clear all; close all;
    delete(gcf);
    
end

return;



function time_Callback(hObject, eventdata, handles)
% hObject    handle to time (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of time as text
%        str2double(get(hObject,'String')) returns contents of time as a double


% --- Executes during object creation, after setting all properties.
function time_CreateFcn(hObject, eventdata, handles)
% hObject    handle to time (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit21_Callback(hObject, eventdata, handles)
% hObject    handle to edit21 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit21 as text
%        str2double(get(hObject,'String')) returns contents of edit21 as a double
aa = get(handles.slider3,'value'); 
set(handles.edit21,'String', num2str(aa));
return;

% --- Executes during object creation, after setting all properties.
function edit21_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit21 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on slider movement.
function slider4_Callback(hObject, eventdata, handles)
% hObject    handle to slider4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider
 
global filter; global extension; global x; global y; global h; global noise_variance;
global y_blurred; global blur_variance;

blur_variance = get(handles.slider4,'value');  set(handles.text_blur_size,'String', num2str(blur_variance)); 
bsnr = get(handles.slider3,'value');   
set(handles.text_bsnr, 'String', num2str(bsnr));

[M, N] = size(y_blurred); mean_y = mean(y_blurred(:));
y_energy = y_blurred-mean_y; y_energy = y_energy(:)'*y_energy(:)/(M*N);
noise_variance = y_energy*10^(-bsnr/10);

set(handles.text_noise_variance, 'String', num2str(noise_variance));

extension_case ='periodic';     symmetry_type = 'NA';
% extension_case ='symmetric';     symmetry_type = 'half_point';
% extension_case = extension{1};     symmetry_type = extension{2};

blur_std = sqrt(blur_variance);        filter  = {'Gaussian', blur_std};
[y_blurred, h] = Image_blurring(x, filter, extension_case, symmetry_type );
y = Image_add_noise(y_blurred, noise_variance);
axes(handles.show_blurred); colormap(gray(256)); image(y);  axis image;
c = PSNR(y,x,255); c = num2str(c);   set(handles.text_input_psnr, 'String', c);

guidata(hObject, handles); 

return;

% --- Executes during object creation, after setting all properties.
function slider4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end



function show_blur_variance_Callback(hObject, eventdata, handles)
% hObject    handle to show_blur_variance (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of show_blur_variance as text
%        str2double(get(hObject,'String')) returns contents of show_blur_variance as a double


% --- Executes during object creation, after setting all properties.
function show_blur_variance_CreateFcn(hObject, eventdata, handles)
% hObject    handle to show_blur_variance (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


