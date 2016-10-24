/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.WindowManager
 *  ij.process.ByteProcessor
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 *  ij.process.ShortProcessor
 */
package imageware;

import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import imageware.ByteSet;
import imageware.DoubleSet;
import imageware.FloatSet;
import imageware.ImageWare;
import imageware.ShortSet;
import java.awt.Image;

public class Builder {
    public static ImageWare wrapOnFocus() {
        ImagePlus imagePlus = WindowManager.getCurrentImage();
        return Builder.wrap(imagePlus.getStack());
    }

    public static ImageWare wrap(ImagePlus imagePlus) {
        return Builder.wrap(imagePlus.getStack());
    }

    public static ImageWare wrap(ImageStack imageStack) {
        ImageProcessor imageProcessor;
        if (imageStack == null) {
            Builder.throw_null();
        }
        if ((imageProcessor = imageStack.getProcessor(1)) instanceof ByteProcessor) {
            return new ByteSet(imageStack, 2);
        }
        if (imageProcessor instanceof ShortProcessor) {
            return new ShortSet(imageStack, 2);
        }
        if (imageProcessor instanceof FloatProcessor) {
            return new FloatSet(imageStack, 2);
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to wrap this ImageStack object.\nSupport only the 8-bit, 16-bit and 32-bits type.\n-------------------------------------------------------\n");
    }

    public static ImageWare create(int n, int n2, int n3, int n4) {
        switch (n4) {
            case 1: {
                return new ByteSet(n, n2, n3);
            }
            case 2: {
                return new ShortSet(n, n2, n3);
            }
            case 3: {
                return new FloatSet(n, n2, n3);
            }
            case 4: {
                return new DoubleSet(n, n2, n3);
            }
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n4 + ".\n" + "-------------------------------------------------------\n");
    }

    public static ImageWare create(Image image) {
        if (image == null) {
            Builder.throw_null();
        }
        return new ByteSet(image, 1);
    }

    public static ImageWare createOnFocus() {
        return Builder.create(WindowManager.getCurrentImage());
    }

    public static ImageWare create(ImageStack imageStack) {
        if (imageStack == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.wrap(imageStack);
        return imageWare.duplicate();
    }

    public static ImageWare create(ImagePlus imagePlus) {
        if (imagePlus == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.wrap(imagePlus);
        return imageWare.duplicate();
    }

    public static ImageWare[] createColors(ImagePlus imagePlus) {
        if (imagePlus == null) {
            Builder.throw_null();
        }
        return Builder.createColors(imagePlus.getStack());
    }

    public static ImageWare[] createColors(ImageStack imageStack) {
        if (imageStack == null) {
            Builder.throw_null();
        }
        ImageWare[] arrimageWare = new ImageWare[]{new ByteSet(imageStack, 0), new ByteSet(imageStack, 1), new ByteSet(imageStack, 2)};
        return arrimageWare;
    }

    public static ImageWare createColorChannel(ImagePlus imagePlus, byte by) {
        if (imagePlus == null) {
            Builder.throw_null();
        }
        return Builder.createColorChannel(imagePlus.getStack(), by);
    }

    public static ImageWare createColorChannel(ImageStack imageStack, byte by) {
        if (imageStack == null) {
            Builder.throw_null();
        }
        return new ByteSet(imageStack, by);
    }

    public static ImageWare create(byte[] arrby) {
        if (arrby == null) {
            Builder.throw_null();
        }
        return new ByteSet(arrby, 1);
    }

    public static ImageWare create(short[] arrs) {
        if (arrs == null) {
            Builder.throw_null();
        }
        return new ShortSet(arrs, 1);
    }

    public static ImageWare create(float[] arrf) {
        if (arrf == null) {
            Builder.throw_null();
        }
        return new FloatSet(arrf, 1);
    }

    public static ImageWare create(double[] arrd) {
        if (arrd == null) {
            Builder.throw_null();
        }
        return new DoubleSet(arrd, 1);
    }

    public static ImageWare create(byte[][] arrby) {
        if (arrby == null) {
            Builder.throw_null();
        }
        return new ByteSet(arrby, 1);
    }

    public static ImageWare create(short[][] arrs) {
        if (arrs == null) {
            Builder.throw_null();
        }
        return new ByteSet(arrs, 1);
    }

    public static ImageWare create(float[][] arrf) {
        if (arrf == null) {
            Builder.throw_null();
        }
        return new FloatSet(arrf, 1);
    }

    public static ImageWare create(double[][] arrd) {
        if (arrd == null) {
            Builder.throw_null();
        }
        return new DoubleSet(arrd, 1);
    }

    public static ImageWare create(byte[][][] arrby) {
        if (arrby == null) {
            Builder.throw_null();
        }
        return new ByteSet(arrby, 1);
    }

    public static ImageWare create(short[][][] arrs) {
        if (arrs == null) {
            Builder.throw_null();
        }
        return new ShortSet(arrs, 1);
    }

    public static ImageWare create(float[][][] arrf) {
        if (arrf == null) {
            Builder.throw_null();
        }
        return new FloatSet(arrf, 1);
    }

    public static ImageWare create(double[][][] arrd) {
        if (arrd == null) {
            Builder.throw_null();
        }
        return new DoubleSet(arrd, 1);
    }

    public static ImageWare create(Image image, int n) {
        if (image == null) {
            Builder.throw_null();
        }
        switch (n) {
            case 1: {
                return new ByteSet(image, 1);
            }
            case 2: {
                return new ShortSet(image, 1);
            }
            case 3: {
                return new FloatSet(image, 1);
            }
            case 4: {
                return new DoubleSet(image, 1);
            }
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n + ".\n" + "-------------------------------------------------------\n");
    }

    public static ImageWare createOnFocus(int n) {
        return Builder.create(WindowManager.getCurrentImage(), n);
    }

    public static ImageWare create(ImagePlus imagePlus, int n) {
        if (imagePlus == null) {
            Builder.throw_null();
        }
        switch (n) {
            case 1: {
                return new ByteSet(imagePlus.getStack(), 1);
            }
            case 2: {
                return new ShortSet(imagePlus.getStack(), 1);
            }
            case 3: {
                return new FloatSet(imagePlus.getStack(), 1);
            }
            case 4: {
                return new DoubleSet(imagePlus.getStack(), 1);
            }
        }
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n + ".\n" + "-------------------------------------------------------\n");
    }

    public static ImageWare create(ImageStack imageStack, int n) {
        if (imageStack == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.wrap(imageStack);
        return imageWare.convert(n);
    }

    public static ImageWare[] createColor(ImagePlus imagePlus, int n) {
        if (imagePlus == null) {
            Builder.throw_null();
        }
        return Builder.createColor(imagePlus.getStack(), n);
    }

    public static ImageWare[] createColor(ImageStack imageStack, int n) {
        if (imageStack == null) {
            Builder.throw_null();
        }
        ImageWare[] arrimageWare = new ImageWare[3];
        switch (n) {
            case 1: {
                arrimageWare[0] = new ByteSet(imageStack, 0);
                arrimageWare[1] = new ByteSet(imageStack, 1);
                arrimageWare[2] = new ByteSet(imageStack, 2);
                break;
            }
            case 2: {
                arrimageWare[0] = new ShortSet(imageStack, 0);
                arrimageWare[1] = new ShortSet(imageStack, 1);
                arrimageWare[2] = new ShortSet(imageStack, 2);
                break;
            }
            case 3: {
                arrimageWare[0] = new FloatSet(imageStack, 0);
                arrimageWare[1] = new FloatSet(imageStack, 1);
                arrimageWare[2] = new FloatSet(imageStack, 2);
                break;
            }
            case 4: {
                arrimageWare[0] = new DoubleSet(imageStack, 0);
                arrimageWare[1] = new DoubleSet(imageStack, 1);
                arrimageWare[2] = new DoubleSet(imageStack, 2);
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n + ".\n" + "-------------------------------------------------------\n");
            }
        }
        return arrimageWare;
    }

    public static ImageWare createColorChannel(ImagePlus imagePlus, byte by, int n) {
        if (imagePlus == null) {
            Builder.throw_null();
        }
        return Builder.createColorChannel(imagePlus.getStack(), by, n);
    }

    public static ImageWare createColorChannel(ImageStack imageStack, byte by, int n) {
        if (imageStack == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = null;
        switch (n) {
            case 1: {
                imageWare = new ByteSet(imageStack, by);
                break;
            }
            case 2: {
                imageWare = new ShortSet(imageStack, by);
                break;
            }
            case 3: {
                imageWare = new FloatSet(imageStack, by);
                break;
            }
            case 4: {
                imageWare = new DoubleSet(imageStack, by);
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnknown type " + n + ".\n" + "-------------------------------------------------------\n");
            }
        }
        return imageWare;
    }

    public static ImageWare create(byte[] arrby, int n) {
        if (arrby == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrby.length, 1, 1, n);
        imageWare.putX(0, 0, 0, arrby);
        return imageWare;
    }

    public static ImageWare create(short[] arrs, int n) {
        if (arrs == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrs.length, 1, 1, n);
        imageWare.putX(0, 0, 0, arrs);
        return imageWare;
    }

    public static ImageWare create(float[] arrf, int n) {
        if (arrf == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrf.length, 1, 1, n);
        imageWare.putX(0, 0, 0, arrf);
        return imageWare;
    }

    public static ImageWare create(double[] arrd, int n) {
        if (arrd == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrd.length, 1, 1, n);
        imageWare.putX(0, 0, 0, arrd);
        return imageWare;
    }

    public static ImageWare create(byte[][] arrby, int n) {
        if (arrby == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrby.length, arrby[0].length, 1, n);
        imageWare.putXY(0, 0, 0, arrby);
        return imageWare;
    }

    public static ImageWare create(short[][] arrs, int n) {
        if (arrs == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrs.length, arrs[0].length, 1, n);
        imageWare.putXY(0, 0, 0, arrs);
        return imageWare;
    }

    public static ImageWare create(float[][] arrf, int n) {
        if (arrf == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrf.length, arrf[0].length, 1, n);
        imageWare.putXY(0, 0, 0, arrf);
        return imageWare;
    }

    public static ImageWare create(double[][] arrd, int n) {
        if (arrd == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrd.length, arrd[0].length, 1, n);
        imageWare.putXY(0, 0, 0, arrd);
        return imageWare;
    }

    public static ImageWare create(byte[][][] arrby, int n) {
        if (arrby == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrby.length, arrby[0].length, arrby[0][0].length, n);
        imageWare.putXYZ(0, 0, 0, arrby);
        return imageWare;
    }

    public static ImageWare create(short[][][] arrs, int n) {
        if (arrs == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrs.length, arrs[0].length, arrs[0][0].length, n);
        imageWare.putXYZ(0, 0, 0, arrs);
        return imageWare;
    }

    public static ImageWare create(float[][][] arrf, int n) {
        if (arrf == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrf.length, arrf[0].length, arrf[0][0].length, n);
        imageWare.putXYZ(0, 0, 0, arrf);
        return imageWare;
    }

    public static ImageWare create(double[][][] arrd, int n) {
        if (arrd == null) {
            Builder.throw_null();
        }
        ImageWare imageWare = Builder.createType(arrd.length, arrd[0].length, arrd[0][0].length, n);
        imageWare.putXYZ(0, 0, 0, arrd);
        return imageWare;
    }

    private static ImageWare createType(int n, int n2, int n3, int n4) {
        ImageWare imageWare = null;
        switch (n4) {
            case 1: {
                imageWare = new ByteSet(n, n2, n3);
                break;
            }
            case 2: {
                imageWare = new ShortSet(n, n2, n3);
                break;
            }
            case 3: {
                imageWare = new FloatSet(n, n2, n3);
                break;
            }
            case 4: {
                imageWare = new DoubleSet(n, n2, n3);
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to create this object.\n-------------------------------------------------------\n");
            }
        }
        return imageWare;
    }

    private static void throw_null() {
        throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to wrap the ImagePlus.\nThe object parameter is null.\n-------------------------------------------------------\n");
    }
}

