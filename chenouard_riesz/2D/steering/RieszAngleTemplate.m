function [th,mx]=RieszAngleTemplate(orig, template, order, debug)
%RIESZANGLETEMPLATE steer Riesz coefficients for a given wavelet band
% in order to maximize the response of a template
%
% --------------------------------------------------------------------------
% Input arguments:
%
% ORIG the original Riesz-wavelet coefficients at a given scale
%
% ORDER the order of the Riesz transform
%
% TEMPLATE coefficients for the linear combination of Riesz channels for which
% the response is to be maximized
%
% DEBUG Display some computation details if 1. Optional. Default is 0;
% orig structure of Riesz-wavelet coefficients
%
% --------------------------------------------------------------------------
% Output arguments:
%
% TH matrix of angles that are estimated pointwise in the wavelet band.
%
% MX Riesz-wavelet coefficient steered with respect to the angles in TH.
%
% --------------------------------------------------------------------------
%
% Part of the Generalized Riesz-wavelet toolbox
%
% Author: Dimitri Van De Ville. Ecole Polytechnique Federale de Lausanne.
%
% Version: Feb. 7, 2012

if nargin < 4,
    debug = 0;
end

% load polynomial coefficients
matfn=sprintf('RieszAngle%d-Flex.dat',order);
mat=reshape(load(matfn),[order+1 order+1 order+1]);
mat=shiftdim(mat,1);

if debug,
    % visualize polynomial
    fprintf('\n');
    for iter1=1:order+1, % degree
        fprintf('tan(t)^%d ( ',order+1-iter1);
        for iter2=1:order+1, % channel
            fprintf('+ ch[%d] ( ',iter2);
            for iter3=1:order+1, % template
                if mat(iter3,iter2,iter1),
                    fprintf('%+3.1f tm[%d] ',mat(iter3,iter2,iter1),iter3);
                end;
            end;
            fprintf(')\n    ');
        end;
        fprintf(')\n');
    end;
end;

% load steering matrix
steermatfn=sprintf('RieszSteer%d.dat',order);
steermat=shiftdim(reshape(load(steermatfn),[order+1 order+1 order+1]),1);

terms=zeros(size(orig));
for iter1=1:order+1, % degree
    for iter2=1:order+1, % channel
        for iter3=1:order+1, % template
            if mat(iter3,iter2,iter1),
                terms(:,:, iter1)=terms(:,:,iter1)+...
                    orig(:,:,iter2).*template(iter3).*mat(iter3,iter2,iter1);
            end;
        end;
    end;
end

% compute th and mx

th = zeros(size(terms, 1), size(terms, 2));
mx = zeros(size(terms, 1), size(terms, 2));
for iterx1=1:size(terms, 1),
    parfor iterx2=1:size(terms, 2),
        C=zeros(1,order+1);%C contains the increasing order terms at position (x1, x2)
        for iter=1:order+1,
            C(iter)=terms(iterx1, iterx2, iter);
        end;
        R=roots(C); %compute the roots of the polynomial C(1)*X^N + ... + C(N)*X + C(N+1)
        R=real(R(find(abs(imag(R))<1e-5))); %convert almost real roots to real numbers
        if isempty(R),
            R=0;
        end;
        tha=atan(R);%compute arctangent of the roots
        costha=cos(tha);
        sintha=sin(tha);
        cossintha=cell(1,order+1);
        for iterterm=1:order+1,%build polynomial of costha and sintha
            cossintha{iterterm}=costha.^(order-iterterm+1).*sintha.^(iterterm-1);
        end;
        V = zeros(size(tha));
        for itertm=1:order+1,           % template (row)
            for iterch=1:order+1,       % channel (column)
                for iterterm=1:order+1, % term
                    V = V + ...
                        template(itertm)*steermat(itertm,iterch,iterterm) * ...
                        cossintha{iterterm} .* ...
                        orig(iterx1,iterx2, iterch);
                end;
            end;
        end;
        idx=find(abs(V(:))==max(abs(V(:))));
        th(iterx1,iterx2)=-tha(idx(1));
        mx(iterx1,iterx2)=max(abs(V(:)));
    end;
end;
