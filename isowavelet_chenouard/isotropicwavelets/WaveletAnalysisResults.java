package plugins.nchenouard.isotropicwavelets;

/**
 *
 * Coefficients and parameters corresponding to the wavelet decomposition of an
 * 2D image*
 *
 * 2D wavelet decomposition and reconstruction algorithms are provided.
 * Transforms are as decribed in:
 *
 * Chenouard, N.; Unser, M., "3D Steerable Wavelets in Practice"
 * IEEE Transactions on Image Processing, vol.21, no.11, pp.4522,4533, Nov. 2012
 * doi: 10.1109/TIP.2012.2206044 *
 *
 *  except being implemented in 2D instead of 3D.
 *
 *  Please cite the above reference in scientific communications upon use of
 * these tools.
 *
 *
 * @author Nicolas Chenouard (nicolas.chenouard.dev@gmail.com)
 * @version 1.0
 * @date 2014-05-22
 * @license gpl v3.0
 */

public class WaveletAnalysisResults {
	double[] hpResidual; // high frequency residual that cannot analyzed with an
	// isotropic filter (i.e. 'corners' of the Fourier
	// domain)
	double[] lpResidual; // low frequency residual (i.e., center disk in the
	// Fourier domain)
	double[][] bands; // Wavelet coefficients. One double array per scale.
	WaveletFilterSet filterSet; // set of filters used for image decomposition
	int padX = 0; // left zero-padding of the image before wavelet decomposition
	int padY = 0; // top zero-padding of the image before wavelet decomposition
	int fullWidth; // width of the image after zero-padding
	int fullHeight; // height of the image after zero-padding

	boolean storeCoefficientsInFourierDomain =
		false; // true if coefficients are stored in the Fourier domain

	/**
	 * Empty constructor
	 * */
	protected WaveletAnalysisResults() {}

	/**
	 * Initialize the wavelet analysis result object.
	 * @params bands wavelet coefficients. One array of double per scale.
	 * @params lpResidual low frequency residual
	 * @params hpResidual high frequency residual
	 * @params filterSet set of filters used for image decomposition
	 * @params coefficientsInFourierDomain  true if coefficients are stored in
	 * the Fourier domain
	 *
	 * */
	public WaveletAnalysisResults(double[][] bands, double[] lpResidual,
			double[] hpResidual, WaveletFilterSet filterSet,
			boolean coefficientsInFourierDomain) {
		this.bands = bands;
		this.lpResidual = lpResidual;
		this.hpResidual = hpResidual;
		this.filterSet = filterSet;
		this.storeCoefficientsInFourierDomain = coefficientsInFourierDomain;
	}

	/**
	 * Get the wavelet filters used for image decomposition
	 * @return the set of wavelet filters used for image decomposition
	 * */
	public WaveletFilterSet getWaveletFilters() {
		return filterSet;
	}

	/**
	 * Get the set of wavelet coefficients at a given scale as a 1D array of
	 * double
	 * @param scale
	 * @return wavelet coefficients
	 * */
	public double[] getWaveletBand(int scale) {
		return bands[scale];
	}

	/**
	 * Get the high frequency residual as a 1D array
	 * @return high frequency residual
	 * */
	public double[] getHPResidual() {
		return hpResidual;
	}

	/**
	 * Get the low frequency residual as a 1D array
	 * @return high frequency residual
	 * */
	public double[] getLPResidual() {
		return lpResidual;
	}

	/**
	 * Get the number of scales used for wavelet decomposition
	 * @return number of wavelet scales
	 * */
	public int getNumScales() {
		if (filterSet != null)
			return filterSet.getNumScales();
		else
			return bands.length;
	}

	/**
	 * Get the padding size at the left of the image
	 * @return x direction padding size
	 * */
	public int getPadX() {
		return padX;
	}

	/**
	 * Get the padding size at the top of the image
	 * @return y direction padding size
	 * */
	public int getPadY() {
		return padY;
	}

	/**
	 * Get the width of the analyzed image after padding
	 * @return width after padding
	 * */
	public int getFullWidth() {
		return fullWidth;
	}

	/**
	 * Get the height of the analyzed image after padding
	 * @return height after padding
	 * */
	public int getFullHeight() {
		return fullHeight;
	}
}
