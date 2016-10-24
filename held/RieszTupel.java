/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 */
package monogenicwavelettoolbox;

import ij.ImagePlus;
import java.util.List;
import java.util.Vector;
import monogenicwavelettoolbox.FloatArray2D;
import monogenicwavelettoolbox.FloatArray3D;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.FourierFilters;
import monogenicwavelettoolbox.PropertiesEnum;
import monogenicwavelettoolbox.PropertyDoesNotExistException;
import monogenicwavelettoolbox.RieszTupel2D;
import monogenicwavelettoolbox.RieszTupel3D;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class RieszTupel {
    protected FloatArrayGeneric mImage;
    protected Vector<FloatArrayGeneric> mImageDerivativesVector;
    protected Vector<FloatArrayGeneric> mRieszVector;
    protected Vector<Vector<FloatArrayGeneric>> mRieszDerivativesVectorVector;
    private static /* synthetic */ int[] $SWITCH_TABLE$monogenicwavelettoolbox$PropertiesEnum;

    public RieszTupel(FloatArrayGeneric f) {
        this.mImage = f;
        this.computeRieszTransforms();
    }

    public RieszTupel(FloatArrayGeneric f, Vector<FloatArrayGeneric> rieszVector) {
        this.mImage = f;
        this.mRieszVector = rieszVector;
    }

    void computeRieszTransforms() {
        List<FloatArrayGeneric> filterList = FourierFilters.getRieszFilter(this.getImage(), true);
        Vector<FloatArrayGeneric> rieszTransformVector = new Vector<FloatArrayGeneric>(filterList.size());
        FloatArrayGeneric complexImage = this.mImage.duplicate();
        complexImage.fft();
        for (FloatArrayGeneric filter : filterList) {
            FloatArrayGeneric rieszImage = complexImage.duplicate();
            rieszImage.multiply(filter);
            rieszImage.ifft();
            rieszTransformVector.add(rieszImage);
        }
        this.mRieszVector = rieszTransformVector;
    }

    public FloatArrayGeneric getR1() {
        return this.mRieszVector.get(0).duplicate();
    }

    public FloatArrayGeneric getR2() {
        return this.mRieszVector.get(1).duplicate();
    }

    public FloatArrayGeneric getR3() {
        if (this.mRieszVector.size() < 3) {
            return null;
        }
        return this.mRieszVector.get(2).duplicate();
    }

    public void showImage() {
        this.getImage().getImagePlusReal("Image").show();
    }

    public void showR1() {
        this.getR1().getImagePlusReal("R1").show();
    }

    public void showR2() {
        this.getR2().getImagePlusReal("R2").show();
    }

    public void showR3() {
        if (this.getR3() == null) {
            return;
        }
        this.getR3().getImagePlusReal("R3").show();
    }

    public void showAll() {
        this.showR1();
        this.showR2();
        this.showR3();
    }

    protected void setR1(FloatArrayGeneric fArr) {
        this.mRieszVector.set(0, fArr);
    }

    protected void setR2(FloatArrayGeneric fArr) {
        this.mRieszVector.set(1, fArr);
    }

    protected void setR3(FloatArrayGeneric fArr) {
        this.mRieszVector.set(2, fArr);
    }

    public FloatArrayGeneric getImage() {
        return this.mImage.duplicate();
    }

    public abstract FloatArrayGeneric getAmplitude();

    public FloatArrayGeneric getAttenuation() {
        return this.getAmplitude().log();
    }

    public FloatArrayGeneric getPhase() {
        return this.getRNorm().atan2(this.getImage());
    }

    public FloatArrayGeneric getFrequency() throws PropertyDoesNotExistException {
        if (this.mRieszDerivativesVectorVector == null || this.mImageDerivativesVector == null) {
            throw new PropertyDoesNotExistException();
        }
        FloatArrayGeneric sum1 = this.mImage.zerosOfSameSize();
        FloatArrayGeneric sum2 = this.mImage.zerosOfSameSize();
        int i = 0;
        while (i < this.mImageDerivativesVector.size()) {
            FloatArrayGeneric prod1 = this.mImageDerivativesVector.get(i).duplicate().multiply(this.mRieszVector.get(i));
            sum1.sub(prod1);
            sum2.add(this.mRieszDerivativesVectorVector.get(i).get(i));
            ++i;
        }
        sum2.multiply(this.mImage);
        sum1.add(sum2);
        sum1.divideReal(this.getAmplitude().sq().add(9.999999747378752E-5));
        return sum1;
    }

    public abstract FloatArrayGeneric getRNorm();

    public abstract FloatArrayGeneric getOrientationAngle(boolean var1) throws PropertyDoesNotExistException;

    public FloatArrayGeneric getProperty(PropertiesEnum property) throws PropertyDoesNotExistException {
        switch (RieszTupel.$SWITCH_TABLE$monogenicwavelettoolbox$PropertiesEnum()[property.ordinal()]) {
            case 1: {
                return this.getImage();
            }
            case 2: {
                return this.getR1();
            }
            case 3: {
                return this.getR2();
            }
            case 4: {
                return this.getR3();
            }
            case 5: {
                return this.getAmplitude();
            }
            case 6: {
                return this.getAttenuation();
            }
            case 7: {
                return this.getPhase();
            }
            case 8: {
                return this.getPhase().cos();
            }
            case 9: {
                return this.getOrientationAngle(true);
            }
            case 10: {
                return this.getFrequency();
            }
        }
        throw new PropertyDoesNotExistException();
    }

    public void setImageDerivatives(Vector<FloatArrayGeneric> imageDerivatives) {
        this.mImageDerivativesVector = imageDerivatives;
    }

    public void setRieszDerivatives(Vector<Vector<FloatArrayGeneric>> rieszDerivatives) {
        this.mRieszDerivativesVectorVector = rieszDerivatives;
    }

    public static RieszTupel createRieszTupel(FloatArrayGeneric floatArr) {
        if (floatArr instanceof FloatArray2D) {
            return new RieszTupel2D((FloatArray2D)floatArr);
        }
        return new RieszTupel3D((FloatArray3D)floatArr);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$monogenicwavelettoolbox$PropertiesEnum() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$monogenicwavelettoolbox$PropertiesEnum;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[PropertiesEnum.values().length];
        try {
            arrn[PropertiesEnum.AMPLITUDE.ordinal()] = 5;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[PropertiesEnum.ATTENUATION.ordinal()] = 6;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[PropertiesEnum.COS_PHASE.ordinal()] = 8;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[PropertiesEnum.FREQUENCY.ordinal()] = 10;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[PropertiesEnum.ORIENTATION.ordinal()] = 9;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[PropertiesEnum.ORIGINAL.ordinal()] = 1;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[PropertiesEnum.PHASE.ordinal()] = 7;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[PropertiesEnum.R1.ordinal()] = 2;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[PropertiesEnum.R2.ordinal()] = 3;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[PropertiesEnum.R3.ordinal()] = 4;
        }
        catch (NoSuchFieldError v10) {}
        $SWITCH_TABLE$monogenicwavelettoolbox$PropertiesEnum = arrn;
        return $SWITCH_TABLE$monogenicwavelettoolbox$PropertiesEnum;
    }
}

