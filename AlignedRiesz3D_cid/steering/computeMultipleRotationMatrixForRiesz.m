function S = computeMultipleRotationMatrixForRiesz(r1, r2, r3, config)
% COMPUTEMULTIPLEROTATIONMATRIXFORRIESZ compute Riesz rotation matrices for
% a set of orientations
%
%  S = computeMultipleRotationMatrixForRiesz(r1, r2, r3, config)
%  compute Riesz rotation matrices for a set of orientations
%
%  Input:
%  ------
%    * r1 : number of Coefficients x 3 matrix for the first column of the
%    orientation matrix
%    * r2 : number of Coefficients x 3 matrix for the second column of the
%    orientation matrix
%    * r3 : number of Coefficients x 3 matrix for the third column of the
%    orientation matrix
%    * config : RieszConfig object characterizing the Riesz-wavelet
%    transform
%
%  Output:
%  -------
%    * S : set of rotation matrices as a 3D matrix of size 3 x 3 x number
%    of coefficients
%
% -------------------------------------------------------------------------
%
%  AUTHOR:
%    Nicolas Chenouard, nicolas.chenouard?epfl.ch
%    Ecole Polytechnique Federale de Lausanne
%
% -------------------------------------------------------------------------
%
%  REFERENCE:
%    N. Chenouard, M. Unser, "3D Steerable Wavelets and Monogenic Analysis
%    for Bioimaging", Proceedings of the Eighth IEEE International
%    Symposium on Biomedical Imaging: From Nano to Macro (ISBI'11), Chicago
%    IL, USA, March 30-April 2, 2011, pp. 2132-2135.
%
% -------------------------------------------------------------------------
%
%  VERSION:
%  v0.1, 20.07.2011
%
%  Riesz-3D-light toolbox

numCoeffs = size(r1, 1);

numBands = config.RieszChannels;


% ydc 10.03.2017 - Optimization
r1 = single(r1);
r2 = single(r2);
r3 = single(r3);

S = zeros(numCoeffs, numBands, numBands, 'single');
RieszOrdersMat = reshape(cell2mat(config.RieszOrders), numel(config.RieszOrders{1}), numel(config.RieszOrders))';
% S = zeros(numBands,numBands, numCoeffs);
% end ydc 10.03.2017 - Optimization


%for each band
for i = 1:numBands,
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
        r1PowK11 = r1(:,1).^k11;
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
            r1PowK21 = r1(:,2).^k21;
            %set the order of r1(3)
            k31 = n1-k11-k21;
            r1PowK31 = r1(:,3).^k31;
            k31Fact = factorial(k31);
            %factorial of k1
            k1Fact = k11Fact*k21Fact*k31Fact;
            %---------------------------------------------------------
            %second R column
            %loop over possible orders of r2(1)
            k12Fact = 1;
            for k12 = 0:n2;
                r2PowK12 = r2(:,1).^k12;
                %k12Fact = factorial(k12);
                if k12,
                    k12Fact = k12Fact*k12;
                end
                %loop over possible orders of r2(2)
                k22Fact = 1;
                for k22 = 0:(n2-k12);
                    r2PowK22 = r2(:,2).^k22;
                    if k22,
                        k22Fact = k22Fact*k22;
                    end
                    %k22Fact = factorial(k22);
                    %set the order of r2(3)
                    k32 = n2-k12-k22;
                    r2PowK32 = r2(:,3).^k32;
                    k32Fact = factorial(k32);
                    k2Fact = k12Fact*k22Fact*k32Fact;
                    %------------------------------------------------------
                    %third R column
                    k13Fact = 1;
                    %loop over possible orders of r3(1)
                    for k13 = 0:n3;
                        if k13,
                            k13Fact = k13*k13Fact;
                        end
                        %k13Fact = factorial(k13);
                        r3PowK13 = r3(:,1).^k13;
                        %loop over possible orders of r3(2)
                        k23Fact = 1;
                        for k23 = 0:(n3-k13);
                            r3PowK23 = r3(:,2).^k23;
                            %k23Fact = factorial(k23);
                            if k23,
                                k23Fact = k23Fact*k23;
                            end
                            %set the order of r3(3)
                            k33 = n3-k13-k23;
                            r3PowK33 = r3(:,3).^k33;
                            k33Fact = factorial(k33);
                            k3Fact = k13Fact*k23Fact*k33Fact;
                            % m coefficient
                            m1 = k11 + k12 + k13;
                            m2 = k21 + k22 + k23;
                            m3 = k31 + k32 + k33;
                            
                            % find which m index corresponds to [m1 m2 m3]
                            % ydc 10.03.2017 - Optimization
                            j = ismember(RieszOrdersMat,[m1 m2 m3],'rows');
                            s = (r1PowK11 .*r1PowK21 .*r1PowK31...
                                 .*r2PowK12 .*r2PowK22 .*r2PowK32...
                                 .*r3PowK13 .*r3PowK23 .*r3PowK33...
                                 *nFact/(k1Fact*k2Fact*k3Fact));
                             
                            S(:,i,j) = S(:,i,j) + s;
                             
                            % for j = 1:numBands
                            %     if sum(config.RieszOrders{j}==[m1 m2 m3])==3
                            %         s = (r1PowK11 .*r1PowK21 .*r1PowK31...
                            %             .*r2PowK12 .*r2PowK22 .*r2PowK32...
                            %             .*r3PowK13 .*r3PowK23 .*r3PowK33...
                            %             *nFact/(k1Fact*k2Fact*k3Fact));
                            %         S(i,j, :) = squeeze(S(i,j,:)) + s;
                            %     end
                            % end
                            % end ydc 10.03.2017 - Optimization
                        end
                    end    
                end
                
            end
        end
        
    end
end

% ydc 10.03.2017 - Optimization
S = shiftdim(S,1);
% end ydc 10.03.2017 - Optimization

%normalize
% ydc 10.03.2017 - Optimization
tmpmat = reshape(cell2mat(config.RieszOrders), numel(config.RieszOrders{1}), numel(config.RieszOrders))';

nFact = factorial(tmpmat(:,1)).*factorial(tmpmat(:,2)).*factorial(tmpmat(:,3));%n! = n1!n2!n3!
nFact = repmat(nFact, 1, numel(nFact))';
mFact = nFact';
    
S = repmat(sqrt(nFact./mFact),1,1,size(S,3)).*S;

% for i = 1:numBands
%     tmp = config.RieszOrders{i};
%     nFact = factorial(tmp(1))*factorial(tmp(2))*factorial(tmp(3));%n! = n1!n2!n3!
%     for j = 1:numBands
%         tmp2 = config.RieszOrders{j};
%         mFact = factorial(tmp2(1))*factorial(tmp2(2))*factorial(tmp2(3));%n! = n1!n2!n3!
%         S(i,j, :) = sqrt(mFact/nFact)*S(i,j,:);
%     end
% end
% end ydc 10.03.2017 - Optimization