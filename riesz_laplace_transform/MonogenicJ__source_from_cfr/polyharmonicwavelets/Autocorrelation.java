/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import java.io.PrintStream;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.GammaFunction;

public class Autocorrelation {
    private int nx;
    private int ny;
    private int order;

    public Autocorrelation(int nx, int ny, int order) {
        this.nx = nx;
        this.ny = ny;
        this.order = order;
    }

    public ComplexImage computeGamma(boolean showLocalisation) {
        double PI2 = 6.283185307179586;
        int N = 0;
        ComplexImage localisationFunc = this.multiplicator(this.nx, this.ny, 0.0, 0.0, PI2, PI2, 2 * this.order, N);
        if (showLocalisation) {
            localisationFunc.showReal("LocalisationFunc Gamma");
        }
        return this.autocorrGamma(localisationFunc, this.order);
    }

    public ComplexImage computeIterative(boolean showLocalisation) {
        double PI2 = 6.283185307179586;
        int N = 0;
        ComplexImage HH = this.multiplicator(2 * this.nx, 2 * this.ny, 0.0, 0.0, 2.0 * PI2, 2.0 * PI2, this.order, N);
        ComplexImage L1 = this.multiplicator(2 * this.nx, 2 * this.ny, 0.0, 0.0, PI2, PI2, this.order, N);
        if (showLocalisation) {
            L1.showReal("LocalisationFunc Iterative");
        }
        double k = 1.0 / Math.pow(2.0, this.order);
        HH.multiply(k);
        HH.divide(L1, 1.0, 0.0);
        HH.squareModulus();
        return this.autocorrIterative(HH);
    }

    private ComplexImage autocorrIterative(ComplexImage HH) {
        double improvement;
        int nx = HH.nx;
        int ny = HH.ny;
        int lx = nx / 2;
        int ly = ny / 2;
        int lyx = ly * lx;
        ComplexImage A0 = new ComplexImage(lx, ly, true);
        ComplexImage Af = new ComplexImage(lx, ly);
        ComplexImage Afe = new ComplexImage(lx + 1, ly + 1);
        ComplexImage Ad = new ComplexImage(lx, ly, true);
        ComplexImage Aq = new ComplexImage(lx, ly, true);
        ComplexImage A1 = new ComplexImage(lx, ly, true);
        ComplexImage At = new ComplexImage(nx, ny, true);
        ComplexImage Ai = new ComplexImage(nx, ny);
        for (int i = 0; i < lx * ly; ++i) {
            A0.real[i] = 1.0;
        }
        double crit = 1.0E-8;
        int lx2 = lx / 2;
        int lx231 = 3 * lx2 - 1;
        int ly2 = ly / 2;
        int ly231 = 3 * ly2 - 1;
        int ly32 = 3 * ly / 2;
        int k1 = nx * 3 * ly / 2 + lx / 2;
        int k3 = nx * ly / 2 + 3 * lx / 2;
        int k2 = k3 - 1;
        int k4 = nx * (ly32 - 1) + lx / 2;
        int count = 0;
        int maxit = 100;
        do {
            ++count;
            Af.copyImageContent(A0);
            Af.iFFT2D();
            Af.shift();
            int x = 0;
            while (x < lx) {
                double[] arrd = Af.real;
                int n = x;
                arrd[n] = arrd[n] / 2.0;
                double[] arrd2 = Af.imag;
                int n2 = x++;
                arrd2[n2] = arrd2[n2] / 2.0;
            }
            for (int y = 0; y < lyx; y += lx) {
                double[] arrd = Af.real;
                int n = y;
                arrd[n] = arrd[n] / 2.0;
                double[] arrd3 = Af.imag;
                int n3 = y;
                arrd3[n3] = arrd3[n3] / 2.0;
            }
            Afe.extend(Af);
            Ai.putZeros();
            Ai.putSubimage(lx2, ly2, Afe);
            Ai.shift();
            Ai.FFT2D();
            A1.putZeros();
            At.copyImageContent(HH);
            At.multiply(Ai);
            Aq.getSubimageContent(0, Aq.nx - 1, 0, Aq.ny - 1, At);
            A1.add(Aq);
            Aq.getSubimageContent(0, Aq.nx - 1, ly, ly + Aq.ny - 1, At);
            A1.add(Aq);
            Aq.getSubimageContent(lx, lx + Aq.nx - 1, 0, Aq.ny - 1, At);
            A1.add(Aq);
            Aq.getSubimageContent(lx, lx + Aq.nx - 1, ly, ly + Aq.ny - 1, At);
            A1.add(Aq);
            Ad.copyImageContent(A1);
            Ad.subtract(A0);
            improvement = Ad.meanModulus();
            A0 = A1.copyImage();
        } while (improvement > 1.0E-8 && count < maxit);
        System.out.println("The autocoorelation has been computed in " + count + " iterations.");
        if (count == maxit) {
            System.out.println("The autocorrelation does not converge!");
        }
        return A0;
    }

    private ComplexImage autocorrGamma(ComplexImage loc, double order) {
        double PI = 3.141592653589793;
        double PI2 = 6.283185307179586;
        double[][] d = new double[][]{{0.0, 1.0}, {0.0, 2.0}, {1.0, 0.0}, {1.0, 1.0}, {1.0, 2.0}, {2.0, 0.0}, {2.0, 1.0}, {-1.0, 0.0}, {-1.0, 1.0}, {-1.0, 2.0}, {-2.0, 0.0}, {-2.0, 1.0}, {0.0, -1.0}, {0.0, -2.0}, {1.0, -1.0}, {1.0, -2.0}, {2.0, -1.0}, {-1.0, -1.0}, {-1.0, -2.0}, {-2.0, -1.0}, {0.0, 0.0}};
        int nx = loc.nx;
        int ny = loc.ny;
        int nxy = nx * ny;
        ComplexImage ac = new ComplexImage(nx, ny, true);
        GammaFunction gm = new GammaFunction();
        double gammanorm = Math.exp(GammaFunction.lnGamma(order));
        int nx2 = nx / 2;
        for (int kx = 0; kx <= nx2; ++kx) {
            int ny2 = ny / 2;
            for (int ky = 0; ky <= ny2; ++ky) {
                int kynx = ky * nx;
                if (ac.real[kynx + kx] != 0.0) continue;
                int kxny = kx * ny;
                double x = (double)kx / (double)nx;
                double y = (double)ky / (double)ny;
                double res = 1.0 / (order - 1.0);
                for (int i = 0; i < 21; ++i) {
                    double sqn = 3.141592653589793 * ((x - d[i][0]) * (x - d[i][0]) + (y - d[i][1]) * (y - d[i][1]));
                    res += GammaFunction.incompleteGammaQ(order, sqn) * gammanorm / Math.pow(sqn, order);
                    sqn = 3.141592653589793 * (d[i][0] * d[i][0] + d[i][1] * d[i][1]);
                    if (sqn <= 0.0) continue;
                    res += Autocorrelation.incompleteGammaGeneral(sqn, 1.0 - order) * Math.cos(6.283185307179586 * (d[i][0] * x + d[i][1] * y)) / Math.pow(sqn, 1.0 - order);
                }
                ac.real[kynx + kx] = res;
                if (kx > 0) {
                    ac.real[kynx + nx - kx] = res;
                }
                if (ky > 0) {
                    ac.real[nxy - kynx + kx] = res;
                }
                if (kx > 0 && ky > 0) {
                    ac.real[nxy - kynx + nx - kx] = res;
                }
                if (kynx / ny * ny != kynx || kxny / nx * nx != kxny) continue;
                int kx1 = ky * nx / ny;
                int ky1 = kx * ny / nx;
                kynx = ky1 * nx;
                kxny = kx1 * ny;
                ac.real[kynx + kx1] = res;
                if (kx1 > 0) {
                    ac.real[kynx + nx - kx1] = res;
                }
                if (ky1 > 0) {
                    ac.real[nxy - kynx + kx1] = res;
                }
                if (kx1 <= 0 || ky1 <= 0) continue;
                ac.real[nxy - kynx + nx - kx1] = res;
            }
        }
        ac.multiply(Math.pow(3.141592653589793, order) / (gammanorm * Math.pow(6.283185307179586, 2.0 * order)));
        ac.multiply(loc);
        ac.real[0] = 1.0;
        return ac;
    }

    private static double incompleteGammaGeneral(double x, double a) {
        double res = 0.0;
        GammaFunction gm = new GammaFunction();
        if (a < 0.0) {
            double a0 = a;
            int iter = 0;
            while (a < 0.0) {
                a += 1.0;
                ++iter;
            }
            res = a == 0.0 ? Autocorrelation.expInt(x) : GammaFunction.incompleteGammaQ(a, x) * Math.exp(GammaFunction.lnGamma(a));
            res *= Math.exp(x - a * Math.log(x));
            for (int k = 1; k <= iter; ++k) {
                res = (x * res - 1.0) / (a - (double)k);
            }
            res *= Math.exp(a0 * Math.log(x) - x);
        } else {
            res = a == 0.0 ? Autocorrelation.expInt(x) : GammaFunction.incompleteGammaQ(a, x) * Math.exp(GammaFunction.lnGamma(a));
        }
        return res;
    }

    private static double expInt(double x) {
        double[] p = new double[]{-3.602693626336023E-9, -4.81953845214096E-7, -2.569498322115933E-5, -6.97379085953419E-4, -0.01019573529845792, -0.07811863559248197, -0.3012432892762715, -0.7773807325735529, 8.267661952366478};
        double d = 0.0;
        double y = 0.0;
        for (int j = 0; j < 9; ++j) {
            d *= x;
            d += p[j];
        }
        if (d > 0.0) {
            double egamma = 0.5772156649015329;
            y = - egamma - Math.log(x);
            double term = x;
            double pterm = x;
            double eps = 1.0E-29;
            double j2 = 2.0;
            while (Math.abs(term) > eps) {
                y += term;
                pterm = (- x) * pterm / j2;
                term = pterm / j2;
                j2 += 1.0;
            }
        } else {
            double n = 1.0;
            double am2 = 0.0;
            double bm2 = 1.0;
            double am1 = 1.0;
            double bm1 = x;
            double f = am1 / bm1;
            double oldf = f + 100.0;
            double j3 = 2.0;
            double eps = 1.0E-27;
            while (Math.abs(f - oldf) > eps) {
                double alpha = n - 1.0 + j3 / 2.0;
                double a = am1 + alpha * am2;
                double b = bm1 + alpha * bm2;
                am2 = am1 / b;
                bm2 = bm1 / b;
                am1 = a / b;
                bm1 = 1.0;
                oldf = f;
                f = am1;
                alpha = ((j3 += 1.0) - 1.0) / 2.0;
                double beta = x;
                a = beta * am1 + alpha * am2;
                b = beta * bm1 + alpha * bm2;
                am2 = am1 / b;
                bm2 = bm1 / b;
                am1 = a / b;
                bm1 = 1.0;
                oldf = f;
                f = am1;
                j3 += 1.0;
            }
            y = Math.exp(- x) * f;
        }
        return y;
    }

    public ComplexImage multiplicator(int sizex, int sizey, double minx, double miny, double maxx, double maxy, double gama, int N) {
        double PI2 = 6.283185307179586;
        ComplexImage result = new ComplexImage(sizex, sizey, N == 0);
        double gama2 = gama / 2.0;
        double d83 = 2.6666666666666665;
        double d23 = 0.6666666666666666;
        double epsx = (maxx - minx) / (4.0 * (double)sizex);
        double epsy = (maxy - miny) / (4.0 * (double)sizey);
        double rx = (maxx - minx) / (double)sizex;
        double ry = (maxy - miny) / (double)sizey;
        double[] sxarr = new double[sizex];
        double[] x1arr = new double[sizex];
        double x = minx;
        int kx = 0;
        while (kx < sizex) {
            double x1;
            sxarr[kx] = Math.sin(x / 2.0) * Math.sin(x / 2.0);
            for (x1 = x; x1 >= 3.141592653589793 - epsx; x1 -= PI2) {
            }
            while (x1 < -3.141592653589793 - epsx) {
                x1 += PI2;
            }
            x1arr[kx] = x1;
            ++kx;
            x += rx;
        }
        double y = miny;
        int ky = 0;
        while (ky < sizey) {
            double y1;
            int kxy = ky * sizex;
            double sy = Math.sin(y / 2.0);
            sy *= sy;
            for (y1 = y; y1 >= 3.141592653589793 - epsy; y1 -= PI2) {
            }
            while (y1 < -3.141592653589793 - epsy) {
                y1 += PI2;
            }
            double y11 = y1;
            int kx2 = 0;
            int index = kxy;
            while (kx2 < sizex) {
                y1 = y11;
                double x1 = x1arr[kx2];
                double sx = sxarr[kx2];
                double a = 1.0;
                a = 4.0 * (sx + sy) - 2.6666666666666665 * (sx * sy);
                double re = Math.pow(a, gama2);
                double im = 0.0;
                if (N > 0) {
                    boolean y0;
                    boolean xpi = x1 < -3.141592653589793 + epsx && x1 > -3.141592653589793 - epsx;
                    boolean ypi = y1 < -3.141592653589793 + epsy && y1 > -3.141592653589793 - epsy;
                    boolean x0 = x1 < epsx && x1 > - epsx;
                    boolean bl = y0 = y1 < epsy && y1 > - epsy;
                    if (!x0 || !y0) {
                        double x1p = x1;
                        double y1p = y1;
                        if (xpi && !y0 && !ypi) {
                            x1p = 0.0;
                        }
                        if (ypi && !x0 && !xpi) {
                            y1p = 0.0;
                        }
                        x1 = x1p;
                        y1 = y1p;
                    }
                    for (int i = 0; i < N; ++i) {
                        double re1 = re * x1 - im * y1;
                        double im1 = re * y1 + im * x1;
                        re = re1;
                        im = im1;
                    }
                    double t = Math.pow(x1 * x1 + y1 * y1, (double)N / 2.0);
                    if (t == 0.0) {
                        result.real[index] = 0.0;
                        result.imag[index] = 0.0;
                    } else {
                        result.real[index] = re / t;
                        result.imag[index] = im / t;
                    }
                } else {
                    result.real[index] = re;
                }
                ++kx2;
                ++index;
            }
            ++ky;
            y += ry;
        }
        return result;
    }

    public ComplexImage denominator(int sizex, int sizey, double minx, double miny, double maxx, double maxy, int order, int N) {
        ComplexImage result = new ComplexImage(sizex, sizey);
        double gamaN2 = (double)(order - N) / 2.0;
        for (int ky = 0; ky < sizey; ++ky) {
            int kxy = ky * sizex;
            double y = miny + (double)ky * (maxy - miny) / (double)sizey;
            int kx = 0;
            int index = kxy;
            while (kx < sizex) {
                double x = minx + (double)kx * (maxx - minx) / (double)sizex;
                double re = Math.pow(x * x + y * y, gamaN2);
                double im = 0.0;
                if (N > 0) {
                    for (int i = 0; i < N; ++i) {
                        double re1 = re * x - im * y;
                        double im1 = re * y + im * x;
                        re = re1;
                        im = im1;
                    }
                    result.real[index] = re;
                    result.imag[index] = im;
                } else {
                    result.real[index] = re;
                }
                ++kx;
                ++index;
            }
        }
        return result;
    }
}

