function varargout = gaussian_blur_size (varargin)
% GAUSSIAN_BLUR_SIZE M-file for gaussian_blur_size.fig
%      GAUSSIAN_BLUR_SIZE, by itself, creates a new GAUSSIAN_BLUR_SIZE or raises the existing
%      singleton*.
%
%      H = GAUSSIAN_BLUR_SIZE returns the handle to a new GAUSSIAN_BLUR_SIZE or the handle to
%      the existing singleton*.
%
%      GAUSSIAN_BLUR_SIZE('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in GAUSSIAN_BLUR_SIZE.M with the given input arguments.
%
%      GAUSSIAN_BLUR_SIZE('Property','Value',...) creates a new GAUSSIAN_BLUR_SIZE or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before gaussian_blur_size_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to gaussian_blur_size_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help gaussian_blur_size

% Last Modified by GUIDE v2.5 03-Dec-2012 14:38:06

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @gaussian_blur_size_OpeningFcn, ...
                   'gui_OutputFcn',  @gaussian_blur_size_OutputFcn, ...
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


% --- Executes just before gaussian_blur_size is made visible.
function gaussian_blur_size_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to gaussian_blur_size (see VARARGIN)

% Choose default command line output for gaussian_blur_size
handles.output = hObject;
handles.slider1 = varargin{1};
handles.edit1 = varargin{2};

% Update handles structure
guidata(hObject, handles);
% 
% if strcmp(get(hObject,'Visible'),'off')
%     initialize_gui(hObject, handles);
% end

% UIWAIT makes gaussian_blur_size wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = gaussian_blur_size_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;



% function initialize_gui(fig_handle, handles)
% 
% % set(handles.slider_value, 'String', getappdata(0, 'test_data'));
% set(handles.slider1, 'Value', str2num(get(handles.slider_value, 'String')));
% set(handles.edit1, 'Value', get(handles.slider_value, 'String') );


% --- Executes on slider movement.
function slider1_Callback(hObject, eventdata, handles)
% hObject    handle to slider1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider
global blur_variance;  global handles_edit1;
% guidata(hObject, handles); 

% blur_variance = get(hObject.slider1,'value');   set(hObject.edit1,'String', num2str(blur_variance));
% set(handles.slider_value, 'String', num2str(ceil(get(handles.slider1, 'value'))) );

blur_variance = get(hObject,'value');   

set(handles.edit1, 'String', num2str(blur_variance));

guidata(hObject, handles);
return;

% --- Executes during object creation, after setting all properties.
function slider1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end



function edit1_Callback(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit1 as text
%        str2double(get(hObject,'String')) returns contents of edit1 as a double
global handles_edit1;  global blur_variance;

handles_edit1 = gcbo;

set(hObject,'String', num2str(blur_variance));

guidata(hObject, handles);

return;

% --- Executes during object creation, after setting all properties.
function edit1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
setappdata(0, 'test_data', get(handles.slider_value, 'String'));
close;

return;


% --- Executes on button press in pushbutton2.
function pushbutton2_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
close;
return;

