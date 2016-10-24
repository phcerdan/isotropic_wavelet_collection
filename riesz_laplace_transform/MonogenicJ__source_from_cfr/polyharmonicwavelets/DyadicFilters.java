/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import polyharmonicwavelets.Autocorrelation;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.Constants;
import polyharmonicwavelets.Filters;
import polyharmonicwavelets.Parameters;

public class DyadicFilters
extends Filters {
    public DyadicFilters(Parameters param, int nx, int ny) {
        super(param, nx, ny);
        this.FA[0] = new ComplexImage(nx, ny, param.N == 0);
        this.FA[1] = new ComplexImage(nx, ny, param.redundancy != 0 && param.N == 0);
        if (param.redundancy == 0) {
            this.FA[2] = new ComplexImage(nx, ny);
            this.FA[3] = new ComplexImage(nx, ny);
        }
        if (!param.analysesonly) {
            int i;
            this.FS = new ComplexImage[4];
            this.FS[0] = new ComplexImage(nx, ny, param.N == 0);
            for (i = 1; i < 4; ++i) {
                this.FS[i] = new ComplexImage(nx, ny);
            }
            if (param.redundancy == 2) {
                this.FP = new ComplexImage[3];
                for (i = 0; i < 3; ++i) {
                    this.FP[i] = new ComplexImage(nx, ny);
                }
            }
        }
    }

    public ComplexImage computePrefilter(double order) {
        ComplexImage P = this.multiplicator(this.nx, this.ny, -3.141592653589793, -3.141592653589793, 3.141592653589793, 3.141592653589793, order, 0);
        ComplexImage d = this.denominator(this.nx, this.ny, -3.141592653589793, -3.141592653589793, 3.141592653589793, 3.141592653589793, 0);
        P.divide(d, 1.0, 0.0);
        P.shift();
        return P;
    }

    public void compute() {
        double k = 1.0 / Math.pow(2.0, this.param.order);
        ComplexImage HH = null;
        ComplexImage L1 = null;
        ComplexImage L = this.multiplicator(this.nx, this.ny, 0.0, 0.0, 12.566370614359172, 12.566370614359172, this.param.order, this.param.N);
        L1 = this.multiplicator(this.nx, this.ny, 0.0, 0.0, 6.283185307179586, 6.283185307179586, this.param.order, this.param.N);
        HH = L;
        HH.multiply(k);
        HH.divide(L1, 1.0, 0.0);
        if (!this.param.analysesonly || this.param.flavor != 7) {
            ComplexImage simpleloc = this.multiplicator(this.nx, this.ny, 0.0, 0.0, 6.283185307179586, 6.283185307179586, 2.0 * this.param.order, 0);
            Autocorrelation autocorrelation = new Autocorrelation(simpleloc.nx, simpleloc.ny, (int)this.param.order);
            this.ac = autocorrelation.computeGamma(false);
        }
        this.calculatePrefilter();
        if (this.param.flavor == 3 || this.param.flavor == 8 || this.param.flavor == 7) {
            this.FA[0].copyImageContent(HH);
            this.FA[0].multiply(2.0);
            ComplexImage G = this.FA[1];
            G.copyImageContent(L1);
            if (this.param.flavor != 7) {
                G.divide(this.ac);
            }
            G.multiply(2.0);
            if (this.param.rieszfreq == 1) {
                ComplexImage V2 = this.multiplicator(this.nx, this.ny, 0.0, 0.0, 6.283185307179586, 6.283185307179586, 2.0, 0);
                G.multiply(V2);
            }
            G.conj();
            java.lang.Object R = null;
            if (this.param.redundancy == 0) {
                this.FA[2].copyImageContent(G);
                this.FA[3].copyImageContent(G);
            }
            this.FA[1] = G;
            if (!this.param.analysesonly) {
                ComplexImage L1conj = L1.copyImage();
                L1conj.conj();
                ComplexImage H = this.FS[0];
                H.copyImageContent(HH);
                H.conj();
                double k1 = k * 4.0;
                H.multiply(k1);
                ComplexImage acd = this.ac.copyImage();
                acd.decimate();
                ComplexImage Gs = this.ac.copyImage();
                Gs.divide(acd);
                Gs.multiply(0.25);
                H.multiply(Gs);
                this.FS[0] = H;
                this.FS[0].multiply(2.0 / k);
                Gs.divide(L1conj, 0.0, 0.0);
                ComplexImage D = HH.copyImage();
                D.squareModulus();
                D.multiply(1.0 / k);
                D.multiply(this.ac);
                D.multiply(k1);
                ComplexImage D1 = D.copyImage();
                ComplexImage D2 = D.copyImage();
                ComplexImage D12 = D.copyImage();
                D1.shiftX();
                D2.shiftY();
                D12.shift();
                D1.multiply(Gs);
                D2.multiply(Gs);
                D12.multiply(Gs);
                this.FS[1] = D1.copyImage();
                this.FS[1].add(D12);
                this.FS[2] = D2.copyImage();
                this.FS[2].add(D12);
                this.FS[3] = D1;
                this.FS[3].add(D2);
            }
            if (this.param.flavor == 8) {
                ComplexImage[] Ftmp = this.FA;
                this.FA = this.FS;
                this.FS = Ftmp;
                this.FA[0].conj();
                this.FA[1].conj();
                this.FA[2].conj();
                this.FA[3].conj();
                this.FS[0].conj();
                this.FS[1].conj();
                this.FS[2].conj();
                this.FS[3].conj();
            }
        }
        if (this.param.redundancy == 0) {
            this.FA[1].modulateMinusX();
            this.FA[2].modulateMinusY();
            this.FA[3].modulateMinusQuincunx();
        }
        if (!this.param.analysesonly) {
            this.FS[1].modulatePlusX();
            this.FS[2].modulatePlusY();
            this.FS[3].modulatePlusQuincunx();
        }
        if (this.param.redundancy == 2 && !this.param.analysesonly) {
            this.pyramidSynthesisFilters();
        }
    }

    private ComplexImage multiplicator(int sizex, int sizey, double minx, double miny, double maxx, double maxy, double gama, int N) {
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
            for (x1 = x; x1 >= 3.141592653589793 - epsx; x1 -= 6.283185307179586) {
            }
            while (x1 < -3.141592653589793 - epsx) {
                x1 += 6.283185307179586;
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
            for (y1 = y; y1 >= 3.141592653589793 - epsy; y1 -= 6.283185307179586) {
            }
            while (y1 < -3.141592653589793 - epsy) {
                y1 += 6.283185307179586;
            }
            double y11 = y1;
            int kx2 = 0;
            int index = kxy;
            while (kx2 < sizex) {
                y1 = y11;
                double x1 = x1arr[kx2];
                double sx = sxarr[kx2];
                double a = 1.0;
                if (this.param.type == 1) {
                    a = 4.0 * (sx + sy) - 2.6666666666666665 * (sx * sy);
                }
                if (this.param.type == 4) {
                    double sigma2 = this.param.s2;
                    double b = -16.0 / sigma2;
                    double c = 24.0 / (sigma2 * sigma2) - 16.0 / (3.0 * sigma2);
                    double d = 8.0 / (sigma2 * sigma2) + 0.7111111111111111 - 16.0 / (3.0 * sigma2);
                    double e = 1.3333333333333333 - 8.0 / sigma2;
                    a = 4.0 * (sx + sy) + b * (sx * sy) + c * (sx * sx * sy + sy * sy * sx) + d * (sx * sx * sx + sy * sy * sy) + e * (sx * sx + sy * sy);
                }
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

    private ComplexImage denominator(int sizex, int sizey, double minx, double miny, double maxx, double maxy, int N) {
        ComplexImage result = new ComplexImage(sizex, sizey);
        double gamaN2 = (this.param.order - (double)N) / 2.0;
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

    private void calculatePrefilter() {
        this.P = this.multiplicator(this.nx, this.ny, -3.141592653589793, -3.141592653589793, 3.141592653589793, 3.141592653589793, this.param.order, 0);
        ComplexImage d = this.denominator(this.nx, this.ny, -3.141592653589793, -3.141592653589793, 3.141592653589793, 3.141592653589793, 0);
        this.P.divide(d, 1.0, 0.0);
        this.P.shift();
        if (this.param.flavor == 8) {
            this.P.divide(this.ac);
        }
    }

    private void pyramidSynthesisFilters() {
        this.FA[1].multiply(1.0 / Constants.SQRT2);
        ComplexImage[] Ge = this.FP;
        Ge[0].copyImageContent(this.FA[1]);
        Ge[1].copyImageContent(this.FA[1]);
        Ge[2].copyImageContent(this.FA[1]);
        Ge[0].multiply(this.FS[1]);
        Ge[1].multiply(this.FS[2]);
        Ge[2].multiply(this.FS[3]);
        Ge[0].multiply(0.5);
        Ge[1].multiply(0.5);
        Ge[2].multiply(0.5);
        ComplexImage[] Geconj = Ge;
        Geconj[0].conj();
        Geconj[1].conj();
        Geconj[2].conj();
        int nx2 = this.FA[1].nx / 2;
        int ny2 = this.FA[1].ny / 2;
        int nxy2 = this.FA[1].nxy / 2;
        int[] d = new int[]{0, nx2, nxy2, nx2 + nxy2};
        double[] mr = new double[9];
        double[] mi = new double[9];
        int km = 0;
        for (int ky = 0; ky < nxy2; ky += this.FA[1].nx) {
            int kx = ky;
            int end = ky + nx2;
            while (kx < end) {
                for (int i = 0; i < 9; ++i) {
                    mi[i] = 0.0;
                    mr[i] = 0.0;
                }
                double inr0 = 0.0;
                double inr1 = 0.0;
                double inr2 = 0.0;
                double inr4 = 0.0;
                double inr5 = 0.0;
                double inr8 = 0.0;
                double inri = 0.0;
                double ini1 = 0.0;
                double ini2 = 0.0;
                double ini4 = 0.0;
                double ini5 = 0.0;
                double ini8 = 0.0;
                for (int l = 0; l < 4; ++l) {
                    int k = kx + d[l];
                    inr0 += Geconj[0].real[k] * Geconj[0].real[k] + Geconj[0].imag[k] * Geconj[0].imag[k];
                    inr4 += Geconj[1].real[k] * Geconj[1].real[k] + Geconj[1].imag[k] * Geconj[1].imag[k];
                    inr8 += Geconj[2].real[k] * Geconj[2].real[k] + Geconj[2].imag[k] * Geconj[2].imag[k];
                    inr1 += Geconj[0].real[k] * Geconj[1].real[k] + Geconj[0].imag[k] * Geconj[1].imag[k];
                    ini1 += (- Geconj[0].real[k]) * Geconj[1].imag[k] + Geconj[0].imag[k] * Geconj[1].real[k];
                    inr2 += Geconj[0].real[k] * Geconj[2].real[k] + Geconj[0].imag[k] * Geconj[2].imag[k];
                    ini2 += (- Geconj[0].real[k]) * Geconj[2].imag[k] + Geconj[0].imag[k] * Geconj[2].real[k];
                    inr5 += Geconj[1].real[k] * Geconj[2].real[k] + Geconj[1].imag[k] * Geconj[2].imag[k];
                    ini5 += (- Geconj[1].real[k]) * Geconj[2].imag[k] + Geconj[1].imag[k] * Geconj[2].real[k];
                }
                mr[0] = inr4 * inr8 - (inr5 * inr5 + ini5 * ini5);
                mr[1] = inr2 * inr5 + ini2 * ini5 - inr1 * inr8;
                mi[1] = (- inr2) * ini5 + ini2 * inr5 - ini1 * inr8;
                mr[2] = inr1 * inr5 - ini1 * ini5 - inr2 * inr4;
                mi[2] = inr1 * ini5 + ini1 * inr5 - ini2 * inr4;
                double dr = mr[0] * inr0 + mr[1] * inr1 + mi[1] * ini1 + mr[2] * inr2 + mi[2] * ini2;
                mr[3] = (inr2 * inr5 + ini2 * ini5 - inr1 * inr8) / dr;
                mi[3] = (inr2 * ini5 - ini2 * inr5 + ini1 * inr8) / dr;
                mr[4] = (inr0 * inr8 - (inr2 * inr2 + ini2 * ini2)) / dr;
                mr[5] = (inr1 * inr2 + ini1 * ini2 - inr0 * inr5) / dr;
                mi[5] = (inr1 * ini2 - ini1 * inr2 - inr0 * ini5) / dr;
                mr[6] = (inr1 * inr5 - ini1 * ini5 - inr2 * inr4) / dr;
                mi[6] = ((- inr1) * ini5 - ini1 * inr5 + ini2 * inr4) / dr;
                mr[7] = (inr2 * inr1 + ini2 * ini1 - inr0 * inr5) / dr;
                mi[7] = (inr2 * ini1 - ini2 * inr1 + inr0 * ini5) / dr;
                mr[8] = (inr0 * inr4 - (inr1 * inr1 + ini1 * ini1)) / dr;
                double[] arrd = mr;
                arrd[0] = arrd[0] / dr;
                double[] arrd2 = mr;
                arrd2[1] = arrd2[1] / dr;
                double[] arrd3 = mi;
                arrd3[1] = arrd3[1] / dr;
                double[] arrd4 = mr;
                arrd4[2] = arrd4[2] / dr;
                double[] arrd5 = mi;
                arrd5[2] = arrd5[2] / dr;
                for (int l2 = 0; l2 < 4; ++l2) {
                    int i2;
                    int k = kx + d[l2];
                    double[] ger = new double[3];
                    double[] gei = new double[3];
                    for (i2 = 0; i2 < 3; ++i2) {
                        ger[i2] = Geconj[i2].real[k];
                        gei[i2] = Geconj[i2].imag[k];
                    }
                    for (i2 = 0; i2 < 3; ++i2) {
                        double gr = 0.0;
                        double gi = 0.0;
                        for (int j = 0; j < 3; ++j) {
                            gr += ger[j] * mr[3 * i2 + j] - gei[j] * mi[3 * i2 + j];
                            gi += ger[j] * mi[3 * i2 + j] + gei[j] * mr[3 * i2 + j];
                        }
                        Geconj[i2].real[k] = gr;
                        Geconj[i2].imag[k] = gi;
                    }
                }
                ++kx;
                ++km;
            }
        }
        this.FP = Geconj;
        this.FA[1].multiply(Constants.SQRT2);
    }
}

