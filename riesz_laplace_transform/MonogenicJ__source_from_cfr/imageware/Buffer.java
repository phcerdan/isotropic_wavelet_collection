/*
 * Decompiled with CFR 0_118.
 */
package imageware;

import imageware.ImageWare;

public interface Buffer {
    public int getType();

    public String getTypeToString();

    public int getDimension();

    public int[] getSize();

    public int getSizeX();

    public int getSizeY();

    public int getSizeZ();

    public int getWidth();

    public int getHeight();

    public int getDepth();

    public int getTotalSize();

    public boolean isSameSize(ImageWare var1);

    public void getX(int var1, int var2, int var3, ImageWare var4);

    public void getY(int var1, int var2, int var3, ImageWare var4);

    public void getZ(int var1, int var2, int var3, ImageWare var4);

    public void getXY(int var1, int var2, int var3, ImageWare var4);

    public void getXZ(int var1, int var2, int var3, ImageWare var4);

    public void getYZ(int var1, int var2, int var3, ImageWare var4);

    public void getXYZ(int var1, int var2, int var3, ImageWare var4);

    public void getX(int var1, int var2, int var3, byte[] var4);

    public void getY(int var1, int var2, int var3, byte[] var4);

    public void getZ(int var1, int var2, int var3, byte[] var4);

    public void getXY(int var1, int var2, int var3, byte[][] var4);

    public void getXZ(int var1, int var2, int var3, byte[][] var4);

    public void getYZ(int var1, int var2, int var3, byte[][] var4);

    public void getXYZ(int var1, int var2, int var3, byte[][][] var4);

    public void getX(int var1, int var2, int var3, short[] var4);

    public void getY(int var1, int var2, int var3, short[] var4);

    public void getZ(int var1, int var2, int var3, short[] var4);

    public void getXY(int var1, int var2, int var3, short[][] var4);

    public void getXZ(int var1, int var2, int var3, short[][] var4);

    public void getYZ(int var1, int var2, int var3, short[][] var4);

    public void getXYZ(int var1, int var2, int var3, short[][][] var4);

    public void getX(int var1, int var2, int var3, float[] var4);

    public void getY(int var1, int var2, int var3, float[] var4);

    public void getZ(int var1, int var2, int var3, float[] var4);

    public void getXY(int var1, int var2, int var3, float[][] var4);

    public void getXZ(int var1, int var2, int var3, float[][] var4);

    public void getYZ(int var1, int var2, int var3, float[][] var4);

    public void getXYZ(int var1, int var2, int var3, float[][][] var4);

    public void getX(int var1, int var2, int var3, double[] var4);

    public void getY(int var1, int var2, int var3, double[] var4);

    public void getZ(int var1, int var2, int var3, double[] var4);

    public void getXY(int var1, int var2, int var3, double[][] var4);

    public void getXZ(int var1, int var2, int var3, double[][] var4);

    public void getYZ(int var1, int var2, int var3, double[][] var4);

    public void getXYZ(int var1, int var2, int var3, double[][][] var4);

    public void putX(int var1, int var2, int var3, ImageWare var4);

    public void putY(int var1, int var2, int var3, ImageWare var4);

    public void putZ(int var1, int var2, int var3, ImageWare var4);

    public void putXY(int var1, int var2, int var3, ImageWare var4);

    public void putXZ(int var1, int var2, int var3, ImageWare var4);

    public void putYZ(int var1, int var2, int var3, ImageWare var4);

    public void putXYZ(int var1, int var2, int var3, ImageWare var4);

    public void putX(int var1, int var2, int var3, byte[] var4);

    public void putY(int var1, int var2, int var3, byte[] var4);

    public void putZ(int var1, int var2, int var3, byte[] var4);

    public void putXY(int var1, int var2, int var3, byte[][] var4);

    public void putXZ(int var1, int var2, int var3, byte[][] var4);

    public void putYZ(int var1, int var2, int var3, byte[][] var4);

    public void putXYZ(int var1, int var2, int var3, byte[][][] var4);

    public void putX(int var1, int var2, int var3, short[] var4);

    public void putY(int var1, int var2, int var3, short[] var4);

    public void putZ(int var1, int var2, int var3, short[] var4);

    public void putXY(int var1, int var2, int var3, short[][] var4);

    public void putXZ(int var1, int var2, int var3, short[][] var4);

    public void putYZ(int var1, int var2, int var3, short[][] var4);

    public void putXYZ(int var1, int var2, int var3, short[][][] var4);

    public void putX(int var1, int var2, int var3, float[] var4);

    public void putY(int var1, int var2, int var3, float[] var4);

    public void putZ(int var1, int var2, int var3, float[] var4);

    public void putXY(int var1, int var2, int var3, float[][] var4);

    public void putXZ(int var1, int var2, int var3, float[][] var4);

    public void putYZ(int var1, int var2, int var3, float[][] var4);

    public void putXYZ(int var1, int var2, int var3, float[][][] var4);

    public void putX(int var1, int var2, int var3, double[] var4);

    public void putY(int var1, int var2, int var3, double[] var4);

    public void putZ(int var1, int var2, int var3, double[] var4);

    public void putXY(int var1, int var2, int var3, double[][] var4);

    public void putXZ(int var1, int var2, int var3, double[][] var4);

    public void putYZ(int var1, int var2, int var3, double[][] var4);

    public void putXYZ(int var1, int var2, int var3, double[][][] var4);

    public Object[] getVolume();

    public byte[] getSliceByte(int var1);

    public short[] getSliceShort(int var1);

    public float[] getSliceFloat(int var1);

    public double[] getSliceDouble(int var1);
}

