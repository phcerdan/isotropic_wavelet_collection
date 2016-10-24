/*
 * Decompiled with CFR 0_118.
 */
package steerabletools;

import imageware.Builder;
import imageware.ImageWare;

public class CubicSpline {
    public static double getInterpolatedPixelCubicSpline(ImageWare coef, double x, double y, int scale) {
        double[][] arr = new double[4][4];
        int i = (int)Math.floor(x);
        int j = (int)Math.floor(y);
        coef.getNeighborhoodXY(i + 1, j + 1, scale, arr, 2);
        double v = CubicSpline.getSampleCubicSpline(x - (double)i, y - (double)j, arr);
        return v;
    }

    public static double getSampleCubicSpline(double x, double y, double[][] neighbor) {
        double[] xw = CubicSpline.getCubicSpline(x);
        double[] yw = CubicSpline.getCubicSpline(y);
        double sum = 0.0;
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < 4; ++i) {
                sum += neighbor[i][j] * xw[i] * yw[j];
            }
        }
        return sum;
    }

    public static double[] getCubicSpline(double t) {
        double[] v = new double[4];
        if (t < 0.0 || t > 1.0) {
            throw new ArrayStoreException("Argument t for cubic B-spline outside of expected range.");
        }
        double t1 = 1.0 - t;
        double t2 = t * t;
        v[0] = t1 * t1 * t1 / 6.0;
        v[1] = 0.6666666666666666 + 0.5 * t2 * (t - 2.0);
        v[3] = t2 * t / 6.0;
        v[2] = 1.0 - v[3] - v[1] - v[0];
        return v;
    }

    public static ImageWare computeCubicSplineCoeffients(ImageWare input, int z) {
        int nx = input.getWidth();
        int ny = input.getHeight();
        ImageWare output = Builder.create(nx, ny, 1, 3);
        double c0 = 6.0;
        double a = Math.sqrt(3.0) - 2.0;
        double[] rowin = new double[nx];
        double[] rowout = new double[nx];
        for (int y = 0; y < ny; ++y) {
            input.getX(0, y, z, rowin);
            CubicSpline.doSymmetricalExponentialFilter(rowin, rowout, c0, a);
            output.putX(0, y, 0, rowout);
        }
        double[] colin = new double[ny];
        double[] colout = new double[ny];
        for (int x = 0; x < nx; ++x) {
            output.getY(x, 0, 0, colin);
            CubicSpline.doSymmetricalExponentialFilter(colin, colout, c0, a);
            output.putY(x, 0, 0, colout);
        }
        return output;
    }

    public static void doSymmetricalExponentialFilter(double[] s, double[] c, double c0, double a) {
        int k;
        int n = s.length;
        double[] cn = new double[n];
        double[] cp = new double[n];
        cp[0] = CubicSpline.computeInitialValueCausal(s, a);
        for (k = 1; k < n; ++k) {
            cp[k] = s[k] + a * cp[k - 1];
        }
        cn[n - 1] = CubicSpline.computeInitialValueAntiCausal(cp, a);
        for (k = n - 2; k >= 0; --k) {
            cn[k] = a * (cn[k + 1] - cp[k]);
        }
        for (k = 0; k < n; ++k) {
            c[k] = c0 * cn[k];
        }
    }

    public static double computeInitialValueCausal(double[] signal, double a) {
        double epsilon = 1.0E-6;
        int k0 = (int)Math.ceil(Math.log(epsilon) / Math.log(Math.abs(a)));
        double polek = a;
        double v = signal[0];
        for (int k = 1; k < k0; ++k) {
            v += polek * signal[k];
            polek *= a;
        }
        return v;
    }

    public static double computeInitialValueAntiCausal(double[] signal, double a) {
        int n = signal.length;
        double v = a / (a * a - 1.0) * (signal[n - 1] + a * signal[n - 2]);
        return v;
    }
}

