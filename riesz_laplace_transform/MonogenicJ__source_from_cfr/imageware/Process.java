/*
 * Decompiled with CFR 0_118.
 */
package imageware;

import imageware.ImageWare;
import imageware.Pointwise;

public interface Process
extends Pointwise {
    public void smoothGaussian(double var1);

    public void smoothGaussian(double var1, double var3, double var5);

    public void max(ImageWare var1);

    public void min(ImageWare var1);

    public void add(ImageWare var1);

    public void multiply(ImageWare var1);

    public void subtract(ImageWare var1);

    public void divide(ImageWare var1);
}

