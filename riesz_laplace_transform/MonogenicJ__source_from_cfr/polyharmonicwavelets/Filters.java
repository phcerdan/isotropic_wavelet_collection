/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

import polyharmonicwavelets.ComplexImage;
import polyharmonicwavelets.Parameters;

public abstract class Filters {
    public ComplexImage[] FA;
    public ComplexImage[] FS;
    public ComplexImage[] FP;
    public ComplexImage P;
    public ComplexImage ac;
    protected Parameters param;
    protected int nx;
    protected int ny;

    public Filters(Parameters param, int nx, int ny) {
        this.param = param;
        this.nx = nx;
        this.ny = ny;
        this.FA = new ComplexImage[4];
        this.FS = new ComplexImage[4];
    }

    public void setParameters(Parameters param) {
        this.param = param;
    }

    public abstract void compute();
}

