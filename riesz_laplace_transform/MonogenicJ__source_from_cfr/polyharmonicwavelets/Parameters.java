/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

public class Parameters {
    public boolean analysesonly = false;
    public int rieszfreq = 0;
    public static final int BSPLINE = 0;
    public static final int ORTHOGONAL = 1;
    public static final int DUAL = 2;
    public static final int OPERATOR = 3;
    public static final int MARR = 7;
    public static final int DUALOPERATOR = 8;
    public int flavor = 1;
    public static final int BASIS = 0;
    public static final int REDUNDANT = 1;
    public static final int PYRAMID = 2;
    public int redundancy = 2;
    public static final int ISOTROPIC = 1;
    public static final int CHANGESIGMA = 4;
    public int type = 1;
    public double s2 = 6.0;
    public static final int QUINCUNX = 0;
    public static final int DYADIC = 1;
    public int lattice = 1;
    public boolean prefilter = true;
    public double order = 2.0;
    public int N = 1;
    public int J = 1;

    public String toString() {
        String s = "";
        s = s + "analysesonly: " + this.analysesonly + " ::: flavor: " + this.flavor + " ::: redundancy: " + this.redundancy + " ::: type: " + this.type + " \n";
        s = s + "lattice: " + this.lattice + " :::  prefilter: " + this.prefilter + " \n";
        s = s + "order: " + this.order + " ::: N: " + this.N + " ::: J: " + this.J + " \n";
        return s;
    }
}

