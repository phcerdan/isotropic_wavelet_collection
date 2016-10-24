package plugins.nchenouard.isotropicwavelets;

import icy.image.IcyBufferedImage;
import icy.main.Icy;
import icy.sequence.Sequence;
import icy.type.collection.array.ArrayUtil;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;

/**
 *
 * Isotropic wavelet transform for 2D images
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

public class IsotropicWaveletTransform {
	/**
	 * Compute x and y direction padding such that the original image is
	 * centered in the padded image
	 *
	 * @param imageWidth the width of the original image
	 * @param imageHeight the height of the original image
	 * @param width the width of the image after padding
	 * @param height the height of the image after padding
	 *
	 * */
	public static int[] computePadding(
			int imageWidth, int imageHeight, int width, int height) {
		int padY = (int) (height - imageHeight) / 2;
		int padX = (int) (width - imageWidth) / 2;
		return new int[] {padX, padY};
			}

	/**
	 * Pad an image with zero values
	 *
	 * @param image the 2D image to pad as a 1D array
	 * @param imageWidth the width of the original image
	 * @param imageHeight the height of the original image
	 * @param width the width of the image after padding
	 * @param height the height of the image after padding
	 * @param padX the size of the pad in x direction, on the left of the image
	 * @param padY the size of the pad in x direction, on the top of the image
	 * */
	public static double[] padImage(double[] image, int imageWidth,
			int imageHeight, int width, int height, int padX, int padY) {
		if (imageWidth == width && imageHeight == height)
			return image;
		double[] paddedImage = new double[width * height];
		for (int y = padY; y < padY + imageHeight; y++) {
			System.arraycopy(image, (y - padY) * imageWidth, paddedImage,
					y * width + padX, imageWidth);
		}
		return paddedImage;
	}

	/**
	 * Crop a padded image to its original dimensions
	 *
	 * @param image the padded 2D image as a 1D array
	 * @param croppedImageWidth the width of the image after cropping
	 * @param croppedImageHeight the height of the image after cropping
	 * @param width the width of the padded image
	 * @param height the height of the padded image
	 * @param padX the size of the pad in x direction, on the left of the image
	 * @param padY the size of the pad in x direction, on the top of the image
	 * */
	public static double[] unpadImage(double[] image, int croppedImageWidth,
			int croppedImageHeight, int width, int height, int padX, int padY) {
		if (croppedImageWidth == width && croppedImageHeight == height)
			return image;
		double[] croppedImage =
			new double[croppedImageWidth * croppedImageHeight];
		for (int y = padY; y < padY + croppedImageHeight; y++) {
			System.arraycopy(image, y * width + padX, croppedImage,
					(y - padY) * croppedImageWidth, croppedImageWidth);
		}
		return croppedImage;
	}

	/**
	 * Downsample the Fourier representation of a 2D image by a factor 2
	 * @param dataFFT Fourier coefficients of the image
	 * @param scaleWidth width of the image
	 * @param scaleHeight height of the image
	 * */
	static public double[] downSampleFourierDomain(
			double[] dataFFT, int scaleWidth, int scaleHeight) {
		//		%downSampling in spatial domain = fold the frequency
		// domain
		//			            c2 = size(Im,2)/2;
		//			            c1 = size(Im,1)/2;
		//			            Im = 0.25*(Im(1:c1, 1:c2) +
		// Im((1:c1)+c1, 1:c2) + Im((1:c1) + c1, (1:c2) +c2) + Im(1:c1, (1:c2)
		//+c2));

		int halfWidth = (int) (scaleWidth / 2);
		int halfHeight = (int) (scaleHeight / 2);
		double[] downSampledData = new double[2 * halfWidth * halfHeight];
		for (int y = 0; y < halfHeight; y++)
			for (int x = 0; x < halfWidth; x++) {
				downSampledData[2 * (y * halfWidth + x)] =
					0.25f * (dataFFT[2 * (y * scaleWidth + x)]
							+ dataFFT[2 * (y * scaleWidth + x + halfWidth)]
							+ dataFFT[2 * ((y + halfHeight) * scaleWidth + x)]
							+ dataFFT[2 * ((y + halfHeight) * scaleWidth + x + halfWidth)]);
				downSampledData[2 * (y * halfWidth + x) + 1] =
					0.25f * (dataFFT[2 * (y * scaleWidth + x) + 1]
							+ dataFFT[2 * (y * scaleWidth + x + halfWidth) + 1]
							+ dataFFT[2 * ((y + halfHeight) * scaleWidth + x) + 1]
							+ dataFFT[2 * ((y + halfHeight) * scaleWidth + x + halfWidth) + 1]);
			}
		return downSampledData;
			}

	/**
	 * Convolve two real images using their Fourier representation and write
	 * results at the first image location.
	 * @param dataFFT Fourier coefficients of the first image. Its values are
	 * overrided by the convolution results.
	 * @param prefilterRealFFT Fourirer of the second image
	 * */
	public static void convolveRealFFTFilterInPlace(
			double[] dataFFT, double[] prefilterRealFFT) {
		for (int i = 0; i < prefilterRealFFT.length; i++) {
			dataFFT[2 * i] = dataFFT[2 * i] * prefilterRealFFT[i];
			dataFFT[2 * i + 1] = dataFFT[2 * i + 1] * prefilterRealFFT[i];
		}
			}

	/**
	 * Convolve two real images using their Fourier representation.
	 * @param dataFFT Fourier coefficients of the first image
	 * @param prefilterRealFFT Fourirer of the second image
	 * @return Fourier coefficients of the convolution results
	 * */
	public static double[] convolveRealFFTFilter(
			double[] dataFFT, double[] prefilterRealFFT) {
		double[] dataCopy = new double[dataFFT.length];
		System.arraycopy(dataFFT, 0, dataCopy, 0, dataCopy.length);
		for (int i = 0; i < prefilterRealFFT.length; i++) {
			dataCopy[2 * i] = dataFFT[2 * i] * prefilterRealFFT[i];
			dataCopy[2 * i + 1] = dataFFT[2 * i + 1] * prefilterRealFFT[i];
		}
		return dataCopy;
			}

	/**
	 * Reconstruct a 2D image from a set of wavelet coefficients
	 * @param waveletResults wavelet coefficients
	 * @return array corresponding to the reconstructed image
	 * */
	public static double[] isotropicBandlimitedSynthesis(
			WaveletAnalysisResults waveletResults) {
		WaveletFilterSet filterSet = waveletResults.getWaveletFilters();
		int scaleWidth = filterSet.getLPWidth();
		int scaleHeight = filterSet.getLPHeight();

		double[] lpFFT;
		if (waveletResults.storeCoefficientsInFourierDomain) {
			lpFFT = waveletResults.lpResidual;
		} else {
			int numCoefficients =
				filterSet.getLPHeight() * filterSet.getLPWidth();
			lpFFT = new double[2 * numCoefficients];
			for (int i = 0; i < numCoefficients; i++)
				lpFFT[2 * i] = waveletResults.lpResidual[i];
			DoubleFFT_2D fftScale = new DoubleFFT_2D(scaleHeight, scaleWidth);
			fftScale.complexForward(lpFFT);
		}
		for (int iter = filterSet.getNumScales() - 1; iter >= 0; iter--) {
			// upsampling of the lowpass image in Fourier domain
			lpFFT = upsampleFFT(lpFFT, scaleWidth, scaleHeight);
			// compute FFT for the wavelet band
			scaleWidth = filterSet.getScaleWidth(iter);
			scaleHeight = filterSet.getScaleHeight(iter);
			double[] hpFFT;
			if (waveletResults.storeCoefficientsInFourierDomain) {
				hpFFT = waveletResults.getWaveletBand(iter);
			} else {
				double[] scale = waveletResults.getWaveletBand(iter);
				int numCoefficients = filterSet.getScaleWidth(iter)
					* filterSet.getScaleHeight(iter);
				hpFFT = new double[2 * numCoefficients];
				for (int i = 0; i < numCoefficients; i++)
					hpFFT[2 * i] = scale[i];
				DoubleFFT_2D fftScale =
					new DoubleFFT_2D(scaleHeight, scaleWidth);
				fftScale.complexForward(hpFFT);
			}
			// retrieve the filters in fourier domain
			double[][] filters = WaveletFunction.getFilterFFT(
					filterSet.getWaveletType(), scaleWidth, scaleHeight);
			// combine low pass and high pass images
			// TODO PHC, dilation factor here? 2^2 in approx.
			for (int i = 0; i < (int) (hpFFT.length / 2); i++) {
				lpFFT[2 * i] = 4 * lpFFT[2 * i] * filters[0][i]
					+ hpFFT[2 * i] * filters[1][i];
				lpFFT[2 * i + 1] = 4 * lpFFT[2 * i + 1] * filters[0][i]
					+ hpFFT[2 * i + 1] * filters[1][i];
			}
		}
		if (filterSet.isPrefilter()) {
			double[][] prefiltersFFT =
				WaveletFunction.getPreFiltersFFT(filterSet.waveletType,
						filterSet.getHPWidth(), filterSet.getHPHeight());
			double[] hpFFT;
			if (waveletResults.storeCoefficientsInFourierDomain) {
				// hpFFT = waveletResults.getHPResidual();
				hpFFT = waveletResults.getHPResidual().clone();
			} else {
				int numCoefficients =
					filterSet.getHPWidth() * filterSet.getHPHeight();
				hpFFT = new double[2 * numCoefficients];
				for (int i = 0; i < numCoefficients; i++)
					hpFFT[2 * i] = waveletResults.hpResidual[i];
				DoubleFFT_2D fftScale = new DoubleFFT_2D(
						filterSet.getHPHeight(), filterSet.getHPWidth());
				fftScale.complexForward(hpFFT);
			}
			convolveRealFFTFilterInPlace(hpFFT, prefiltersFFT[1]);
			convolveRealFFTFilterInPlace(lpFFT, prefiltersFFT[0]);
			for (int i = 0; i < lpFFT.length; i++)
				lpFFT[i] = lpFFT[i] + hpFFT[i];
		}
		// project back to spatial domain
		DoubleFFT_2D fftScale =
			new DoubleFFT_2D(filterSet.getHeight(), filterSet.getWidth());
		fftScale.complexInverse(lpFFT, true);
		double[] reconstruction = new double[lpFFT.length / 2];
		for (int i = 0; i < reconstruction.length; i++)
			reconstruction[i] = lpFFT[2 * i];
		return reconstruction;
			}

	/**
	 * Up sample a 2D image by duplicating its Fourier coefficients
	 * @param dataFFT Fourier coefficients of the image to upsample
	 * @param width width of the image
	 * @param height height of the image
	 * @return an array containing the Fourier coefficients of the upsampled
	 * image
	 *
	 * */
	static public double[] upsampleFFT(
			double[] dataFFT, int width, int height) {
		// upsampling by a factor two corresponds to duplicating the fourier
		// plane
		double[] upsampledFFT = new double[4 * dataFFT.length];
		for (int y = 0; y < height; y++) {
			System.arraycopy(dataFFT, 2 * y * width, upsampledFFT,
					2 * y * (2 * width), 2 * width);
			System.arraycopy(dataFFT, 2 * y * width, upsampledFFT,
					2 * (y * (2 * width) + width), 2 * width);
			System.arraycopy(dataFFT, 2 * y * width, upsampledFFT,
					2 * (y + height) * (2 * width), 2 * width);
			System.arraycopy(dataFFT, 2 * y * width, upsampledFFT,
					2 * ((y + height) * (2 * width) + width), 2 * width);
		}
		return upsampledFFT;
			}

	/**
	 * Perform wavelet decomposition of a 2D image
	 * @param image the 2D image to decompose as a 1D array pixel value at (x,
	 * y) = image[x +y*width]
	 * @param imageWidth width of the 2D image
	 * @param imageHeight height of the 2D image
	 * @param waveletFilters set of wavelet filters to use for the decomposition
	 * */
	public static WaveletAnalysisResults isotropicBandlimitedAnalysis(
			double[] image, int imageWidth, int imageHeight,
			WaveletFilterSet waveletFilters) {
		return isotropicBandlimitedAnalysis(
				image, imageWidth, imageHeight, waveletFilters, false);
			}

	/**
	 * Perform wavelet decomposition of a 2D image
	 * @param image the 2D image to decompose as a 1D array pixel value at (x,
	 * y) = image[x +y*width]
	 * @param imageWidth width of the 2D image
	 * @param imageHeight height of the 2D image
	 * @param waveletFilters set of wavelet filters to use for the decomposition
	 * @param coefficientsInFourierDomain true if wavelet coefficients are to be
	 * stored in the Fourier domain
	 * */
	public static WaveletAnalysisResults isotropicBandlimitedAnalysis(
			double[] image, int imageWidth, int imageHeight,
			WaveletFilterSet waveletFilters, boolean coefficientsInFourierDomain) {
		WaveletAnalysisResults results = new WaveletAnalysisResults();
		results.filterSet = waveletFilters;
		results.storeCoefficientsInFourierDomain = coefficientsInFourierDomain;
		results.bands = new double[waveletFilters.getNumScales()][];

		int width = waveletFilters.width;
		int height = waveletFilters.height;

		// pad image to fit filter size
		int[] pad = computePadding(imageWidth, imageHeight, width, height);
		results.padX = pad[0];
		results.padY = pad[1];
		results.fullWidth = imageWidth;
		results.fullHeight = imageHeight;
		image = padImage(
				image, imageWidth, imageHeight, width, height, pad[0], pad[1]);
		// Sequence padSequence = new Sequence("pad");
		// padSequence.addImage(new IcyBufferedImage(width, height, image));
		// Icy.getMainInterface().addSequence(padSequence);

		double[] dataFFT_t = new double[image.length * 2];
		for (int i = 0; i < image.length; i++)
			dataFFT_t[2 * i] = image[i];
		DoubleFFT_2D fft = new DoubleFFT_2D(height, width);
		fft.complexForward(dataFFT_t);
		if (waveletFilters.prefilter) {
			double[][] prefiltersFFT = waveletFilters.prefilterFFT;
			double[] hpFFT = convolveRealFFTFilter(dataFFT_t, prefiltersFFT[1]);
			convolveRealFFTFilterInPlace(dataFFT_t, prefiltersFFT[0]);
			// results.hpSize = new int[]{width, height};
			if (coefficientsInFourierDomain) {
				results.hpResidual = hpFFT;
			} else {
				DoubleFFT_2D fftScale = new DoubleFFT_2D(height, width);
				fftScale.complexInverse(hpFFT, true);
				results.hpResidual = new double[height * width];
				for (int i = 0; i < results.hpResidual.length; i++)
					results.hpResidual[i] = hpFFT[2 * i];
			}
		}
		for (int scale = 0; scale < waveletFilters.numScales; scale++) {
			int scaleWidth = waveletFilters.getScaleWidth(scale);
			int scaleHeight = waveletFilters.getScaleHeight(scale);
			double[][] filters = waveletFilters.waveletFiltersFFT.get(scale);
			double[] fftHP = convolveRealFFTFilter(dataFFT_t, filters[1]);
			convolveRealFFTFilterInPlace(dataFFT_t, filters[0]);
			dataFFT_t =
				downSampleFourierDomain(dataFFT_t, scaleWidth, scaleHeight);
			if (coefficientsInFourierDomain) {
				results.bands[scale] = fftHP;
			} else {
				DoubleFFT_2D fftScale =
					new DoubleFFT_2D(scaleHeight, scaleWidth);
				fftScale.complexInverse(fftHP, true);
				double[] hp = new double[scaleWidth * scaleHeight];
				for (int i = 0; i < hp.length; i++)
					hp[i] = fftHP[2 * i];
				results.bands[scale] = hp;
			}
		}
		// project back the low pass residual
		if (coefficientsInFourierDomain) {
			results.lpResidual = dataFFT_t;
		} else {
			int scaleHeight = waveletFilters.getLPHeight();
			int scaleWidth = waveletFilters.getLPWidth();
			DoubleFFT_2D fftScale = new DoubleFFT_2D(scaleHeight, scaleWidth);
			fftScale.complexInverse(dataFFT_t, true);
			results.lpResidual = new double[scaleHeight * scaleWidth];
			for (int i = 0; i < results.lpResidual.length; i++)
				results.lpResidual[i] = dataFFT_t[2 * i];
		}
		return results;
			}

	/**
	 * Test function of Fourier domain decomposition and reconstruction.
	 * */
	protected static void testFFT() {
		Sequence seq = Icy.getMainInterface().getActiveSequence();
		if (seq == null)
			return;
		int width = seq.getSizeX();
		int height = seq.getSizeY();
		// just show FFT  for the first image of the sequence
		DoubleFFT_2D fft = new DoubleFFT_2D(height, width);
		IcyBufferedImage image = seq.getFirstImage();
		double[] data = (double[]) ArrayUtil.arrayToDoubleArray(
				image.getDataXY(0), image.isSignedDataType());
		double[] dataFFT = new double[data.length * 2];
		for (int i = 0; i < data.length; i++)
			dataFFT[2 * i] = data[i];
		fft.complexForward(dataFFT);
		Sequence resSeq = new Sequence();
		resSeq.setName("FFT");
		double[] dataReal = new double[dataFFT.length / 2];
		double[] dataImaginary = new double[dataFFT.length / 2];
		for (int i = 0; i < dataReal.length; i++) {
			dataReal[i] = dataFFT[2 * i];
			dataImaginary[i] = dataFFT[2 * i + 1];
		}
		IcyBufferedImage realImage =
			new IcyBufferedImage(width, height, dataReal);
		IcyBufferedImage complexImage =
			new IcyBufferedImage(width, height, dataImaginary);
		resSeq.addImage(0, realImage);
		resSeq.addImage(1, complexImage);
		Icy.getMainInterface().addSequence(resSeq);

		double[] reverse = new double[dataFFT.length];
		System.arraycopy(dataFFT, 0, reverse, 0, reverse.length);
		DoubleFFT_2D fftRec = new DoubleFFT_2D(height, width);
		fftRec.complexInverse(reverse, true);
		double[] revImage = new double[width * height];
		for (int i = 0; i < revImage.length; i++)
			revImage[i] = reverse[2 * i];
		Sequence reconstructSeq = new Sequence();
		reconstructSeq.setName("reconstruction");
		IcyBufferedImage inverseImage =
			new IcyBufferedImage(width, height, revImage);
		reconstructSeq.addImage(0, inverseImage);
		Icy.getMainInterface().addSequence(reconstructSeq);
	}
}
