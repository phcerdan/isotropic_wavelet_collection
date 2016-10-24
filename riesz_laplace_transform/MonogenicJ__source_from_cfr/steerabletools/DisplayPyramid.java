/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.IJ
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.ColorProcessor
 *  ij.process.ImageProcessor
 */
package steerabletools;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import imageware.Builder;
import imageware.ImageWare;
import java.awt.Color;

public class DisplayPyramid {
    public static int RESCALE = 0;
    public static int NORESCALE = 1;

    public static void show(ImageWare image, String title, int scaled, int stacked, boolean pyramid) {
        ImageWare disp = DisplayPyramid.create(image, scaled, stacked, pyramid);
        disp.show(title);
    }

    public static ImageWare create(ImageWare image, int scaled, int stacked, boolean pyramid) {
        int nscale;
        int py = 0;
        int px = 0;
        int mx = image.getWidth();
        int my = image.getHeight();
        int nx = mx;
        int ny = my;
        int factSize = nscale = image.getSizeZ();
        if (pyramid) {
            int n = factSize = nscale > 1 ? 2 : 1;
        }
        ImageWare out = stacked == 0 ? Builder.create(nx, ny, nscale, scaled == RESCALE ? 1 : 3) : (stacked == 1 ? Builder.create(nx * factSize, ny, 1, scaled == RESCALE ? 1 : 3) : Builder.create(nx, ny * factSize, 1, scaled == RESCALE ? 1 : 3));
        for (int k = 0; k < nscale; ++k) {
            ImageWare band = Builder.create(mx, my, 1, 3);
            image.getXY(0, 0, k, band);
            if (scaled == RESCALE) {
                band.rescale();
            }
            if (stacked == 0) {
                out.putXY(0, 0, k, band);
            } else if (stacked == 1) {
                out.putXY(px, 0, 0, band);
            } else {
                out.putXY(0, py, 0, band);
            }
            if (pyramid) {
                mx /= 2;
                my /= 2;
                if (stacked == 1) {
                    px = (int)((double)px + (double)nx / Math.pow(2.0, k));
                }
                if (stacked != 2) continue;
                py = (int)((double)py + (double)ny / Math.pow(2.0, k));
                continue;
            }
            if (stacked == 1) {
                px += nx;
            }
            if (stacked != 2) continue;
            py += ny;
        }
        return out;
    }

    public static ImagePlus colorHSB(String name, ImageWare hue, ImageWare sat, ImageWare bri, int stacked, boolean pyramid) {
        int py = 0;
        int px = 0;
        int nx = hue.getSizeX();
        int ny = hue.getSizeY();
        int mx = nx;
        int my = ny;
        int nscale = hue.getSizeZ();
        int fx = nx;
        int fy = ny;
        if (pyramid) {
            if (stacked == 1) {
                fx = nx * (nscale > 1 ? 2 : 1);
            }
            if (stacked == 2) {
                fy = ny * (nscale > 1 ? 2 : 1);
            }
        } else {
            if (stacked == 1) {
                fx = nx * nscale;
            }
            if (stacked == 2) {
                fy = ny * nscale;
            }
        }
        int size = fx * fy;
        ImageStack stack = new ImageStack(fx, fy);
        int[] cpixels = new int[size];
        for (int k = 0; k < nscale; ++k) {
            int x;
            float h;
            int y;
            float s;
            float b;
            IJ.showStatus((String)("Show Color Image " + (k + 1) + "/" + nscale));
            if (stacked == 0) {
                ColorProcessor cp = new ColorProcessor(fx, fy);
                for (y = 0; y < my; ++y) {
                    for (x = 0; x < mx; ++x) {
                        h = (float)hue.getPixel(x, y, k);
                        s = (float)sat.getPixel(x, y, k);
                        b = (float)bri.getPixel(x, y, k);
                        cp.putPixel(x, y, Color.HSBtoRGB(h, s, b) + -16777216);
                    }
                }
                stack.addSlice("", (ImageProcessor)cp);
            } else {
                int[] pixels = new int[size];
                for (y = 0; y < my; ++y) {
                    for (x = 0; x < mx; ++x) {
                        h = (float)hue.getPixel(x, y, k);
                        s = (float)sat.getPixel(x, y, k);
                        b = (float)bri.getPixel(x, y, k);
                        cpixels[(py + y) * fx + (x + px)] = Color.HSBtoRGB(h, s, b) + -16777216;
                    }
                }
            }
            if (pyramid) {
                mx /= 2;
                my /= 2;
                if (stacked == 1) {
                    px = (int)((double)px + (double)nx / Math.pow(2.0, k));
                }
                if (stacked != 2) continue;
                py = (int)((double)py + (double)ny / Math.pow(2.0, k));
                continue;
            }
            if (stacked == 1) {
                px += nx;
            }
            if (stacked != 2) continue;
            py += ny;
        }
        if (stacked != 0) {
            stack.addSlice("", (ImageProcessor)new ColorProcessor(fx, fy, cpixels));
        }
        ImagePlus imp = new ImagePlus(name, stack);
        imp.show();
        return imp;
    }

    public static ImagePlus colorRGB(String name, ImageWare red, ImageWare green, ImageWare blue) {
        int nx = red.getSizeX();
        int ny = red.getSizeY();
        int nz = red.getSizeZ();
        ImageStack stack = new ImageStack(nx, ny);
        int size = nx * ny;
        for (int k = 0; k < nz; ++k) {
            IJ.showStatus((String)("Show Color Image " + (k + 1) + "/" + nz));
            int[] pixels = new int[size];
            float[] r = red.getSliceFloat(k < red.getSizeZ() ? k : 0);
            float[] g = green.getSliceFloat(k < green.getSizeZ() ? k : 0);
            float[] b = blue.getSliceFloat(k < blue.getSizeZ() ? k : 0);
            for (int index = 0; index < size; ++index) {
                int ri = (int)(r[index] * 255.0f);
                int gi = (int)(g[index] * 255.0f);
                int bi = (int)(b[index] * 255.0f);
                pixels[index] = bi + (gi << 8) + (ri << 16) + -16777216;
            }
            stack.addSlice("", (ImageProcessor)new ColorProcessor(nx, ny, pixels));
            IJ.showProgress((double)((double)k / (double)nz));
        }
        ImagePlus imp = new ImagePlus(name, stack);
        imp.show();
        return imp;
    }

    public static ImageWare rescaleAngle(ImageWare in, boolean pyramid) {
        int nx = in.getSizeX();
        int ny = in.getSizeY();
        int nz = in.getSizeZ();
        int mx = nx;
        int my = ny;
        float PI = 3.1415927f;
        float PI2 = 1.5707964f;
        ImageWare out = in.replicate();
        for (int k = 0; k < nz; ++k) {
            ImageWare band = Builder.create(mx, my, 1, 3);
            in.getXY(0, 0, k, band);
            float[] pix = band.getSliceFloat(0);
            float[] opix = out.getSliceFloat(k);
            for (int x = 0; x < mx; ++x) {
                for (int y = 0; y < my; ++y) {
                    opix[x + y * nx] = (pix[x + y * mx] + PI2) / PI;
                }
            }
            if (!pyramid) continue;
            mx /= 2;
            my /= 2;
        }
        return out;
    }
}

