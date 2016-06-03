/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import monogenicwavelettoolbox.FloatArray2D;
import monogenicwavelettoolbox.FloatArray3D;
import monogenicwavelettoolbox.FloatArrayGeneric;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class FourierFilters {
    private static final int NO_DERIVATIVE = 0;
    private static final int DERIVATIVE_X = 1;
    private static final int DERIVATIVE_Y = 2;
    private static final int DERIVATIVE_Z = 3;

    public static List<FloatArrayGeneric> getRieszFilter(FloatArrayGeneric fArr, boolean ifftshift) {
        if (fArr.getMDepth() == 1) {
            return FourierFilters.getRieszFilter2D(fArr, ifftshift);
        }
        return FourierFilters.getRieszFilter3D(fArr, ifftshift);
    }

    private static List<FloatArrayGeneric> getRieszFilter2D(FloatArrayGeneric fArr, boolean ifftshift) {
        int width = fArr.getMWidth();
        int height = fArr.getMHeight();
        FloatArray2D rieszFilter1 = new FloatArray2D(height, width);
        FloatArray2D rieszFilter2 = new FloatArray2D(height, width);
        int hh = height / 2;
        int wh = width / 2;
        int i = 0;
        while (i < height) {
            int j = 0;
            while (j < width) {
                double w1 = (double)(i - hh) / (double)height;
                double w2 = (double)(j - wh) / (double)width;
                double w = Math.sqrt(w1 * w1 + w2 * w2);
                rieszFilter1.setImagValue(i, j, w1 / w);
                rieszFilter1.setRealValue(i, j, 0.0);
                rieszFilter2.setImagValue(i, j, w2 / w);
                rieszFilter2.setRealValue(i, j, 0.0);
                ++j;
            }
            ++i;
        }
        rieszFilter1.setImagValue(hh, wh, 0.0);
        rieszFilter2.setImagValue(hh, wh, 0.0);
        if (ifftshift) {
            rieszFilter1.ifftshift();
            rieszFilter2.ifftshift();
        }
        ArrayList<FloatArrayGeneric> list = new ArrayList<FloatArrayGeneric>();
        list.add(rieszFilter1);
        list.add(rieszFilter2);
        return list;
    }

    private static List<FloatArrayGeneric> getRieszFilter3D(FloatArrayGeneric fArr, boolean ifftshift) {
        int width = fArr.getMWidth();
        int height = fArr.getMHeight();
        int depth = fArr.getMDepth();
        FloatArray3D rieszFilter1 = new FloatArray3D(depth, height, width);
        FloatArray3D rieszFilter2 = new FloatArray3D(depth, height, width);
        FloatArray3D rieszFilter3 = new FloatArray3D(depth, height, width);
        int s1 = depth / 2;
        int s2 = height / 2;
        int s3 = width / 2;
        int i = 0;
        while (i < depth) {
            int j = 0;
            while (j < height) {
                int k = 0;
                while (k < width) {
                    double w1 = (double)(i - s1) / (double)(s1 * 2);
                    double w2 = (double)(j - s2) / (double)(s2 * 2);
                    double w3 = (double)(k - s3) / (double)(s3 * 2);
                    double w = Math.sqrt(w1 * w1 + w2 * w2 + w3 * w3);
                    rieszFilter1.setImagValue(i, j, k, w1 / w);
                    rieszFilter1.setRealValue(i, j, k, 0.0);
                    rieszFilter2.setImagValue(i, j, k, w2 / w);
                    rieszFilter2.setRealValue(i, j, k, 0.0);
                    rieszFilter3.setImagValue(i, j, k, w3 / w);
                    rieszFilter3.setRealValue(i, j, k, 0.0);
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        rieszFilter1.setImagValue(s1, s2, s3, 0.0);
        rieszFilter2.setImagValue(s1, s2, s3, 0.0);
        rieszFilter3.setImagValue(s1, s2, s3, 0.0);
        if (ifftshift) {
            rieszFilter1.ifftshift();
            rieszFilter2.ifftshift();
            rieszFilter3.ifftshift();
        }
        ArrayList<FloatArrayGeneric> list = new ArrayList<FloatArrayGeneric>();
        list.add(rieszFilter1);
        list.add(rieszFilter2);
        list.add(rieszFilter3);
        return list;
    }

    public static List<FloatArrayGeneric> getWaveletFilter(FloatArrayGeneric fArr, boolean ifftshift, int numberOfChannels, int order) {
        if (fArr.getMDepth() == 1) {
            return FourierFilters.getWaveletFilter2D(fArr, ifftshift, numberOfChannels, order, 0);
        }
        return FourierFilters.getWaveletFilter3D(fArr, ifftshift, numberOfChannels, order, 0);
    }

    public static List<List<FloatArrayGeneric>> getWaveletFilterDerivative(FloatArrayGeneric fArr, boolean ifftshift, int numberOfChannels, int order) {
        ArrayList<List<FloatArrayGeneric>> waveletFilterDerivative = new ArrayList<List<FloatArrayGeneric>>();
        if (fArr.getMDepth() == 1) {
            int i = 1;
            while (i < 3) {
                waveletFilterDerivative.add(FourierFilters.getWaveletFilter2D(fArr, ifftshift, numberOfChannels, order, i));
                ++i;
            }
        } else {
            int i = 1;
            while (i < 4) {
                waveletFilterDerivative.add(FourierFilters.getWaveletFilter3D(fArr, ifftshift, numberOfChannels, order, i));
                ++i;
            }
        }
        return waveletFilterDerivative;
    }

    private static List<FloatArrayGeneric> getWaveletFilter2D(FloatArrayGeneric fArr, boolean ifftshift, int numberOfChannels, int order, int derivative) {
        int width = fArr.getMWidth();
        int height = fArr.getMHeight();
        LinkedList<FloatArrayGeneric> waveletFilterList = new LinkedList<FloatArrayGeneric>();
        double hh = height / 2;
        double wh = width / 2;
        int l = 1;
        while (l < numberOfChannels + 2) {
            FloatArray2D waveletFilter = new FloatArray2D(height, width);
            int i = 0;
            while (i < height) {
                int j = 0;
                while (j < width) {
                    double w1 = ((double)i - hh) / (double)height;
                    double w2 = ((double)j - wh) / (double)width;
                    double rad = Math.sqrt(w1 * w1 + w2 * w2);
                    double grid = Math.pow(rad, numberOfChannels);
                    double val = FourierFilters.isotropicWavelet(grid * Math.pow(2.0, (double)l + (double)numberOfChannels - 2.0), order);
                    if (l == 1) {
                        if (0.5 * Math.pow(2.0, -1.0f / (float)numberOfChannels) < rad) {
                            val = 1.0;
                        }
                    } else if (l == numberOfChannels + 1 && 0.25 * Math.pow(2.0, -1.0f / (float)numberOfChannels) > rad) {
                        val = 1.0;
                    }
                    if (derivative == 1) {
                        val *= -6.283185307179586 * w1;
                    } else if (derivative == 2) {
                        val *= -6.283185307179586 * w2;
                    }
                    if (derivative == 0) {
                        waveletFilter.setRealValue(i, j, val);
                        waveletFilter.setImagValue(i, j, 0.0);
                    } else {
                        waveletFilter.setImagValue(i, j, val);
                        waveletFilter.setRealValue(i, j, 0.0);
                    }
                    ++j;
                }
                ++i;
            }
            if (ifftshift) {
                waveletFilter.ifftshift();
            }
            waveletFilterList.add(waveletFilter);
            ++l;
        }
        return waveletFilterList;
    }

    private static List<FloatArrayGeneric> getWaveletFilter3D(FloatArrayGeneric fArr, boolean ifftshift, int numberOfChannels, int order, int derivative) {
        int width = fArr.getMWidth();
        int height = fArr.getMHeight();
        int depth = fArr.getMDepth();
        LinkedList<FloatArrayGeneric> waveletFilterList = new LinkedList<FloatArrayGeneric>();
        double hh = height / 2;
        double wh = width / 2;
        double dh = depth / 2;
        int l = 1;
        while (l < numberOfChannels + 2) {
            FloatArray3D waveletFilter = new FloatArray3D(depth, height, width);
            int i = 0;
            while (i < depth) {
                int j = 0;
                while (j < height) {
                    int k = 0;
                    while (k < width) {
                        double w1 = ((double)i - dh) / (double)depth;
                        double w2 = ((double)j - hh) / (double)height;
                        double w3 = ((double)k - wh) / (double)width;
                        double rad = Math.sqrt(w1 * w1 + w2 * w2 + w3 * w3);
                        double grid = Math.pow(rad, numberOfChannels);
                        double val = FourierFilters.isotropicWavelet(grid * Math.pow(2.0, (double)l + (double)numberOfChannels - 2.0), order);
                        if (l == 1) {
                            if (0.5 * Math.pow(2.0, -1.0f / (float)numberOfChannels) < rad) {
                                val = 1.0;
                            }
                        } else if (l == numberOfChannels + 1 && 0.25 * Math.pow(2.0, -1.0f / (float)numberOfChannels) > rad) {
                            val = 1.0;
                        }
                        if (derivative == 1) {
                            val *= -6.283185307179586 * w1;
                        } else if (derivative == 2) {
                            val *= -6.283185307179586 * w2;
                        } else if (derivative == 3) {
                            val *= -6.283185307179586 * w3;
                        }
                        waveletFilter.setRealValue(i, j, k, val);
                        waveletFilter.setImagValue(i, j, k, 0.0);
                        ++k;
                    }
                    ++j;
                }
                ++i;
            }
            if (ifftshift) {
                waveletFilter.ifftshift();
            }
            waveletFilterList.add(waveletFilter);
            ++l;
        }
        return waveletFilterList;
    }

    public static double isotropicWavelet(double x, int order) {
        if ((x *= 8.0) > 1.0 && x < 2.0) {
            return Math.cos(6.283185307179586 * FourierFilters.polynom(x, order));
        }
        if (x >= 2.0 && x < 4.0) {
            return Math.sin(6.283185307179586 * FourierFilters.polynom(x / 2.0, order));
        }
        return 0.0;
    }

    public static double polynom(double x, int order) {
        double y = 0.0;
        switch (order) {
            case 0: {
                y = 0.5 - 0.25 * x;
                break;
            }
            case 1: {
                y = 0.5 * Math.pow(x, 3.0) - 2.25 * Math.pow(x, 2.0) + 3.0 * x - 1.0;
                break;
            }
            case 2: {
                y = 8.0 - 30.0 * x + 45.0 * Math.pow(x, 2.0) - 32.5 * Math.pow(x, 3.0) + 11.25 * Math.pow(x, 4.0) - 1.5 * Math.pow(x, 5.0);
                break;
            }
            case 3: {
                y = -52.0 + 280.0 * x - 630.0 * Math.pow(x, 2.0) + 770.0 * Math.pow(x, 3.0) - 551.25 * Math.pow(x, 4.0) + 231.0 * Math.pow(x, 5.0) - 52.5 * Math.pow(x, 6.0) + 5.0 * Math.pow(x, 7.0);
                break;
            }
            case 4: {
                y = 368.0 - 2520.0 * x + 7560.0 * Math.pow(x, 2.0) - 13020.0 * Math.pow(x, 3.0) + 14175.0 * Math.pow(x, 4.0) - 10111.5 * Math.pow(x, 5.0) + 4725.0 * Math.pow(x, 6.0) - 1395.0 * Math.pow(x, 7.0) + 236.25 * Math.pow(x, 8.0) - 17.5 * Math.pow(x, 9.0);
                break;
            }
            case 5: {
                y = -2656.0 + 22176.0 * x - 83160.0 * Math.pow(x, 2.0) + 184800.0 * Math.pow(x, 3.0) - 270270.0 * Math.pow(x, 4.0) + 273042.0 * Math.pow(x, 5.0) - 194386.5 * Math.pow(x, 6.0) + 97515.0 * Math.pow(x, 7.0) - 33783.75 * Math.pow(x, 8.0) + 7700.0 * Math.pow(x, 9.0) - 1039.5 * Math.pow(x, 10.0) + 63.0 * Math.pow(x, 11.0);
                break;
            }
            default: {
                throw new RuntimeException("Choose Order smaller than 6");
            }
        }
        return y;
    }
}

