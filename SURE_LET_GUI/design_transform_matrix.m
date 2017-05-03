function [matrix_1, matrix_2, matrix_3, matrix_4, matrix_5, matrix_6, matrix_7, matrix_8] = ...
    design_transform_matrix(M,N,extension_case,type_of_symmetry,option)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% this program is to generate some transformation matrices used for DCT

% input paramters:
% x specifies the size of Q, to keep insistence
% cutoff-frequency is the band-width of the passband
% output paramters:
% Q_low  is the generated low-pass  filter, including only ones and zeros
% Q_high is the generated high-pass filter, including only ones and zeros

% returned matrix:
% matrix_1: DCT  of h of size M (type I or II)
% matrix_2: DCT  of h of size N (type I or II)
% matrix_7: IDCT of h of size M (type I or II)
% matrix_8: IDCT of h of size N (type I or II)

% matrix_3: DCT  of x of size M (type I or II)
% matrix_4: IDCT of x of size M (type I or II)
% matrix_5: DCT  of x of size N (type I or II)
% matrix_6: IDCT of x of size N (type I or II)
% Remark:
% (1): matrix 1--6 all used in half-point symmetric extension of x
% (2): matrix 3--6 all used in whole-point symmetric extension of x 
% (matrix 1--2 is not used, being equal to matrix 3 and 5, respectively)
% (3): all matrix 1--6 are not used in DFT consideration
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% periodic boundary extension
if (strcmp(extension_case, 'periodic') == 1)
matrix_1 = 0; matrix_2 = 0; matrix_3 = 0; matrix_4 = 0; 
matrix_5 = 0; matrix_6 = 0; matrix_7 = 0; matrix_8 = 0;
end


%% half-point symmetric boundary condition, with DFT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'half_point') == 1) ...
        &&  (strcmp(option, 'DFT') == 1)
matrix_1 = 0; matrix_2 = 0; matrix_3 = 0; matrix_4 = 0; 
matrix_5 = 0; matrix_6 = 0; matrix_7 = 0; matrix_8 = 0;
end


%% half-point symmetric boundary condition, with DCT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'half_point') == 1) ...
        && (strcmp(option, 'DCT') == 1)
    
if (M == N)
[DCT_2_matrix,IDCT_2_matrix] = DCT_type2_matrix(N); 
[DCT_1_filter_matrix, IDCT_1_filter_matrix] = DCT_type1_matrix(N+1);
matrix_1 = DCT_1_filter_matrix; matrix_2 = DCT_1_filter_matrix;
matrix_3 = DCT_2_matrix; matrix_4 = IDCT_2_matrix;
matrix_5 = DCT_2_matrix; matrix_6 = IDCT_2_matrix;
matrix_7 = IDCT_1_filter_matrix; matrix_8 = IDCT_1_filter_matrix;
end

if (M ~= N)
[DCT_2_matrix_M,IDCT_2_matrix_M] = DCT_type2_matrix(M); 
[DCT_2_matrix_N,IDCT_2_matrix_N] = DCT_type2_matrix(N);
[DCT_1_filter_matrix_M, IDCT_1_filter_matrix_M] = DCT_type1_matrix(M+1);
[DCT_1_filter_matrix_N, IDCT_1_filter_matrix_N] = DCT_type1_matrix(N+1);
matrix_1 = DCT_1_filter_matrix_M; matrix_2 = DCT_1_filter_matrix_N;
matrix_3 = DCT_2_matrix_M; matrix_4 = IDCT_2_matrix_M;
matrix_5 = DCT_2_matrix_N; matrix_6 = IDCT_2_matrix_N;
matrix_7 = IDCT_1_filter_matrix_M; matrix_8 = IDCT_1_filter_matrix_N;
end

end


%% whole-point symmetric boundary condition, with DFT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'whole_point') == 1) ...
        && (strcmp(option, 'DFT') == 1)
matrix_1 = 0; matrix_2 = 0; matrix_3 = 0; matrix_4 = 0; 
matrix_5 = 0; matrix_6 = 0; matrix_7 = 0; matrix_8 = 0;
end


%% whole-point symmetric boundary condition, with DFT solver
if (strcmp(extension_case, 'symmetric') == 1) && (strcmp(type_of_symmetry, 'whole_point') == 1) ...
        && (strcmp(option, 'DCT') == 1)
 
if (M == N)
[DCT_1_matrix,IDCT_1_matrix] = DCT_type1_matrix(N); 
matrix_1 = DCT_1_matrix; matrix_2 = DCT_1_matrix;
matrix_3 = DCT_1_matrix; matrix_4 = IDCT_1_matrix;
matrix_5 = DCT_1_matrix; matrix_6 = IDCT_1_matrix;
matrix_7 = IDCT_1_matrix; matrix_8 = IDCT_1_matrix;
end

if (M ~= N)
[DCT_1_matrix_M,IDCT_1_matrix_M] = DCT_type1_matrix(M); 
[DCT_1_matrix_N,IDCT_1_matrix_N] = DCT_type1_matrix(N); 
matrix_1 = DCT_1_matrix_M; matrix_2 = DCT_1_matrix_N;
matrix_3 = DCT_1_matrix_M; matrix_4 = IDCT_1_matrix_M;
matrix_5 = DCT_1_matrix_N; matrix_6 = IDCT_1_matrix_N;
matrix_7 = IDCT_1_matrix_M; matrix_8 = IDCT_1_matrix_N;
end

end

return;



%% embedded function 3
function [DCT_1_matrix, IDCT_1_matrix] = DCT_type1_matrix(N)

%%%%%%%%%%% forward DCT
DCT_1_matrix = zeros(N,N);

for k = 0:N-1
    for n = 0:N-1
        DCT_1_matrix(k+1,n+1) = cos(pi*k*n/(N-1));
    end
end

DCT_1_matrix = 2*DCT_1_matrix;
DCT_1_matrix(:,1) = 1; 
for k = 1:N
    DCT_1_matrix(k,N) = (-1)^(k-1);
end

%%%%%%%%%%%%%% inverse DCT
IDCT_1_matrix = DCT_1_matrix';
IDCT_1_matrix(1,:) = 2*IDCT_1_matrix(1,:); IDCT_1_matrix(N,:) = 2*IDCT_1_matrix(N,:);
IDCT_1_matrix(:,1) = 0.5*IDCT_1_matrix(:,1); IDCT_1_matrix(:,N) = 0.5*IDCT_1_matrix(:,N);
IDCT_1_matrix = IDCT_1_matrix/(2*(N-1));

return;

%% embedded function 4
function [DCT_2_matrix,IDCT_2_matrix] = DCT_type2_matrix(N)

%%%%%%%%% forward DCT
DCT_2_matrix = zeros(N,N);

for k = 0:N-1
    for n = 0:N-1
        DCT_2_matrix(k+1,n+1) = exp(sqrt(-1)*pi*k/(2*N))*cos(pi*k*(n+0.5)/N);
    end
end

DCT_2_matrix = 2*DCT_2_matrix;

%%%%%%%%%%%%%%%% inverse DCT
IDCT_2_matrix = DCT_2_matrix';
IDCT_2_matrix(:,1) = IDCT_2_matrix(:,1)/(4*N);
for i = 2:N
    IDCT_2_matrix(:,i) = IDCT_2_matrix(:,i)/(2*N);
end

return;


%% embedded function 5
function [DCT_type2_matrix,IDCT_type2_matrix] = DCT_type2_real_matrix(N)

%%%%%%%%% forward DCT
DCT_type2_matrix = zeros(N,N);

for k = 0:N-1
    for n = 0:N-1
        DCT_type2_matrix(k+1,n+1) = cos(pi*k*(n+0.5)/N);
    end
end

DCT_type2_matrix = 2*DCT_type2_matrix;

%%%%%%%%%%%%%%%% inverse DCT
IDCT_type2_matrix = DCT_type2_matrix';
IDCT_type2_matrix(:,1) = IDCT_type2_matrix(:,1)/(4*N);
for i = 2:N
    IDCT_type2_matrix(:,i) = IDCT_type2_matrix(:,i)/(2*N);
end

return;


