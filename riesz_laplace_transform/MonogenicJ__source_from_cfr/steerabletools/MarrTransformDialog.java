/*
 * Decompiled with CFR 0_118.
 */
package steerabletools;

import additionaluserinterface.GridPanel;
import additionaluserinterface.Settings;
import additionaluserinterface.SpinnerInteger;
import additionaluserinterface.WalkBar;
import imageware.Builder;
import imageware.ImageWare;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.DyadicFilters;
import polyharmonicwavelets.DyadicTransform;
import polyharmonicwavelets.Filters;
import polyharmonicwavelets.Parameters;
import polyharmonicwavelets.QuincunxFilters;
import polyharmonicwavelets.QuincunxTransform;
import steerabletools.TransformDialog;

public class MarrTransformDialog
extends TransformDialog {
    public static final int BSPLINE = 0;
    public static final int ORTHOGONAL = 1;
    public static final int DUAL = 2;
    public static final int OPERATOR = 3;
    public static final int MARR = 7;
    public static final int DUALOPERATOR = 8;
    public static final int BASIS = 0;
    public static final int REDUNDANT = 1;
    public static final int PYRAMID = 2;
    public static final int ISOTROPIC = 1;
    public static final int CHANGESIGMA = 4;
    public static final double s2 = 6.0;
    public static final int QUINCUNX = 0;
    public static final int DYADIC = 1;
    public static final int ITERATIVE = 0;
    public static final int GAMMA = 1;
    private SpinnerInteger spnScale;
    private JRadioButton chkDyadic;
    private JRadioButton chkQuincunx;
    private Parameters params = new Parameters();

    public MarrTransformDialog(String title, Settings settings, WalkBar walk) {
        super(title, settings, walk);
        this.params.analysesonly = false;
        this.params.rieszfreq = 0;
        this.params.flavor = 7;
        this.params.type = 1;
        this.params.redundancy = 2;
        this.params.lattice = 1;
        this.params.order = 2.0;
        this.params.N = 1;
        this.params.prefilter = false;
        this.params.J = this.spnScale.get();
    }

    public void recordParameters() {
        this.settings.record("MarrWavelet-spnScale", this.spnScale, "3");
        this.settings.record("MarrWavelet-chkDyadic", this.chkDyadic, true);
        this.settings.record("MarrWavelet-chkQuincunx", this.chkQuincunx, false);
    }

    public GridPanel createPanelParameters() {
        this.spnScale = new SpinnerInteger(2, 1, 100, 1);
        this.chkDyadic = new JRadioButton("Dyadic", true);
        this.chkQuincunx = new JRadioButton("Quincunx", false);
        ButtonGroup lattice = new ButtonGroup();
        lattice.add(this.chkDyadic);
        lattice.add(this.chkQuincunx);
        GridPanel pn = new GridPanel("Parameters");
        pn.place(0, 0, new JLabel("Scale"));
        pn.place(0, 1, this.spnScale);
        pn.place(1, 0, this.chkDyadic);
        pn.place(1, 1, this.chkQuincunx);
        this.chkQuincunx.setEnabled(false);
        return pn;
    }

    public void analysisAndShowCoef(ImageWare input) {
        DyadicFilters filters;
        this.params.J = this.spnScale.get();
        ComplexImage in = new ComplexImage(input);
        ComplexImage[] coef = null;
        if (this.chkDyadic.isSelected()) {
            filters = new DyadicFilters(this.params, in.nx, in.ny);
            filters.compute();
            DyadicTransform transform = new DyadicTransform(filters, this.params);
            coef = transform.analysis(in);
        } else {
            filters = new QuincunxFilters(this.params, in.nx, in.ny);
            filters.compute();
            QuincunxTransform transform = new QuincunxTransform((QuincunxFilters)((Object)filters), this.params);
            coef = transform.analysis(in);
        }
        ImageWare out = this.convertComplexToFlatten(coef, this.chkDyadic.isSelected());
        out.show("Marr Coefficients - Rescaled - [real | imag]");
    }

    private ImageWare convertComplexToFlatten(ComplexImage[] coef, boolean dyadic) {
        int nx;
        int ny = 0;
        int posy = 0;
        int mx = nx = coef[0].nx;
        for (int s = 0; s < coef.length; ++s) {
            ny += coef[s].ny;
        }
        ImageWare module = Builder.create(nx * 2, ny, 1, 1);
        for (int s2 = 0; s2 < coef.length; ++s2) {
            nx = coef[s2].nx;
            ny = coef[s2].ny;
            ImageWare real = Builder.create(nx, ny, 1, 3);
            ImageWare imag = Builder.create(nx, ny, 1, 3);
            for (int i = 0; i < nx; ++i) {
                for (int j = 0; j < ny; ++j) {
                    int index = j * nx + i;
                    real.putPixel(i, j, 0, coef[s2].real[index]);
                    imag.putPixel(i, j, 0, coef[s2].imag[index]);
                }
            }
            real.rescale();
            imag.rescale();
            module.putXY(0, posy, 0, real);
            module.putXY(mx, posy, 0, imag);
            posy += ny;
        }
        module.show("Marr Coefficients - Rescaled - [real | imag]");
        return module;
    }

    public void analysis(ImageWare input) {
        DyadicFilters filters;
        Object transform;
        this.params.J = this.spnScale.get();
        ComplexImage in = new ComplexImage(input);
        ComplexImage[] coef = null;
        if (this.chkDyadic.isSelected()) {
            filters = new DyadicFilters(this.params, in.nx, in.ny);
            filters.compute();
            transform = new DyadicTransform(filters, this.params);
            coef = transform.analysis(in);
        } else {
            filters = new QuincunxFilters(this.params, in.nx, in.ny);
            filters.compute();
            transform = new QuincunxTransform((QuincunxFilters)((Object)filters), this.params);
            coef = transform.analysis(in);
        }
        int nx = coef[0].nx;
        int ny = coef[0].ny;
        int mx = nx;
        ImageWare out = Builder.create(nx * 2, ny, coef.length, 3);
        for (int s = 0; s < coef.length; ++s) {
            int i;
            int j;
            nx = coef[s].nx;
            ny = coef[s].ny;
            if (coef[s].imag == null) {
                for (i = 0; i < nx; ++i) {
                    for (j = 0; j < ny; ++j) {
                        out.putPixel(i, j, s, coef[s].real[j * nx + i]);
                    }
                }
                continue;
            }
            for (i = 0; i < nx; ++i) {
                for (j = 0; j < ny; ++j) {
                    int index = j * nx + i;
                    out.putPixel(i, j, s, coef[s].real[index]);
                    out.putPixel(mx + i, j, s, coef[s].imag[index]);
                }
            }
        }
        out.show("Marr Wavelets Bands [real | imag]");
    }

    public void synthesis(ImageWare[] pyramid) {
        DyadicFilters filters;
        ComplexImage out;
        Object transform;
        this.params.J = this.spnScale.get();
        int nx = pyramid[0].getWidth() / 2;
        int ny = pyramid[0].getHeight();
        int n = pyramid.length;
        int mx = nx;
        int my = ny;
        ComplexImage[] coef = new ComplexImage[n];
        for (int s = 0; s < n; ++s) {
            ImageWare real = Builder.create(nx, ny, 1, 3);
            ImageWare imag = Builder.create(nx, ny, 1, 3);
            pyramid[s].getXY(0, 0, 0, real);
            pyramid[s].getXY(mx, 0, 0, imag);
            coef[s] = new ComplexImage(real, imag);
            nx /= 2;
            ny /= 2;
        }
        nx = pyramid[0].getWidth();
        ny = pyramid[0].getHeight();
        if (this.chkDyadic.isSelected()) {
            filters = new DyadicFilters(this.params, mx, my);
            filters.compute();
            transform = new DyadicTransform(filters, this.params);
            out = transform.synthesis(coef);
        } else {
            filters = new QuincunxFilters(this.params, mx, my);
            filters.compute();
            transform = new QuincunxTransform((QuincunxFilters)((Object)filters), this.params);
            out = transform.synthesis(coef);
        }
        out.showModulus("Inverse Marr Wavelets");
    }

    public void showFilter(int nx, int ny) {
        Filters filters;
        this.params.J = this.spnScale.get();
        String title = "";
        if (this.chkDyadic.isSelected()) {
            filters = new DyadicFilters(this.params, nx, ny);
            title = "Dyadic";
        } else {
            filters = new QuincunxFilters(this.params, nx, ny);
            title = "Quincunx";
        }
        filters.compute();
        filters.FA[0].shift();
        filters.FA[0].showModulus(title + " Analysis Lowpass");
        filters.FA[0].shift();
        filters.FA[1].shift();
        filters.FA[1].showModulus(title + " Analysis highpass");
        filters.FA[1].shift();
    }

    public void checkPerfectReconstruction(ImageWare input) {
        this.params.J = this.spnScale.get();
        ComplexImage in = new ComplexImage(input);
        if (this.chkDyadic.isSelected()) {
            DyadicFilters dfilters = new DyadicFilters(this.params, in.nx, in.ny);
            dfilters.compute();
            DyadicTransform dtransform = new DyadicTransform(dfilters, this.params);
            ComplexImage[] dcoef = dtransform.analysis(in);
            ComplexImage dout = dtransform.synthesis(dcoef);
            dout.showModulus("Perfect Reconstruction");
            dout.subtract(in);
            dout.showModulus("Error");
        }
        if (this.chkQuincunx.isSelected()) {
            QuincunxFilters qfilters = new QuincunxFilters(this.params, in.nx, in.ny);
            qfilters.compute();
            QuincunxTransform qtransform = new QuincunxTransform(qfilters, this.params);
            ComplexImage[] qcoef = qtransform.analysis(in);
            ComplexImage qout = qtransform.synthesis(qcoef);
            qout.showModulus("Q Perfect Reconstruction");
            qout.subtract(in);
            qout.showModulus("Q Error");
        }
    }
}

