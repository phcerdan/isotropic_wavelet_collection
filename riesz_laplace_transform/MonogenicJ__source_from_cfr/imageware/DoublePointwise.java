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
import imageware.DoubleAccess;
import imageware.Pointwise;
import java.awt.Image;
import java.util.Random;

public class DoublePointwise
extends DoubleAccess
implements Pointwise {
    protected DoublePointwise(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected DoublePointwise(Image image, int n) {
        super(image, n);
    }

    protected DoublePointwise(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected DoublePointwise(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected DoublePointwise(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected DoublePointwise(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected DoublePointwise(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected DoublePointwise(short[] arrs, int n) {
        super(arrs, n);
    }

    protected DoublePointwise(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected DoublePointwise(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected DoublePointwise(float[] arrf, int n) {
        super(arrf, n);
    }

    protected DoublePointwise(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected DoublePointwise(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected DoublePointwise(double[] arrd, int n) {
        super(arrd, n);
    }

    protected DoublePointwise(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected DoublePointwise(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public void fillConstant(double d) {
        double d2 = d;
        double[] arrd = null;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = d2;
            }
        }
    }

    public void fillRamp() {
        int n = 0;
        double[] arrd = null;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = n + j;
            }
            n += this.nxy;
        }
    }

    public void fillGaussianNoise(double d) {
        Random random = new Random();
        double[] arrd = null;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = random.nextGaussian() * d;
            }
        }
    }

    public void fillUniformNoise(double d) {
        Random random = new Random();
        double[] arrd = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = (random.nextDouble() - 0.5) * d;
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
                ((double[])this.data[n2])[n3] = random.nextDouble() * d;
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                ((double[])this.data[n2])[n3] = (- random.nextDouble()) * d;
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
                arrf[j] = (float)((double[])this.data[i])[j];
            }
            imageStack.addSlice("" + i, (ImageProcessor)floatProcessor);
        }
        return imageStack;
    }

    public void invert() {
        double[] arrd;
        double d = -1.7976931348623157E308;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if (arrd[j] <= d) continue;
                d = arrd[j];
            }
        }
        double d2 = d;
        for (int j = 0; j < this.nz; ++j) {
            arrd = (double[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrd[k] = d - arrd[k];
            }
        }
    }

    public void negate() {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = - arrd[j];
            }
        }
    }

    public void clip() {
        this.clip(0.0, 255.0);
    }

    public void clip(double d, double d2) {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            double d3 = d;
            double d4 = d2;
            for (int j = 0; j < this.nxy; ++j) {
                double d5 = arrd[j];
                if (d5 < d3) {
                    arrd[j] = d3;
                }
                if (d5 <= d4) continue;
                arrd[j] = d4;
            }
        }
    }

    public void rescale() {
        double[] arrd;
        double d;
        double d2 = -1.7976931348623157E308;
        double d3 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if (arrd[j] > d2) {
                    d2 = arrd[j];
                }
                if (arrd[j] >= d3) continue;
                d3 = arrd[j];
            }
        }
        if (d3 - d2 == 0.0) {
            d = 1.0;
            d3 = 128.0;
        } else {
            d = 255.0 / (d2 - d3);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrd = (double[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrd[k] = d * (arrd[k] - d3);
            }
        }
    }

    public void rescale(double d, double d2) {
        double d3;
        double[] arrd;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if (arrd[j] > d4) {
                    d4 = arrd[j];
                }
                if (arrd[j] >= d5) continue;
                d5 = arrd[j];
            }
        }
        if (d5 - d4 == 0.0) {
            d3 = 1.0;
            d5 = (d2 - d) / 2.0;
        } else {
            d3 = (d2 - d) / (d4 - d5);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrd = (double[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrd[k] = d3 * (arrd[k] - d5) + d;
            }
        }
    }

    public void rescaleCenter(double d, double d2) {
        double d3;
        double[] arrd;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if (arrd[j] > d4) {
                    d4 = arrd[j];
                }
                if (arrd[j] >= d5) continue;
                d5 = arrd[j];
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
            arrd = (double[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrd[k] = d3 * (arrd[k] - d5) + d6;
            }
        }
    }

    public void abs() {
        double d = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if (arrd[j] >= d) continue;
                arrd[j] = - arrd[j];
            }
        }
    }

    public void log() {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = Math.log(arrd[j]);
            }
        }
    }

    public void exp() {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = Math.exp(arrd[j]);
            }
        }
    }

    public void sqrt() {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = Math.sqrt(arrd[j]);
            }
        }
    }

    public void sqr() {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double[] arrd2 = arrd;
                int n = j;
                arrd2[n] = arrd2[n] * arrd[j];
            }
        }
    }

    public void pow(double d) {
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = Math.pow(arrd[j], d);
            }
        }
    }

    public void add(double d) {
        double d2 = d;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                double[] arrd2 = arrd;
                int n2 = n++;
                arrd2[n2] = arrd2[n2] + d2;
            }
        }
    }

    public void multiply(double d) {
        double d2 = d;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                double[] arrd2 = arrd;
                int n2 = n++;
                arrd2[n2] = arrd2[n2] * d2;
            }
        }
    }

    public void subtract(double d) {
        double d2 = d;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                double[] arrd2 = arrd;
                int n2 = n++;
                arrd2[n2] = arrd2[n2] - d2;
            }
        }
    }

    public void divide(double d) {
        if (d == 0.0) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to divide because the constant is 0.\n-------------------------------------------------------\n");
        }
        double d2 = d;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                double[] arrd2 = arrd;
                int n2 = n++;
                arrd2[n2] = arrd2[n2] / d2;
            }
        }
    }

    public void threshold(double d) {
        this.threshold(d, 0.0, 255.0);
    }

    public void threshold(double d, double d2, double d3) {
        double d4 = d2;
        double d5 = d3;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrd[j] = arrd[j] > d ? d5 : d4;
            }
        }
    }

    public void thresholdSoft(double d) {
        double d2 = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d3 = arrd[j];
                arrd[j] = d3 <= - d ? d3 + d : (d3 > d ? d3 - d : d2);
            }
        }
    }

    public void thresholdHard(double d) {
        double d2 = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            double[] arrd = (double[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d3 = arrd[j];
                if (d3 <= - d || d3 >= d) continue;
                arrd[j] = d2;
            }
        }
    }

    public void addGaussianNoise(double d) {
        Random random = new Random();
        double[] arrd = null;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                double[] arrd2 = arrd;
                int n2 = n++;
                arrd2[n2] = arrd2[n2] + random.nextGaussian() * d;
            }
        }
    }

    public void addUniformNoise(double d) {
        Random random = new Random();
        double[] arrd = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrd = (double[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                double[] arrd2 = arrd;
                int n2 = n++;
                arrd2[n2] = arrd2[n2] + (random.nextDouble() - 0.5) * d;
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
                double[] arrd = (double[])this.data[n2];
                int n4 = n3;
                arrd[n4] = arrd[n4] + random.nextDouble() * d;
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                double[] arrd = (double[])this.data[n2];
                int n5 = n3;
                arrd[n5] = arrd[n5] - random.nextDouble() * d;
                ++n;
            }
        }
    }
}

