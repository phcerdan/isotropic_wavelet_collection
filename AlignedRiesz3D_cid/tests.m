clear;clc;close all;

dim=256;
[X,Y,Z] = meshgrid(1:dim,1:dim,1:dim);
D = zeros([dim dim dim]);
D(:,:,:) = sin(Z/(dim/32)+2*sin(Y/(dim/16)+2*sin(X/(dim/16))-sin(X/(dim/32))));%+rand(dim,dim,dim);


slice(D,[dim-(dim/4) 0 0],[0 dim-(dim/4) 0],[0 0 dim-(3*dim/4)])
view(3); axis vis3d tight; material shiny; shading interp;
colormap(gray(100))
camlight right; lighting phong;
set(findobj(gca,'Type','Surface'),'EdgeColor','none');
% camlight; lighting phong;

Riesz3Ddemo;