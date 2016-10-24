/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import additionaluserinterface.Chrono;
import imageware.Builder;
import imageware.ImageWare;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.DyadicFilters;
import polyharmonicwavelets.DyadicTransform;
import polyharmonicwavelets.Parameters;

public class Riesz {
    public ComplexImage image;
    public ComplexImage[] p;
    public ComplexImage[] q;
    public ComplexImage[] q1xq2y = null;
    public ComplexImage[] pxpy = null;
    private final double ORDER = 2.0;

    public Riesz(ComplexImage image, int scale, boolean pyramid) {
        this.image = image;
        Parameters param = new Parameters();
        param.J = scale;
        param.redundancy = pyramid ? 2 : 1;
        param.analysesonly = true;
        param.flavor = 7;
        param.prefilter = true;
        param.lattice = 1;
        Chrono.tic();
        param.order = 2.0;
        param.rieszfreq = 0;
        param.N = 0;
        DyadicFilters filtersPHW = new DyadicFilters(param, image.nx, image.ny);
        filtersPHW.setParameters(param);
        filtersPHW.compute();
        DyadicTransform transformPHW = new DyadicTransform(filtersPHW, param);
        ComplexImage[][] plp = transformPHW.analysisLowpass(image);
        this.p = plp[0];
        Chrono.tic();
        param.order = 2.0;
        param.rieszfreq = 0;
        param.N = 1;
        DyadicFilters filters = new DyadicFilters(param, image.nx, image.ny);
        filters.compute();
        DyadicTransform transform = new DyadicTransform(filters, param);
        this.q = transform.analysis(image);
        Chrono.tic();
        param.order = 1.0;
        param.rieszfreq = 1;
        param.N = 0;
        DyadicFilters filters1 = new DyadicFilters(param, image.nx, image.ny);
        filters1.setParameters(param);
        filters1.compute();
        transform = new DyadicTransform(filters1, param);
        this.q1xq2y = transform.analysis(image);
        Chrono.tic();
        param.order = 1.0;
        param.rieszfreq = 1;
        param.N = 1;
        filters.setParameters(param);
        filters.compute();
        transform = new DyadicTransform(filters, param);
        this.pxpy = transform.analysis(image);
    }

    private void show(DyadicFilters filters, String msg) {
        for (int s = 0; s <= 1; ++s) {
            ComplexImage temp = filters.FA[s].copyImage();
            temp.shift();
            temp.showReal(msg + " FA real" + s);
            if (temp.imag != null) {
                temp.showImag(msg + " FA imag" + s);
            }
            if (temp.imag == null) continue;
            temp.showModulus(msg + " FA mod" + s);
        }
    }

    private void show(ComplexImage[] c, String msg) {
        for (int s = 0; s < 1; ++s) {
            ComplexImage temp = c[s].copyImage();
            temp.showReal(msg + " real" + s);
            if (temp.imag != null) {
                temp.showImag(msg + " imag" + s);
            }
            if (temp.imag == null) continue;
            temp.showModulus(msg + " mod" + s);
        }
    }

    private ImageWare convertDoubleToImageWare(double[] array, int mx, int my) {
        ImageWare image = Builder.create(mx, my, 1, 4);
        int k = 0;
        for (int y = 0; y < my; ++y) {
            for (int x = 0; x < mx; ++x) {
                image.putPixel(x, y, 0, array[k++]);
            }
        }
        return image;
    }

    private double[] convertImageWareToDouble(ImageWare image) {
        int nx = image.getWidth();
        int ny = image.getHeight();
        int k = 0;
        double[] array = new double[nx * ny];
        for (int y = 0; y < ny; ++y) {
            for (int x = 0; x < nx; ++x) {
                array[k] = image.getPixel(x, y, 0);
                ++k;
            }
        }
        return array;
    }
}

