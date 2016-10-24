/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.ImageProcessor
 */
package monogenicj;

import additionaluserinterface.Chrono;
import additionaluserinterface.WalkBar;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import imageware.Builder;
import imageware.ImageWare;
import java.util.Arrays;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.DyadicFilters;
import polyharmonicwavelets.DyadicTransform;
import polyharmonicwavelets.Parameters;
import riesz.RieszTransform;
import steerabletools.StructureTensor;

public class MonogenicImage {
    public ImageWare source;
    public ImageWare sourceColorChannel;
    public ImageWare rx;
    public ImageWare ry;
    public ImageWare laplace;
    public ImageWare energy;
    public ImageWare coherency;
    public ImageWare orientation;
    public ImageWare monogenicFrequency;
    public ImageWare monogenicPhase;
    public ImageWare monogenicModulus;
    public ImageWare directionalHilbert;
    public int nx;
    public int ny;
    public int scale;
    public boolean pyramid;
    private double sigma;
    private WalkBar walk;
    private final double ORDER = 2.0;

    public MonogenicImage(WalkBar walk, ImageProcessor ip, int scale, boolean pyramid, double sigma) {
        this.walk = walk;
        this.source = Builder.create(new ImagePlus("", ip));
        this.nx = this.source.getWidth();
        this.ny = this.source.getHeight();
        this.scale = scale;
        this.pyramid = pyramid;
        this.sigma = sigma;
        long kb = this.nx * this.ny * scale * 4 / 1024;
        walk.reset();
        this.monogenicFrequency = Builder.create(this.nx, this.ny, scale, 3);
        this.monogenicModulus = Builder.create(this.nx, this.ny, scale, 3);
        this.monogenicPhase = Builder.create(this.nx, this.ny, scale, 3);
        this.directionalHilbert = Builder.create(this.nx, this.ny, scale, 3);
        this.sourceColorChannel = this.computeSourceForColorChannel();
    }

    private ImageWare computeSourceForColorChannel() {
        ImageWare out = Builder.create(this.nx, this.ny, this.scale, 3);
        ImageWare slice = Builder.create(this.nx, this.ny, 1, 3);
        this.source.getXY(0, 0, 0, slice);
        slice.rescale(0.0, 1.0);
        out.putXY(0, 0, 0, slice);
        if (this.pyramid) {
            for (int s = 1; s < this.scale; ++s) {
                int div = (int)Math.round(Math.pow(2.0, s));
                int mx = this.nx / div;
                int my = this.ny / div;
                for (int i = 0; i < mx; ++i) {
                    for (int j = 0; j < my; ++j) {
                        out.putPixel(i, j, s, out.getPixel(i * div, j * div, 0));
                    }
                }
            }
        } else {
            for (int s = 1; s < this.scale; ++s) {
                out.putXY(0, 0, s, slice);
            }
        }
        return out;
    }

    public void compute(double sigma, double epsilon, boolean prefilter, boolean signedDir) {
        this.walk.progress("Polyharmonic", 20);
        this.laplace = this.computePolyharmonicWavelets(this.pyramid, this.source);
        this.walk.progress("Riesz", 50);
        ImageWare pre = prefilter ? this.prefilter(this.source) : this.source;
        RieszTransform rt = new RieszTransform(this.nx, this.ny, 1, true);
        ImageWare[] rieszChannels = rt.analysis(pre);
        this.rx = this.computePolyharmonicWavelets(this.pyramid, rieszChannels[0]);
        this.ry = this.computePolyharmonicWavelets(this.pyramid, rieszChannels[1]);
        ImagePlus impSource = new ImagePlus("", this.source.buildImageStack());
        ComplexImage image = new ComplexImage(impSource);
        this.walk.progress("Wavenumber", 60);
        Parameters param = new Parameters();
        param.J = this.scale;
        param.redundancy = this.pyramid ? 2 : 1;
        param.analysesonly = true;
        param.flavor = 7;
        param.prefilter = true;
        param.lattice = 1;
        param.order = 1.0;
        param.rieszfreq = 1;
        param.N = 0;
        DyadicFilters filters1 = new DyadicFilters(param, image.nx, image.ny);
        filters1.compute();
        DyadicTransform transform = new DyadicTransform(filters1, param);
        ComplexImage[] q1xq2y = transform.analysis(image);
        Chrono.tic();
        Parameters param1 = new Parameters();
        param1.J = this.scale;
        param1.redundancy = this.pyramid ? 2 : 1;
        param1.analysesonly = true;
        param1.flavor = 7;
        param1.prefilter = true;
        param1.lattice = 1;
        param1.order = 1.0;
        param1.rieszfreq = 1;
        param1.N = 1;
        DyadicFilters filters = new DyadicFilters(param1, image.nx, image.ny);
        filters.compute();
        DyadicTransform transform1 = new DyadicTransform(filters, param1);
        ComplexImage[] pxpy = transform1.analysis(image);
        this.walk.progress("ST", 70);
        this.computeStructureTensor(sigma, epsilon, signedDir);
        this.walk.progress("Monogenic", 70);
        this.computeMonogenic(pxpy, q1xq2y);
        this.walk.finish();
    }

    private ImageWare prefilter(ImageWare in) {
        Parameters param = new Parameters();
        param.J = this.scale;
        param.analysesonly = true;
        param.flavor = 7;
        param.prefilter = true;
        param.lattice = 1;
        param.order = 2.0;
        param.rieszfreq = 0;
        param.N = 0;
        DyadicFilters filters = new DyadicFilters(param, in.getWidth(), in.getHeight());
        ComplexImage P = filters.computePrefilter(2.0);
        ComplexImage X = new ComplexImage(in);
        X.FFT2D();
        X.multiply(P);
        X.iFFT2D();
        return this.convertDoubleToImageWare(X.real, in.getWidth(), in.getHeight());
    }

    public ImageWare computePolyharmonicWavelets(boolean pyramid, ImageWare in) {
        Parameters param = new Parameters();
        param.J = this.scale;
        param.redundancy = pyramid ? 2 : 1;
        param.analysesonly = true;
        param.flavor = 7;
        param.prefilter = true;
        param.lattice = 1;
        param.order = 2.0;
        param.rieszfreq = 0;
        param.N = 0;
        DyadicFilters filters = new DyadicFilters(param, in.getWidth(), in.getHeight());
        filters.compute();
        DyadicTransform transform = new DyadicTransform(filters, param);
        ImageWare out = transform.analysisImage(in);
        return out;
    }

    public ComplexImage[] computePolyharmonicWavelets(Parameters param, ComplexImage image) {
        param.order = 2.0;
        param.rieszfreq = 0;
        param.N = 0;
        DyadicFilters filtersPHW = new DyadicFilters(param, image.nx, image.ny);
        filtersPHW.setParameters(param);
        filtersPHW.compute();
        DyadicTransform transform = new DyadicTransform(filtersPHW, param);
        ComplexImage[][] plp = transform.analysisLowpass(image);
        return plp[0];
    }

    public void computeStructureTensor(double sigma, double epsilon, boolean signedDir) {
        StructureTensor tensor = new StructureTensor(null);
        tensor.compute(this.rx, this.ry, sigma, epsilon);
        this.orientation = tensor.getOrientation();
        this.energy = tensor.getEnergy();
        this.coherency = tensor.getCoherency();
        if (signedDir) {
            for (int k = 0; k < this.scale; ++k) {
                for (int x = 0; x < this.nx; ++x) {
                    for (int y = 0; y < this.ny; ++y) {
                        if (this.ry.getPixel(x, y, k) >= 0.0) continue;
                        this.orientation.putPixel(x, y, k, - this.orientation.getPixel(x, y, k));
                    }
                }
            }
        }
    }

    public void computeMonogenic(ComplexImage[] pxpy, ComplexImage[] q1xq2y) {
        double PI2 = 1.5707963267948966;
        double correctionFreq = 1.0;
        for (int j = 0; j < this.scale; ++j) {
            ComplexImage w = pxpy[j];
            ComplexImage r = q1xq2y[j];
            for (int x = 0; x < w.nx; ++x) {
                for (int y = 0; y < w.ny; ++y) {
                    int k = x + y * w.nx;
                    double theta = this.orientation.getPixel(x, y, j);
                    double p = this.laplace.getPixel(x, y, j);
                    double r1 = this.rx.getPixel(x, y, j);
                    double r2 = this.ry.getPixel(x, y, j);
                    double cos = Math.cos(theta);
                    double sin = Math.sin(theta);
                    double q = r2 * cos + r1 * sin;
                    double amp = Math.sqrt(p * p + q * q);
                    double nu = q * (w.real[k] * cos + w.imag[k] * sin) + p * r.real[k];
                    this.monogenicModulus.putPixel(x, y, j, amp);
                    this.monogenicPhase.putPixel(x, y, j, Math.atan2(q, p));
                    this.monogenicFrequency.putPixel(x, y, j, nu * correctionFreq / (amp * amp));
                    this.directionalHilbert.putPixel(x, y, j, q);
                }
            }
            correctionFreq /= 2.0;
        }
    }

    private void show(ComplexImage[] c, String msg) {
        for (int s = 0; s < c.length; ++s) {
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

    private ImageWare median(ImageWare in) {
        ImageWare out = in.replicate();
        for (int s = 0; s < in.getSizeZ(); ++s) {
            double[][] block = new double[3][3];
            double[] array = new double[5];
            for (int i = 0; i < in.getWidth(); ++i) {
                for (int j = 0; j < in.getHeight(); ++j) {
                    in.getNeighborhoodXY(i, j, s, block, 2);
                    array[0] = block[1][0];
                    array[1] = block[1][2];
                    array[2] = block[0][1];
                    array[3] = block[2][1];
                    array[4] = block[1][1];
                    Arrays.sort(array);
                    out.putPixel(i, j, s, array[2]);
                }
            }
        }
        return out;
    }
}

