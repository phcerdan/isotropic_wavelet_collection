/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package imageware;

import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import imageware.FloatAccess;
import imageware.Pointwise;
import java.awt.Image;
import java.util.Random;

public class FloatPointwise
extends FloatAccess
implements Pointwise {
    protected FloatPointwise(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected FloatPointwise(Image image, int n) {
        super(image, n);
    }

    protected FloatPointwise(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected FloatPointwise(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected FloatPointwise(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected FloatPointwise(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected FloatPointwise(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected FloatPointwise(short[] arrs, int n) {
        super(arrs, n);
    }

    protected FloatPointwise(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected FloatPointwise(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected FloatPointwise(float[] arrf, int n) {
        super(arrf, n);
    }

    protected FloatPointwise(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected FloatPointwise(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected FloatPointwise(double[] arrd, int n) {
        super(arrd, n);
    }

    protected FloatPointwise(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected FloatPointwise(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public void fillConstant(double d) {
        float f = (float)d;
        float[] arrf = null;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = f;
            }
        }
    }

    public void fillRamp() {
        int n = 0;
        float[] arrf = null;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = n + j;
            }
            n += this.nxy;
        }
    }

    public void fillGaussianNoise(double d) {
        Random random = new Random();
        float[] arrf = null;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)(random.nextGaussian() * d);
            }
        }
    }

    public void fillUniformNoise(double d) {
        Random random = new Random();
        float[] arrf = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)((random.nextDouble() - 0.5) * d);
            }
        }
    }

    public void fillSaltPepper(double d, double d2, double d3, double d4) {
        double d5;
        int n;
        int n2;
        int n3;
        Random random = new Random();
        Object var10_6 = null;
        if (d3 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d3;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                ((float[])this.data[n2])[n3] = (float)(random.nextDouble() * d);
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                ((float[])this.data[n2])[n3] = (float)((- random.nextDouble()) * d);
                ++n;
            }
        }
    }

    public ImageStack buildImageStack() {
        ImageStack imageStack = new ImageStack(this.nx, this.ny);
        for (int i = 0; i < this.nz; ++i) {
            FloatProcessor floatProcessor = new FloatProcessor(this.nx, this.ny);
            float[] arrf = (float[])floatProcessor.getPixels();
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = ((float[])this.data[i])[j];
            }
            imageStack.addSlice("" + i, (ImageProcessor)floatProcessor);
        }
        return imageStack;
    }

    public void invert() {
        float[] arrf;
        double d = -1.7976931348623157E308;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] <= d) continue;
                d = arrf[j];
            }
        }
        double d2 = d;
        for (int j = 0; j < this.nz; ++j) {
            arrf = (float[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrf[k] = (float)(d - (double)arrf[k]);
            }
        }
    }

    public void negate() {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)(- (double)arrf[j]);
            }
        }
    }

    public void clip() {
        this.clip(0.0, 255.0);
    }

    public void clip(double d, double d2) {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            float f = (float)d;
            float f2 = (float)d2;
            for (int j = 0; j < this.nxy; ++j) {
                float f3 = arrf[j];
                if (f3 < f) {
                    arrf[j] = f;
                }
                if (f3 <= f2) continue;
                arrf[j] = f2;
            }
        }
    }

    public void rescale() {
        float[] arrf;
        double d;
        double d2 = -1.7976931348623157E308;
        double d3 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] > d2) {
                    d2 = arrf[j];
                }
                if ((double)arrf[j] >= d3) continue;
                d3 = arrf[j];
            }
        }
        if (d3 - d2 == 0.0) {
            d = 1.0;
            d3 = 128.0;
        } else {
            d = 255.0 / (d2 - d3);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrf = (float[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrf[k] = (float)(d * ((double)arrf[k] - d3));
            }
        }
    }

    public void rescale(double d, double d2) {
        float[] arrf;
        double d3;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] > d4) {
                    d4 = arrf[j];
                }
                if ((double)arrf[j] >= d5) continue;
                d5 = arrf[j];
            }
        }
        if (d5 - d4 == 0.0) {
            d3 = 1.0;
            d5 = (d2 - d) / 2.0;
        } else {
            d3 = (d2 - d) / (d4 - d5);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrf = (float[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrf[k] = (float)(d3 * ((double)arrf[k] - d5) + d);
            }
        }
    }

    public void rescaleCenter(double d, double d2) {
        float[] arrf;
        double d3;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] > d4) {
                    d4 = arrf[j];
                }
                if ((double)arrf[j] >= d5) continue;
                d5 = arrf[j];
            }
        }
        double d6 = (d2 + d) / 2.0;
        if (d5 - d4 == 0.0) {
            d3 = 1.0;
            d5 = (d2 - d) / 2.0;
        } else {
            d3 = Math.abs(d4) > Math.abs(d5) ? (d2 - d6) / Math.abs(d4) : (d6 - d) / Math.abs(d5);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrf = (float[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrf[k] = (float)(d3 * ((double)arrf[k] - d5) + d6);
            }
        }
    }

    public void abs() {
        float f = 0.0f;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if (arrf[j] >= f) continue;
                arrf[j] = - arrf[j];
            }
        }
    }

    public void log() {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)Math.log(arrf[j]);
            }
        }
    }

    public void exp() {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)Math.exp(arrf[j]);
            }
        }
    }

    public void sqrt() {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)Math.sqrt(arrf[j]);
            }
        }
    }

    public void sqr() {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                float[] arrf2 = arrf;
                int n = j;
                arrf2[n] = arrf2[n] * arrf[j];
            }
        }
    }

    public void pow(double d) {
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (float)Math.pow(arrf[j], d);
            }
        }
    }

    public void add(double d) {
        float f = (float)d;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                float[] arrf2 = arrf;
                int n2 = n++;
                arrf2[n2] = arrf2[n2] + f;
            }
        }
    }

    public void multiply(double d) {
        float f = (float)d;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                float[] arrf2 = arrf;
                int n2 = n++;
                arrf2[n2] = arrf2[n2] * f;
            }
        }
    }

    public void subtract(double d) {
        float f = (float)d;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                float[] arrf2 = arrf;
                int n2 = n++;
                arrf2[n2] = arrf2[n2] - f;
            }
        }
    }

    public void divide(double d) {
        if (d == 0.0) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to divide because the constant is 0.\n-------------------------------------------------------\n");
        }
        float f = (float)d;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                float[] arrf2 = arrf;
                int n2 = n++;
                arrf2[n2] = arrf2[n2] / f;
            }
        }
    }

    public void threshold(double d) {
        this.threshold(d, 0.0, 255.0);
    }

    public void threshold(double d, double d2, double d3) {
        float f = (float)d2;
        float f2 = (float)d3;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrf[j] = (double)arrf[j] > d ? f2 : f;
            }
        }
    }

    public void thresholdSoft(double d) {
        float f = 0.0f;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d2 = arrf[j];
                arrf[j] = d2 <= - d ? (float)(d2 + d) : (d2 > d ? (float)(d2 - d) : f);
            }
        }
    }

    public void thresholdHard(double d) {
        float f = 0.0f;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d2 = arrf[j];
                if (d2 <= - d || d2 >= d) continue;
                arrf[j] = f;
            }
        }
    }

    public void addGaussianNoise(double d) {
        Random random = new Random();
        float[] arrf = null;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                float[] arrf2 = arrf;
                int n2 = n++;
                arrf2[n2] = arrf2[n2] + (float)(random.nextGaussian() * d);
            }
        }
    }

    public void addUniformNoise(double d) {
        Random random = new Random();
        float[] arrf = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrf = (float[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                float[] arrf2 = arrf;
                int n2 = n++;
                arrf2[n2] = arrf2[n2] + (float)((random.nextDouble() - 0.5) * d);
            }
        }
    }

    public void addSaltPepper(double d, double d2, double d3, double d4) {
        double d5;
        int n;
        int n2;
        int n3;
        Random random = new Random();
        Object var10_6 = null;
        if (d3 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d3;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                float[] arrf = (float[])this.data[n2];
                int n4 = n3;
                arrf[n4] = arrf[n4] + (float)(random.nextDouble() * d);
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                float[] arrf = (float[])this.data[n2];
                int n5 = n3;
                arrf[n5] = arrf[n5] - (float)(random.nextDouble() * d);
                ++n;
            }
        }
    }
}

