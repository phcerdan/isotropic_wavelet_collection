/*
 * Decompiled with CFR 0_118.
 */
package ijtools;

public class Interpolator {
    private double[] input;
    private double[] c;
    private int nx;
    private int ny;
    private String mode;
    private double a;
    private double c0;

    public Interpolator(double[] arrd, int n, int n2, String string) {
        this.input = arrd;
        this.nx = n;
        this.ny = n2;
        this.mode = string;
        if (string.equals("linear")) {
            this.c = arrd;
        } else if (string.equals("quadratic")) {
            this.c0 = 8.0;
            this.a = -3.0 + 2.0 * Math.sqrt(2.0);
            this.computeCoefficients();
        } else if (string.equals("cubic")) {
            this.c0 = 6.0;
            this.a = -2.0 + Math.sqrt(3.0);
            this.computeCoefficients();
        }
    }

    public double getValue(double d, double d2) {
        double[] arrd;
        double[] arrd2;
        int n = (int)d;
        int n2 = (int)d2;
        if (this.mode.equals("linear")) {
            int n3;
            int n4;
            double d3 = d - (double)n;
            double d4 = d2 - (double)n2;
            if (d < 0.0) {
                d3 = - d3;
                n4 = this.mirror(n - 1, this.nx);
            } else {
                n4 = this.mirror(n + 1, this.nx);
            }
            if (d2 < 0.0) {
                d4 = - d4;
                n3 = this.mirror(n2 - 1, this.ny);
            } else {
                n3 = this.mirror(n2 + 1, this.ny);
            }
            int n5 = this.mirror(n, this.nx);
            int n6 = this.mirror(n2, this.ny);
            return (1.0 - d4) * (d3 * this.input[n4 + n6 * this.nx] + (1.0 - d3) * this.input[n5 + n6 * this.nx]) + d4 * (d3 * this.input[n4 + n3 * this.nx] + (1.0 - d3) * this.input[n5 + n3 * this.nx]);
        }
        double d5 = d - (double)n;
        double d6 = d2 - (double)n2;
        if (d < 0.0) {
            --n;
            d5 = 1.0 + d5;
        }
        if (d2 < 0.0) {
            --n2;
            d6 = 1.0 + d6;
        }
        if (this.mode.equals("quadratic")) {
            arrd2 = Interpolator.getQuadraticSpline(d5);
            arrd = Interpolator.getQuadraticSpline(d6);
        } else {
            arrd2 = Interpolator.getCubicSpline(d5);
            arrd = Interpolator.getCubicSpline(d6);
        }
        int n7 = n - 1;
        int n8 = n;
        int n9 = n + 1;
        int n10 = n + 2;
        n7 = this.mirror(n7, this.nx);
        n8 = this.mirror(n8, this.nx);
        n9 = this.mirror(n9, this.nx);
        n10 = this.mirror(n10, this.nx);
        int n11 = n2 - 1;
        int n12 = n2;
        int n13 = n2 + 1;
        int n14 = n2 + 2;
        n11 = this.mirror(n11, this.ny);
        n12 = this.mirror(n12, this.ny);
        n13 = this.mirror(n13, this.ny);
        n14 = this.mirror(n14, this.ny);
        double d7 = arrd2[0] * (arrd[0] * this.c[n7 + n11] + arrd[1] * this.c[n7 + n12] + arrd[2] * this.c[n7 + n13] + arrd[3] * this.c[n7 + n14]) + arrd2[1] * (arrd[0] * this.c[n8 + n11] + arrd[1] * this.c[n8 + n12] + arrd[2] * this.c[n8 + n13] + arrd[3] * this.c[n8 + n14]) + arrd2[2] * (arrd[0] * this.c[n9 + n11] + arrd[1] * this.c[n9 + n12] + arrd[2] * this.c[n9 + n13] + arrd[3] * this.c[n9 + n14]) + arrd2[3] * (arrd[0] * this.c[n10 + (n11 *= this.nx)] + arrd[1] * this.c[n10 + (n12 *= this.nx)] + arrd[2] * this.c[n10 + (n13 *= this.nx)] + arrd[3] * this.c[n10 + (n14 *= this.nx)]);
        return d7;
    }

    private int mirror(int n, int n2) {
        if (n >= 0 && n < n2) {
            return n;
        }
        if (n < 0) {
            return - n;
        }
        return 2 * n2 - 2 - n;
    }

    private void computeCoefficients() {
        int n;
        int n2;
        int n3;
        double[] arrd = this.getCausalInitHorizontal(this.input, this.a);
        for (n = 0; n < this.ny; ++n) {
            for (n2 = 1; n2 < this.nx; ++n2) {
                n3 = n2 + n * this.nx;
                arrd[n3] = this.input[n3] + this.a * arrd[n3 - 1];
            }
        }
        this.c = this.getAntiCausalInitHorizontal(arrd, this.a);
        for (n = 0; n < this.ny; ++n) {
            for (n2 = this.nx - 2; n2 >= 0; --n2) {
                n3 = n2 + n * this.nx;
                this.c[n3] = this.a * (this.c[n3 + 1] - arrd[n3]);
            }
        }
        arrd = this.getCausalInitVertical(this.c, this.a);
        for (n = 0; n < this.nx; ++n) {
            for (n2 = 1; n2 < this.ny; ++n2) {
                n3 = n + n2 * this.nx;
                arrd[n3] = this.c[n3] + this.a * arrd[n3 - this.nx];
            }
        }
        this.c = this.getAntiCausalInitVertical(arrd, this.a);
        for (n = 0; n < this.nx; ++n) {
            for (n2 = this.ny - 2; n2 >= 0; --n2) {
                n3 = n + n2 * this.nx;
                this.c[n3] = this.a * (this.c[n3 + this.nx] - arrd[n3]);
            }
        }
        double d = this.c0 * this.c0;
        for (n3 = 0; n3 < this.ny * this.ny; ++n3) {
            this.c[n3] = d * this.c[n3];
        }
    }

    private static double[] getQuadraticSpline(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new ArrayStoreException("Argument t for quadratic B-spline outside of expected range [0, 1]: " + d);
        }
        double[] arrd = new double[4];
        if (d <= 0.5) {
            arrd[0] = (d - 0.5) * (d - 0.5) / 2.0;
            arrd[1] = 0.75 - d * d;
            arrd[2] = 1.0 - arrd[1] - arrd[0];
            arrd[3] = 0.0;
        } else {
            arrd[0] = 0.0;
            arrd[1] = (d - 1.5) * (d - 1.5) / 2.0;
            arrd[3] = (d - 0.5) * (d - 0.5) / 2.0;
            arrd[2] = 1.0 - arrd[3] - arrd[1];
        }
        return arrd;
    }

    private static double[] getCubicSpline(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new ArrayStoreException("Argument t for cubic B-spline outside of expected range [0, 1]: " + d);
        }
        double[] arrd = new double[4];
        double d2 = 1.0 - d;
        double d3 = d * d;
        arrd[0] = d2 * d2 * d2 / 6.0;
        arrd[1] = 0.6666666666666666 + 0.5 * d3 * (d - 2.0);
        arrd[3] = d3 * d / 6.0;
        arrd[2] = 1.0 - arrd[3] - arrd[1] - arrd[0];
        return arrd;
    }

    private double[] getAntiCausalInitVertical(double[] arrd, double d) {
        double[] arrd2 = new double[this.nx * this.ny];
        int n = (this.ny - 1) * this.nx;
        double d2 = d * d - 1.0;
        for (int i = 0; i < this.nx; ++i) {
            arrd2[i + n] = d * (arrd[i + n] + d * arrd[i + n - this.nx]) / d2;
        }
        return arrd2;
    }

    private double[] getAntiCausalInitHorizontal(double[] arrd, double d) {
        double[] arrd2 = new double[this.nx * this.ny];
        double d2 = d * d - 1.0;
        for (int i = 0; i < this.ny; ++i) {
            arrd2[this.nx - 1 + i * this.nx] = d * (arrd[this.nx - 1 + i * this.nx] + d * arrd[this.nx - 2 + i * this.nx]) / d2;
        }
        return arrd2;
    }

    private double[] getCausalInitVertical(double[] arrd, double d) {
        double[] arrd2 = new double[this.nx * this.ny];
        for (int i = 0; i < this.nx; ++i) {
            double d2 = Math.pow(d, this.ny - 1);
            double d3 = arrd[i] + arrd[i + (this.ny - 1) * this.nx] * d2;
            double d4 = d2 * d2;
            d2 = d4 / d;
            double d5 = d;
            for (int j = 1; j < this.ny - 1; ++j) {
                d3 += arrd[i + j * this.nx] * (d5 + d2);
                d5 *= d;
                d2 /= d;
            }
            arrd2[i] = d3 /= 1.0 - d4;
        }
        return arrd2;
    }

    private double[] getCausalInitHorizontal(double[] arrd, double d) {
        double[] arrd2 = new double[this.nx * this.ny];
        for (int i = 0; i < this.ny; ++i) {
            double d2 = Math.pow(d, this.nx - 1);
            double d3 = arrd[i * this.nx] + arrd[this.nx - 1 + i * this.nx] * d2;
            double d4 = d2 * d2;
            d2 = d4 / d;
            double d5 = d;
            for (int j = 1; j < this.nx - 1; ++j) {
                d3 += arrd[j + i * this.nx] * (d5 + d2);
                d5 *= d;
                d2 /= d;
            }
            arrd2[i * this.nx] = d3 /= 1.0 - d4;
        }
        return arrd2;
    }
}

