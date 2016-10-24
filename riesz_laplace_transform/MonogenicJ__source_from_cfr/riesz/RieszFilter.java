/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.IJ
 */
package riesz;

import fft.ComplexSignal;
import ij.IJ;
import imageware.Builder;
import imageware.ImageWare;

public class RieszFilter {
    private ComplexSignal[] A;
    private ComplexSignal[] S;
    private String[] name;
    private int channels;
    private int order;
    private boolean cancelDC = false;

    public RieszFilter(int nx, int ny, int order, boolean cancelDC) {
        int k;
        this.order = order;
        this.cancelDC = cancelDC;
        this.channels = order + 1;
        this.A = new ComplexSignal[this.channels];
        this.S = new ComplexSignal[this.channels];
        this.name = new String[this.channels];
        ComplexSignal baseX = this.generateBaseX(nx, ny);
        ComplexSignal baseY = this.generateBaseY(nx, ny);
        double c = Math.sqrt(Math.pow(2.0, order));
        for (k = 0; k < this.channels; ++k) {
            this.name[k] = "F";
        }
        for (k = 0; k < this.channels; ++k) {
            IJ.showStatus((String)("Create Riesz Filter " + (k + 1) + "/" + this.channels));
            for (int kx = 1; kx < this.channels - k; ++kx) {
                if (this.A[k] == null) {
                    this.A[k] = baseX.duplicate();
                } else {
                    this.A[k].multiply(baseX);
                }
                String[] arrstring = this.name;
                int n = k;
                arrstring[n] = arrstring[n] + "X";
            }
            for (int ky = this.channels - k; ky < this.channels; ++ky) {
                if (this.A[k] == null) {
                    this.A[k] = baseY.duplicate();
                } else {
                    this.A[k].multiply(baseY);
                }
                String[] arrstring = this.name;
                int n = k;
                arrstring[n] = arrstring[n] + "Y";
            }
            double coef = Math.sqrt(this.binomial(this.channels - 1, k));
            this.A[k].multiply(coef);
            double[] arrd = this.A[k].imag;
            arrd[0] = arrd[0] / c;
            double[] arrd2 = this.A[k].real;
            arrd2[0] = arrd2[0] / c;
            this.S[k] = this.A[k].conjugate();
        }
    }

    public int getChannels() {
        return this.channels;
    }

    public ComplexSignal getAnalysis(int channel) {
        return this.A[channel];
    }

    public ComplexSignal getSynthesis(int channel) {
        return this.S[channel];
    }

    public int getOrder() {
        return this.order;
    }

    public ImageWare getAnalysisVisible(int channel) {
        int nx = this.A[channel].nx;
        int ny = this.A[channel].ny;
        ImageWare out = Builder.create(nx, ny, 1, 3);
        float[] pout = out.getSliceFloat(0);
        this.A[channel].shift();
        if (this.order % 2 == 0) {
            for (int k = 0; k < nx * ny; ++k) {
                pout[k] = (float)this.A[channel].real[k];
            }
        } else {
            for (int k = 0; k < nx * ny; ++k) {
                pout[k] = (float)this.A[channel].imag[k];
            }
        }
        this.A[channel].shift();
        return out;
    }

    public String getName(int channel) {
        return this.name[channel];
    }

    private ComplexSignal generateBaseX(int nx, int ny) {
        double px;
        int x;
        double py;
        int y;
        double w;
        ComplexSignal filter = new ComplexSignal(nx, ny);
        for (x = 0; x < nx / 2; ++x) {
            for (y = 0; y <= ny / 2; ++y) {
                px = (double)x / (double)(nx - 1);
                py = (double)y / (double)(ny - 1);
                w = Math.sqrt(px * px + py * py);
                filter.imag[x + y * nx] = (- px) / w;
                if (y >= 1) {
                    filter.imag[x + (ny - y) * nx] = (- px) / w;
                }
                if (x >= 1) {
                    filter.imag[nx - x + y * nx] = px / w;
                }
                if (y < 1 || x < 1) continue;
                filter.imag[nx - x + (ny - y) * nx] = px / w;
            }
        }
        // PHC: This is nyquist band), y <=
        x = nx / 2;
        for (y = 0; y <= ny / 2; ++y) {
            px = (double)x / (double)(nx - 1);
            py = (double)y / (double)(ny - 1);
            w = Math.sqrt(px * px + py * py);
            filter.real[x + nx * y] = px / w;
            filter.imag[x + nx * y] = 0.0;
            if (y < 1) continue;
            filter.real[x + nx * (ny - y)] = px / w;
            filter.imag[x + nx * (ny - y)] = 0.0;
        }
        filter.imag[0] = 0.0;
        filter.real[0] = (double)this.cancelDC ? 1 : 0;
        return filter;
    }

    private ComplexSignal generateBaseY(int nx, int ny) {
        double px;
        double py;
        double w;
        ComplexSignal filter = new ComplexSignal(nx, ny);
        for (int x = 0; x <= nx / 2; ++x) {
            for (int y = 0; y < ny / 2; ++y) {
                px = (double)x / (double)(nx - 1);
                py = (double)y / (double)(ny - 1);
                w = Math.sqrt(px * px + py * py);
                filter.imag[x + nx * y] = (- py) / w;
                if (x >= 1) {
                    filter.imag[nx - x + nx * y] = (- py) / w;
                }
                if (y >= 1) {
                    filter.imag[x + nx * (ny - y)] = py / w;
                }
                if (y < 1 || x < 1) continue;
                filter.imag[nx - x + nx * (ny - y)] = py / w;
            }
        }
        int y = ny / 2;
        for (int x2 = 0; x2 <= nx / 2; ++x2) {
            px = (double)x2 / (double)(nx - 1);
            py = (double)y / (double)(ny - 1);
            w = Math.sqrt(px * px + py * py);
            filter.real[x2 + nx * y] = py / w;
            filter.imag[x2 + nx * y] = 0.0;
            if (x2 < 1) continue;
            filter.real[nx - x2 + nx * y] = py / w;
            filter.imag[nx - x2 + nx * y] = 0.0;
        }
        filter.imag[0] = 0.0;
        filter.real[0] = (double)this.cancelDC ? 1 : 0;
        return filter;
    }

    private double binomial(int n, int k) {
        return this.factorial(n) / this.factorial(k) / this.factorial(n - k);
    }

    private double factorial(int n) {
        if (n == 0) {
            return 1.0;
        }
        int fact = 1;
        for (int i = 1; i <= n; ++i) {
            fact *= i;
        }
        return fact;
    }
}

