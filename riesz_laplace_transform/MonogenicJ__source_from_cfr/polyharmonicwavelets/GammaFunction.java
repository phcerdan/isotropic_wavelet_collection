/*
 * Decompiled with CFR 0_118.
 */
package polyharmonicwavelets;

public class GammaFunction {
    public static double lnGamma(double alpha) {
        double z;
        double x = alpha;
        double f = 0.0;
        if (x < 7.0) {
            f = 1.0;
            z = x - 1.0;
            while ((z += 1.0) < 7.0) {
                f *= z;
            }
            x = z;
            f = - Math.log(f);
        }
        z = 1.0 / (x * x);
        return f + (x - 0.5) * Math.log(x) - x + 0.918938533204673 + (((-5.95238095238E-4 * z + 7.93650793651E-4) * z - 0.002777777777778) * z + 0.083333333333333) / x;
    }

    public static double incompleteGammaQ(double a, double x) {
        return 1.0 - GammaFunction.incompleteGamma(x, a, GammaFunction.lnGamma(a));
    }

    public static double incompleteGammaP(double a, double x) {
        return GammaFunction.incompleteGamma(x, a, GammaFunction.lnGamma(a));
    }

    public static double incompleteGammaP(double a, double x, double lnGammaA) {
        return GammaFunction.incompleteGamma(x, a, lnGammaA);
    }

    private static double incompleteGamma(double x, double alpha, double ln_gamma_alpha) {
        double gin;
        double accurate = 1.0E-8;
        double overflow = 1.0E30;
        if (x == 0.0) {
            return 0.0;
        }
        if (x < 0.0 || alpha <= 0.0) {
            throw new IllegalArgumentException("Arguments out of bounds");
        }
        double factor = Math.exp(alpha * Math.log(x) - x - ln_gamma_alpha);
        if (x > 1.0 && x >= alpha) {
            double a = 1.0 - alpha;
            double b = a + x + 1.0;
            double term = 0.0;
            double pn0 = 1.0;
            double pn1 = x;
            double pn2 = x + 1.0;
            double pn3 = x * b;
            gin = pn2 / pn3;
            do {
                double an = (a += 1.0) * (term += 1.0);
                double pn4 = (b += 2.0) * pn2 - an * pn0;
                double pn5 = b * pn3 - an * pn1;
                if (pn5 != 0.0) {
                    double rn = pn4 / pn5;
                    double dif = Math.abs(gin - rn);
                    if (dif <= accurate && dif <= accurate * rn) break;
                    gin = rn;
                }
                pn0 = pn2;
                pn1 = pn3;
                pn2 = pn4;
                pn3 = pn5;
                if (Math.abs(pn4) < overflow) continue;
                pn0 /= overflow;
                pn1 /= overflow;
                pn2 /= overflow;
                pn3 /= overflow;
            } while (true);
            gin = 1.0 - factor * gin;
        } else {
            gin = 1.0;
            double term = 1.0;
            double rn = alpha;
            do {
                gin += (term *= x / (rn += 1.0));
            } while (term > accurate);
            gin *= factor / alpha;
        }
        return gin;
    }
}

