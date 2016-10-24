/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

import java.util.Vector;
import monogenicwavelettoolbox.FloatArray2D;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.RieszTupel;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RieszTupel2D
extends RieszTupel {
    public RieszTupel2D(FloatArray2D f) {
        super(f);
    }

    public RieszTupel2D(FloatArray2D f, Vector<FloatArrayGeneric> r) {
        super(f);
        this.mRieszVector = r;
    }

    @Override
    public FloatArrayGeneric getAmplitude() {
        FloatArrayGeneric amp = this.getImage().duplicate();
        return amp.sq().add(this.getR1().sq()).add(this.getR2().sq()).sqrt();
    }

    @Override
    public FloatArrayGeneric getRNorm() {
        FloatArray2D norm = (FloatArray2D)this.getR1();
        return norm.sq().add(this.getR2().sq()).sqrt();
    }

    @Override
    public FloatArrayGeneric getOrientationAngle(boolean phaseWrapping) {
        FloatArray2D fArr = (FloatArray2D)this.getR2().atan2(this.getR1());
        if (phaseWrapping) {
            fArr.phaseWrapping();
        }
        return fArr;
    }
}

