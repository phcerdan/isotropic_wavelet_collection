/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 */
package imageware;

import ij.ImagePlus;
import ij.ImageStack;
import imageware.ByteSet;
import imageware.DoubleSet;
import imageware.FloatProcess;
import imageware.ImageWare;
import imageware.ShortSet;
import java.awt.Image;
import java.io.PrintStream;

public class FloatSet
extends FloatProcess
implements ImageWare {
    protected FloatSet(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected FloatSet(Image image, int n) {
        super(image, n);
    }

    protected FloatSet(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected FloatSet(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected FloatSet(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected FloatSet(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected FloatSet(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected FloatSet(short[] arrs, int n) {
        super(arrs, n);
    }

    protected FloatSet(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected FloatSet(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected FloatSet(float[] arrf, int n) {
        super(arrf, n);
    }

    protected FloatSet(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected FloatSet(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected FloatSet(double[] arrd, int n) {
        super(arrd, n);
    }

    protected FloatSet(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected FloatSet(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public ImageWare duplicate() {
        FloatSet floatSet = new FloatSet(this.nx, this.ny, this.nz);
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])floatSet.data[i];
            System.arraycopy(this.data[i], 0, arrf, 0, this.nxy);
        }
        return floatSet;
    }

    public ImageWare replicate() {
        return new FloatSet(this.nx, this.ny, this.nz);
    }

    public ImageWare replicate(int n) {
        switch (n) {
            case 1: {
                return new ByteSet(this.nx, this.ny, this.nz);
            }
            case 2: {
                return new ShortSet(this.nx, this.ny, this.nz);
            }
            case 3: {
                return new FloatSet(this.nx, this.ny, this.nz);
            }
            case 4: {
                return new DoubleSet(this.nx, this.ny, this.nz);
            }
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n + "].\n" + "-------------------------------------------------------\n");
    }

    public void copy(ImageWare imageWare) {
        if (this.nx != imageWare.getSizeX()) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to copy because it is not the same size (" + this.nx + " != " + imageWare.getSizeX() + ").\n" + "-------------------------------------------------------\n");
        }
        if (this.ny != imageWare.getSizeY()) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to copy because it is not the same size (" + this.ny + " != " + imageWare.getSizeY() + ").\n" + "-------------------------------------------------------\n");
        }
        if (this.nz != imageWare.getSizeZ()) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to copy because it is not the same size (" + this.nz + " != " + imageWare.getSizeZ() + ").\n" + "-------------------------------------------------------\n");
        }
        if (this.getType() != imageWare.getType()) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to copy because it is not the same type (" + this.getType() + " != " + imageWare.getType() + ").\n" + "-------------------------------------------------------\n");
        }
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])((FloatSet)imageWare).data[i];
            System.arraycopy(arrf, 0, this.data[i], 0, this.nxy);
        }
    }

    public ImageWare convert(int n) {
        if (n == 3) {
            return this.duplicate();
        }
        ImageWare imageWare = null;
        switch (n) {
            case 1: {
                imageWare = new ByteSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = (float[])this.data[i];
                    byte[] arrby = (byte[])imageWare.data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrby[j] = (byte)arrf[j];
                    }
                }
                break;
            }
            case 2: {
                imageWare = new ShortSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = (float[])this.data[i];
                    short[] arrs = (short[])((ShortSet)imageWare).data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrs[j] = (short)arrf[j];
                    }
                }
                break;
            }
            case 3: {
                imageWare = new FloatSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = (float[])this.data[i];
                    float[] arrf2 = (float[])((FloatSet)imageWare).data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrf2[j] = arrf[j];
                    }
                }
                break;
            }
            case 4: {
                imageWare = new DoubleSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = (float[])this.data[i];
                    double[] arrd = (double[])((DoubleSet)imageWare).data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrd[j] = arrf[j];
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n + "].\n" + "-------------------------------------------------------\n");
            }
        }
        return imageWare;
    }

    public void printInfo() {
        System.out.println("ImageWare object information");
        System.out.println("Dimension: " + this.getDimension());
        System.out.println("Size: [" + this.nx + ", " + this.ny + ", " + this.nz + "]");
        System.out.println("TotalSize: " + this.getTotalSize());
        System.out.println("Type: " + this.getTypeToString());
        System.out.println("Maximun: " + this.getMaximum());
        System.out.println("Minimun: " + this.getMinimum());
        System.out.println("Mean: " + this.getMean());
        System.out.println("Norm1: " + this.getNorm1());
        System.out.println("Norm2: " + this.getNorm2());
        System.out.println("Total: " + this.getTotal());
        System.out.println("");
    }

    public void show() {
        String string = this.getTypeToString();
        switch (this.getDimension()) {
            case 1: {
                string = string + " line";
                break;
            }
            case 2: {
                string = string + " image";
                break;
            }
            case 3: {
                string = string + " volume";
            }
        }
        ImagePlus imagePlus = new ImagePlus(string, this.buildImageStack());
        imagePlus.show();
    }

    public void show(String string) {
        ImagePlus imagePlus = new ImagePlus(string, this.buildImageStack());
        imagePlus.show();
    }

    public double getMinimum() {
        double d = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] >= d) continue;
                d = arrf[j];
            }
        }
        return d;
    }

    public double getMaximum() {
        double d = -1.7976931348623157E308;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] <= d) continue;
                d = arrf[j];
            }
        }
        return d;
    }

    public double getMean() {
        return this.getTotal() / (double)(this.nz * this.nxy);
    }

    public double getNorm1() {
        double d = 0.0;
        double d2 = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                d2 = arrf[j];
                d += d2 > 0.0 ? d2 : - d2;
            }
        }
        return d;
    }

    public double getNorm2() {
        double d = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                d += (double)(arrf[j] * arrf[j]);
            }
        }
        return d;
    }

    public double getTotal() {
        double d = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                d += (double)arrf[j];
            }
        }
        return d;
    }

    public double[] getMinMax() {
        double d = -1.7976931348623157E308;
        double d2 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            float[] arrf = (float[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)arrf[j] > d) {
                    d = arrf[j];
                }
                if ((double)arrf[j] >= d2) continue;
                d2 = arrf[j];
            }
        }
        double[] arrd = new double[]{d2, d};
        return arrd;
    }
}

