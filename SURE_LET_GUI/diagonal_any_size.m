function [LH, HL, HH, LL] = diagonal_any_size  (x,type_of_symmetry,it)

[M,N] = size(x); M = M-1; N = N-1;

%% half-point symmetry
if  (strcmp(type_of_symmetry, 'half_point') == 1) 
shift = 2^(it-1); 

h0_LH = 4*(x(1,1)+x(1,shift+1)-x(shift+1,1)-x(shift+1,shift+1)); 
h0_HL = 4*(x(1,1)-x(1,shift+1)+x(shift+1,1)-x(shift+1,shift+1));
h0_HH = 4*(x(1,1)-x(1,shift+1)-x(shift+1,1)+x(shift+1,shift+1)); 
h0_LL = 4*(x(1,1)+x(1,shift+1)+x(shift+1,1)+x(shift+1,shift+1));

    if (mod(M,2)==0) && (mod(N,2)==0)

% size --- M/2 x N/2
if it == 1
    m_1 = 1:2:M-1; n_1 = 1:2:N-1; m_2 = m_1+2; n_2 = n_1+2;
else if it > 1
        m_1 = [shift:-2:2   2:2:M-shift]; m_2 = [shift+2:2:M   M:-2:M-shift+2];
        n_1  = [shift:-2:2   2:2:N-shift]; n_2  = [shift+2:2:N   N:-2:N-shift+2];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[2:2:N]); term_6 = x(m_1,[2:2:N]); second = 2*(term_5+term_6); 
term_7 = x([2:2:M],n_1); term_8 = x([2:2:M],n_2); third = 2*(term_7+term_8);
term_9 = x([2:2:M],[2:2:N]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;
LH = half_point_diagonal_half_size (LH,h0_LH); HL = half_point_diagonal_half_size (HL,h0_HL);
HH = half_point_diagonal_half_size (HH,h0_HH); LL = half_point_diagonal_half_size (LL,h0_LL);

LH = padarray(LH,[M/2 N/2],'symmetric','post'); HL = padarray(HL,[M/2 N/2],'symmetric','post');
HH = padarray(HH,[M/2 N/2],'symmetric','post'); LL = padarray(LL,[M/2 N/2],'symmetric','post');
    end
    
    if (mod(M,2)~=0) && (mod(N,2)==0)
        
% size (M+1)/2 x N/2
if it == 1
    m_1 = 1:2:M; n_1 = 1:2:N-1; n_2 = n_1+2; m_2 = [3:2:M,M+1];
else if it > 1
        m_1 = [shift:-2:2   2:2:M-shift+1]; m_2 = [shift+2:2:M+1   M+1:-2:M-shift+3];
        n_1  = [shift:-2:2   2:2:N-shift]; n_2  = [shift+2:2:N   N:-2:N-shift+2];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[2:2:N]); term_6 = x(m_1,[2:2:N]); second = 2*(term_5+term_6); 
term_7 = x([2:2:M+1],n_1); term_8 = x([2:2:M+1],n_2); third = 2*(term_7+term_8);
term_9 = x([2:2:M+1],[2:2:N]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;
LH = half_point_diagonal_half_size (LH,h0_LH); HL = half_point_diagonal_half_size (HL,h0_HL);
HH = half_point_diagonal_half_size (HH,h0_HH); LL = half_point_diagonal_half_size (LL,h0_LL);

LH = padarray(LH,[(M+1)/2 N/2],'symmetric','post'); HL = padarray(HL,[(M+1)/2 N/2],'symmetric','post');
HH = padarray(HH,[(M+1)/2 N/2],'symmetric','post'); LL = padarray(LL,[(M+1)/2 N/2],'symmetric','post');
LH((M+1)/2,:)=[]; HL((M+1)/2,:)=[]; HH((M+1)/2,:)=[]; LL((M+1)/2,:)=[]; 
    end
       
    
    
    if (mod(M,2)==0) && (mod(N,2)~=0)
        
% size (M+1)/2 x N/2
if it == 1
    m_1 = 1:2:M-1; n_1 = 1:2:N; m_2 = m_1+2; n_2 = [3:2:N,N+1];
else if it > 1
        m_1 = [shift:-2:2   2:2:M-shift]; m_2 = [shift+2:2:M   M:-2:M-shift+2];
        n_1  = [shift:-2:2   2:2:N-shift+1]; n_2  = [shift+2:2:N+1   N+1:-2:N-shift+3];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[2:2:N+1]); term_6 = x(m_1,[2:2:N+1]); second = 2*(term_5+term_6); 
term_7 = x([2:2:M+1],n_1); term_8 = x([2:2:M+1],n_2); third = 2*(term_7+term_8);
term_9 = x([2:2:M],[2:2:N+1]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;
LH = half_point_diagonal_half_size (LH,h0_LH); HL = half_point_diagonal_half_size (HL,h0_HL);
HH = half_point_diagonal_half_size (HH,h0_HH); LL = half_point_diagonal_half_size (LL,h0_LL);

LH = padarray(LH,[M/2 (N+1)/2],'symmetric','post'); HL = padarray(HL,[M/2 (N+1)/2],'symmetric','post');
HH = padarray(HH,[M/2 (N+1)/2],'symmetric','post'); LL = padarray(LL,[M/2 (N+1)/2],'symmetric','post');
LH(:,(N+1)/2)=[]; HL(:,(N+1)/2)=[]; HH(:,(N+1)/2)=[]; LL(:,(N+1)/2)=[]; 
    end
    
    
    if (mod(M,2)~=0) && (mod(N,2)~=0)
        
% size (M+1)/2 x (N+1)/2
if it == 1
    m_1 = 1:2:M; n_1 = 1:2:N; m_2 = [3:2:M,M+1]; n_2 = [3:2:N,N+1];
else if it > 1
        m_1 = [shift:-2:2   2:2:M-shift+1]; m_2 = [shift+2:2:M+1   M+1:-2:M-shift+3];
        n_1  = [shift:-2:2   2:2:N-shift+1]; n_2  = [shift+2:2:N+1   N+1:-2:N-shift+3];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[2:2:N+1]); term_6 = x(m_1,[2:2:N+1]); second = 2*(term_5+term_6); 
term_7 = x([2:2:M+1],n_1); term_8 = x([2:2:M+1],n_2); third = 2*(term_7+term_8);
term_9 = x([2:2:M+1],[2:2:N+1]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;
LH = half_point_diagonal_half_size (LH,h0_LH); HL = half_point_diagonal_half_size (HL,h0_HL);
HH = half_point_diagonal_half_size (HH,h0_HH); LL = half_point_diagonal_half_size (LL,h0_LL);

LH = padarray(LH,[(M+1)/2 (N+1)/2],'symmetric','post'); HL = padarray(HL,[(M+1)/2 (N+1)/2],'symmetric','post');
HH = padarray(HH,[(M+1)/2 (N+1)/2],'symmetric','post'); LL = padarray(LL,[(M+1)/2 (N+1)/2],'symmetric','post');
LH(:,(N+1)/2)=[]; HL(:,(N+1)/2)=[]; HH(:,(N+1)/2)=[]; LL(:,(N+1)/2)=[]; 
LH((M+1)/2,:)=[]; HL((M+1)/2,:)=[]; HH((M+1)/2,:)=[]; LL((M+1)/2,:)=[]; 
    end
    
end


%% whole-point symmetry
if  (strcmp(type_of_symmetry, 'whole_point') == 1) 

shift = 2^(it-1); 

    if (mod(M,2)==0) && (mod(N,2)==0)

% size (M/2+1) x (N/2+1)
if it == 1
    m_1 = [2:2:M, M]; m_2 = [2, 2:2:M]; n_1 = [2:2:N, N]; n_2 = [2, 2:2:N]; 
else if it > 1
        m_1 = [shift+1:-2:3   1:2:M-shift+1]; m_2 = [shift+1:2:M+1   M-1:-2:M-shift+1];
        n_1  = [shift+1:-2:3   1:2:N-shift+1]; n_2  = [shift+1:2:N+1    N-1:-2:N-shift+1];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[1:2:N+1]); term_6 = x(m_1,[1:2:N+1]); second = 2*(term_5+term_6); 
term_7 = x([1:2:M+1],n_1); term_8 = x([1:2:M+1],n_2); third = 2*(term_7+term_8);
term_9 = x([1:2:M+1],[1:2:N+1]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;

LH = padarray(LH,[M/2 N/2],'symmetric','post'); LH(M/2+1,:) = []; LH(:,N/2+1) = [];
HL = padarray(HL,[M/2 N/2],'symmetric','post'); HL(M/2+1,:) = []; HL(:,N/2+1) = [];
HH = padarray(HH,[M/2 N/2],'symmetric','post'); HH(M/2+1,:) = []; HH(:,N/2+1) = [];
LL = padarray(LL,[M/2 N/2],'symmetric','post'); LL(M/2+1,:) = []; LL(:,N/2+1) = [];
LH = whole_point_diagonal_same_size (LH); HL = whole_point_diagonal_same_size (HL);
HH = whole_point_diagonal_same_size (HH); LL = whole_point_diagonal_same_size (LL);
    end

   
    if (mod(M,2)~=0) && (mod(N,2)==0)
        
% size (M+1)/2 x (N/2+1)
if it == 1
    m_1 = [2, 2:2:M-1]; n_1 = [2, 2:2:N]; n_2 = [2:2:N,N]; m_2 = [2:2:M+1];
else if it > 1
        m_1 = [shift+1:-2:3   1:2:M-shift]; m_2 = [shift+1:2:M   M-2:-2:M-shift];
        n_1  = [shift+1:-2:3   1:2:N-shift+1]; n_2  = [shift+1:2:N+1   N-1:-2:N-shift+1];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[1:2:N+1]); term_6 = x(m_1,[1:2:N+1]); second = 2*(term_5+term_6); 
term_7 = x([1:2:M],n_1); term_8 = x([1:2:M],n_2); third = 2*(term_7+term_8);
term_9 = x([1:2:M],[1:2:N+1]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;
LH = padarray(LH,[(M+1)/2 N/2],'symmetric','post'); LH((M+1)/2,:) = []; LH(:,N/2+1) = [];
HL = padarray(HL,[(M+1)/2 N/2],'symmetric','post'); HL((M+1)/2,:) = []; HL(:,N/2+1) = [];
HH = padarray(HH,[(M+1)/2 N/2],'symmetric','post'); HH((M+1)/2,:) = []; HH(:,N/2+1) = [];
LL = padarray(LL,[(M+1)/2 N/2],'symmetric','post'); LL((M+1)/2,:) = []; LL(:,N/2+1) = [];
LH = whole_point_diagonal_same_size (LH); HL = whole_point_diagonal_same_size (HL);
HH = whole_point_diagonal_same_size (HH); LL = whole_point_diagonal_same_size (LL);
    end


   if (mod(M,2)==0) && (mod(N,2)~=0)
        
% size (M/2+1) x (N+1)/2
if it == 1
    m_1 = [2 2:2:M]; n_1 = [2 2:2:N-1]; m_2 = [2:2:M, M]; n_2 = [2:2:N+1];
else if it > 1
        m_1 = [shift+1:-2:3   1:2:M-shift+1]; m_2 = [shift+1:2:M+1   M-1:-2:M-shift+1];
        n_1  = [shift+1:-2:3   1:2:N-shift]; n_2  = [shift+1:2:N   N-2:-2:N-shift];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[1:2:N]); term_6 = x(m_1,[1:2:N]); second = 2*(term_5+term_6); 
term_7 = x([1:2:M+1],n_1); term_8 = x([1:2:M+1],n_2); third = 2*(term_7+term_8);
term_9 = x([1:2:M+1],[1:2:N]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;

LH = padarray(LH,[M/2 (N+1)/2],'symmetric','post'); LH(M/2+1,:) = []; LH(:,(N+1)/2) = [];
HL = padarray(HL,[M/2 (N+1)/2],'symmetric','post'); HL(M/2+1,:) = []; HL(:,(N+1)/2) = [];
HH = padarray(HH,[M/2 (N+1)/2],'symmetric','post'); HH(M/2+1,:) = []; HH(:,(N+1)/2) = [];
LL = padarray(LL,[M/2 (N+1)/2],'symmetric','post'); LL(M/2+1,:) = []; LL(:,(N+1)/2) = [];
LH = whole_point_diagonal_same_size (LH); HL = whole_point_diagonal_same_size (HL);
HH = whole_point_diagonal_same_size (HH); LL = whole_point_diagonal_same_size (LL);
    end
    
   if (mod(M,2)~=0) && (mod(N,2)~=0)
        
% size (M+1)/2 x (N+1)/2
if it == 1
    m_1 = [2, 2:2:M-1]; n_1 = [2, 2:2:N-1]; m_2 = [2:2:M+1]; n_2 = [2:2:N+1];
else if it > 1
        m_1 = [shift+1:-2:3   1:2:M-shift]; m_2 = [shift+1:2:M   M-2:-2:M-shift];
        n_1  = [shift+1:-2:3   1:2:N-shift]; n_2  = [shift+1:2:N   N-2:-2:N-shift];
    end
end

term_1 = x(m_1,n_1); term_2 = x(m_1,n_2); term_3 = x(m_2,n_1); term_4 = x(m_2,n_2);
first = term_1+term_2+term_3+term_4;    
term_5 = x(m_2,[1:2:N]); term_6 = x(m_1,[1:2:N]); second = 2*(term_5+term_6); 
term_7 = x([1:2:M],n_1); term_8 = x([1:2:M],n_2); third = 2*(term_7+term_8);
term_9 = x([1:2:M],[1:2:N]); fourth = 4*term_9;

LH = -first-second+third+fourth; HL = -first+second-third+fourth;
HH =  first-second-third+fourth; LL =  first+second+third+fourth;

LH = padarray(LH,[(M+1)/2 (N+1)/2],'symmetric','post'); LH((M+1)/2,:) = []; LH(:,(N+1)/2) = [];
HL = padarray(HL,[(M+1)/2 (N+1)/2],'symmetric','post'); HL((M+1)/2,:) = []; HL(:,(N+1)/2) = [];
HH = padarray(HH,[(M+1)/2 (N+1)/2],'symmetric','post'); HH((M+1)/2,:) = []; HH(:,(N+1)/2) = [];
LL = padarray(LL,[(M+1)/2 (N+1)/2],'symmetric','post'); LL((M+1)/2,:) = []; LL(:,(N+1)/2) = [];
LH = whole_point_diagonal_same_size (LH); HL = whole_point_diagonal_same_size (HL);
HH = whole_point_diagonal_same_size (HH); LL = whole_point_diagonal_same_size (LL);
    end
       
       
end

return;


%% embedded functions
function diagonal = half_point_diagonal_half_size (candidate,h0)
% candidate refers to LH HH HL LL, with exactly half size N/2
[M,N] = size(candidate);
second_term = ones(M,1)*candidate(1,:);
third_term = candidate(:,1)*ones(1,N);
diagonal = h0+second_term+third_term+candidate;
return;


function diagonal = whole_point_diagonal_same_size (candidate)
% candidate refers to LH HH HL LL, with exactly the same size N (after padarray)
[M,N] = size(candidate); 
first_term = candidate; first_term(:,[1,end]) = 0;
second_term = ones(M,1)*candidate(1,:)+candidate(:,1)*ones(1,N);
second_term([1,end],:) = 0;
third_term = ones(M,N)*candidate(1,1); third_term(2:end-1,[1,end]) = 0;
diagonal = first_term+second_term+third_term;
return;
    
    