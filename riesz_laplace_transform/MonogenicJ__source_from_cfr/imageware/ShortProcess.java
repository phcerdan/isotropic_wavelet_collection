/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 */
package imageware;

import ij.ImageStack;
import imageware.ByteSet;
import imageware.Convolver;
import imageware.DoubleSet;
import imageware.FloatSet;
import imageware.ImageWare;
import imageware.Process;
import imageware.ShortPointwise;
import imageware.ShortSet;
import java.awt.Image;

public class ShortProcess
extends ShortPointwise
implements Process {
    protected ShortProcess(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected ShortProcess(Image image, int n) {
        super(image, n);
    }

    protected ShortProcess(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected ShortProcess(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected ShortProcess(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected ShortProcess(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortProcess(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortProcess(short[] arrs, int n) {
        super(arrs, n);
    }

    protected ShortProcess(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortProcess(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortProcess(float[] arrf, int n) {
        super(arrf, n);
    }

    protected ShortProcess(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortProcess(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortProcess(double[] arrd, int n) {
        super(arrd, n);
    }

    protected ShortProcess(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected ShortProcess(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public void smoothGaussian(double d) {
        this.smoothGaussian(d, d, d);
    }

    public void smoothGaussian(double d, double d2, double d3) {
        int n;
        double d4;
        double[] arrd;
        int n2;
        double d5;
        int n3 = 3;
        double d6 = n3;
        double d7 = 1.0 / (double)(this.nx + this.ny + this.nz);
        double[] arrd2 = new double[n3];
        if (this.nx > 1 && d > 0.0) {
            d5 = d * d;
            arrd2[1] = arrd2[2] = (d4 = 1.0 + d6 / d5 - Math.sqrt(d6 * d6 + 2.0 * d6 * d5) / d5);
            arrd2[0] = arrd2[2];
            arrd = new double[this.nx];
            for (n = 0; n < this.nz; ++n) {
                for (n2 = 0; n2 < this.ny; ++n2) {
                    this.getX(0, n2, n, arrd);
                    this.putX(0, n2, n, Convolver.convolveIIR(arrd, arrd2));
                }
            }
        }
        if (this.ny > 1 && d2 > 0.0) {
            d5 = d2 * d2;
            arrd2[1] = arrd2[2] = (d4 = 1.0 + d6 / d5 - Math.sqrt(d6 * d6 + 2.0 * d6 * d5) / d5);
            arrd2[0] = arrd2[2];
            arrd = new double[this.ny];
            for (n = 0; n < this.nx; ++n) {
                for (n2 = 0; n2 < this.nz; ++n2) {
                    this.getY(n, 0, n2, arrd);
                    this.putY(n, 0, n2, Convolver.convolveIIR(arrd, arrd2));
                }
            }
        }
        if (this.nz > 1 && d3 > 0.0) {
            d5 = d3 * d3;
            arrd2[1] = arrd2[2] = (d4 = 1.0 + d6 / d5 - Math.sqrt(d6 * d6 + 2.0 * d6 * d5) / d5);
            arrd2[0] = arrd2[2];
            arrd = new double[this.nz];
            for (n = 0; n < this.ny; ++n) {
                for (n2 = 0; n2 < this.nx; ++n2) {
                    this.getZ(n2, n, 0, arrd);
                    this.putZ(n2, n, 0, Convolver.convolveIIR(arrd, arrd2));
                }
            }
        }
    }

    public void max(ImageWare imageWare) {
        if (!this.isSameSize(imageWare)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to get the maximum because the two operands are not the same size.\n[" + this.nx + "," + this.ny + "," + "," + this.nz + "] != " + "[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        switch (imageWare.getType()) {
            case 1: {
                for (int i = 0; i < this.nz; ++i) {
                    byte[] arrby = ((ByteSet)imageWare).getSliceByte(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] >= arrby[j]) continue;
                        ((short[])this.data[i])[j] = arrby[j];
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = ((ShortSet)imageWare).getSliceShort(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] >= arrs[j]) continue;
                        ((short[])this.data[i])[j] = arrs[j];
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = ((FloatSet)imageWare).getSliceFloat(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] >= (short)arrf[j]) continue;
                        ((short[])this.data[i])[j] = (short)arrf[j];
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.nz; ++i) {
                    double[] arrd = ((DoubleSet)imageWare).getSliceDouble(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] >= (short)arrd[j]) continue;
                        ((short[])this.data[i])[j] = (short)arrd[j];
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + imageWare.getType() + "].\n" + "-------------------------------------------------------\n");
            }
        }
    }

    public void min(ImageWare imageWare) {
        if (!this.isSameSize(imageWare)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to get the minimum because the two operands are not the same size.\n[" + this.nx + "," + this.ny + "," + "," + this.nz + "] != " + "[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        switch (imageWare.getType()) {
            case 1: {
                for (int i = 0; i < this.nz; ++i) {
                    byte[] arrby = ((ByteSet)imageWare).getSliceByte(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] <= arrby[j]) continue;
                        ((short[])this.data[i])[j] = arrby[j];
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = ((ShortSet)imageWare).getSliceShort(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] <= arrs[j]) continue;
                        ((short[])this.data[i])[j] = arrs[j];
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = ((FloatSet)imageWare).getSliceFloat(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] <= (short)arrf[j]) continue;
                        ((short[])this.data[i])[j] = (short)arrf[j];
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.nz; ++i) {
                    double[] arrd = ((DoubleSet)imageWare).getSliceDouble(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        if (((short[])this.data[i])[j] <= (short)arrd[j]) continue;
                        ((short[])this.data[i])[j] = (short)arrd[j];
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + imageWare.getType() + "].\n" + "-------------------------------------------------------\n");
            }
        }
    }

    public void add(ImageWare imageWare) {
        if (!this.isSameSize(imageWare)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to add because the two operands are not the same size.\n[" + this.nx + "," + this.ny + "," + "," + this.nz + "] != " + "[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        switch (imageWare.getType()) {
            case 1: {
                for (int i = 0; i < this.nz; ++i) {
                    byte[] arrby = ((ByteSet)imageWare).getSliceByte(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] + arrby[j]);
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = ((ShortSet)imageWare).getSliceShort(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs2 = (short[])this.data[i];
                        int n = j;
                        arrs2[n] = (short)(arrs2[n] + arrs[j]);
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = ((FloatSet)imageWare).getSliceFloat(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] + (short)arrf[j]);
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.nz; ++i) {
                    double[] arrd = ((DoubleSet)imageWare).getSliceDouble(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] + (short)arrd[j]);
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + imageWare.getType() + "].\n" + "-------------------------------------------------------\n");
            }
        }
    }

    public void multiply(ImageWare imageWare) {
        if (!this.isSameSize(imageWare)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to multiply because the two operands are not the same size.\n[" + this.nx + "," + this.ny + "," + "," + this.nz + "] != " + "[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        switch (imageWare.getType()) {
            case 1: {
                for (int i = 0; i < this.nz; ++i) {
                    byte[] arrby = ((ByteSet)imageWare).getSliceByte(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] * arrby[j]);
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = ((ShortSet)imageWare).getSliceShort(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs2 = (short[])this.data[i];
                        int n = j;
                        arrs2[n] = (short)(arrs2[n] * arrs[j]);
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = ((FloatSet)imageWare).getSliceFloat(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] * (short)arrf[j]);
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.nz; ++i) {
                    double[] arrd = ((DoubleSet)imageWare).getSliceDouble(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] * (short)arrd[j]);
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + imageWare.getType() + "].\n" + "-------------------------------------------------------\n");
            }
        }
    }

    public void subtract(ImageWare imageWare) {
        if (!this.isSameSize(imageWare)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to subtract because the two operands are not the same size.\n[" + this.nx + "," + this.ny + "," + "," + this.nz + "] != " + "[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        switch (imageWare.getType()) {
            case 1: {
                for (int i = 0; i < this.nz; ++i) {
                    byte[] arrby = ((ByteSet)imageWare).getSliceByte(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] - arrby[j]);
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = ((ShortSet)imageWare).getSliceShort(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs2 = (short[])this.data[i];
                        int n = j;
                        arrs2[n] = (short)(arrs2[n] - arrs[j]);
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = ((FloatSet)imageWare).getSliceFloat(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] - (short)arrf[j]);
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.nz; ++i) {
                    double[] arrd = ((DoubleSet)imageWare).getSliceDouble(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] - (short)arrd[j]);
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + imageWare.getType() + "].\n" + "-------------------------------------------------------\n");
            }
        }
    }

    public void divide(ImageWare imageWare) {
        if (!this.isSameSize(imageWare)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to divide because the two operands are not the same size.\n[" + this.nx + "," + this.ny + "," + "," + this.nz + "] != " + "[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        switch (imageWare.getType()) {
            case 1: {
                for (int i = 0; i < this.nz; ++i) {
                    byte[] arrby = ((ByteSet)imageWare).getSliceByte(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] / arrby[j]);
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.nz; ++i) {
                    short[] arrs = ((ShortSet)imageWare).getSliceShort(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs2 = (short[])this.data[i];
                        int n = j;
                        arrs2[n] = (short)(arrs2[n] / arrs[j]);
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.nz; ++i) {
                    float[] arrf = ((FloatSet)imageWare).getSliceFloat(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] / (short)arrf[j]);
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.nz; ++i) {
                    double[] arrd = ((DoubleSet)imageWare).getSliceDouble(i);
                    for (int j = 0; j < this.nxy; ++j) {
                        short[] arrs = (short[])this.data[i];
                        int n = j;
                        arrs[n] = (short)(arrs[n] / (short)arrd[j]);
                    }
                }
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + imageWare.getType() + "].\n" + "-------------------------------------------------------\n");
            }
        }
    }
}

