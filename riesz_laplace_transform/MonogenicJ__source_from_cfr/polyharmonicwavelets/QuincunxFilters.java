/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import polyharmonicwavelets.Autocorrelation;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.Filters;
import polyharmonicwavelets.Parameters;

public class QuincunxFilters
extends Filters {
    public QuincunxFilters(Parameters param, int nx, int ny) {
        super(param, nx, ny);
    }

    public void compute() {
        ComplexImage ac = null;
        ComplexImage L = null;
        ComplexImage LD = this.localization(this.nx, this.ny, 0.0, 0.0, 6.283185307179586, 6.283185307179586, this.param.order, this.param.N, 1);
        double c = Math.pow(0.5, this.param.order / 2.0);
        L = this.localization(this.nx, this.ny, 0.0, 0.0, 6.283185307179586, 6.283185307179586, this.param.order, this.param.N, 0);
        ComplexImage simpleloc = this.localization(this.nx, this.ny, 0.0, 0.0, 6.283185307179586, 6.283185307179586, 2.0 * this.param.order, 0, 0);
        Autocorrelation autocorrelation = new Autocorrelation(simpleloc.nx, simpleloc.ny, (int)this.param.order);
        ac = autocorrelation.computeIterative(false);
        ComplexImage B = LD.copyImage();
        B.multiply(c);
        B.divide(L, Math.cos(0.7853981633974483 * (double)this.param.N), - Math.sin(0.7853981633974483 * (double)this.param.N));
        ComplexImage acD = this.interpolateQuincunxReal(ac);
        ComplexImage loc = L;
        this.quincunxPrefilter(ac);
        ComplexImage ac0 = ac.copyImage();
        ComplexImage loc0 = loc.copyImage();
        this.computeLowpassHighpass(B, ac, acD, loc, true);
        B = loc0;
        B.decimate();
        B.multiply(c);
        B.divide(LD, Math.cos(0.7853981633974483 * (double)this.param.N), - Math.sin(0.7853981633974483 * (double)this.param.N));
        loc = LD;
        ac = acD;
        acD = ac0;
        acD.decimate();
        this.computeLowpassHighpass(B, ac, acD, loc, false);
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

    private void quincunxPrefilter(ComplexImage ac) {
        this.P = new ComplexImage(this.nx, this.ny, true);
        this.P.settoConstant(1.0);
        this.P = this.localization(this.nx, this.ny, -3.141592653589793, -3.141592653589793, 3.141592653589793, 3.141592653589793, this.param.order, 0, 0);
        ComplexImage d = this.denominator(this.nx, this.ny, -3.141592653589793, -3.141592653589793, 3.141592653589793, 3.141592653589793, 0);
        this.P.divide(d, 1.0, 0.0);
        this.P.shift();
        if (this.param.flavor == 1) {
            ComplexImage acsqrt = ac.copyImage();
            acsqrt.rootReal();
            this.P.divide(acsqrt);
        }
        if (this.param.flavor == 0 || this.param.flavor == 8) {
            this.P.divide(ac);
        }
    }

    private void computeLowpassHighpass(ComplexImage B, ComplexImage ac, ComplexImage acD, ComplexImage loc, boolean even) {
        ComplexImage loc0;
        ComplexImage ac0;
        ComplexImage L = null;
        ComplexImage L1 = null;
        ComplexImage H = null;
        ComplexImage H1 = null;
        ComplexImage ortho = acD.copyImage();
        ortho.divide(ac);
        double sqrt2 = Math.sqrt(2.0);
        B.multiply(sqrt2);
        if (this.param.flavor == 1) {
            ComplexImage orthot = ortho;
            orthot.rootReal();
            L1 = B;
            L1.divide(orthot);
            H = L1.copyImage();
            if (even) {
                H.shift();
            } else {
                H.shiftY();
            }
            if (!this.param.analysesonly) {
                L = L1.copyImage();
                H1 = H.copyImage();
                H1.conj();
            }
            L1.conj();
        }
        if (this.param.flavor == 2) {
            L1 = B.copyImage();
            if (even) {
                ac.shift();
                B.shift();
            } else {
                ac.shiftY();
                B.shiftY();
            }
            H = B.copyImage();
            H.conj();
            H.multiply(ac);
            if (!this.param.analysesonly) {
                L = L1.copyImage();
                L.divide(ortho);
                L.conj();
                H1 = B;
                H1.divide(acD);
            }
        }
        if (this.param.flavor == 0) {
            L1 = B.copyImage();
            L1.divide(ortho);
            L1.conj();
            if (!this.param.analysesonly) {
                L = B.copyImage();
                H = B.copyImage();
                H.divide(acD);
            }
            if (even) {
                B.shift();
            } else {
                B.shiftY();
            }
            H1 = B;
            H1.conj();
            ac.shift();
            H1.multiply(ac);
        }
        if (this.param.flavor == 3) {
            L1 = B.copyImage();
            ac0 = ac.copyImage();
            loc0 = loc;
            loc0.multiply(sqrt2);
            if (even) {
                ac.shift();
                B.shift();
            } else {
                ac.shiftY();
                B.shiftY();
            }
            if (!this.param.analysesonly) {
                L = L1.copyImage();
                L.divide(ortho);
                L.conj();
                H1 = B;
                H1.squareModulus();
                H1.multiply(ac);
                H1.multiply(ac0);
                H1.divide(loc0, 0.0, 0.0);
                H1.divide(acD, 0.0, 0.0);
                H1.conj();
            }
            H = loc0;
            H.conj();
            H.divide(ac0);
        }
        if (this.param.flavor == 8) {
            L1 = B.copyImage();
            if (!this.param.analysesonly) {
                L = L1.copyImage();
                L.conj();
                H1 = loc.copyImage();
                H1.multiply(sqrt2);
                H1.divide(ac);
            }
            L1.divide(ortho);
            ac0 = ac.copyImage();
            loc0 = loc;
            if (even) {
                ac.shift();
                ortho.shift();
                B.shift();
            } else {
                ac.shiftY();
                ortho.shiftY();
                B.shiftY();
            }
            H = B;
            H.squareModulus();
            H.multiply(1.0 / Math.sqrt(2.0));
            H.multiply(ac);
            H.multiply(ac0);
            H.divide(loc0, 0.0, 0.0);
            H.divide(acD, 0.0, 0.0);
        }
        if (this.param.flavor == 7) {
            loc.multiply(sqrt2);
            L1 = B.copyImage();
            ac0 = ac.copyImage();
            loc0 = loc;
            if (even) {
                ac.shift();
                B.shift();
            } else {
                ac.shiftY();
                B.shiftY();
            }
            if (!this.param.analysesonly) {
                H1 = B;
                H1.squareModulus();
                H1.multiply(ac);
                H1.multiply(ac0);
                H1.divide(loc0, 0.0, 0.0);
                H1.divide(acD);
                H1.conj();
                L = L1.copyImage();
                L.divide(ortho);
                L.conj();
            }
            H = loc0;
            H.conj();
        }
        if (even) {
            if (this.param.redundancy == 0) {
                H.modulateMinusY();
                if (!this.param.analysesonly) {
                    H1.modulatePlusY();
                }
            }
            if (this.param.redundancy == 2 && !this.param.analysesonly) {
                H1.modulatePlusY();
            }
            this.FA[1] = H;
            this.FS[1] = H1;
            this.FA[0] = L1;
            this.FS[0] = L;
        } else {
            if (this.param.redundancy == 0) {
                H.modulateMinusQuincunx();
                if (!this.param.analysesonly) {
                    H1.modulatePlusQuincunx();
                }
            }
            if (this.param.redundancy == 2 && !this.param.analysesonly) {
                H1.modulatePlusQuincunx();
            }
            this.FA[3] = H;
            this.FS[3] = H1;
            this.FA[2] = L1;
            this.FS[2] = L;
        }
    }

    private ComplexImage interpolateQuincunxReal(ComplexImage ac) {
        ComplexImage out = new ComplexImage(this.nx, this.ny, ac.imag == null);
        int nx1 = this.nx - 1;
        int ny1 = this.ny - 1;
        for (int cy = 0; cy < this.ny; ++cy) {
            for (int cx = 0; cx < this.nx; ++cx) {
                int ky1;
                int kx1;
                int ky;
                double res;
                double x = (double)cx / (double)this.nx * 6.283185307179586;
                double y = (double)cy / (double)this.ny * 6.283185307179586;
                double sum = x + y;
                double dif = y - x;
                double x1 = sum * (double)this.nx / 6.283185307179586;
                double y1 = dif * (double)this.ny / 6.283185307179586;
                double fx = Math.floor(x1);
                double fy = Math.floor(y1);
                x1 -= fx;
                y1 -= fy;
                int kx = (int)fx;
                for (ky = (int)fy; ky > ny1; ky -= this.ny) {
                }
                while (ky < 0) {
                    ky += this.ny;
                }
                while (kx > nx1) {
                    kx -= this.nx;
                }
                while (kx < 0) {
                    kx += this.nx;
                }
                for (ky1 = ky + 1; ky1 > ny1; ky1 -= this.ny) {
                }
                for (kx1 = kx + 1; kx1 > nx1; kx1 -= this.nx) {
                }
                double a = ac.real[this.nx * ky1 + kx1];
                double b = ac.real[this.nx * ky1 + kx];
                double c = ac.real[this.nx * ky + kx];
                double d = ac.real[this.nx * ky + kx1];
                out.real[cy * this.nx + cx] = res = y1 * (x1 * a + (1.0 - x1) * b) + (1.0 - y1) * (x1 * d + (1.0 - x1) * c);
            }
        }
        return out;
    }

    private ComplexImage localization(int sizex, int sizey, double minx, double miny, double maxx, double maxy, double gama, int N, int type) {
        ComplexImage result = new ComplexImage(sizex, sizey, N == 0);
        double gama2 = gama / 2.0;
        double epsx = 6.283185307179586 / (5.0 * (double)sizex);
        double epsy = 6.283185307179586 / (5.0 * (double)sizey);
        double d83 = 2.6666666666666665;
        double d23 = 0.6666666666666666;
        for (int ky = 0; ky < sizey; ++ky) {
            int kxy = ky * sizex;
            double rx = (maxx - minx) / (double)sizex;
            double ry = (maxy - miny) / (double)sizey;
            int kx = 0;
            int index = kxy;
            while (kx < sizex) {
                double y1;
                double x1;
                double y = miny + (double)ky * ry;
                double x = minx + (double)kx * rx;
                if (type == 1) {
                    double xt = x;
                    double yt = y;
                    x = xt + yt;
                    y = yt - xt;
                }
                for (y1 = y; y1 >= 3.141592653589793 - epsy; y1 -= 6.283185307179586) {
                }
                while (y1 < -3.141592653589793 - epsy) {
                    y1 += 6.283185307179586;
                }
                for (x1 = x; x1 >= 3.141592653589793 - epsx; x1 -= 6.283185307179586) {
                }
                while (x1 < -3.141592653589793 - epsx) {
                    x1 += 6.283185307179586;
                }
                double a = 1.0;
                double sx = Math.sin(x / 2.0);
                sx *= sx;
                double sy = Math.sin(y / 2.0);
                sy *= sy;
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
                ++kx;
                ++index;
            }
        }
        return result;
    }
}

