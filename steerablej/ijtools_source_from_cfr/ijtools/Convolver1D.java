/*
 * Decompiled with CFR 0_118.
 */
package ijtools;

public class Convolver1D {
    public static double[] convolveEven(double[] arrd, double[] arrd2) {
        int n;
        int n2;
        int n3 = arrd.length;
        int n4 = arrd2.length;
        int n5 = n4 - 1;
        int n6 = 2 * n3 - 2;
        double[] arrd3 = new double[n3];
        for (n = 0; n < n5; ++n) {
            arrd3[n] = arrd2[0] * arrd[n];
            for (n2 = 1; n2 <= n; ++n2) {
                double[] arrd4 = arrd3;
                int n7 = n;
                arrd4[n7] = arrd4[n7] + arrd2[n2] * (arrd[n - n2] + arrd[n + n2]);
            }
            for (n2 = n + 1; n2 < n4; ++n2) {
                double[] arrd5 = arrd3;
                int n8 = n;
                arrd5[n8] = arrd5[n8] + arrd2[n2] * (arrd[n2 - n] + arrd[n + n2]);
            }
        }
        for (n = n5; n <= n3 - n4; ++n) {
            arrd3[n] = arrd2[0] * arrd[n];
            for (n2 = 1; n2 < n4; ++n2) {
                double[] arrd6 = arrd3;
                int n9 = n;
                arrd6[n9] = arrd6[n9] + arrd2[n2] * (arrd[n - n2] + arrd[n + n2]);
            }
        }
        for (n = n3 - n5; n < n3; ++n) {
            arrd3[n] = arrd2[0] * arrd[n];
            for (n2 = 1; n2 < n3 - n; ++n2) {
                double[] arrd7 = arrd3;
                int n10 = n;
                arrd7[n10] = arrd7[n10] + arrd2[n2] * (arrd[n - n2] + arrd[n + n2]);
            }
            for (n2 = n3 - n; n2 < n4; ++n2) {
                double[] arrd8 = arrd3;
                int n11 = n;
                arrd8[n11] = arrd8[n11] + arrd2[n2] * (arrd[n6 - n2 - n] + arrd[n - n2]);
            }
        }
        return arrd3;
    }

    public static double[] convolveOdd(double[] arrd, double[] arrd2) {
        int n;
        int n2;
        int n3 = arrd.length;
        int n4 = arrd2.length;
        int n5 = n4 - 1;
        int n6 = 2 * n3 - 2;
        double[] arrd3 = new double[n3];
        for (n = 0; n < n5; ++n) {
            arrd3[n] = 0.0;
            for (n2 = 1; n2 <= n; ++n2) {
                double[] arrd4 = arrd3;
                int n7 = n;
                arrd4[n7] = arrd4[n7] + arrd2[n2] * (arrd[n + n2] - arrd[n - n2]);
            }
            for (n2 = n + 1; n2 < n4; ++n2) {
                double[] arrd5 = arrd3;
                int n8 = n;
                arrd5[n8] = arrd5[n8] + arrd2[n2] * (arrd[n + n2] - arrd[n2 - n]);
            }
        }
        for (n = n5; n <= n3 - n4; ++n) {
            arrd3[n] = 0.0;
            for (n2 = 1; n2 < n4; ++n2) {
                double[] arrd6 = arrd3;
                int n9 = n;
                arrd6[n9] = arrd6[n9] + arrd2[n2] * (arrd[n + n2] - arrd[n - n2]);
            }
        }
        for (n = n3 - n5; n < n3; ++n) {
            arrd3[n] = 0.0;
            for (n2 = 1; n2 < n3 - n; ++n2) {
                double[] arrd7 = arrd3;
                int n10 = n;
                arrd7[n10] = arrd7[n10] + arrd2[n2] * (arrd[n + n2] - arrd[n - n2]);
            }
            for (n2 = n3 - n; n2 < n4; ++n2) {
                double[] arrd8 = arrd3;
                int n11 = n;
                arrd8[n11] = arrd8[n11] + arrd2[n2] * (arrd[n6 - n2 - n] - arrd[n - n2]);
            }
        }
        return arrd3;
    }

    public static double[] movingSum(double[] arrd, int n) {
        int n2;
        int n3 = arrd.length;
        double[] arrd2 = new double[n3 + n - 1];
        arrd2[0] = arrd[0];
        for (n2 = 1; n2 < n; ++n2) {
            double[] arrd3 = arrd2;
            arrd3[0] = arrd3[0] + arrd[n2];
        }
        for (n2 = 1; n2 < n; ++n2) {
            arrd2[n2] = arrd2[n2 - 1] - arrd[n - n2] + arrd[n2];
        }
        for (n2 = n; n2 < n3; ++n2) {
            arrd2[n2] = arrd2[n2 - 1] - arrd[n2 - n] + arrd[n2];
        }
        for (n2 = n3; n2 < n3 + n - 1; ++n2) {
            arrd2[n2] = arrd2[n2 - 1] - arrd[n2 - n] + arrd[2 * n3 - n2 - 2];
        }
        return arrd2;
    }
}

