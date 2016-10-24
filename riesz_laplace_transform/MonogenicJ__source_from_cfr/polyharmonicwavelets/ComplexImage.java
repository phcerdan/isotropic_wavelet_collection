/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package polyharmonicwavelets;

import fft.FFT1D;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import imageware.ImageWare;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public class ComplexImage {
    public double[] real;
    public double[] imag;
    public int nx;
    public int ny;
    public int nxy;
    private final double PI2 = 6.283185307179586;
    private final double sqrt2 = Math.sqrt(2.0);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ComplexImage(ImageWare image) {
        this.nx = image.getWidth();
        this.ny = image.getHeight();
        int nc = image.getSizeZ();
        this.nxy = this.nx * this.ny;
        this.real = new double[this.nxy];
        this.imag = new double[this.nxy];
        if (image.getType() == 1) {
            byte[] pixels = image.getSliceByte(0);
            for (int k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k] & 255;
            }
            if (nc != 2) return;
            byte[] ipixels = image.getSliceByte(1);
            int k2 = 0;
            while (k2 < this.nx * this.ny) {
                this.imag[k2] = ipixels[k2] & 255;
                ++k2;
            }
            return;
        }
        if (image.getType() == 2) {
            short[] pixels = image.getSliceShort(0);
            for (int k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
            if (nc != 2) return;
            short[] ipixels = image.getSliceShort(1);
            int k3 = 0;
            while (k3 < this.nx * this.ny) {
                this.imag[k3] = ipixels[k3];
                ++k3;
            }
            return;
        }
        if (image.getType() == 3) {
            float[] pixels = image.getSliceFloat(0);
            for (int k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
            if (nc != 2) return;
            float[] ipixels = image.getSliceFloat(1);
            int k4 = 0;
            while (k4 < this.nx * this.ny) {
                this.imag[k4] = ipixels[k4];
                ++k4;
            }
            return;
        }
        if (image.getType() != 4) return;
        double[] pixels = image.getSliceDouble(0);
        for (int k = 0; k < this.nx * this.ny; ++k) {
            this.real[k] = pixels[k];
        }
        if (nc != 2) return;
        double[] ipixels = image.getSliceDouble(1);
        int k5 = 0;
        while (k5 < this.nx * this.ny) {
            this.imag[k5] = ipixels[k5];
            ++k5;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ComplexImage(ImageWare inr, ImageWare ini) {
        int k;
        byte[] pixels;
        this.nx = inr.getWidth();
        this.ny = inr.getHeight();
        this.nxy = this.nx * this.ny;
        this.real = new double[this.nxy];
        this.imag = new double[this.nxy];
        if (inr.getType() == 1) {
            pixels = inr.getSliceByte(0);
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k] & 255;
            }
        } else if (inr.getType() == 2) {
            pixels = inr.getSliceShort(0);
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
        } else if (inr.getType() == 3) {
            pixels = inr.getSliceFloat(0);
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
        } else if (inr.getType() == 4) {
            pixels = inr.getSliceDouble(0);
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
        }
        if (ini.getType() == 1) {
            pixels = ini.getSliceByte(0);
            k = 0;
            while (k < this.nx * this.ny) {
                this.imag[k] = pixels[k] & 255;
                ++k;
            }
            return;
        }
        if (ini.getType() == 2) {
            pixels = ini.getSliceShort(0);
            k = 0;
            while (k < this.nx * this.ny) {
                this.imag[k] = pixels[k];
                ++k;
            }
            return;
        }
        if (ini.getType() == 3) {
            pixels = ini.getSliceFloat(0);
            k = 0;
            while (k < this.nx * this.ny) {
                this.imag[k] = pixels[k];
                ++k;
            }
            return;
        }
        if (ini.getType() != 4) return;
        pixels = ini.getSliceDouble(0);
        k = 0;
        while (k < this.nx * this.ny) {
            this.imag[k] = pixels[k];
            ++k;
        }
    }

    public ComplexImage(ImagePlus imp) {
        byte[] pixels;
        int k;
        this.nx = imp.getWidth();
        this.ny = imp.getHeight();
        this.nxy = this.nx * this.ny;
        this.real = new double[this.nxy];
        this.imag = new double[this.nxy];
        if (imp.getType() == 0) {
            pixels = (byte[])imp.getProcessor().getPixels();
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k] & 255;
            }
        }
        if (imp.getType() == 1) {
            pixels = (short[])imp.getProcessor().getPixels();
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
        }
        if (imp.getType() == 2) {
            pixels = (float[])imp.getProcessor().getPixels();
            for (k = 0; k < this.nx * this.ny; ++k) {
                this.real[k] = pixels[k];
            }
        }
    }

    public ComplexImage(int sizex, int sizey) {
        this.nx = sizex;
        this.ny = sizey;
        this.nxy = this.nx * this.ny;
        this.real = new double[this.nxy];
        this.imag = new double[this.nxy];
    }

    public ComplexImage(int sizex, int sizey, double[] real) {
        this.nx = sizex;
        this.ny = sizey;
        this.nxy = this.nx * this.ny;
        this.real = real;
        this.imag = null;
    }

    public ComplexImage(int sizex, int sizey, double[] real, double[] imag) {
        this.nx = sizex;
        this.ny = sizey;
        this.nxy = this.nx * this.ny;
        this.real = real;
        this.imag = imag;
    }

    public ComplexImage(int sizex, int sizey, boolean r) {
        this.nx = sizex;
        this.ny = sizey;
        this.nxy = this.nx * this.ny;
        this.real = new double[this.nxy];
        this.imag = null;
        if (!r) {
            this.imag = new double[this.nxy];
        }
    }

    public void cropQuincunx(int J) {
        int j;
        int ind = 1;
        int lx = this.nx;
        int ly = this.ny;
        for (j = 0; j < J; ++j) {
            if (ind == 1) {
                lx /= 2;
            } else {
                ly /= 2;
            }
            ind = 1 - ind;
        }
        ind = 1 - ind;
        for (j = 0; j < J; ++j) {
            if (ind == 1) {
                lx *= 2;
            } else {
                ly *= 2;
            }
            ind = 1 - ind;
        }
        if (lx != this.nx || ly != this.ny) {
            System.out.println("Image has been cropped");
            int dx = (this.nx - lx) / 2;
            int dy = (this.ny - ly) / 2;
            ComplexImage temp = this.getSubimage(dx, lx - 1 + dx, dy, ly - 1 + dy);
            this.nx = temp.nx;
            this.ny = temp.ny;
            this.nxy = temp.nxy;
            System.arraycopy(temp.real, 0, this.real, 0, this.nxy);
            System.arraycopy(temp.imag, 0, this.imag, 0, this.nxy);
            this.showReal("Cropped image");
        }
    }

    public void cropDyadic(int J) {
        int j;
        int lx = this.nx;
        int ly = this.ny;
        for (j = 0; j < J; ++j) {
            lx /= 2;
            ly /= 2;
        }
        for (j = 0; j < J; ++j) {
            lx *= 2;
            ly *= 2;
        }
        if (lx != this.nx || ly != this.ny) {
            System.out.println("Image has been cropped");
            int dx = (this.nx - lx) / 2;
            int dy = (this.ny - ly) / 2;
            ComplexImage temp = this.getSubimage(dx, lx - 1 + dx, dy, ly - 1 + dy);
            this.nx = temp.nx;
            this.ny = temp.ny;
            this.nxy = temp.nxy;
            System.arraycopy(temp.real, 0, this.real, 0, this.nxy);
            System.arraycopy(temp.imag, 0, this.imag, 0, this.nxy);
            this.showReal("Cropped image");
        }
    }

    public int noIterations(String type) {
        int noit = 0;
        int dx = this.nx;
        int dy = this.ny;
        if (type == "Dyadic") {
            while (dx % 2 == 0 && dy % 2 == 0) {
                ++noit;
                dx /= 2;
                dy /= 2;
            }
        }
        if (type == "Quincunx") {
            while (dx % 2 == 0 && dy % 2 == 0) {
                noit += 2;
                dx /= 2;
                dy /= 2;
            }
            if (dx % 2 == 0) {
                ++noit;
            }
        }
        return noit;
    }

    public void croptoSize(int lx, int ly) {
        if (lx != this.nx || ly != this.ny) {
            int dx = (this.nx - lx) / 2;
            int dy = (this.ny - ly) / 2;
            ComplexImage temp = this.getSubimage(dx, lx - 1 + dx, dy, ly - 1 + dy);
            this.nx = temp.nx;
            this.ny = temp.ny;
            this.nxy = temp.nxy;
            System.arraycopy(temp.real, 0, this.real, 0, this.nxy);
            System.arraycopy(temp.imag, 0, this.imag, 0, this.nxy);
        }
    }

    public void zone() {
        int size = this.nx;
        int size2 = size / 2;
        double c = 3.141592653589793 / this.sqrt2 / (double)this.nx;
        int ind = 0;
        for (int y1 = - size2; y1 < size2; ++y1) {
            for (int x1 = - size2; x1 < size2; ++x1) {
                this.real[ind++] = (1.0 + Math.cos(c * (double)(y1 * y1 + x1 * x1))) * 127.5;
            }
        }
    }

    public void makeNoise() {
        Random random = new Random();
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = random.nextGaussian();
        }
    }

    public void makeGaussianSpots() {
        int k;
        Random random = new Random();
        int N = random.nextInt(12) + 9;
        int[] X0 = new int[N];
        int[] Y0 = new int[N];
        double[] sigma = new double[N];
        for (k = 0; k < N; ++k) {
            X0[k] = random.nextInt(this.nx - 60) + 30;
            Y0[k] = random.nextInt(this.ny - 60) + 30;
            sigma[k] = random.nextDouble() * 5.0 + 1.0;
        }
        for (k = 0; k < N; ++k) {
            double s2 = 2.0 * sigma[k] * sigma[k];
            double s1 = 0.15915494309189535;
            for (int y = -30; y < 30; ++y) {
                int dy = this.nx * y;
                for (int x = -30; x < 30; ++x) {
                    int p0 = Y0[k] * this.nx + X0[k];
                    double s = s1 * Math.exp((- (double)(x * x + y * y)) / s2);
                    double[] arrd = this.real;
                    int n = p0 + dy + x;
                    arrd[n] = arrd[n] + s;
                }
            }
        }
    }

    public void showReal(String text) {
        float[][] re = new float[this.nx][this.ny];
        for (int k = 0; k < this.nx; ++k) {
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k;
                re[k][l] = (float)this.real[index];
            }
        }
        FloatProcessor fp = new FloatProcessor(re);
        ImagePlus imp = new ImagePlus(text, (ImageProcessor)fp);
        imp.show();
    }

    public void showImag(String text) {
        float[][] imaginary = new float[this.nx][this.ny];
        for (int k = 0; k < this.nx; ++k) {
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k;
                imaginary[k][l] = (float)this.imag[index];
            }
        }
        FloatProcessor fp = new FloatProcessor(imaginary);
        ImagePlus imp = new ImagePlus(text, (ImageProcessor)fp);
        imp.show();
    }

    public void showModulus(String text) {
        float[][] mod = new float[this.nx][this.ny];
        for (int k = 0; k < this.nx; ++k) {
            int index;
            int l;
            if (this.imag == null) {
                for (l = 0; l < this.ny; ++l) {
                    index = l * this.nx + k;
                    mod[k][l] = (float)Math.abs(this.real[index]);
                }
                continue;
            }
            for (l = 0; l < this.ny; ++l) {
                index = l * this.nx + k;
                mod[k][l] = (float)Math.sqrt(this.real[index] * this.real[index] + this.imag[index] * this.imag[index]);
            }
        }
        FloatProcessor fp = new FloatProcessor(mod);
        ImagePlus imp = new ImagePlus(text, (ImageProcessor)fp);
        imp.show();
    }

    public void displayComplexStack(String text) {
        double color = -255.0;
        ComplexImage[] out = new ComplexImage[2];
        ImageStack stack = new ImageStack(this.nx, this.ny);
        float[][] ima = new float[this.nx][this.ny];
        for (int k = 0; k < this.nx; ++k) {
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k;
                ima[k][l] = (float)this.real[index];
            }
        }
        FloatProcessor fp = new FloatProcessor(ima);
        fp.setValue(color);
        stack.addSlice("Real part", (ImageProcessor)fp);
        for (int k2 = 0; k2 < this.nx; ++k2) {
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k2;
                ima[k2][l] = (float)this.imag[index];
            }
        }
        fp = new FloatProcessor(ima);
        fp.setValue(color);
        stack.addSlice("Imaginary part", (ImageProcessor)fp);
        ImagePlus imp = new ImagePlus(text, stack);
        imp.show();
    }

    public static void displayStack(ComplexImage[] array, String text) {
        int nx = array[0].nx;
        int ny = array[0].ny;
        ImageStack stack = new ImageStack(nx, ny);
        float[][] ima = new float[nx][ny];
        for (int j = 0; j < array.length; ++j) {
            float[][] im = new float[nx][ny];
            for (int k = 0; k < nx; ++k) {
                for (int l = 0; l < ny; ++l) {
                    int index = l * nx + k;
                    ima[k][l] = (float)array[j].real[index];
                }
            }
            FloatProcessor fp = new FloatProcessor(ima);
            stack.addSlice("j", (ImageProcessor)fp);
        }
        ImagePlus imp = new ImagePlus(text, stack);
        imp.show();
    }

    public static void displayStackImag(ComplexImage[] array, String text) {
        int nx = array[0].nx;
        int ny = array[0].ny;
        ImageStack stack = new ImageStack(nx, ny);
        float[][] ima = new float[nx][ny];
        for (int j = 0; j < array.length; ++j) {
            float[][] im = new float[nx][ny];
            for (int k = 0; k < nx; ++k) {
                for (int l = 0; l < ny; ++l) {
                    int index = l * nx + k;
                    ima[k][l] = (float)array[j].imag[index];
                }
            }
            FloatProcessor fp = new FloatProcessor(ima);
            stack.addSlice("j", (ImageProcessor)fp);
        }
        ImagePlus imp = new ImagePlus(text, stack);
        imp.show();
    }

    public void displayValues(String text) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("0.0000");
        System.out.println(" ");
        System.out.println(text);
        System.out.println("sizenxnx=" + this.nx);
        System.out.println("sizeny=" + this.ny);
        for (int i = 0; i < this.ny; ++i) {
            for (int j = 0; j < this.nx; ++j) {
                System.out.print("  " + decimalFormat.format(this.real[i * this.nx + j]) + "+" + decimalFormat.format(this.imag[i * this.nx + j]) + "i");
            }
            System.out.println(" ");
        }
    }

    public void putZeros() {
        if (this.imag == null) {
            for (int i = 0; i < this.nxy; ++i) {
                this.real[i] = 0.0;
            }
        } else {
            for (int i = 0; i < this.nxy; ++i) {
                this.imag[i] = 0.0;
                this.real[i] = 0.0;
            }
        }
    }

    public void setRealtoZero() {
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = 0.0;
        }
    }

    public void setImagtoZero() {
        for (int k = 0; k < this.nxy; ++k) {
            this.imag[k] = 0.0;
        }
    }

    public void settoConstant(double r, double i) {
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = r;
            this.imag[k] = i;
        }
    }

    public void settoConstant(double r) {
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = r;
        }
    }

    public ComplexImage getRow(int y) {
        ComplexImage row = new ComplexImage(this.nx, 1, this.imag == null);
        System.arraycopy(this.real, y * this.nx, row.real, 0, this.nx);
        if (this.imag != null) {
            System.arraycopy(this.imag, y * this.nx, row.imag, 0, this.nx);
        }
        return row;
    }

    public void getRowContent(int y, ComplexImage image) {
        this.nx = image.nx;
        this.ny = 1;
        int s = y * this.nx;
        System.arraycopy(image.real, s, this.real, 0, image.nx);
        if (this.imag != null && image.imag != null) {
            System.arraycopy(image.imag, s, this.imag, 0, image.nx);
        }
    }

    public void putRow(int y, ComplexImage row) {
        System.arraycopy(row.real, 0, this.real, y * this.nx, this.nx);
        if (this.imag != null && row.imag != null) {
            System.arraycopy(row.imag, 0, this.imag, y * this.nx, this.nx);
        }
    }

    public ComplexImage getColumn(int x) {
        ComplexImage column = new ComplexImage(this.ny, 1, this.imag == null);
        if (this.imag == null) {
            int n = x;
            for (int y = 0; y < this.ny; ++y) {
                column.real[y] = this.real[n];
                n += this.nx;
            }
        } else {
            int n = x;
            for (int y = 0; y < this.ny; ++y) {
                column.real[y] = this.real[n];
                column.imag[y] = this.imag[n];
                n += this.nx;
            }
        }
        return column;
    }

    public void getColumnContent(int x, ComplexImage image) {
        this.nx = 1;
        this.ny = image.ny;
        if (image.imag == null || this.imag == null) {
            int n = x;
            for (int y = 0; y < image.ny; ++y) {
                this.real[y] = image.real[n];
                n += image.nx;
            }
        } else {
            int n = x;
            for (int y = 0; y < image.ny; ++y) {
                this.real[y] = image.real[n];
                this.imag[y] = image.imag[n];
                n += image.nx;
            }
        }
    }

    public void putColumn(int x, ComplexImage column) {
        if (this.imag != null && column.imag != null) {
            int n = x;
            for (int y = 0; y < this.ny; ++y) {
                this.real[n] = column.real[y];
                this.imag[n] = column.imag[y];
                n += this.nx;
            }
        } else {
            int n = x;
            for (int y = 0; y < this.ny; ++y) {
                this.real[n] = column.real[y];
                n += this.nx;
            }
        }
    }

    public ComplexImage getSubimage(int x1, int x2, int y1, int y2) {
        ComplexImage sub = new ComplexImage(x2 - x1 + 1, y2 - y1 + 1, this.imag == null);
        int d = x1 + y1 * this.nx;
        if (this.imag == null) {
            for (int y = 0; y < sub.ny; ++y) {
                System.arraycopy(this.real, d + y * this.nx, sub.real, y * sub.nx, sub.nx);
            }
        } else {
            for (int y = 0; y < sub.ny; ++y) {
                System.arraycopy(this.real, d + y * this.nx, sub.real, y * sub.nx, sub.nx);
                System.arraycopy(this.imag, d + y * this.nx, sub.imag, y * sub.nx, sub.nx);
            }
        }
        return sub;
    }

    public void getSubimageContent(int x1, int x2, int y1, int y2, ComplexImage image) {
        int d = x1 + y1 * image.nx;
        this.nx = x2 - x1 + 1;
        this.ny = y2 - y1 + 1;
        this.nxy = this.nx * this.ny;
        if (this.imag == null || image.imag == null) {
            for (int y = 0; y < this.ny; ++y) {
                System.arraycopy(image.real, d + y * image.nx, this.real, y * this.nx, this.nx);
            }
        } else {
            for (int y = 0; y < this.ny; ++y) {
                System.arraycopy(image.real, d + y * image.nx, this.real, y * this.nx, this.nx);
                System.arraycopy(image.imag, d + y * image.nx, this.imag, y * this.nx, this.nx);
            }
        }
    }

    public void putSubimage(int x1, int y1, ComplexImage sub) {
        int d = x1 + y1 * this.nx;
        if (sub.imag == null) {
            for (int y = 0; y < sub.ny; ++y) {
                System.arraycopy(sub.real, y * sub.nx, this.real, d + y * this.nx, sub.nx);
            }
        } else {
            for (int y = 0; y < sub.ny; ++y) {
                System.arraycopy(sub.real, y * sub.nx, this.real, d + y * this.nx, sub.nx);
                System.arraycopy(sub.imag, y * sub.nx, this.imag, d + y * this.nx, sub.nx);
            }
        }
    }

    public ComplexImage copyImage() {
        ComplexImage temp = null;
        if (this.imag == null) {
            temp = new ComplexImage(this.nx, this.ny, true);
            System.arraycopy(this.real, 0, temp.real, 0, this.nxy);
        } else {
            temp = new ComplexImage(this.nx, this.ny);
            System.arraycopy(this.real, 0, temp.real, 0, this.nxy);
            System.arraycopy(this.imag, 0, temp.imag, 0, this.nxy);
        }
        return temp;
    }

    public void copyImageContent(ComplexImage original) {
        this.nx = original.nx;
        this.ny = original.ny;
        this.nxy = original.nxy;
        if (original.imag == null || this.imag == null) {
            System.arraycopy(original.real, 0, this.real, 0, this.nxy);
        } else {
            System.arraycopy(original.real, 0, this.real, 0, this.nxy);
            System.arraycopy(original.imag, 0, this.imag, 0, this.nxy);
        }
    }

    public void squareModulus() {
        if (this.imag == null) {
            for (int i = 0; i < this.nxy; ++i) {
                this.real[i] = this.real[i] * this.real[i];
            }
        } else {
            for (int i = 0; i < this.nxy; ++i) {
                this.real[i] = this.real[i] * this.real[i] + this.imag[i] * this.imag[i];
                this.imag[i] = 0.0;
            }
        }
    }

    public void modulus() {
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = Math.sqrt(this.real[k] * this.real[k] + this.imag[k] * this.imag[k]);
            this.imag[k] = 0.0;
        }
    }

    public void phase() {
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = Math.atan(this.imag[k] / this.real[k]);
            if (this.real[k] < 0.0) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] + 3.141592653589793;
            }
            this.imag[k] = 0.0;
        }
    }

    public void add(ComplexImage im) {
        if (im.imag != null) {
            if (this.imag == null) {
                this.imag = new double[this.nxy];
            }
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] + im.real[k];
                double[] arrd2 = this.imag;
                int n2 = k;
                arrd2[n2] = arrd2[n2] + im.imag[k];
            }
        } else {
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] + im.real[k];
            }
        }
    }

    public void add(double d) {
        int k = 0;
        while (k < this.nxy) {
            double[] arrd = this.real;
            int n = k++;
            arrd[n] = arrd[n] + d;
        }
    }

    public void subtract(ComplexImage im) {
        if (im.imag != null) {
            if (this.imag == null) {
                this.imag = new double[this.nxy];
            }
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] - im.real[k];
                double[] arrd2 = this.imag;
                int n2 = k;
                arrd2[n2] = arrd2[n2] - im.imag[k];
            }
        } else {
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] - im.real[k];
            }
        }
    }

    public void conj() {
        if (this.imag != null) {
            for (int k = 0; k < this.nxy; ++k) {
                this.imag[k] = - this.imag[k];
            }
        }
    }

    public void multiply(double constant) {
        if (this.imag == null) {
            int k = 0;
            while (k < this.nxy) {
                double[] arrd = this.real;
                int n = k++;
                arrd[n] = arrd[n] * constant;
            }
        } else {
            int k = 0;
            while (k < this.nxy) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] * constant;
                double[] arrd2 = this.imag;
                int n2 = k++;
                arrd2[n2] = arrd2[n2] * constant;
            }
        }
    }

    public void multiply(double re, double im) {
        for (int k = 0; k < this.nxy; ++k) {
            double r = this.real[k];
            this.real[k] = re * this.real[k] - im * this.imag[k];
            this.imag[k] = re * this.imag[k] + im * r;
        }
    }

    public void multiply(ComplexImage im) {
        if (im.imag != null) {
            if (this.imag == null) {
                this.imag = new double[this.nxy];
            }
            for (int k = 0; k < this.nxy; ++k) {
                double re = this.real[k];
                this.real[k] = im.real[k] * re - im.imag[k] * this.imag[k];
                this.imag[k] = im.real[k] * this.imag[k] + im.imag[k] * re;
            }
        } else if (this.imag == null) {
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] * im.real[k];
            }
        } else {
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] * im.real[k];
                double[] arrd2 = this.imag;
                int n2 = k;
                arrd2[n2] = arrd2[n2] * im.real[k];
            }
        }
    }

    public void multiplyCircular(ComplexImage im, int l) {
        int l2 = l * l;
        if (im.imag != null) {
            if (this.imag == null) {
                this.imag = new double[this.nxy];
            }
            for (int y = 0; y < this.ny; ++y) {
                int l2y = l * y % im.ny * im.nx;
                for (int x = 0; x < this.nx; ++x) {
                    int k = this.nx * y + x;
                    int t = l2y + l * x % im.nx;
                    double re = this.real[k];
                    double ima = this.imag[k];
                    this.real[k] = im.real[t] * re - im.imag[t] * ima;
                    this.imag[k] = im.real[t] * ima + im.imag[t] * re;
                }
            }
        } else if (this.imag == null) {
            for (int y = 0; y < this.ny; ++y) {
                int l2y = l * y % im.ny * im.nx;
                for (int x = 0; x < this.nx; ++x) {
                    int k = this.nx * y + x;
                    int t = l2y + l * x % im.nx;
                    double[] arrd = this.real;
                    int n = k;
                    arrd[n] = arrd[n] * im.real[t];
                }
            }
        } else {
            for (int y = 0; y < this.ny; ++y) {
                int l2y = l * y % im.ny * im.nx;
                for (int x = 0; x < this.nx; ++x) {
                    int k = this.nx * y + x;
                    int t = l2y + l * x % im.nx;
                    double re = this.real[k];
                    double ima = this.imag[k];
                    double[] arrd = this.real;
                    int n = k;
                    arrd[n] = arrd[n] * im.real[t];
                    double[] arrd2 = this.imag;
                    int n2 = k;
                    arrd2[n2] = arrd2[n2] * im.real[t];
                }
            }
        }
    }

    public void multiply(ComplexImage im, int l) {
        if (l == 1) {
            this.multiply(im);
        } else {
            int l2 = l * l;
            int d = l * im.nx;
            if (im.imag != null) {
                if (this.imag == null) {
                    this.imag = new double[this.nxy];
                }
                int y = 0;
                int l2y = 0;
                while (y < this.ny) {
                    int t = l2y;
                    int end = this.nx * y + this.nx;
                    for (int k = this.nx * y; k < end; ++k) {
                        double re = this.real[k];
                        this.real[k] = im.real[t] * re - im.imag[t] * this.imag[k];
                        this.imag[k] = im.real[t] * this.imag[k] + im.imag[t] * re;
                        t += l;
                    }
                    ++y;
                    l2y += d;
                }
            } else if (this.imag == null) {
                int y = 0;
                int l2y = 0;
                while (y < this.ny) {
                    int t = l2y;
                    int k = this.nx * y;
                    int end = this.nx * y + this.nx;
                    while (k < end) {
                        double[] arrd = this.real;
                        int n = k++;
                        arrd[n] = arrd[n] * im.real[t];
                        t += l;
                    }
                    ++y;
                    l2y += d;
                }
            } else {
                int y = 0;
                int l2y = 0;
                while (y < this.ny) {
                    int t = l2y;
                    int k = this.nx * y;
                    int end = this.nx * y + this.nx;
                    while (k < end) {
                        double[] arrd = this.real;
                        int n = k;
                        arrd[n] = arrd[n] * im.real[t];
                        double[] arrd2 = this.imag;
                        int n2 = k++;
                        arrd2[n2] = arrd2[n2] * im.real[t];
                        t += l;
                    }
                    ++y;
                    l2y += d;
                }
            }
        }
    }

    public void divide(ComplexImage im, double cr, double cim) {
        double eps = 1.0E-30;
        if (im.imag != null) {
            if (this.imag == null) {
                this.imag = new double[this.nxy];
            }
            for (int k = 0; k < this.nxy; ++k) {
                if (Math.abs(im.real[k]) < eps && Math.abs(im.imag[k]) < eps) {
                    this.real[k] = cr;
                    this.imag[k] = cim;
                    continue;
                }
                double rea = this.real[k];
                double ima = this.imag[k];
                this.real[k] = (rea * im.real[k] + ima * im.imag[k]) / (im.real[k] * im.real[k] + im.imag[k] * im.imag[k]);
                this.imag[k] = (ima * im.real[k] - rea * im.imag[k]) / (im.real[k] * im.real[k] + im.imag[k] * im.imag[k]);
            }
        } else if (this.imag == null) {
            for (int k = 0; k < this.nxy; ++k) {
                if (Math.abs(im.real[k]) < eps) {
                    this.real[k] = cr;
                    continue;
                }
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] / im.real[k];
            }
        } else {
            for (int k = 0; k < this.nxy; ++k) {
                if (Math.abs(im.real[k]) < eps) {
                    this.real[k] = cr;
                    this.imag[k] = cim;
                    continue;
                }
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] / im.real[k];
                double[] arrd2 = this.imag;
                int n2 = k;
                arrd2[n2] = arrd2[n2] / im.real[k];
            }
        }
    }

    public void divide(ComplexImage im) {
        if (im.imag != null) {
            if (this.imag == null) {
                this.imag = new double[this.nxy];
            }
            for (int k = 0; k < this.nxy; ++k) {
                double rea = this.real[k];
                double d = im.real[k] * im.real[k] + im.imag[k] * im.imag[k];
                this.real[k] = (rea * im.real[k] + this.imag[k] * im.imag[k]) / d;
                this.imag[k] = (this.imag[k] * im.real[k] - rea * im.imag[k]) / d;
            }
        } else if (this.imag == null) {
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] / im.real[k];
            }
        } else {
            for (int k = 0; k < this.nxy; ++k) {
                double[] arrd = this.real;
                int n = k;
                arrd[n] = arrd[n] / im.real[k];
                double[] arrd2 = this.imag;
                int n2 = k;
                arrd2[n2] = arrd2[n2] / im.real[k];
            }
        }
    }

    public void rootReal() {
        for (int k = 0; k < this.nxy; ++k) {
            this.real[k] = Math.sqrt(this.real[k]);
        }
    }

    public double median() {
        ComplexImage temp = this.copyImage();
        Arrays.sort(temp.real);
        double m = temp.real[this.nxy / 2];
        return m;
    }

    public double mad() {
        double m = this.median();
        ComplexImage temp = this.copyImage();
        temp.add(- m);
        for (int x = 0; x < this.nxy; ++x) {
            temp.real[x] = Math.abs(temp.real[x]);
        }
        double md = temp.median();
        return md;
    }

    public void powerReal(double exp) {
        for (int i = 0; i < this.nxy; ++i) {
            this.real[i] = Math.pow(this.real[i], exp);
        }
    }

    public double meanReal() {
        double d = 0.0;
        for (int k = 0; k < this.nxy; ++k) {
            d += this.real[k];
        }
        return d /= (double)this.nxy;
    }

    public double meanMask(int[] ma, int val) {
        double cnt = 0.0;
        double s = 0.0;
        for (int k = 0; k < this.nxy; ++k) {
            if (ma[k] != val) continue;
            cnt += 1.0;
            s += this.real[k];
        }
        return s /= cnt;
    }

    public double meanModulus() {
        double d = 0.0;
        if (this.imag == null) {
            for (int k = 0; k < this.nxy; ++k) {
                d += Math.abs(this.real[k]);
            }
        } else {
            for (int k = 0; k < this.nxy; ++k) {
                d += Math.pow(this.real[k] * this.real[k] + this.imag[k] * this.imag[k], 0.5);
            }
        }
        return d /= (double)this.nxy;
    }

    public double sumReal() {
        double d = 0.0;
        for (int k = 0; k < this.nxy; ++k) {
            d += this.real[k];
        }
        return d;
    }

    public double maxAbsReal() {
        double max = this.real[0];
        double min = this.real[0];
        for (int k = 0; k < this.nxy; ++k) {
            if (this.real[k] > max) {
                max = this.real[k];
                continue;
            }
            if (this.real[k] >= min) continue;
            min = this.real[k];
        }
        max = Math.abs(max);
        if ((min = Math.abs(min)) > max) {
            max = min;
        }
        return max;
    }

    public double max() {
        double m = this.real[0];
        for (int k = 0; k < this.nxy; ++k) {
            if (this.real[k] <= m) continue;
            m = this.real[k];
        }
        return m;
    }

    public double min() {
        double m = this.real[0];
        for (int k = 0; k < this.nxy; ++k) {
            if (this.real[k] >= m) continue;
            m = this.real[k];
        }
        return m;
    }

    public double deviation() {
        double m = this.meanReal();
        double s = 0.0;
        for (int k = 0; k < this.nxy; ++k) {
            double d = this.real[k] - m;
            s += d * d;
        }
        s /= (double)this.nxy;
        s = Math.sqrt(s);
        return s;
    }

    public void FFT2D() {
        FFT1D FFTrow = new FFT1D(this.nx);
        ComplexImage row = new ComplexImage(this.nx, 1);
        for (int y = 0; y < this.ny; ++y) {
            row.getRowContent(y, this);
            FFTrow.transform(row.real, row.imag, this.nx, 0);
            this.putRow(y, row);
        }
        ComplexImage column = new ComplexImage(1, this.ny);
        FFT1D FFTcolumn = new FFT1D(this.ny);
        for (int x = 0; x < this.nx; ++x) {
            column.getColumnContent(x, this);
            FFTcolumn.transform(column.real, column.imag, this.ny, 0);
            this.putColumn(x, column);
        }
    }

    public void iFFT2D() {
        ComplexImage row = new ComplexImage(this.nx, 1);
        FFT1D FFTrow = new FFT1D(this.nx);
        for (int y = 0; y < this.ny; ++y) {
            row.getRowContent(y, this);
            FFTrow.inverse(row.real, row.imag, this.nx, 0);
            this.putRow(y, row);
        }
        ComplexImage column = new ComplexImage(1, this.ny);
        FFT1D FFTcolumn = new FFT1D(this.ny);
        for (int x = 0; x < this.nx; ++x) {
            column.getColumnContent(x, this);
            FFTcolumn.inverse(column.real, column.imag, this.ny, 0);
            this.putColumn(x, column);
        }
    }

    public void shift() {
        int nxy2 = this.nxy / 2;
        int nx2 = this.nx / 2;
        if (this.imag == null) {
            for (int y = 0; y < this.ny / 2; ++y) {
                int ynx = this.nx * y;
                for (int x = 0; x < nx2; ++x) {
                    int q1 = ynx + x;
                    int q2 = q1 + nx2;
                    int q4 = q1 + nxy2;
                    int q3 = q4 + nx2;
                    double p = this.real[q1];
                    this.real[q1] = this.real[q3];
                    this.real[q3] = p;
                    p = this.real[q2];
                    this.real[q2] = this.real[q4];
                    this.real[q4] = p;
                }
            }
        } else {
            for (int y = 0; y < this.ny / 2; ++y) {
                int ynx = this.nx * y;
                for (int x = 0; x < nx2; ++x) {
                    int q1 = ynx + x;
                    int q2 = q1 + nx2;
                    int q4 = q1 + nxy2;
                    int q3 = q4 + nx2;
                    double p = this.real[q1];
                    this.real[q1] = this.real[q3];
                    this.real[q3] = p;
                    p = this.real[q2];
                    this.real[q2] = this.real[q4];
                    this.real[q4] = p;
                    p = this.imag[q1];
                    this.imag[q1] = this.imag[q3];
                    this.imag[q3] = p;
                    p = this.imag[q2];
                    this.imag[q2] = this.imag[q4];
                    this.imag[q4] = p;
                }
            }
        }
    }

    public void shiftX() {
        int nx2 = this.nx / 2;
        if (this.imag == null) {
            for (int y = 0; y < this.ny; ++y) {
                int ynx = this.nx * y;
                for (int x = 0; x < nx2; ++x) {
                    int q1 = ynx + x;
                    int q2 = q1 + nx2;
                    double p = this.real[q1];
                    this.real[q1] = this.real[q2];
                    this.real[q2] = p;
                }
            }
        } else {
            for (int y = 0; y < this.ny; ++y) {
                int ynx = this.nx * y;
                for (int x = 0; x < nx2; ++x) {
                    int q1 = ynx + x;
                    int q2 = q1 + nx2;
                    double p = this.real[q1];
                    this.real[q1] = this.real[q2];
                    this.real[q2] = p;
                    p = this.imag[q1];
                    this.imag[q1] = this.imag[q2];
                    this.imag[q2] = p;
                }
            }
        }
    }

    public void shiftY() {
        int halfim = this.nxy / 2;
        if (this.imag == null) {
            for (int k = 0; k < halfim; ++k) {
                double p = this.real[k];
                this.real[k] = this.real[k + halfim];
                this.real[k + halfim] = p;
            }
        } else {
            for (int k = 0; k < halfim; ++k) {
                double p = this.real[k];
                this.real[k] = this.real[k + halfim];
                this.real[k + halfim] = p;
                p = this.imag[k];
                this.imag[k] = this.imag[k + halfim];
                this.imag[k + halfim] = p;
            }
        }
    }

    public ComplexImage circShift(int shiftx, int shifty) {
        while (shiftx < 0) {
            shiftx = this.nx + shiftx;
        }
        while (shifty < 0) {
            shifty = this.ny + shifty;
        }
        while (shiftx > this.nx - 1) {
            shiftx -= this.nx;
        }
        while (shifty > this.ny - 1) {
            shifty -= this.ny;
        }
        ComplexImage res = new ComplexImage(this.nx, this.ny);
        ComplexImage temp = this.getSubimage(0, this.nx - shiftx - 1, 0, this.ny - shifty - 1);
        res.putSubimage(shiftx, shifty, temp);
        temp = this.getSubimage(this.nx - shiftx, this.nx - 1, this.ny - shifty, this.ny - 1);
        res.putSubimage(0, 0, temp);
        temp = this.getSubimage(0, this.nx - shiftx - 1, this.ny - shifty, this.ny - 1);
        res.putSubimage(shiftx, 0, temp);
        temp = this.getSubimage(this.nx - shiftx, this.nx - 1, 0, this.ny - shifty - 1);
        res.putSubimage(0, shifty, temp);
        return res;
    }

    public void quincunxDownUp() {
        int nx2 = this.nx / 2;
        int ny2 = this.ny / 2;
        int nxy2 = this.nxy / 2;
        for (int y = 0; y < nxy2; y += this.nx) {
            int q1;
            int end = q1 + nx2;
            for (q1 = y; q1 < end; ++q1) {
                int q2 = q1 + nx2;
                int q4 = q1 + nxy2;
                int q3 = q4 + nx2;
                double r = this.real[q1] + this.real[q3];
                double im = this.imag[q1] + this.imag[q3];
                this.real[q1] = this.real[q3] = r;
                this.imag[q1] = this.imag[q3] = im;
                r = this.real[q2] + this.real[q4];
                im = this.imag[q2] + this.imag[q4];
                this.real[q2] = this.real[q4] = r;
                this.imag[q2] = this.imag[q4] = im;
            }
        }
    }

    public void downUpY() {
        int nxy2 = this.nxy / 2;
        for (int q1 = 0; q1 < nxy2; ++q1) {
            int q2 = q1 + nxy2;
            double r = this.real[q1] + this.real[q2];
            double im = this.imag[q1] + this.imag[q2];
            this.real[q1] = this.real[q2] = r;
            this.imag[q1] = this.imag[q2] = im;
        }
    }

    public void dyadicDownUp() {
        int nx2 = this.nx / 2;
        int ny2 = this.ny / 2;
        int nxy2 = this.nxy / 2;
        for (int y = 0; y < nxy2; y += this.nx) {
            int q1;
            int end = q1 + nx2;
            for (q1 = y; q1 < end; ++q1) {
                double im;
                double r;
                int q2 = q1 + nx2;
                int q3 = q1 + nxy2;
                int q4 = q3 + nx2;
                this.real[q3] = this.real[q4] = (r = this.real[q1] + this.real[q2] + this.real[q3] + this.real[q4]);
                this.real[q2] = this.real[q4];
                this.real[q1] = this.real[q4];
                this.imag[q3] = this.imag[q4] = (im = this.imag[q1] + this.imag[q2] + this.imag[q3] + this.imag[q4]);
                this.imag[q2] = this.imag[q4];
                this.imag[q1] = this.imag[q4];
            }
        }
    }

    public void dyadicDownUpCrop() {
        int nx2 = this.nx / 2;
        int ny2 = this.ny / 2;
        int nxy2 = this.nxy / 2;
        int ind = 0;
        for (int y = 0; y < nxy2; y += this.nx) {
            int q1;
            int end = q1 + nx2;
            for (q1 = y; q1 < end; ++q1) {
                double r;
                int q2 = q1 + nx2;
                int q3 = q1 + nxy2;
                int q4 = q3 + nx2;
                this.real[ind] = r = this.real[q1] + this.real[q2] + this.real[q3] + this.real[q4];
                double im = this.imag[q1] + this.imag[q2] + this.imag[q3] + this.imag[q4];
                this.imag[ind++] = im;
            }
        }
        this.nx = nx2;
        this.ny = ny2;
        this.nxy /= 4;
    }

    public void dyadicDownY() {
        ComplexImage temp = this.getSubimage(0, this.nx - 1, 0, this.ny / 2 - 1);
        ComplexImage temp1 = this.getSubimage(0, this.nx - 1, this.ny / 2, this.ny - 1);
        temp1.add(temp);
        this.putSubimage(0, 0, temp1);
        this.ny /= 2;
        this.nxy /= 2;
    }

    public void decimateCrop() {
        int nx2 = this.nx / 2;
        int ny2 = this.ny / 2;
        if (this.imag == null) {
            for (int y = 0; y < ny2; ++y) {
                int x = 0;
                int k = 2 * this.nx * y;
                int k1 = nx2 * y;
                while (x < nx2) {
                    this.real[k1] = this.real[k];
                    ++x;
                    k += 2;
                    ++k1;
                }
            }
        } else {
            for (int y = 0; y < ny2; ++y) {
                int x = 0;
                int k = 2 * this.nx * y;
                int k1 = nx2 * y;
                while (x < nx2) {
                    this.real[k1] = this.real[k];
                    this.imag[k1] = this.imag[k];
                    ++x;
                    k += 2;
                    ++k1;
                }
            }
        }
        this.nx /= 2;
        this.ny /= 2;
        this.nxy /= 4;
    }

    public void decimate() {
        ComplexImage temp = this.copyImage();
        if (this.imag == null) {
            int l = 0;
            int y = 0;
            while (l < this.nxy) {
                if (y >= this.ny) {
                    y -= this.ny;
                }
                int k = l;
                int x = 0;
                while (k < l + this.nx) {
                    if (x >= this.nx) {
                        x -= this.nx;
                    }
                    int k1 = y * this.nx + x;
                    this.real[k] = temp.real[k1];
                    ++k;
                    x += 2;
                }
                l += this.nx;
                y += 2;
            }
        } else {
            int l = 0;
            int y = 0;
            while (l < this.nxy) {
                if (y >= this.ny) {
                    y -= this.ny;
                }
                int k = l;
                int x = 0;
                while (k < l + this.nx) {
                    if (x >= this.nx) {
                        x -= this.nx;
                    }
                    int k1 = y * this.nx + x;
                    this.real[k] = temp.real[k1];
                    this.imag[k] = temp.imag[k1];
                    ++k;
                    x += 2;
                }
                l += this.nx;
                y += 2;
            }
        }
    }

    public void fold() {
        int nxy2 = this.nxy / 2;
        for (int k = 0; k < nxy2; ++k) {
            this.real[k] = this.real[2 * k] + this.real[2 * k + 1];
            this.imag[k] = this.imag[2 * k] + this.imag[2 * k + 1];
        }
        this.nx /= 2;
        this.nxy /= 2;
    }

    public void unfold() {
        int k;
        double[] real1 = new double[2 * this.nxy];
        double[] imag1 = new double[2 * this.nxy];
        for (k = 0; k < 2 * this.nxy; ++k) {
            imag1[k] = 0.0;
            real1[k] = 0.0;
        }
        for (k = 0; k < this.nx * this.ny; ++k) {
            if (k % (2 * this.nx) < this.nx) {
                real1[2 * k] = this.real[k];
                imag1[2 * k] = this.imag[k];
                continue;
            }
            real1[2 * k + 1] = this.real[k];
            imag1[2 * k + 1] = this.imag[k];
        }
        this.real = real1;
        this.imag = imag1;
        this.nx *= 2;
        this.nxy *= 2;
    }

    public void dyadicUpsample() {
        int nx2 = 2 * this.nx;
        int nxy2 = 2 * this.nxy;
        if (this.real.length < 4 * this.nxy) {
            double[] re = new double[4 * this.nxy];
            System.arraycopy(this.real, 0, re, 0, this.nxy);
            this.real = re;
        }
        if (this.imag.length < 4 * this.nxy) {
            double[] im = new double[4 * this.nxy];
            System.arraycopy(this.imag, 0, im, 0, this.nxy);
            this.imag = im;
        }
        for (int y = this.ny - 1; y >= 0; --y) {
            int ynx = y * this.nx;
            int ynx2 = 2 * ynx;
            for (int x = this.nx - 1; x >= 0; --x) {
                int index = ynx + x;
                double r = this.real[index];
                double i = this.imag[index];
                int q1 = ynx2 + x;
                this.real[q1] = r;
                this.imag[q1] = i;
                int q2 = q1 + this.nx;
                this.real[q2] = r;
                this.imag[q2] = i;
                int q3 = q1 + nxy2;
                this.real[q3] = r;
                this.imag[q3] = i;
                int q4 = q3 + this.nx;
                this.real[q4] = r;
                this.imag[q4] = i;
            }
        }
        this.nx *= 2;
        this.ny *= 2;
        this.nxy *= 4;
    }

    public void modulatePlusX() {
        if (this.imag == null) {
            this.imag = new double[this.nxy];
        }
        for (int k = 1; k < this.nx; ++k) {
            double x = 6.283185307179586 * (double)k / (double)this.nx;
            double c = Math.cos(x);
            double s = Math.sin(x);
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k;
                double re = this.real[index];
                double im = this.imag[index];
                this.real[index] = c * re - s * im;
                this.imag[index] = s * re + c * im;
            }
        }
    }

    public void modulateMinusX() {
        if (this.imag == null) {
            this.imag = new double[this.nxy];
        }
        for (int k = 1; k < this.nx; ++k) {
            double x = (double)k * 6.283185307179586 / (double)this.nx;
            double c = Math.cos(x);
            double s = Math.sin(x);
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k;
                double re = this.real[index];
                double im = this.imag[index];
                this.real[index] = c * re + s * im;
                this.imag[index] = c * im - s * re;
            }
        }
    }

    public void modulatePlusY() {
        if (this.imag == null) {
            this.imag = new double[this.nxy];
        }
        for (int k = 1; k < this.ny; ++k) {
            double y = 6.283185307179586 * (double)k / (double)this.ny;
            double c = Math.cos(y);
            double s = Math.sin(y);
            int knx = k * this.nx;
            for (int l = 0; l < this.nx; ++l) {
                int index = knx + l;
                double re = this.real[index];
                double im = this.imag[index];
                this.real[index] = c * re - s * im;
                this.imag[index] = s * re + c * im;
            }
        }
    }

    public void modulateMinusY() {
        if (this.imag == null) {
            this.imag = new double[this.nxy];
        }
        for (int k = 1; k < this.ny; ++k) {
            double y = 6.283185307179586 * (double)k / (double)this.ny;
            double c = Math.cos(y);
            double s = Math.sin(y);
            int knx = k * this.nx;
            for (int l = 0; l < this.nx; ++l) {
                int index = knx + l;
                double re = this.real[index];
                double im = this.imag[index];
                this.real[index] = c * re + s * im;
                this.imag[index] = c * im - s * re;
            }
        }
    }

    public void modulatePlusQuincunx() {
        if (this.imag == null) {
            this.imag = new double[this.nxy];
        }
        for (int k = 0; k < this.nx; ++k) {
            for (int l = 0; l < this.ny; ++l) {
                double x = 6.283185307179586 * ((double)k / (double)this.nx + (double)l / (double)this.ny);
                double c = Math.cos(x);
                double s = Math.sin(x);
                int index = l * this.nx + k;
                double re = this.real[index];
                double im = this.imag[index];
                this.real[index] = c * re - s * im;
                this.imag[index] = s * re + c * im;
            }
        }
    }

    public void modulateMinusQuincunx() {
        if (this.imag == null) {
            this.imag = new double[this.nxy];
        }
        for (int k = 0; k < this.nx; ++k) {
            for (int l = 0; l < this.ny; ++l) {
                double x = 6.283185307179586 * ((double)k / (double)this.nx + (double)l / (double)this.ny);
                double c = Math.cos(x);
                double s = Math.sin(x);
                int index = l * this.nx + k;
                double re = this.real[index];
                double im = this.imag[index];
                this.real[index] = c * re + s * im;
                this.imag[index] = c * im - s * re;
            }
        }
    }

    public ComplexImage rotate(double back) {
        double s1 = 0.0;
        double s0 = 0.0;
        int k = 0;
        int i = 1;
        for (int y = 0; y < this.nxy; y += this.nx) {
            int end = y + this.nx;
            for (int ind = y + k; ind < end; ind += 2) {
                s0 += Math.abs(this.real[ind]);
                s1 += Math.abs(this.real[ind + i]);
            }
            k = 1 - k;
            i = - i;
        }
        k = s0 < s1 ? 1 : 0;
        int s = (this.nx + this.ny) / 2;
        ComplexImage rot = new ComplexImage(s, s);
        rot.settoConstant(back, back);
        for (int y2 = 0; y2 < this.ny; ++y2) {
            int x = k;
            int ind = y2 * this.nx + k;
            while (x < this.nx) {
                int x1 = (x - y2 + this.ny - 1) / 2;
                int y1 = (x + y2) / 2;
                rot.real[rot.nx * y1 + x1] = this.real[ind];
                rot.imag[rot.nx * y1 + x1] = this.imag[ind];
                x += 2;
                ind += 2;
            }
            k = 1 - k;
        }
        return rot;
    }

    public void unrotate(int kx, int ky) {
        int kxy = kx * ky;
        int nx1 = this.nx;
        int ny1 = this.ny;
        double[] re = new double[kxy];
        double[] im = new double[kxy];
        this.nx = kx;
        this.ny = ky;
        this.nxy = this.nx * this.ny;
        int k = 0;
        for (int y = 0; y < ky; ++y) {
            int x = k;
            int ind = kx * y + k;
            while (x < kx) {
                int x1 = (x - y + ky - 1) / 2;
                int y1 = (x + y) / 2;
                re[ind] = this.real[nx1 * y1 + x1];
                im[ind] = this.imag[nx1 * y1 + x1];
                x += 2;
                ind += 2;
            }
            k = 1 - k;
        }
        System.arraycopy(re, 0, this.real, 0, kxy);
        System.arraycopy(im, 0, this.imag, 0, kxy);
    }

    public void extend(ComplexImage original) {
        this.putSubimage(0, 0, original);
        for (int x = 0; x < original.nx; ++x) {
            this.real[(original.nx + 1) * original.ny + x] = this.real[x];
        }
        for (int y = 0; y < original.ny + 1; ++y) {
            this.real[(original.nx + 1) * y + original.nx] = this.real[(original.nx + 1) * y];
        }
    }

    public void stretch() {
        double max = 250.0;
        double back = 255.0;
        ComplexImage mod = this.copyImage();
        if (this.imag == null) {
            double maximage = mod.maxAbsReal();
            double sp = max / maximage;
            for (int i = 0; i < this.nxy; ++i) {
                if (this.real[i] == back) continue;
                double[] arrd = this.real;
                int n = i;
                arrd[n] = arrd[n] * sp;
            }
        } else {
            mod.modulus();
            double maximage = mod.max();
            double sp = max / maximage;
            for (int i = 0; i < this.nxy; ++i) {
                if (this.real[i] == back) continue;
                double[] arrd = this.real;
                int n = i;
                arrd[n] = arrd[n] * sp;
                double[] arrd2 = this.imag;
                int n2 = i;
                arrd2[n2] = arrd2[n2] * sp;
            }
        }
    }

    void displayQuincunxNonredundant(int J) {
        int dx = this.nx;
        int dy = this.ny;
        for (int j = 1; j <= J; ++j) {
            ComplexImage sub;
            if (j % 2 == 1) {
                sub = this.getSubimage(dx / 2, dx - 1, 0, dy - 1);
                sub.stretch();
                this.putSubimage(dx / 2, 0, sub);
                dx /= 2;
                continue;
            }
            sub = this.getSubimage(0, dx - 1, dy / 2, dy - 1);
            sub.stretch();
            this.putSubimage(0, dy / 2, sub);
            dy /= 2;
        }
    }

    public void frame(double color) {
        int k;
        int nxy1 = this.nxy - 1;
        for (k = 0; k < this.nx; ++k) {
            double d = color;
            this.real[nxy1 - k] = d;
            this.real[k] = d;
        }
        for (k = 1; k < this.ny; ++k) {
            double d = color;
            this.real[k * this.nx - 1] = d;
            this.real[k * this.nx] = d;
        }
    }

    public void frame(double colorr, double colori) {
        int k;
        int nxy1 = this.nxy - 1;
        for (k = 0; k < this.nx; ++k) {
            double d = colorr;
            this.real[nxy1 - k] = d;
            this.real[k] = d;
            double d2 = colori;
            this.imag[nxy1 - k] = d2;
            this.imag[k] = d2;
        }
        for (k = 1; k < this.ny; ++k) {
            double d = colorr;
            this.real[k * this.nx - 1] = d;
            this.real[k * this.nx] = d;
            double d3 = colori;
            this.imag[k * this.nx - 1] = d3;
            this.imag[k * this.nx] = d3;
        }
    }

    public void extendWithZeros() {
        ComplexImage temp = new ComplexImage(this.nx + 2, this.ny + 2);
        ComplexImage temp1 = this.copyImage();
        temp.putSubimage(1, 1, temp1);
        this.real = temp.real;
        this.imag = temp.imag;
        this.nx += 2;
        this.ny += 2;
        this.nxy = this.nx * this.ny;
    }

    public void reduce() {
        ComplexImage temp = this.getSubimage(1, this.nx - 2, 1, this.ny - 2);
        this.real = temp.real;
        this.imag = temp.imag;
        this.nx -= 2;
        this.ny -= 2;
        this.nxy = this.nx * this.ny;
    }

    public void createMap() {
        for (int i = 0; i < this.nxy; ++i) {
            if (this.real[i] == 0.0) continue;
            this.real[i] = 1.0;
        }
    }

    public void softThreshold(double t) {
        double t2 = t * t;
        for (int k = 0; k < this.nxy; ++k) {
            if (this.real[k] * this.real[k] + this.imag[k] * this.imag[k] < t2) {
                this.imag[k] = 0.0;
                this.real[k] = 0.0;
                continue;
            }
            double r = this.real[k];
            double im = this.imag[k];
            double m = Math.sqrt(r * r + im * im);
            double[] arrd = this.real;
            int n = k;
            arrd[n] = arrd[n] - r / m;
            double[] arrd2 = this.imag;
            int n2 = k;
            arrd2[n2] = arrd2[n2] - im / m;
        }
    }

    public void hardThreshold(double t) {
        double t2 = t * t;
        for (int k = 0; k < this.nxy; ++k) {
            if (this.real[k] * this.real[k] + this.imag[k] * this.imag[k] >= t2) continue;
            this.imag[k] = 0.0;
            this.real[k] = 0.0;
        }
    }

    public void limitImage(double min, double max) {
        for (int i = 0; i < this.nxy; ++i) {
            if (this.real[i] > max) {
                this.real[i] = max;
                continue;
            }
            if (this.real[i] >= min) continue;
            this.real[i] = min;
        }
    }

    private void search8(int x, int y) {
        int i;
        if (x < this.nx && x >= 0 && y < this.ny && y >= 0 && this.real[i = y * this.nx + x] == 1.0) {
            this.real[i] = 3.0;
            this.search8(x + 1, y);
            this.search8(x - 1, y);
            this.search8(x, y + 1);
            this.search8(x, y - 1);
            this.search8(x + 1, y + 1);
            this.search8(x - 1, y - 1);
            this.search8(x - 1, y + 1);
            this.search8(x + 1, y - 1);
        }
    }

    private void zeroCrossHysteresis8() {
        for (int y = 0; y < this.ny; ++y) {
            int i = y * this.nx;
            int x = 0;
            while (x < this.nx) {
                if (this.real[i] == 2.0) {
                    this.search8(x + 1, y);
                    this.search8(x - 1, y);
                    this.search8(x, y + 1);
                    this.search8(x, y - 1);
                    this.search8(x + 1, y + 1);
                    this.search8(x - 1, y - 1);
                    this.search8(x - 1, y + 1);
                    this.search8(x + 1, y - 1);
                }
                ++x;
                ++i;
            }
        }
        for (int x = 0; x < this.nxy; ++x) {
            this.real[x] = this.real[x] < 1.5 ? 0.0 : 1.0;
        }
    }

    public double canny(double Tl, double Th) {
        ComplexImage mod = this.copyImage();
        mod.modulus();
        int[] dir = new int[this.nxy];
        for (int x = 0; x < this.nxy; ++x) {
            if (this.imag[x] <= 0.0 && this.real[x] > - this.imag[x] || this.imag[x] >= 0.0 && this.real[x] < - this.imag[x]) {
                dir[x] = 0;
            }
            if (this.real[x] > 0.0 && - this.imag[x] >= this.real[x] || this.real[x] < 0.0 && this.real[x] >= - this.imag[x]) {
                dir[x] = 1;
            }
            if (this.real[x] <= 0.0 && this.real[x] > this.imag[x] || this.real[x] >= 0.0 && this.real[x] < this.imag[x]) {
                dir[x] = 2;
            }
            if ((this.imag[x] >= 0.0 || this.real[x] > this.imag[x]) && (this.imag[x] <= 0.0 || this.real[x] < this.imag[x])) continue;
            dir[x] = 3;
        }
        ComplexImage mod1 = mod.copyImage();
        int[][] neighbours = new int[][]{{this.nx, this.nx - 1, - this.nx, - this.nx + 1}, {-1, this.nx - 1, 1, - this.nx + 1}, {-1, - this.nx - 1, 1, this.nx + 1}, {- this.nx, - this.nx - 1, this.nx, this.nx + 1}};
        int x2 = 0;
        int x1 = this.nxy - 1;
        while (x2 < this.nx) {
            mod1.real[x1] = 0.0;
            mod1.real[x2] = 0.0;
            ++x2;
            --x1;
        }
        x2 = 0;
        x1 = this.nxy - 1;
        while (x2 < this.nxy) {
            mod1.real[x1] = 0.0;
            mod1.real[x2] = 0.0;
            x2 += this.nx;
            x1 -= this.nx;
        }
        for (x2 = this.nx + 1; x2 < this.nxy - this.nx - 1; ++x2) {
            double d = Math.abs(this.imag[x2] / this.real[x2]);
            if (d > 1.0) {
                d = 1.0 / d;
            }
            double ss1 = mod.real[x2 + neighbours[dir[x2]][0]] * (1.0 - d) + mod.real[x2 + neighbours[dir[x2]][1]] * d;
            double ss2 = mod.real[x2 + neighbours[dir[x2]][2]] * (1.0 - d) + mod.real[x2 + neighbours[dir[x2]][3]] * d;
            if (mod.real[x2] > ss1 && mod.real[x2] > ss2) continue;
            mod1.real[x2] = 0.0;
        }
        ComplexImage map = new ComplexImage(this.nx, this.ny);
        for (int x3 = 0; x3 < this.nxy; ++x3) {
            if (mod1.real[x3] > Th) {
                map.real[x3] = 2.0;
                continue;
            }
            if (mod1.real[x3] <= Tl) continue;
            map.real[x3] = 1.0;
        }
        map.zeroCrossHysteresis8();
        double pr = map.sumReal() / (double)map.nxy;
        map.frame(1.0);
        this.multiply(map);
        return pr;
    }

    public void derivativeX() {
        ComplexImage temp = new ComplexImage(this.nx, this.ny);
        System.arraycopy(this.real, 0, temp.real, 0, this.nxy);
        temp.FFT2D();
        for (int k = 0; k < this.nx; ++k) {
            double x = 6.283185307179586 * (double)k / (double)this.nx;
            if (x > 3.141592653589793) {
                x -= 6.283185307179586;
            }
            for (int l = 0; l < this.ny; ++l) {
                int index = l * this.nx + k;
                double[] arrd = temp.real;
                int n = index;
                arrd[n] = arrd[n] * x;
                double[] arrd2 = temp.imag;
                int n2 = index;
                arrd2[n2] = arrd2[n2] * x;
            }
        }
        temp.conj();
        System.arraycopy(temp.real, 0, this.imag, 0, this.nxy);
        System.arraycopy(temp.imag, 0, this.real, 0, this.nxy);
        this.iFFT2D();
    }

    public void derivativeY() {
        ComplexImage temp = new ComplexImage(this.nx, this.ny);
        System.arraycopy(this.real, 0, temp.real, 0, this.nxy);
        temp.FFT2D();
        for (int k = 0; k < this.ny; ++k) {
            double y = 6.283185307179586 * (double)k / (double)this.ny;
            if (y > 3.141592653589793) {
                y -= 6.283185307179586;
            }
            for (int l = 0; l < this.nx; ++l) {
                int index = k * this.nx + l;
                double[] arrd = temp.real;
                int n = index;
                arrd[n] = arrd[n] * y;
                double[] arrd2 = temp.imag;
                int n2 = index;
                arrd2[n2] = arrd2[n2] * y;
            }
        }
        temp.conj();
        System.arraycopy(temp.real, 0, this.imag, 0, this.nxy);
        System.arraycopy(temp.imag, 0, this.real, 0, this.nxy);
        this.iFFT2D();
    }

    public void smooth() {
        int[] d = new int[]{1, -1, this.nx, - this.nx, this.nx + 1, this.nx - 1, - this.nx + 1, - this.nx - 1, 0};
        double[] w1 = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        ComplexImage temp = this.copyImage();
        for (int i = this.nx + 1; i < this.nxy - this.nx - 1; ++i) {
            double m = 0.0;
            double s = 0.0;
            for (int k = 0; k < 9; ++k) {
                double w = w1[k];
                s += w;
                m += temp.real[i + d[k]] * w;
            }
            this.real[i] = m /= s;
        }
    }

    public void smooth(ComplexImage weights) {
        int[] d = new int[]{1, -1, this.nx, - this.nx, this.nx + 1, this.nx - 1, - this.nx + 1, - this.nx - 1, 0};
        double[] w1 = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        ComplexImage temp = this.copyImage();
        for (int i = this.nx + 1; i < this.nxy - this.nx - 1; ++i) {
            double m = 0.0;
            double s = 0.0;
            for (int k = 0; k < 9; ++k) {
                double w = weights.real[i + d[k]] * w1[k];
                s += w;
                double t = temp.real[i + d[k]];
                m += t * w;
            }
            this.real[i] = m /= s;
        }
    }

    public void riesz1() {
        this.FFT2D();
        ComplexImage mult1 = new ComplexImage(this.nx, this.ny);
        for (int y = 0; y < this.ny; ++y) {
            double omy = 6.283185307179586 * (double)y / (double)this.ny - 3.141592653589793;
            for (int x = 0; x < this.nx; ++x) {
                double omx = 6.283185307179586 * (double)x / (double)this.nx - 3.141592653589793;
                mult1.real[this.nx * y + x] = 1.0 / Math.sqrt(omx * omx + omy * omy);
                double[] arrd = mult1.real;
                int n = this.nx * y + x;
                arrd[n] = arrd[n] * omx;
            }
        }
        mult1.shift();
        mult1.real[0] = 1.0;
        this.multiply(mult1);
        this.iFFT2D();
        double[] temparray = new double[this.nxy];
        System.arraycopy(this.real, 0, temparray, 0, this.nxy);
        System.arraycopy(this.imag, 0, this.real, 0, this.nxy);
        System.arraycopy(temparray, 0, this.imag, 0, this.nxy);
        this.conj();
    }

    public void riesz2() {
        this.FFT2D();
        ComplexImage mult1 = new ComplexImage(this.nx, this.ny);
        for (int y = 0; y < this.ny; ++y) {
            double omy = 6.283185307179586 * (double)y / (double)this.ny - 3.141592653589793;
            for (int x = 0; x < this.nx; ++x) {
                double omx = 6.283185307179586 * (double)x / (double)this.nx - 3.141592653589793;
                mult1.real[this.nx * y + x] = 1.0 / Math.sqrt(omx * omx + omy * omy);
                double[] arrd = mult1.real;
                int n = this.nx * y + x;
                arrd[n] = arrd[n] * omy;
            }
        }
        mult1.shift();
        mult1.real[0] = 1.0;
        this.multiply(mult1);
        double[] temparray = new double[this.nxy];
        System.arraycopy(this.real, 0, temparray, 0, this.nxy);
        System.arraycopy(this.imag, 0, this.real, 0, this.nxy);
        System.arraycopy(temparray, 0, this.imag, 0, this.nxy);
        this.conj();
        this.iFFT2D();
    }
}

