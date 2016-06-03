/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.ImageProcessor
 */
package monogenicwavelettoolbox;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.util.Arrays;
import monogenicwavelettoolbox.FloatArray2D;
import monogenicwavelettoolbox.FloatArray3D;

public abstract class FloatArrayGeneric {
    protected double[] mFArr;
    protected int mDepth;
    protected int mHeight;
    protected int mWidth;
    protected int mWidthStep;
    protected boolean symmetricExtended;

    public double[] getArray() {
        return this.mFArr;
    }

    public FloatArrayGeneric multiply(FloatArrayGeneric f2) {
        if (f2 == null) {
            return this;
        }
        this.checkSize(f2);
        double[] fArr2 = f2.getArray();
        int i = 0;
        while (i < this.mFArr.length) {
            double farr_aux = this.mFArr[i];
            this.mFArr[i] = this.mFArr[i] * fArr2[i] - this.mFArr[i + 1] * fArr2[i + 1];
            this.mFArr[i + 1] = farr_aux * fArr2[i + 1] + this.mFArr[i + 1] * fArr2[i];
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric divideReal(FloatArrayGeneric f2) {
        if (f2 == null) {
            return this;
        }
        this.checkSize(f2);
        double[] fArr2 = f2.getArray();
        int i = 0;
        while (i < this.mFArr.length) {
            if (fArr2[i] == 0.0) {
                throw new RuntimeException("Division by Zero");
            }
            this.mFArr[i] = this.mFArr[i] / fArr2[i];
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric multiply(double f2) {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = this.mFArr[i] * f2;
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric divide(double f2) {
        if (f2 == 0.0) {
            throw new RuntimeException("Division by Zero");
        }
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = this.mFArr[i] / f2;
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric sq() {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = this.mFArr[i] * this.mFArr[i] + this.mFArr[i + 1] * this.mFArr[i + 1];
            this.mFArr[i + 1] = 0.0;
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric cos() {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = Math.cos(this.mFArr[i]);
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric sqrt() {
        int i = 0;
        while (i < this.mFArr.length) {
            if (this.mFArr[i] < 0.0 || this.mFArr[i + 1] != 0.0) {
                throw new RuntimeException("Attempt to compute sqrt of complex or negative number");
            }
            this.mFArr[i] = Math.sqrt(this.mFArr[i]);
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric log() {
        int i = 0;
        while (i < this.mFArr.length) {
            if (this.mFArr[i] < 0.0 || this.mFArr[i + 1] != 0.0) {
                throw new RuntimeException("Attempt to compute log of complex or negative number");
            }
            this.mFArr[i] = Math.log(this.mFArr[i]);
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric add(FloatArrayGeneric f2) {
        if (f2 == null) {
            return this;
        }
        this.checkSize(f2);
        double[] fArr2 = f2.getArray();
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = this.mFArr[i] + fArr2[i];
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric add(double f2) {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = this.mFArr[i] + f2;
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric sub(FloatArrayGeneric f2) {
        this.checkSize(f2);
        double[] fArr2 = f2.getArray();
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = this.mFArr[i] - fArr2[i];
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric invertSign() {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = - this.mFArr[i];
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric atan2(FloatArrayGeneric f2) {
        double[] fArr2 = f2.getArray();
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = Math.atan2(this.mFArr[i], fArr2[i]);
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric atan() {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = Math.atan(this.mFArr[i]);
            ++i;
        }
        return this;
    }

    public FloatArrayGeneric abs() {
        this.sq().sqrt();
        return this;
    }

    protected void checkSize(FloatArrayGeneric f2) {
        if (!this.hasEqualDimension(f2)) {
            throw new RuntimeException("Array dimensions do not match");
        }
    }

    protected boolean hasEqualDimension(FloatArrayGeneric f2) {
        if (this.mDepth == f2.getMDepth() && this.mWidth == f2.getMWidth() && this.mHeight == f2.mHeight) {
            return true;
        }
        return false;
    }

    public FloatArrayGeneric phaseWrapping() {
        int i = 0;
        while (i < this.mFArr.length) {
            if (this.mFArr[i] < 0.0) {
                this.mFArr[i] = this.mFArr[i] + 3.141592653589793;
            }
            ++i;
        }
        return this;
    }

    public double getRealValue(int i, int j, int k) {
        return this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k];
    }

    public double getImagValue(int i, int j, int k) {
        return this.mFArr[i * this.mHeight * this.mWidthStep + j * this.mWidthStep + 2 * k + 1];
    }

    public int getMDepth() {
        return this.mDepth;
    }

    public int getMHeight() {
        return this.mHeight;
    }

    public int getMWidth() {
        return this.mWidth;
    }

    public static FloatArrayGeneric getFloatArrayOfImagePlus(ImagePlus imp) {
        int width = imp.getWidth();
        int height = imp.getHeight();
        int depth = imp.getImageStackSize();
        if (depth == 1) {
            return new FloatArray2D(imp.getProcessor().getFloatArray());
        }
        ImageStack stack = imp.getImageStack();
        float[][][] fArr = new float[depth][height][width];
        int k = 1;
        while (k <= stack.getHeight()) {
            fArr[k - 1] = stack.getProcessor(k).getFloatArray();
            ++k;
        }
        return new FloatArray3D(fArr);
    }

    public abstract void ifftshift();

    public FloatArrayGeneric duplicate() {
        if (this.mDepth == 1) {
            return new FloatArray2D((FloatArray2D)this);
        }
        return new FloatArray3D((FloatArray3D)this);
    }

    public FloatArrayGeneric zerosOfSameSize() {
        if (this.mDepth == 1) {
            return new FloatArray2D(this.mHeight, this.mWidth);
        }
        return new FloatArray3D(this.mDepth, this.mHeight, this.mWidth);
    }

    public double[] copy() {
        double[] fArr = new double[this.mFArr.length];
        int i = 0;
        while (i < this.mFArr.length) {
            fArr[i] = this.mFArr[i];
            ++i;
        }
        return fArr;
    }

    public abstract void ifft();

    public abstract void fft();

    public abstract ImagePlus getImagePlusReal(String var1);

    public abstract ImagePlus getImagePlusReal(String var1, int var2, int var3, int var4);

    public abstract FloatArrayGeneric downsampling(int var1);

    public void downsampling() {
        this.downsampling(1);
    }

    public abstract void upsampling(int var1);

    public void upsampling() {
        this.upsampling(1);
    }

    public String toString() {
        return this.mFArr.toString();
    }

    public double getMeanValue() {
        double average = 0.0;
        int i = 0;
        while (i < this.mFArr.length) {
            average += this.mFArr[i];
            i += 2;
        }
        return average /= (double)(this.mFArr.length / 2);
    }

    public double getMedianReal() {
        double[] fArrCopy = new double[this.mFArr.length / 2];
        int i = 0;
        while (i < this.mFArr.length) {
            fArrCopy[i / 2] = this.mFArr[i];
            i += 2;
        }
        Arrays.sort(fArrCopy);
        return (fArrCopy[fArrCopy.length / 2] + fArrCopy[fArrCopy.length / 2 + 1]) / 2.0;
    }

    public double getThreeQuartile() {
        double[] fArrCopy = new double[this.mFArr.length / 2];
        int i = 0;
        while (i < this.mFArr.length) {
            fArrCopy[i / 2] = this.mFArr[i];
            i += 2;
        }
        Arrays.sort(fArrCopy);
        return (fArrCopy[3 * fArrCopy.length / 4] + fArrCopy[3 * fArrCopy.length / 4 + 1]) / 2.0;
    }

    public double getStandardDeviation() {
        double meanValue = this.getMeanValue();
        double dev = 0.0;
        int i = 0;
        while (i < this.mFArr.length) {
            dev += Math.pow(this.mFArr[i] - meanValue, 2.0);
            i += 2;
        }
        return Math.sqrt(dev /= (double)(this.mHeight * this.mWidth * this.mDepth - 1));
    }

    public double getNorm() {
        double norm = 0.0;
        int i = 0;
        while (i < this.mFArr.length) {
            norm += this.mFArr[i] * this.mFArr[i];
            i += 2;
        }
        norm = Math.sqrt(norm);
        return norm /= (double)(this.mHeight * this.mWidth * this.mDepth);
    }

    public abstract void downsamplingAverage();

    public abstract void expand();

    public FloatArrayGeneric setMax(FloatArrayGeneric f2) {
        this.checkSize(f2);
        double[] fArr2 = f2.getArray();
        int i = 0;
        while (i < this.mFArr.length) {
            if (fArr2[i] > this.mFArr[i]) {
                this.mFArr[i] = fArr2[i];
            }
            ++i;
        }
        return this;
    }

    public abstract void extendSizeToPowerOf2();

    public double lInftyNorm() {
        double norm = 0.0;
        double abs = 0.0;
        int i = 0;
        while (i < this.mFArr.length) {
            abs = Math.sqrt(this.mFArr[i] * this.mFArr[i] + this.mFArr[i + 1] * this.mFArr[i + 1]);
            norm = abs > norm ? abs : norm;
            i += 2;
        }
        return norm;
    }

    public abstract void symmetricExtension();

    public abstract void revertSymmetricExtension();

    public abstract FloatArrayGeneric localStd(int var1);

    public double getMax() {
        double max = Double.MIN_VALUE;
        int i = 0;
        while (i < this.mFArr.length) {
            double val = this.mFArr[i];
            if (val > max) {
                max = val;
            }
            i += 2;
        }
        return max;
    }

    public double getMin() {
        double min = Double.MAX_VALUE;
        int i = 0;
        while (i < this.mFArr.length) {
            double val = this.mFArr[i];
            if (val < min) {
                min = val;
            }
            i += 2;
        }
        return min;
    }

    public FloatArrayGeneric exp() {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = Math.exp(this.mFArr[i]);
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric softThreshold(double threshold) {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = Math.signum(this.mFArr[i]) * Math.max(Math.abs(this.mFArr[i]) - threshold, 0.0);
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric hardThreshold(double threshold) {
        int i = 0;
        while (i < this.mFArr.length) {
            if (Math.abs(this.mFArr[i]) < threshold) {
                this.mFArr[i] = 0.0;
            }
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric softThreshold(FloatArrayGeneric threshold) {
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = Math.signum(this.mFArr[i]) * Math.max(Math.abs(this.mFArr[i]) - threshold.getArray()[i], 0.0);
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric hardThreshold(FloatArrayGeneric threshold) {
        int i = 0;
        while (i < this.mFArr.length) {
            if (Math.abs(this.mFArr[i]) < threshold.getArray()[i]) {
                this.mFArr[i] = 0.0;
            }
            i += 2;
        }
        return this;
    }

    public FloatArrayGeneric scaleTo(double newMin, double newMax) {
        double max = this.getMax();
        double min = this.getMin();
        if (min == max) {
            return this;
        }
        double a = (- newMax - newMin) / (min - max);
        double b = (- min * newMax - max * newMin) / (max - min);
        int i = 0;
        while (i < this.mFArr.length) {
            this.mFArr[i] = a * this.mFArr[i] + b;
            i += 2;
        }
        return this;
    }
}

