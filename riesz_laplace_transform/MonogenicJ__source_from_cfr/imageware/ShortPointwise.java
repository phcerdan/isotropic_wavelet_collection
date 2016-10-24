/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 *  ij.process.ImageProcessor
 *  ij.process.ShortProcessor
 */
package imageware;

import ij.ImageStack;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import imageware.Pointwise;
import imageware.ShortAccess;
import java.awt.Image;
import java.util.Random;

public class ShortPointwise
extends ShortAccess
implements Pointwise {
    protected ShortPointwise(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected ShortPointwise(Image image, int n) {
        super(image, n);
    }

    protected ShortPointwise(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected ShortPointwise(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected ShortPointwise(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected ShortPointwise(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortPointwise(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortPointwise(short[] arrs, int n) {
        super(arrs, n);
    }

    protected ShortPointwise(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortPointwise(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortPointwise(float[] arrf, int n) {
        super(arrf, n);
    }

    protected ShortPointwise(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortPointwise(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortPointwise(double[] arrd, int n) {
        super(arrd, n);
    }

    protected ShortPointwise(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected ShortPointwise(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public void fillConstant(double d) {
        short s = (short)d;
        short[] arrs = null;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = s;
            }
        }
    }

    public void fillRamp() {
        int n = 0;
        short[] arrs = null;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)(n + j);
            }
            n += this.nxy;
        }
    }

    public void fillGaussianNoise(double d) {
        Random random = new Random();
        short[] arrs = null;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)(random.nextGaussian() * d);
            }
        }
    }

    public void fillUniformNoise(double d) {
        Random random = new Random();
        short[] arrs = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)((random.nextDouble() - 0.5) * d);
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
                ((short[])this.data[n2])[n3] = (short)(random.nextDouble() * d);
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                ((short[])this.data[n2])[n3] = (short)((- random.nextDouble()) * d);
                ++n;
            }
        }
    }

    public ImageStack buildImageStack() {
        ImageStack imageStack = new ImageStack(this.nx, this.ny);
        for (int i = 0; i < this.nz; ++i) {
            ShortProcessor shortProcessor = new ShortProcessor(this.nx, this.ny);
            short[] arrs = (short[])shortProcessor.getPixels();
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = ((short[])this.data[i])[j];
            }
            imageStack.addSlice("" + i, (ImageProcessor)shortProcessor);
        }
        return imageStack;
    }

    public void invert() {
        short[] arrs;
        double d = -1.7976931348623157E308;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) <= d) continue;
                d = arrs[j] & 65535;
            }
        }
        double d2 = d;
        for (int j = 0; j < this.nz; ++j) {
            arrs = (short[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrs[k] = (short)(d - (double)(arrs[k] & 65535));
            }
        }
    }

    public void negate() {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)(- (double)(arrs[j] & 65535));
            }
        }
    }

    public void clip() {
        this.clip(0.0, 255.0);
    }

    public void clip(double d, double d2) {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            short s = (short)d;
            short s2 = (short)d2;
            for (int j = 0; j < this.nxy; ++j) {
                short s3 = (short)(arrs[j] & 65535);
                if (s3 < s) {
                    arrs[j] = s;
                }
                if (s3 <= s2) continue;
                arrs[j] = s2;
            }
        }
    }

    public void rescale() {
        short[] arrs;
        double d;
        double d2 = -1.7976931348623157E308;
        double d3 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) > d2) {
                    d2 = arrs[j] & 65535;
                }
                if ((double)(arrs[j] & 65535) >= d3) continue;
                d3 = arrs[j] & 65535;
            }
        }
        if (d3 - d2 == 0.0) {
            d = 1.0;
            d3 = 128.0;
        } else {
            d = 255.0 / (d2 - d3);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrs = (short[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrs[k] = (short)(d * ((double)(arrs[k] & 65535) - d3));
            }
        }
    }

    public void rescale(double d, double d2) {
        short[] arrs;
        double d3;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) > d4) {
                    d4 = arrs[j] & 65535;
                }
                if ((double)(arrs[j] & 65535) >= d5) continue;
                d5 = arrs[j] & 65535;
            }
        }
        if (d5 - d4 == 0.0) {
            d3 = 1.0;
            d5 = (d2 - d) / 2.0;
        } else {
            d3 = (d2 - d) / (d4 - d5);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrs = (short[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrs[k] = (short)(d3 * ((double)(arrs[k] & 65535) - d5) + d);
            }
        }
    }

    public void rescaleCenter(double d, double d2) {
        short[] arrs;
        double d3;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) > d4) {
                    d4 = arrs[j] & 65535;
                }
                if ((double)(arrs[j] & 65535) >= d5) continue;
                d5 = arrs[j] & 65535;
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
            arrs = (short[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrs[k] = (short)(d3 * ((double)(arrs[k] & 65535) - d5) + d6);
            }
        }
    }

    public void abs() {
    }

    public void log() {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)Math.log(arrs[j]);
            }
        }
    }

    public void exp() {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)Math.exp(arrs[j]);
            }
        }
    }

    public void sqrt() {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)Math.sqrt(arrs[j]);
            }
        }
    }

    public void sqr() {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                short[] arrs2 = arrs;
                int n = j;
                arrs2[n] = (short)(arrs2[n] * arrs[j]);
            }
        }
    }

    public void pow(double d) {
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (short)Math.pow(arrs[j], d);
            }
        }
    }

    public void add(double d) {
        short s = (short)d;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                short[] arrs2 = arrs;
                int n2 = n++;
                arrs2[n2] = (short)(arrs2[n2] + s);
            }
        }
    }

    public void multiply(double d) {
        short s = (short)d;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                short[] arrs2 = arrs;
                int n2 = n++;
                arrs2[n2] = (short)(arrs2[n2] * s);
            }
        }
    }

    public void subtract(double d) {
        short s = (short)d;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                short[] arrs2 = arrs;
                int n2 = n++;
                arrs2[n2] = (short)(arrs2[n2] - s);
            }
        }
    }

    public void divide(double d) {
        if (d == 0.0) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to divide because the constant is 0.\n-------------------------------------------------------\n");
        }
        short s = (short)d;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                short[] arrs2 = arrs;
                int n2 = n++;
                arrs2[n2] = (short)(arrs2[n2] / s);
            }
        }
    }

    public void threshold(double d) {
        this.threshold(d, 0.0, 255.0);
    }

    public void threshold(double d, double d2, double d3) {
        short s = (short)d2;
        short s2 = (short)d3;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrs[j] = (double)(arrs[j] & 65535) > d ? s2 : s;
            }
        }
    }

    public void thresholdSoft(double d) {
        short s = 0;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d2 = arrs[j] & 65535;
                arrs[j] = d2 <= - d ? (short)(d2 + d) : (d2 > d ? (short)(d2 - d) : s);
            }
        }
    }

    public void thresholdHard(double d) {
        short s = 0;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d2 = arrs[j] & 65535;
                if (d2 <= - d || d2 >= d) continue;
                arrs[j] = s;
            }
        }
    }

    public void addGaussianNoise(double d) {
        Random random = new Random();
        short[] arrs = null;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                short[] arrs2 = arrs;
                int n2 = n++;
                arrs2[n2] = (short)(arrs2[n2] + (short)(random.nextGaussian() * d));
            }
        }
    }

    public void addUniformNoise(double d) {
        Random random = new Random();
        short[] arrs = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrs = (short[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                short[] arrs2 = arrs;
                int n2 = n++;
                arrs2[n2] = (short)(arrs2[n2] + (short)((random.nextDouble() - 0.5) * d));
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
                short[] arrs = (short[])this.data[n2];
                int n4 = n3;
                arrs[n4] = (short)(arrs[n4] + (short)(random.nextDouble() * d));
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                short[] arrs = (short[])this.data[n2];
                int n5 = n3;
                arrs[n5] = (short)(arrs[n5] - (short)(random.nextDouble() * d));
                ++n;
            }
        }
    }
}

