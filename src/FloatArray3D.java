/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  edu.emory.mathcs.jtransforms.fft.DoubleFFT_3D
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package monogenicwavelettoolbox;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_3D;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import monogenicwavelettoolbox.FloatArrayGeneric;

public class FloatArray3D
extends FloatArrayGeneric {
    public FloatArray3D(int depth, int height, int width) {
        this.mDepth = depth;
        this.mHeight = height;
        this.mWidth = width;
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = new double[this.mDepth * this.mHeight * this.mWidthStep];
    }

    public FloatArray3D(FloatArray3D f2) {
        this.mDepth = f2.getMDepth();
        this.mHeight = f2.getMHeight();
        this.mWidth = f2.getMWidth();
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = f2.copy();
    }

    public FloatArray3D(double[][][] realArray) {
        this.mDepth = realArray.length;
        this.mHeight = realArray[0].length;
        this.mWidth = realArray[0][0].length;
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = new double[this.mDepth * this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mDepth) {
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k] = realArray[i][j][k];
                    this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1] = 0.0;
                    ++k;
                }
                ++j;
            }
            ++i;
        }
    }

    public FloatArray3D(float[][][] realArray) {
        this.mDepth = realArray.length;
        this.mHeight = realArray[0].length;
        this.mWidth = realArray[0][0].length;
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = new double[this.mDepth * this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mDepth) {
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k] = realArray[i][j][k];
                    this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1] = 0.0;
                    ++k;
                }
                ++j;
            }
            ++i;
        }
    }

    public FloatArray3D(ImagePlus imp) {
        this.mDepth = imp.getStackSize();
        this.mHeight = imp.getHeight();
        this.mWidth = imp.getWidth();
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = new double[this.mDepth * this.mHeight * this.mWidthStep];
        int i = 0;
        while (i < this.mDepth) {
            ImageProcessor ip = imp.getStack().getProcessor(i + 1);
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k] = ip.getPixelValue(k, j);
                    this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1] = 0.0;
                    ++k;
                }
                ++j;
            }
            ++i;
        }
    }

    public void ifftshift() {
        FloatArray3D f_aux = (FloatArray3D)this.duplicate();
        int dh = this.mDepth / 2;
        int hh = this.mHeight / 2;
        int wh = this.mWidth / 2;
        int i = 0;
        while (i < dh) {
            int j = 0;
            while (j < hh) {
                int k = 0;
                while (k < wh) {
                    this.setComplexValue(i, j, k, f_aux.getComplexValue(i + dh, j + hh, k + wh));
                    this.setComplexValue(i + dh, j + hh, k + wh, f_aux.getComplexValue(i, j, k));
                    this.setComplexValue(i + dh, j, k, f_aux.getComplexValue(i, j + hh, k + wh));
                    this.setComplexValue(i, j + hh, k, f_aux.getComplexValue(i + dh, j, k + wh));
                    this.setComplexValue(i, j, k + wh, f_aux.getComplexValue(i + dh, j + hh, k));
                    this.setComplexValue(i + dh, j + hh, k, f_aux.getComplexValue(i, j, k + wh));
                    this.setComplexValue(i + dh, j, k + wh, f_aux.getComplexValue(i, j + hh, k));
                    this.setComplexValue(i, j + hh, k + wh, f_aux.getComplexValue(i + dh, j, k));
                    ++k;
                }
                ++j;
            }
            ++i;
        }
    }

    public void setRealValue(int i, int j, int k, double val) {
        this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k] = val;
    }

    public void setImagValue(int i, int j, int k, double val) {
        this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1] = val;
    }

    public void setComplexValue(int i, int j, int k, double[] complex) {
        this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k] = complex[0];
        this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1] = complex[1];
    }

    public double[] getComplexValue(int i, int j, int k) {
        double[] complex = new double[]{this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k], this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1]};
        return complex;
    }

    public void fft() {
        DoubleFFT_3D fftTransformer = new DoubleFFT_3D(this.mDepth, this.mHeight, this.mWidth);
        fftTransformer.complexForward(this.mFArr);
    }

    public void ifft(boolean scale) {
        DoubleFFT_3D fftTransformer = new DoubleFFT_3D(this.mDepth, this.mHeight, this.mWidth);
        fftTransformer.complexInverse(this.mFArr, scale);
    }

    public void ifft() {
        this.ifft(true);
    }

    public ImagePlus getImagePlusReal(String name) {
        ImageStack stack = new ImageStack(this.mWidth, this.mHeight);
        int i = 0;
        while (i < this.mDepth) {
            float[][] fArr2 = new float[this.mWidth][this.mHeight];
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    fArr2[k][j] = (float)this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k];
                    ++k;
                }
                ++j;
            }
            stack.addSlice(null, (ImageProcessor)new FloatProcessor(fArr2));
            ++i;
        }
        ImagePlus imp = new ImagePlus(name, stack);
        imp.resetDisplayRange();
        imp.updateAndDraw();
        return imp;
    }

    public ImagePlus getImagePlusImag(String name) {
        ImageStack stack = new ImageStack(this.mHeight, this.mWidth);
        int i = 0;
        while (i < this.mDepth) {
            float[][] fArr2 = new float[this.mHeight][this.mWidth];
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    fArr2[j][k] = (float)this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1];
                    ++k;
                }
                ++j;
            }
            stack.addSlice(null, (ImageProcessor)new FloatProcessor(fArr2));
            ++i;
        }
        ImagePlus imp = new ImagePlus(name, stack);
        imp.resetDisplayRange();
        imp.updateAndDraw();
        return imp;
    }

    public void showReal() {
        this.getImagePlusReal("test").show();
    }

    public void showImag() {
        this.getImagePlusImag("test").show();
    }

    public FloatArrayGeneric downsampling(int n) {
        if (n < 0) {
            throw new RuntimeException("Attempt to downsampling less than 0");
        }
        int m = (int)Math.pow(2.0, n);
        if (m >= this.mWidth || m >= this.mHeight || m >= this.mDepth) {
            throw new RuntimeException("Downsampling not possible, array too small");
        }
        int newDepth = this.mDepth / m;
        int newHeight = this.mHeight / m;
        int newWidth = this.mWidth / m;
        int newWidthStep = newWidth * 2;
        double[] fArr2 = new double[newDepth * newHeight * newWidthStep];
        int i = 0;
        while (i < newDepth) {
            int j = 0;
            while (j < newHeight) {
                int k = 0;
                while (k < newWidth) {
                    fArr2[i * newHeight * newWidthStep + j * newWidthStep + 2 * k] = this.mFArr[m * i * this.mHeight * this.mWidthStep + m * j * this.mWidthStep + m * 2 * k];
                    fArr2[i * newHeight * newWidthStep + j * newWidthStep + 2 * k + 1] = this.mFArr[m * i * this.mHeight * this.mWidthStep + m * j * this.mWidthStep + m * 2 * k + 1];
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        this.mWidth = newWidth;
        this.mHeight = newHeight;
        this.mDepth = newDepth;
        this.mWidthStep = newWidthStep;
        this.mFArr = (double[])fArr2.clone();
        return this;
    }

    public void upsampling(int n) {
        if (n < 1) {
            throw new RuntimeException("Attempt to upsampling less than once");
        }
        int m = (int)Math.pow(2.0, n);
        int newDepth = this.mDepth * m;
        int newHeight = this.mHeight * m;
        int newWidth = this.mWidth * m;
        int newWidthStep = newWidth * 2;
        double[] fArr2 = new double[newDepth * newHeight * newWidthStep];
        int i = 0;
        while (i < this.mDepth) {
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    fArr2[m * i * newHeight * newWidthStep + m * j * newWidthStep + m * 2 * k] = this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k];
                    fArr2[m * i * newHeight * newWidthStep + m * j * newWidthStep + m * 2 * k + 1] = this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1];
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        this.mWidth = newWidth;
        this.mHeight = newHeight;
        this.mDepth = newDepth;
        this.mWidthStep = newWidthStep;
        this.mFArr = (double[])fArr2.clone();
    }

    public void expand() {
        FloatArray3D f2 = new FloatArray3D(this.mDepth * 2, this.mHeight * 2, this.mWidth * 2);
        int i = 0;
        while (i < this.mDepth * 2) {
            int j = 0;
            while (j < this.mHeight * 2) {
                int k = 0;
                while (k < this.mWidth * 2) {
                    double[] complex = this.getComplexValue(i / 2, j / 2, k / 2);
                    f2.setComplexValue(i, j, k, complex);
                    f2.setComplexValue(i + 1, j, k, complex);
                    f2.setComplexValue(i + 1, j + 1, k, complex);
                    f2.setComplexValue(i + 1, j + 1, k + 1, complex);
                    f2.setComplexValue(i, j + 1, k, complex);
                    f2.setComplexValue(i, j + 1, k + 1, complex);
                    f2.setComplexValue(i, j, k + 1, complex);
                    f2.setComplexValue(i + 1, j, k + 1, complex);
                    k += 2;
                }
                j += 2;
            }
            i += 2;
        }
        this.mDepth *= 2;
        this.mHeight *= 2;
        this.mWidth *= 2;
        this.mWidthStep = this.mWidth * 2;
        this.mFArr = f2.getArray();
    }

    public void downsamplingAverage() {
        FloatArray3D f2 = new FloatArray3D(this.mDepth / 2, this.mHeight / 2, this.mWidth / 2);
        int i = 0;
        while (i < this.mDepth) {
            int j = 0;
            while (j < this.mHeight) {
                int k = 0;
                while (k < this.mWidth) {
                    double[] a = this.getComplexValue(i, j, k);
                    int l = 0;
                    while (l < 1) {
                        double[] arrd = a;
                        int n = l;
                        arrd[n] = arrd[n] + this.getComplexValue(i + 1, j, k)[l];
                        double[] arrd2 = a;
                        int n2 = l;
                        arrd2[n2] = arrd2[n2] + this.getComplexValue(i + 1, j + 1, k)[l];
                        double[] arrd3 = a;
                        int n3 = l;
                        arrd3[n3] = arrd3[n3] + this.getComplexValue(i + 1, j + 1, k + 1)[l];
                        double[] arrd4 = a;
                        int n4 = l;
                        arrd4[n4] = arrd4[n4] + this.getComplexValue(i + 1, j, k + 1)[l];
                        double[] arrd5 = a;
                        int n5 = l;
                        arrd5[n5] = arrd5[n5] + this.getComplexValue(i, j + 1, k)[l];
                        double[] arrd6 = a;
                        int n6 = l;
                        arrd6[n6] = arrd6[n6] + this.getComplexValue(i, j + 1, k + 1)[l];
                        double[] arrd7 = a;
                        int n7 = l;
                        arrd7[n7] = arrd7[n7] + this.getComplexValue(i, j, k + 1)[l];
                        ++l;
                    }
                    f2.setComplexValue(i / 2, j / 2, k / 2, a);
                    k += 2;
                }
                j += 2;
            }
            i += 2;
        }
        this.mDepth /= 2;
        this.mHeight /= 2;
        this.mWidth /= 2;
        this.mWidthStep = this.mWidth / 2;
        this.mFArr = f2.getArray();
    }

    public void extendSizeToPowerOf2() {
    }

    public ImagePlus getImagePlusReal(String name, int depth, int height, int width) {
        return this.getImagePlusReal(name);
    }

    public void revertSymmetricExtension() {
    }

    public void symmetricExtension() {
    }

    public FloatArrayGeneric localStd(int windowSize) {
        return null;
    }
}

