/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 */
package monogenicwavelettoolbox;

import ij.ImagePlus;
import monogenicwavelettoolbox.FloatArray3D;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.MonogenicDecompositionTree;
import monogenicwavelettoolbox.RieszTupel;
import monogenicwavelettoolbox.RieszTupel3D;

public class MonogenicDecompositionTree3D
extends MonogenicDecompositionTree {
    public MonogenicDecompositionTree3D(ImagePlus oimp, int numberOfOctaves, int numberOfChannels, int orderOfWavelet) {
        super(oimp, numberOfOctaves, numberOfChannels, orderOfWavelet);
        this.mDimension = 3;
        this.mImage = new FloatArray3D(oimp);
        this.subtractMeanValue();
        this.mOriginalRieszTupel = new RieszTupel3D(this.mImage.duplicate());
        this.computeDecompositionTree();
    }
}

