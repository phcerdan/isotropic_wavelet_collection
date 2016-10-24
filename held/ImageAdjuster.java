/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.process.ColorProcessor
 *  ij.process.FloatProcessor
 *  ij.process.ImageConverter
 *  ij.process.ImageProcessor
 *  ij.process.StackConverter
 */
package monogenicwavelettoolbox;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.StackConverter;
import java.util.Arrays;
import monogenicwavelettoolbox.ColorSpaceConverterPlugin;

public class ImageAdjuster {
    public static boolean isPowerOf2(int number) {
        while (number >= 2) {
            if (number % 2 != 0) {
                return false;
            }
            number /= 2;
        }
        return true;
    }

    public static int getMaximumPowerOf2(int n) {
        int m = 0;
        while (n >= 2) {
            n /= 2;
            ++m;
        }
        return m;
    }

    public static void normalizeImage(ImagePlus image) throws IllegalArgumentException {
        ImageProcessor ip;
        int j;
        StackConverter imageConverter;
        int i;
        if (image.getType() == 2) {
            return;
        }
        if (image.getStackSize() > 1) {
            imageConverter = new StackConverter(image);
            imageConverter.convertToGray32();
        } else {
            imageConverter = new ImageConverter(image);
            imageConverter.convertToGray32();
        }
        float maxValue = 0.0f;
        ImageStack stack = image.getStack();
        int k = 1;
        while (k < image.getStackSize() + 1) {
            ip = stack.getProcessor(k);
            i = 0;
            while (i < image.getHeight()) {
                j = 0;
                while (j < image.getWidth()) {
                    if (ip.getf(j, i) > maxValue) {
                        maxValue = ip.getf(j, i);
                    }
                    ++j;
                }
                ++i;
            }
            ++k;
        }
        k = 1;
        while (k < image.getStackSize() + 1) {
            ip = stack.getProcessor(k);
            i = 0;
            while (i < image.getHeight()) {
                j = 0;
                while (j < image.getWidth()) {
                    ip.setf(j, i, ip.getf(j, i) / maxValue);
                    ++j;
                }
                ++i;
            }
            ++k;
        }
        image.resetDisplayRange();
        image.updateAndDraw();
    }

    public static void extendSize(ImagePlus imp) {
        if (ImageAdjuster.checkDimension(imp)) {
            return;
        }
        if (imp.getStackSize() == 1) {
            imp.setProcessor(imp.getTitle(), ImageAdjuster.extendSize(imp.getProcessor()));
        }
        imp.updateImage();
    }

    public static ImageProcessor extendSize(ImageProcessor ip) {
        int k;
        int newHeight = ImageAdjuster.nextPowerOf2(ip.getHeight());
        int newWidth = ImageAdjuster.nextPowerOf2(ip.getWidth());
        float[][] oldFloat = ip.getFloatArray();
        float[][] newFloat = new float[newHeight][newWidth];
        int j = 0;
        while (j < ip.getHeight()) {
            k = 0;
            while (k < ip.getWidth()) {
                newFloat[j][k] = oldFloat[j][k];
                ++k;
            }
            ++j;
        }
        j = ip.getHeight();
        while (j < newHeight) {
            k = 0;
            while (k < ip.getWidth()) {
                newFloat[j][k] = oldFloat[2 * ip.getHeight() - j - 1][k];
                ++k;
            }
            ++j;
        }
        j = 0;
        while (j < ip.getHeight()) {
            k = ip.getWidth();
            while (k < newWidth) {
                newFloat[j][k] = oldFloat[j][2 * ip.getWidth() - k - 1];
                ++k;
            }
            ++j;
        }
        j = ip.getHeight();
        while (j < newHeight) {
            k = ip.getWidth();
            while (k < newWidth) {
                newFloat[j][k] = oldFloat[2 * ip.getHeight() - j - 1][2 * ip.getWidth() - k - 1];
                ++k;
            }
            ++j;
        }
        return new FloatProcessor(newFloat);
    }

    public static int nextPowerOf2(int n) {
        int i = 1;
        while (i < n) {
            i *= 2;
        }
        return i;
    }

    public static boolean checkDimension(ImagePlus imp) {
        if (ImageAdjuster.isPowerOf2(imp.getHeight()) && ImageAdjuster.isPowerOf2(imp.getWidth()) && ImageAdjuster.isPowerOf2(imp.getStackSize())) {
            return true;
        }
        return false;
    }

    public static int getMaximalNumberOfOctaves(ImagePlus imp) {
        int dim;
        int n = dim = imp.getHeight() > imp.getWidth() ? imp.getWidth() : imp.getHeight();
        if (imp.getStackSize() > 1) {
            dim = imp.getStackSize() > dim ? dim : imp.getStackSize();
        }
        return ImageAdjuster.getMaximumPowerOf2(dim) - 3;
    }

    public static void scaleArray(float[] arr, float min, float max) {
        float currentMin = arr[0];
        float currentMax = arr[0];
        int i = 0;
        while (i < arr.length) {
            if (arr[i] < currentMin) {
                currentMin = arr[i];
            }
            if (arr[i] > currentMax) {
                currentMax = arr[i];
            }
            ++i;
        }
        float a = (min - max) / (currentMin - currentMax);
        float b = min - a * currentMin;
        int i2 = 0;
        while (i2 < arr.length) {
            arr[i2] = a * arr[i2] + b;
            ++i2;
        }
    }

    public static FloatProcessor[] RGBtoLAB(ColorProcessor ip) {
        ColorSpaceConverterPlugin csc = new ColorSpaceConverterPlugin();
        ColorSpaceConverterPlugin.ColorSpaceConverter colorSpaceConverter = new ColorSpaceConverterPlugin.ColorSpaceConverter(csc);
        FloatProcessor[] ips = new FloatProcessor[]{new FloatProcessor(ip.getWidth(), ip.getHeight()), new FloatProcessor(ip.getWidth(), ip.getHeight()), new FloatProcessor(ip.getWidth(), ip.getHeight())};
        int[] values = new int[3];
        double[] dvalues = new double[3];
        int i = 0;
        while (i < ip.getHeight()) {
            int j = 0;
            while (j < ip.getWidth()) {
                values = ip.getPixel(j, i, values);
                dvalues = colorSpaceConverter.RGBtoLAB(values);
                ips[0].putPixelValue(j, i, dvalues[0]);
                ips[1].putPixelValue(j, i, dvalues[1]);
                ips[2].putPixelValue(j, i, dvalues[2]);
                ++j;
            }
            ++i;
        }
        return ips;
    }

    public static ColorProcessor LABtoRGB(ImageProcessor[] ips) {
        ColorSpaceConverterPlugin csc = new ColorSpaceConverterPlugin();
        ColorSpaceConverterPlugin.ColorSpaceConverter colorSpaceConverter = new ColorSpaceConverterPlugin.ColorSpaceConverter(csc);
        ColorProcessor ip = new ColorProcessor(ips[0].getWidth(), ips[0].getHeight());
        int[] values = new int[3];
        double[] dvalues = new double[3];
        int i = 0;
        while (i < ips[0].getHeight()) {
            int j = 0;
            while (j < ips[0].getWidth()) {
                dvalues[0] = ips[0].getPixelValue(j, i);
                dvalues[1] = ips[1].getPixelValue(j, i);
                dvalues[2] = ips[2].getPixelValue(j, i);
                values = colorSpaceConverter.LABtoRGB(dvalues);
                ip.putPixel(j, i, values);
                ++j;
            }
            ++i;
        }
        return ip;
    }

    public static void monochromeToHue(ImagePlus imp) {
        if (imp.getStackSize() != 1) {
            return;
        }
        byte[] h = (byte[])imp.getProcessor().convertToByte(true).getPixels();
        byte[] s = new byte[imp.getHeight() * imp.getWidth()];
        byte[] b = new byte[imp.getHeight() * imp.getWidth()];
        Arrays.fill(s, -1);
        Arrays.fill(b, -1);
        imp.setProcessor(imp.getTitle(), (ImageProcessor)new ColorProcessor(imp.getWidth(), imp.getHeight()));
        ((ColorProcessor)imp.getProcessor()).setHSB(h, s, b);
    }
}

