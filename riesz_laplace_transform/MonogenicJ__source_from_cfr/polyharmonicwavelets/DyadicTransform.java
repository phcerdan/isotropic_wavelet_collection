/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import imageware.Builder;
import imageware.ImageWare;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.DyadicFilters;
import polyharmonicwavelets.Parameters;

public class DyadicTransform {
    private ComplexImage[] FA;
    private ComplexImage[] FS;
    private ComplexImage[] FP;
    private ComplexImage P;
    private int J;
    private Parameters param;
    private final int nx;
    private final int ny;
    private final double PI2 = 6.283185307179586;
    private final double sqrt2 = Math.sqrt(2.0);
    private DyadicFilters filters;

    public DyadicTransform(DyadicFilters filt, Parameters par) {
        this.J = par.J;
        this.param = par;
        this.FA = filt.FA;
        this.FS = filt.FS;
        this.FP = filt.FP;
        this.P = filt.P;
        this.nx = this.FA[0].nx;
        this.ny = this.FA[0].ny;
        this.filters = filt;
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

    public ComplexImage[][] analysisLowpass(ComplexImage image) {
        if (this.param.redundancy == 2) {
            return this.analysisPyramidLowpass(image);
        }
        return this.analysisRedundantLowpass(image);
    }

    public ImageWare analysisImage(ImageWare image) {
        if (this.param.redundancy == 2) {
            return this.analysisPyramidImage(image);
        }
        return this.analysisRedundantImage(image);
    }

    public ImageWare analysisRedundantImage(ImageWare image) {
        int nx = image.getWidth();
        int ny = image.getHeight();
        ImageWare out = Builder.create(nx, ny, this.J, 4);
        ComplexImage H = this.FA[0].copyImage();
        ComplexImage G = this.FA[1].copyImage();
        G.multiply(0.5 * this.sqrt2);
        ComplexImage X = new ComplexImage(image);
        X.FFT2D();
        if (this.param.prefilter) {
            X.multiply(this.P);
        }
        int l = 1;
        for (int j = 0; j < this.J; ++j) {
            ComplexImage band = X.copyImage();
            band.multiplyCircular(G, l);
            band.iFFT2D();
            X.multiplyCircular(H, l);
            l *= 2;
            double[] pix = out.getSliceDouble(j);
            System.arraycopy(band.real, 0, pix, 0, nx * ny);
        }
        return out;
    }

    public ImageWare analysisPyramidImage(ImageWare image) {
        int nx = image.getWidth();
        int ny = image.getHeight();
        ImageWare out = Builder.create(nx, ny, this.J, 4);
        ComplexImage H = this.FA[0];
        ComplexImage G = this.FA[1];
        H.multiply(0.25);
        G.multiply(0.5 * this.sqrt2);
        ComplexImage X = new ComplexImage(image);
        X.FFT2D();
        if (this.param.prefilter) {
            X.multiply(this.P);
        }
        int l = 1;
        for (int j = 0; j < this.J; ++j) {
            ComplexImage band = new ComplexImage(X.nx, X.ny);
            band.copyImageContent(X);
            band.multiply(G, l);
            band.iFFT2D();
            X.multiply(H, l);
            X.dyadicDownUpCrop();
            l *= 2;
            double[] pix = out.getSliceDouble(j);
            int mx = band.nx;
            int my = band.ny;
            for (int x = 0; x < mx; ++x) {
                for (int y = 0; y < my; ++y) {
                    pix[x + nx * y] = band.real[x + y * mx];
                }
            }
        }
        return out;
    }

    public ComplexImage[][] analysisRedundantLowpass(ComplexImage image) {
        ComplexImage[] lowpassSubbands = new ComplexImage[this.J + 1];
        ComplexImage[] array = new ComplexImage[this.J + 1];
        ComplexImage H = this.FA[0].copyImage();
        ComplexImage G = this.FA[1].copyImage();
        G.multiply(0.5 * this.sqrt2);
        ComplexImage X = image.copyImage();
        X.FFT2D();
        if (this.param.prefilter) {
            X.multiply(this.P);
        }
        int l = 1;
        for (int j = 1; j <= this.J; ++j) {
            ComplexImage Yh = X.copyImage();
            Yh.multiplyCircular(G, l);
            Yh.iFFT2D();
            array[j - 1] = Yh;
            ComplexImage Yl = X;
            Yl.multiplyCircular(H, l);
            lowpassSubbands[j - 1] = Yl.copyImage();
            lowpassSubbands[j - 1].iFFT2D();
            X = Yl;
            l *= 2;
        }
        X.iFFT2D();
        array[this.J] = X;
        lowpassSubbands[this.J] = X.copyImage();
        G.multiply(this.sqrt2);
        ComplexImage[][] out = new ComplexImage[][]{array, lowpassSubbands};
        return out;
    }

    private ComplexImage[] analysisRedundant(ComplexImage image) {
        ComplexImage[] array = new ComplexImage[this.J + 1];
        ComplexImage H = this.FA[0].copyImage();
        ComplexImage G = this.FA[1].copyImage();
        G.multiply(0.5 * this.sqrt2);
        ComplexImage X = image.copyImage();
        X.FFT2D();
        if (this.param.prefilter) {
            X.multiply(this.P);
        }
        int l = 1;
        for (int j = 1; j <= this.J; ++j) {
            ComplexImage Yh = X.copyImage();
            Yh.multiplyCircular(G, l);
            Yh.iFFT2D();
            array[j - 1] = Yh;
            ComplexImage Yl = X;
            Yl.multiplyCircular(H, l);
            X = Yl;
            l *= 2;
        }
        X.iFFT2D();
        array[this.J] = X;
        G.multiply(this.sqrt2);
        return array;
    }

    private ComplexImage synthesisRedundant(ComplexImage[] array) {
        ComplexImage H = this.FS[0].copyImage();
        ComplexImage G = this.FS[1].copyImage();
        G.add(this.FS[2]);
        G.add(this.FS[3]);
        G.multiply(0.25 * this.sqrt2);
        H.multiply(0.25);
        if (this.param.flavor == 7) {
            G.divide(this.filters.ac);
        }
        int l = 1;
        int k = 1;
        while (k < this.J) {
            ++k;
            l *= 2;
        }
        ComplexImage Y = array[this.J].copyImage();
        Y.FFT2D();
        for (int j = this.J; j > 0; --j) {
            ComplexImage Z = array[j - 1].copyImage();
            Z.FFT2D();
            Y.multiplyCircular(H, l);
            Z.multiplyCircular(G, l);
            l /= 2;
            Y.add(Z);
        }
        if (this.param.prefilter) {
            Y.divide(this.P);
        }
        Y.iFFT2D();
        return Y;
    }

    private ComplexImage[] analysisPyramid(ComplexImage image) {
        ComplexImage[] array = new ComplexImage[this.J + 1];
        ComplexImage H = this.FA[0];
        ComplexImage G = this.FA[1];
        H.multiply(0.25);
        G.multiply(0.5 * this.sqrt2);
        ComplexImage X = image.copyImage();
        X.FFT2D();
        if (this.param.prefilter) {
            X.multiply(this.P);
        }
        int l = 1;
        for (int j = 1; j <= this.J; ++j) {
            ComplexImage Yh = X.copyImage();
            Yh.multiply(G, l);
            Yh.iFFT2D();
            array[j - 1] = Yh;
            ComplexImage Yl = X;
            Yl.multiply(H, l);
            l *= 2;
            Yl.dyadicDownUpCrop();
            X = Yl;
        }
        X.iFFT2D();
        array[this.J] = X;
        H.multiply(4.0);
        G.multiply(this.sqrt2);
        return array;
    }

    private ComplexImage[][] analysisPyramidLowpass(ComplexImage image) {
        ComplexImage[] lowpassSubbands = new ComplexImage[this.J + 1];
        ComplexImage[] array = new ComplexImage[this.J + 1];
        ComplexImage H = this.FA[0];
        ComplexImage G = this.FA[1];
        H.multiply(0.25);
        G.multiply(0.5 * this.sqrt2);
        ComplexImage X = image.copyImage();
        X.FFT2D();
        if (this.param.prefilter) {
            X.multiply(this.P);
        }
        int l = 1;
        for (int j = 1; j <= this.J; ++j) {
            ComplexImage Yh = new ComplexImage(image.nx, image.ny);
            Yh.copyImageContent(X);
            Yh.multiply(G, l);
            Yh.iFFT2D();
            array[j - 1] = Yh;
            ComplexImage Yl = X;
            Yl.multiply(H, l);
            lowpassSubbands[j - 1] = Yl.copyImage();
            lowpassSubbands[j - 1].iFFT2D();
            l *= 2;
            Yl.dyadicDownUpCrop();
            X = Yl;
        }
        X.iFFT2D();
        array[this.J] = X;
        lowpassSubbands[this.J] = X.copyImage();
        ComplexImage[][] out = new ComplexImage[][]{array, lowpassSubbands};
        return out;
    }

    private ComplexImage synthesisPyramid(ComplexImage[] array) {
        this.FS[1].multiply(0.5);
        this.FS[2].multiply(0.5);
        this.FS[3].multiply(0.5);
        ComplexImage Gconj = this.FA[1].copyImage();
        Gconj.multiply(1.0 / this.sqrt2);
        ComplexImage[] G0 = this.FP;
        int l = 1;
        int i = 0;
        while (i < this.J) {
            ++i;
            l *= 2;
        }
        ComplexImage Y1 = new ComplexImage(array[0].nx, array[0].ny);
        ComplexImage Y2 = new ComplexImage(array[0].nx, array[0].ny);
        ComplexImage GY = new ComplexImage(array[0].nx, array[0].ny);
        Y1.copyImageContent(array[this.J]);
        Y1.FFT2D();
        for (int j = this.J - 1; j >= 0; --j) {
            Y2.copyImageContent(array[j]);
            Y2.FFT2D();
            Y1.dyadicUpsample();
            Y1.multiply(this.FS[0], l /= 2);
            GY.copyImageContent(Y1);
            GY.multiply(Gconj, l);
            ComplexImage Y21 = Y2;
            Y21.subtract(GY);
            GY.copyImageContent(Y21);
            GY.multiply(G0[1], l);
            GY.dyadicDownUp();
            GY.multiply(this.FS[2], l);
            Y1.add(GY);
            GY.copyImageContent(Y21);
            GY.multiply(G0[2], l);
            GY.dyadicDownUp();
            GY.multiply(this.FS[3], l);
            Y1.add(GY);
            Y21.multiply(G0[0], l);
            Y21.dyadicDownUp();
            Y21.multiply(this.FS[1], l);
            Y1.add(Y21);
        }
        if (this.param.prefilter) {
            Y1.divide(this.P);
        }
        Y1.iFFT2D();
        this.FS[1].multiply(2.0);
        this.FS[2].multiply(2.0);
        this.FS[3].multiply(2.0);
        return Y1;
    }

    public ComplexImage displayDyadicPyramidReal(ComplexImage[] array, double back, boolean rescale, boolean lp) {
        int J1 = this.J;
        if (!lp) {
            --J1;
        }
        int nx = array[0].nx;
        int ny = array[0].ny;
        ComplexImage disp = new ComplexImage(nx, 2 * ny);
        disp.settoConstant(back, back);
        int x = 0;
        int y = 0;
        int dx = nx / 4;
        int dy = ny;
        for (int j = 0; j <= J1; ++j) {
            ComplexImage temp = array[j].copyImage();
            if (rescale) {
                temp.stretch();
            }
            temp.frame(back);
            disp.putSubimage(x, y, temp);
            x += dx;
            y += dy;
            dx /= 2;
            dy /= 2;
        }
        return disp;
    }

    private ComplexImage displayDyadicPyramid(ComplexImage[] array, double back, boolean rescale, boolean lp) {
        int J1 = this.J;
        if (!lp) {
            --J1;
        }
        int nx = array[0].nx;
        int ny = array[0].ny;
        ComplexImage disp = new ComplexImage(2 * nx, 2 * ny);
        disp.settoConstant(back, back);
        int x = 0;
        int y = 0;
        int dx = nx / 2;
        int dy = ny;
        for (int j = 0; j <= J1; ++j) {
            ComplexImage temp = array[j].copyImage();
            if (rescale) {
                temp.stretch();
            }
            temp.frame(back, back);
            disp.putSubimage(x, y, temp);
            System.arraycopy(temp.imag, 0, temp.real, 0, temp.nxy);
            disp.putSubimage(x + temp.nx, y, temp);
            disp.setImagtoZero();
            x += dx;
            y += dy;
            dx /= 2;
            dy /= 2;
        }
        return disp;
    }

    private ComplexImage displayBasis(ComplexImage image) {
        ComplexImage sub;
        int dx = image.nx;
        int dy = image.ny;
        ComplexImage out = new ComplexImage(dx, dy);
        for (int j = 1; j <= this.J; ++j) {
            sub = image.getSubimage(dx / 2, dx - 1, 0, dy / 2 - 1);
            sub.stretch();
            out.putSubimage(dx / 2, 0, sub);
            sub.getSubimageContent(dx / 2, dx / 2 + sub.nx - 1, dy / 2, dy / 2 + sub.ny - 1, image);
            sub.stretch();
            out.putSubimage(dx / 2, dy / 2, sub);
            sub.getSubimageContent(0, sub.nx - 1, dy / 2, dy / 2 + sub.ny - 1, image);
            sub.stretch();
            out.putSubimage(0, dy / 2, sub);
            dx /= 2;
            dy /= 2;
        }
        sub = image.getSubimage(0, dx - 1, 0, dy - 1);
        sub.stretch();
        out.putSubimage(0, 0, sub);
        return out;
    }
}

