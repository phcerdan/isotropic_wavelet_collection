/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package steerabletools;

import additionaluserinterface.WalkBar;
import fft.ComplexSignal;
import fft.FFT2D;
import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import imageware.Builder;
import imageware.ImageWare;
import riesz.RieszFilter;
import riesz.RieszTransform;
import steerabletools.CubicSpline;

public class Gradient {
    private WalkBar walk;
    private ImageWare gradx;
    private ImageWare grady;

    public Gradient(WalkBar walk) {
        this.walk = walk;
    }

    public Gradient() {
        this.walk = new WalkBar("(c) 2009 EPFL, Biomedical Imaging Group", false, false, false);
    }

    public ImageWare getGradientX() {
        return this.gradx;
    }

    public ImageWare getGradientY() {
        return this.grady;
    }

    public void computeFiniteDifference(ImageWare input) {
        int nx = input.getWidth();
        int ny = input.getHeight();
        int nz = input.getSizeZ();
        double[] rowin = new double[nx];
        double[] rowou = new double[nx];
        double[] colin = new double[ny];
        double[] colou = new double[ny];
        this.walk.reset();
        this.gradx = Builder.create(nx, ny, nz, 3);
        int z = 0;
        while (z < nz) {
            for (int y = 0; y < ny; ++y) {
                input.getX(0, y, z, rowin);
                for (int x = 1; x < nx - 1; ++x) {
                    rowou[x] = rowin[x - 1] - rowin[x + 1];
                }
                this.gradx.putX(0, y, z, rowou);
            }
            this.walk.progress("Gradient FD", (double)(++z) * 50.0 / (double)nz);
        }
        this.grady = Builder.create(nx, ny, nz, 3);
        z = 0;
        while (z < nz) {
            for (int x = 0; x < nx; ++x) {
                input.getY(x, 0, z, colin);
                for (int y = 1; y < ny - 1; ++y) {
                    colou[y] = - colin[y + 1] + colin[y - 1];
                }
                this.grady.putY(x, 0, z, colou);
            }
            this.walk.progress("Gradient FD", 50.0 + (double)(++z) * 50.0 / (double)nz);
        }
        this.walk.finish("Gradient FD...");
    }

    public void computeCubicSpline(ImageWare input) {
        int nx = input.getWidth();
        int ny = input.getHeight();
        int nz = input.getSizeZ();
        double[] rowin = new double[nx];
        double[] rowou = new double[nx];
        double[] colin = new double[ny];
        double[] colou = new double[ny];
        this.walk.reset();
        this.walk.progress("Gradient Spline", 10);
        this.gradx = Builder.create(nx, ny, nz, 3);
        this.walk.progress("Gradient Spline", 20);
        this.grady = Builder.create(nx, ny, nz, 3);
        int z = 0;
        while (z < nz) {
            ImageWare coef = CubicSpline.computeCubicSplineCoeffients(input, z);
            for (int y = 0; y < ny; ++y) {
                coef.getX(0, y, 0, rowin);
                for (int x = 1; x < nx - 1; ++x) {
                    rowou[x] = (rowin[x - 1] - rowin[x + 1]) * 0.75;
                }
                this.gradx.putX(0, y, z, rowou);
            }
            for (int x = 0; x < nx; ++x) {
                coef.getY(x, 0, 0, colin);
                for (int y2 = 1; y2 < ny - 1; ++y2) {
                    colou[y2] = (- colin[y2 + 1] + colin[y2 - 1]) * 0.75;
                }
                this.grady.putY(x, 0, z, colou);
            }
            this.walk.progress("Gradient Spline", 30.0 + (double)(++z) * 70.0 / (double)nz);
        }
        this.walk.finish("Gradient Spline...");
    }

    public void computeRieszFilter(ImageWare input) {
        int nx = input.getWidth();
        int ny = input.getHeight();
        int nz = input.getSizeZ();
        RieszTransform riesz = new RieszTransform(nx, ny, 1, false);
        RieszFilter filter = riesz.getFilters();
        this.walk.progress("Gradient Riesz", 20);
        ImageWare slice = Builder.create(nx, ny, 1, 4);
        this.gradx = Builder.create(nx, ny, nz, 3);
        this.walk.progress("Gradient Riesz", 30);
        this.grady = Builder.create(nx, ny, nz, 3);
        this.walk.progress("Gradient Riesz", 25);
        ImageWare tmp = Builder.create(nx, ny, 1, 4);
        int z = 0;
        while (z < nz) {
            input.getXY(0, 0, z, slice);
            ComplexSignal in = new ComplexSignal(slice.getSliceDouble(0), nx, ny);
            ComplexSignal fin = FFT2D.transform(in);
            ComplexSignal cfx = ComplexSignal.multiply(fin, filter.getAnalysis(0));
            ComplexSignal cfy = ComplexSignal.multiply(fin, filter.getAnalysis(1));
            ComplexSignal ax = FFT2D.inverse(cfx);
            ComplexSignal ay = FFT2D.inverse(cfy);
            this.storeReal(ax, this.gradx, z);
            this.storeReal(ay, this.grady, z);
            this.walk.progress("Gradient Riesz", 30.0 + (double)(++z) * 70.0 / (double)nz);
        }
    }

    private void storeReal(ComplexSignal signal, ImageWare grad, int z) {
        int nxy = grad.getWidth() * grad.getHeight();
        float[] data = grad.getSliceFloat(z);
        for (int k = 0; k < nxy; ++k) {
            data[k] = (float)signal.real[k];
        }
    }

    private void store(ComplexSignal signal, ImageWare real, ImageWare imag, int z) {
        int nx = real.getWidth();
        int ny = real.getHeight();
        ImageStack sr = new ImageStack(nx, ny);
        sr.addSlice("", (ImageProcessor)new FloatProcessor(nx, ny, signal.real));
        ImageWare fx = Builder.wrap(sr);
        real.putXY(0, 0, z, fx);
        ImageStack si = new ImageStack(nx, ny);
        si.addSlice("", (ImageProcessor)new FloatProcessor(nx, ny, signal.imag));
        ImageWare fy = Builder.wrap(sr);
        imag.putXY(0, 0, z, fy);
    }
}

