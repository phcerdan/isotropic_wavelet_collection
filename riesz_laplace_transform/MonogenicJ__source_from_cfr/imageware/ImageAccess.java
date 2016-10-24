/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.gui.ImageWindow
 *  ij.process.ByteProcessor
 *  ij.process.ColorProcessor
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 *  ij.process.ShortProcessor
 */
package imageware;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageWindow;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import imageware.Builder;
import imageware.DoubleSet;
import imageware.ImageWare;
import java.awt.Point;

public class ImageAccess {
    public static final int PATTERN_SQUARE_3x3 = 0;
    public static final int PATTERN_CROSS_3x3 = 1;
    private ImageWare imageware = null;
    private int nx = 0;
    private int ny = 0;

    public ImageAccess(double[][] arrd) {
        if (arrd == null) {
            throw new ArrayStoreException("Constructor: array == null.");
        }
        this.imageware = Builder.create(arrd);
        this.nx = this.imageware.getSizeX();
        this.ny = this.imageware.getSizeY();
    }

    public ImageAccess(ImageProcessor imageProcessor) {
        if (imageProcessor == null) {
            throw new ArrayStoreException("Constructor: ImageProcessor == null.");
        }
        ImagePlus imagePlus = new ImagePlus("", imageProcessor);
        if (imageProcessor instanceof ByteProcessor) {
            this.imageware = Builder.create(imagePlus, 4);
        } else if (imageProcessor instanceof ShortProcessor) {
            this.imageware = Builder.create(imagePlus, 4);
        } else if (imageProcessor instanceof FloatProcessor) {
            this.imageware = Builder.create(imagePlus, 4);
        }
        this.nx = this.imageware.getSizeX();
        this.ny = this.imageware.getSizeY();
    }

    public ImageAccess(ColorProcessor colorProcessor, int n) {
        if (colorProcessor == null) {
            throw new ArrayStoreException("Constructor: ColorProcessor == null.");
        }
        if (n < 0) {
            throw new ArrayStoreException("Constructor: colorPlane < 0.");
        }
        if (n > 2) {
            throw new ArrayStoreException("Constructor: colorPlane > 2.");
        }
        this.nx = colorProcessor.getWidth();
        this.ny = colorProcessor.getHeight();
        int n2 = this.nx * this.ny;
        ImagePlus imagePlus = new ImagePlus("", (ImageProcessor)colorProcessor);
        this.imageware = new DoubleSet(imagePlus.getStack(), (byte)n);
    }

    public ImageAccess(int n, int n2) {
        this.imageware = new DoubleSet(n, n2, 1);
        this.nx = n;
        this.ny = n2;
    }

    public ImageWare getDataset() {
        return this.imageware;
    }

    public int getWidth() {
        return this.nx;
    }

    public int getHeight() {
        return this.ny;
    }

    public double getMaximum() {
        return this.imageware.getMaximum();
    }

    public double getMinimum() {
        return this.imageware.getMinimum();
    }

    public double getMean() {
        return this.imageware.getMean();
    }

    public double[][] getArrayPixels() {
        double[][] arrd = new double[this.nx][this.ny];
        this.imageware.getXY(0, 0, 0, arrd);
        return arrd;
    }

    public double[] getPixels() {
        return this.imageware.getSliceDouble(0);
    }

    public FloatProcessor createFloatProcessor() {
        FloatProcessor floatProcessor = new FloatProcessor(this.nx, this.ny);
        double[] arrd = this.getPixels();
        int n = arrd.length;
        float[] arrf = new float[n];
        for (int i = 0; i < n; ++i) {
            arrf[i] = (float)arrd[i];
        }
        floatProcessor.setPixels((Object)arrf);
        return floatProcessor;
    }

    public ByteProcessor createByteProcessor() {
        ByteProcessor byteProcessor = new ByteProcessor(this.nx, this.ny);
        double[] arrd = this.getPixels();
        int n = arrd.length;
        byte[] arrby = new byte[n];
        for (int i = 0; i < n; ++i) {
            double d = arrd[i];
            if (d < 0.0) {
                d = 0.0;
            }
            if (d > 255.0) {
                d = 255.0;
            }
            arrby[i] = (byte)d;
        }
        byteProcessor.setPixels((Object)arrby);
        return byteProcessor;
    }

    public ImageAccess duplicate() {
        double[][] arrd = new double[this.nx][this.ny];
        this.imageware.getXY(0, 0, 0, arrd);
        ImageAccess imageAccess = new ImageAccess(arrd);
        return imageAccess;
    }

    public double getPixel(int n, int n2) {
        return this.imageware.getPixel(n, n2, 0, 2);
    }

    public double getInterpolatedPixel(double d, double d2) {
        return this.imageware.getInterpolatedPixel(d, d2, 0.0, 2);
    }

    public void getColumn(int n, double[] arrd) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("getColumn: x < 0.");
        }
        if (n >= this.nx) {
            throw new IndexOutOfBoundsException("getColumn: x >= nx.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("getColumn: column == null.");
        }
        if (arrd.length != this.ny) {
            throw new ArrayStoreException("getColumn: column.length != ny.");
        }
        this.imageware.getBlockY(n, 0, 0, arrd, 2);
    }

    public void getColumn(int n, int n2, double[] arrd) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("getColumn: x < 0.");
        }
        if (n >= this.nx) {
            throw new IndexOutOfBoundsException("getColumn: x >= nx.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("getColumn: column == null.");
        }
        this.imageware.getBlockY(n, n2, 0, arrd, 2);
    }

    public void getRow(int n, double[] arrd) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("getRow: y < 0.");
        }
        if (n >= this.ny) {
            throw new IndexOutOfBoundsException("getRow: y >= ny.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("getColumn: row == null.");
        }
        if (arrd.length != this.nx) {
            throw new ArrayStoreException("getColumn: row.length != nx.");
        }
        this.imageware.getBlockX(0, n, 0, arrd, 2);
    }

    public void getRow(int n, int n2, double[] arrd) {
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("getRow: y < 0.");
        }
        if (n2 >= this.ny) {
            throw new IndexOutOfBoundsException("getRow: y >= ny.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("getRow: row == null.");
        }
        this.imageware.getBlockX(n, n2, 0, arrd, 2);
    }

    public void getNeighborhood(int n, int n2, double[][] arrd) {
        this.imageware.getNeighborhoodXY(n, n2, 0, arrd, 2);
    }

    public void getPattern(int n, int n2, double[] arrd, int n3) {
        if (arrd == null) {
            throw new ArrayStoreException("getPattern: neigh == null.");
        }
        double[][] arrd2 = new double[3][3];
        this.imageware.getNeighborhoodXY(n, n2, 0, arrd2, 2);
        switch (n3) {
            case 0: {
                if (arrd.length != 9) {
                    throw new ArrayStoreException("getPattern: neigh.length != 9.");
                }
                arrd[0] = arrd2[0][0];
                arrd[1] = arrd2[1][0];
                arrd[2] = arrd2[2][0];
                arrd[3] = arrd2[0][1];
                arrd[4] = arrd2[1][1];
                arrd[5] = arrd2[2][1];
                arrd[6] = arrd2[0][2];
                arrd[7] = arrd2[1][2];
                arrd[8] = arrd2[2][2];
                break;
            }
            case 1: {
                if (arrd.length != 5) {
                    throw new ArrayStoreException("getPattern: neigh.length != 5");
                }
                arrd[0] = arrd2[1][0];
                arrd[1] = arrd2[0][1];
                arrd[2] = arrd2[1][1];
                arrd[3] = arrd2[2][1];
                arrd[4] = arrd2[0][1];
                break;
            }
            default: {
                throw new ArrayStoreException("getPattern: unexpected pattern.");
            }
        }
    }

    public void getSubImage(int n, int n2, ImageAccess imageAccess) {
        if (imageAccess == null) {
            throw new ArrayStoreException("getSubImage: output == null.");
        }
        if (n < 0) {
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        }
        if (n2 < 0) {
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        }
        if (n >= this.nx) {
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        }
        if (n2 >= this.ny) {
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        }
        int n3 = imageAccess.getWidth();
        int n4 = imageAccess.getHeight();
        double[][] arrd = new double[n3][n4];
        this.imageware.getBlockXY(n, n2, 0, arrd, 2);
        imageAccess.putArrayPixels(arrd);
    }

    public void putPixel(int n, int n2, double d) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("putPixel: x < 0");
        }
        if (n >= this.nx) {
            throw new IndexOutOfBoundsException("putPixel: x >= nx");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("putPixel:  y < 0");
        }
        if (n2 >= this.ny) {
            throw new IndexOutOfBoundsException("putPixel:  y >= ny");
        }
        this.imageware.putPixel(n, n2, 0, d);
    }

    public void putColumn(int n, double[] arrd) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("putColumn: x < 0.");
        }
        if (n >= this.nx) {
            throw new IndexOutOfBoundsException("putColumn: x >= nx.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("putColumn: column == null.");
        }
        if (arrd.length != this.ny) {
            throw new ArrayStoreException("putColumn: column.length != ny.");
        }
        this.imageware.putBoundedY(n, 0, 0, arrd);
    }

    public void putColumn(int n, int n2, double[] arrd) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("putColumn: x < 0.");
        }
        if (n >= this.nx) {
            throw new IndexOutOfBoundsException("putColumn: x >= nx.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("putColumn: column == null.");
        }
        this.imageware.putBoundedY(n, n2, 0, arrd);
    }

    public void putRow(int n, double[] arrd) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("putRow: y < 0.");
        }
        if (n >= this.ny) {
            throw new IndexOutOfBoundsException("putRow: y >= ny.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("putRow: row == null.");
        }
        if (arrd.length != this.nx) {
            throw new ArrayStoreException("putRow: row.length != nx.");
        }
        this.imageware.putBoundedX(0, n, 0, arrd);
    }

    public void putRow(int n, int n2, double[] arrd) {
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("putRow: y < 0.");
        }
        if (n2 >= this.ny) {
            throw new IndexOutOfBoundsException("putRow: y >= ny.");
        }
        if (arrd == null) {
            throw new ArrayStoreException("putRow: row == null.");
        }
        this.imageware.putBoundedX(n, n2, 0, arrd);
    }

    public void putArrayPixels(double[][] arrd) {
        if (arrd == null) {
            throw new IndexOutOfBoundsException("putArrayPixels: array == null.");
        }
        this.imageware.putBoundedXY(0, 0, 0, arrd);
    }

    public void putSubImage(int n, int n2, ImageAccess imageAccess) {
        if (imageAccess == null) {
            throw new ArrayStoreException("putSubImage: input == null.");
        }
        if (n < 0) {
            throw new IndexOutOfBoundsException("putSubImage: x < 0.");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("putSubImage: y < 0.");
        }
        if (n >= this.nx) {
            throw new IndexOutOfBoundsException("putSubImage: x >= nx.");
        }
        if (n2 >= this.ny) {
            throw new IndexOutOfBoundsException("putSubImage: y >= ny.");
        }
        int n3 = imageAccess.getWidth();
        int n4 = imageAccess.getHeight();
        double[][] arrd = imageAccess.getArrayPixels();
        this.imageware.putBoundedXY(n, n2, 0, arrd);
    }

    public void setConstant(double d) {
        this.imageware.fillConstant(d);
    }

    public void normalizeContrast() {
        this.imageware.rescale();
    }

    public void show(String string, Point point) {
        FloatProcessor floatProcessor = this.createFloatProcessor();
        floatProcessor.resetMinAndMax();
        ImagePlus imagePlus = new ImagePlus(string, (ImageProcessor)floatProcessor);
        ImageWindow imageWindow = imagePlus.getWindow();
        imageWindow.setLocation(point.x, point.y);
        imagePlus.show();
    }

    public void show(String string) {
        this.imageware.show(string);
    }

    public void abs() {
        this.imageware.abs();
    }

    public void sqrt() {
        this.imageware.sqrt();
    }

    public void pow(double d) {
        this.imageware.pow(d);
    }

    public void add(double d) {
        this.imageware.add(d);
    }

    public void multiply(double d) {
        this.imageware.multiply(d);
    }

    public void subtract(double d) {
        this.imageware.add(- d);
    }

    public void divide(double d) {
        if (d == 0.0) {
            throw new ArrayStoreException("divide: Divide by 0");
        }
        this.imageware.multiply(1.0 / d);
    }

    public void add(ImageAccess imageAccess, ImageAccess imageAccess2) {
        if (imageAccess.getWidth() != this.nx) {
            throw new ArrayStoreException("add: incompatible size.");
        }
        if (imageAccess.getHeight() != this.ny) {
            throw new ArrayStoreException("add: incompatible size.");
        }
        if (imageAccess2.getWidth() != this.nx) {
            throw new ArrayStoreException("add: incompatible size.");
        }
        if (imageAccess2.getHeight() != this.ny) {
            throw new ArrayStoreException("add: incompatible size.");
        }
        this.imageware.copy(imageAccess.getDataset());
        this.imageware.add(imageAccess2.getDataset());
    }

    public void multiply(ImageAccess imageAccess, ImageAccess imageAccess2) {
        if (imageAccess.getWidth() != this.nx) {
            throw new ArrayStoreException("multiply: incompatible size.");
        }
        if (imageAccess.getHeight() != this.ny) {
            throw new ArrayStoreException("multiply: incompatible size.");
        }
        if (imageAccess2.getWidth() != this.nx) {
            throw new ArrayStoreException("multiply: incompatible size.");
        }
        if (imageAccess2.getHeight() != this.ny) {
            throw new ArrayStoreException("multiply: incompatible size.");
        }
        this.imageware.copy(imageAccess.getDataset());
        this.imageware.multiply(imageAccess2.getDataset());
    }

    public void subtract(ImageAccess imageAccess, ImageAccess imageAccess2) {
        if (imageAccess.getWidth() != this.nx) {
            throw new ArrayStoreException("subtract: incompatible size.");
        }
        if (imageAccess.getHeight() != this.ny) {
            throw new ArrayStoreException("subtract: incompatible size.");
        }
        if (imageAccess2.getWidth() != this.nx) {
            throw new ArrayStoreException("subtract: incompatible size.");
        }
        if (imageAccess2.getHeight() != this.ny) {
            throw new ArrayStoreException("subtract: incompatible size.");
        }
        this.imageware.copy(imageAccess.getDataset());
        this.imageware.subtract(imageAccess2.getDataset());
    }

    public void divide(ImageAccess imageAccess, ImageAccess imageAccess2) {
        if (imageAccess.getWidth() != this.nx) {
            throw new ArrayStoreException("divide: incompatible size.");
        }
        if (imageAccess.getHeight() != this.ny) {
            throw new ArrayStoreException("divide: incompatible size.");
        }
        if (imageAccess2.getWidth() != this.nx) {
            throw new ArrayStoreException("divide: incompatible size.");
        }
        if (imageAccess2.getHeight() != this.ny) {
            throw new ArrayStoreException("divide: incompatible size.");
        }
        this.imageware.copy(imageAccess.getDataset());
        this.imageware.divide(imageAccess2.getDataset());
    }
}

