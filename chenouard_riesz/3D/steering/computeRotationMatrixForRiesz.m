function S = computeRotationMatrixForRiesz(R, config)
% COMPUTEROTATIONMATRIXFORRIESZ compute a Riesz rotation matrix for a
% spatial rotataion matrix
%
%   S = computeRotationMatrixForRiesz(R, config)
%   compute the steering matrix for the Riesz coefficients which corresponds
%   to the rotation matrix in the space domain defined by R. config is the
%   RieszConfig object characterizing the Riesz transform.
%   Returns a steering matrix S.
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard@gmail.com
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets in practice",
%   IEEE Transactions on Image Processing,
%   Vol. 21, Num. 11, pp 4522--4533, Nov 2012
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.3, 18.11.2012
%
%  Riesz-3D-light toolbox


numBands = config.RieszChannels;
S = zeros(numBands,numBands);

%find which Riesz band correspond to each direction hilbert
%transform

r1 = R(:,1);
r2 = R(:,2);
r3 = R(:,3);

%for each band
for i = 1:numBands
    tmp = config.RieszOrders{i};
    n1 = tmp(1);
    n2 = tmp(2);
    n3 = tmp(3);
    nFact = factorial(n1)*factorial(n2)*factorial(n3);%n! = n1!n2!n3!
    %---------------------------------------------------------
    %first R column
    %loop over possible orders of r1(1)
    k11Fact = 1;
    for k11 = 0:n1;
        r1PowK11 = r1(1)^k11;
        %k11Fact = factorial(k11);
        if k11
            k11Fact = k11Fact*k11;
        end
        %loop over possible orders of r1(2)
        k21Fact = 1;
        for k21 = 0:(n1-k11);
            %k21Fact = factorial(k21);
            if k21
                k21Fact = k21Fact*k21;
            end
            r1PowK21 = r1(2)^k21;
            %set the order of r1(3)
            k31 = n1-k11-k21;
            r1PowK31 = r1(3)^k31;
            k31Fact = factorial(k31);
            %factorial of k1
            k1Fact = k11Fact*k21Fact*k31Fact;
            %k1Fact = factorial(k11)*factorial(k21)*factorial(k31);
            %---------------------------------------------------------
            %second R column
            %loop over possible orders of r2(1)
            k12Fact = 1;
            for k12 = 0:n2;
                r2PowK12 = r2(1)^k12;
                %k12Fact = factorial(k12);
                if k12,
                    k12Fact = k12Fact*k12;
                end
                %loop over possible orders of r2(2)
                k22Fact = 1;
                for k22 = 0:(n2-k12);
                    r2PowK22 = r2(2)^k22;
                    if k22,
                        k22Fact = k22Fact*k22;
                    end
                    %k22Fact = factorial(k22);
                    %set the order of r2(3)
                    k32 = n2-k12-k22;
                    r2PowK32 = r2(3)^k32;
                    k32Fact = factorial(k32);
                    k2Fact = k12Fact*k22Fact*k32Fact;
                    %k2Fact = factorial(k12)*factorial(k22)*factorial(k32);
                    %------------------------------------------------------
                    %third R column
                    k13Fact = 1;
                    %loop over possible orders of r3(1)
                    for k13 = 0:n3;
                        if k13,
                            k13Fact = k13*k13Fact;
                        end
                        %k13Fact = factorial(k13);
                        r3PowK13 = r3(1)^k13;
                        %loop over possible orders of r3(2)
                        k23Fact = 1;
                        for k23 = 0:(n3-k13);
                            r3PowK23 = r3(2)^k23;
                            %k23Fact = factorial(k23);
                            if k23,
                                k23Fact = k23Fact*k23;
                            end
                            %set the order of r3(3)
                            k33 = n3-k13-k23;
                            r3PowK33 = r3(3)^k33;
                            k33Fact = factorial(k33);
                            k3Fact = k13Fact*k23Fact*k33Fact;
                            %k3Fact = factorial(k13)*factorial(k23)*factorial(k33);
                            % m coefficient
                            m1 = k11 + k12 + k13;
                            m2 = k21 + k22 + k23;
                            m3 = k31 + k32 + k33;
                            
                            % find which m index corresponds to [m1 m2 m3]
                            for j = 1:numBands
                                if sum(config.RieszOrders{j}==[m1 m2 m3])==3
                                    %                                     S(i,j) = S(i,j) + ...
                                    %                                         ((r1(1)^k11) * (r1(2)^k21) * (r1(3)^k31))...
                                    %                                         *((r2(1)^k12) * (r2(2)^k22) * (r2(3)^k32))...
                                    %                                         *((r3(1)^k13) * (r3(2)^k23) * (r3(3)^k33))...
                                    %                                         *nFact/(k1Fact*k2Fact*k3Fact);
                                    S(i,j) = S(i,j) + ...
                                        r1PowK11 * r1PowK21* r1PowK31...
                                        *r2PowK12 * r2PowK22 * r2PowK32...
                                        *r3PowK13 * r3PowK23 * r3PowK33...
                                        *nFact/(k1Fact*k2Fact*k3Fact);
                                end
                            end
                        end
                    end
                    
                    
                end
                
            end
        end
        
    end
end


%normalize
for i = 1:numBands
    tmp = config.RieszOrders{i};
    nFact = factorial(tmp(1))*factorial(tmp(2))*factorial(tmp(3));%n! = n1!n2!n3!
    for j = 1:numBands
        tmp2 = config.RieszOrders{j};
        mFact = factorial(tmp2(1))*factorial(tmp2(2))*factorial(tmp2(3));%n! = n1!n2!n3!
        S(i,j) = sqrt(mFact/nFact)*S(i,j);
    end
end