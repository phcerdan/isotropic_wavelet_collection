/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.Parameters;
import polyharmonicwavelets.QuincunxFilters;

public class QuincunxTransform {
    private Parameters param;
    private ComplexImage[] FA;
    private ComplexImage[] FS;
    private ComplexImage[] FP;
    private ComplexImage P;
    private int J;
    private final double PI2 = 6.283185307179586;
    private final double sqrt2 = Math.sqrt(2.0);

    public QuincunxTransform(QuincunxFilters filt, Parameters par) {
        this.J = par.J;
        this.param = par;
        this.FA = new ComplexImage[4];
        this.FS = new ComplexImage[4];
        for (int i = 0; i < 4; ++i) {
            this.FA[i] = filt.FA[i];
            this.FS[i] = filt.FS[i];
        }
        this.P = filt.P.copyImage();
    }

    public ComplexImage[] analysis(ComplexImage image) {
        if (this.param.redundancy == 2) {
            return this.analysisPyramid(image);
        }
        return this.analysisRedundant(image);
    }

    public ComplexImage synthesis(ComplexImage[] coef) {
        if (this.param.redundancy == 2) {
            return this.synthesisPyramid(coef);
        }
        return this.synthesisRedundant(coef);
    }

    private ComplexImage[] analysisRedundant(ComplexImage image) {
        ComplexImage H = this.FA[0];
        ComplexImage G = this.FA[1];
        ComplexImage HD = this.FA[2];
        ComplexImage GD = this.FA[3];
        G.multiply(this.sqrt2);
        GD.multiply(this.sqrt2);
        H.multiply(this.sqrt2);
        HD.multiply(this.sqrt2);
        double sqrt2inv = 1.0 / this.sqrt2;
        ComplexImage[] array = new ComplexImage[this.J + 1];
        int l = 1;
        ComplexImage R = image.copyImage();
        R.FFT2D();
        if (this.param.prefilter) {
            R.multiply(this.P);
        }
        for (int j = 1; j < this.J + 1; ++j) {
            int k = j - 1;
            array[k] = R.copyImage();
            if (j % 2 == 1) {
                array[k].multiplyCircular(G, l);
                R.multiplyCircular(H, l);
            } else {
                array[k].multiplyCircular(GD, l);
                R.multiplyCircular(HD, l);
                l *= 2;
            }
            array[k].multiply(sqrt2inv);
            R.multiply(sqrt2inv);
            array[k].iFFT2D();
        }
        array[this.J] = R.copyImage();
        array[this.J].iFFT2D();
        G.multiply(1.0 / this.sqrt2);
        GD.multiply(1.0 / this.sqrt2);
        H.multiply(1.0 / this.sqrt2);
        HD.multiply(1.0 / this.sqrt2);
        return array;
    }

    private ComplexImage synthesisRedundant(ComplexImage[] array) {
        ComplexImage H = this.FS[0];
        ComplexImage G = this.FS[1];
        ComplexImage HD = this.FS[2];
        ComplexImage GD = this.FS[3];
        G.multiply(1.0 / this.sqrt2);
        GD.multiply(1.0 / this.sqrt2);
        H.multiply(1.0 / this.sqrt2);
        HD.multiply(1.0 / this.sqrt2);
        double sqrt2inv = 1.0 / Math.sqrt(2.0);
        ComplexImage LP = array[this.J];
        LP.FFT2D();
        int l = 1;
        int j = 1;
        while (j < (this.J + 1) / 2) {
            ++j;
            l *= 2;
        }
        for (j = this.J; j > 0; --j) {
            ComplexImage HP = array[j - 1];
            HP.FFT2D();
            if (j % 2 == 1) {
                HP.multiplyCircular(G, l);
                LP.multiplyCircular(H, l);
                l /= 2;
            } else {
                HP.multiplyCircular(GD, l);
                LP.multiplyCircular(HD, l);
            }
            LP.add(HP);
            LP.multiply(sqrt2inv);
        }
        if (this.param.prefilter) {
            LP.divide(this.P, 1.0, 0.0);
        }
        LP.iFFT2D();
        G.multiply(this.sqrt2);
        GD.multiply(this.sqrt2);
        H.multiply(this.sqrt2);
        HD.multiply(this.sqrt2);
        return LP;
    }

    private ComplexImage[] analysisPyramid(ComplexImage image) {
        ComplexImage[] array = new ComplexImage[this.J + 1];
        ComplexImage H1 = this.FA[0].copyImage();
        H1.multiply(0.5);
        ComplexImage H1Dl = this.FA[2].getSubimage(0, this.FA[2].nx / 2 - 1, 0, this.FA[2].ny - 1);
        H1Dl.multiply(0.5);
        ComplexImage G1 = this.FA[1].copyImage();
        ComplexImage G1D = this.FA[3].copyImage();
        ComplexImage Y2 = null;
        ComplexImage Y1 = image.copyImage();
        Y1.FFT2D();
        if (this.param.prefilter) {
            Y1.multiply(this.P);
        }
        int l = 1;
        for (int j = 1; j <= this.J; ++j) {
            int mj = j % 2;
            Y2 = Y1.copyImage();
            if (mj == 1) {
                Y2.multiply(G1, l);
                Y1.multiply(H1, l);
                Y1.quincunxDownUp();
                Y2.iFFT2D();
                array[j - 1] = Y2;
                continue;
            }
            Y2.multiply(G1D, l);
            Y2.iFFT2D();
            array[j - 1] = Y2 = Y2.rotate(0.0);
            Y1 = Y1.getSubimage(0, Y1.nx / 2 - 1, 0, Y1.ny - 1);
            Y1.multiply(H1Dl, l);
            Y1.dyadicDownY();
            l *= 2;
        }
        Y1.iFFT2D();
        if (this.J % 2 == 1) {
            Y1 = Y1.rotate(0.0);
        }
        array[this.J] = Y1;
        return array;
    }

    private ComplexImage synthesisPyramid(ComplexImage[] array) {
        ComplexImage Ge = this.FS[1].copyImage();
        Ge.multiply(this.FA[1]);
        ComplexImage Gemodsqr = Ge.copyImage();
        Gemodsqr.squareModulus();
        Gemodsqr.quincunxDownUp();
        ComplexImage Gls = Ge;
        Gls.conj();
        Gls.divide(Gemodsqr, 1.0, 0.0);
        ComplexImage GeD = this.FS[3].copyImage();
        GeD.multiply(this.FA[3]);
        Gemodsqr = GeD.copyImage();
        Gemodsqr.squareModulus();
        Gemodsqr.downUpY();
        ComplexImage GlsD = GeD.copyImage();
        GlsD.conj();
        GlsD.divide(Gemodsqr, 1.0, 0.0);
        ComplexImage H = this.FS[0];
        ComplexImage H1 = this.FA[1];
        ComplexImage G = this.FS[1];
        ComplexImage HD = this.FS[2];
        ComplexImage H1D = this.FA[3];
        ComplexImage GD = this.FS[3];
        ComplexImage HP = new ComplexImage(array[1].nx, array[1].ny);
        ComplexImage LP = new ComplexImage(array[1].nx, array[0].ny);
        ComplexImage LP1 = new ComplexImage(array[1].nx, array[1].ny);
        LP.copyImageContent(array[this.J]);
        if (this.J % 2 == 1) {
            LP.unrotate(array[this.J - 1].nx, array[this.J - 1].ny);
        }
        LP.FFT2D();
        int l = 1;
        int i = 0;
        int J12 = (this.J - 1) / 2;
        while (i < J12) {
            ++i;
            l *= 2;
        }
        int mj = this.J % 2;
        for (int j = this.J; j > 0; --j) {
            HP.copyImageContent(array[j - 1]);
            if (mj == 0) {
                HP.unrotate(array[j - 2].nx, array[j - 2].ny);
                HP.FFT2D();
                LP.dyadicUpsample();
                LP.multiply(HD, l);
                LP1.copyImageContent(LP);
                LP1.multiply(H1D, l);
                HP.subtract(LP1);
                HP.multiply(GlsD, l);
                HP.downUpY();
                HP.multiplyCircular(GD, l);
                LP.add(HP);
            } else {
                HP.FFT2D();
                LP.multiplyCircular(H, l);
                LP1.copyImageContent(LP);
                LP1.multiply(H1, l);
                HP.subtract(LP1);
                HP.multiply(Gls, l);
                HP.quincunxDownUp();
                HP.multiply(G, l);
                LP.add(HP);
                l /= 2;
            }
            mj = 1 - mj;
        }
        if (this.param.prefilter) {
            LP.divide(this.P, 1.0, 0.0);
        }
        LP.iFFT2D();
        return LP;
    }

    public ComplexImage displayPyramid(ComplexImage[] array, double back, boolean rescale, boolean lp) {
        int nx = array[0].nx;
        int ny = array[0].ny;
        int s = (nx + ny) / 2;
        int l = nx > ny ? (nx + ny) / 2 : ny;
        ComplexImage display = new ComplexImage(nx + s, 2 * l);
        display.settoConstant(back, back);
        int x = 0;
        int y = 0;
        for (int j = 0; j < this.J; ++j) {
            ComplexImage temp = array[j].copyImage();
            if (rescale) {
                temp.stretch();
            }
            if (j % 2 == 1) {
                temp.unrotate(array[j - 1].nx, array[j - 1].ny);
                temp = temp.rotate(back);
            }
            temp.frame(back);
            display.putSubimage(x, y, temp);
            if (j == this.J - 1 && this.J % 2 == 0) {
                y += l;
                x = nx - array[this.J].nx / 2;
                continue;
            }
            if (x == nx) {
                y += l;
                l /= 2;
                x = nx - array[j + 1].nx;
                continue;
            }
            x = nx;
        }
        if (lp) {
            ComplexImage temp = array[this.J].copyImage();
            if (this.J % 2 == 1) {
                temp.unrotate(array[this.J - 1].nx, array[this.J - 1].ny);
                temp = temp.rotate(back);
            }
            temp.stretch();
            temp.frame(back);
            display.putSubimage(x, y, temp);
        }
        return display;
    }

    public ComplexImage displayBasis(ComplexImage image) {
        ComplexImage sub;
        int dx = image.nx;
        int dy = image.ny;
        ComplexImage out = new ComplexImage(dx, dy);
        for (int j = 1; j <= this.J; ++j) {
            if (j % 2 == 1) {
                sub = image.getSubimage(dx / 2, dx - 1, 0, dy - 1);
                sub.stretch();
                out.putSubimage(dx / 2, 0, sub);
                dx /= 2;
                continue;
            }
            sub = image.getSubimage(0, dx - 1, dy / 2, dy - 1);
            sub.stretch();
            out.putSubimage(0, dy / 2, sub);
            dy /= 2;
        }
        sub = image.getSubimage(0, dx - 1, 0, dy - 1);
        sub.stretch();
        out.putSubimage(0, 0, sub);
        return out;
    }
}

