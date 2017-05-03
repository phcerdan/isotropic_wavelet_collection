function [hafun,hdfun,hfun,U,harmonics] = Init(htype, Utype, Uparams)
%
% function [hafun,hdfun,hfun,U,harmonics] = Init(htype, Utype, Uparams)
%
% initialize steerable wavelet construction.
%
%
% INPUTS
% ------
%
% htype         isotropic wavelet type (string), one of 'Simoncelli', 'Meyer',
%               'Papadakis' or 'Shannon'
% 
% Utype         steerable construction type (string). Available constructions
%               are 'Gradient', 'Monogenic', 'Hessian', 'CircularHarmonic',
%               'Prolate', 'ProlateUniSided', 'Slepian', 'RieszEven',
%               'RieszOdd', 'RieszComplete', 'SimoncelliEven', 'SimoncelliOdd',
%               'SimoncelliComplete', 'UserDefined'.
%
% Uparams       parameter structure for the chosen steerable construction. Run
%               SteerableDesign() with Utype as the first argument and the
%               string 'help' as the second argument to get a list of
%               parameters for the given Utype.
%
%
% OUTPUTS
% -------
%
% hafun         approximation filter (function handle)
%
% hdfun         detail filter  (function handle)
%
% hfun          isotropic mother wavelet Fourier transform (function handle)
%
% U             construction matrix
%
% harmonics     vector of harmonics for the chosen design and parameters
%
%
% REFERENCE
% ---------
% You are free to use this software for research purposes, but you should 
% not redistribute it without our consent.
%
% In addition, we expect you to include the following citations:
%
% Z. Puspoki and M. Unser, "Template-Free Wavelet-Based Detection of Local
% Symmetries", IEEE Transactions on Image Processing, vol. 24, no. 10, 
% pp. 3009-3018, October 2015
% M. Unser and N. Chenouard, "A Unifying Parametric Framework for 2D Steerable
% Wavelet Transforms", SIAM Journal on Imaging Sciences, vol. 6, no. 1, 
% pp. 102-135, 2013. 
%
%
% AUTHOR
% ------
%
% Z. Puspoki (zsuzsanna.puspoki@epfl.ch)
%
% Biomedical Imaging Group
% Ecole Polytechnique Federale de Lausanne (EPFL)
%


switch htype
    case 'Simoncelli'
        hfun  = @(r) h_Simoncelli(r);
        hafun = @(r) h_Simoncelli_a(r);
        hdfun = @(r) h_Simoncelli_d(r);
        
    case 'Meyer'
        hfun  = @(r) h_Meyer(r);
        hafun = @(r) h_Meyer_a(r);
        hdfun = @(r) h_Meyer_d(r);       
 
    case 'Papadakis'
        hfun  = @(r) h_Papadakis(r);
        hafun = @(r) h_Papadakis_a(r);
        hdfun = @(r) h_Papadakis_d(r);    
                
    case 'Shannon'
        hfun  = @(r) h_Shannon(r);
        hafun = @(r) h_Shannon_a(r);
        hdfun = @(r) h_Shannon_d(r);

    otherwise
        error('unsupported htype.');
        
end

[U,harmonics] = SteerableDesign(Utype,Uparams); 

end

