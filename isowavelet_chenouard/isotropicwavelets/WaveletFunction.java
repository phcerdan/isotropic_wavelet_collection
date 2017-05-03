package plugins.nchenouard.isotropicwavelets;

import icy.image.IcyBufferedImage;
import icy.main.Icy;
import icy.sequence.Sequence;

/**
 *
 * Filters for wavelet transform and low-pass prefiltering
 *
 * 2D wavelet decomposition and reconstruction algorithms are provided.
 * Transforms are as decribed in:

 * Chenouard, N.; Unser, M., "3D Steerable Wavelets in Practice"
 * IEEE Transactions on Image Processing, vol.21, no.11, pp.4522,4533, Nov. 2012
 * doi: 10.1109/TIP.2012.2206044 *
 *
 *  except being implemented in 2D instead of 3D.
 *
 *  Please cite the above reference in scientific communications upon use of
 these tools.
 *
 *
 * @author Nicolas Chenouard (nicolas.chenouard.dev@gmail.com)
 * @version 1.0
 * @date 2014-05-22
 * @license gpl v3.0
 */

public class WaveletFunction {
	/**
	 * nu function for Meyer wavelets
	 * @param t
	 * @return nu(t)
	 * */
	private static double nu(double t) {
		if (t <= 0 || t >= 1)
			return 0;
		else
			return t * t * t * t * (35 - 84 * t + 70 * t * t - 20 * t * t * t);
	}

	/**
	 * Get wavelet filter analysis filters in Fourier domain
	 * @param waveletType type of radial function for the wavelet
	 * @param width width of the filter
	 * @param height height of the filter
	 * @return 2D wavelet filter in the Fourier domain
	 * */
	public static double[][] getFilterFFT(
			IsotropicWaveletType waveletType, int width, int height) {
		double[] distMap = new double[width * height];
		int cX = (int) Math.ceil((width + 1) / 2);
		int cY = (int) Math.ceil((height + 1) / 2);
		double normX = (double) (cX * cX);
		double normY = (double) (cY * cY);

		for (int i = 0; i <= cX; i++) {
			for (int j = 0; j <= cY; j++) {
				distMap[i + j * width] =
					(double) Math.sqrt(i * i / normX + j * j / normY);
			}
			for (int j = cY + 1; j < height; j++) {
				distMap[i + j * width] = (double) Math.sqrt(
						i * i / normX + (j - 2 * cY) * (j - 2 * cY) / normY);
			}
		}
		for (int i = cX + 1; i < width; i++) {
			for (int j = 0; j <= cY; j++) {
				distMap[i + j * width] = (double) Math.sqrt(
						(i - 2 * cX) * (i - 2 * cX) / normX + j * j / normY);
			}
			for (int j = cY + 1; j < height; j++) {
				distMap[i + j * width] =
					(double) Math.sqrt((i - 2 * cX) * (i - 2 * cX) / normX
							+ (j - 2 * cY) * (j - 2 * cY) / normY);
			}
		}
		double[] lp = new double[distMap.length];
		double[] hp = new double[distMap.length];
		switch (waveletType) {
			case Meyer: {
							//			 maskLP = mask4; < pi/4
							//			    maskLP = maskLP +
							//cos(0.5*pi*nu(4*dist-1)).*mask2.*(1-mask4);
							//			    maskHP = sqrt(1-maskLP.^2);
							for (int i = 0; i < distMap.length; i++) {
								double dist = distMap[i];
								if (dist < 1d / 4) {
									lp[i] = 1;
								} else if (dist < 1d / 2) {
									lp[i] = Math.cos(0.5 * Math.PI * nu(4 * dist - 1));
								}
								hp[i] = Math.sqrt(1 - lp[i] * lp[i]);
							}
							break;
			}
			case Papadakis: {
								//			%create mask selecting r<=3*pi/10
								//			maskLP = sqrt((1+cos(5*dist*pi -
								//3*pi/2))/2).*mask2.*(1-mask310);
								//			maskLP(1) = 0;
								//			maskLP = maskLP + mask310;
								//
								//			maskHP = sqrt(1-maskLP.^2);

								for (int i = 0; i < distMap.length; i++) {
									double dist = distMap[i];
									if (dist <= 3d / 10) {
										lp[i] = 1;
									} else if (dist <= 1d / 2) {
										lp[i] = Math.sqrt(
												(1 + Math.cos(5 * dist * Math.PI - 3 * Math.PI / 2))
												/ 2);
										hp[i] = Math.sqrt(1 - lp[i] * lp[i]);
									} else
										hp[i] = 1;
								}
								break;
			}
			case Shannon: {
							  for (int i = 0; i < distMap.length; i++) {
								  double dist = distMap[i];
								  if (dist < 0.5f) // ||w|| < pi
								  {
									  lp[i] = 1;
								  } else // ||w|| >= pi
								  {
									  hp[i] = 1;
								  }
							  }
							  hp[0] = 0;
							  //			lp[0] = 0;
							  break;
			}
			case Simoncelli: {
								 for (int i = 0; i < distMap.length; i++) {
									 double dist = distMap[i];
									 if (dist < 0.5f) // ||w|| < pi
									 {
										 if (dist <= 0.25f) // ||w|| <= pi/2
										 {
											 lp[i] = 1;
										 } else // ||w|| > pi/2
										 {
											 hp[i] = (double) Math.cos(Math.PI * 0.5
													 * Math.log(2 * dist) / Math.log(2));
											 lp[i] = (double) Math.cos(Math.PI * 0.5
													 * Math.log(4 * dist) / Math.log(2));
										 }
									 } else // ||w|| >= pi
									 {
										 hp[i] = 1;
									 }
								 }
								 hp[0] = 0;
								 //			lp[0] = 0;
								 break;
			}
		}
		boolean debug = false;
		if (debug) {
			double[] sumSqFilters = new double[hp.length];
			for (int i = 0; i < sumSqFilters.length; i++) {
				sumSqFilters[i] = hp[i] * hp[i] + lp[i] * lp[i];
			}
			Sequence distSeq = new Sequence();
			distSeq.setName("dist");
			IcyBufferedImage realImage =
				new IcyBufferedImage(width, height, distMap);
			distSeq.addImage(0, realImage);
			IcyBufferedImage lpImage = new IcyBufferedImage(width, height, lp);
			distSeq.addImage(1, lpImage);
			IcyBufferedImage hpImage = new IcyBufferedImage(width, height, hp);
			distSeq.addImage(2, hpImage);
			IcyBufferedImage sumSqImage =
				new IcyBufferedImage(width, height, sumSqFilters);
			distSeq.addImage(3, sumSqImage);
			Icy.getMainInterface().addSequence(distSeq);
		}
		return new double[][] {lp, hp};
			}

	/**
	 * Get prefilter in Fourier domain for removing high frequency coefficients
	 * (corners of Fourier domain)
	 * @param waveletType type of radial function for the wavelet
	 * @param width width of the filter
	 * @param height height of the filter
	 * @return 2D Fourier coefficients of the prefilter
	 * */
	public static double[][] getPreFiltersFFT(
			IsotropicWaveletType waveletType, int width, int height) {
		double[] distMap = new double[width * height];
		int cX = (int) Math.ceil((width + 1) / 2);
		int cY = (int) Math.ceil((height + 1) / 2);
		double normX = (double) (cX * cX);
		double normY = (double) (cY * cY);

		for (int i = 0; i <= cX; i++) {
			for (int j = 0; j <= cY; j++) {
				distMap[i + j * width] =
					(double) Math.sqrt(i * i / normX + j * j / normY);
			}
			for (int j = cY + 1; j < height; j++) {
				distMap[i + j * width] = (double) Math.sqrt(
						i * i / normX + (j - 2 * cY) * (j - 2 * cY) / normY);
			}
		}
		for (int i = cX + 1; i < width; i++) {
			for (int j = 0; j <= cY; j++) {
				distMap[i + j * width] = (double) Math.sqrt(
						(i - 2 * cX) * (i - 2 * cX) / normX + j * j / normY);
			}
			for (int j = cY + 1; j < height; j++) {
				distMap[i + j * width] =
					(double) Math.sqrt((i - 2 * cX) * (i - 2 * cX) / normX
							+ (j - 2 * cY) * (j - 2 * cY) / normY);
			}
		}
		double[] hp = new double[distMap.length];
		double[] lp = new double[distMap.length];
		switch (waveletType) {
			case Meyer: {
							for (int i = 0; i < distMap.length; i++) {
								double dist = distMap[i] / 2;
								if (dist < 1d / 4) {
									lp[i] = 1;
								} else if (dist < 1d / 2) {
									lp[i] = Math.cos(0.5 * Math.PI * nu(4 * dist - 1));
								}
								hp[i] = Math.sqrt(1 - lp[i] * lp[i]);
							}
							break;
			}
			case Papadakis: {
								for (int i = 0; i < distMap.length; i++) {
									double dist = distMap[i] / 2;
									if (dist <= 3d / 10) {
										lp[i] = 1;
									} else if (dist <= 1d / 2) {
										lp[i] = Math.sqrt(
												(1 + Math.cos(5 * dist * Math.PI - 3 * Math.PI / 2))
												/ 2);
										hp[i] = Math.sqrt(1 - lp[i] * lp[i]);
									} else
										hp[i] = 1;
								}
								break;
			}
			case Shannon: {
							  for (int i = 0; i < distMap.length; i++) {
								  double dist = distMap[i];
								  if (dist > 1f)
									  hp[i] = 1;
								  else
									  lp[i] = 1;
							  }
							  break;
			}
			case Simoncelli: {
								 for (int i = 0; i < distMap.length; i++) {
									 double dist = distMap[i];
									 if (dist > 1f)
										 hp[i] = 1;
									 else if (dist > 0.5f) // ||w|| > pi
									 {
										 hp[i] = (double) Math.cos(
												 Math.PI * 0.5 * Math.log(dist) / Math.log(2));
										 lp[i] = (double) Math.sqrt(1 - hp[i] * hp[i]);
									 } else {
										 lp[i] = 1;
									 }
								 }
								 break;
			}
		}
		boolean debug = false;
		if (debug) {
			Sequence distSeq = new Sequence();
			distSeq.setName("dist");
			IcyBufferedImage hpImage = new IcyBufferedImage(width, height, hp);
			distSeq.addImage(1, hpImage);
			Icy.getMainInterface().addSequence(distSeq);
		}
		return new double[][] {lp, hp};
			}
}
