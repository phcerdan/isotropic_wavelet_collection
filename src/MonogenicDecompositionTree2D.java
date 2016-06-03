/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 */
package monogenicwavelettoolbox;

import ij.ImagePlus;
import monogenicwavelettoolbox.FloatArray2D;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.MonogenicDecompositionTree;
import monogenicwavelettoolbox.RieszTupel;
import monogenicwavelettoolbox.RieszTupel2D;

public class MonogenicDecompositionTree2D
extends MonogenicDecompositionTree {
    private boolean mSymmetricExtension;

    public MonogenicDecompositionTree2D(ImagePlus oimp, int numberOfOctaves, int numberOfChannels, int orderOfWavelet, boolean symmExtend) {
        super(oimp, numberOfOctaves, numberOfChannels, orderOfWavelet);
        this.mDimension = 2;
        this.mImage = new FloatArray2D(oimp);
        this.mImage.extendSizeToPowerOf2();
        this.mSymmetricExtension = symmExtend;
        if (this.mSymmetricExtension) {
            this.mImage.symmetricExtension();
        }
        this.subtractMeanValue();
        this.mOriginalRieszTupel = new RieszTupel2D((FloatArray2D)this.mImage.duplicate());
        this.computeDecompositionTree();
    }
}

