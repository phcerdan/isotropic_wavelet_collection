function config = RieszPrepareTransform(config)
% RIESZPREPARETRANSFORM Prepare the Riesz filters for Riesz pyramid in 3D
%
% config = RieszPrepareTransform(config) Prepare the Riesz filters for the
% Riesz pyramid in 3D corresponding to the RieszConfig object config.
% Returns a new RieszConfig object config containing the Riesz filters and
% the prefilter.

% warning: intensive memory usage here
% each grid direction is the same size as the original image
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


[grid.yc, grid.xc, grid.zc]=ndgrid(2*pi*((1:config.size(1))-1)/config.size(1) - pi,2*pi*((1:config.size(2))-1)/config.size(2) - pi, 2*pi*((1:config.size(3))-1)/config.size(3) - pi);

%% prepare prefilter
prefilterReady = 0;
if (config.prefilterType == PrefilterType.Shannon)
    [config.prefilter.filterLow config.prefilter.filterHigh] = buildShannonPrefilter(config.size(2), config.size(1), config.size(3));
    prefilterReady = 1;
end
if (config.prefilterType == PrefilterType.Simoncelli)
    [config.prefilter.filterLow config.prefilter.filterHigh] = buildRaisedCosinePrefilter(config.size(2), config.size(1), config.size(3));
    prefilterReady = 1;
end
if (config.prefilterType == PrefilterType.None)
    config.prefilter.filterLow = [];
    config.prefilter.filterHigh = [];
    prefilterReady = 1;
end
if ~prefilterReady,
    error('wrong prefilter type. See PrefilterType enumeration.');
end

%% prepare spline wavelet decomposition
if strcmpi(config.waveletType,'spline'),
    error('3D spline wavelets are not included in this toolbox, please consider using isotropic wavelets instead')
    %config = prepareSplineWavelets2(config, grid);
end;

%prepare Riesz transform
if config.RieszOrder>0, % enable Riesz transform
    tmp = 1./sqrt(grid.xc.^2+grid.yc.^2+grid.zc.^2);
    
    %warning: memory usage same size as image
    
    %number of components
    d = 3;
    config.RieszChannels = nchoosek(d+config.RieszOrder-1,config.RieszOrder);
    
    %1d filter: -j freq / norm of pulsation vector, and swap quadrants
    base1=ifftshift(-1i*grid.yc.*tmp);%warning: memory usage same size as image
    % set the 0 frequency to 1
    base1(1,1,1)=1;
    clear grid.yc;
    
    base2=ifftshift(-1i*grid.xc.*tmp);%warning: memory usage same size as image
    % set the 0 frequency to 1
    base2(1,1,1)=1;
    clear grid.xc;
    
    base3=ifftshift(-1i*grid.zc.*tmp);%warning: memory usage same size as image
    % set the 0 frequency to 1
    base3(1,1,1)=1;
    clear grid.zc;
    
    
    % keep only the imaginary part of the central frequency as to
    % alleviate aliasing
    base1(end/2+1,:,:)=imag(base1(end/2+1,:,:));
    base2(:,end/2+1,:)=imag(base2(:,end/2+1,:));
    base3(:,:,end/2+1)=imag(base3(:,:,end/2+1));
    
    config.RieszFilters = cell(1, config.RieszChannels);
    config.RieszOrders = cell(1, config.RieszChannels);
    
    iter = 1;
    for n1=0:config.RieszOrder,
        for n2=0:(config.RieszOrder-n1)
            n3 = config.RieszOrder-n1-n2;
            config.RieszOrders{iter} = [n1 n2 n3];
            switch n1,
                case 0,
                    switch n2,
                        case 0,
                            switch n3,
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base3.^(n3);
                            end
                        case 1,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base2;
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base2.*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base2.*base3.^(n3);
                            end
                        otherwise,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base2.^(n2);
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base2.^(n2).*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base2.^(n2).*base3.^(n3);
                            end
                    end
                case 1,
                    switch n2,
                        case 0,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1;
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base3.^(n3);
                            end
                        case 1,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base2;
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base2.*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base2.*base3.^(n3);
                            end
                        otherwise,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base2.^(n2);
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base2.^(n2).*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.*base2.^(n2).*base3.^(n3);
                            end
                    end
                otherwise,
                    switch n2,
                        case 0,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1);
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base3.^(n3);
                            end
                        case 1,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base2;
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base2.*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base2.*base3.^(n3);
                            end
                        otherwise,
                            switch n3,
                                case 0,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base2.^(n2);
                                case 1,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base2.^(n2).*base3;
                                otherwise,
                                    config.RieszFilters{iter}  = ...
                                        sqrt(multinomial(config.RieszOrder,n1,n2,n3))*base1.^(n1).*base2.^(n2).*base3.^(n3);
                            end
                    end
            end
            iter = iter+1;
        end
    end;
    
    dN = d^(0.5*config.RieszOrder);
    for i=1:config.RieszChannels
        config.RieszFilters{i}(1) = config.RieszFilters{i}(1)/dN;
    end
else
    config.RieszChannels=1;
end;
end

function c = multinomial(n,varargin)
% MULTINOMIAL Multinomial coefficients
%
%   MULTINOMIAL(N, K1, K2, ..., Km) where N and Ki are floating point
%   arrays of non-negative integers satisfying N = K1 + K2 + ... + Km,
%   returns the multinomial  coefficient   N!/( K1!* K2! ... *Km!).
%
%   MULTINOMIAL(N, [K1 K2 ... Km]) when Ki's are all scalar, is the
%   same as MULTINOMIAL(N, K1, K2, ..., Km) and runs faster.
%
%   Non-integer input arguments are pre-rounded by FLOOR function.
%
% EXAMPLES:
%    multinomial(8, 2, 6) returns  28
%    binomial(8, 2) returns  28
%
%    multinomial(8, 2, 3, 3)  returns  560
%    multinomial(8, [2, 3, 3])  returns  560
%
%    multinomial([8 10], 2, [6 8]) returns  [28  45]

% Mukhtar Ullah
% November 1, 2004
% mukhtar.ullah@informatik.uni-rostock.de

nIn = nargin;
error(nargchk(2, nIn, nIn))

if ~isreal(n) || ~isfloat(n) || any(n(:)<0)
    error('Inputs must be floating point arrays of non-negative reals')
end

arg2 = varargin;
dim = 2;

if nIn < 3
    k = arg2{1}(:).';
    if isscalar(k)
        error('In case of two arguments, the 2nd cannot be scalar')
    end
else
    [arg2{:},sizk] = sclrexpnd(arg2{:});
    if sizk == 1
        k = [arg2{:}];
    else
        if ~isscalar(n) && ~isequal(sizk,size(n))
            error('Non-scalar arguments must have the same size')
        end
        dim = numel(sizk) + 1;
        k = cat(dim,arg2{:});
    end
end

if ~isreal(k) || ~isfloat(k) || any(k(:)<0)
    error('Inputs must be floating point arrays of non-negative reals')
end

n = floor(n);
k = floor(k);

if any(sum(k,dim)~=n)
    error('Inputs must satisfy N = K1 + K2 ... + Km ')
end

c = floor(exp(gammaln(n+1) - sum(gammaln(k+1),dim)) + .5);
end

function [varargout] = sclrexpnd(varargin)
% SCLREXPND expands scalars to the size of non-scalars.
%    [X1,X2,...,Xn] = SCLREXPND(X1,X2,...,Xn) expands the scalar
%    arguments, if any, to the (common) size of the non-scalar arguments,
%    if any.
%
%    [X1,X2,...,Xn,SIZ] = SCLREXPND(X1,X2,...,Xn) also returns the
%    resulting common size in SIZ.
%
% Example:
% >> c1 = 1; c2 = rand(2,3); c3 = 5; c4 = rand(2,3);
% >> [c1,c2,c3,c4,sz] = sclrexpnd(c1,c2,c3,c4)
%
% c1 =
%      1     1     1
%      1     1     1
%
% c2 =
%     0.7036    0.1146    0.3654
%     0.4850    0.6649    0.1400
%
% c3 =
%      5     5     5
%      5     5     5
%
% c4 =
%     0.5668    0.6739    0.9616
%     0.8230    0.9994    0.0589
%
% sz =
%      2     3

% Mukhtar Ullah
% November 2, 2004
% mukhtar.ullah@informatik.uni-rostock.de

C = varargin;
issC = cellfun('prodofsize',C) == 1;
if issC
    sz = [1 1];
else
    nsC = C(~issC);
    if ~isscalar(nsC)
        for i = 1:numel(nsC), nsC{i}(:) = 0;  end
        if ~isequal(nsC{:})
            error('non-scalar arguments must have the same size')
        end
    end
    s = find(issC);
    sz = size(nsC{1});
    for i = 1:numel(s)
        C{s(i)} = C{s(i)}(ones(sz));
    end
end
varargout = [C {sz}];
end