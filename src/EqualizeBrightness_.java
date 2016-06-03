/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.IJ
 *  ij.ImagePlus
 *  ij.plugin.filter.PlugInFilter
 *  ij.process.ImageProcessor
 */
package monogenicwavelettoolbox;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import monogenicwavelettoolbox.MonogenicWaveletToolbox_;

public class EqualizeBrightness_
implements PlugInFilter {
    ImagePlus mImp;

    public void run(ImageProcessor arg0) {
        MonogenicWaveletToolbox_ monogenicWavelet_Toolbox = new MonogenicWaveletToolbox_(false);
        monogenicWavelet_Toolbox.performEqualizationOfBrightness(this.mImp, false, false);
    }

    public int setup(String arg, ImagePlus imp) {
        if (arg.equals("about")) {
            this.showAbout();
            return 4096;
        }
        if (imp != null) {
            this.mImp = imp;
        }
        return 29;
    }

    void showAbout() {
        IJ.showMessage((String)"About Equalize Brightness...", (String)"This plug-in filter equalizes the brightness of an image.\n");
    }
}

