/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 */
package imageware;

import ij.ImageStack;
import imageware.Access;

public interface Pointwise
extends Access {
    public void fillConstant(double var1);

    public void fillRamp();

    public void fillGaussianNoise(double var1);

    public void fillUniformNoise(double var1);

    public void fillSaltPepper(double var1, double var3, double var5, double var7);

    public ImageStack buildImageStack();

    public void invert();

    public void negate();

    public void rescale();

    public void clip();

    public void clip(double var1, double var3);

    public void rescale(double var1, double var3);

    public void rescaleCenter(double var1, double var3);

    public void abs();

    public void log();

    public void exp();

    public void sqrt();

    public void sqr();

    public void pow(double var1);

    public void add(double var1);

    public void multiply(double var1);

    public void subtract(double var1);

    public void divide(double var1);

    public void threshold(double var1);

    public void threshold(double var1, double var3, double var5);

    public void thresholdHard(double var1);

    public void thresholdSoft(double var1);

    public void addGaussianNoise(double var1);

    public void addUniformNoise(double var1);

    public void addSaltPepper(double var1, double var3, double var5, double var7);
}

