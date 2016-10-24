/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.IJ
 */
package riesz;

import fft.ComplexSignal;
import fft.FFT2D;
import ij.IJ;
import imageware.Builder;
import imageware.ImageWare;
import riesz.RieszFilter;

public class RieszTransform {
    private final int REAL = 0;
    private final int IMAG = 1;
    private int nx;
    private int ny;
    private RieszFilter filter;

    public RieszTransform(int nx, int ny, int order, boolean cancelDC) {
        this.nx = nx;
        this.ny = ny;
        this.filter = new RieszFilter(nx, ny, order, cancelDC);
    }

    public ImageWare[] analysis(ImageWare image) {
        int N = this.filter.getChannels();
        double[] in = image.convert(4).getSliceDouble(0);
        ComplexSignal sin = new ComplexSignal(in, this.nx, this.ny);
        ComplexSignal fin = FFT2D.transform(sin);
        ImageWare[] channelsReal = new ImageWare[N];
        for (int k = 0; k < N; ++k) {
            IJ.showStatus((String)("Analysis Channel " + (k + 1) + "/" + N));
            channelsReal[k] = Builder.create(this.nx, this.ny, 1, 4);
            ComplexSignal fcurr = this.filter.getAnalysis(k);
            ComplexSignal fg = ComplexSignal.multiply(fin, fcurr);
            ComplexSignal g = FFT2D.inverse(fg);
            this.storeReal(g, channelsReal[k]);
        }
        return channelsReal;
    }

    public ImageWare synthesis(ImageWare[] channels) {
        int N = this.filter.getChannels();
        if (N != channels.length) {
            IJ.error((String)"Not compatible stack of images for inverting Riesz Transform");
            return null;
        }
        ComplexSignal csum = new ComplexSignal(this.nx, this.ny);
        for (int k = 0; k < N; ++k) {
            IJ.showStatus((String)("Synthesis Channel " + (k + 1) + "/" + N));
            ComplexSignal fc = new ComplexSignal(channels[k].getSliceDouble(0), this.nx, this.ny);
            ComplexSignal fg = FFT2D.transform(fc);
            ComplexSignal fi = ComplexSignal.multiply(fg, this.filter.getSynthesis(k));
            for (int i = 0; i < this.nx * this.ny; ++i) {
                double[] arrd = csum.real;
                int n = i;
                arrd[n] = arrd[n] + fi.real[i];
                double[] arrd2 = csum.imag;
                int n2 = i;
                arrd2[n2] = arrd2[n2] + fi.imag[i];
            }
        }
        ComplexSignal rsum = FFT2D.inverse(csum);
        ImageWare out = Builder.create(this.nx, this.ny, 1, 4);
        this.storeReal(rsum, out);
        return out;
    }

    public RieszFilter getFilters() {
        return this.filter;
    }

    private void storeReal(ComplexSignal signal, ImageWare channel) {
        int nx = channel.getWidth();
        int ny = channel.getHeight();
        double[] data = channel.getSliceDouble(0);
        System.arraycopy(signal.real, 0, data, 0, nx * ny);
    }
}

