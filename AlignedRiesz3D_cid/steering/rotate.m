function Qmax = rotate(Q, config, orientations, scales, forward, sparse)
% ROTATE steer 3D Riesz-wavelet coefficients
%
%  Qmax = rotate(Q, config, orientations, J, forward, sparse)
%  steer the 3D Riesz-wavelet coefficients in Q according to the
%  directions 'orientations'.
%
%  Input:
%  ------
%    * Q : 3D Riesz-wavelet coefficients (cell of 4D matrices)
%    * config : RieszConfig object characterizing the Riesz-wavelet transform
%    * orientations : set of orientations provided by the monogenic signal
%    analysis.
%    * scales : integer array specifying the wavelet scales to process
%    * forward : 1 for forward steering, 0 for backward coefficient steering
%    * sparse : 1 if orientations corresponds to a sparse monogenic analysis
%    (only a subset of coefficients is analyzed), 0 if the orientation
%    information is provided for all the coefficients
%
%  Output:
%  -------
%    * Qmax : Riesz-wavelet coefficients after steering
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

if sparse, % sparse orientation information, steer only coefficients for which orientation is provided
    Qmax = Q;
    %parfor (j = scales, matlabpool('SIZE'))
    for j = scales
        orientation = orientations{j};
        if  ~isempty(orientation) && ~isempty(orientation.coordinates)
            idxTab = find(orientation.coordinates(:,1))';
            % compute rotation matrices
            S = computeMultipleRotationMatrixForRiesz(full(orientation.u1(idxTab, :)), full(orientation.u2(idxTab, :)), full(orientation.u3(idxTab, :)), config);
            % perform steering
            q=reshape(Q{j}, config.RieszChannels, size(Q{j},2)*size(Q{j},3)*size(Q{j},4));
            qi = q(:, idxTab)';
            qr = zeros(size(qi));
            S2 = reshape(S, size(S,1)*size(S,2), size(S,3))';
            if forward,
                for n1 = 1:config.RieszChannels,
                    for n2 = 1:config.RieszChannels,
                        qr(:,n1) = qr(:, n1) + S2(:,(n2-1)*config.RieszChannels + n1).*qi(:,n2);
                    end
                end
            else
                for n1 = 1:config.RieszChannels,
                    for n2 = 1:config.RieszChannels,
                        qr(:,n1) = qr(:, n1) + S2(:,(n1-1)*config.RieszChannels + n2).*qi(:,n2);
                    end
                end
            end
            q(:,idxTab) = qr';
            Qmax{j} = reshape(q, size(Q{j}));
        end
    end
else % full orientation information, steer all the coefficients
     Qmax = Q;
    for j = scales, % PARFOR !!!!!!!!!!!!!
        orientation = orientations{j};
        if  ~isempty(orientation)
            % compute rotation matrices
            S = computeMultipleRotationMatrixForRiesz(orientation.u1, orientation.u2, orientation.u3, config);
            % perform steering
            q=reshape(Q{j}, config.RieszChannels, size(Q{j},2)*size(Q{j},3)*size(Q{j},4))';
            qr = zeros(size(q));
            S2 = reshape(S, size(S,1)*size(S,2), size(S,3))';
            if forward,
                for n1 = 1:config.RieszChannels,
                    for n2 = 1:config.RieszChannels,
                        qr(:,n1) = qr(:, n1) + S2(:,(n2-1)*config.RieszChannels + n1).*q(:,n2);
                    end
                end
            else
                for n1 = 1:config.RieszChannels,
                    for n2 = 1:config.RieszChannels,
                        qr(:,n1) = qr(:, n1) + S2(:,(n1-1)*config.RieszChannels + n2).*q(:,n2);
                    end
                end
            end
            Qmax{j} = reshape(qr', size(Q{j})); %% MEMORY ISSUES DUE TO THE DUPLICATION OF Q
        end
    end
end