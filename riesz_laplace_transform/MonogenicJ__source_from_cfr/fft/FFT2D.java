/*
 * Decompiled with CFR 0_118.
 */
package fft;

import fft.ComplexSignal;
import fft.FFT1D;

public class FFT2D {
    public static ComplexSignal transform(ComplexSignal in) {
        int nx = in.nx;
        int ny = in.ny;
        ComplexSignal fourier = new ComplexSignal(nx, ny);
        double[] colReal = new double[ny];
        double[] colImag = new double[ny];
        FFT1D ffty = new FFT1D(ny);
        for (int x = 0; x < nx; ++x) {
            FFT2D.getY(x, in.real, colReal);
            if (in.imag == null) {
                for (int i = 0; i < ny; ++i) {
                    colImag[i] = 0.0;
                }
            } else {
                FFT2D.getY(x, in.imag, colImag);
            }
            ffty.transform(colReal, colImag, ny, 0);
            FFT2D.putY(x, fourier.real, colReal);
            FFT2D.putY(x, fourier.imag, colImag);
        }
        double[] rowReal = new double[nx];
        double[] rowImag = new double[nx];
        FFT1D fftx = new FFT1D(nx);
        for (int y = 0; y < ny; ++y) {
            FFT2D.getX(y, fourier.real, rowReal);
            FFT2D.getX(y, fourier.imag, rowImag);
            fftx.transform(rowReal, rowImag, nx, 0);
            FFT2D.putX(y, fourier.real, rowReal);
            FFT2D.putX(y, fourier.imag, rowImag);
        }
        return fourier;
    }

    public static ComplexSignal inverse(ComplexSignal fourier) {
        int nx = fourier.nx;
        int ny = fourier.ny;
        ComplexSignal out = new ComplexSignal(nx, ny);
        double[] colReal = new double[ny];
        double[] colImag = new double[ny];
        FFT1D ffty = new FFT1D(ny);
        for (int x = 0; x < nx; ++x) {
            FFT2D.getY(x, fourier.real, colReal);
            FFT2D.getY(x, fourier.imag, colImag);
            ffty.inverse(colReal, colImag, ny, 0);
            FFT2D.putY(x, out.real, colReal);
            FFT2D.putY(x, out.imag, colImag);
        }
        double[] rowReal = new double[nx];
        double[] rowImag = new double[nx];
        FFT1D fftx = new FFT1D(nx);
        for (int y = 0; y < ny; ++y) {
            FFT2D.getX(y, out.real, rowReal);
            FFT2D.getX(y, out.imag, rowImag);
            fftx.inverse(rowReal, rowImag, nx, 0);
            FFT2D.putX(y, out.real, rowReal);
            FFT2D.putX(y, out.imag, rowImag);
        }
        return out;
    }

    private static void getY(int x, double[] in, double[] col) {
        int ny = col.length;
        int nx = in.length / ny;
        int j = 0;
        int k = x;
        while (j < ny) {
            col[j] = in[k];
            ++j;
            k += nx;
        }
    }

    private static void getX(int y, double[] in, double[] row) {
        int nx = row.length;
        System.arraycopy(in, y * nx, row, 0, nx);
    }

    private static void putY(int x, double[] in, double[] col) {
        int ny = col.length;
        int nx = in.length / ny;
        int j = 0;
        int k = x;
        while (j < ny) {
            in[k] = col[j];
            ++j;
            k += nx;
        }
    }

    private static void putX(int y, double[] in, double[] row) {
        int nx = row.length;
        System.arraycopy(row, 0, in, y * nx, nx);
    }
}

