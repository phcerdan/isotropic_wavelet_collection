/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.IJ
 *  ij.ImagePlus
 *  ij.gui.GenericDialog
 *  ij.plugin.filter.PlugInFilter
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package monogenicwavelettoolbox;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.awt.Color;

public class ColorSpaceConverterPlugin
implements PlugInFilter {
    static final String version = "$Id$";
    String title;
    String to;
    String from;
    boolean separated;
    String whitePoint;
    ColorSpaceConverter csc;

    public void run(ImageProcessor ip) {
        int cols = ip.getWidth();
        int rows = ip.getHeight();
        ImagePlus[] imps = null;
        ImageProcessor[] ips = null;
        if (this.separated) {
            imps = new ImagePlus[3];
            ips = new ImageProcessor[]{new FloatProcessor(cols, rows), new FloatProcessor(cols, rows), new FloatProcessor(cols, rows)};
            String[] label = new String[3];
            if (this.to.equals("RGB")) {
                label[0] = " (R)";
                label[1] = " (G)";
                label[2] = " (B)";
            } else if (this.to.equals("HSB")) {
                label[0] = " (H)";
                label[1] = " (S)";
                label[2] = " (B)";
            } else if (this.to.equals("LAB")) {
                label[0] = " (L*)";
                label[1] = " (a*)";
                label[2] = " (b*)";
            } else if (this.to.equals("XYZ")) {
                label[0] = " (X)";
                label[1] = " (Y)";
                label[2] = " (Z)";
            }
            imps[0] = new ImagePlus(String.valueOf(this.title) + label[0], ips[0]);
            imps[1] = new ImagePlus(String.valueOf(this.title) + label[1], ips[1]);
            imps[2] = new ImagePlus(String.valueOf(this.title) + label[2], ips[2]);
        }
        int[] values = new int[3];
        double[] dvalues = new double[3];
        int x = 0;
        while (x < cols) {
            int y = 0;
            while (y < rows) {
                values = ip.getPixel(x, y, values);
                if (this.from.equals("RGB")) {
                    if (this.to.equals("HSB")) {
                        dvalues = this.csc.RGBtoHSB(values);
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] * 245.0);
                            values[1] = (int)Math.round(dvalues[1] * 245.0);
                            values[2] = (int)Math.round(dvalues[2] * 245.0);
                        }
                    } else if (this.to.equals("LAB")) {
                        dvalues = this.csc.RGBtoLAB(values);
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] / 125.0 * 245.0);
                            values[1] = (int)Math.round((dvalues[1] + 125.0) / 250.0 * 245.0);
                            values[2] = (int)Math.round((dvalues[2] + 125.0) / 250.0 * 245.0);
                        }
                    } else if (this.to.equals("XYZ")) {
                        dvalues = this.csc.RGBtoXYZ(values);
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] / 125.0 * 245.0);
                            values[1] = (int)Math.round(dvalues[1] / 125.0 * 245.0);
                            values[2] = (int)Math.round(dvalues[2] / 125.0 * 245.0);
                        }
                    }
                } else if (this.from.equals("HSB")) {
                    dvalues[0] = (double)values[0] / 245.0;
                    dvalues[1] = (double)values[1] / 245.0;
                    dvalues[2] = (double)values[2] / 245.0;
                    if (this.to.equals("RGB")) {
                        values = this.csc.HSBtoRGB(dvalues);
                        if (this.separated) {
                            dvalues[0] = values[0];
                            dvalues[1] = values[1];
                            dvalues[2] = values[2];
                        }
                    } else if (this.to.equals("LAB")) {
                        dvalues = this.csc.RGBtoLAB(this.csc.HSBtoRGB(dvalues));
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] / 125.0 * 245.0);
                            values[1] = (int)Math.round((dvalues[1] + 125.0) / 250.0 * 245.0);
                            values[2] = (int)Math.round((dvalues[2] + 125.0) / 250.0 * 245.0);
                        }
                    } else if (this.to.equals("XYZ")) {
                        dvalues = this.csc.RGBtoXYZ(this.csc.HSBtoRGB(dvalues));
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] / 125.0 * 245.0);
                            values[1] = (int)Math.round(dvalues[1] / 125.0 * 245.0);
                            values[2] = (int)Math.round(dvalues[2] / 125.0 * 245.0);
                        }
                    }
                } else if (this.from.equals("LAB")) {
                    dvalues[0] = (double)values[0] / 245.0 * 125.0;
                    dvalues[1] = (double)values[1] / 245.0 * 250.0 - 125.0;
                    dvalues[2] = (double)values[2] / 245.0 * 250.0 - 125.0;
                    if (this.to.equals("RGB")) {
                        values = this.csc.LABtoRGB(dvalues);
                        if (this.separated) {
                            dvalues[0] = values[0];
                            dvalues[1] = values[1];
                            dvalues[2] = values[2];
                        }
                    } else if (this.to.equals("HSB")) {
                        dvalues = this.csc.RGBtoHSB(this.csc.LABtoRGB(dvalues));
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] * 245.0);
                            values[1] = (int)Math.round(dvalues[1] * 245.0);
                            values[2] = (int)Math.round(dvalues[2] * 245.0);
                        }
                    } else if (this.to.equals("XYZ")) {
                        dvalues = this.csc.LABtoXYZ(dvalues);
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] / 125.0 * 245.0);
                            values[1] = (int)Math.round(dvalues[1] / 125.0 * 245.0);
                            values[2] = (int)Math.round(dvalues[2] / 125.0 * 245.0);
                        }
                    }
                } else if (this.from.equals("XYZ")) {
                    dvalues[0] = (double)values[0] / 245.0 * 125.0;
                    dvalues[1] = (double)values[1] / 245.0 * 125.0;
                    dvalues[2] = (double)values[2] / 245.0 * 125.0;
                    if (this.to.equals("RGB")) {
                        values = this.csc.XYZtoRGB(dvalues);
                        if (this.separated) {
                            dvalues[0] = values[0];
                            dvalues[1] = values[1];
                            dvalues[2] = values[2];
                        }
                    } else if (this.to.equals("LAB")) {
                        dvalues = this.csc.XYZtoLAB(dvalues);
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] / 125.0 * 255.0);
                            values[1] = (int)Math.round((dvalues[1] + 125.0) / 250.0 * 245.0);
                            values[2] = (int)Math.round((dvalues[2] + 125.0) / 250.0 * 245.0);
                        }
                    } else if (this.to.equals("HSB")) {
                        dvalues = this.csc.RGBtoHSB(this.csc.XYZtoRGB(dvalues));
                        if (!this.separated) {
                            values[0] = (int)Math.round(dvalues[0] * 245.0);
                            values[1] = (int)Math.round(dvalues[1] * 245.0);
                            values[2] = (int)Math.round(dvalues[2] * 245.0);
                        }
                    }
                }
                if (this.separated) {
                    ips[0].putPixelValue(x, y, dvalues[0]);
                    ips[1].putPixelValue(x, y, dvalues[1]);
                    ips[2].putPixelValue(x, y, dvalues[2]);
                } else {
                    ip.putPixel(x, y, values);
                }
                ++y;
            }
            ++x;
        }
        if (this.separated) {
            imps[0].getProcessor().resetMinAndMax();
            imps[0].show();
            imps[1].getProcessor().resetMinAndMax();
            imps[1].show();
            imps[2].getProcessor().resetMinAndMax();
            imps[2].show();
        }
    }

    public int setup(String arg, ImagePlus imp) {
        if (arg.equals("about")) {
            this.showAbout();
            return 4096;
        }
        if (imp != null) {
            this.title = imp.getTitle();
            this.separated = false;
            this.to = "RGB";
            this.from = "RGB";
            this.whitePoint = "D65";
            if (this.title.endsWith(" (RGB)")) {
                this.from = "RGB";
            } else if (this.title.endsWith(" (HSB)")) {
                this.from = "HSB";
            } else if (this.title.endsWith(" (LAB)")) {
                this.from = "LAB";
            } else if (this.title.endsWith(" (XYZ)")) {
                this.from = "XYZ";
            }
            if (!this.showDialog()) {
                return 4096;
            }
            if (!this.separated) {
                if (this.title.endsWith(" (RGB)")) {
                    this.title = this.title.substring(0, this.title.length() - 6);
                } else if (this.title.endsWith(" (HSB)")) {
                    this.title = this.title.substring(0, this.title.length() - 6);
                } else if (this.title.endsWith(" (LAB)")) {
                    this.title = this.title.substring(0, this.title.length() - 6);
                } else if (this.title.endsWith(" (XYZ)")) {
                    this.title = this.title.substring(0, this.title.length() - 6);
                }
                imp.setTitle(String.valueOf(this.title) + " (" + this.to + ")");
            }
        }
        this.csc = new ColorSpaceConverter(this, this.whitePoint);
        return 48;
    }

    void showAbout() {
        IJ.showMessage((String)"About Color Space Calculator...", (String)"This plug-in filter converts an image to a different color space.\n");
    }

    boolean showDialog() {
        String[] spaces = new String[]{"RGB", "HSB", "LAB", "XYZ"};
        String[] whitePoints = new String[]{"D50", "D55", "D65", "D75"};
        GenericDialog dialog = new GenericDialog("Color Space Converter settings");
        dialog.addChoice("from ColorSpace", spaces, this.from);
        dialog.addChoice("to ColorSpace", spaces, this.to);
        dialog.addChoice("white point", whitePoints, this.whitePoint);
        dialog.addCheckbox("separate images", this.separated);
        dialog.showDialog();
        if (dialog.wasCanceled()) {
            return false;
        }
        this.from = dialog.getNextChoice();
        this.to = dialog.getNextChoice();
        this.whitePoint = dialog.getNextChoice();
        this.separated = dialog.getNextBoolean();
        if (this.from.equals(this.to)) {
            return false;
        }
        return true;
    }

    public class ColorSpaceConverter {
        public double[] D50;
        public double[] D55;
        public double[] D65;
        public double[] D75;
        public double[] whitePoint;
        public double[] chromaD50;
        public double[] chromaD55;
        public double[] chromaD65;
        public double[] chromaD75;
        public double[] chromaWhitePoint;
        public double[][] M;
        public double[][] Mi;
        final /* synthetic */ ColorSpaceConverterPlugin this$0;

        public ColorSpaceConverter(ColorSpaceConverterPlugin colorSpaceConverterPlugin) {
            this.this$0 = colorSpaceConverterPlugin;
            this.D50 = new double[]{96.4212, 100.0, 82.5188};
            this.D55 = new double[]{95.6797, 100.0, 92.1481};
            this.D65 = new double[]{95.0429, 100.0, 108.89};
            this.D75 = new double[]{94.9722, 100.0, 122.6394};
            this.whitePoint = this.D65;
            this.chromaD50 = new double[]{0.3457, 0.3585, 100.0};
            this.chromaD55 = new double[]{0.3324, 0.3474, 100.0};
            this.chromaD65 = new double[]{0.3127, 0.329, 100.0};
            this.chromaD75 = new double[]{0.299, 0.3149, 100.0};
            this.chromaWhitePoint = this.chromaD65;
            this.M = new double[][]{{0.4124, 0.3576, 0.1805}, {0.2126, 0.7152, 0.0722}, {0.0193, 0.1192, 0.9505}};
            this.Mi = new double[][]{{3.2406, -1.5372, -0.4986}, {-0.9689, 1.8758, 0.0415}, {0.0557, -0.204, 1.057}};
            this.whitePoint = this.D65;
            this.chromaWhitePoint = this.chromaD65;
        }

        public ColorSpaceConverter(ColorSpaceConverterPlugin colorSpaceConverterPlugin, String white) {
            this.this$0 = colorSpaceConverterPlugin;
            this.D50 = new double[]{96.4212, 100.0, 82.5188};
            this.D55 = new double[]{95.6797, 100.0, 92.1481};
            this.D65 = new double[]{95.0429, 100.0, 108.89};
            this.D75 = new double[]{94.9722, 100.0, 122.6394};
            this.whitePoint = this.D65;
            this.chromaD50 = new double[]{0.3457, 0.3585, 100.0};
            this.chromaD55 = new double[]{0.3324, 0.3474, 100.0};
            this.chromaD65 = new double[]{0.3127, 0.329, 100.0};
            this.chromaD75 = new double[]{0.299, 0.3149, 100.0};
            this.chromaWhitePoint = this.chromaD65;
            this.M = new double[][]{{0.4124, 0.3576, 0.1805}, {0.2126, 0.7152, 0.0722}, {0.0193, 0.1192, 0.9505}};
            this.Mi = new double[][]{{3.2406, -1.5372, -0.4986}, {-0.9689, 1.8758, 0.0415}, {0.0557, -0.204, 1.057}};
            this.whitePoint = this.D65;
            this.chromaWhitePoint = this.chromaD65;
            if (white.equalsIgnoreCase("d50")) {
                this.whitePoint = this.D50;
                this.chromaWhitePoint = this.chromaD50;
            } else if (white.equalsIgnoreCase("d55")) {
                this.whitePoint = this.D55;
                this.chromaWhitePoint = this.chromaD55;
            } else if (white.equalsIgnoreCase("d65")) {
                this.whitePoint = this.D65;
                this.chromaWhitePoint = this.chromaD65;
            } else if (white.equalsIgnoreCase("d75")) {
                this.whitePoint = this.D75;
                this.chromaWhitePoint = this.chromaD75;
            }
        }

        public int[] HSBtoRGB(double H, double S, double B) {
            int[] result = new int[3];
            int rgb = Color.HSBtoRGB((float)H, (float)S, (float)B);
            result[0] = rgb >> 16 & 255;
            result[1] = rgb >> 8 & 255;
            result[2] = rgb >> 0 & 255;
            return result;
        }

        public int[] HSBtoRGB(double[] HSB) {
            return this.HSBtoRGB(HSB[0], HSB[1], HSB[2]);
        }

        public int[] LABtoRGB(double L, double a, double b) {
            return this.XYZtoRGB(this.LABtoXYZ(L, a, b));
        }

        public int[] LABtoRGB(double[] Lab) {
            return this.XYZtoRGB(this.LABtoXYZ(Lab));
        }

        public double[] LABtoXYZ(double L, double a, double b) {
            double[] result = new double[3];
            double y = (L + 16.0) / 116.0;
            double y3 = Math.pow(y, 3.0);
            double x = a / 500.0 + y;
            double x3 = Math.pow(x, 3.0);
            double z = y - b / 200.0;
            double z3 = Math.pow(z, 3.0);
            y = y3 > 0.008856 ? y3 : (y - 0.13793103448275862) / 7.787;
            x = x3 > 0.008856 ? x3 : (x - 0.13793103448275862) / 7.787;
            z = z3 > 0.008856 ? z3 : (z - 0.13793103448275862) / 7.787;
            result[0] = x * this.whitePoint[0];
            result[1] = y * this.whitePoint[1];
            result[2] = z * this.whitePoint[2];
            return result;
        }

        public double[] LABtoXYZ(double[] Lab) {
            return this.LABtoXYZ(Lab[0], Lab[1], Lab[2]);
        }

        public double[] RGBtoHSB(int R, int G, int B) {
            double[] result = new double[3];
            float[] hsb = new float[3];
            Color.RGBtoHSB(R, G, B, hsb);
            result[0] = hsb[0];
            result[1] = hsb[1];
            result[2] = hsb[2];
            return result;
        }

        public double[] RGBtoHSB(int[] RGB) {
            return this.RGBtoHSB(RGB[0], RGB[1], RGB[2]);
        }

        public double[] RGBtoLAB(int R, int G, int B) {
            return this.XYZtoLAB(this.RGBtoXYZ(R, G, B));
        }

        public double[] RGBtoLAB(int[] RGB) {
            return this.XYZtoLAB(this.RGBtoXYZ(RGB));
        }

        public double[] RGBtoXYZ(int R, int G, int B) {
            double[] result = new double[3];
            double r = (double)R / 255.0;
            double g = (double)G / 255.0;
            double b = (double)B / 255.0;
            r = r <= 0.04045 ? (r /= 12.92) : Math.pow((r + 0.055) / 1.055, 2.4);
            g = g <= 0.04045 ? (g /= 12.92) : Math.pow((g + 0.055) / 1.055, 2.4);
            b = b <= 0.04045 ? (b /= 12.92) : Math.pow((b + 0.055) / 1.055, 2.4);
            result[0] = (r *= 100.0) * this.M[0][0] + (g *= 100.0) * this.M[0][1] + (b *= 100.0) * this.M[0][2];
            result[1] = r * this.M[1][0] + g * this.M[1][1] + b * this.M[1][2];
            result[2] = r * this.M[2][0] + g * this.M[2][1] + b * this.M[2][2];
            return result;
        }

        public double[] RGBtoXYZ(int[] RGB) {
            return this.RGBtoXYZ(RGB[0], RGB[1], RGB[2]);
        }

        public double[] xyYtoXYZ(double x, double y, double Y) {
            double[] result = new double[3];
            if (y == 0.0) {
                result[0] = 0.0;
                result[1] = 0.0;
                result[2] = 0.0;
            } else {
                result[0] = x * Y / y;
                result[1] = Y;
                result[2] = (1.0 - x - y) * Y / y;
            }
            return result;
        }

        public double[] xyYtoXYZ(double[] xyY) {
            return this.xyYtoXYZ(xyY[0], xyY[1], xyY[2]);
        }

        public double[] XYZtoLAB(double X, double Y, double Z) {
            double x = X / this.whitePoint[0];
            double y = Y / this.whitePoint[1];
            double z = Z / this.whitePoint[2];
            x = x > 0.008856 ? Math.pow(x, 0.3333333333333333) : 7.787 * x + 0.13793103448275862;
            y = y > 0.008856 ? Math.pow(y, 0.3333333333333333) : 7.787 * y + 0.13793103448275862;
            z = z > 0.008856 ? Math.pow(z, 0.3333333333333333) : 7.787 * z + 0.13793103448275862;
            double[] result = new double[]{116.0 * y - 16.0, 500.0 * (x - y), 200.0 * (y - z)};
            return result;
        }

        public double[] XYZtoLAB(double[] XYZ) {
            return this.XYZtoLAB(XYZ[0], XYZ[1], XYZ[2]);
        }

        public int[] XYZtoRGB(double X, double Y, double Z) {
            int[] result = new int[3];
            double x = X / 100.0;
            double y = Y / 100.0;
            double z = Z / 100.0;
            double r = x * this.Mi[0][0] + y * this.Mi[0][1] + z * this.Mi[0][2];
            double g = x * this.Mi[1][0] + y * this.Mi[1][1] + z * this.Mi[1][2];
            double b = x * this.Mi[2][0] + y * this.Mi[2][1] + z * this.Mi[2][2];
            r = r > 0.0031308 ? 1.055 * Math.pow(r, 0.4166666666666667) - 0.055 : (r *= 12.92);
            g = g > 0.0031308 ? 1.055 * Math.pow(g, 0.4166666666666667) - 0.055 : (g *= 12.92);
            b = b > 0.0031308 ? 1.055 * Math.pow(b, 0.4166666666666667) - 0.055 : (b *= 12.92);
            r = r < 0.0 ? 0.0 : r;
            g = g < 0.0 ? 0.0 : g;
            b = b < 0.0 ? 0.0 : b;
            result[0] = (int)Math.round(r * 255.0);
            result[1] = (int)Math.round(g * 255.0);
            result[2] = (int)Math.round(b * 255.0);
            return result;
        }

        public int[] XYZtoRGB(double[] XYZ) {
            return this.XYZtoRGB(XYZ[0], XYZ[1], XYZ[2]);
        }

        public double[] XYZtoxyY(double X, double Y, double Z) {
            double[] result = new double[3];
            if (X + Y + Z == 0.0) {
                result[0] = this.chromaWhitePoint[0];
                result[1] = this.chromaWhitePoint[1];
                result[2] = this.chromaWhitePoint[2];
            } else {
                result[0] = X / (X + Y + Z);
                result[1] = Y / (X + Y + Z);
                result[2] = Y;
            }
            return result;
        }

        public double[] XYZtoxyY(double[] XYZ) {
            return this.XYZtoxyY(XYZ[0], XYZ[1], XYZ[2]);
        }
    }

}

