/*
 * Decompiled with CFR 0_118.
 */
package fft;

import java.io.PrintStream;

public class FFT1D {
    private boolean radix2 = true;
    private double[] Rearg;
    private double[] Imarg;
    private double[] yReOut;
    private double[] yImOut;
    private int maxPrimeFactor;
    private int maxPrimeFactorDiv2;
    private int maxFactorCount = 20;
    private int n;
    private int nFactor;
    private final double c3_1 = -1.5;
    private final double c3_2 = 0.86602540378444;
    private final double u5 = 1.2566370614359;
    private final double c5_1 = -1.25;
    private final double c5_2 = 0.55901699437495;
    private final double c5_3 = -0.95105651629515;
    private final double c5_4 = -1.5388417685876;
    private final double c5_5 = 0.36327126400268;
    private final double c8 = 0.70710678118655;
    private int groupOffset;
    private int dataOffset;
    private int blockOffset;
    private int adr;
    private int groupNo;
    private int dataNo;
    private int blockNo;
    private int twNo;
    private double omega;
    private double tw_re;
    private double tw_im;
    private double[] twiddleRe;
    private double[] twiddleIm;
    private double[] trigRe;
    private double[] trigIm;
    private double[] zRe;
    private double[] zIm;
    private double[] vRe;
    private double[] vIm;
    private double[] wRe;
    private double[] wIm;
    private int[] sofarRadix;
    private int[] actualRadix;
    private int[] remainRadix;

    public FFT1D(int size) {
        int m = 1;
        int size1 = size;
        while (size1 > 2) {
            size1 /= 2;
            ++m;
        }
        if ((int)Math.round(Math.pow(2.0, m)) == size) {
            this.radix2 = true;
            this.n = 1 << m;
            double fact = 6.283185307179586 / (double)this.n;
            this.Imarg = new double[this.n];
            this.Rearg = new double[this.n];
            for (int i = 0; i < this.n; ++i) {
                double arg = fact * (double)i;
                this.Rearg[i] = Math.cos(arg);
                this.Imarg[i] = - Math.sin(arg);
            }
        } else {
            this.radix2 = false;
            this.maxPrimeFactor = size + 1;
            this.maxPrimeFactorDiv2 = (this.maxPrimeFactor + 1) / 2;
            this.twiddleRe = new double[this.maxPrimeFactor];
            this.twiddleIm = new double[this.maxPrimeFactor];
            this.trigRe = new double[this.maxPrimeFactor];
            this.trigIm = new double[this.maxPrimeFactor];
            this.zRe = new double[this.maxPrimeFactor];
            this.zIm = new double[this.maxPrimeFactor];
            this.vRe = new double[this.maxPrimeFactorDiv2];
            this.vIm = new double[this.maxPrimeFactorDiv2];
            this.wRe = new double[this.maxPrimeFactorDiv2];
            this.wIm = new double[this.maxPrimeFactorDiv2];
            this.yReOut = new double[size];
            this.yImOut = new double[size];
            this.n = size;
            this.sofarRadix = new int[this.maxFactorCount];
            this.actualRadix = new int[this.maxFactorCount];
            this.remainRadix = new int[this.maxFactorCount];
            this.transTableSetup(this.sofarRadix, this.actualRadix, this.remainRadix);
        }
    }

    public void transform(double[] Re, double[] Im, int size, int shift) {
        this.n = size;
        if (this.radix2) {
            this.doFFT1D_CooleyTukey(Re, Im, size, shift);
        } else if (shift == 0) {
            this.doFFT_Mix(Re, Im, size);
        } else {
            this.doFFT_Mix(Re, Im, size, shift);
        }
    }

    public void inverse(double[] Re, double[] Im, int size, int shift) {
        this.n = size;
        if (this.radix2) {
            this.doIFFT1D_CooleyTukey(Re, Im, size, shift);
        } else if (shift == 0) {
            this.doIFFT_Mix(Re, Im, size);
        } else {
            this.doIFFT_Mix(Re, Im, size, shift);
        }
    }

    private void doFFT1D_CooleyTukey(double[] Re, double[] Im, int size, int shift) {
        int i;
        double Retmp;
        int j;
        double Imtmp;
        int m = 1;
        int size1 = size;
        while (size1 > 2) {
            size1 /= 2;
            ++m;
        }
        for (i = j = shift; i < shift + this.n - 1; ++i) {
            if (i < j) {
                Retmp = Re[i];
                Imtmp = Im[i];
                Re[i] = Re[j];
                Im[i] = Im[j];
                Re[j] = Retmp;
                Im[j] = Imtmp;
            }
            int k = this.n >> 1;
            while (k + shift <= j) {
                j -= k;
                k /= 2;
            }
            j += k;
        }
        int stepsize = 1;
        int shifter = m - 1;
        while (stepsize < this.n) {
            for (j = shift; j < shift + this.n; j += stepsize << 1) {
                for (i = 0; i < stepsize; ++i) {
                    int i_j = i + j;
                    int i_j_s = i_j + stepsize;
                    if (i > 0) {
                        Retmp = this.Rearg[i << shifter] * Re[i_j_s] - this.Imarg[i << shifter] * Im[i_j_s];
                        Im[i_j_s] = this.Rearg[i << shifter] * Im[i_j_s] + this.Imarg[i << shifter] * Re[i_j_s];
                        Re[i_j_s] = Retmp;
                    }
                    Retmp = Re[i_j] - Re[i_j_s];
                    Imtmp = Im[i_j] - Im[i_j_s];
                    double[] arrd = Re;
                    int n = i_j;
                    arrd[n] = arrd[n] + Re[i_j_s];
                    double[] arrd2 = Im;
                    int n2 = i_j;
                    arrd2[n2] = arrd2[n2] + Im[i_j_s];
                    Re[i_j_s] = Retmp;
                    Im[i_j_s] = Imtmp;
                }
            }
            stepsize <<= 1;
            --shifter;
        }
    }

    private void doIFFT1D_CooleyTukey(double[] Re, double[] Im, int size, int shift) {
        int i;
        for (i = shift; i < shift + size; ++i) {
            Im[i] = - Im[i];
        }
        this.transform(Re, Im, size, shift);
        for (i = shift; i < shift + size; ++i) {
            Re[i] = Re[i] / (double)size;
            Im[i] = (- Im[i]) / (double)size;
        }
    }

    private void factorize(int[] fact, int num) {
        int j;
        int[] radices = new int[7];
        int[] factors = new int[this.maxFactorCount];
        int nRadix = 6;
        radices[1] = 2;
        radices[2] = 3;
        radices[3] = 4;
        radices[4] = 5;
        radices[5] = 8;
        radices[6] = 10;
        if (num == 1) {
            j = 1;
            factors[1] = 1;
        } else {
            j = 0;
        }
        int i = nRadix;
        while (num > 1 && i > 0) {
            if (num % radices[i] == 0) {
                num /= radices[i];
                factors[++j] = radices[i];
                continue;
            }
            --i;
        }
        if (factors[j] == 2) {
            for (i = j - 1; i > 0 && factors[i] != 8; --i) {
            }
            if (i > 0) {
                factors[j] = 4;
                factors[i] = 4;
            }
        }
        if (num > 1) {
            int k = 2;
            while ((double)k < Math.sqrt(num) + 1.0) {
                while (num % k == 0) {
                    num /= k;
                    factors[++j] = k;
                }
                ++k;
            }
            if (num > 1) {
                factors[++j] = num;
            }
        }
        for (i = 1; i <= j; ++i) {
            fact[i] = factors[j - i + 1];
        }
        this.nFactor = j;
    }

    private void transTableSetup(int[] sofar, int[] actual, int[] remain) {
        this.factorize(actual, this.n);
        if (actual[1] > this.maxPrimeFactor) {
            System.out.println("\nPrime factor of FFT length too large : %6d" + actual[1]);
            System.out.println("\nPlease modify the value of maxPrimeFactor in mixfft.c");
        }
        remain[0] = this.n;
        sofar[1] = 1;
        remain[1] = this.n / actual[1];
        for (int i = 2; i <= this.nFactor; ++i) {
            sofar[i] = sofar[i - 1] * actual[i - 1];
            remain[i] = remain[i - 1] / actual[i];
        }
    }

    private void permute(int[] fact, int[] remain, double[] xRe, double[] xIm, double[] yRe, double[] yIm) {
        int i;
        int j = 0;
        int[] count = new int[this.maxFactorCount];
        for (i = 1; i <= this.nFactor; ++i) {
            count[i] = 0;
        }
        int k = 0;
        for (i = 0; i <= this.n - 2; ++i) {
            yRe[i] = xRe[k];
            yIm[i] = xIm[k];
            j = 1;
            k += remain[j];
            count[1] = count[1] + 1;
            while (count[j] >= fact[j]) {
                count[j] = 0;
                k = k - remain[j - 1] + remain[j + 1];
                count[++j] = count[j] + 1;
            }
        }
        yRe[this.n - 1] = xRe[this.n - 1];
        yIm[this.n - 1] = xIm[this.n - 1];
    }

    private final void initTrig(int radix) {
        double w = 6.283185307179586 / (double)radix;
        this.trigRe[0] = 1.0;
        this.trigIm[0] = 0.0;
        double xre = Math.cos(w);
        double xim = - Math.sin(w);
        this.trigRe[1] = xre;
        this.trigIm[1] = xim;
        for (int i = 2; i < radix; ++i) {
            this.trigRe[i] = xre * this.trigRe[i - 1] - xim * this.trigIm[i - 1];
            this.trigIm[i] = xim * this.trigRe[i - 1] + xre * this.trigIm[i - 1];
        }
    }

    private void fft_4(double[] aRe, double[] aIm) {
        double t1_re = aRe[0] + aRe[2];
        double t1_im = aIm[0] + aIm[2];
        double t2_re = aRe[1] + aRe[3];
        double t2_im = aIm[1] + aIm[3];
        double m2_re = aRe[0] - aRe[2];
        double m2_im = aIm[0] - aIm[2];
        double m3_re = aIm[1] - aIm[3];
        double m3_im = aRe[3] - aRe[1];
        aRe[0] = t1_re + t2_re;
        aIm[0] = t1_im + t2_im;
        aRe[2] = t1_re - t2_re;
        aIm[2] = t1_im - t2_im;
        aRe[1] = m2_re + m3_re;
        aIm[1] = m2_im + m3_im;
        aRe[3] = m2_re - m3_re;
        aIm[3] = m2_im - m3_im;
    }

    private void fft_5(double[] aRe, double[] aIm) {
        double t1_re = aRe[1] + aRe[4];
        double t1_im = aIm[1] + aIm[4];
        double t2_re = aRe[2] + aRe[3];
        double t2_im = aIm[2] + aIm[3];
        double t3_re = aRe[1] - aRe[4];
        double t3_im = aIm[1] - aIm[4];
        double t4_re = aRe[3] - aRe[2];
        double t4_im = aIm[3] - aIm[2];
        double t5_re = t1_re + t2_re;
        double t5_im = t1_im + t2_im;
        aRe[0] = aRe[0] + t5_re;
        aIm[0] = aIm[0] + t5_im;
        double m1_re = -1.25 * t5_re;
        double m1_im = -1.25 * t5_im;
        double m2_re = 0.55901699437495 * (t1_re - t2_re);
        double m2_im = 0.55901699437495 * (t1_im - t2_im);
        double m3_re = 0.95105651629515 * (t3_im + t4_im);
        double m3_im = -0.95105651629515 * (t3_re + t4_re);
        double m4_re = 1.5388417685876 * t4_im;
        double m4_im = -1.5388417685876 * t4_re;
        double m5_re = -0.36327126400268 * t3_im;
        double m5_im = 0.36327126400268 * t3_re;
        double s3_re = m3_re - m4_re;
        double s3_im = m3_im - m4_im;
        double s5_re = m3_re + m5_re;
        double s5_im = m3_im + m5_im;
        double s1_re = aRe[0] + m1_re;
        double s1_im = aIm[0] + m1_im;
        double s2_re = s1_re + m2_re;
        double s2_im = s1_im + m2_im;
        double s4_re = s1_re - m2_re;
        double s4_im = s1_im - m2_im;
        aRe[1] = s2_re + s3_re;
        aIm[1] = s2_im + s3_im;
        aRe[2] = s4_re + s5_re;
        aIm[2] = s4_im + s5_im;
        aRe[3] = s4_re - s5_re;
        aIm[3] = s4_im - s5_im;
        aRe[4] = s2_re - s3_re;
        aIm[4] = s2_im - s3_im;
    }

    private void fft_8() {
        double[] aRe = new double[4];
        double[] aIm = new double[4];
        double[] bRe = new double[4];
        double[] bIm = new double[4];
        aRe[0] = this.zRe[0];
        bRe[0] = this.zRe[1];
        aRe[1] = this.zRe[2];
        bRe[1] = this.zRe[3];
        aRe[2] = this.zRe[4];
        bRe[2] = this.zRe[5];
        aRe[3] = this.zRe[6];
        bRe[3] = this.zRe[7];
        aIm[0] = this.zIm[0];
        bIm[0] = this.zIm[1];
        aIm[1] = this.zIm[2];
        bIm[1] = this.zIm[3];
        aIm[2] = this.zIm[4];
        bIm[2] = this.zIm[5];
        aIm[3] = this.zIm[6];
        bIm[3] = this.zIm[7];
        this.fft_4(aRe, aIm);
        this.fft_4(bRe, bIm);
        double gem = 0.70710678118655 * (bRe[1] + bIm[1]);
        bIm[1] = 0.70710678118655 * (bIm[1] - bRe[1]);
        bRe[1] = gem;
        gem = bIm[2];
        bIm[2] = - bRe[2];
        bRe[2] = gem;
        gem = 0.70710678118655 * (bIm[3] - bRe[3]);
        bIm[3] = -0.70710678118655 * (bRe[3] + bIm[3]);
        bRe[3] = gem;
        this.zRe[0] = aRe[0] + bRe[0];
        this.zRe[4] = aRe[0] - bRe[0];
        this.zRe[1] = aRe[1] + bRe[1];
        this.zRe[5] = aRe[1] - bRe[1];
        this.zRe[2] = aRe[2] + bRe[2];
        this.zRe[6] = aRe[2] - bRe[2];
        this.zRe[3] = aRe[3] + bRe[3];
        this.zRe[7] = aRe[3] - bRe[3];
        this.zIm[0] = aIm[0] + bIm[0];
        this.zIm[4] = aIm[0] - bIm[0];
        this.zIm[1] = aIm[1] + bIm[1];
        this.zIm[5] = aIm[1] - bIm[1];
        this.zIm[2] = aIm[2] + bIm[2];
        this.zIm[6] = aIm[2] - bIm[2];
        this.zIm[3] = aIm[3] + bIm[3];
        this.zIm[7] = aIm[3] - bIm[3];
    }

    private void fft_10() {
        double[] aRe = new double[5];
        double[] aIm = new double[5];
        double[] bRe = new double[5];
        double[] bIm = new double[5];
        aRe[0] = this.zRe[0];
        bRe[0] = this.zRe[5];
        aRe[1] = this.zRe[2];
        bRe[1] = this.zRe[7];
        aRe[2] = this.zRe[4];
        bRe[2] = this.zRe[9];
        aRe[3] = this.zRe[6];
        bRe[3] = this.zRe[1];
        aRe[4] = this.zRe[8];
        bRe[4] = this.zRe[3];
        aIm[0] = this.zIm[0];
        bIm[0] = this.zIm[5];
        aIm[1] = this.zIm[2];
        bIm[1] = this.zIm[7];
        aIm[2] = this.zIm[4];
        bIm[2] = this.zIm[9];
        aIm[3] = this.zIm[6];
        bIm[3] = this.zIm[1];
        aIm[4] = this.zIm[8];
        bIm[4] = this.zIm[3];
        this.fft_5(aRe, aIm);
        this.fft_5(bRe, bIm);
        this.zRe[0] = aRe[0] + bRe[0];
        this.zRe[5] = aRe[0] - bRe[0];
        this.zRe[6] = aRe[1] + bRe[1];
        this.zRe[1] = aRe[1] - bRe[1];
        this.zRe[2] = aRe[2] + bRe[2];
        this.zRe[7] = aRe[2] - bRe[2];
        this.zRe[8] = aRe[3] + bRe[3];
        this.zRe[3] = aRe[3] - bRe[3];
        this.zRe[4] = aRe[4] + bRe[4];
        this.zRe[9] = aRe[4] - bRe[4];
        this.zIm[0] = aIm[0] + bIm[0];
        this.zIm[5] = aIm[0] - bIm[0];
        this.zIm[6] = aIm[1] + bIm[1];
        this.zIm[1] = aIm[1] - bIm[1];
        this.zIm[2] = aIm[2] + bIm[2];
        this.zIm[7] = aIm[2] - bIm[2];
        this.zIm[8] = aIm[3] + bIm[3];
        this.zIm[3] = aIm[3] - bIm[3];
        this.zIm[4] = aIm[4] + bIm[4];
        this.zIm[9] = aIm[4] - bIm[4];
    }

    private void fft_odd(int radix) {
        int j;
        int p = radix;
        int max = (p + 1) / 2;
        for (j = 1; j < max; ++j) {
            this.vRe[j] = this.zRe[j] + this.zRe[p - j];
            this.vIm[j] = this.zIm[j] - this.zIm[p - j];
            this.wRe[j] = this.zRe[j] - this.zRe[p - j];
            this.wIm[j] = this.zIm[j] + this.zIm[p - j];
        }
        for (j = 1; j < max; ++j) {
            this.zRe[j] = this.zRe[0];
            this.zIm[j] = this.zIm[0];
            this.zRe[p - j] = this.zRe[0];
            this.zIm[p - j] = this.zIm[0];
            int k = j;
            for (int i = 1; i < max; ++i) {
                double rere = this.trigRe[k] * this.vRe[i];
                double imim = this.trigIm[k] * this.vIm[i];
                double reim = this.trigRe[k] * this.wIm[i];
                double imre = this.trigIm[k] * this.wRe[i];
                double[] arrd = this.zRe;
                int n = p - j;
                arrd[n] = arrd[n] + (rere + imim);
                double[] arrd2 = this.zIm;
                int n2 = p - j;
                arrd2[n2] = arrd2[n2] + (reim - imre);
                double[] arrd3 = this.zRe;
                int n3 = j;
                arrd3[n3] = arrd3[n3] + (rere - imim);
                double[] arrd4 = this.zIm;
                int n4 = j;
                arrd4[n4] = arrd4[n4] + (reim + imre);
                if ((k += j) < p) continue;
                k -= p;
            }
        }
        for (j = 1; j < max; ++j) {
            this.zRe[0] = this.zRe[0] + this.vRe[j];
            this.zIm[0] = this.zIm[0] + this.wIm[j];
        }
    }

    private void twiddleTransf(int sofarRadix, int radix, int remainRadix, double[] yRe, double[] yIm) {
        this.initTrig(radix);
        this.omega = 6.283185307179586 / (double)(sofarRadix * radix);
        double cosw = Math.cos(this.omega);
        double sinw = - Math.sin(this.omega);
        this.tw_re = 1.0;
        this.tw_im = 0.0;
        this.adr = this.groupOffset = (this.dataOffset = 0);
        this.dataNo = 0;
        while (this.dataNo < sofarRadix) {
            double gem;
            if (sofarRadix > 1) {
                this.twiddleRe[0] = 1.0;
                this.twiddleIm[0] = 0.0;
                this.twiddleRe[1] = this.tw_re;
                this.twiddleIm[1] = this.tw_im;
                this.twNo = 2;
                while (this.twNo < radix) {
                    this.twiddleRe[this.twNo] = this.tw_re * this.twiddleRe[this.twNo - 1] - this.tw_im * this.twiddleIm[this.twNo - 1];
                    this.twiddleIm[this.twNo] = this.tw_im * this.twiddleRe[this.twNo - 1] + this.tw_re * this.twiddleIm[this.twNo - 1];
                    ++this.twNo;
                }
                gem = cosw * this.tw_re - sinw * this.tw_im;
                this.tw_im = sinw * this.tw_re + cosw * this.tw_im;
                this.tw_re = gem;
            }
            this.groupNo = 0;
            while (this.groupNo < remainRadix) {
                if (sofarRadix > 1 && this.dataNo > 0) {
                    this.zRe[0] = yRe[this.adr];
                    this.zIm[0] = yIm[this.adr];
                    this.blockNo = 1;
                    do {
                        this.adr += sofarRadix;
                        this.zRe[this.blockNo] = this.twiddleRe[this.blockNo] * yRe[this.adr] - this.twiddleIm[this.blockNo] * yIm[this.adr];
                        this.zIm[this.blockNo] = this.twiddleRe[this.blockNo] * yIm[this.adr] + this.twiddleIm[this.blockNo] * yRe[this.adr];
                        ++this.blockNo;
                    } while (this.blockNo < radix);
                } else {
                    this.blockNo = 0;
                    while (this.blockNo < radix) {
                        this.zRe[this.blockNo] = yRe[this.adr];
                        this.zIm[this.blockNo] = yIm[this.adr];
                        this.adr += sofarRadix;
                        ++this.blockNo;
                    }
                }
                switch (radix) {
                    double m2_im;
                    double m3_im;
                    double m1_re;
                    double m3_re;
                    double t1_im;
                    double t2_re;
                    double s1_re;
                    double m1_im;
                    double t2_im;
                    double s1_im;
                    double t1_re;
                    double m2_re;
                    case 2: {
                        gem = this.zRe[0] + this.zRe[1];
                        this.zRe[1] = this.zRe[0] - this.zRe[1];
                        this.zRe[0] = gem;
                        gem = this.zIm[0] + this.zIm[1];
                        this.zIm[1] = this.zIm[0] - this.zIm[1];
                        this.zIm[0] = gem;
                        break;
                    }
                    case 3: {
                        t1_re = this.zRe[1] + this.zRe[2];
                        t1_im = this.zIm[1] + this.zIm[2];
                        this.zRe[0] = this.zRe[0] + t1_re;
                        this.zIm[0] = this.zIm[0] + t1_im;
                        m1_re = -1.5 * t1_re;
                        m1_im = -1.5 * t1_im;
                        m2_re = 0.86602540378444 * (this.zIm[1] - this.zIm[2]);
                        m2_im = 0.86602540378444 * (this.zRe[2] - this.zRe[1]);
                        s1_re = this.zRe[0] + m1_re;
                        s1_im = this.zIm[0] + m1_im;
                        this.zRe[1] = s1_re + m2_re;
                        this.zIm[1] = s1_im + m2_im;
                        this.zRe[2] = s1_re - m2_re;
                        this.zIm[2] = s1_im - m2_im;
                        break;
                    }
                    case 4: {
                        t1_re = this.zRe[0] + this.zRe[2];
                        t1_im = this.zIm[0] + this.zIm[2];
                        t2_re = this.zRe[1] + this.zRe[3];
                        t2_im = this.zIm[1] + this.zIm[3];
                        m2_re = this.zRe[0] - this.zRe[2];
                        m2_im = this.zIm[0] - this.zIm[2];
                        m3_re = this.zIm[1] - this.zIm[3];
                        m3_im = this.zRe[3] - this.zRe[1];
                        this.zRe[0] = t1_re + t2_re;
                        this.zIm[0] = t1_im + t2_im;
                        this.zRe[2] = t1_re - t2_re;
                        this.zIm[2] = t1_im - t2_im;
                        this.zRe[1] = m2_re + m3_re;
                        this.zIm[1] = m2_im + m3_im;
                        this.zRe[3] = m2_re - m3_re;
                        this.zIm[3] = m2_im - m3_im;
                        break;
                    }
                    case 5: {
                        t1_re = this.zRe[1] + this.zRe[4];
                        t1_im = this.zIm[1] + this.zIm[4];
                        t2_re = this.zRe[2] + this.zRe[3];
                        t2_im = this.zIm[2] + this.zIm[3];
                        double t3_re = this.zRe[1] - this.zRe[4];
                        double t3_im = this.zIm[1] - this.zIm[4];
                        double t4_re = this.zRe[3] - this.zRe[2];
                        double t4_im = this.zIm[3] - this.zIm[2];
                        double t5_re = t1_re + t2_re;
                        double t5_im = t1_im + t2_im;
                        this.zRe[0] = this.zRe[0] + t5_re;
                        this.zIm[0] = this.zIm[0] + t5_im;
                        m1_re = -1.25 * t5_re;
                        m1_im = -1.25 * t5_im;
                        m2_re = 0.55901699437495 * (t1_re - t2_re);
                        m2_im = 0.55901699437495 * (t1_im - t2_im);
                        m3_re = 0.95105651629515 * (t3_im + t4_im);
                        m3_im = -0.95105651629515 * (t3_re + t4_re);
                        double m4_re = 1.5388417685876 * t4_im;
                        double m4_im = -1.5388417685876 * t4_re;
                        double m5_re = -0.36327126400268 * t3_im;
                        double m5_im = 0.36327126400268 * t3_re;
                        double s3_re = m3_re - m4_re;
                        double s3_im = m3_im - m4_im;
                        double s5_re = m3_re + m5_re;
                        double s5_im = m3_im + m5_im;
                        s1_re = this.zRe[0] + m1_re;
                        s1_im = this.zIm[0] + m1_im;
                        double s2_re = s1_re + m2_re;
                        double s2_im = s1_im + m2_im;
                        double s4_re = s1_re - m2_re;
                        double s4_im = s1_im - m2_im;
                        this.zRe[1] = s2_re + s3_re;
                        this.zIm[1] = s2_im + s3_im;
                        this.zRe[2] = s4_re + s5_re;
                        this.zIm[2] = s4_im + s5_im;
                        this.zRe[3] = s4_re - s5_re;
                        this.zIm[3] = s4_im - s5_im;
                        this.zRe[4] = s2_re - s3_re;
                        this.zIm[4] = s2_im - s3_im;
                        break;
                    }
                    case 8: {
                        this.fft_8();
                        break;
                    }
                    case 10: {
                        this.fft_10();
                        break;
                    }
                    default: {
                        this.fft_odd(radix);
                    }
                }
                this.adr = this.groupOffset;
                this.blockNo = 0;
                while (this.blockNo < radix) {
                    yRe[this.adr] = this.zRe[this.blockNo];
                    yIm[this.adr] = this.zIm[this.blockNo];
                    this.adr += sofarRadix;
                    ++this.blockNo;
                }
                this.groupOffset += sofarRadix * radix;
                this.adr = this.groupOffset;
                ++this.groupNo;
            }
            ++this.dataOffset;
            this.adr = this.groupOffset = this.dataOffset;
            ++this.dataNo;
        }
    }

    private void doFFT_Mix(double[] xRe, double[] xIm, int size) {
        this.n = size;
        this.transTableSetup(this.sofarRadix, this.actualRadix, this.remainRadix);
        this.permute(this.actualRadix, this.remainRadix, xRe, xIm, this.yReOut, this.yImOut);
        for (int count = 1; count <= this.nFactor; ++count) {
            this.twiddleTransf(this.sofarRadix[count], this.actualRadix[count], this.remainRadix[count], this.yReOut, this.yImOut);
        }
        for (int i = 0; i < this.n; ++i) {
            xRe[i] = this.yReOut[i];
            xIm[i] = this.yImOut[i];
        }
    }

    private void doFFT_Mix(double[] xRe, double[] xIm, int size, int shift) {
        int i;
        double[] tmp_xRe = new double[size];
        double[] tmp_xIm = new double[size];
        for (i = 0; i < size; ++i) {
            tmp_xRe[i] = xRe[i + shift];
            tmp_xIm[i] = xIm[i + shift];
        }
        this.doFFT_Mix(tmp_xRe, tmp_xIm, size);
        for (i = 0; i < size; ++i) {
            xRe[i + shift] = tmp_xRe[i];
            xIm[i + shift] = tmp_xIm[i];
        }
    }

    private void doIFFT_Mix(double[] xRe, double[] xIm, int size) {
        int i;
        for (i = 0; i < size; ++i) {
            xIm[i] = - xIm[i];
        }
        this.doFFT_Mix(xRe, xIm, size);
        for (i = 0; i < size; ++i) {
            xRe[i] = xRe[i] / (double)size;
            xIm[i] = (- xIm[i]) / (double)size;
        }
    }

    private void doIFFT_Mix(double[] xRe, double[] xIm, int size, int shift) {
        int i;
        double[] tmp_xRe = new double[size];
        double[] tmp_xIm = new double[size];
        for (i = 0; i < size; ++i) {
            tmp_xRe[i] = xRe[i + shift];
            tmp_xIm[i] = xIm[i + shift];
        }
        this.doIFFT_Mix(tmp_xRe, tmp_xIm, size);
        for (i = 0; i < size; ++i) {
            xRe[i + shift] = tmp_xRe[i];
            xIm[i + shift] = tmp_xIm[i];
        }
    }
}

