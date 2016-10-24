/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.gui.ImageCanvas
 *  ij.gui.ImageWindow
 *  ij.process.ColorProcessor
 *  ij.process.ImageProcessor
 */
package imageware;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import imageware.ImageWare;

public class Display {
    public static void show(String string, ImageWare imageWare) {
        new ImagePlus(string, imageWare.buildImageStack()).show();
    }

    public static void showColor(String string, ImageWare imageWare, ImageWare imageWare2, ImageWare imageWare3) {
        new ImagePlus(string, Display.buildColor(imageWare, imageWare2, imageWare3)).show();
    }

    public static void show(String string, ImageWare imageWare, double d) {
        ImagePlus imagePlus = new ImagePlus(string, imageWare.buildImageStack());
        imagePlus.show();
        ImageWindow imageWindow = imagePlus.getWindow();
        ImageCanvas imageCanvas = imageWindow.getCanvas();
        imageCanvas.setMagnification(d);
        imageCanvas.setDrawingSize((int)Math.ceil((double)imageWare.getWidth() * d), (int)Math.ceil((double)imageWare.getHeight() * d));
        imageWindow.pack();
        imagePlus.updateAndRepaintWindow();
    }

    public static void showColor(String string, ImageWare imageWare, ImageWare imageWare2, ImageWare imageWare3, double d) {
        ImagePlus imagePlus = new ImagePlus(string, Display.buildColor(imageWare, imageWare2, imageWare3));
        imagePlus.show();
        ImageWindow imageWindow = imagePlus.getWindow();
        ImageCanvas imageCanvas = imageWindow.getCanvas();
        imageCanvas.setMagnification(d);
        imageCanvas.setDrawingSize((int)Math.ceil((double)imageWare.getWidth() * d), (int)Math.ceil((double)imageWare.getHeight() * d));
        imageWindow.pack();
        imagePlus.updateAndRepaintWindow();
    }

    private static ImageStack buildColor(ImageWare imageWare, ImageWare imageWare2, ImageWare imageWare3) {
        if (!imageWare.isSameSize(imageWare2)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to create a ImageStack the channel are not the same size.\n[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "] != " + "[" + imageWare2.getSizeX() + "," + imageWare2.getSizeY() + "," + imageWare2.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        if (!imageWare.isSameSize(imageWare3)) {
            throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to create a ImageStack the channel are not the same size.\n[" + imageWare.getSizeX() + "," + imageWare.getSizeY() + "," + imageWare.getSizeZ() + "] != " + "[" + imageWare3.getSizeX() + "," + imageWare3.getSizeY() + "," + imageWare3.getSizeZ() + "].\n" + "-------------------------------------------------------\n");
        }
        int n = imageWare.getSizeX();
        int n2 = imageWare.getSizeY();
        int n3 = imageWare.getSizeZ();
        int n4 = n * n2;
        ImageStack imageStack = new ImageStack(n, n2);
        byte[] arrby = new byte[n4];
        byte[] arrby2 = new byte[n4];
        byte[] arrby3 = new byte[n4];
        for (int i = 0; i < n3; ++i) {
            int n5;
            double[] arrd;
            int n6;
            int n7;
            ColorProcessor colorProcessor = new ColorProcessor(n, n2);
            switch (imageWare.getType()) {
                case 4: {
                    arrd = imageWare.getSliceDouble(i);
                    for (n7 = 0; n7 < n4; ++n7) {
                        arrby[n7] = (byte)arrd[n7];
                    }
                    break;
                }
                case 3: {
                    float[] arrf = imageWare.getSliceFloat(i);
                    for (n5 = 0; n5 < n4; ++n5) {
                        arrby[n5] = (byte)arrf[n5];
                    }
                    break;
                }
                case 2: {
                    short[] arrs = imageWare.getSliceShort(i);
                    for (n6 = 0; n6 < n4; ++n6) {
                        arrby[n6] = (byte)arrs[n6];
                    }
                    break;
                }
                case 1: {
                    arrby = imageWare.getSliceByte(i);
                }
            }
            switch (imageWare2.getType()) {
                case 4: {
                    arrd = imageWare2.getSliceDouble(i);
                    for (n7 = 0; n7 < n4; ++n7) {
                        arrby2[n7] = (byte)arrd[n7];
                    }
                    break;
                }
                case 3: {
                    float[] arrf = imageWare2.getSliceFloat(i);
                    for (n5 = 0; n5 < n4; ++n5) {
                        arrby2[n5] = (byte)arrf[n5];
                    }
                    break;
                }
                case 2: {
                    short[] arrs = imageWare2.getSliceShort(i);
                    for (n6 = 0; n6 < n4; ++n6) {
                        arrby2[n6] = (byte)arrs[n6];
                    }
                    break;
                }
                case 1: {
                    arrby2 = imageWare2.getSliceByte(i);
                }
            }
            switch (imageWare3.getType()) {
                case 4: {
                    arrd = imageWare3.getSliceDouble(i);
                    for (n7 = 0; n7 < n4; ++n7) {
                        arrby3[n7] = (byte)arrd[n7];
                    }
                    break;
                }
                case 3: {
                    float[] arrf = imageWare3.getSliceFloat(i);
                    for (n5 = 0; n5 < n4; ++n5) {
                        arrby3[n5] = (byte)arrf[n5];
                    }
                    break;
                }
                case 2: {
                    short[] arrs = imageWare3.getSliceShort(i);
                    for (n6 = 0; n6 < n4; ++n6) {
                        arrby3[n6] = (byte)arrs[n6];
                    }
                    break;
                }
                case 1: {
                    arrby3 = imageWare3.getSliceByte(i);
                }
            }
            colorProcessor.setRGB(arrby, arrby2, arrby3);
            imageStack.addSlice("" + i, (ImageProcessor)colorProcessor);
        }
        return imageStack;
    }
}

