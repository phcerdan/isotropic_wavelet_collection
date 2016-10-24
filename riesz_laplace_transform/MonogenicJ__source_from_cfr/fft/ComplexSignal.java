/*
 * Decompiled with CFR 0_118.
 */
package fft;

public class ComplexSignal {
    public static final byte REAL = 0;
    public static final byte IMAGINARY = 1;
    public static final byte COMPLEX = 2;
    public double[] real;
    public double[] imag;
    public int nx;
    public int ny;

    public ComplexSignal(int nx, int ny) {
        this.real = new double[nx * ny];
        this.imag = new double[nx * ny];
        this.nx = nx;
        this.ny = ny;
    }

    public ComplexSignal(int nx, int ny, byte allocate) {
        if (allocate == 2 || allocate == 0) {
            this.real = new double[nx * ny];
        }
        if (allocate == 2 || allocate == 1) {
            this.imag = new double[nx * ny];
        }
        this.nx = nx;
        this.ny = ny;
    }

    public ComplexSignal(double[] real, int nx, int ny) {
        this.real = real;
        this.nx = nx;
        this.ny = ny;
    }

    public ComplexSignal(double[] real, double[] imag, int nx, int ny) {
        this.real = real;
        this.imag = imag;
        this.nx = nx;
        this.ny = ny;
    }

    public double[] module() {
        int k;
        double[] module = new double[this.nx * this.ny];
        if (this.real != null && this.imag != null) {
            for (k = 0; k < this.ny * this.nx; ++k) {
                module[k] = Math.sqrt(this.real[k] * this.real[k] + this.imag[k] * this.imag[k]);
            }
        }
        if (this.real == null && this.imag != null) {
            for (k = 0; k < this.ny * this.nx; ++k) {
                module[k] = Math.abs(this.real[k]);
            }
        }
        if (this.real != null && this.imag == null) {
            for (k = 0; k < this.ny * this.nx; ++k) {
                module[k] = Math.abs(this.real[k]);
            }
        }
        return module;
    }

    public void shift() {
        double[] tmpreal = new double[this.nx * this.ny];
        double[] tmpimag = new double[this.nx * this.ny];
        int nx2 = this.nx / 2;
        int ny2 = this.ny / 2;
        int ky = 0;
        int kj = 0;
        for (int x = 0; x < this.nx; ++x) {
            int i = x >= nx2 ? x - nx2 : x + nx2;
            for (int y = 0; y < this.ny; ++y) {
                ky = y * this.nx;
                tmpreal[i + ky] = this.real[x + ky];
                tmpimag[i + ky] = this.imag[x + ky];
            }
        }
        for (int y = 0; y < this.ny; ++y) {
            int j = y >= ny2 ? y - ny2 : y + ny2;
            ky = y * this.nx;
            kj = j * this.nx;
            for (int x2 = 0; x2 < this.nx; ++x2) {
                this.real[x2 + kj] = tmpreal[x2 + ky];
                this.imag[x2 + kj] = tmpimag[x2 + ky];
            }
        }
    }

    public static ComplexSignal multiply(ComplexSignal a, ComplexSignal b) {
        int nx = a.nx;
        int ny = a.ny;
        int nxy = nx * ny;
        ComplexSignal p = new ComplexSignal(nx, ny);
        for (int k = 0; k < nxy; ++k) {
            p.real[k] = a.real[k] * b.real[k] - a.imag[k] * b.imag[k];
            p.imag[k] = a.real[k] * b.imag[k] + a.imag[k] * b.real[k];
        }
        return p;
    }

    public void multiply(ComplexSignal a) {
        int nx = a.nx;
        int ny = a.ny;
        int nxy = nx * ny;
        ComplexSignal p = new ComplexSignal(nx, ny);
        double tmp = 0.0;
        for (int k = 0; k < nxy; ++k) {
            tmp = a.real[k] * this.real[k] - a.imag[k] * this.imag[k];
            this.imag[k] = a.real[k] * this.imag[k] + a.imag[k] * this.real[k];
            this.real[k] = tmp;
        }
    }

    public static ComplexSignal subtract(ComplexSignal a, ComplexSignal b) {
        int nx = a.nx;
        int ny = a.ny;
        int nxy = nx * ny;
        ComplexSignal p = new ComplexSignal(nx, ny);
        for (int k = 0; k < nxy; ++k) {
            p.real[k] = a.real[k] - b.real[k];
            p.imag[k] = a.imag[k] - b.imag[k];
        }
        return p;
    }

    public void subtract(ComplexSignal a) {
        int nx = a.nx;
        int ny = a.ny;
        int nxy = nx * ny;
        ComplexSignal p = new ComplexSignal(nx, ny);
        double tmp = 0.0;
        for (int k = 0; k < nxy; ++k) {
            double[] arrd = this.real;
            int n = k;
            arrd[n] = arrd[n] - a.real[k];
            double[] arrd2 = this.imag;
            int n2 = k;
            arrd2[n2] = arrd2[n2] - a.imag[k];
        }
    }

    public ComplexSignal conjugate() {
        int nxy = this.nx * this.ny;
        ComplexSignal out = new ComplexSignal(this.nx, this.ny);
        System.arraycopy(this.real, 0, out.real, 0, nxy);
        for (int k = 0; k < nxy; ++k) {
            out.imag[k] = - this.imag[k];
        }
        return out;
    }

    public void multiply(double coef) {
        int nxy = this.nx * this.ny;
        int k = 0;
        while (k < nxy) {
            double[] arrd = this.real;
            int n = k;
            arrd[n] = arrd[n] * coef;
            double[] arrd2 = this.imag;
            int n2 = k++;
            arrd2[n2] = arrd2[n2] * coef;
        }
    }

    public ComplexSignal duplicate() {
        int nxy = this.nx * this.ny;
        ComplexSignal out = new ComplexSignal(this.nx, this.ny);
        System.arraycopy(this.real, 0, out.real, 0, nxy);
        System.arraycopy(this.imag, 0, out.imag, 0, nxy);
        return out;
    }
}

