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
import imageware.FloatSet;
import imageware.ImageWare;
import imageware.ShortProcess;
import java.awt.Image;
import java.io.PrintStream;

public class ShortSet
extends ShortProcess
implements ImageWare {
    protected ShortSet(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected ShortSet(Image image, int n) {
        super(image, n);
    }

    protected ShortSet(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected ShortSet(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected ShortSet(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected ShortSet(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortSet(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortSet(short[] arrs, int n) {
        super(arrs, n);
    }

    protected ShortSet(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortSet(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortSet(float[] arrf, int n) {
        super(arrf, n);
    }

    protected ShortSet(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortSet(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortSet(double[] arrd, int n) {
        super(arrd, n);
    }

    protected ShortSet(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected ShortSet(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public ImageWare duplicate() {
        ShortSet shortSet = new ShortSet(this.nx, this.ny, this.nz);
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])shortSet.data[i];
            System.arraycopy(this.data[i], 0, arrs, 0, this.nxy);
        }
        return shortSet;
    }

    public ImageWare replicate() {
        return new ShortSet(this.nx, this.ny, this.nz);
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
            short[] arrs = (short[])((ShortSet)imageWare).data[i];
            System.arraycopy(arrs, 0, this.data[i], 0, this.nxy);
        }
    }

    public ImageWare convert(int n) {
        if (n == 2) {
            return this.duplicate();
        }
        ImageWare imageWare = null;
        switch (n) {
            case 1: {
                imageWare = new ByteSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = (short[])this.data[i];
                    byte[] arrby = (byte[])imageWare.data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrby[j] = (byte)(arrs[j] & 65535);
                    }
                }
                break;
            }
            case 2: {
                imageWare = new ShortSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = (short[])this.data[i];
                    short[] arrs2 = (short[])((ShortSet)imageWare).data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrs2[j] = (short)(arrs[j] & 65535);
                    }
                }
                break;
            }
            case 3: {
                imageWare = new FloatSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = (short[])this.data[i];
                    float[] arrf = (float[])((FloatSet)imageWare).data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrf[j] = arrs[j] & 65535;
                    }
                }
                break;
            }
            case 4: {
                imageWare = new DoubleSet(this.nx, this.ny, this.nz);
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = (short[])this.data[i];
                    double[] arrd = (double[])((DoubleSet)imageWare).data[i];
                    for (int j = 0; j < this.nxy; ++j) {
                        arrd[j] = arrs[j] & 65535;
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
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) >= d) continue;
                d = arrs[j] & 65535;
            }
        }
        return d;
    }

    public double getMaximum() {
        double d = -1.7976931348623157E308;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) <= d) continue;
                d = arrs[j] & 65535;
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
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                d2 = arrs[j] & 65535;
                d += d2 > 0.0 ? d2 : - d2;
            }
        }
        return d;
    }

    public double getNorm2() {
        double d = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                d += (double)((arrs[j] & 65535) * (arrs[j] & 65535));
            }
        }
        return d;
    }

    public double getTotal() {
        double d = 0.0;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                d += (double)(arrs[j] & 65535);
            }
        }
        return d;
    }

    public double[] getMinMax() {
        double d = -1.7976931348623157E308;
        double d2 = Double.MAX_VALUE;
        for (int i = 0; i < this.nz; ++i) {
            short[] arrs = (short[])this.data[i];
            for (int j = 0; j < this.nxy; ++j) {
                if ((double)(arrs[j] & 65535) > d) {
                    d = arrs[j] & 65535;
                }
                if ((double)(arrs[j] & 65535) >= d2) continue;
                d2 = arrs[j] & 65535;
            }
        }
        double[] arrd = new double[]{d2, d};
        return arrd;
    }
}

