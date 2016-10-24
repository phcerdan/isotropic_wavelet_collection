/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 *  ij.process.ByteProcessor
 *  ij.process.ColorProcessor
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 *  ij.process.ShortProcessor
 */
package imageware;

import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import imageware.Buffer;
import imageware.ImageWare;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

public class ShortBuffer
implements Buffer {
    protected Object[] data = null;
    protected int nx = 0;
    protected int ny = 0;
    protected int nz = 0;
    protected int nxy = 0;

    protected ShortBuffer(int n, int n2, int n3) {
        this.nx = n;
        this.ny = n2;
        this.nz = n3;
        if (n <= 0 || n2 <= 0 || n3 <= 0) {
            this.throw_constructor(n, n2, n3);
        }
        this.allocate();
    }

    protected ShortBuffer(Image image, int n) {
        if (image == null) {
            this.throw_constructor();
        }
        ImageObserver imageObserver = null;
        this.nx = image.getWidth(imageObserver);
        this.ny = image.getHeight(imageObserver);
        this.nz = 1;
        this.nxy = this.nx * this.ny;
        byte[] arrby = new byte[this.nxy];
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, this.nx, this.ny, false);
        try {
            pixelGrabber.grabPixels();
            arrby = (byte[])pixelGrabber.getPixels();
        }
        catch (Exception var6_6) {
            this.throw_constructor();
        }
        this.allocate();
        for (int i = 0; i < this.nxy; ++i) {
            ((short[])this.data[0])[i] = (short)(arrby[i] & 255);
        }
    }

    protected ShortBuffer(ImageStack imageStack, int n) {
        if (imageStack == null) {
            this.throw_constructor();
        }
        this.nx = imageStack.getWidth();
        this.ny = imageStack.getHeight();
        this.nz = imageStack.getSize();
        this.nxy = this.nx * this.ny;
        switch (n) {
            case 2: {
                this.data = imageStack.getImageArray();
                break;
            }
            case 1: {
                this.allocate();
                ImageProcessor imageProcessor = imageStack.getProcessor(1);
                if (imageProcessor instanceof ByteProcessor) {
                    Object[] arrobject = imageStack.getImageArray();
                    for (int i = 0; i < this.nz; ++i) {
                        byte[] arrby = (byte[])arrobject[i];
                        for (int j = 0; j < this.nxy; ++j) {
                            ((short[])this.data[i])[j] = (short)(arrby[j] & 255);
                        }
                    }
                    break;
                }
                if (imageProcessor instanceof ShortProcessor) {
                    Object[] arrobject = imageStack.getImageArray();
                    for (int i = 0; i < this.nz; ++i) {
                        short[] arrs = (short[])arrobject[i];
                        for (int j = 0; j < this.nxy; ++j) {
                            ((short[])this.data[i])[j] = (short)(arrs[j] & 65535);
                        }
                    }
                    break;
                }
                if (imageProcessor instanceof FloatProcessor) {
                    Object[] arrobject = imageStack.getImageArray();
                    for (int i = 0; i < this.nz; ++i) {
                        float[] arrf = (float[])arrobject[i];
                        for (int j = 0; j < this.nxy; ++j) {
                            ((short[])this.data[i])[j] = (short)arrf[j];
                        }
                    }
                    break;
                }
                if (imageProcessor instanceof ColorProcessor) {
                    for (int i = 0; i < this.nz; ++i) {
                        ColorProcessor colorProcessor = (ColorProcessor)imageStack.getProcessor(i + 1);
                        int[] arrn = (int[])colorProcessor.getPixels();
                        for (int j = 0; j < this.nxy; ++j) {
                            int n2 = arrn[j];
                            double d = (n2 & 16711680) >> 16;
                            double d2 = (n2 & 65280) >> 8;
                            double d3 = n2 & 255;
                            ((short[])this.data[i])[j] = (short)((d + d2 + d3) / 3.0);
                        }
                    }
                    break;
                }
                this.throw_constructor();
                break;
            }
            default: {
                this.throw_constructor();
            }
        }
    }

    protected ShortBuffer(ImageStack imageStack, byte by) {
        if (imageStack == null) {
            this.throw_constructor();
        }
        this.nx = imageStack.getWidth();
        this.ny = imageStack.getHeight();
        this.nz = imageStack.getSize();
        this.nxy = this.nx * this.ny;
        this.allocate();
        ImageProcessor imageProcessor = imageStack.getProcessor(1);
        if (imageProcessor instanceof ByteProcessor) {
            Object[] arrobject = imageStack.getImageArray();
            for (int i = 0; i < this.nz; ++i) {
                byte[] arrby = (byte[])arrobject[i];
                for (int j = 0; j < this.nxy; ++j) {
                    ((short[])this.data[i])[j] = (short)(arrby[j] & 255);
                }
            }
        } else if (imageProcessor instanceof ShortProcessor) {
            Object[] arrobject = imageStack.getImageArray();
            for (int i = 0; i < this.nz; ++i) {
                short[] arrs = (short[])arrobject[i];
                for (int j = 0; j < this.nxy; ++j) {
                    ((short[])this.data[i])[j] = (short)(arrs[j] & 65535);
                }
            }
        } else if (imageProcessor instanceof FloatProcessor) {
            Object[] arrobject = imageStack.getImageArray();
            for (int i = 0; i < this.nz; ++i) {
                float[] arrf = (float[])arrobject[i];
                for (int j = 0; j < this.nxy; ++j) {
                    ((short[])this.data[i])[j] = (short)arrf[j];
                }
            }
        } else if (imageProcessor instanceof ColorProcessor) {
            block11 : for (int i = 0; i < this.nz; ++i) {
                ColorProcessor colorProcessor = (ColorProcessor)imageStack.getProcessor(i + 1);
                int[] arrn = (int[])colorProcessor.getPixels();
                switch (by) {
                    int n;
                    case 0: {
                        for (n = 0; n < this.nxy; ++n) {
                            ((short[])this.data[i])[n] = (short)((arrn[n] & 16711680) >> 16);
                        }
                        continue block11;
                    }
                    case 1: {
                        for (n = 0; n < this.nxy; ++n) {
                            ((short[])this.data[i])[n] = (short)((arrn[n] & 65280) >> 8);
                        }
                        continue block11;
                    }
                    case 2: {
                        for (n = 0; n < this.nxy; ++n) {
                            ((short[])this.data[i])[n] = (short)(arrn[n] & 255);
                        }
                        continue block11;
                    }
                    default: {
                        this.throw_constructor();
                    }
                }
            }
        } else {
            this.throw_constructor();
        }
    }

    protected ShortBuffer(byte[] arrby, int n) {
        if (arrby == null) {
            this.throw_constructor();
        }
        this.nx = arrby.length;
        this.ny = 1;
        this.nz = 1;
        this.allocate();
        this.putX(0, 0, 0, arrby);
    }

    protected ShortBuffer(byte[][] arrby, int n) {
        if (arrby == null) {
            this.throw_constructor();
        }
        this.nx = arrby.length;
        this.ny = arrby[0].length;
        this.nz = 1;
        this.allocate();
        this.putXY(0, 0, 0, arrby);
    }

    protected ShortBuffer(byte[][][] arrby, int n) {
        if (arrby == null) {
            this.throw_constructor();
        }
        this.nx = arrby.length;
        this.ny = arrby[0].length;
        this.nz = arrby[0][0].length;
        this.allocate();
        this.putXYZ(0, 0, 0, arrby);
    }

    protected ShortBuffer(short[] arrs, int n) {
        if (arrs == null) {
            this.throw_constructor();
        }
        this.nx = arrs.length;
        this.ny = 1;
        this.nz = 1;
        this.allocate();
        this.putX(0, 0, 0, arrs);
    }

    protected ShortBuffer(short[][] arrs, int n) {
        if (arrs == null) {
            this.throw_constructor();
        }
        this.nx = arrs.length;
        this.ny = arrs[0].length;
        this.nz = 1;
        this.allocate();
        this.putXY(0, 0, 0, arrs);
    }

    protected ShortBuffer(short[][][] arrs, int n) {
        if (arrs == null) {
            this.throw_constructor();
        }
        this.nx = arrs.length;
        this.ny = arrs[0].length;
        this.nz = arrs[0][0].length;
        this.allocate();
        this.putXYZ(0, 0, 0, arrs);
    }

    protected ShortBuffer(float[] arrf, int n) {
        if (arrf == null) {
            this.throw_constructor();
        }
        this.nx = arrf.length;
        this.ny = 1;
        this.nz = 1;
        this.allocate();
        this.putX(0, 0, 0, arrf);
    }

    protected ShortBuffer(float[][] arrf, int n) {
        if (arrf == null) {
            this.throw_constructor();
        }
        this.nx = arrf.length;
        this.ny = arrf[0].length;
        this.nz = 1;
        this.allocate();
        this.putXY(0, 0, 0, arrf);
    }

    protected ShortBuffer(float[][][] arrf, int n) {
        if (arrf == null) {
            this.throw_constructor();
        }
        this.nx = arrf.length;
        this.ny = arrf[0].length;
        this.nz = arrf[0][0].length;
        this.allocate();
        this.putXYZ(0, 0, 0, arrf);
    }

    protected ShortBuffer(double[] arrd, int n) {
        if (arrd == null) {
            this.throw_constructor();
        }
        this.nx = arrd.length;
        this.ny = 1;
        this.nz = 1;
        this.allocate();
        this.putX(0, 0, 0, arrd);
    }

    protected ShortBuffer(double[][] arrd, int n) {
        if (arrd == null) {
            this.throw_constructor();
        }
        this.nx = arrd.length;
        this.ny = arrd[0].length;
        this.nz = 1;
        this.allocate();
        this.putXY(0, 0, 0, arrd);
    }

    protected ShortBuffer(double[][][] arrd, int n) {
        if (arrd == null) {
            this.throw_constructor();
        }
        this.nx = arrd.length;
        this.ny = arrd[0].length;
        this.nz = arrd[0][0].length;
        this.allocate();
        this.putXYZ(0, 0, 0, arrd);
    }

    public int getType() {
        return 2;
    }

    public String getTypeToString() {
        return "Short";
    }

    public int getDimension() {
        int n = 0;
        n += this.nx > 1 ? 1 : 0;
        n += this.ny > 1 ? 1 : 0;
        return n += this.nz > 1 ? 1 : 0;
    }

    public int[] getSize() {
        int[] arrn = new int[]{this.nx, this.ny, this.nz};
        return arrn;
    }

    public int getSizeX() {
        return this.nx;
    }

    public int getSizeY() {
        return this.ny;
    }

    public int getSizeZ() {
        return this.nz;
    }

    public int getWidth() {
        return this.nx;
    }

    public int getHeight() {
        return this.ny;
    }

    public int getDepth() {
        return this.nz;
    }

    public int getTotalSize() {
        return this.nxy * this.nz;
    }

    public boolean isSameSize(ImageWare imageWare) {
        if (this.nx != imageWare.getSizeX()) {
            return false;
        }
        if (this.ny != imageWare.getSizeY()) {
            return false;
        }
        if (this.nz != imageWare.getSizeZ()) {
            return false;
        }
        return true;
    }

    public void putX(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        double[] arrd = new double[n4];
        imageWare.getX(0, 0, 0, arrd);
        this.putX(n, n2, n3, arrd);
    }

    public void putY(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeY();
        double[] arrd = new double[n4];
        imageWare.getY(0, 0, 0, arrd);
        this.putY(n, n2, n3, arrd);
    }

    public void putZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeZ();
        double[] arrd = new double[n4];
        imageWare.getZ(0, 0, 0, arrd);
        this.putZ(n, n2, n3, arrd);
    }

    public void putXY(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        int n5 = imageWare.getSizeY();
        double[][] arrd = new double[n4][n5];
        imageWare.getXY(0, 0, 0, arrd);
        this.putXY(n, n2, n3, arrd);
    }

    public void putXZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        int n5 = imageWare.getSizeZ();
        double[][] arrd = new double[n4][n5];
        imageWare.getXZ(0, 0, 0, arrd);
        this.putXZ(n, n2, n3, arrd);
    }

    public void putYZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeY();
        int n5 = imageWare.getSizeZ();
        double[][] arrd = new double[n4][n5];
        imageWare.getYZ(0, 0, 0, arrd);
        this.putYZ(n, n2, n3, arrd);
    }

    public void putXYZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        int n5 = imageWare.getSizeY();
        int n6 = imageWare.getSizeZ();
        double[][][] arrd = new double[n4][n5][n6];
        imageWare.getXYZ(0, 0, 0, arrd);
        this.putXYZ(n, n2, n3, arrd);
    }

    public void putX(int n, int n2, int n3, byte[] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[n4] = (short)(arrby[i] & 255);
                ++n4;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "No check", arrby, n, n2, n3);
        }
    }

    public void putX(int n, int n2, int n3, short[] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            short[] arrs2 = (short[])this.data[n3];
            System.arraycopy(arrs, 0, arrs2, n4, n5);
        }
        catch (Exception var5_6) {
            this.throw_put("X", "No check", arrs, n, n2, n3);
        }
    }

    public void putX(int n, int n2, int n3, float[] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[n4] = (short)arrf[i];
                ++n4;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "No check", arrf, n, n2, n3);
        }
    }

    public void putX(int n, int n2, int n3, double[] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[n4] = (short)arrd[i];
                ++n4;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "No check", arrd, n, n2, n3);
        }
    }

    public void putY(int n, int n2, int n3, byte[] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[n4] = (short)(arrby[i] & 255);
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "No check", arrby, n, n2, n3);
        }
    }

    public void putY(int n, int n2, int n3, short[] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs2[n4] = (short)(arrs[i] & 65535);
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "No check", arrs, n, n2, n3);
        }
    }

    public void putY(int n, int n2, int n3, float[] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[n4] = (short)arrf[i];
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "No check", arrf, n, n2, n3);
        }
    }

    public void putY(int n, int n2, int n3, double[] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[n4] = (short)arrd[i];
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "No check", arrd, n, n2, n3);
        }
    }

    public void putZ(int n, int n2, int n3, byte[] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            for (int i = 0; i < n5; ++i) {
                ((short[])this.data[n3])[n4] = (short)(arrby[i] & 255);
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "No check", arrby, n, n2, n3);
        }
    }

    public void putZ(int n, int n2, int n3, short[] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            for (int i = 0; i < n5; ++i) {
                ((short[])this.data[n3])[n4] = (short)(arrs[i] & 65535);
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "No check", arrs, n, n2, n3);
        }
    }

    public void putZ(int n, int n2, int n3, float[] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            for (int i = 0; i < n5; ++i) {
                ((short[])this.data[n3])[n4] = (short)arrf[i];
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "No check", arrf, n, n2, n3);
        }
    }

    public void putZ(int n, int n2, int n3, double[] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            for (int i = 0; i < n5; ++i) {
                ((short[])this.data[n3])[n4] = (short)arrd[i];
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "No check", arrd, n, n2, n3);
        }
    }

    public void putXY(int n, int n2, int n3, byte[][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrs[n4] = (short)(arrby[n7][i] & 255);
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "No check", arrby, n, n2, n3);
        }
    }

    public void putXY(int n, int n2, int n3, short[][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrs2[n4] = (short)(arrs[n7][i] & 65535);
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "No check", arrs, n, n2, n3);
        }
    }

    public void putXY(int n, int n2, int n3, float[][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrs[n4] = (short)arrf[n7][i];
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "No check", arrf, n, n2, n3);
        }
    }

    public void putXY(int n, int n2, int n3, double[][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrs[n4] = (short)arrd[n7][i];
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "No check", arrd, n, n2, n3);
        }
    }

    public void putXZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n7 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)(arrby[n8][n7] & 255);
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "No check", arrby, n, n2, n3);
        }
    }

    public void putXZ(int n, int n2, int n3, short[][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n7 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)(arrs[n8][n7] & 65535);
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "No check", arrs, n, n2, n3);
        }
    }

    public void putXZ(int n, int n2, int n3, float[][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n7 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)arrf[n8][n7];
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "No check", arrf, n, n2, n3);
        }
    }

    public void putXZ(int n, int n2, int n3, double[][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n7 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)arrd[n8][n7];
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "No check", arrd, n, n2, n3);
        }
    }

    public void putYZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)(arrby[n8][n7] & 255);
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "No check", arrby, n, n2, n3);
        }
    }

    public void putYZ(int n, int n2, int n3, short[][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)(arrs[n8][n7] & 65535);
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "No check", arrs, n, n2, n3);
        }
    }

    public void putYZ(int n, int n2, int n3, float[][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)arrf[n8][n7];
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "No check", arrf, n, n2, n3);
        }
    }

    public void putYZ(int n, int n2, int n3, double[][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    ((short[])this.data[n3])[n4] = (short)arrd[n8][n7];
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "No check", arrd, n, n2, n3);
        }
    }

    public void putXYZ(int n, int n2, int n3, byte[][][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            int n7 = arrby[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrs[n4] = (short)(arrby[n9][i][n8] & 255);
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "No check", arrby, n, n2, n3);
        }
    }

    public void putXYZ(int n, int n2, int n3, short[][][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            int n7 = arrs[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs2 = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrs2[n4] = (short)(arrs[n9][i][n8] & 65535);
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "No check", arrs, n, n2, n3);
        }
    }

    public void putXYZ(int n, int n2, int n3, float[][][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            int n7 = arrf[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrs[n4] = (short)arrf[n9][i][n8];
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "No check", arrf, n, n2, n3);
        }
    }

    public void putXYZ(int n, int n2, int n3, double[][][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            int n7 = arrd[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrs[n4] = (short)arrd[n9][i][n8];
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "No check", arrd, n, n2, n3);
        }
    }

    public void getX(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        double[] arrd = new double[n4];
        this.getX(n, n2, n3, arrd);
        imageWare.putX(0, 0, 0, arrd);
    }

    public void getY(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeY();
        double[] arrd = new double[n4];
        this.getY(n, n2, n3, arrd);
        imageWare.putY(0, 0, 0, arrd);
    }

    public void getZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeZ();
        double[] arrd = new double[n4];
        this.getZ(n, n2, n3, arrd);
        imageWare.putZ(0, 0, 0, arrd);
    }

    public void getXY(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        int n5 = imageWare.getSizeY();
        double[][] arrd = new double[n4][n5];
        this.getXY(n, n2, n3, arrd);
        imageWare.putXY(0, 0, 0, arrd);
    }

    public void getXZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        int n5 = imageWare.getSizeZ();
        double[][] arrd = new double[n4][n5];
        this.getXZ(n, n2, n3, arrd);
        imageWare.putXZ(0, 0, 0, arrd);
    }

    public void getYZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeY();
        int n5 = imageWare.getSizeZ();
        double[][] arrd = new double[n4][n5];
        this.getYZ(n, n2, n3, arrd);
        imageWare.putYZ(0, 0, 0, arrd);
    }

    public void getXYZ(int n, int n2, int n3, ImageWare imageWare) {
        int n4 = imageWare.getSizeX();
        int n5 = imageWare.getSizeY();
        int n6 = imageWare.getSizeZ();
        double[][][] arrd = new double[n4][n5][n6];
        this.getXYZ(n, n2, n3, arrd);
        imageWare.putXYZ(0, 0, 0, arrd);
    }

    public void getX(int n, int n2, int n3, byte[] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrby[i] = (byte)(arrs[n4] & 65535);
                ++n4;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrby, n, n2, n3);
        }
    }

    public void getX(int n, int n2, int n3, short[] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            short[] arrs2 = (short[])this.data[n3];
            System.arraycopy(arrs2, n4, arrs, 0, n5);
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrs, n, n2, n3);
        }
    }

    public void getX(int n, int n2, int n3, float[] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrf[i] = arrs[n4] & 65535;
                ++n4;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrf, n, n2, n3);
        }
    }

    public void getX(int n, int n2, int n3, double[] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrd[i] = arrs[n4] & 65535;
                ++n4;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrd, n, n2, n3);
        }
    }

    public void getY(int n, int n2, int n3, byte[] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrby[i] = (byte)(arrs[n4] & 65535);
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrby, n, n2, n3);
        }
    }

    public void getY(int n, int n2, int n3, short[] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrs[i] = (short)(arrs2[n4] & 65535);
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrs, n, n2, n3);
        }
    }

    public void getY(int n, int n2, int n3, float[] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrf[i] = arrs[n4] & 65535;
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrf, n, n2, n3);
        }
    }

    public void getY(int n, int n2, int n3, double[] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n5; ++i) {
                arrd[i] = arrs[n4] & 65535;
                n4 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "No check", arrd, n, n2, n3);
        }
    }

    public void getZ(int n, int n2, int n3, byte[] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            for (int i = 0; i < n5; ++i) {
                arrby[i] = (byte)(((short[])this.data[n3])[n4] & 65535);
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "No check", arrby, n, n2, n3);
        }
    }

    public void getZ(int n, int n2, int n3, short[] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            for (int i = 0; i < n5; ++i) {
                arrs[i] = (short)(((short[])this.data[n3])[n4] & 65535);
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "No check", arrs, n, n2, n3);
        }
    }

    public void getZ(int n, int n2, int n3, float[] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            for (int i = 0; i < n5; ++i) {
                arrf[i] = ((short[])this.data[n3])[n4] & 65535;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "No check", arrf, n, n2, n3);
        }
    }

    public void getZ(int n, int n2, int n3, double[] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            for (int i = 0; i < n5; ++i) {
                arrd[i] = ((short[])this.data[n3])[n4] & 65535;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "No check", arrd, n, n2, n3);
        }
    }

    public void getXY(int n, int n2, int n3, byte[][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrby[n7][i] = (byte)(arrs[n4] & 65535);
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "No check", arrby, n, n2, n3);
        }
    }

    public void getXY(int n, int n2, int n3, short[][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrs[n7][i] = (short)(arrs2[n4] & 65535);
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "No check", arrs, n, n2, n3);
        }
    }

    public void getXY(int n, int n2, int n3, float[][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrf[n7][i] = arrs[n4] & 65535;
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "No check", arrf, n, n2, n3);
        }
    }

    public void getXY(int n, int n2, int n3, double[][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            short[] arrs = (short[])this.data[n3];
            for (int i = 0; i < n6; ++i) {
                n4 = n + (n2 + i) * this.nx;
                int n7 = 0;
                while (n7 < n5) {
                    arrd[n7][i] = arrs[n4] & 65535;
                    ++n7;
                    ++n4;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "No check", arrd, n, n2, n3);
        }
    }

    public void getXZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n2 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    arrby[n8][n7] = (byte)(((short[])this.data[n3])[n4] & 65535);
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "No check", arrby, n, n2, n3);
        }
    }

    public void getXZ(int n, int n2, int n3, short[][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n2 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    arrs[n8][n7] = (short)(((short[])this.data[n3])[n4] & 65535);
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "No check", arrs, n, n2, n3);
        }
    }

    public void getXZ(int n, int n2, int n3, float[][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n2 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    arrf[n8][n7] = ((short[])this.data[n3])[n4] & 65535;
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "No check", arrf, n, n2, n3);
        }
    }

    public void getXZ(int n, int n2, int n3, double[][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            int n7 = 0;
            while (n7 < n6) {
                n4 = n + n2 * this.nx;
                int n8 = 0;
                while (n8 < n5) {
                    arrd[n8][n7] = ((short[])this.data[n3])[n4] & 65535;
                    ++n8;
                    ++n4;
                }
                ++n7;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "No check", arrd, n, n2, n3);
        }
    }

    public void getYZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    arrby[n8][n7] = (byte)(((short[])this.data[n3])[n4] & 65535);
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "No check", arrby, n, n2, n3);
        }
    }

    public void getYZ(int n, int n2, int n3, short[][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    arrs[n8][n7] = (short)(((short[])this.data[n3])[n4] & 65535);
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "No check", arrs, n, n2, n3);
        }
    }

    public void getYZ(int n, int n2, int n3, float[][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    arrf[n8][n7] = ((short[])this.data[n3])[n4] & 65535;
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "No check", arrf, n, n2, n3);
        }
    }

    public void getYZ(int n, int n2, int n3, double[][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            int n7 = 0;
            while (n7 < n6) {
                int n8 = 0;
                while (n8 < n5) {
                    arrd[n8][n7] = ((short[])this.data[n3])[n4] & 65535;
                    ++n8;
                    n4 += this.nx;
                }
                ++n7;
                ++n3;
                n4 = n + this.nx * n2;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "No check", arrd, n, n2, n3);
        }
    }

    public void getXYZ(int n, int n2, int n3, byte[][][] arrby) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrby.length;
            int n6 = arrby[0].length;
            int n7 = arrby[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrby[n9][i][n8] = (byte)(arrs[n4] & 65535);
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "No check", arrby, n, n2, n3);
        }
    }

    public void getXYZ(int n, int n2, int n3, short[][][] arrs) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrs.length;
            int n6 = arrs[0].length;
            int n7 = arrs[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs2 = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrs[n9][i][n8] = (short)(arrs2[n4] & 65535);
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "No check", arrs, n, n2, n3);
        }
    }

    public void getXYZ(int n, int n2, int n3, float[][][] arrf) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrf.length;
            int n6 = arrf[0].length;
            int n7 = arrf[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrf[n9][i][n8] = arrs[n4] & 65535;
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "No check", arrf, n, n2, n3);
        }
    }

    public void getXYZ(int n, int n2, int n3, double[][][] arrd) {
        try {
            int n4 = n + n2 * this.nx;
            int n5 = arrd.length;
            int n6 = arrd[0].length;
            int n7 = arrd[0][0].length;
            int n8 = 0;
            while (n8 < n7) {
                short[] arrs = (short[])this.data[n3];
                for (int i = 0; i < n6; ++i) {
                    n4 = n + (i + n2) * this.nx;
                    int n9 = 0;
                    while (n9 < n5) {
                        arrd[n9][i][n8] = arrs[n4] & 65535;
                        ++n9;
                        ++n4;
                    }
                }
                ++n8;
                ++n3;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "No check", arrd, n, n2, n3);
        }
    }

    protected void throw_constructor() {
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to create a short imageware.\n-------------------------------------------------------\n");
    }

    protected void throw_constructor(int n, int n2, int n3) {
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to create a short imageware " + n + "," + n2 + "," + n3 + "].\n" + "-------------------------------------------------------\n");
    }

    protected void throw_get(String string, String string2, Object object, int n, int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        String string3 = " unknown type";
        if (object instanceof byte[]) {
            n4 = ((byte[])object).length;
            string3 = " 1D byte";
        } else if (object instanceof short[]) {
            n4 = ((short[])object).length;
            string3 = " 1D short";
        } else if (object instanceof float[]) {
            n4 = ((float[])object).length;
            string3 = " 1D float";
        } else if (object instanceof double[]) {
            n4 = ((double[])object).length;
            string3 = " 1D double";
        } else if (object instanceof byte[][]) {
            n4 = ((byte[][])object).length;
            n5 = ((byte[][])object)[0].length;
            string3 = " 2D byte";
        } else if (object instanceof short[][]) {
            n4 = ((short[][])object).length;
            n5 = ((short[][])object)[0].length;
            string3 = " 2D short";
        } else if (object instanceof float[][]) {
            n4 = ((float[][])object).length;
            n5 = ((float[][])object)[0].length;
            string3 = " 2D float";
        } else if (object instanceof double[][]) {
            n4 = ((double[][])object).length;
            n5 = ((double[][])object)[0].length;
            string3 = " 2D double";
        } else if (object instanceof byte[][][]) {
            n4 = ((byte[][][])object).length;
            n5 = ((byte[][][])object)[0].length;
            n6 = ((byte[][][])object)[0][0].length;
            string3 = " 3D byte";
        } else if (object instanceof short[][][]) {
            n4 = ((short[][][])object).length;
            n5 = ((short[][][])object)[0].length;
            n6 = ((short[][][])object)[0][0].length;
            string3 = " 3D short";
        } else if (object instanceof float[][][]) {
            n4 = ((float[][][])object).length;
            n5 = ((float[][][])object)[0].length;
            n6 = ((float[][][])object)[0][0].length;
            string3 = " 3D float";
        } else if (object instanceof double[][][]) {
            n4 = ((double[][][])object).length;
            n5 = ((double[][][])object)[0].length;
            n6 = ((double[][][])object)[0][0].length;
            string3 = " 3D double";
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to get a" + string3 + " buffer [" + (n4 == 0 ? "" : new StringBuilder().append("").append(n4).toString()) + (n5 == 0 ? "" : new StringBuilder().append(",").append(n5).toString()) + (n6 == 0 ? "" : new StringBuilder().append(",").append(n6).toString()) + "] \n" + "from the short imageware [" + this.nx + "," + this.ny + "," + this.nz + "]\n" + "at the position (" + n + "," + n2 + "," + n3 + ") in direction " + string + "\n" + "using " + string2 + ".\n" + "-------------------------------------------------------\n");
    }

    protected void throw_put(String string, String string2, Object object, int n, int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        String string3 = " unknown type";
        if (object instanceof byte[]) {
            n4 = ((byte[])object).length;
            string3 = " 1D byte";
        } else if (object instanceof short[]) {
            n4 = ((short[])object).length;
            string3 = " 1D short";
        } else if (object instanceof float[]) {
            n4 = ((float[])object).length;
            string3 = " 1D float";
        } else if (object instanceof double[]) {
            n4 = ((double[])object).length;
            string3 = " 1D double";
        } else if (object instanceof byte[][]) {
            n4 = ((byte[][])object).length;
            n5 = ((byte[][])object)[0].length;
            string3 = " 2D byte";
        } else if (object instanceof short[][]) {
            n4 = ((short[][])object).length;
            n5 = ((short[][])object)[0].length;
            string3 = " 2D short";
        } else if (object instanceof float[][]) {
            n4 = ((float[][])object).length;
            n5 = ((float[][])object)[0].length;
            string3 = " 2D float";
        } else if (object instanceof double[][]) {
            n4 = ((double[][])object).length;
            n5 = ((double[][])object)[0].length;
            string3 = " 2D double";
        } else if (object instanceof byte[][][]) {
            n4 = ((byte[][][])object).length;
            n5 = ((byte[][][])object)[0].length;
            n6 = ((byte[][][])object)[0][0].length;
            string3 = " 3D byte";
        } else if (object instanceof short[][][]) {
            n4 = ((short[][][])object).length;
            n5 = ((short[][][])object)[0].length;
            n6 = ((short[][][])object)[0][0].length;
            string3 = " 3D short";
        } else if (object instanceof float[][][]) {
            n4 = ((float[][][])object).length;
            n5 = ((float[][][])object)[0].length;
            n6 = ((float[][][])object)[0][0].length;
            string3 = " 3D float";
        } else if (object instanceof double[][][]) {
            n4 = ((double[][][])object).length;
            n5 = ((double[][][])object)[0].length;
            n6 = ((double[][][])object)[0][0].length;
            string3 = " 3D double";
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to put a" + string3 + " buffer [" + (n4 == 0 ? "" : new StringBuilder().append("").append(n4).toString()) + (n5 == 0 ? "" : new StringBuilder().append(",").append(n5).toString()) + (n6 == 0 ? "" : new StringBuilder().append(",").append(n6).toString()) + "] \n" + "into the short imageware [" + this.nx + "," + this.ny + "," + this.nz + "]\n" + "at the position (" + n + "," + n2 + "," + n3 + ") in direction " + string + "\n" + "using " + string2 + ".\n" + "-------------------------------------------------------\n");
    }

    public Object[] getVolume() {
        return this.data;
    }

    public byte[] getSliceByte(int n) {
        return null;
    }

    public short[] getSliceShort(int n) {
        return (short[])this.data[n];
    }

    public float[] getSliceFloat(int n) {
        return null;
    }

    public double[] getSliceDouble(int n) {
        return null;
    }

    private void allocate() {
        try {
            this.data = new Object[this.nz];
            this.nxy = this.nx * this.ny;
            for (int i = 0; i < this.nz; ++i) {
                this.data[i] = new short[this.nxy];
            }
        }
        catch (Exception var1_2) {
            this.throw_constructor(this.nx, this.ny, this.nz);
        }
    }
}

