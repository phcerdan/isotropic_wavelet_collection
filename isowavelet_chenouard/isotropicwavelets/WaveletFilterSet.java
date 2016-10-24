package plugins.nchenouard.isotropicwavelets;

/**
 *
 * Set of wavelet filters for image decomposition and reconstruction
 *
 * 2D wavelet decomposition and reconstruction algorithms are provided.
 * Transforms are as decribed in:
 *
 * Chenouard, N.; Unser, M., "3D Steerable Wavelets in Practice"
 * IEEE Transactions on Image Processing, vol.21, no.11, pp.4522,4533, Nov. 2012
 * doi: 10.1109/TIP.2012.2206044
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

import java.util.ArrayList;

public class WaveletFilterSet {
	ArrayList<double[][]> waveletFiltersFFT; // set of wavelet filters in the
	// Fourier domain. One filter as a
	// double[][] per scale.
	double[][] prefilterFFT; // set of filters in the Fourier domain for high
	// frequency band filtering out
	int numScales; // number of scales for wavelet decomposition
	IsotropicWaveletType
		waveletType; // type of radial profile of the wavelet function
	boolean prefilter; // true if high frequencies (corners of the Fourier
	// domain) need to be filtered out
	int width; // width of the 2D images to process
	int height; // height of the 2D images to process
	int[][] scaleDims; // dimensions of the wavelet bands
	int[] lpSize; // dimensions of the low frequency residual
	int[] hpSize; // dimensions of the high frequency residual
	boolean isotropicPadding; // true if zero-padding is used to obtain an
	// isotropic image

	/**
	 * Initialize the set of filters without using zero-padding
	 *
	 * @param waveletType type of radial profile of the wavelet function
	 * @param numScales number of scales for wavelet decomposition
	 * @param prefilter true if high frequencies (corners of the Fourier domain)
	 * need to be filtered out
	 * @param width width of the 2D images to process
	 * @param height height of the 2D images to process
	 * */
	public WaveletFilterSet(IsotropicWaveletType waveletType, int numScales,
			boolean prefilter, int width, int height) {
		this(waveletType, numScales, prefilter, width, height, false);
	}

	/**
	 * Initialize the set of filters
	 *
	 * @param waveletType type of radial profile of the wavelet function
	 * @param numScales number of scales for wavelet decomposition
	 * @param prefilter true if high frequencies (corners of the Fourier domain)
	 * need to be filtered out
	 * @param width width of the 2D images to process
	 * @param height height of the 2D images to process
	 * @param isotropicPadding true if zero-padding is used to obtain an
	 * isotropic image
	 * */
	public WaveletFilterSet(IsotropicWaveletType waveletType, int numScales,
			boolean prefilter, int width, int height, boolean isotropicPadding) {
		this.waveletType = waveletType;
		this.numScales = numScales;
		this.isotropicPadding = isotropicPadding;
		// check image dimensions
		if (areFeasibleDimensions(width, height, numScales)) {
			this.width = width;
			this.height = height;
		} else {
			this.width = getFeasibleSize(width, numScales);
			this.height = getFeasibleSize(height, numScales);
			if (isotropicPadding) {
				this.width = Math.max(this.width, this.height);
				this.height = this.width;
			}
		}
		this.prefilter = prefilter;
		if (prefilter)
			this.prefilterFFT = WaveletFunction.getPreFiltersFFT(
					waveletType, this.width, this.height);
		hpSize = new int[] {this.width, this.height};
		waveletFiltersFFT = new ArrayList<double[][]>(numScales);
		int scaleWidth = this.width;
		int scaleHeight = this.height;
		scaleDims = new int[numScales][2];
		for (int i = 0; i < numScales; i++) {
			double[][] filters = WaveletFunction.getFilterFFT(
					waveletType, scaleWidth, scaleHeight);
			waveletFiltersFFT.add(filters);
			scaleDims[i][0] = scaleWidth;
			scaleDims[i][1] = scaleHeight;
			scaleWidth = (int) scaleWidth / 2;
			scaleHeight = (int) scaleHeight / 2;
		}
		lpSize = new int[] {scaleWidth, scaleHeight};
	}

	/**
	 * Return the next image size (superior to the original image size) such
	 * that dyadic subsampling is feasible for a number of scales
	 * @param size size of the image along a dimension
	 * @param numScales number of wavelet scales
	 * @return the minimum size the image should be to make dyadic subsampling
	 * feasible
	 * */
	private int getFeasibleSize(int size, int numScales) {
		int feasibleSize = size;
		if (feasibleSize % 2 != 0)
			feasibleSize++;
		if (Math.floor(feasibleSize / Math.pow(2, numScales))
				== feasibleSize / Math.pow(2, numScales))
			return feasibleSize;
		int minSize = (int) Math.pow(2, numScales);
		feasibleSize =
			(int) Math.ceil(feasibleSize / (double) minSize) * minSize;
		return feasibleSize;
	}

	/**
	 * Test whether the width and height of the image comply with the
	 * subsampling requirements of wavelet decomposition
	 * @param width width of the image to decompose
	 * @param height height of the image to decompose
	 * @param numScales number of wavelet scales to use
	 * @return true if the height and width of the image comply with the number
	 * of wavelet scales, false otherwise
	 * */
	private boolean areFeasibleDimensions(
			int width, int height, int numScales) {
		if (width % 2 != 0 || height != 0)
			return false;
		if (Math.floor(width / Math.pow(2, numScales))
				!= (width / Math.pow(2, numScales))
				|| Math.floor(height / Math.pow(2, numScales))
				!= (width / Math.pow(2, numScales)))
			return false;
		return true;
			}

	/**
	 * Get the width of the analyzed image
	 * @return width of the analyzed image
	 * */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the analyzed image
	 * @return height of the analyzed image
	 * */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the number of scales for the wavelet decomposition
	 * @return number of wavelet scales
	 * */
	public int getNumScales() {
		return numScales;
	}

	/**
	 * Determines whether high frequencies are filtered out
	 * @return true if the high frequency component is prefiltered, false else
	 * */
	public boolean isPrefilter() {
		return prefilter;
	}

	/**
	 * Get the width of a given wavelet scale
	 * @param scale scale of the wavelet transform
	 * @return width of the wavelet band
	 * */
	public int getScaleWidth(int scale) {
		return scaleDims[scale][0];
	}

	/**
	 * Get the height of a given wavelet scale
	 * @param scale scale of the wavelet transform
	 * @return height of the wavelet band
	 * */
	public int getScaleHeight(int scale) {
		return scaleDims[scale][1];
	}

	/**
	 * Get the width of the low frequency residual
	 * @return width of the low frequency residual
	 * */
	public int getLPWidth() {
		return lpSize[0];
	}

	/**
	 * Get the height of the low frequency residual
	 * @return height of the low frequency residual
	 * */
	public int getLPHeight() {
		return lpSize[1];
	}

	/**
	 * Get the width of the high frequency residual
	 * @return width of the high frequency residual
	 * */
	public int getHPWidth() {
		return hpSize[0];
	}

	/**
	 * Get the width of the height frequency residual
	 * @return width of the height frequency residual
	 * */
	public int getHPHeight() {
		return hpSize[1];
	}

	/**
	 * get the type of radial profile of the wavelet function
	 * @return type of radial profile
	 * */
	public IsotropicWaveletType getWaveletType() {
		return this.waveletType;
	}

	/**
	 * Determines whether zero-padding is used to obtain an isotropic image
	 * @return true if zero-padding is used to obtain an isotropic image
	 * */
	public boolean isIsotropicPadding() {
		return isotropicPadding;
	}
}
