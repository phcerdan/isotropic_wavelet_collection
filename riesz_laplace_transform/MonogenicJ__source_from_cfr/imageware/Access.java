/*
 * Decompiled with CFR 0_118.
 */
package imageware;

import imageware.Buffer;

public interface Access
extends Buffer {
    public double getPixel(int var1, int var2, int var3);

    public double getPixel(int var1, int var2, int var3, byte var4);

    public double getInterpolatedPixel(double var1, double var3, double var5);

    public double getInterpolatedPixel(double var1, double var3, double var5, byte var7);

    public void putPixel(int var1, int var2, int var3, double var4);

    public void getBoundedX(int var1, int var2, int var3, byte[] var4);

    public void getBoundedY(int var1, int var2, int var3, byte[] var4);

    public void getBoundedZ(int var1, int var2, int var3, byte[] var4);

    public void getBoundedXY(int var1, int var2, int var3, byte[][] var4);

    public void getBoundedXZ(int var1, int var2, int var3, byte[][] var4);

    public void getBoundedYZ(int var1, int var2, int var3, byte[][] var4);

    public void getBoundedXYZ(int var1, int var2, int var3, byte[][][] var4);

    public void getBoundedX(int var1, int var2, int var3, short[] var4);

    public void getBoundedY(int var1, int var2, int var3, short[] var4);

    public void getBoundedZ(int var1, int var2, int var3, short[] var4);

    public void getBoundedXY(int var1, int var2, int var3, short[][] var4);

    public void getBoundedXZ(int var1, int var2, int var3, short[][] var4);

    public void getBoundedYZ(int var1, int var2, int var3, short[][] var4);

    public void getBoundedXYZ(int var1, int var2, int var3, short[][][] var4);

    public void getBoundedX(int var1, int var2, int var3, float[] var4);

    public void getBoundedY(int var1, int var2, int var3, float[] var4);

    public void getBoundedZ(int var1, int var2, int var3, float[] var4);

    public void getBoundedXY(int var1, int var2, int var3, float[][] var4);

    public void getBoundedXZ(int var1, int var2, int var3, float[][] var4);

    public void getBoundedYZ(int var1, int var2, int var3, float[][] var4);

    public void getBoundedXYZ(int var1, int var2, int var3, float[][][] var4);

    public void getBoundedX(int var1, int var2, int var3, double[] var4);

    public void getBoundedY(int var1, int var2, int var3, double[] var4);

    public void getBoundedZ(int var1, int var2, int var3, double[] var4);

    public void getBoundedXY(int var1, int var2, int var3, double[][] var4);

    public void getBoundedXZ(int var1, int var2, int var3, double[][] var4);

    public void getBoundedYZ(int var1, int var2, int var3, double[][] var4);

    public void getBoundedXYZ(int var1, int var2, int var3, double[][][] var4);

    public void getBlockX(int var1, int var2, int var3, byte[] var4, byte var5);

    public void getBlockY(int var1, int var2, int var3, byte[] var4, byte var5);

    public void getBlockZ(int var1, int var2, int var3, byte[] var4, byte var5);

    public void getBlockXY(int var1, int var2, int var3, byte[][] var4, byte var5);

    public void getBlockXZ(int var1, int var2, int var3, byte[][] var4, byte var5);

    public void getBlockYZ(int var1, int var2, int var3, byte[][] var4, byte var5);

    public void getBlockXYZ(int var1, int var2, int var3, byte[][][] var4, byte var5);

    public void getBlockX(int var1, int var2, int var3, short[] var4, byte var5);

    public void getBlockY(int var1, int var2, int var3, short[] var4, byte var5);

    public void getBlockZ(int var1, int var2, int var3, short[] var4, byte var5);

    public void getBlockXY(int var1, int var2, int var3, short[][] var4, byte var5);

    public void getBlockXZ(int var1, int var2, int var3, short[][] var4, byte var5);

    public void getBlockYZ(int var1, int var2, int var3, short[][] var4, byte var5);

    public void getBlockXYZ(int var1, int var2, int var3, short[][][] var4, byte var5);

    public void getBlockX(int var1, int var2, int var3, float[] var4, byte var5);

    public void getBlockY(int var1, int var2, int var3, float[] var4, byte var5);

    public void getBlockZ(int var1, int var2, int var3, float[] var4, byte var5);

    public void getBlockXY(int var1, int var2, int var3, float[][] var4, byte var5);

    public void getBlockXZ(int var1, int var2, int var3, float[][] var4, byte var5);

    public void getBlockYZ(int var1, int var2, int var3, float[][] var4, byte var5);

    public void getBlockXYZ(int var1, int var2, int var3, float[][][] var4, byte var5);

    public void getBlockX(int var1, int var2, int var3, double[] var4, byte var5);

    public void getBlockY(int var1, int var2, int var3, double[] var4, byte var5);

    public void getBlockZ(int var1, int var2, int var3, double[] var4, byte var5);

    public void getBlockXY(int var1, int var2, int var3, double[][] var4, byte var5);

    public void getBlockXZ(int var1, int var2, int var3, double[][] var4, byte var5);

    public void getBlockYZ(int var1, int var2, int var3, double[][] var4, byte var5);

    public void getBlockXYZ(int var1, int var2, int var3, double[][][] var4, byte var5);

    public void getNeighborhoodX(int var1, int var2, int var3, byte[] var4, byte var5);

    public void getNeighborhoodY(int var1, int var2, int var3, byte[] var4, byte var5);

    public void getNeighborhoodZ(int var1, int var2, int var3, byte[] var4, byte var5);

    public void getNeighborhoodXY(int var1, int var2, int var3, byte[][] var4, byte var5);

    public void getNeighborhoodXZ(int var1, int var2, int var3, byte[][] var4, byte var5);

    public void getNeighborhoodYZ(int var1, int var2, int var3, byte[][] var4, byte var5);

    public void getNeighborhoodXYZ(int var1, int var2, int var3, byte[][][] var4, byte var5);

    public void getNeighborhoodX(int var1, int var2, int var3, short[] var4, byte var5);

    public void getNeighborhoodY(int var1, int var2, int var3, short[] var4, byte var5);

    public void getNeighborhoodZ(int var1, int var2, int var3, short[] var4, byte var5);

    public void getNeighborhoodXY(int var1, int var2, int var3, short[][] var4, byte var5);

    public void getNeighborhoodXZ(int var1, int var2, int var3, short[][] var4, byte var5);

    public void getNeighborhoodYZ(int var1, int var2, int var3, short[][] var4, byte var5);

    public void getNeighborhoodXYZ(int var1, int var2, int var3, short[][][] var4, byte var5);

    public void getNeighborhoodX(int var1, int var2, int var3, float[] var4, byte var5);

    public void getNeighborhoodY(int var1, int var2, int var3, float[] var4, byte var5);

    public void getNeighborhoodZ(int var1, int var2, int var3, float[] var4, byte var5);

    public void getNeighborhoodXY(int var1, int var2, int var3, float[][] var4, byte var5);

    public void getNeighborhoodXZ(int var1, int var2, int var3, float[][] var4, byte var5);

    public void getNeighborhoodYZ(int var1, int var2, int var3, float[][] var4, byte var5);

    public void getNeighborhoodXYZ(int var1, int var2, int var3, float[][][] var4, byte var5);

    public void getNeighborhoodX(int var1, int var2, int var3, double[] var4, byte var5);

    public void getNeighborhoodY(int var1, int var2, int var3, double[] var4, byte var5);

    public void getNeighborhoodZ(int var1, int var2, int var3, double[] var4, byte var5);

    public void getNeighborhoodXY(int var1, int var2, int var3, double[][] var4, byte var5);

    public void getNeighborhoodXZ(int var1, int var2, int var3, double[][] var4, byte var5);

    public void getNeighborhoodYZ(int var1, int var2, int var3, double[][] var4, byte var5);

    public void getNeighborhoodXYZ(int var1, int var2, int var3, double[][][] var4, byte var5);

    public void putBoundedX(int var1, int var2, int var3, byte[] var4);

    public void putBoundedY(int var1, int var2, int var3, byte[] var4);

    public void putBoundedZ(int var1, int var2, int var3, byte[] var4);

    public void putBoundedXY(int var1, int var2, int var3, byte[][] var4);

    public void putBoundedXZ(int var1, int var2, int var3, byte[][] var4);

    public void putBoundedYZ(int var1, int var2, int var3, byte[][] var4);

    public void putBoundedXYZ(int var1, int var2, int var3, byte[][][] var4);

    public void putBoundedX(int var1, int var2, int var3, short[] var4);

    public void putBoundedY(int var1, int var2, int var3, short[] var4);

    public void putBoundedZ(int var1, int var2, int var3, short[] var4);

    public void putBoundedXY(int var1, int var2, int var3, short[][] var4);

    public void putBoundedXZ(int var1, int var2, int var3, short[][] var4);

    public void putBoundedYZ(int var1, int var2, int var3, short[][] var4);

    public void putBoundedXYZ(int var1, int var2, int var3, short[][][] var4);

    public void putBoundedX(int var1, int var2, int var3, float[] var4);

    public void putBoundedY(int var1, int var2, int var3, float[] var4);

    public void putBoundedZ(int var1, int var2, int var3, float[] var4);

    public void putBoundedXY(int var1, int var2, int var3, float[][] var4);

    public void putBoundedXZ(int var1, int var2, int var3, float[][] var4);

    public void putBoundedYZ(int var1, int var2, int var3, float[][] var4);

    public void putBoundedXYZ(int var1, int var2, int var3, float[][][] var4);

    public void putBoundedX(int var1, int var2, int var3, double[] var4);

    public void putBoundedY(int var1, int var2, int var3, double[] var4);

    public void putBoundedZ(int var1, int var2, int var3, double[] var4);

    public void putBoundedXY(int var1, int var2, int var3, double[][] var4);

    public void putBoundedXZ(int var1, int var2, int var3, double[][] var4);

    public void putBoundedYZ(int var1, int var2, int var3, double[][] var4);

    public void putBoundedXYZ(int var1, int var2, int var3, double[][][] var4);
}

