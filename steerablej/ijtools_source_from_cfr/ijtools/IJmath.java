/*
 * Decompiled with CFR 0_118.
 */
package ijtools;

public class IJmath {
    public static double[] quadraticRoots(double d, double d2) {
        double[] arrd;
        double d3 = d * d - 4.0 * d2;
        if (d3 < 0.0) {
            arrd = new double[]{};
        } else {
            arrd = new double[2];
            d3 = Math.sqrt(d3);
            arrd[0] = (- d + d3) / 2.0;
            arrd[1] = (- d - d3) / 2.0;
        }
        return arrd;
    }

    public static double[] cubicRoots(double d, double d2, double d3) {
        double[] arrd;
        double d4 = d * d - 3.0 * d2;
        double d5 = d4 / 9.0;
        double d6 = (2.0 * d * d * d - 9.0 * d * d2 + 27.0 * d3) / 54.0;
        if (d3 == 0.0) {
            double d7 = d * d - 4.0 * d2;
            arrd = d7 >= 0.0 ? new double[]{0.0, (- d + Math.sqrt(d7)) / 2.0, (- d - Math.sqrt(d7)) / 2.0} : new double[]{0.0};
        } else {
            double d8 = d6 >= 0.0 ? 1.0 : -1.0;
            if (d6 * d6 <= d5 * d5 * d5) {
                arrd = new double[3];
                double d9 = Math.acos(d6 / Math.sqrt(d5 * d5 * d5));
                arrd[0] = -2.0 * Math.sqrt(d5) * Math.cos(d9 / 3.0) - d / 3.0;
                arrd[1] = -2.0 * Math.sqrt(d5) * Math.cos((d9 + 6.283185307179586) / 3.0) - d / 3.0;
                arrd[2] = -2.0 * Math.sqrt(d5) * Math.cos((d9 - 6.283185307179586) / 3.0) - d / 3.0;
            } else {
                double d10 = (- d8) * Math.pow(Math.abs(d6) + Math.sqrt(d6 * d6 - d5 * d5 * d5), 0.3333333333333333);
                double d11 = d10 == 0.0 ? 0.0 : d5 / d10;
                arrd = new double[]{d10 + d11 - d / 3.0};
            }
        }
        return arrd;
    }

    public static double[] quarticRoots(double d, double d2, double d3, double d4) {
        double[] arrd;
        if (d == 0.0 && d3 == 0.0) {
            double[] arrd2 = IJmath.quadraticRoots(d2, d4);
            if (arrd2.length != 0) {
                if (arrd2[0] >= 0.0 && arrd2[1] >= 0.0) {
                    arrd = new double[]{Math.sqrt(arrd2[0]), - arrd[0], Math.sqrt(arrd2[1]), arrd[2]};
                } else if (arrd2[0] < 0.0 && arrd2[1] < 0.0) {
                    arrd = new double[]{};
                } else {
                    arrd = new double[2];
                    if (arrd2[0] >= 0.0) {
                        arrd[0] = Math.sqrt(arrd2[0]);
                        arrd[1] = - arrd[0];
                    } else {
                        arrd[0] = Math.sqrt(arrd2[1]);
                        arrd[1] = - arrd[0];
                    }
                }
            } else {
                arrd = new double[]{};
            }
        } else {
            double[] arrd3 = IJmath.cubicRoots(- d2, d3 * d - 4.0 * d4, 4.0 * d2 * d4 - d3 * d3 - d * d * d4);
            double d5 = arrd3[0];
            double d6 = 0.25 * d * d - d2 + d5;
            if (d6 < 0.0) {
                arrd = new double[]{};
            } else {
                double d7 = Math.sqrt(d6);
                if (d7 == 0.0) {
                    double d8 = 0.75 * d * d - 2.0 * d2 + 2.0 * Math.sqrt(d5 * d5 - 4.0 * d4);
                    double d9 = 0.75 * d * d - 2.0 * d2 - 2.0 * Math.sqrt(d5 * d5 - 4.0 * d4);
                    if (d8 >= 0.0) {
                        double d10 = Math.sqrt(d8);
                        if (d9 >= 0.0) {
                            double d11 = Math.sqrt(d9);
                            arrd = new double[]{-0.25 * d + 0.5 * d10, -0.25 * d - 0.5 * d10, -0.25 * d + 0.5 * d11, -0.25 * d - 0.5 * d11};
                        } else {
                            arrd = new double[]{-0.25 * d + 0.5 * d10, -0.25 * d - 0.5 * d10};
                        }
                    } else if (d9 >= 0.0) {
                        double d12 = Math.sqrt(d9);
                        arrd = new double[]{-0.25 * d + 0.5 * d12, -0.25 * d - 0.5 * d12};
                    } else {
                        arrd = new double[]{};
                    }
                } else {
                    double d13 = 0.75 * d * d - d7 * d7 - 2.0 * d2 + (d * d2 - 2.0 * d3 - 0.25 * d * d * d) / d7;
                    double d14 = 0.75 * d * d - d7 * d7 - 2.0 * d2 - (d * d2 - 2.0 * d3 - 0.25 * d * d * d) / d7;
                    if (d13 >= 0.0) {
                        double d15 = Math.sqrt(d13);
                        if (d14 >= 0.0) {
                            double d16 = Math.sqrt(d14);
                            arrd = new double[]{-0.25 * d + 0.5 * d7 + 0.5 * d15, -0.25 * d + 0.5 * d7 - 0.5 * d15, -0.25 * d - 0.5 * d7 + 0.5 * d16, -0.25 * d - 0.5 * d7 - 0.5 * d16};
                        } else {
                            arrd = new double[]{-0.25 * d + 0.5 * d7 + 0.5 * d15, -0.25 * d + 0.5 * d7 - 0.5 * d15};
                        }
                    } else if (d14 >= 0.0) {
                        double d17 = Math.sqrt(d14);
                        arrd = new double[]{-0.25 * d - 0.5 * d7 + 0.5 * d17, -0.25 * d - 0.5 * d7 - 0.5 * d17};
                    } else {
                        arrd = new double[]{};
                    }
                }
            }
        }
        return arrd;
    }

    public static double[] divPolyByRoot(double[] arrd, double d) {
        int n = arrd.length;
        double[] arrd2 = new double[n - 1];
        double d2 = arrd[n - 1];
        for (int i = n - 2; i >= 0; --i) {
            arrd2[i] = d2;
            d2 = arrd[i] + d2 * d;
        }
        return arrd2;
    }

    public static double[] divPolyByConjRoots(double[] arrd, double d, double d2) {
        double d3 = 2.0 * d;
        double d4 = d * d + d2 * d2;
        int n = arrd.length - 2;
        double[] arrd2 = new double[n];
        arrd2[n - 1] = arrd[n + 1];
        arrd2[n - 2] = arrd[n] + d3 * arrd2[n - 1];
        for (int i = n - 1; i >= 2; --i) {
            arrd2[i - 2] = arrd[i] + d3 * arrd2[i - 1] - d4 * arrd2[i];
        }
        return arrd2;
    }

    public static double evalPoly(double[] arrd, double d) {
        int n = arrd.length;
        double d2 = arrd[n - 1];
        for (int i = n - 2; i >= 0; --i) {
            d2 = d2 * d + arrd[i];
        }
        return d2;
    }

    public static double[] evalPolyD(double[] arrd, double d) {
        int n = arrd.length;
        double[] arrd2 = new double[]{arrd[n - 1], 0.0};
        for (int i = n - 2; i >= 0; --i) {
            arrd2[1] = arrd2[1] * d + arrd2[0];
            arrd2[0] = arrd2[0] * d + arrd[i];
        }
        return arrd2;
    }

    public static double[] laguerre(double[] arrd, double d) {
        int n = arrd.length - 1;
        double[] arrd2 = IJmath.complex(d, 0.0);
        double[] arrd3 = new double[]{0.0, 0.5, 0.25, 0.75, 0.125, 0.375, 0.625, 0.875, 1.0};
        int n2 = 10;
        int n3 = 30;
        double d2 = 1.0E-15;
        int[] arrn = new int[]{-2};
        for (int i = 1; i <= n3; ++i) {
            double[] arrd4;
            double[] arrd5 = IJmath.complex(arrd[n], 0.0);
            double[] arrd6 = IJmath.complex(0.0, 0.0);
            double[] arrd7 = IJmath.complex(0.0, 0.0);
            double d3 = IJmath.cabs(arrd2);
            double d4 = IJmath.cabs(arrd5);
            for (int j = n - 1; j >= 0; --j) {
                arrd7 = IJmath.cadd(IJmath.cmul(arrd7, arrd2), arrd6);
                arrd6 = IJmath.cadd(IJmath.cmul(arrd6, arrd2), arrd5);
                arrd5 = IJmath.cadd(IJmath.cmul(arrd5, arrd2), IJmath.complex(arrd[j], 0.0));
                d4 = IJmath.cabs(arrd5) + d3 * d4;
            }
            if (IJmath.cabs(arrd5) <= d4 * d2) {
                arrn[0] = 2;
                break;
            }
            double[] arrd8 = IJmath.cdiv(arrd6, arrd5);
            double[] arrd9 = IJmath.cmul(arrd8, arrd8);
            double[] arrd10 = IJmath.csub(arrd9, IJmath.cdiv(arrd7, arrd5));
            double[] arrd11 = IJmath.rcmul(n - 1, IJmath.csub(IJmath.rcmul(n, arrd10), arrd9));
            double[] arrd12 = IJmath.csqrt(arrd11);
            double[] arrd13 = IJmath.cadd(arrd8, arrd12);
            double[] arrd14 = IJmath.csub(arrd8, arrd12);
            double d5 = IJmath.cabs(arrd13);
            double d6 = IJmath.cabs(arrd14);
            if (d6 > d5) {
                arrd13 = arrd14;
                d5 = d6;
            }
            if (d5 > 0.0) {
                arrd4 = IJmath.cdiv(IJmath.complex(n, 0.0), arrd13);
            } else {
                arrd4 = IJmath.rcmul(1.0 + d3, IJmath.complex(Math.cos(i), Math.sin(i)));
                arrd4 = IJmath.complex(1.0, 0.0);
            }
            arrd2 = i % n2 != 0 ? IJmath.csub(arrd2, arrd4) : IJmath.csub(arrd2, IJmath.rcmul(arrd3[i / n2], arrd4));
        }
        return arrd2;
    }

    private static double[] complex(double d, double d2) {
        double[] arrd = new double[]{d, d2};
        return arrd;
    }

    private static double[] cadd(double[] arrd, double[] arrd2) {
        double[] arrd3 = new double[]{arrd[0] + arrd2[0], arrd[1] + arrd2[1]};
        return arrd3;
    }

    private static double[] csub(double[] arrd, double[] arrd2) {
        double[] arrd3 = new double[]{arrd[0] - arrd2[0], arrd[1] - arrd2[1]};
        return arrd3;
    }

    private static double cabs(double[] arrd) {
        return Math.sqrt(arrd[0] * arrd[0] + arrd[1] * arrd[1]);
    }

    private static double[] rcmul(double d, double[] arrd) {
        double[] arrd2 = new double[]{d * arrd[0], d * arrd[1]};
        return arrd2;
    }

    private static double[] cmul(double[] arrd, double[] arrd2) {
        double[] arrd3 = new double[]{arrd[0] * arrd2[0] - arrd[1] * arrd2[1], arrd[0] * arrd2[1] + arrd2[0] * arrd[1]};
        return arrd3;
    }

    private static double[] cdiv(double[] arrd, double[] arrd2) {
        double d = arrd2[0] * arrd2[0] + arrd2[1] * arrd2[1];
        double[] arrd3 = new double[]{(arrd[0] * arrd2[0] + arrd[1] * arrd2[1]) / d, (arrd2[0] * arrd[1] - arrd[0] * arrd2[1]) / d};
        return arrd3;
    }

    private static double[] csqrt(double[] arrd) {
        double d = IJmath.cabs(arrd);
        double d2 = Math.sqrt(2.0);
        double[] arrd2 = new double[]{Math.sqrt(d + arrd[0]) / d2, IJmath.csign(arrd[1]) * Math.sqrt(d - arrd[0]) / d2};
        return arrd2;
    }

    private static double csign(double d) {
        if (d >= 0.0) {
            return 1.0;
        }
        return -1.0;
    }

    public static double mean(double[] arrd) {
        return IJmath.sum(arrd) / (double)arrd.length;
    }

    public static double mean(double[][] arrd) {
        return IJmath.sum(arrd) / (double)(arrd.length * arrd[0].length);
    }

    public static double sum(double[] arrd) {
        int n = arrd.length;
        double d = 0.0;
        for (int i = 0; i < n; ++i) {
            d += arrd[i];
        }
        return d;
    }

    public static double sum(double[][] arrd) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        double d = 0.0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                d += arrd[i][j];
            }
        }
        return d;
    }

    public static double[] add(double[] arrd, double[] arrd2) {
        int n = arrd.length;
        double[] arrd3 = new double[n];
        for (int i = 0; i < n; ++i) {
            arrd3[i] = arrd[i] + arrd2[i];
        }
        return arrd3;
    }

    public static double[][] add(double[][] arrd, double[][] arrd2) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        double[][] arrd3 = new double[n][n2];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                arrd3[i][j] = arrd[i][j] + arrd2[i][j];
            }
        }
        return arrd3;
    }

    public static double[] subtract(double[] arrd, double[] arrd2) {
        int n = arrd.length;
        double[] arrd3 = new double[n];
        for (int i = 0; i < n; ++i) {
            arrd3[i] = arrd[i] - arrd2[i];
        }
        return arrd3;
    }

    public static double[][] subtract(double[][] arrd, double[][] arrd2) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        double[][] arrd3 = new double[n][n2];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                arrd3[i][j] = arrd[i][j] - arrd2[i][j];
            }
        }
        return arrd3;
    }

    public static double[] mutiply(double[] arrd, double[] arrd2) {
        int n = arrd.length;
        double[] arrd3 = new double[n];
        for (int i = 0; i < n; ++i) {
            arrd3[i] = arrd[i] * arrd2[i];
        }
        return arrd3;
    }

    public static double[][] multiply(double[][] arrd, double[][] arrd2) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        double[][] arrd3 = new double[n][n2];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                arrd3[i][j] = arrd[i][j] * arrd2[i][j];
            }
        }
        return arrd3;
    }
}

