/*
 * Decompiled with CFR 0_118.
 */
package ijtools;

public class Convolver2D {
    public static double[][] movingSum(double[][] arrd, int n) {
        int n2;
        int n3;
        int n4 = arrd.length;
        int n5 = arrd[0].length;
        double[][] arrd2 = new double[n4 + n - 1][n5 + n - 1];
        for (n2 = 0; n2 < n5; ++n2) {
            arrd2[0][n2] = arrd[0][n2];
            for (n3 = 1; n3 < n; ++n3) {
                double[] arrd3 = arrd2[0];
                int n6 = n2;
                arrd3[n6] = arrd3[n6] + arrd[n3][n2];
            }
            for (n3 = 1; n3 < n; ++n3) {
                arrd2[n3][n2] = arrd2[n3 - 1][n2] - arrd[n - n3][n2] + arrd[n3][n2];
            }
            for (n3 = n; n3 < n4; ++n3) {
                arrd2[n3][n2] = arrd2[n3 - 1][n2] - arrd[n3 - n][n2] + arrd[n3][n2];
            }
            for (n3 = n4; n3 < n4 + n; ++n3) {
                arrd2[n3][n2] = arrd2[n3 - 1][n2] - arrd[n3 - n][n2] + arrd[2 * n4 - n3 - 2][n2];
            }
        }
        for (n2 = 0; n2 < n4 + n - 1; ++n2) {
            arrd2[n2][0] = arrd2[n2][0];
            for (n3 = 1; n3 < n; ++n3) {
                double[] arrd4 = arrd2[n2];
                arrd4[0] = arrd4[0] + arrd2[n2][n3];
            }
            for (n3 = 1; n3 < n; ++n3) {
                arrd2[n2][n3] = arrd2[n2][n3 - 1] - arrd2[n2][n - n3] + arrd2[n2][n3];
            }
            for (n3 = n; n3 < n5; ++n3) {
                arrd2[n2][n3] = arrd2[n2][n3 - 1] - arrd2[n2][n3 - n] + arrd2[n2][n3];
            }
            for (n3 = n5; n3 < n5 + n - n3; ++n3) {
                arrd2[n2][n3] = arrd2[n2][n3 - 1] - arrd2[n2][n3 - n] + arrd2[n2][2 * n5 - n3 - 2];
            }
        }
        return arrd2;
    }

    public static double[][] movingSumX(double[][] arrd, int n) {
        int n2 = arrd.length;
        int n3 = arrd[0].length;
        double[][] arrd2 = new double[n2 + n - 1][n3 + n - 1];
        for (int i = 0; i < n3; ++i) {
            int n4;
            arrd2[0][i] = arrd[0][i];
            for (n4 = 1; n4 < n; ++n4) {
                double[] arrd3 = arrd2[0];
                int n5 = i;
                arrd3[n5] = arrd3[n5] + arrd[n4][i];
            }
            for (n4 = 1; n4 < n; ++n4) {
                arrd2[n4][i] = arrd2[n4 - 1][i] - arrd[n - n4][i] + arrd[n4][i];
            }
            for (n4 = n; n4 < n2; ++n4) {
                arrd2[n4][i] = arrd2[n4 - 1][i] - arrd[n4 - n][i] + arrd[n4][i];
            }
            for (n4 = n2; n4 < n2 + n; ++n4) {
                arrd2[n4][i] = arrd2[n4 - 1][i] - arrd[n4 - n][i] + arrd[2 * n2 - n4 - 2][i];
            }
        }
        return arrd2;
    }

    public static double[][] movingSumY(double[][] arrd, int n) {
        int n2 = arrd.length;
        int n3 = arrd[0].length;
        double[][] arrd2 = new double[n2 + n - 1][n3 + n - 1];
        for (int i = 0; i < n2 + n - 1; ++i) {
            int n4;
            arrd2[i][0] = arrd2[i][0];
            for (n4 = 1; n4 < n; ++n4) {
                double[] arrd3 = arrd2[i];
                arrd3[0] = arrd3[0] + arrd2[i][n4];
            }
            for (n4 = 1; n4 < n; ++n4) {
                arrd2[i][n4] = arrd2[i][n4 - 1] - arrd2[i][n - n4] + arrd2[i][n4];
            }
            for (n4 = n; n4 < n3; ++n4) {
                arrd2[i][n4] = arrd2[i][n4 - 1] - arrd2[i][n4 - n] + arrd2[i][n4];
            }
            for (n4 = n3; n4 < n3 + n - n4; ++n4) {
                arrd2[i][n4] = arrd2[i][n4 - 1] - arrd2[i][n4 - n] + arrd2[i][2 * n3 - n4 - 2];
            }
        }
        return arrd2;
    }

    public static double[][] convolveEvenX(double[][] arrd, double[] arrd2) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n - 2;
        double[][] arrd3 = new double[n][n2];
        boolean bl = false;
        for (int i = 0; i < n2; ++i) {
            int n6;
            int n7;
            for (n6 = 0; n6 < n4; ++n6) {
                arrd3[n6][i] = arrd2[0] * arrd[n6][i];
                for (n7 = 1; n7 <= n6; ++n7) {
                    double[] arrd4 = arrd3[n6];
                    int n8 = i;
                    arrd4[n8] = arrd4[n8] + arrd2[n7] * (arrd[n6 - n7][i] + arrd[n6 + n7][i]);
                }
                for (n7 = n6 + 1; n7 < n3; ++n7) {
                    double[] arrd5 = arrd3[n6];
                    int n9 = i;
                    arrd5[n9] = arrd5[n9] + arrd2[n7] * (arrd[n7 - n6][i] + arrd[n6 + n7][i]);
                }
            }
            for (n6 = n4; n6 <= n - n3; ++n6) {
                arrd3[n6][i] = arrd2[0] * arrd[n6][i];
                for (n7 = 1; n7 < n3; ++n7) {
                    double[] arrd6 = arrd3[n6];
                    int n10 = i;
                    arrd6[n10] = arrd6[n10] + arrd2[n7] * (arrd[n6 - n7][i] + arrd[n6 + n7][i]);
                }
            }
            for (n6 = n - n4; n6 < n; ++n6) {
                arrd3[n6][i] = arrd2[0] * arrd[n6][i];
                for (n7 = 1; n7 < n - n6; ++n7) {
                    double[] arrd7 = arrd3[n6];
                    int n11 = i;
                    arrd7[n11] = arrd7[n11] + arrd2[n7] * (arrd[n6 - n7][i] + arrd[n6 + n7][i]);
                }
                for (n7 = n - n6; n7 < n3; ++n7) {
                    double[] arrd8 = arrd3[n6];
                    int n12 = i;
                    arrd8[n12] = arrd8[n12] + arrd2[n7] * (arrd[n5 - n7 - n6][i] + arrd[n6 - n7][i]);
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveEvenX(double[] arrd, double[] arrd2, int n, int n2) {
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n - 2;
        double[] arrd3 = new double[n * n2];
        int n6 = 0;
        for (int i = 0; i < n2; ++i) {
            int n7;
            int n8;
            for (n7 = 0; n7 < n4; ++n7) {
                arrd3[n6] = arrd2[0] * arrd[n6];
                for (n8 = 1; n8 <= n7; ++n8) {
                    double[] arrd4 = arrd3;
                    int n9 = n6;
                    arrd4[n9] = arrd4[n9] + arrd2[n8] * (arrd[n6 - n8] + arrd[n6 + n8]);
                }
                for (n8 = n7 + 1; n8 < n3; ++n8) {
                    double[] arrd5 = arrd3;
                    int n10 = n6;
                    arrd5[n10] = arrd5[n10] + arrd2[n8] * (arrd[n8 - n7 + i * n] + arrd[n6 + n8]);
                }
                ++n6;
            }
            for (n7 = n4; n7 <= n - n3; ++n7) {
                arrd3[n6] = arrd2[0] * arrd[n6];
                for (n8 = 1; n8 < n3; ++n8) {
                    double[] arrd6 = arrd3;
                    int n11 = n6;
                    arrd6[n11] = arrd6[n11] + arrd2[n8] * (arrd[n6 - n8] + arrd[n6 + n8]);
                }
                ++n6;
            }
            for (n7 = n - n4; n7 < n; ++n7) {
                arrd3[n6] = arrd2[0] * arrd[n6];
                for (n8 = 1; n8 < n - n7; ++n8) {
                    double[] arrd7 = arrd3;
                    int n12 = n6;
                    arrd7[n12] = arrd7[n12] + arrd2[n8] * (arrd[n6 - n8] + arrd[n6 + n8]);
                }
                for (n8 = n - n7; n8 < n3; ++n8) {
                    double[] arrd8 = arrd3;
                    int n13 = n6;
                    arrd8[n13] = arrd8[n13] + arrd2[n8] * (arrd[n5 - n8 - n7 + i * n] + arrd[n6 - n8]);
                }
                ++n6;
            }
        }
        return arrd3;
    }

    public static float[] convolveEvenX(float[] arrf, float[] arrf2, int n, int n2) {
        int n3 = arrf2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n - 2;
        float[] arrf3 = new float[n * n2];
        int n6 = 0;
        for (int i = 0; i < n2; ++i) {
            int n7;
            int n8;
            for (n7 = 0; n7 < n4; ++n7) {
                arrf3[n6] = arrf2[0] * arrf[n6];
                for (n8 = 1; n8 <= n7; ++n8) {
                    float[] arrf4 = arrf3;
                    int n9 = n6;
                    arrf4[n9] = arrf4[n9] + arrf2[n8] * (arrf[n6 - n8] + arrf[n6 + n8]);
                }
                for (n8 = n7 + 1; n8 < n3; ++n8) {
                    float[] arrf5 = arrf3;
                    int n10 = n6;
                    arrf5[n10] = arrf5[n10] + arrf2[n8] * (arrf[n8 - n7 + i * n] + arrf[n6 + n8]);
                }
                ++n6;
            }
            for (n7 = n4; n7 <= n - n3; ++n7) {
                arrf3[n6] = arrf2[0] * arrf[n6];
                for (n8 = 1; n8 < n3; ++n8) {
                    float[] arrf6 = arrf3;
                    int n11 = n6;
                    arrf6[n11] = arrf6[n11] + arrf2[n8] * (arrf[n6 - n8] + arrf[n6 + n8]);
                }
                ++n6;
            }
            for (n7 = n - n4; n7 < n; ++n7) {
                arrf3[n6] = arrf2[0] * arrf[n6];
                for (n8 = 1; n8 < n - n7; ++n8) {
                    float[] arrf7 = arrf3;
                    int n12 = n6;
                    arrf7[n12] = arrf7[n12] + arrf2[n8] * (arrf[n6 - n8] + arrf[n6 + n8]);
                }
                for (n8 = n - n7; n8 < n3; ++n8) {
                    float[] arrf8 = arrf3;
                    int n13 = n6;
                    arrf8[n13] = arrf8[n13] + arrf2[n8] * (arrf[n5 - n8 - n7 + i * n] + arrf[n6 - n8]);
                }
                ++n6;
            }
        }
        return arrf3;
    }

    public static double[][] convolveEvenY(double[][] arrd, double[] arrd2) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n2 - 2;
        double[][] arrd3 = new double[n][n2];
        for (int i = 0; i < n; ++i) {
            int n6;
            int n7;
            for (n7 = 0; n7 < n4; ++n7) {
                arrd3[i][n7] = arrd2[0] * arrd[i][n7];
                for (n6 = 1; n6 <= n7; ++n6) {
                    double[] arrd4 = arrd3[i];
                    int n8 = n7;
                    arrd4[n8] = arrd4[n8] + arrd2[n6] * (arrd[i][n7 - n6] + arrd[i][n7 + n6]);
                }
                for (n6 = n7 + 1; n6 < n3; ++n6) {
                    double[] arrd5 = arrd3[i];
                    int n9 = n7;
                    arrd5[n9] = arrd5[n9] + arrd2[n6] * (arrd[i][n6 - n7] + arrd[i][n7 + n6]);
                }
            }
            for (n7 = n4; n7 <= n2 - n3; ++n7) {
                arrd3[i][n7] = arrd2[0] * arrd[i][n7];
                for (n6 = 1; n6 < n3; ++n6) {
                    double[] arrd6 = arrd3[i];
                    int n10 = n7;
                    arrd6[n10] = arrd6[n10] + arrd2[n6] * (arrd[i][n7 - n6] + arrd[i][n7 + n6]);
                }
            }
            for (n7 = n2 - n4; n7 < n2; ++n7) {
                arrd3[i][n7] = arrd2[0] * arrd[i][n7];
                for (n6 = 1; n6 < n2 - n7; ++n6) {
                    double[] arrd7 = arrd3[i];
                    int n11 = n7;
                    arrd7[n11] = arrd7[n11] + arrd2[n6] * (arrd[i][n7 - n6] + arrd[i][n7 + n6]);
                }
                for (n6 = n2 - n7; n6 < n3; ++n6) {
                    double[] arrd8 = arrd3[i];
                    int n12 = n7;
                    arrd8[n12] = arrd8[n12] + arrd2[n6] * (arrd[i][n5 - n6 - n7] + arrd[i][n7 - n6]);
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveEvenY(double[] arrd, double[] arrd2, int n, int n2) {
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n2 - 2;
        double[] arrd3 = new double[n * n2];
        for (int i = 0; i < n; ++i) {
            int n6;
            int n7;
            int n8;
            int n9;
            for (n9 = 0; n9 < n4; ++n9) {
                n7 = i + n9 * n;
                arrd3[n7] = arrd2[0] * arrd[n7];
                for (n8 = 1; n8 <= n9; ++n8) {
                    n6 = n8 * n;
                    double[] arrd4 = arrd3;
                    int n10 = n7;
                    arrd4[n10] = arrd4[n10] + arrd2[n8] * (arrd[n7 - n6] + arrd[n7 + n6]);
                }
                for (n8 = n9 + 1; n8 < n3; ++n8) {
                    double[] arrd5 = arrd3;
                    int n11 = n7;
                    arrd5[n11] = arrd5[n11] + arrd2[n8] * (arrd[(n8 - n9) * n + i] + arrd[n7 + n8 * n]);
                }
            }
            for (n9 = n4; n9 <= n2 - n3; ++n9) {
                n7 = i + n9 * n;
                arrd3[n7] = arrd2[0] * arrd[n7];
                for (n8 = 1; n8 < n3; ++n8) {
                    n6 = n8 * n;
                    double[] arrd6 = arrd3;
                    int n12 = n7;
                    arrd6[n12] = arrd6[n12] + arrd2[n8] * (arrd[n7 - n6] + arrd[n7 + n6]);
                }
            }
            for (n9 = n2 - n4; n9 < n2; ++n9) {
                n7 = i + n9 * n;
                arrd3[n7] = arrd2[0] * arrd[n7];
                for (n8 = 1; n8 < n2 - n9; ++n8) {
                    n6 = n8 * n;
                    double[] arrd7 = arrd3;
                    int n13 = n7;
                    arrd7[n13] = arrd7[n13] + arrd2[n8] * (arrd[n7 - n6] + arrd[n7 + n6]);
                }
                for (n8 = n2 - n9; n8 < n3; ++n8) {
                    double[] arrd8 = arrd3;
                    int n14 = n7;
                    arrd8[n14] = arrd8[n14] + arrd2[n8] * (arrd[(n5 - n8 - n9) * n + i] + arrd[n7 - n8 * n]);
                }
            }
        }
        return arrd3;
    }

    public static float[] convolveEvenY(float[] arrf, float[] arrf2, int n, int n2) {
        int n3 = arrf2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n2 - 2;
        float[] arrf3 = new float[n * n2];
        for (int i = 0; i < n; ++i) {
            int n6;
            int n7;
            int n8;
            int n9;
            for (n9 = 0; n9 < n4; ++n9) {
                n7 = i + n9 * n;
                arrf3[n7] = arrf2[0] * arrf[n7];
                for (n8 = 1; n8 <= n9; ++n8) {
                    n6 = n8 * n;
                    float[] arrf4 = arrf3;
                    int n10 = n7;
                    arrf4[n10] = arrf4[n10] + arrf2[n8] * (arrf[n7 - n6] + arrf[n7 + n6]);
                }
                for (n8 = n9 + 1; n8 < n3; ++n8) {
                    float[] arrf5 = arrf3;
                    int n11 = n7;
                    arrf5[n11] = arrf5[n11] + arrf2[n8] * (arrf[(n8 - n9) * n + i] + arrf[n7 + n8 * n]);
                }
            }
            for (n9 = n4; n9 <= n2 - n3; ++n9) {
                n7 = i + n9 * n;
                arrf3[n7] = arrf2[0] * arrf[n7];
                for (n8 = 1; n8 < n3; ++n8) {
                    n6 = n8 * n;
                    float[] arrf6 = arrf3;
                    int n12 = n7;
                    arrf6[n12] = arrf6[n12] + arrf2[n8] * (arrf[n7 - n6] + arrf[n7 + n6]);
                }
            }
            for (n9 = n2 - n4; n9 < n2; ++n9) {
                n7 = i + n9 * n;
                arrf3[n7] = arrf2[0] * arrf[n7];
                for (n8 = 1; n8 < n2 - n9; ++n8) {
                    n6 = n8 * n;
                    float[] arrf7 = arrf3;
                    int n13 = n7;
                    arrf7[n13] = arrf7[n13] + arrf2[n8] * (arrf[n7 - n6] + arrf[n7 + n6]);
                }
                for (n8 = n2 - n9; n8 < n3; ++n8) {
                    float[] arrf8 = arrf3;
                    int n14 = n7;
                    arrf8[n14] = arrf8[n14] + arrf2[n8] * (arrf[(n5 - n8 - n9) * n + i] + arrf[n7 - n8 * n]);
                }
            }
        }
        return arrf3;
    }

    public static double[][] convolveOddX(double[][] arrd, double[] arrd2) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n - 2;
        double[][] arrd3 = new double[n][n2];
        for (int i = 0; i < n2; ++i) {
            int n6;
            int n7;
            for (n7 = 0; n7 < n4; ++n7) {
                arrd3[n7][i] = 0.0;
                for (n6 = 1; n6 <= n7; ++n6) {
                    double[] arrd4 = arrd3[n7];
                    int n8 = i;
                    arrd4[n8] = arrd4[n8] + arrd2[n6] * (arrd[n7 + n6][i] - arrd[n7 - n6][i]);
                }
                for (n6 = n7 + 1; n6 < n3; ++n6) {
                    double[] arrd5 = arrd3[n7];
                    int n9 = i;
                    arrd5[n9] = arrd5[n9] + arrd2[n6] * (arrd[n7 + n6][i] - arrd[n6 - n7][i]);
                }
            }
            for (n7 = n4; n7 <= n - n3; ++n7) {
                arrd3[n7][i] = 0.0;
                for (n6 = 1; n6 < n3; ++n6) {
                    double[] arrd6 = arrd3[n7];
                    int n10 = i;
                    arrd6[n10] = arrd6[n10] + arrd2[n6] * (arrd[n7 + n6][i] - arrd[n7 - n6][i]);
                }
            }
            for (n7 = n - n4; n7 < n; ++n7) {
                arrd3[n7][i] = 0.0;
                for (n6 = 1; n6 < n - n7; ++n6) {
                    double[] arrd7 = arrd3[n7];
                    int n11 = i;
                    arrd7[n11] = arrd7[n11] + arrd2[n6] * (arrd[n7 + n6][i] - arrd[n7 - n6][i]);
                }
                for (n6 = n - n7; n6 < n3; ++n6) {
                    double[] arrd8 = arrd3[n7];
                    int n12 = i;
                    arrd8[n12] = arrd8[n12] + arrd2[n6] * (arrd[n5 - n6 - n7][i] - arrd[n7 - n6][i]);
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveOddX(double[] arrd, double[] arrd2, int n, int n2) {
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n - 2;
        double[] arrd3 = new double[n * n2];
        int n6 = 0;
        for (int i = 0; i < n2; ++i) {
            int n7;
            int n8;
            for (n7 = 0; n7 < n4; ++n7) {
                arrd3[n6] = 0.0;
                for (n8 = 1; n8 <= n7; ++n8) {
                    double[] arrd4 = arrd3;
                    int n9 = n6;
                    arrd4[n9] = arrd4[n9] + arrd2[n8] * (arrd[n6 + n8] - arrd[n6 - n8]);
                }
                for (n8 = n7 + 1; n8 < n3; ++n8) {
                    double[] arrd5 = arrd3;
                    int n10 = n6;
                    arrd5[n10] = arrd5[n10] + arrd2[n8] * (arrd[n6 + n8] - arrd[n8 - n7 + i * n]);
                }
                ++n6;
            }
            for (n7 = n4; n7 <= n - n3; ++n7) {
                arrd3[n6] = 0.0;
                for (n8 = 1; n8 < n3; ++n8) {
                    double[] arrd6 = arrd3;
                    int n11 = n6;
                    arrd6[n11] = arrd6[n11] + arrd2[n8] * (arrd[n6 + n8] - arrd[n6 - n8]);
                }
                ++n6;
            }
            for (n7 = n - n4; n7 < n; ++n7) {
                arrd3[n6] = 0.0;
                for (n8 = 1; n8 < n - n7; ++n8) {
                    double[] arrd7 = arrd3;
                    int n12 = n6;
                    arrd7[n12] = arrd7[n12] + arrd2[n8] * (arrd[n6 + n8] - arrd[n6 - n8]);
                }
                for (n8 = n - n7; n8 < n3; ++n8) {
                    double[] arrd8 = arrd3;
                    int n13 = n6;
                    arrd8[n13] = arrd8[n13] + arrd2[n8] * (arrd[n5 - n8 - n7 + i * n] - arrd[n6 - n8]);
                }
                ++n6;
            }
        }
        return arrd3;
    }

    public static float[] convolveOddX(float[] arrf, float[] arrf2, int n, int n2) {
        int n3 = arrf2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n - 2;
        float[] arrf3 = new float[n * n2];
        int n6 = 0;
        for (int i = 0; i < n2; ++i) {
            int n7;
            int n8;
            for (n7 = 0; n7 < n4; ++n7) {
                arrf3[n6] = 0.0f;
                for (n8 = 1; n8 <= n7; ++n8) {
                    float[] arrf4 = arrf3;
                    int n9 = n6;
                    arrf4[n9] = arrf4[n9] + arrf2[n8] * (arrf[n6 + n8] - arrf[n6 - n8]);
                }
                for (n8 = n7 + 1; n8 < n3; ++n8) {
                    float[] arrf5 = arrf3;
                    int n10 = n6;
                    arrf5[n10] = arrf5[n10] + arrf2[n8] * (arrf[n6 + n8] - arrf[n8 - n7 + i * n]);
                }
                ++n6;
            }
            for (n7 = n4; n7 <= n - n3; ++n7) {
                arrf3[n6] = 0.0f;
                for (n8 = 1; n8 < n3; ++n8) {
                    float[] arrf6 = arrf3;
                    int n11 = n6;
                    arrf6[n11] = arrf6[n11] + arrf2[n8] * (arrf[n6 + n8] - arrf[n6 - n8]);
                }
                ++n6;
            }
            for (n7 = n - n4; n7 < n; ++n7) {
                arrf3[n6] = 0.0f;
                for (n8 = 1; n8 < n - n7; ++n8) {
                    float[] arrf7 = arrf3;
                    int n12 = n6;
                    arrf7[n12] = arrf7[n12] + arrf2[n8] * (arrf[n6 + n8] - arrf[n6 - n8]);
                }
                for (n8 = n - n7; n8 < n3; ++n8) {
                    float[] arrf8 = arrf3;
                    int n13 = n6;
                    arrf8[n13] = arrf8[n13] + arrf2[n8] * (arrf[n5 - n8 - n7 + i * n] - arrf[n6 - n8]);
                }
                ++n6;
            }
        }
        return arrf3;
    }

    public static double[][] convolveOddY(double[][] arrd, double[] arrd2) {
        int n = arrd.length;
        int n2 = arrd.length;
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n2 - 2;
        double[][] arrd3 = new double[n][n2];
        for (int i = 0; i < n; ++i) {
            int n6;
            int n7;
            for (n7 = 0; n7 < n4; ++n7) {
                arrd3[i][n7] = 0.0;
                for (n6 = 1; n6 <= n7; ++n6) {
                    double[] arrd4 = arrd3[i];
                    int n8 = n7;
                    arrd4[n8] = arrd4[n8] + arrd2[n6] * (arrd[i][n7 + n6] - arrd[i][n7 - n6]);
                }
                for (n6 = n7 + 1; n6 < n3; ++n6) {
                    double[] arrd5 = arrd3[i];
                    int n9 = n7;
                    arrd5[n9] = arrd5[n9] + arrd2[n6] * (arrd[i][n7 + n6] - arrd[i][n6 - n7]);
                }
            }
            for (n7 = n4; n7 <= n2 - n3; ++n7) {
                arrd3[i][n7] = 0.0;
                for (n6 = 1; n6 < n3; ++n6) {
                    double[] arrd6 = arrd3[i];
                    int n10 = n7;
                    arrd6[n10] = arrd6[n10] + arrd2[n6] * (arrd[i][n7 + n6] - arrd[i][n7 - n6]);
                }
            }
            for (n7 = n2 - n4; n7 < n2; ++n7) {
                arrd3[i][n7] = 0.0;
                for (n6 = 1; n6 < n2 - n7; ++n6) {
                    double[] arrd7 = arrd3[i];
                    int n11 = n7;
                    arrd7[n11] = arrd7[n11] + arrd2[n6] * (arrd[i][n7 + n6] - arrd[i][n7 - n6]);
                }
                for (n6 = n2 - n7; n6 < n3; ++n6) {
                    double[] arrd8 = arrd3[i];
                    int n12 = n7;
                    arrd8[n12] = arrd8[n12] + arrd2[n6] * (arrd[i][n5 - n6 - n7] - arrd[i][n7 - n6]);
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveOddY(double[] arrd, double[] arrd2, int n, int n2) {
        int n3 = arrd2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n2 - 2;
        double[] arrd3 = new double[n * n2];
        for (int i = 0; i < n; ++i) {
            int n6;
            int n7;
            int n8;
            int n9;
            for (n9 = 0; n9 < n4; ++n9) {
                n7 = i + n9 * n;
                arrd3[n7] = 0.0;
                for (n8 = 1; n8 <= n9; ++n8) {
                    n6 = n8 * n;
                    double[] arrd4 = arrd3;
                    int n10 = n7;
                    arrd4[n10] = arrd4[n10] + arrd2[n8] * (arrd[n7 + n6] - arrd[n7 - n6]);
                }
                for (n8 = n9 + 1; n8 < n3; ++n8) {
                    double[] arrd5 = arrd3;
                    int n11 = n7;
                    arrd5[n11] = arrd5[n11] + arrd2[n8] * (arrd[n7 + n8 * n] - arrd[(n8 - n9) * n + i]);
                }
            }
            for (n9 = n4; n9 <= n2 - n3; ++n9) {
                n7 = i + n9 * n;
                arrd3[n7] = 0.0;
                for (n8 = 1; n8 < n3; ++n8) {
                    n6 = n8 * n;
                    double[] arrd6 = arrd3;
                    int n12 = n7;
                    arrd6[n12] = arrd6[n12] + arrd2[n8] * (arrd[n7 + n6] - arrd[n7 - n6]);
                }
            }
            for (n9 = n2 - n4; n9 < n2; ++n9) {
                n7 = i + n9 * n;
                arrd3[n7] = 0.0;
                for (n8 = 1; n8 < n2 - n9; ++n8) {
                    n6 = n8 * n;
                    double[] arrd7 = arrd3;
                    int n13 = n7;
                    arrd7[n13] = arrd7[n13] + arrd2[n8] * (arrd[n7 + n6] - arrd[n7 - n6]);
                }
                for (n8 = n2 - n9; n8 < n3; ++n8) {
                    double[] arrd8 = arrd3;
                    int n14 = n7;
                    arrd8[n14] = arrd8[n14] + arrd2[n8] * (arrd[(n5 - n8 - n9) * n + i] - arrd[n7 - n8 * n]);
                }
            }
        }
        return arrd3;
    }

    public static float[] convolveOddY(float[] arrf, float[] arrf2, int n, int n2) {
        int n3 = arrf2.length;
        int n4 = n3 - 1;
        int n5 = 2 * n2 - 2;
        float[] arrf3 = new float[n * n2];
        for (int i = 0; i < n; ++i) {
            int n6;
            int n7;
            int n8;
            int n9;
            for (n9 = 0; n9 < n4; ++n9) {
                n7 = i + n9 * n;
                arrf3[n7] = 0.0f;
                for (n8 = 1; n8 <= n9; ++n8) {
                    n6 = n8 * n;
                    float[] arrf4 = arrf3;
                    int n10 = n7;
                    arrf4[n10] = arrf4[n10] + arrf2[n8] * (arrf[n7 + n6] - arrf[n7 - n6]);
                }
                for (n8 = n9 + 1; n8 < n3; ++n8) {
                    float[] arrf5 = arrf3;
                    int n11 = n7;
                    arrf5[n11] = arrf5[n11] + arrf2[n8] * (arrf[n7 + n8 * n] - arrf[(n8 - n9) * n + i]);
                }
            }
            for (n9 = n4; n9 <= n2 - n3; ++n9) {
                n7 = i + n9 * n;
                arrf3[n7] = 0.0f;
                for (n8 = 1; n8 < n3; ++n8) {
                    n6 = n8 * n;
                    float[] arrf6 = arrf3;
                    int n12 = n7;
                    arrf6[n12] = arrf6[n12] + arrf2[n8] * (arrf[n7 + n6] - arrf[n7 - n6]);
                }
            }
            for (n9 = n2 - n4; n9 < n2; ++n9) {
                n7 = i + n9 * n;
                arrf3[n7] = 0.0f;
                for (n8 = 1; n8 < n2 - n9; ++n8) {
                    n6 = n8 * n;
                    float[] arrf7 = arrf3;
                    int n13 = n7;
                    arrf7[n13] = arrf7[n13] + arrf2[n8] * (arrf[n7 + n6] - arrf[n7 - n6]);
                }
                for (n8 = n2 - n9; n8 < n3; ++n8) {
                    float[] arrf8 = arrf3;
                    int n14 = n7;
                    arrf8[n14] = arrf8[n14] + arrf2[n8] * (arrf[(n5 - n8 - n9) * n + i] - arrf[n7 - n8 * n]);
                }
            }
        }
        return arrf3;
    }

    public static double[] convolveEvenX(double[] arrd, double[] arrd2, int[] arrn, int n, int n2, int n3, int n4) {
        int n5 = arrn[0];
        int n6 = arrn[1];
        int n7 = n5 * n6;
        int n8 = arrd2.length;
        int n9 = n8 - 1;
        int n10 = 2 * n5 - 2;
        double[] arrd3 = new double[(n2 - n + 1) * (n4 - n3 + 1)];
        int n11 = 0;
        if (n < n9 && n2 > n5 - n8) {
            for (int i = n3; i <= n4; ++i) {
                int n12;
                int n13;
                int n14;
                for (n13 = n; n13 < n9; ++n13) {
                    n14 = n13 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n14];
                    for (n12 = 1; n12 <= n13; ++n12) {
                        double[] arrd4 = arrd3;
                        int n15 = n11;
                        arrd4[n15] = arrd4[n15] + arrd2[n12] * (arrd[n14 - n12] + arrd[n14 + n12]);
                    }
                    for (n12 = n13 + 1; n12 < n8; ++n12) {
                        double[] arrd5 = arrd3;
                        int n16 = n11;
                        arrd5[n16] = arrd5[n16] + arrd2[n12] * (arrd[n12 - n13 + i * n5] + arrd[n14 + n12]);
                    }
                    ++n11;
                }
                for (n13 = n9; n13 < n2 - n9; ++n13) {
                    n14 = n13 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n14];
                    for (n12 = 1; n12 < n8; ++n12) {
                        double[] arrd6 = arrd3;
                        int n17 = n11;
                        arrd6[n17] = arrd6[n17] + arrd2[n12] * (arrd[n14 - n12] + arrd[n14 + n12]);
                    }
                    ++n11;
                }
                for (n13 = n2 - n9; n13 <= n2; ++n13) {
                    n14 = n13 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n14];
                    for (n12 = 1; n12 < n5 - n13; ++n12) {
                        double[] arrd7 = arrd3;
                        int n18 = n11;
                        arrd7[n18] = arrd7[n18] + arrd2[n12] * (arrd[n14 - n12] + arrd[n14 + n12]);
                    }
                    for (n12 = n5 - n13; n12 < n8; ++n12) {
                        double[] arrd8 = arrd3;
                        int n19 = n11;
                        arrd8[n19] = arrd8[n19] + arrd2[n12] * (arrd[n10 - n12 - n13 + i * n5] + arrd[n14 - n12]);
                    }
                    ++n11;
                }
            }
        } else if (n < n9 && n2 <= n5 - n8) {
            for (int i = n3; i <= n4; ++i) {
                int n20;
                int n21;
                int n22;
                for (n22 = n; n22 < n9; ++n22) {
                    n21 = n22 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n21];
                    for (n20 = 1; n20 <= n22; ++n20) {
                        double[] arrd9 = arrd3;
                        int n23 = n11;
                        arrd9[n23] = arrd9[n23] + arrd2[n20] * (arrd[n21 - n20] + arrd[n21 + n20]);
                    }
                    for (n20 = n22 + 1; n20 < n8; ++n20) {
                        double[] arrd10 = arrd3;
                        int n24 = n11;
                        arrd10[n24] = arrd10[n24] + arrd2[n20] * (arrd[n20 - n22 + i * n5] + arrd[n21 + n20]);
                    }
                    ++n11;
                }
                for (n22 = n9; n22 <= n2; ++n22) {
                    n21 = n22 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n21];
                    for (n20 = 1; n20 < n8; ++n20) {
                        double[] arrd11 = arrd3;
                        int n25 = n11;
                        arrd11[n25] = arrd11[n25] + arrd2[n20] * (arrd[n21 - n20] + arrd[n21 + n20]);
                    }
                    ++n11;
                }
            }
        } else if (n >= n9 && n2 > n5 - n8) {
            for (int i = n3; i <= n4; ++i) {
                int n26;
                int n27;
                int n28;
                for (n28 = n; n28 < n2 - n9; ++n28) {
                    n27 = n28 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n27];
                    for (n26 = 1; n26 < n8; ++n26) {
                        double[] arrd12 = arrd3;
                        int n29 = n11;
                        arrd12[n29] = arrd12[n29] + arrd2[n26] * (arrd[n27 - n26] + arrd[n27 + n26]);
                    }
                    ++n11;
                }
                for (n28 = n2 - n9; n28 <= n2; ++n28) {
                    n27 = n28 + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n27];
                    for (n26 = 1; n26 < n5 - n28; ++n26) {
                        double[] arrd13 = arrd3;
                        int n30 = n11;
                        arrd13[n30] = arrd13[n30] + arrd2[n26] * (arrd[n27 - n26] + arrd[n27 + n26]);
                    }
                    for (n26 = n5 - n28; n26 < n8; ++n26) {
                        double[] arrd14 = arrd3;
                        int n31 = n11;
                        arrd14[n31] = arrd14[n31] + arrd2[n26] * (arrd[n10 - n26 - n28 + i * n5] + arrd[n27 - n26]);
                    }
                    ++n11;
                }
            }
        } else {
            for (int i = n3; i <= n4; ++i) {
                for (int j = n; j <= n2; ++j) {
                    int n32 = j + i * n5;
                    arrd3[n11] = arrd2[0] * arrd[n32];
                    for (int k = 1; k < n8; ++k) {
                        double[] arrd15 = arrd3;
                        int n33 = n11;
                        arrd15[n33] = arrd15[n33] + arrd2[k] * (arrd[n32 - k] + arrd[n32 + k]);
                    }
                    ++n11;
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveOddX(double[] arrd, double[] arrd2, int[] arrn, int n, int n2, int n3, int n4) {
        int n5 = arrn[0];
        int n6 = arrn[1];
        int n7 = n5 * n6;
        int n8 = arrd2.length;
        int n9 = n8 - 1;
        int n10 = 2 * n5 - 2;
        double[] arrd3 = new double[(n2 - n + 1) * (n4 - n3 + 1)];
        int n11 = 0;
        if (n < n9 && n2 > n5 - n8) {
            for (int i = n3; i <= n4; ++i) {
                int n12;
                int n13;
                int n14;
                for (n13 = n; n13 < n9; ++n13) {
                    n14 = n13 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n12 = 1; n12 <= n13; ++n12) {
                        double[] arrd4 = arrd3;
                        int n15 = n11;
                        arrd4[n15] = arrd4[n15] + arrd2[n12] * (arrd[n14 + n12] - arrd[n14 - n12]);
                    }
                    for (n12 = n13 + 1; n12 < n8; ++n12) {
                        double[] arrd5 = arrd3;
                        int n16 = n11;
                        arrd5[n16] = arrd5[n16] + arrd2[n12] * (arrd[n14 + n12] - arrd[n12 - n13 + i * n5]);
                    }
                    ++n11;
                }
                for (n13 = n9; n13 < n2 - n9; ++n13) {
                    n14 = n13 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n12 = 1; n12 < n8; ++n12) {
                        double[] arrd6 = arrd3;
                        int n17 = n11;
                        arrd6[n17] = arrd6[n17] + arrd2[n12] * (arrd[n14 + n12] - arrd[n14 - n12]);
                    }
                    ++n11;
                }
                for (n13 = n2 - n9; n13 <= n2; ++n13) {
                    n14 = n13 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n12 = 1; n12 < n5 - n13; ++n12) {
                        double[] arrd7 = arrd3;
                        int n18 = n11;
                        arrd7[n18] = arrd7[n18] + arrd2[n12] * (arrd[n14 + n12] - arrd[n14 - n12]);
                    }
                    for (n12 = n5 - n13; n12 < n8; ++n12) {
                        double[] arrd8 = arrd3;
                        int n19 = n11;
                        arrd8[n19] = arrd8[n19] + arrd2[n12] * (arrd[n10 - n12 - n13 + i * n5] - arrd[n14 - n12]);
                    }
                    ++n11;
                }
            }
        } else if (n < n9 && n2 <= n5 - n8) {
            for (int i = n3; i <= n4; ++i) {
                int n20;
                int n21;
                int n22;
                for (n22 = n; n22 < n9; ++n22) {
                    n21 = n22 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n20 = 1; n20 <= n22; ++n20) {
                        double[] arrd9 = arrd3;
                        int n23 = n11;
                        arrd9[n23] = arrd9[n23] + arrd2[n20] * (arrd[n21 + n20] - arrd[n21 - n20]);
                    }
                    for (n20 = n22 + 1; n20 < n8; ++n20) {
                        double[] arrd10 = arrd3;
                        int n24 = n11;
                        arrd10[n24] = arrd10[n24] + arrd2[n20] * (arrd[n21 + n20] - arrd[n20 - n22 + i * n5]);
                    }
                    ++n11;
                }
                for (n22 = n9; n22 <= n2; ++n22) {
                    n21 = n22 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n20 = 1; n20 < n8; ++n20) {
                        double[] arrd11 = arrd3;
                        int n25 = n11;
                        arrd11[n25] = arrd11[n25] + arrd2[n20] * (arrd[n21 + n20] - arrd[n21 - n20]);
                    }
                    ++n11;
                }
            }
        } else if (n >= n9 && n2 > n5 - n8) {
            for (int i = n3; i <= n4; ++i) {
                int n26;
                int n27;
                int n28;
                for (n28 = n; n28 < n2 - n9; ++n28) {
                    n27 = n28 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n26 = 1; n26 < n8; ++n26) {
                        double[] arrd12 = arrd3;
                        int n29 = n11;
                        arrd12[n29] = arrd12[n29] + arrd2[n26] * (arrd[n27 + n26] - arrd[n27 - n26]);
                    }
                    ++n11;
                }
                for (n28 = n2 - n9; n28 <= n2; ++n28) {
                    n27 = n28 + i * n5;
                    arrd3[n11] = 0.0;
                    for (n26 = 1; n26 < n5 - n28; ++n26) {
                        double[] arrd13 = arrd3;
                        int n30 = n11;
                        arrd13[n30] = arrd13[n30] + arrd2[n26] * (arrd[n27 + n26] - arrd[n27 - n26]);
                    }
                    for (n26 = n5 - n28; n26 < n8; ++n26) {
                        double[] arrd14 = arrd3;
                        int n31 = n11;
                        arrd14[n31] = arrd14[n31] + arrd2[n26] * (arrd[n10 - n26 - n28 + i * n5] - arrd[n27 - n26]);
                    }
                    ++n11;
                }
            }
        } else {
            for (int i = n3; i <= n4; ++i) {
                for (int j = n; j <= n2; ++j) {
                    int n32 = j + i * n5;
                    arrd3[n11] = 0.0;
                    for (int k = 1; k < n8; ++k) {
                        double[] arrd15 = arrd3;
                        int n33 = n11;
                        arrd15[n33] = arrd15[n33] + arrd2[k] * (arrd[n32 + k] - arrd[n32 - k]);
                    }
                    ++n11;
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveEvenY(double[] arrd, double[] arrd2, int[] arrn, int n, int n2) {
        int n3 = arrn[0];
        int n4 = arrn[1];
        int n5 = n3 * n4;
        int n6 = arrd2.length;
        int n7 = n6 - 1;
        int n8 = 2 * n4 - 2;
        double[] arrd3 = new double[n3 * (n2 - n + 1)];
        int n9 = 0;
        if (n < n7 && n2 > n4 - n6) {
            for (int i = 0; i < n3; ++i) {
                int n10;
                int n11;
                int n12;
                int n13;
                for (n11 = n; n11 < n7; ++n11) {
                    n13 = i + n11 * n3;
                    n9 = n13 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n13];
                    for (n12 = 1; n12 <= n11; ++n12) {
                        n10 = n12 * n3;
                        double[] arrd4 = arrd3;
                        int n14 = n9;
                        arrd4[n14] = arrd4[n14] + arrd2[n12] * (arrd[n13 - n10] + arrd[n13 + n10]);
                    }
                    for (n12 = n11 + 1; n12 < n6; ++n12) {
                        double[] arrd5 = arrd3;
                        int n15 = n9;
                        arrd5[n15] = arrd5[n15] + arrd2[n12] * (arrd[(n12 - n11) * n3 + i] + arrd[n13 + n12 * n3]);
                    }
                }
                for (n11 = n7; n11 < n2 - n7; ++n11) {
                    n13 = i + n11 * n3;
                    n9 = n13 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n13];
                    for (n12 = 1; n12 < n6; ++n12) {
                        n10 = n12 * n3;
                        double[] arrd6 = arrd3;
                        int n16 = n9;
                        arrd6[n16] = arrd6[n16] + arrd2[n12] * (arrd[n13 - n10] + arrd[n13 + n10]);
                    }
                }
                for (n11 = n2 - n7; n11 <= n2; ++n11) {
                    n13 = i + n11 * n3;
                    n9 = n13 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n13];
                    for (n12 = 1; n12 < n4 - n11; ++n12) {
                        n10 = n12 * n3;
                        double[] arrd7 = arrd3;
                        int n17 = n9;
                        arrd7[n17] = arrd7[n17] + arrd2[n12] * (arrd[n13 - n10] + arrd[n13 + n10]);
                    }
                    for (n12 = n4 - n11; n12 < n6; ++n12) {
                        double[] arrd8 = arrd3;
                        int n18 = n9;
                        arrd8[n18] = arrd8[n18] + arrd2[n12] * (arrd[(n8 - n12 - n11) * n3 + i] + arrd[n13 - n12 * n3]);
                    }
                }
            }
        } else if (n < n7 && n2 <= n4 - n6) {
            for (int i = 0; i < n3; ++i) {
                int n19;
                int n20;
                int n21;
                int n22;
                for (n20 = n; n20 < n7; ++n20) {
                    n21 = i + n20 * n3;
                    n9 = n21 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n21];
                    for (n22 = 1; n22 <= n20; ++n22) {
                        n19 = n22 * n3;
                        double[] arrd9 = arrd3;
                        int n23 = n9;
                        arrd9[n23] = arrd9[n23] + arrd2[n22] * (arrd[n21 - n19] + arrd[n21 + n19]);
                    }
                    for (n22 = n20 + 1; n22 < n6; ++n22) {
                        double[] arrd10 = arrd3;
                        int n24 = n9;
                        arrd10[n24] = arrd10[n24] + arrd2[n22] * (arrd[(n22 - n20) * n3 + i] + arrd[n21 + n22 * n3]);
                    }
                }
                for (n20 = n7; n20 <= n2; ++n20) {
                    n21 = i + n20 * n3;
                    n9 = n21 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n21];
                    for (n22 = 1; n22 < n6; ++n22) {
                        n19 = n22 * n3;
                        double[] arrd11 = arrd3;
                        int n25 = n9;
                        arrd11[n25] = arrd11[n25] + arrd2[n22] * (arrd[n21 - n19] + arrd[n21 + n19]);
                    }
                }
            }
        } else if (n >= n7 && n2 > n4 - n6) {
            for (int i = 0; i < n3; ++i) {
                int n26;
                int n27;
                int n28;
                int n29;
                for (n28 = n; n28 < n4 - n7; ++n28) {
                    n27 = i + n28 * n3;
                    n9 = n27 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n27];
                    for (n29 = 1; n29 < n6; ++n29) {
                        n26 = n29 * n3;
                        double[] arrd12 = arrd3;
                        int n30 = n9;
                        arrd12[n30] = arrd12[n30] + arrd2[n29] * (arrd[n27 - n26] + arrd[n27 + n26]);
                    }
                }
                for (n28 = n4 - n7; n28 <= n2; ++n28) {
                    n27 = i + n28 * n3;
                    n9 = n27 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n27];
                    for (n29 = 1; n29 < n4 - n28; ++n29) {
                        n26 = n29 * n3;
                        double[] arrd13 = arrd3;
                        int n31 = n9;
                        arrd13[n31] = arrd13[n31] + arrd2[n29] * (arrd[n27 - n26] + arrd[n27 + n26]);
                    }
                    for (n29 = n4 - n28; n29 < n6; ++n29) {
                        double[] arrd14 = arrd3;
                        int n32 = n9;
                        arrd14[n32] = arrd14[n32] + arrd2[n29] * (arrd[(n8 - n29 - n28) * n3 + i] + arrd[n27 - n29 * n3]);
                    }
                }
            }
        } else {
            for (int i = 0; i < n3; ++i) {
                for (int j = n; j <= n2; ++j) {
                    int n33 = i + j * n3;
                    n9 = n33 - n * n3;
                    arrd3[n9] = arrd2[0] * arrd[n33];
                    for (int k = 1; k < n6; ++k) {
                        int n34 = k * n3;
                        double[] arrd15 = arrd3;
                        int n35 = n9;
                        arrd15[n35] = arrd15[n35] + arrd2[k] * (arrd[n33 - n34] + arrd[n33 + n34]);
                    }
                }
            }
        }
        return arrd3;
    }

    public static double[] convolveOddY(double[] arrd, double[] arrd2, int[] arrn, int n, int n2) {
        int n3 = arrn[0];
        int n4 = arrn[1];
        int n5 = n3 * n4;
        int n6 = arrd2.length;
        int n7 = n6 - 1;
        int n8 = 2 * n4 - 2;
        double[] arrd3 = new double[n3 * (n2 - n + 1)];
        int n9 = 0;
        if (n < n7 && n2 > n4 - n6) {
            for (int i = 0; i < n3; ++i) {
                int n10;
                int n11;
                int n12;
                int n13;
                for (n11 = n; n11 < n7; ++n11) {
                    n13 = i + n11 * n3;
                    n9 = i + (n11 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n12 = 1; n12 <= n11; ++n12) {
                        n10 = n12 * n3;
                        double[] arrd4 = arrd3;
                        int n14 = n9;
                        arrd4[n14] = arrd4[n14] + arrd2[n12] * (arrd[n13 + n10] - arrd[n13 - n10]);
                    }
                    for (n12 = n11 + 1; n12 < n6; ++n12) {
                        double[] arrd5 = arrd3;
                        int n15 = n9;
                        arrd5[n15] = arrd5[n15] + arrd2[n12] * (arrd[n13 + n12 * n3] - arrd[(n12 - n11) * n3 + i]);
                    }
                }
                for (n11 = n7; n11 <= n4 - n6; ++n11) {
                    n13 = i + n11 * n3;
                    n9 = i + (n11 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n12 = 1; n12 < n6; ++n12) {
                        n10 = n12 * n3;
                        double[] arrd6 = arrd3;
                        int n16 = n9;
                        arrd6[n16] = arrd6[n16] + arrd2[n12] * (arrd[n13 + n10] - arrd[n13 - n10]);
                    }
                }
                for (n11 = n4 - n7; n11 <= n2; ++n11) {
                    n13 = i + n11 * n3;
                    n9 = i + (n11 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n12 = 1; n12 < n4 - n11; ++n12) {
                        n10 = n12 * n3;
                        double[] arrd7 = arrd3;
                        int n17 = n9;
                        arrd7[n17] = arrd7[n17] + arrd2[n12] * (arrd[n13 + n10] - arrd[n13 - n10]);
                    }
                    for (n12 = n4 - n11; n12 < n6; ++n12) {
                        double[] arrd8 = arrd3;
                        int n18 = n9;
                        arrd8[n18] = arrd8[n18] + arrd2[n12] * (arrd[(n8 - n12 - n11) * n3 + i] - arrd[n13 - n12 * n3]);
                    }
                }
            }
        } else if (n < n7 && n2 <= n4 - n6) {
            for (int i = 0; i < n3; ++i) {
                int n19;
                int n20;
                int n21;
                int n22;
                for (n20 = n; n20 < n7; ++n20) {
                    n21 = i + n20 * n3;
                    n9 = i + (n20 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n22 = 1; n22 <= n20; ++n22) {
                        n19 = n22 * n3;
                        double[] arrd9 = arrd3;
                        int n23 = n9;
                        arrd9[n23] = arrd9[n23] + arrd2[n22] * (arrd[n21 + n19] - arrd[n21 - n19]);
                    }
                    for (n22 = n20 + 1; n22 < n6; ++n22) {
                        double[] arrd10 = arrd3;
                        int n24 = n9;
                        arrd10[n24] = arrd10[n24] + arrd2[n22] * (arrd[n21 + n22 * n3] - arrd[(n22 - n20) * n3 + i]);
                    }
                }
                for (n20 = n7; n20 <= n2; ++n20) {
                    n21 = i + n20 * n3;
                    n9 = i + (n20 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n22 = 1; n22 < n6; ++n22) {
                        n19 = n22 * n3;
                        double[] arrd11 = arrd3;
                        int n25 = n9;
                        arrd11[n25] = arrd11[n25] + arrd2[n22] * (arrd[n21 + n19] - arrd[n21 - n19]);
                    }
                }
            }
        } else if (n >= n7 && n2 > n4 - n6) {
            for (int i = 0; i < n3; ++i) {
                int n26;
                int n27;
                int n28;
                int n29;
                for (n28 = n; n28 <= n4 - n6; ++n28) {
                    n27 = i + n28 * n3;
                    n9 = i + (n28 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n29 = 1; n29 < n6; ++n29) {
                        n26 = n29 * n3;
                        double[] arrd12 = arrd3;
                        int n30 = n9;
                        arrd12[n30] = arrd12[n30] + arrd2[n29] * (arrd[n27 + n26] - arrd[n27 - n26]);
                    }
                }
                for (n28 = n4 - n7; n28 < n2; ++n28) {
                    n27 = i + n28 * n3;
                    n9 = i + (n28 - n) * n3;
                    arrd3[n9] = 0.0;
                    for (n29 = 1; n29 < n4 - n28; ++n29) {
                        n26 = n29 * n3;
                        double[] arrd13 = arrd3;
                        int n31 = n9;
                        arrd13[n31] = arrd13[n31] + arrd2[n29] * (arrd[n27 + n26] - arrd[n27 - n26]);
                    }
                    for (n29 = n4 - n28; n29 < n6; ++n29) {
                        double[] arrd14 = arrd3;
                        int n32 = n9;
                        arrd14[n32] = arrd14[n32] + arrd2[n29] * (arrd[(n8 - n29 - n28) * n3 + i] - arrd[n27 - n29 * n3]);
                    }
                }
            }
        } else {
            for (int i = 0; i < n3; ++i) {
                for (int j = n; j <= n2; ++j) {
                    int n33 = i + j * n3;
                    n9 = i + (j - n) * n3;
                    arrd3[n9] = 0.0;
                    for (int k = 1; k < n6; ++k) {
                        int n34 = k * n3;
                        double[] arrd15 = arrd3;
                        int n35 = n9;
                        arrd15[n35] = arrd15[n35] + arrd2[k] * (arrd[n33 + n34] - arrd[n33 - n34]);
                    }
                }
            }
        }
        return arrd3;
    }
}

