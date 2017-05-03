function viewRieszRotationMovies(config)
% VIEWRIESZROTATIONMOVIES view an animation of the steering of 3D Riesz
% filters
%
% viewRieszRotationMovies(config) view an animation of the steering of the
% 3D Riesz filters specified by the RieszConfig object config
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

saveImages = 0;
angTheta = 0:0.1:2*pi;

% frequency mask to make the filters radially bandlimited
mask = getDiskMask(config.size(1));

%build the non rotated coordinates matrix
sub = eye(config.RieszChannels);


%build the set of rotation matrices for the rotation around the z axis
S = {};
cnt = 1;
for theta=angTheta,
    R(1,1) = cos(theta);
    R(1,2) = -sin(theta);
    R(1,3) = 0;
    
    R(2,1) = sin(theta);
    R(2,2) = cos(theta);
    R(2,3) = 0;
    
    R(3,1) = 0;
    R(3,2) = 0;
    R(3,3) = 1;
    
    S{cnt} = computeRotationMatrixForRiesz(R, config);
    cnt = cnt+1;
end

%build the set of rotation matrices for the rotation around the x axis
cnt = 1;
for theta=angTheta,
    
    %y coordinate rotation
    R(1,1) = cos(theta);
    R(1,2) = 0;
    R(1,3) = -sin(theta);
    
    %x coordinate does not rotate
    R(2,1) = 0;
    R(2,2) = 1;
    R(2,3) = 0;
    
    %z coordinate rotation
    R(3,1) = sin(theta);
    R(3,2) = 0;
    R(3,3) = cos(theta);
    
    S2{cnt} = computeRotationMatrixForRiesz(R, config);
    cnt = cnt+1;
end


%build the set of rotation matrices for the rotation around the y axis
cnt = 1;
for theta=angTheta,
    
    %y coordinate does not rotate
    R(1,1) = 1;
    R(1,2) = 0;
    R(1,3) = 0;
    
    %x coordinate rotation
    R(2,1) = 0;
    R(2,2) = cos(theta);
    R(2,3) = -sin(theta);
    
    %z coordinate rotation
    R(3,1) = 0;
    R(3,2) = sin(theta);
    R(3,3) = cos(theta);
    
    S3{cnt} = computeRotationMatrixForRiesz(R, config);
    cnt = cnt+1;
end


for i = 1:config.RieszChannels,
    if saveImages,
        dirname = sprintf('band%d', i);
        mkdir(dirname);
    end
    h = figure;
    for cnt = 1:length(S2)
        %rotate the vector coordinate subR
        subR = sub*S2{cnt};
        %combine the filters with respect to the rotated coordinates
        f = zeros(config.size);
        for j=1:config.RieszChannels
            f = f + subR(i,j)*config.RieszFilters{j};
        end
        %display the filter in the space domain
        viewFilterInSpace(f, mask);
        if saveImages,
            if cnt<10
                filename = strcat(dirname, '/im000',num2str(cnt),'.tiff');
            else
                if cnt<100,
                    filename = strcat(dirname, '/im00',num2str(cnt),'.tiff');
                else
                    if cnt<1000
                        filename = strcat(dirname, '/im0',num2str(cnt),'.tiff');
                    end
                end
            end
            saveas(h, filename,'tiff');
        else
            title(sprintf('band %d , angle around y-axis %f', i, angTheta(cnt)))
            pause(0.01)
        end
        clf reset;
    end
    cnt = length(S2);
    for cnt2 = 1:length(S)
        %rotate the vector coordinate subR
        subR = sub*S{cnt2};
        %combine the filters with respect to the rotated coordinates
        f = zeros(config.size);
        for j=1:config.RieszChannels
            f = f + subR(i,j)*config.RieszFilters{j};
        end
        %display the filter in the space domain
        viewFilterInSpace(f, mask);
        if saveImages,
            if (cnt+cnt2)<10
                filename = strcat(dirname, '/im000',num2str((cnt+cnt2)),'.tiff');
            else
                if (cnt+cnt2)<100,
                    filename = strcat(dirname, '/im00',num2str((cnt+cnt2)),'.tiff');
                else
                    if (cnt+cnt2)<1000
                        filename = strcat(dirname, '/im0',num2str((cnt+cnt2)),'.tiff');
                    end
                end
            end
            saveas(h, filename,'tiff');
        else
            title(sprintf('band %d , angle around x-axis %f', i, angTheta(cnt2)))
            pause(0.01)
        end
        clf reset;
    end
    cnt2 = length(S);
    for cnt3 = 1:length(S3)
        %rotate the vector coordinate subR
        subR = sub*S3{cnt3};
        %combine the filters with respect to the rotated coordinates
        f = zeros(config.size);
        for j=1:config.RieszChannels
            f = f + subR(i,j)*config.RieszFilters{j};
        end
        %display the filter in the space domain
        viewFilterInSpace(f, mask);
        if saveImages,
            if (cnt+cnt2+cnt3)<10
                filename = strcat(dirname, '/im000',num2str((cnt+cnt2+cnt3)),'.tiff');
            else
                if (cnt+cnt2+cnt3)<100,
                    filename = strcat(dirname, '/im00',num2str((cnt+cnt2+cnt3)),'.tiff');
                else
                    if (cnt+cnt2+cnt3)<1000
                        filename = strcat(dirname, '/im0',num2str((cnt+cnt2+cnt3)),'.tiff');
                    end
                end
            end
            saveas(h, filename,'tiff');
        else
            title(sprintf('band %d , angle around x-axis %f', i, angTheta(cnt3)))
            pause(0.01)
        end
        clf reset;
    end
end

end