/*
 * Decompiled with CFR 0_118.
 */
package imageware;

public class Convolver {
    private static double tolerance = 1.0E-5;

    public static double[] convolveFIR(double[] arrd, double[] arrd2) {
        int n;
        int n2 = arrd.length;
        if (n2 <= 1) {
            throw new IllegalArgumentException("convolveFIR: input signal too short");
        }
        double[] arrd3 = new double[n2];
        int n3 = arrd2.length - 1;
        int n4 = 0;
        int n5 = 2 * (n2 - 1);
        int n6 = arrd2.length / 2;
        n -= (long)(n = 1 + n6 - arrd2.length) < 0 ? n5 * ((n + 1 - n5) / n5) : n5 * (n / n5);
        for (int i = 0; i < n2; ++i) {
            int n7 = - arrd2.length;
            int n8 = n;
            n3 = arrd2.length - 1;
            double d = 0.0;
            while (n7 < 0) {
                int n9;
                int n10;
                n4 = n8;
                int n11 = n10 = n8 - n2 < n7 ? n7 : n8 - n2;
                if ((long)n10 < 0) {
                    for (n9 = n10; n9 < 0; ++n9) {
                        d += arrd[n4] * arrd2[n3];
                        --n3;
                        ++n4;
                    }
                    n8 -= n10;
                    n7 -= n10;
                }
                n4 = n5 - n8;
                int n12 = n9 = n8 - n5 < n7 ? n7 : n8 - n5;
                if ((long)n9 < 0) {
                    for (int j = n9; j < 0; ++j) {
                        d += arrd[n4] * arrd2[n3];
                        --n3;
                        --n4;
                    }
                    n7 -= n9;
                }
                n8 = 0;
            }
            if (++n == n5) {
                n = 0;
            }
            arrd3[i] = d;
        }
        return arrd3;
    }

    public static double[] convolveIIR(double[] arrd, double[] arrd2) {
        int n;
        double d = 1.0;
        int n2 = arrd.length;
        double[] arrd3 = new double[n2];
        for (n = 0; n < arrd2.length; ++n) {
            d = d * (1.0 - arrd2[n]) * (1.0 - 1.0 / arrd2[n]);
        }
        for (n = 0; n < n2; ++n) {
            arrd3[n] = arrd[n] * d;
        }
        for (n = 0; n < arrd2.length; ++n) {
            int n3;
            arrd3[0] = Convolver.getInitialCausalCoefficientMirror(arrd3, arrd2[n]);
            for (n3 = 1; n3 < n2; ++n3) {
                arrd3[n3] = arrd3[n3] + arrd2[n] * arrd3[n3 - 1];
            }
            arrd3[n2 - 1] = Convolver.getInitialAntiCausalCoefficientMirror(arrd3, arrd2[n]);
            for (n3 = n2 - 2; 0 <= n3; --n3) {
                arrd3[n3] = arrd2[n] * (arrd3[n3 + 1] - arrd3[n3]);
            }
        }
        return arrd3;
    }

    public static double[] convolveIIR2(double[] arrd, double d, double d2) {
        int n;
        int n2;
        int n3 = arrd.length;
        int n4 = 2 * n3;
        double d3 = (- d2 + 1.0) * (1.0 - d + d2) / ((d2 - 1.0) * (1.0 + d + d2));
        double d4 = (- d3) * d2 * d / (d2 + 1.0);
        double[] arrd2 = new double[n4];
        double[] arrd3 = new double[n4];
        for (n2 = 0; n2 < n3; ++n2) {
            arrd3[n2] = arrd[n2];
            arrd3[n4 - n2 - 1] = arrd[n2];
        }
        n2 = 2;
        if (tolerance > 0.0 && d2 != 1.0) {
            n2 = n4 - (int)Math.ceil(2.0 * Math.log(tolerance) / Math.log(d2));
        }
        if (n2 < 2) {
            n2 = 2;
        }
        arrd2[n2 - 1] = 0.0;
        arrd2[n2 - 2] = 0.0;
        for (n = n2; n < n4; ++n) {
            arrd2[n] = d3 * arrd3[n] + d4 * arrd3[n - 1] + d * arrd2[n - 1] - d2 * arrd2[n - 2];
        }
        arrd2[0] = d3 * arrd3[0] + d4 * arrd3[n4 - 1] + d * arrd2[n4 - 1] - d2 * arrd2[n4 - 2];
        arrd2[1] = d3 * arrd3[1] + d4 * arrd3[0] + d * arrd2[0] - d2 * arrd2[n4 - 1];
        for (n = 2; n < n4; ++n) {
            arrd2[n] = d3 * arrd3[n] + d4 * arrd3[n - 1] + d * arrd2[n - 1] - d2 * arrd2[n - 2];
        }
        double[] arrd4 = new double[n3];
        for (int i = 0; i < n3; ++i) {
            arrd4[i] = arrd2[i] + arrd2[n4 - i - 1] - d3 * arrd[i];
        }
        return arrd4;
    }

    private static double getInitialAntiCausalCoefficientMirror(double[] arrd, double d) {
        return (d * arrd[arrd.length - 2] + arrd[arrd.length - 1]) * d / (d * d - 1.0);
    }

    private static double getInitialCausalCoefficientMirror(double[] arrd, double d) {
        double d2 = d;
        double d3 = Math.pow(d, arrd.length - 1);
        double d4 = arrd[0] + d3 * arrd[arrd.length - 1];
        int n = arrd.length;
        if (0.0 < tolerance) {
            n = 2 + (int)(Math.log(tolerance) / Math.log(Math.abs(d)));
            n = n < arrd.length ? n : arrd.length;
        }
        d3 *= d3;
        for (int i = 1; i < n - 1; ++i) {
            d4 += (d2 + (d3 /= d)) * arrd[i];
            d2 *= d;
        }
        return d4 / (1.0 - Math.pow(d, 2 * arrd.length - 2));
    }
}

