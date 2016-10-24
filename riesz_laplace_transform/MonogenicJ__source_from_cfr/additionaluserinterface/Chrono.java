/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import java.text.DecimalFormat;

public class Chrono {
    private static double chrono = 0.0;

    public static void tic() {
        chrono = System.currentTimeMillis();
    }

    public static String toc() {
        return Chrono.toc("");
    }

    public static String toc(String string) {
        double d = (double)System.currentTimeMillis() - chrono;
        String string2 = string + " ";
        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        if (d < 3000.0) {
            return string2 + decimalFormat.format(d) + " ms";
        }
        if ((d /= 1000.0) < 600.1) {
            return string2 + decimalFormat.format(d) + " s";
        }
        if ((d /= 60.0) < 240.1) {
            return string2 + decimalFormat.format(d) + " min.";
        }
        return string2 + decimalFormat.format(d /= 24.0) + " h.";
    }
}

