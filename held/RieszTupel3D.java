/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

import java.util.Vector;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.PropertyDoesNotExistException;
import monogenicwavelettoolbox.RieszTupel;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RieszTupel3D
extends RieszTupel {
    public RieszTupel3D(FloatArrayGeneric f) {
        super(f);
    }

    public RieszTupel3D(FloatArrayGeneric f, Vector<FloatArrayGeneric> riesz) {
        super(f, riesz);
        this.mRieszVector = riesz;
    }

    @Override
    public FloatArrayGeneric getAmplitude() {
        return this.getImage().sq().add(this.getR1().sq()).add(this.getR2().sq()).add(this.getR3().sq()).sqrt();
    }

    @Override
    public FloatArrayGeneric getOrientationAngle(boolean phaseWrapping) throws PropertyDoesNotExistException {
        throw new PropertyDoesNotExistException();
    }

    @Override
    public FloatArrayGeneric getRNorm() {
        return this.getR1().sq().add(this.getR2().sq()).add(this.getR3().sq()).sqrt();
    }
}

