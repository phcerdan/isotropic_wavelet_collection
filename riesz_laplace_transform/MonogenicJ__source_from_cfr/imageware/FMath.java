/*
 * Decompiled with CFR 0_118.
 */
package imageware;

public final class FMath {
    public static int floor(float f) {
        if (f >= 0.0f) {
            return (int)f;
        }
        int n = (int)f - 1;
        return (int)(f - (float)n) + n;
    }

    public static int floor(double d) {
        if (d >= 0.0) {
            return (int)d;
        }
        int n = (int)d - 1;
        return (int)(d - (double)n) + n;
    }

    public static int ceil(float f) {
        float f2 = - f;
        if (f2 >= 0.0f) {
            return - (int)f2;
        }
        int n = (int)f2 - 1;
        return - (int)(f2 - (float)n) + n;
    }

    public static int ceil(double d) {
        double d2 = - d;
        if (d2 >= 0.0) {
            return - (int)d2;
        }
        int n = (int)d2 - 1;
        return - (int)(d2 - (double)n) + n;
    }

    public static int round(float f) {
        float f2 = f + 0.5f;
        if ((double)f2 >= 0.0) {
            return (int)f2;
        }
        int n = (int)f2 - 1;
        return (int)(f2 - (float)n) + n;
    }

    public static int round(double d) {
        double d2 = d + 0.5;
        if (d2 >= 0.0) {
            return (int)d2;
        }
        int n = (int)d2 - 1;
        return (int)(d2 - (double)n) + n;
    }

    public static float min(float f, float f2) {
        return f < f2 ? f : f2;
    }

    public static double min(double d, double d2) {
        return d < d2 ? d : d2;
    }

    public static float max(float f, float f2) {
        return f > f2 ? f : f2;
    }

    public static double max(double d, double d2) {
        return d > d2 ? d : d2;
    }
}

