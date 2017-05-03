function y = Haar_irdwt  (x,orientation,scale,extension_case,type_of_symmetry)


%% periodic boundary condition
if (strcmp(extension_case, 'periodic') == 1)
candidate = x; [M,N] = size(x);

% scale = 2;
if scale > 1
power = scale-2; power_x = 0:1:power;  shift = 2.^power_x; 
end

if scale > 1
    
for i = 1:scale-1
    shift_i = shift(i);  
candidate = candidate+[candidate(:,end-shift_i+1:end) candidate(:,1:end-shift_i)]; 
candidate = candidate+[candidate(end-shift_i+1:end,:); candidate(1:end-shift_i,:)]; 
end

end


shift_final = 2^(scale-1);    a = 1/8^scale;
a1_N = (N-shift_final+1):N;  a2_N = 1:(N-shift_final);   %   a_N = [a1_N  a2_N];
a1_M = (M-shift_final+1):M;  a2_M = 1:(M-shift_final);   %  a_M = [a1_M  a2_M];

switch orientation

    case 1
y_i = candidate+[candidate(:,a1_N) candidate(:,a2_N)];
y_i = a*(y_i-[y_i(a1_M,:); y_i(a2_M,:)]); 
        
% y_i = candidate+candidate(:,a_N);   y_i = a*(y_i-y_i(a_M,:)); 

    case 2
y_i = candidate-[candidate(:,a1_N) candidate(:,a2_N)]; 
y_i = a*(y_i+[y_i(a1_M,:); y_i(a2_M,:)]); 

% y_i = candidate-candidate(:,a_N);   y_i = a*(y_i+y_i(a_M,:)); 

    case 0
y_i = candidate-[candidate(:,a1_N) candidate(:,a2_N)]; 
y_i = a*(y_i-[y_i(a1_M,:); y_i(a2_M,:)]); 

% y_i = candidate-candidate(:,a_N);   y_i = a*(y_i-y_i(a_M,:)); 

    case 3
y_i = candidate+[candidate(:,a1_N) candidate(:,a2_N)]; 
y_i = a*(y_i+[y_i(a1_M,:); y_i(a2_M,:)]); 

% y_i = candidate+candidate(:,a_N);   y_i = a*(y_i+y_i(a_M,:)); 

end

y = y_i;

end


%% symmetric boundary condition
if (strcmp(extension_case, 'symmetric') == 1)
    
    if (strcmp(type_of_symmetry, 'half_point') == 1)
candidate = x;  shift_final = 2^(scale-1);

switch orientation

    case 1
        
          y_i = candidate+[candidate(:,shift_final:-1:1)  candidate(:,1:end-shift_final)]; 
          y_i = y_i-[y_i(shift_final:-1:1,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
        
    case 2
        
          y_i = candidate-[candidate(:,shift_final:-1:1)  candidate(:,1:end-shift_final)]; 
          y_i = y_i + [y_i(shift_final:-1:1,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
      
    case 0
        
          y_i = candidate-[candidate(:,shift_final:-1:1)  candidate(:,1:end-shift_final)]; 
          y_i = y_i - [y_i(shift_final:-1:1,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
          
    case 3
        
          y_i = candidate + [candidate(:,shift_final:-1:1)  candidate(:,1:end-shift_final)]; 
          y_i = y_i + [y_i(shift_final:-1:1,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
       
end


% % correcting after the first reconstruction from most inner level
candidate = y_i(2*shift_final:-1:shift_final+1,shift_final+1:end); 
y_i(1:shift_final,shift_final+1:end)=2*y_i(1:shift_final,shift_final+1:end)-candidate;
candidate = y_i(shift_final+1:end,2*shift_final:-1:shift_final+1); 
y_i(shift_final+1:end,1:shift_final)=2*y_i(shift_final+1:end,1:shift_final)-candidate;

second_term = y_i(1:shift_final,2*shift_final:-1:shift_final+1); 
third_term = y_i(2*shift_final:-1:shift_final+1,1:shift_final);
fourth_term = y_i(2*shift_final:-1:shift_final+1,2*shift_final:-1:shift_final+1);
y_i(1:shift_final,1:shift_final) = 4*y_i(1:shift_final,1:shift_final)-second_term-third_term-fourth_term;

% then, to reconstruct the other outer levels, simply add

% scale = 5;
if scale > 1
power = scale-2; power_x = 0:1:power;  shift = 2.^power_x; 
end


if scale > 1
    
for i = scale-1:-1:1
    shift_i = shift(i);
    
y_i = y_i+[y_i(:,shift_i:-1:1)  y_i(:,1:end-shift_i)]; y_i = y_i+[y_i(shift_i:-1:1,:); y_i(1:end-shift_i,:)];  
y_i = (1/8)*y_i;

candidate = y_i(2*shift_i:-1:shift_i+1,shift_i+1:end); 
y_i(1:shift_i,shift_i+1:end)=2*y_i(1:shift_i,shift_i+1:end)-candidate;
candidate = y_i(shift_i+1:end,2*shift_i:-1:shift_i+1); 
y_i(shift_i+1:end,1:shift_i)=2*y_i(shift_i+1:end,1:shift_i)-candidate;

second_term = y_i(1:shift_i,2*shift_i:-1:shift_i+1); third_term = y_i(2*shift_i:-1:shift_i+1,1:shift_i);
fourth_term = y_i(2*shift_i:-1:shift_i+1,2*shift_i:-1:shift_i+1);
y_i(1:shift_i,1:shift_i) = 4*y_i(1:shift_i,1:shift_i)-second_term-third_term-fourth_term;

end
end
y = y_i;
    end
    
    
    
    if (strcmp(type_of_symmetry, 'whole_point') == 1)
        
candidate = x;   shift_final = 2^(scale-1);

switch orientation

    case 1
        
          y_i = candidate+[candidate(:,shift_final+1:-1:2)  candidate(:,1:end-shift_final)]; 
          y_i = y_i-[y_i(shift_final+1:-1:2,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
        
    case 2
        
          y_i = candidate-[candidate(:,shift_final+1:-1:2)  candidate(:,1:end-shift_final)]; 
          y_i = y_i + [y_i(shift_final+1:-1:2,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
      
    case 0
        
          y_i = candidate-[candidate(:,shift_final+1:-1:2)  candidate(:,1:end-shift_final)]; 
          y_i = y_i - [y_i(shift_final+1:-1:2,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
          
    case 3
        
          y_i = candidate + [candidate(:,shift_final+1:-1:2)  candidate(:,1:end-shift_final)]; 
          y_i = y_i + [y_i(shift_final+1:-1:2,:); y_i(1:end-shift_final,:)]; y_i = (1/8)*y_i;
       
end


% % correcting after the first reconstruction from most inner level

candidate = y_i(2*shift_final+1:-1:shift_final+2,shift_final+1:end); 
y_i(1:shift_final,shift_final+1:end)=2*y_i(1:shift_final,shift_final+1:end)-candidate;
candidate = y_i(shift_final+1:end,2*shift_final+1:-1:shift_final+2); 
y_i(shift_final+1:end,1:shift_final)=2*y_i(shift_final+1:end,1:shift_final)-candidate;

second_term = y_i(1:shift_final,2*shift_final+1:-1:shift_final+2); 
third_term = y_i(2*shift_final+1:-1:shift_final+2,1:shift_final);
fourth_term = y_i(2*shift_final+1:-1:shift_final+2,2*shift_final+1:-1:shift_final+2);
y_i(1:shift_final,1:shift_final) = 4*y_i(1:shift_final,1:shift_final)-second_term-third_term-fourth_term;

% then, to reconstruct the other outer levels, simply add

% scale = 5;
if scale > 1
power = scale-2; power_x = 0:1:power;  shift = 2.^power_x; 
end

if scale > 1
    
for i = scale-1:-1:1
    shift_i = shift(i);
    y_i = y_i+[y_i(:,shift_i+1:-1:2)  y_i(:,1:end-shift_i)]; y_i = y_i+[y_i(shift_i+1:-1:2,:); y_i(1:end-shift_i,:)];  
    y_i = (1/8)*y_i;

candidate = y_i(2*shift_i+1:-1:shift_i+2,shift_i+1:end); 
y_i(1:shift_i,shift_i+1:end)=2*y_i(1:shift_i,shift_i+1:end)-candidate;
candidate = y_i(shift_i+1:end,2*shift_i+1:-1:shift_i+2); 
y_i(shift_i+1:end,1:shift_i)=2*y_i(shift_i+1:end,1:shift_i)-candidate;

second_term = y_i(1:shift_i,2*shift_i+1:-1:shift_i+2); 
third_term = y_i(2*shift_i+1:-1:shift_i+2,1:shift_i);
fourth_term = y_i(2*shift_i+1:-1:shift_i+2,2*shift_i+1:-1:shift_i+2);
y_i(1:shift_i,1:shift_i) = 4*y_i(1:shift_i,1:shift_i)-second_term-third_term-fourth_term;

end

end
y = y_i;
    end
    
end


return;


