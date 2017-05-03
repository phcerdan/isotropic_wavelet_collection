/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.ByteProcessor
 *  ij.process.ColorProcessor
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 *  ij.process.ShortProcessor
 */
package ijtools;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.awt.image.ColorModel;

public class IJtools {
    public static void show(String string, double[] arrd, int n, int n2) {
        new ImagePlus(string, (ImageProcessor)new FloatProcessor(n, n2, arrd)).show();
    }

    public static void show(String string, float[] arrf, int n, int n2) {
        new ImagePlus(string, (ImageProcessor)new FloatProcessor(n, n2, arrf, null)).show();
    }

    public static void show(String string, int[] arrn, int n, int n2) {
        new ImagePlus(string, (ImageProcessor)new FloatProcessor(n, n2, arrn)).show();
    }

    public static void show(String string, short[] arrs, int n, int n2) {
        new ImagePlus(string, (ImageProcessor)new ShortProcessor(n, n2, arrs, null)).show();
    }

    public static void show(String string, byte[] arrby, int n, int n2) {
        new ImagePlus(string, (ImageProcessor)new ByteProcessor(n, n2, arrby, null)).show();
    }

    public static void showRGB(String string, int[] arrn, int n, int n2) {
        new ImagePlus(string, (ImageProcessor)new ColorProcessor(n, n2, arrn)).show();
    }

    public static void show(String string, double[][] arrd) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        double[] arrd2 = new double[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                arrd2[j + i * n] = arrd[j][i];
            }
        }
        IJtools.show(string, arrd2, n, n2);
    }

    public static void show(String string, float[][] arrf) {
        int n = arrf.length;
        int n2 = arrf[0].length;
        float[] arrf2 = new float[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                arrf2[j + i * n] = arrf[j][i];
            }
        }
        IJtools.show(string, arrf2, n, n2);
    }

    public static void show(String string, int[][] arrn) {
        int n = arrn.length;
        int n2 = arrn[0].length;
        int[] arrn2 = new int[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                arrn2[j + i * n] = arrn[j][i];
            }
        }
        IJtools.show(string, arrn2, n, n2);
    }

    public static void show(String string, short[][] arrs) {
        int n = arrs.length;
        int n2 = arrs[0].length;
        short[] arrs2 = new short[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                arrs2[j + i * n] = arrs[j][i];
            }
        }
        IJtools.show(string, arrs2, n, n2);
    }

    public static void show(String string, byte[][] arrby) {
        int n = arrby.length;
        int n2 = arrby[0].length;
        byte[] arrby2 = new byte[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                arrby2[j + i * n] = arrby[j][i];
            }
        }
        IJtools.show(string, arrby2, n, n2);
    }

    public static void showRGB(String string, int[][] arrn) {
        int n = arrn.length;
        int n2 = arrn[0].length;
        int[] arrn2 = new int[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                arrn2[j + i * n] = arrn[j][i];
            }
        }
        IJtools.showRGB(string, arrn2, n, n2);
    }

    public static void showStack(String string, double[] arrd, int n, int n2, int n3) {
        ImageStack imageStack = new ImageStack(n, n2);
        for (int i = 0; i < n3; ++i) {
            double[] arrd2 = new double[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                arrd2[j] = arrd[j + i * n * n2];
            }
            imageStack.addSlice("", (ImageProcessor)new FloatProcessor(n, n2, arrd2));
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void showStack(String string, float[] arrf, int n, int n2, int n3) {
        ImageStack imageStack = new ImageStack(n, n2);
        for (int i = 0; i < n3; ++i) {
            float[] arrf2 = new float[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                arrf2[j] = arrf[j + i * n * n2];
            }
            imageStack.addSlice("", (Object)arrf2);
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void showStack(String string, int[] arrn, int n, int n2, int n3) {
        ImageStack imageStack = new ImageStack(n, n2);
        int[] arrn2 = new int[n * n2];
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n * n2; ++j) {
                arrn2[j] = arrn[j + i * n * n2];
            }
            imageStack.addSlice("", (ImageProcessor)new FloatProcessor(n, n2, arrn2));
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void showStack(String string, short[] arrs, int n, int n2, int n3) {
        ImageStack imageStack = new ImageStack(n, n2);
        for (int i = 0; i < n3; ++i) {
            short[] arrs2 = new short[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                arrs2[j] = arrs[j + i * n * n2];
            }
            imageStack.addSlice("", (Object)arrs2);
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void showStack(String string, byte[] arrby, int n, int n2, int n3) {
        ImageStack imageStack = new ImageStack(n, n2);
        for (int i = 0; i < n3; ++i) {
            byte[] arrby2 = new byte[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                arrby2[j] = arrby[j + i * n * n2];
            }
            imageStack.addSlice("", (Object)arrby2);
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void showStackRGB(String string, int[] arrn, int n, int n2, int n3) {
        ImageStack imageStack = new ImageStack(n, n2);
        int[] arrn2 = new int[n * n2];
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n * n2; ++j) {
                arrn2[j] = arrn[j + i * n * n2];
            }
            imageStack.addSlice("", (ImageProcessor)new ColorProcessor(n, n2, arrn2));
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void showStack(String string, double[][] arrd, int n, int n2) {
        int n3 = arrd.length;
        ImageStack imageStack = new ImageStack(n, n2);
        double d = -1.7976931348623157E308;
        double d2 = Double.MAX_VALUE;
        for (int i = 0; i < n3; ++i) {
            double[] arrd2 = new double[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                double d3 = arrd[i][j];
                if (d3 > d) {
                    d = d3;
                }
                if (d3 >= d2) continue;
                d2 = d3;
            }
            imageStack.addSlice("", (Object)arrd2);
        }
        ImagePlus imagePlus = new ImagePlus(string, imageStack);
        imagePlus.getProcessor().setMinAndMax(d2, d);
        imagePlus.show();
    }

    public static void showStack(String string, float[][] arrf, int n, int n2) {
        int n3 = arrf.length;
        ImageStack imageStack = new ImageStack(n, n2);
        float f = -3.4028235E38f;
        float f2 = Float.MAX_VALUE;
        for (int i = 0; i < n3; ++i) {
            float[] arrf2 = new float[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                float f3 = arrf[i][j];
                if (f3 > f) {
                    f = f3;
                }
                if (f3 >= f2) continue;
                f2 = f3;
            }
            imageStack.addSlice("", (Object)arrf2);
        }
        ImagePlus imagePlus = new ImagePlus(string, imageStack);
        imagePlus.getProcessor().setMinAndMax((double)f2, (double)f);
        imagePlus.show();
    }

    public static void showStack(String string, int[][] arrn, int n, int n2) {
        int n3 = arrn.length;
        ImageStack imageStack = new ImageStack(n, n2);
        int n4 = -2147483647;
        int n5 = Integer.MAX_VALUE;
        int[] arrn2 = new int[n * n2];
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n * n2; ++j) {
                int n6 = arrn[i][j];
                if (n6 > n4) {
                    n4 = n6;
                }
                if (n6 >= n5) continue;
                n5 = n6;
            }
            imageStack.addSlice("", (ImageProcessor)new FloatProcessor(n, n2, arrn2));
        }
        ImagePlus imagePlus = new ImagePlus(string, imageStack);
        imagePlus.getProcessor().setMinAndMax((double)n5, (double)n4);
        imagePlus.show();
    }

    public static void showStack(String string, short[][] arrs, int n, int n2) {
        int n3 = arrs.length;
        ImageStack imageStack = new ImageStack(n, n2);
        short s = -32767;
        short s2 = 32767;
        for (int i = 0; i < n3; ++i) {
            short[] arrs2 = new short[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                short s3 = arrs[i][j];
                if (s3 > s) {
                    s = s3;
                }
                if (s3 >= s2) continue;
                s2 = s3;
            }
            imageStack.addSlice("", (Object)arrs2);
        }
        ImagePlus imagePlus = new ImagePlus(string, imageStack);
        imagePlus.getProcessor().setMinAndMax((double)s2, (double)s);
        imagePlus.show();
    }

    public static void showStack(String string, byte[][] arrby, int n, int n2) {
        int n3 = arrby.length;
        ImageStack imageStack = new ImageStack(n, n2);
        byte by = -127;
        byte by2 = 127;
        for (int i = 0; i < n3; ++i) {
            byte[] arrby2 = new byte[n * n2];
            for (int j = 0; j < n * n2; ++j) {
                byte by3 = arrby[i][j];
                if (by3 > by) {
                    by = by3;
                }
                if (by3 >= by2) continue;
                by2 = by3;
            }
            imageStack.addSlice("", (Object)arrby2);
        }
        ImagePlus imagePlus = new ImagePlus(string, imageStack);
        imagePlus.getProcessor().setMinAndMax((double)by2, (double)by);
        imagePlus.show();
    }

    public static void showStackRGB(String string, int[][] arrn, int n, int n2) {
        int n3 = arrn.length;
        ImageStack imageStack = new ImageStack(n, n2);
        for (int i = 0; i < n3; ++i) {
            imageStack.addSlice("", (ImageProcessor)new ColorProcessor(n, n2, arrn[i]));
        }
        new ImagePlus(string, imageStack).show();
    }

    public static void loadImage(ImageProcessor imageProcessor, double[] arrd) {
        if (imageProcessor == null) {
            throw new ArrayStoreException("ImageProcessor == null.");
        }
        int n = imageProcessor.getWidth() * imageProcessor.getHeight();
        if (n != arrd.length) {
            throw new IndexOutOfBoundsException("Array sizes do not match.");
        }
        if (imageProcessor.getPixels() instanceof byte[]) {
            byte[] arrby = (byte[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrd[i] = arrby[i] & 255;
            }
        } else if (imageProcessor.getPixels() instanceof short[]) {
            short[] arrs = (short[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrd[i] = arrs[i] & 65535;
            }
        } else if (imageProcessor.getPixels() instanceof float[]) {
            float[] arrf = (float[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrd[i] = arrf[i];
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadImage(ImageProcessor imageProcessor, float[] arrf) {
        if (imageProcessor == null) {
            throw new ArrayStoreException("ImageProcessor == null.");
        }
        int n = imageProcessor.getWidth() * imageProcessor.getHeight();
        if (n != arrf.length) {
            throw new IndexOutOfBoundsException("Array sizes do not match");
        }
        if (imageProcessor.getPixels() instanceof byte[]) {
            byte[] arrby = (byte[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrf[i] = arrby[i] & 255;
            }
        } else if (imageProcessor.getPixels() instanceof short[]) {
            short[] arrs = (short[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrf[i] = arrs[i] & 65535;
            }
        } else if (imageProcessor.getPixels() instanceof float[]) {
            float[] arrf2 = (float[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrf[i] = arrf2[i];
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadImage(ImageProcessor imageProcessor, double[][] arrd) {
        if (imageProcessor == null) {
            throw new ArrayStoreException("ImageProcessor == null.");
        }
        int n = imageProcessor.getWidth();
        int n2 = imageProcessor.getHeight();
        if (n != arrd.length || n2 != arrd[0].length) {
            throw new IndexOutOfBoundsException("Array sizes do not match.");
        }
        if (imageProcessor.getPixels() instanceof byte[]) {
            byte[] arrby = (byte[])imageProcessor.getPixels();
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    arrd[j][i] = arrby[j + i * n] & 255;
                }
            }
        } else if (imageProcessor.getPixels() instanceof short[]) {
            short[] arrs = (short[])imageProcessor.getPixels();
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    arrd[j][i] = arrs[j + i * n] & 65535;
                }
            }
        } else if (imageProcessor.getPixels() instanceof float[]) {
            float[] arrf = (float[])imageProcessor.getPixels();
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    arrd[j][i] = arrf[j + i * n];
                }
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadImage(ImageProcessor imageProcessor, float[][] arrf) {
        if (imageProcessor == null) {
            throw new ArrayStoreException("ImageProcessor == null.");
        }
        int n = imageProcessor.getWidth();
        int n2 = imageProcessor.getHeight();
        if (n != arrf.length || n2 != arrf[0].length) {
            throw new IndexOutOfBoundsException("Array sizes do not match.");
        }
        if (imageProcessor.getPixels() instanceof byte[]) {
            byte[] arrby = (byte[])imageProcessor.getPixels();
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    arrf[j][i] = arrby[j + i * n] & 255;
                }
            }
        } else if (imageProcessor.getPixels() instanceof short[]) {
            short[] arrs = (short[])imageProcessor.getPixels();
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    arrf[j][i] = arrs[j + i * n] & 65535;
                }
            }
        } else if (imageProcessor.getPixels() instanceof float[]) {
            float[] arrf2 = (float[])imageProcessor.getPixels();
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    arrf[j][i] = arrf2[j + i * n];
                }
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadImageRGB(ImageProcessor imageProcessor, float[] arrf, float[] arrf2, float[] arrf3) {
        if (imageProcessor == null) {
            throw new ArrayStoreException("ImageProcessor == null.");
        }
        int n = imageProcessor.getWidth() * imageProcessor.getHeight();
        if (n != arrf.length || n != arrf2.length || n != arrf3.length) {
            throw new IndexOutOfBoundsException("Array sizes do not match");
        }
        if (imageProcessor.getPixels() instanceof int[]) {
            int[] arrn = (int[])imageProcessor.getPixels();
            for (int i = 0; i < n; ++i) {
                arrf[i] = arrn[i] >> 16 & 255;
                arrf2[i] = arrn[i] >> 8 & 255;
                arrf3[i] = arrn[i] & 255;
            }
        } else {
            throw new ArrayStoreException("Unexpected image type");
        }
    }

    public static void loadStack(ImageStack imageStack, double[] arrd) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = imageStack.getWidth();
        int n2 = imageStack.getHeight();
        int n3 = imageStack.getSize();
        int n4 = 0;
        int n5 = n * n2;
        if (imageStack.getPixels(1) instanceof byte[]) {
            for (int i = 0; i < n3; ++i) {
                byte[] arrby = (byte[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrd[j + n4] = arrby[j] & 255;
                }
                n4 += n5;
            }
        } else if (imageStack.getPixels(1) instanceof short[]) {
            for (int i = 0; i < n3; ++i) {
                short[] arrs = (short[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrd[j + n4] = arrs[j] & 65535;
                }
                n4 += n5;
            }
        } else if (imageStack.getPixels(1) instanceof float[]) {
            for (int i = 0; i < n3; ++i) {
                float[] arrf = (float[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrd[j + n4] = arrf[j];
                }
                n4 += n5;
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadStack(ImageStack imageStack, float[] arrf) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = imageStack.getWidth();
        int n2 = imageStack.getHeight();
        int n3 = imageStack.getSize();
        int n4 = 0;
        int n5 = n * n2;
        if (imageStack.getPixels(1) instanceof byte[]) {
            for (int i = 0; i < n3; ++i) {
                byte[] arrby = (byte[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrf[j + n4] = arrby[j] & 255;
                }
                n4 += n5;
            }
        } else if (imageStack.getPixels(1) instanceof short[]) {
            for (int i = 0; i < n3; ++i) {
                short[] arrs = (short[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrf[j + n4] = arrs[j] & 65535;
                }
                n4 += n5;
            }
        } else if (imageStack.getPixels(1) instanceof float[]) {
            for (int i = 0; i < n3; ++i) {
                float[] arrf2 = (float[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrf[j + n4] = arrf2[j];
                }
                n4 += n5;
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadStack(ImageStack imageStack, double[][] arrd) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = arrd.length;
        int n2 = arrd[0].length;
        if (imageStack.getPixels(1) instanceof float[]) {
            for (int i = 0; i < n; ++i) {
                float[] arrf = (float[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrd[i][j] = arrf[j];
                }
            }
        } else if (imageStack.getPixels(1) instanceof short[]) {
            for (int i = 0; i < n; ++i) {
                short[] arrs = (short[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrd[i][j] = arrs[j] & 65535;
                }
            }
        } else if (imageStack.getPixels(1) instanceof byte[]) {
            for (int i = 0; i < n; ++i) {
                byte[] arrby = (byte[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrd[i][j] = arrby[j] & 255;
                }
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadStack(ImageStack imageStack, float[][] arrf) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = arrf.length;
        int n2 = arrf[0].length;
        if (imageStack.getPixels(1) instanceof float[]) {
            for (int i = 0; i < n; ++i) {
                float[] arrf2 = (float[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrf[i][j] = arrf2[j];
                }
            }
        } else if (imageStack.getPixels(1) instanceof short[]) {
            for (int i = 0; i < n; ++i) {
                short[] arrs = (short[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrf[i][j] = arrs[j] & 65535;
                }
            }
        } else if (imageStack.getPixels(1) instanceof byte[]) {
            for (int i = 0; i < n; ++i) {
                byte[] arrby = (byte[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrf[i][j] = arrby[j] & 255;
                }
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }

    public static void loadStackRGB(ImageStack imageStack, int[][] arrn) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = arrn.length;
        int n2 = arrn[0].length;
        if (imageStack.getPixels(1) instanceof int[]) {
            for (int i = 0; i < n; ++i) {
                int[] arrn2 = (int[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    arrn[i][j] = arrn2[j];
                }
            }
        } else {
            throw new ArrayStoreException("Input stack must be RGB.");
        }
    }

    public static void loadStackRGB(ImageStack imageStack, int[] arrn) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = imageStack.getWidth();
        int n2 = imageStack.getHeight();
        int n3 = imageStack.getSize();
        int n4 = 0;
        int n5 = n * n2;
        if (imageStack.getPixels(1) instanceof int[]) {
            for (int i = 0; i < n3; ++i) {
                int[] arrn2 = (int[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n5; ++j) {
                    arrn[j + n4] = arrn2[j];
                }
                n4 += n5;
            }
        } else {
            throw new ArrayStoreException("Input stack must be RGB.");
        }
    }

    public static void loadAndConvertRGBStack(ImageStack imageStack, float[][] arrf) {
        if (imageStack == null) {
            throw new ArrayStoreException("ImageStack == null.");
        }
        int n = arrf.length;
        int n2 = arrf[0].length;
        if (imageStack.getPixels(1) instanceof int[]) {
            for (int i = 0; i < n; ++i) {
                int[] arrn = (int[])imageStack.getPixels(i + 1);
                for (int j = 0; j < n2; ++j) {
                    float f = arrn[j] >> 16 & 255;
                    float f2 = arrn[j] >> 8 & 255;
                    float f3 = arrn[j] & 255;
                    arrf[i][j] = (f * f + f2 * f2 + f3 * f3) / (f + f2 + f3);
                }
            }
        } else {
            throw new ArrayStoreException("Unexpected image type.");
        }
    }
}

