/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 *  ij.process.ByteProcessor
 *  ij.process.ImageProcessor
 */
package imageware;

import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import imageware.ByteAccess;
import imageware.Pointwise;
import java.awt.Image;
import java.util.Random;

public class BytePointwise
extends ByteAccess
implements Pointwise {
    protected BytePointwise(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected BytePointwise(Image image, int n) {
        super(image, n);
    }

    protected BytePointwise(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected BytePointwise(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected BytePointwise(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected BytePointwise(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected BytePointwise(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected BytePointwise(short[] arrs, int n) {
        super(arrs, n);
    }

    protected BytePointwise(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected BytePointwise(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected BytePointwise(float[] arrf, int n) {
        super(arrf, n);
    }

    protected BytePointwise(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected BytePointwise(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected BytePointwise(double[] arrd, int n) {
        super(arrd, n);
    }

    protected BytePointwise(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected BytePointwise(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public void fillConstant(double d) {
        byte by = (byte)d;
        byte[] arrby = null;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = by;
            }
        }
    }

    public void fillRamp() {
        int n = 0;
        byte[] arrby = null;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)(n + j);
            }
            n += this.nxy;
        }
    }

    public void fillGaussianNoise(double d) {
        Random random = new Random();
        byte[] arrby = null;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)(random.nextGaussian() * d);
            }
        }
    }

    public void fillUniformNoise(double d) {
        Random random = new Random();
        byte[] arrby = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)((random.nextDouble() - 0.5) * d);
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
                ((byte[])this.data[n2])[n3] = (byte)(random.nextDouble() * d);
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                ((byte[])this.data[n2])[n3] = (byte)((- random.nextDouble()) * d);
                ++n;
            }
        }
    }

    public ImageStack buildImageStack() {
        ImageStack imageStack = new ImageStack(this.nx, this.ny);
        for (int i = 0; i < this.nz; ++i) {
            ByteProcessor byteProcessor = new ByteProcessor(this.nx, this.ny);
            byte[] arrby = (byte[])byteProcessor.getPixels();
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = ((byte[])this.data[i])[j];
            }
            imageStack.addSlice("" + i, (ImageProcessor)byteProcessor);
        }
        return imageStack;
    }

    public void invert() {
        byte[] arrby;
        double d = -1.7976931348623157E308;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrby[j] & 255) <= d) continue;
                d = arrby[j] & 255;
            }
        }
        double d2 = d;
        for (int j = 0; j < this.nz; ++j) {
            arrby = (byte[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrby[k] = (byte)(d - (double)(arrby[k] & 255));
            }
        }
    }

    public void negate() {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)(- (double)(arrby[j] & 255));
            }
        }
    }

    public void clip() {
        this.clip(0.0, 255.0);
    }

    public void clip(double d, double d2) {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            byte by = (byte)d;
            byte by2 = (byte)d2;
            for (int j = 0; j < this.nxy; ++j) {
                byte by3 = (byte)(arrby[j] & 255);
                if (by3 < by) {
                    arrby[j] = by;
                }
                if (by3 <= by2) continue;
                arrby[j] = by2;
            }
        }
    }

    public void rescale() {
        byte[] arrby;
        double d;
        double d2 = -1.7976931348623157E308;
        double d3 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrby[j] & 255) > d2) {
                    d2 = arrby[j] & 255;
                }
                if ((double)(arrby[j] & 255) >= d3) continue;
                d3 = arrby[j] & 255;
            }
        }
        if (d3 - d2 == 0.0) {
            d = 1.0;
            d3 = 128.0;
        } else {
            d = 255.0 / (d2 - d3);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrby = (byte[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrby[k] = (byte)(d * ((double)(arrby[k] & 255) - d3));
            }
        }
    }

    public void rescale(double d, double d2) {
        byte[] arrby;
        double d3;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrby[j] & 255) > d4) {
                    d4 = arrby[j] & 255;
                }
                if ((double)(arrby[j] & 255) >= d5) continue;
                d5 = arrby[j] & 255;
            }
        }
        if (d5 - d4 == 0.0) {
            d3 = 1.0;
            d5 = (d2 - d) / 2.0;
        } else {
            d3 = (d2 - d) / (d4 - d5);
        }
        for (int j = 0; j < this.nz; ++j) {
            arrby = (byte[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrby[k] = (byte)(d3 * ((double)(arrby[k] & 255) - d5) + d);
            }
        }
    }

    public void rescaleCenter(double d, double d2) {
        byte[] arrby;
        double d3;
        double d4 = -1.7976931348623157E308;
        double d5 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrby[j] & 255) > d4) {
                    d4 = arrby[j] & 255;
                }
                if ((double)(arrby[j] & 255) >= d5) continue;
                d5 = arrby[j] & 255;
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
            arrby = (byte[])this.data[j];
            for (int k = 0; k < this.nxy; ++k) {
                arrby[k] = (byte)(d3 * ((double)(arrby[k] & 255) - d5) + d6);
            }
        }
    }

    public void abs() {
    }

    public void log() {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)Math.log(arrby[j]);
            }
        }
    }

    public void exp() {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)Math.exp(arrby[j]);
            }
        }
    }

    public void sqrt() {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)Math.sqrt(arrby[j]);
            }
        }
    }

    public void sqr() {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                byte[] arrby2 = arrby;
                int n = j;
                arrby2[n] = (byte)(arrby2[n] * arrby[j]);
            }
        }
    }

    public void pow(double d) {
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (byte)Math.pow(arrby[j], d);
            }
        }
    }

    public void add(double d) {
        byte by = (byte)d;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                byte[] arrby2 = arrby;
                int n2 = n++;
                arrby2[n2] = (byte)(arrby2[n2] + by);
            }
        }
    }

    public void multiply(double d) {
        byte by = (byte)d;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                byte[] arrby2 = arrby;
                int n2 = n++;
                arrby2[n2] = (byte)(arrby2[n2] * by);
            }
        }
    }

    public void subtract(double d) {
        byte by = (byte)d;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                byte[] arrby2 = arrby;
                int n2 = n++;
                arrby2[n2] = (byte)(arrby2[n2] - by);
            }
        }
    }

    public void divide(double d) {
        if (d == 0.0) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to divide because the constant is 0.\n-------------------------------------------------------\n");
        }
        byte by = (byte)d;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                byte[] arrby2 = arrby;
                int n2 = n++;
                arrby2[n2] = (byte)(arrby2[n2] / by);
            }
        }
    }

    public void threshold(double d) {
        this.threshold(d, 0.0, 255.0);
    }

    public void threshold(double d, double d2, double d3) {
        byte by = (byte)d2;
        byte by2 = (byte)d3;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                arrby[j] = (double)(arrby[j] & 255) > d ? by2 : by;
            }
        }
    }

    public void thresholdSoft(double d) {
        byte by = 0;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d2 = arrby[j] & 255;
                arrby[j] = d2 <= - d ? (byte)(d2 + d) : (d2 > d ? (byte)(d2 - d) : by);
            }
        }
    }

    public void thresholdHard(double d) {
        byte by = 0;
        for (int i = 0; i < this.nz; ++i) {
            byte[] arrby = (byte[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                double d2 = arrby[j] & 255;
                if (d2 <= - d || d2 >= d) continue;
                arrby[j] = by;
            }
        }
    }

    public void addGaussianNoise(double d) {
        Random random = new Random();
        byte[] arrby = null;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                byte[] arrby2 = arrby;
                int n2 = n++;
                arrby2[n2] = (byte)(arrby2[n2] + (byte)(random.nextGaussian() * d));
            }
        }
    }

    public void addUniformNoise(double d) {
        Random random = new Random();
        byte[] arrby = null;
        d *= 2.0;
        for (int i = 0; i < this.nz; ++i) {
            arrby = (byte[])this.data[i];
            int n = 0;
            while (n < this.nxy) {
                byte[] arrby2 = arrby;
                int n2 = n++;
                arrby2[n2] = (byte)(arrby2[n2] + (byte)((random.nextDouble() - 0.5) * d));
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
                byte[] arrby = (byte[])this.data[n2];
                int n4 = n3;
                arrby[n4] = (byte)(arrby[n4] + (byte)(random.nextDouble() * d));
                ++n;
            }
        }
        if (d4 > 0.0) {
            d5 = (double)(this.nxy * this.nz) / d4;
            n = 0;
            while ((double)n < d5) {
                n3 = (int)(random.nextDouble() * (double)this.nxy);
                n2 = (int)(random.nextDouble() * (double)this.nz);
                byte[] arrby = (byte[])this.data[n2];
                int n5 = n3;
                arrby[n5] = (byte)(arrby[n5] - (byte)(random.nextDouble() * d));
                ++n;
            }
        }
    }
}

