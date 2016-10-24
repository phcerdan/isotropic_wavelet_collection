/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.ImageProcessor
 */
package riesz;

import additionaluserinterface.GridPanel;
import additionaluserinterface.Settings;
import additionaluserinterface.SpinnerInteger;
import additionaluserinterface.WalkBar;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import imageware.Builder;
import imageware.ImageWare;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import riesz.RieszFilter;
import riesz.RieszTransform;
import steerabletools.TransformDialog;

public class RieszTransformDialog
extends TransformDialog {
    private SpinnerInteger spnOrder;

    public RieszTransformDialog(String title, Settings settings, WalkBar walk) {
        super(title, settings, walk);
    }

    public void recordParameters() {
        this.settings.record("RieszTransform-order", this.spnOrder, "2");
    }

    public GridPanel createPanelParameters() {
        this.spnOrder = new SpinnerInteger(2, 1, 100, 1);
        GridPanel pn = new GridPanel("Parameters");
        pn.place(0, 0, new JLabel("Order"));
        pn.place(0, 1, this.spnOrder);
        return pn;
    }

    public void analysisAndShowCoef(ImageWare input) {
        RieszTransform riesz = new RieszTransform(input.getWidth(), input.getHeight(), this.spnOrder.get(), false);
        ImageWare[] channels = riesz.analysis(input);
        if (channels != null && channels.length > 1) {
            int nx = channels[0].getWidth();
            int ny = channels[0].getHeight();
            int n = channels.length;
            int my = ny * n;
            ny = channels[0].getHeight();
            ImageWare out = Builder.create(nx, my, 1, 1);
            int posy = 0;
            for (int s = 0; s < n; ++s) {
                ImageWare band = Builder.create(nx, ny, 1, 3);
                channels[s].getXY(0, 0, s, band);
                band.rescale();
                out.putXY(0, posy, 0, band);
                posy += ny;
            }
            out.show("Riesz Transform Coefficients");
        }
    }

    public void analysis(ImageWare input) {
        RieszTransform riesz = new RieszTransform(input.getWidth(), input.getHeight(), this.spnOrder.get(), false);
        ImageWare[] channels = riesz.analysis(input);
        if (channels != null && channels.length > 1) {
            int nx = channels[0].getWidth();
            int ny = channels[0].getHeight();
            int n = channels.length;
            ImageStack stack = new ImageStack(nx, ny);
            RieszFilter filter = riesz.getFilters();
            for (int k = 0; k < n; ++k) {
                ImagePlus temp = new ImagePlus("", channels[k].buildImageStack());
                stack.addSlice(filter.getName(k / 2), temp.getProcessor());
            }
            new ImagePlus("Riesz Transform", stack).show();
        }
    }

    public void synthesis(ImageWare[] channels) {
        RieszTransform riesz = new RieszTransform(channels[0].getWidth(), channels[0].getHeight(), this.spnOrder.get(), false);
        ImageWare image = riesz.synthesis(channels);
        if (image != null) {
            image.show("Inverse Riesz Transform");
        }
    }

    public void showFilter(int nx, int ny) {
        RieszFilter filter = new RieszFilter(nx, ny, this.spnOrder.get(), false);
        int nc = filter.getChannels();
        ImageWare filters = Builder.create(nx, ny, nc, 3);
        for (int k = 0; k < nc; ++k) {
            ImageWare s = filter.getAnalysisVisible(k);
            filters.putXY(0, 0, k, s);
        }
        ImageStack stack = filters.buildImageStack();
        for (int k2 = 0; k2 < nc; ++k2) {
            stack.setSliceLabel(filter.getName(k2), k2 + 1);
        }
        new ImagePlus("Filter (order:" + this.spnOrder.get() + ")", stack).show();
    }

    public void checkPerfectReconstruction(ImageWare input) {
        RieszTransform riesz = new RieszTransform(input.getWidth(), input.getHeight(), this.spnOrder.get(), false);
        ImageWare[] channels = riesz.analysis(input);
        ImageWare image = riesz.synthesis(channels);
        image.show("Perfect Reconstruction");
    }
}

