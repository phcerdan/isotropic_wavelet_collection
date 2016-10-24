/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D
 *  ij.ImagePlus
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package monogenicwavelettoolbox;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.ImageAdjuster;

public class FloatArray2D
extends FloatArrayGeneric {
    public FloatArray2D(int height, int width) {
        this.mDepth = 1;
        this.mHeight = height;
        this.mWidth = width;
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = new double[this.mHeight * this.mWidthStep];
    }

    public FloatArray2D(double[][] realArray) {
        this.mDepth = 1;
        this.mHeight = realArray.length;
        this.mWidth = realArray[0].length;
        this.mWidthStep = 2 * this.mWidth;
        this.mFArr = new double[this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                this.mFArr[i * this.mWidthStep + 2 * j] = realArray[i][j];
                this.mFArr[i * this.mWidthStep + 2 * j + 1] = 0.0;
                ++j;
            }
            ++i;
        }
    }

    public FloatArray2D(float[][] realArray) {
        this.mDepth = 1;
        this.mHeight = realArray.length;
        this.mWidth = realArray[0].length;
        this.mWidthStep = 2 * this.mWidth;
        this.mFArr = new double[this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                this.mFArr[i * this.mWidthStep + 2 * j] = realArray[i][j];
                this.mFArr[i * this.mWidthStep + 2 * j + 1] = 0.0;
                ++j;
            }
            ++i;
        }
    }

    public FloatArray2D(ImagePlus imp) {
        this(imp.getProcessor().getFloatArray());
    }

    public FloatArray2D(FloatArray2D f) {
        this.mFArr = f.copy();
        this.mDepth = f.getMDepth();
        this.mHeight = f.getMHeight();
        this.mWidth = f.getMWidth();
        this.mWidthStep = this.mWidth * 2;
    }

    public double[][] getRealArray2D(int height, int width) {
        double[][] fArr = new double[height][width];
        int i = 0;
        while (i < height) {
            int j = 0;
            while (j < width) {
                fArr[i][j] = this.mFArr[i * this.mWidthStep + j * 2];
                ++j;
            }
            ++i;
        }
        return fArr;
    }

    public float[][] getRealArray2DFloat(int height, int width) {
        float[][] fArr = new float[height][width];
        int i = 0;
        while (i < height) {
            int j = 0;
            while (j < width) {
                fArr[i][j] = (float)this.mFArr[i * this.mWidthStep + j * 2];
                ++j;
            }
            ++i;
        }
        return fArr;
    }

    public ImagePlus getImagePlusReal(String name) {
        return this.getImagePlusReal(name, this.mDepth, this.mHeight, this.mWidth);
    }

    public double[][] getImagArray2D() {
        double[][] fArr = new double[this.mHeight][this.mWidth];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                fArr[i][j] = this.mFArr[i * this.mWidthStep + j * 2 + 1];
                ++j;
            }
            ++i;
        }
        return fArr;
    }

    public void ifftshift() {
        FloatArray2D f_aux = (FloatArray2D)this.duplicate();
        int hh = this.mHeight / 2;
        int wh = this.mWidth / 2;
        int i = 0;
        while (i < hh) {
            int j = 0;
            while (j < wh) {
                this.setComplexValue(i, j, f_aux.getComplexValue(i + hh, j + wh));
                this.setComplexValue(i + hh, j + wh, f_aux.getComplexValue(i, j));
                this.setComplexValue(i + hh, j, f_aux.getComplexValue(i, j + wh));
                this.setComplexValue(i, j + wh, f_aux.getComplexValue(i + hh, j));
                ++j;
            }
            ++i;
        }
    }

    public void setRealValue(int i, int j, double val) {
        this.mFArr[i * this.mWidthStep + 2 * j] = val;
    }

    public void setImagValue(int i, int j, double val) {
        this.mFArr[i * this.mWidthStep + 2 * j + 1] = val;
    }

    public void setComplexValue(int i, int j, double[] complex) {
        this.mFArr[i * this.mWidthStep + 2 * j] = complex[0];
        this.mFArr[i * this.mWidthStep + 2 * j + 1] = complex[1];
    }

    public double getRealValue(int i, int j) {
        return this.mFArr[i * this.mWidthStep + 2 * j];
    }

    public double getImagValue(int i, int j) {
        return this.mFArr[i * this.mWidthStep + 2 * j + 1];
    }

    public double[] getComplexValue(int i, int j) {
        double[] complex = new double[]{this.mFArr[i * this.mWidthStep + 2 * j], this.mFArr[i * this.mWidthStep + 2 * j + 1]};
        return complex;
    }

    public void fft() {
        DoubleFFT_2D fftTransformer = new DoubleFFT_2D(this.mHeight, this.mWidth);
        fftTransformer.complexForward(this.mFArr);
    }

    public void ifft(boolean scale) {
        DoubleFFT_2D fftTransformer = new DoubleFFT_2D(this.mHeight, this.mWidth);
        fftTransformer.complexInverse(this.mFArr, scale);
    }

    public void ifft() {
        this.ifft(true);
    }

    public FloatArrayGeneric downsampling(int n) {
        if (n < 0) {
            throw new RuntimeException("Attempt to downsampling less than 0");
        }
        int m = (int)Math.pow(2.0, n);
        if (m >= this.mWidth || m >= this.mHeight) {
            throw new RuntimeException("Downsampling not possible, array too small");
        }
        this.mWidth /= m;
        this.mHeight /= m;
        int widthStepOld = this.mWidthStep;
        this.mWidthStep /= m;
        double[] fArr2 = new double[this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                fArr2[i * this.mWidthStep + 2 * j] = this.mFArr[m * i * widthStepOld + m * 2 * j];
                fArr2[i * this.mWidthStep + 2 * j + 1] = this.mFArr[m * i * widthStepOld + m * 2 * j + 1];
                ++j;
            }
            ++i;
        }
        this.mFArr = (double[])fArr2.clone();
        return this;
    }

    public void upsampling(int n) {
        if (n < 1) {
            throw new RuntimeException("Attempt to upsampling less than once");
        }
        int m = (int)Math.pow(2.0, n);
        int widthStepOld = this.mWidthStep;
        this.mWidthStep *= m;
        double[] fArr2 = new double[this.mHeight * m * this.mWidthStep];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                fArr2[m * i * this.mWidthStep + m * 2 * j] = this.mFArr[i * widthStepOld + 2 * j];
                fArr2[m * i * this.mWidthStep + m * 2 * j + 1] = this.mFArr[i * widthStepOld + 2 * j + 1];
                ++j;
            }
            ++i;
        }
        this.mWidth *= m;
        this.mHeight *= m;
        this.mFArr = (double[])fArr2.clone();
    }

    public void expand() {
        int m = 2;
        double[] fArr2 = new double[this.mHeight * 2 * this.mWidthStep * 2];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                fArr2[4 * i * this.mWidthStep + 4 * j] = this.mFArr[i * this.mWidthStep + 2 * j];
                fArr2[4 * i * this.mWidthStep + 4 * j + 1] = this.mFArr[i * this.mWidthStep + 2 * j + 1];
                fArr2[4 * i * this.mWidthStep + 4 * j + 2] = this.mFArr[i * this.mWidthStep + 2 * j];
                fArr2[4 * i * this.mWidthStep + 4 * j + 3] = this.mFArr[i * this.mWidthStep + 2 * j + 1];
                fArr2[2 * (2 * i + 1) * this.mWidthStep + 4 * j] = this.mFArr[i * this.mWidthStep + 2 * j];
                fArr2[2 * (2 * i + 1) * this.mWidthStep + 4 * j + 1] = this.mFArr[i * this.mWidthStep + 2 * j + 1];
                fArr2[2 * (2 * i + 1) * this.mWidthStep + 4 * j + 2] = this.mFArr[i * this.mWidthStep + 2 * j];
                fArr2[2 * (2 * i + 1) * this.mWidthStep + 4 * j + 3] = this.mFArr[i * this.mWidthStep + 2 * j + 1];
                ++j;
            }
            ++i;
        }
        this.mWidth *= 2;
        this.mHeight *= 2;
        this.mWidthStep *= 2;
        this.mFArr = (double[])fArr2.clone();
    }

    public void downsamplingAverage() {
        int m = 2;
        this.mWidth /= 2;
        this.mHeight /= 2;
        int widthStepOld = this.mWidthStep;
        this.mWidthStep /= 2;
        double[] fArr2 = new double[this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                fArr2[i * this.mWidthStep + 2 * j] = (this.mFArr[i * widthStepOld + 4 * j] + this.mFArr[i * widthStepOld + 4 * (j + 1)] + this.mFArr[(i + 1) * widthStepOld + 4 * j] + this.mFArr[(i + 1) * widthStepOld + 4 * (j + 1)]) / 4.0;
                fArr2[i * this.mWidthStep + 2 * j + 1] = (this.mFArr[i * widthStepOld + 4 * j + 1] + this.mFArr[i * widthStepOld + 4 * (j + 1) + 1] + this.mFArr[(i + 1) * widthStepOld + 4 * j + 1] + this.mFArr[(i + 1) * widthStepOld + 4 * (j + 1) + 1]) / 4.0;
                ++j;
            }
            ++i;
        }
        this.mFArr = (double[])fArr2.clone();
    }

    public ImagePlus getImagePlusReal(String name, int depth, int height, int width) {
        ImagePlus imp = new ImagePlus(name, (ImageProcessor)new FloatProcessor(this.getRealArray2DFloat(width, height)));
        return imp;
    }

    public void extendSizeToPowerOf2() {
        if (ImageAdjuster.isPowerOf2(this.mWidth) && ImageAdjuster.isPowerOf2(this.mHeight)) {
            return;
        }
        int newHeight = ImageAdjuster.nextPowerOf2(this.mHeight);
        int newWidth = ImageAdjuster.nextPowerOf2(this.mWidth);
        this.symmExtend(newHeight, newWidth);
    }

    public void revertSymmetricExtension() {
    }

    public void symmetricExtension() {
        this.symmExtend(2 * this.mHeight, 2 * this.mWidth);
    }

    private void symmExtend(int newHeight, int newWidth) {
        int k;
        int newWidthStep = 2 * newWidth;
        double[] oldFloat = this.mFArr;
        double[] newFloat = new double[newHeight * newWidthStep];
        int j = 0;
        while (j < this.mHeight) {
            k = 0;
            while (k < this.mWidth) {
                newFloat[j * newWidthStep + 2 * k] = oldFloat[j * this.mWidthStep + 2 * k];
                newFloat[j * newWidthStep + 2 * k + 1] = oldFloat[j * this.mWidthStep + 2 * k + 1];
                ++k;
            }
            ++j;
        }
        j = this.mHeight;
        while (j < newHeight) {
            k = 0;
            while (k < this.mWidth) {
                newFloat[j * newWidthStep + 2 * k] = oldFloat[(2 * this.mHeight - j - 1) * this.mWidthStep + 2 * k];
                newFloat[j * newWidthStep + 2 * k + 1] = oldFloat[(2 * this.mHeight - j - 1) * this.mWidthStep + 2 * k + 1];
                ++k;
            }
            ++j;
        }
        j = 0;
        while (j < newHeight) {
            k = this.mWidthStep;
            while (k < newWidthStep - 1) {
                newFloat[j * newWidthStep + k] = newFloat[(j + 1) * newWidthStep - k - 2];
                ++k;
            }
            ++j;
        }
        this.mFArr = newFloat;
        this.mHeight = newHeight;
        this.mWidth = newWidth;
        this.mWidthStep = newWidthStep;
    }

    public FloatArrayGeneric localStd(int windowSize) {
        int windowSize_half = windowSize / 2;
        double windowSizeSquared = windowSize * windowSize;
        FloatArray2D varArray = new FloatArray2D(this.mHeight, this.mWidth);
        int i = 0;
        while (i < this.mHeight) {
            int j = 0;
            while (j < this.mWidth) {
                int m;
                double var = 0.0;
                double mu = 0.0;
                int n = - windowSize_half;
                while (n <= windowSize_half) {
                    m = - windowSize_half;
                    while (m <= windowSize_half) {
                        mu += this.getRealValue((i + n + this.mHeight) % this.mHeight, (j + m + this.mWidth) % this.mWidth);
                        ++m;
                    }
                    ++n;
                }
                mu /= windowSizeSquared;
                n = - windowSize_half;
                while (n <= windowSize_half) {
                    m = - windowSize_half;
                    while (m <= windowSize_half) {
                        var += Math.pow(mu - this.getRealValue((i + n + this.mHeight) % this.mHeight, (j + m + this.mWidth) % this.mWidth), 2.0);
                        ++m;
                    }
                    ++n;
                }
                varArray.setRealValue(i, j, Math.sqrt(var / (windowSizeSquared - 1.0)));
                ++j;
            }
            ++i;
        }
        return varArray;
    }
}

