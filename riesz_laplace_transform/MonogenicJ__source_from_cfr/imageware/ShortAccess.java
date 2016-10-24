/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImageStack
 */
package imageware;

import ij.ImageStack;
import imageware.Access;
import imageware.ShortBuffer;
import java.awt.Image;

public class ShortAccess
extends ShortBuffer
implements Access {
    protected ShortAccess(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    protected ShortAccess(Image image, int n) {
        super(image, n);
    }

    protected ShortAccess(ImageStack imageStack, int n) {
        super(imageStack, n);
    }

    protected ShortAccess(ImageStack imageStack, byte by) {
        super(imageStack, by);
    }

    protected ShortAccess(byte[] arrby, int n) {
        super(arrby, n);
    }

    protected ShortAccess(byte[][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortAccess(byte[][][] arrby, int n) {
        super(arrby, n);
    }

    protected ShortAccess(short[] arrs, int n) {
        super(arrs, n);
    }

    protected ShortAccess(short[][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortAccess(short[][][] arrs, int n) {
        super(arrs, n);
    }

    protected ShortAccess(float[] arrf, int n) {
        super(arrf, n);
    }

    protected ShortAccess(float[][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortAccess(float[][][] arrf, int n) {
        super(arrf, n);
    }

    protected ShortAccess(double[] arrd, int n) {
        super(arrd, n);
    }

    protected ShortAccess(double[][] arrd, int n) {
        super(arrd, n);
    }

    protected ShortAccess(double[][][] arrd, int n) {
        super(arrd, n);
    }

    public double getPixel(int n, int n2, int n3) {
        if (n >= this.nx) {
            return 0.0;
        }
        if (n2 >= this.ny) {
            return 0.0;
        }
        if (n3 >= this.nz) {
            return 0.0;
        }
        if (n < 0) {
            return 0.0;
        }
        if (n2 < 0) {
            return 0.0;
        }
        if (n3 < 0) {
            return 0.0;
        }
        return ((short[])this.data[n3])[n + n2 * this.nx] & 65535;
    }

    public double getPixel(int n, int n2, int n3, byte by) {
        int n4;
        int n5;
        int n6;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        switch (by) {
            case 2: {
                n7 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n8 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n9 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n7 = this.nx;
                n8 = this.ny;
                n9 = this.nz;
                break;
            }
            default: {
                throw new ArrayStoreException("\n-------------------------------------------------------\nError in imageware package\nUnable to put a pixel \nat the position (" + n + "," + n2 + "," + n3 + ".\n" + "-------------------------------------------------------\n");
            }
        }
        for (n6 = n; n6 < 0; n6 += n7) {
        }
        while (n6 >= this.nx) {
            n6 = (n6 = n7 - n6) < 0 ? - n6 : n6;
        }
        for (n4 = n2; n4 < 0; n4 += n8) {
        }
        while (n4 >= this.ny) {
            n4 = (n4 = n8 - n4) < 0 ? - n4 : n4;
        }
        for (n5 = n3; n5 < 0; n5 += n9) {
        }
        while (n5 >= this.nz) {
            n5 = (n5 = n9 - n5) < 0 ? - n5 : n5;
        }
        return ((short[])this.data[n5])[n6 + n4 * this.nx] & 65535;
    }

    public double getInterpolatedPixel(double d, double d2, double d3) {
        if (d > (double)(this.nx - 1)) {
            return 0.0;
        }
        if (d2 > (double)(this.ny - 1)) {
            return 0.0;
        }
        if (d3 > (double)(this.nz - 1)) {
            return 0.0;
        }
        if (d < 0.0) {
            return 0.0;
        }
        if (d2 < 0.0) {
            return 0.0;
        }
        if (d3 < 0.0) {
            return 0.0;
        }
        double d4 = 0.0;
        int n = d >= 0.0 ? (int)d : (int)d - 1;
        int n2 = d2 >= 0.0 ? (int)d2 : (int)d2 - 1;
        int n3 = d3 >= 0.0 ? (int)d3 : (int)d3 - 1;
        boolean bl = n == this.nx - 1;
        boolean bl2 = n2 == this.ny - 1;
        boolean bl3 = n3 == this.nz - 1;
        int n4 = n + n2 * this.nx;
        switch (this.getDimension()) {
            case 1: {
                double d5 = ((short[])this.data[n3])[n4] & 65535;
                double d6 = bl ? d5 : (double)(((short[])this.data[n3])[n4 + 1] & 65535);
                double d7 = d - (double)n;
                return d6 * d7 - d5 * (d7 - 1.0);
            }
            case 2: {
                double d8;
                double d9 = ((short[])this.data[n3])[n4] & 65535;
                double d10 = bl ? d9 : (double)(((short[])this.data[n3])[n4 + 1] & 65535);
                double d11 = d8 = bl2 ? d9 : (double)(((short[])this.data[n3])[n4 + this.nx] & 65535);
                double d12 = bl ? (bl2 ? d9 : d8) : (double)(((short[])this.data[n3])[n4 + 1 + this.nx] & 65535);
                double d13 = d - (double)n;
                double d14 = d2 - (double)n2;
                return d13 * (d12 * d14 - d10 * (d14 - 1.0)) - (d13 - 1.0) * (d8 * d14 - d9 * (d14 - 1.0));
            }
            case 3: {
                double d15;
                double d16;
                double d17;
                double d18;
                double d19 = ((short[])this.data[n3])[n4] & 65535;
                double d20 = bl ? d19 : (double)(((short[])this.data[n3])[n4 + 1] & 65535);
                double d21 = d17 = bl2 ? d19 : (double)(((short[])this.data[n3])[n4 + this.nx] & 65535);
                double d22 = bl ? (bl2 ? d19 : d17) : (double)(((short[])this.data[n3])[n4 + 1 + this.nx] & 65535);
                double d23 = d16 = bl3 ? d19 : (double)(((short[])this.data[n3 + 1])[n4] & 65535);
                double d24 = bl3 ? (bl2 ? d19 : d17) : (d15 = (double)(((short[])this.data[n3 + 1])[n4 + 1] & 65535));
                double d25 = bl3 ? (bl ? d19 : d20) : (d18 = (double)(((short[])this.data[n3 + 1])[n4 + this.nx] & 65535));
                double d26 = bl3 ? (bl2 ? (bl ? d19 : d20) : d22) : (double)(((short[])this.data[n3 + 1])[n4 + 1 + this.nx] & 65535);
                double d27 = d - (double)n;
                double d28 = d2 - (double)n2;
                double d29 = d3 - (double)n3;
                double d30 = d27 * (d22 * d28 - d20 * (d28 - 1.0)) - (d27 - 1.0) * (d17 * d28 - d19 * (d28 - 1.0));
                double d31 = d27 * (d26 * d28 - d18 * (d28 - 1.0)) - (d27 - 1.0) * (d15 * d28 - d16 * (d28 - 1.0));
                return d31 * d29 - d30 * (d29 - 1.0);
            }
        }
        return d4;
    }

    public double getInterpolatedPixel(double d, double d2, double d3, byte by) {
        double d4 = 0.0;
        int n = d >= 0.0 ? (int)d : (int)d - 1;
        int n2 = d2 >= 0.0 ? (int)d2 : (int)d2 - 1;
        int n3 = d3 >= 0.0 ? (int)d3 : (int)d3 - 1;
        switch (this.getDimension()) {
            case 1: {
                double d5 = this.getPixel(n, n2, n3, by);
                double d6 = this.getPixel(n + 1, n2, n3, by);
                double d7 = d - (double)n;
            }
            case 2: {
                double d8 = this.getPixel(n, n2, n3, by);
                double d9 = this.getPixel(n + 1, n2, n3, by);
                double d10 = this.getPixel(n, n2 + 1, n3, by);
                double d11 = this.getPixel(n + 1, n2 + 1, n3, by);
                double d12 = d - (double)n;
                double d13 = d2 - (double)n2;
                return d12 * (d11 * d13 - d9 * (d13 - 1.0)) - (d12 - 1.0) * (d10 * d13 - d8 * (d13 - 1.0));
            }
            case 3: {
                double d14 = this.getPixel(n, n2, n3, by);
                double d15 = this.getPixel(n + 1, n2, n3, by);
                double d16 = this.getPixel(n, n2 + 1, n3, by);
                double d17 = this.getPixel(n + 1, n2 + 1, n3, by);
                double d18 = this.getPixel(n, n2, n3 + 1, by);
                double d19 = this.getPixel(n + 1, n2, n3 + 1, by);
                double d20 = this.getPixel(n, n2 + 1, n3 + 1, by);
                double d21 = this.getPixel(n + 1, n2 + 1, n3 + 1, by);
                double d22 = d - (double)n;
                double d23 = d2 - (double)n2;
                double d24 = d3 - (double)n3;
                double d25 = d22 * (d17 * d23 - d15 * (d23 - 1.0)) - (d22 - 1.0) * (d16 * d23 - d14 * (d23 - 1.0));
                double d26 = d22 * (d21 * d23 - d20 * (d23 - 1.0)) - (d22 - 1.0) * (d19 * d23 - d18 * (d23 - 1.0));
                return d26 * d24 - d25 * (d24 - 1.0);
            }
        }
        return d4;
    }

    public void putPixel(int n, int n2, int n3, double d) {
        if (n >= this.nx) {
            return;
        }
        if (n2 >= this.ny) {
            return;
        }
        if (n3 >= this.nz) {
            return;
        }
        if (n < 0) {
            return;
        }
        if (n2 < 0) {
            return;
        }
        if (n3 < 0) {
            return;
        }
        ((short[])this.data[n3])[n + n2 * this.nx] = (short)d;
    }

    public void getBoundedX(int n, int n2, int n3, byte[] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrby.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrby[i] = (byte)(arrs[n5] & 65535);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedX(int n, int n2, int n3, short[] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrs.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[i] = (short)(arrs2[n5] & 65535);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedX(int n, int n2, int n3, float[] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrf.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrf[i] = arrs[n5] & 65535;
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedX(int n, int n2, int n3, double[] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrd.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrd[i] = arrs[n5] & 65535;
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("X", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBoundedY(int n, int n2, int n3, byte[] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrby.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrby[i] = (byte)(arrs[n5] & 65535);
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedY(int n, int n2, int n3, short[] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrs.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[i] = (short)(arrs2[n5] & 65535);
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedY(int n, int n2, int n3, float[] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrf.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrf[i] = arrs[n5] & 65535;
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedY(int n, int n2, int n3, double[] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrd.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrd[i] = arrs[n5] & 65535;
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Y", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBoundedZ(int n, int n2, int n3, byte[] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrby.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                arrby[i] = (byte)(((short[])this.data[n5])[n6] & 65535);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Z", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedZ(int n, int n2, int n3, short[] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrs.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                arrs[i] = (short)(((short[])this.data[n5])[n6] & 65535);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Z", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedZ(int n, int n2, int n3, float[] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrf.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                arrf[i] = ((short[])this.data[n5])[n6] & 65535;
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Z", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedZ(int n, int n2, int n3, double[] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrd.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                arrd[i] = ((short[])this.data[n5])[n6] & 65535;
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("Z", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBoundedXY(int n, int n2, int n3, byte[][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrby[j][i] = (byte)(arrs[n6] & 65535);
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedXY(int n, int n2, int n3, short[][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs[j][i] = (short)(arrs2[n6] & 65535);
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedXY(int n, int n2, int n3, float[][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrf[j][i] = arrs[n6] & 65535;
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedXY(int n, int n2, int n3, double[][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrd[j][i] = arrs[n6] & 65535;
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XY", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBoundedXZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrby[j][i] = (byte)(((short[])this.data[n3])[n7] & 65535);
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedXZ(int n, int n2, int n3, short[][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs[j][i] = (short)(((short[])this.data[n3])[n7] & 65535);
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedXZ(int n, int n2, int n3, float[][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrf[j][i] = ((short[])this.data[n3])[n7] & 65535;
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedXZ(int n, int n2, int n3, double[][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrd[j][i] = ((short[])this.data[n3])[n7] & 65535;
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("YZ", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBoundedYZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrby[j][i] = (byte)(((short[])this.data[n3])[n7] & 65535);
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedYZ(int n, int n2, int n3, short[][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs[j][i] = (short)(((short[])this.data[n3])[n7] & 65535);
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedYZ(int n, int n2, int n3, float[][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrf[j][i] = ((short[])this.data[n3])[n7] & 65535;
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedYZ(int n, int n2, int n3, double[][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrd[j][i] = ((short[])this.data[n3])[n7] & 65535;
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XZ", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBoundedXYZ(int n, int n2, int n3, byte[][][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrby.length;
            int n10 = arrby[0].length;
            int n11 = arrby[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrby[k][j][i] = (byte)(arrs[n8] & 65535);
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void getBoundedXYZ(int n, int n2, int n3, short[][][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrs.length;
            int n10 = arrs[0].length;
            int n11 = arrs[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs2 = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrs[k][j][i] = (short)(arrs2[n8] & 65535);
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void getBoundedXYZ(int n, int n2, int n3, float[][][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrf.length;
            int n10 = arrf[0].length;
            int n11 = arrf[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrf[k][j][i] = arrs[n8] & 65535;
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void getBoundedXYZ(int n, int n2, int n3, double[][][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrd.length;
            int n10 = arrd[0].length;
            int n11 = arrd[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrd[k][j][i] = arrs[n8] & 65535;
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_get("XYZ", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void getBlockX(int n, int n2, int n3, byte[] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("X", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrby.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            n8 *= this.nx;
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n + i; n11 < 0; n11 += n4) {
                }
                while (n11 >= this.nx) {
                    n11 = (n11 = n4 - n11) < 0 ? - n11 : n11;
                }
                arrby[i] = (byte)(arrs[n11 + n8] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockX(int n, int n2, int n3, short[] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("X", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrs.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            n8 *= this.nx;
            short[] arrs2 = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n + i; n11 < 0; n11 += n4) {
                }
                while (n11 >= this.nx) {
                    n11 = (n11 = n4 - n11) < 0 ? - n11 : n11;
                }
                arrs[i] = (short)(arrs2[n11 + n8] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockX(int n, int n2, int n3, float[] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("X", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrf.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            n8 *= this.nx;
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n + i; n11 < 0; n11 += n4) {
                }
                while (n11 >= this.nx) {
                    n11 = (n11 = n4 - n11) < 0 ? - n11 : n11;
                }
                arrf[i] = arrs[n11 + n8] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockX(int n, int n2, int n3, double[] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("X", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrd.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            n8 *= this.nx;
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n + i; n11 < 0; n11 += n4) {
                }
                while (n11 >= this.nx) {
                    n11 = (n11 = n4 - n11) < 0 ? - n11 : n11;
                }
                arrd[i] = arrs[n11 + n8] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getBlockY(int n, int n2, int n3, byte[] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Y", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrby.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n; n8 < 0; n8 += n4) {
            }
            while (n8 >= this.nx) {
                n8 = (n8 = n4 - n8) < 0 ? - n8 : n8;
            }
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                arrby[i] = (byte)(arrs[n8 + n11 * this.nx] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockY(int n, int n2, int n3, short[] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Y", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrs.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n; n8 < 0; n8 += n4) {
            }
            while (n8 >= this.nx) {
                n8 = (n8 = n4 - n8) < 0 ? - n8 : n8;
            }
            short[] arrs2 = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                arrs[i] = (short)(arrs2[n8 + n11 * this.nx] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockY(int n, int n2, int n3, float[] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Y", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrf.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n; n8 < 0; n8 += n4) {
            }
            while (n8 >= this.nx) {
                n8 = (n8 = n4 - n8) < 0 ? - n8 : n8;
            }
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                arrf[i] = arrs[n8 + n11 * this.nx] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockY(int n, int n2, int n3, double[] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Y", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrd.length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n; n8 < 0; n8 += n4) {
            }
            while (n8 >= this.nx) {
                n8 = (n8 = n4 - n8) < 0 ? - n8 : n8;
            }
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                arrd[i] = arrs[n8 + n11 * this.nx] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getBlockZ(int n, int n2, int n3, byte[] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Z", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrby.length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            int n11 = n7 + n8 * this.nx;
            for (int i = 0; i < n10; ++i) {
                int n12;
                for (n12 = n3 + i; n12 < 0; n12 += n6) {
                }
                while (n12 >= this.nz) {
                    n12 = (n12 = n6 - n12) < 0 ? - n12 : n12;
                }
                arrby[i] = (byte)(((short[])this.data[n12])[n11] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockZ(int n, int n2, int n3, short[] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Z", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrs.length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            int n11 = n7 + n8 * this.nx;
            for (int i = 0; i < n10; ++i) {
                int n12;
                for (n12 = n3 + i; n12 < 0; n12 += n6) {
                }
                while (n12 >= this.nz) {
                    n12 = (n12 = n6 - n12) < 0 ? - n12 : n12;
                }
                arrs[i] = (short)(((short[])this.data[n12])[n11] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockZ(int n, int n2, int n3, float[] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Z", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrf.length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            int n11 = n7 + n8 * this.nx;
            for (int i = 0; i < n10; ++i) {
                int n12;
                for (n12 = n3 + i; n12 < 0; n12 += n6) {
                }
                while (n12 >= this.nz) {
                    n12 = (n12 = n6 - n12) < 0 ? - n12 : n12;
                }
                arrf[i] = ((short[])this.data[n12])[n11] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockZ(int n, int n2, int n3, double[] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("Z", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8;
            int n9 = n + n2 * this.nx;
            int n10 = arrd.length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (n8 = n2; n8 < 0; n8 += n5) {
            }
            while (n8 >= this.ny) {
                n8 = (n8 = n5 - n8) < 0 ? - n8 : n8;
            }
            int n11 = n7 + n8 * this.nx;
            for (int i = 0; i < n10; ++i) {
                int n12;
                for (n12 = n3 + i; n12 < 0; n12 += n6) {
                }
                while (n12 >= this.nz) {
                    n12 = (n12 = n6 - n12) < 0 ? - n12 : n12;
                }
                arrd[i] = ((short[])this.data[n12])[n11] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getBlockXY(int n, int n2, int n3, byte[][] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XY", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrby.length;
            int n10 = arrby[0].length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                n11 *= this.nx;
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrby[j][i] = (byte)(arrs[n12 + n11] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockXY(int n, int n2, int n3, short[][] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XY", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrs.length;
            int n10 = arrs[0].length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            short[] arrs2 = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                n11 *= this.nx;
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrs[j][i] = (short)(arrs2[n12 + n11] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockXY(int n, int n2, int n3, float[][] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XY", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrf.length;
            int n10 = arrf[0].length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                n11 *= this.nx;
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrf[j][i] = arrs[n12 + n11] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockXY(int n, int n2, int n3, double[][] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XY", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrd.length;
            int n10 = arrd[0].length;
            for (n7 = n3; n7 < 0; n7 += n6) {
            }
            while (n7 >= this.nz) {
                n7 = (n7 = n6 - n7) < 0 ? - n7 : n7;
            }
            short[] arrs = (short[])this.data[n7];
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n2 + i; n11 < 0; n11 += n5) {
                }
                while (n11 >= this.ny) {
                    n11 = (n11 = n5 - n11) < 0 ? - n11 : n11;
                }
                n11 *= this.nx;
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrd[j][i] = arrs[n12 + n11] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getBlockXZ(int n, int n2, int n3, byte[][] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrby.length;
            int n10 = arrby[0].length;
            for (n7 = n2; n7 < 0; n7 += n5) {
            }
            while (n7 >= this.ny) {
                n7 = (n7 = n5 - n7) < 0 ? - n7 : n7;
            }
            n7 *= this.nx;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = n6 - n7;
                    n11 = n11 < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrby[j][i] = (byte)(((short[])this.data[n11])[n12 + n7] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockXZ(int n, int n2, int n3, short[][] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrs.length;
            int n10 = arrs[0].length;
            for (n7 = n2; n7 < 0; n7 += n5) {
            }
            while (n7 >= this.ny) {
                n7 = (n7 = n5 - n7) < 0 ? - n7 : n7;
            }
            n7 *= this.nx;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = n6 - n7;
                    n11 = n11 < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrs[j][i] = (short)(((short[])this.data[n11])[n12 + n7] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockXZ(int n, int n2, int n3, float[][] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrf.length;
            int n10 = arrf[0].length;
            for (n7 = n2; n7 < 0; n7 += n5) {
            }
            while (n7 >= this.ny) {
                n7 = (n7 = n5 - n7) < 0 ? - n7 : n7;
            }
            n7 *= this.nx;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = n6 - n7;
                    n11 = n11 < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrf[j][i] = ((short[])this.data[n11])[n12 + n7] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockXZ(int n, int n2, int n3, double[][] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrd.length;
            int n10 = arrd[0].length;
            for (n7 = n2; n7 < 0; n7 += n5) {
            }
            while (n7 >= this.ny) {
                n7 = (n7 = n5 - n7) < 0 ? - n7 : n7;
            }
            n7 *= this.nx;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = n6 - n7;
                    n11 = n11 < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n + j; n12 < 0; n12 += n4) {
                    }
                    while (n12 >= this.nx) {
                        n12 = (n12 = n4 - n12) < 0 ? - n12 : n12;
                    }
                    arrd[j][i] = ((short[])this.data[n11])[n12 + n7] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getBlockYZ(int n, int n2, int n3, byte[][] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrby.length;
            int n10 = arrby[0].length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    arrby[j][i] = (byte)(((short[])this.data[n11])[n7 + n12 * this.nx] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockYZ(int n, int n2, int n3, short[][] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrs.length;
            int n10 = arrs[0].length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    arrs[j][i] = (short)(((short[])this.data[n11])[n7 + n12 * this.nx] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockYZ(int n, int n2, int n3, float[][] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrf.length;
            int n10 = arrf[0].length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    arrf[j][i] = ((short[])this.data[n11])[n7 + n12 * this.nx] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockYZ(int n, int n2, int n3, double[][] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7;
            int n8 = n + n2 * this.nx;
            int n9 = arrd.length;
            int n10 = arrd[0].length;
            for (n7 = n; n7 < 0; n7 += n4) {
            }
            while (n7 >= this.nx) {
                n7 = (n7 = n4 - n7) < 0 ? - n7 : n7;
            }
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    arrd[j][i] = ((short[])this.data[n11])[n7 + n12 * this.nx] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getBlockXYZ(int n, int n2, int n3, byte[][][] arrby, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
            }
        }
        try {
            int n7 = n + n2 * this.nx;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            int n10 = arrby[0][0].length;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                short[] arrs = (short[])this.data[n11];
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    n12 *= this.nx;
                    for (int k = 0; k < n8; ++k) {
                        int n13;
                        for (n13 = n + k; n13 < 0; n13 += n4) {
                        }
                        while (n13 >= this.nx) {
                            n13 = (n13 = n4 - n13) < 0 ? - n13 : n13;
                        }
                        arrby[k][j][i] = (byte)(arrs[n13 + n12] & 65535);
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getBlockXYZ(int n, int n2, int n3, short[][][] arrs, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
            }
        }
        try {
            int n7 = n + n2 * this.nx;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            int n10 = arrs[0][0].length;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                short[] arrs2 = (short[])this.data[n11];
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    n12 *= this.nx;
                    for (int k = 0; k < n8; ++k) {
                        int n13;
                        for (n13 = n + k; n13 < 0; n13 += n4) {
                        }
                        while (n13 >= this.nx) {
                            n13 = (n13 = n4 - n13) < 0 ? - n13 : n13;
                        }
                        arrs[k][j][i] = (short)(arrs2[n13 + n12] & 65535);
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getBlockXYZ(int n, int n2, int n3, float[][][] arrf, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
            }
        }
        try {
            int n7 = n + n2 * this.nx;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            int n10 = arrf[0][0].length;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                short[] arrs = (short[])this.data[n11];
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    n12 *= this.nx;
                    for (int k = 0; k < n8; ++k) {
                        int n13;
                        for (n13 = n + k; n13 < 0; n13 += n4) {
                        }
                        while (n13 >= this.nx) {
                            n13 = (n13 = n4 - n13) < 0 ? - n13 : n13;
                        }
                        arrf[k][j][i] = arrs[n13 + n12] & 65535;
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getBlockXYZ(int n, int n2, int n3, double[][][] arrd, byte by) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (by) {
            case 2: {
                n4 = this.nx <= 1 ? 1 : 2 * this.nx - 2;
                n5 = this.ny <= 1 ? 1 : 2 * this.ny - 2;
                n6 = this.nz <= 1 ? 1 : 2 * this.nz - 2;
                break;
            }
            case 3: {
                n4 = this.nx;
                n5 = this.ny;
                n6 = this.nz;
                break;
            }
            default: {
                this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
            }
        }
        try {
            int n7 = n + n2 * this.nx;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            int n10 = arrd[0][0].length;
            for (int i = 0; i < n10; ++i) {
                int n11;
                for (n11 = n3 + i; n11 < 0; n11 += n6) {
                }
                while (n11 >= this.nz) {
                    n11 = (n11 = n6 - n11) < 0 ? - n11 : n11;
                }
                short[] arrs = (short[])this.data[n11];
                for (int j = 0; j < n9; ++j) {
                    int n12;
                    for (n12 = n2 + j; n12 < 0; n12 += n5) {
                    }
                    while (n12 >= this.ny) {
                        n12 = (n12 = n5 - n12) < 0 ? - n12 : n12;
                    }
                    n12 *= this.nx;
                    for (int k = 0; k < n8; ++k) {
                        int n13;
                        for (n13 = n + k; n13 < 0; n13 += n4) {
                        }
                        while (n13 >= this.nx) {
                            n13 = (n13 = n4 - n13) < 0 ? - n13 : n13;
                        }
                        arrd[k][j][i] = arrs[n13 + n12] & 65535;
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodX(int n, int n2, int n3, byte[] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrby.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n2; n10 < 0; n10 += n5) {
            }
            while (n10 >= this.ny) {
                n10 = (n10 = n5 - n10) < 0 ? - n10 : n10;
            }
            n10 *= this.nx;
            int n13 = n - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n4) {
                }
                while (n14 >= this.nx) {
                    n14 = (n14 = n4 - n14) < 0 ? - n14 : n14;
                }
                arrby[i] = (byte)(arrs[n14 + n10] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodX(int n, int n2, int n3, short[] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrs.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n2; n10 < 0; n10 += n5) {
            }
            while (n10 >= this.ny) {
                n10 = (n10 = n5 - n10) < 0 ? - n10 : n10;
            }
            n10 *= this.nx;
            int n13 = n - n12 / 2;
            short[] arrs2 = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n4) {
                }
                while (n14 >= this.nx) {
                    n14 = (n14 = n4 - n14) < 0 ? - n14 : n14;
                }
                arrs[i] = (short)(arrs2[n14 + n10] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodX(int n, int n2, int n3, float[] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrf.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n2; n10 < 0; n10 += n5) {
            }
            while (n10 >= this.ny) {
                n10 = (n10 = n5 - n10) < 0 ? - n10 : n10;
            }
            n10 *= this.nx;
            int n13 = n - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n4) {
                }
                while (n14 >= this.nx) {
                    n14 = (n14 = n4 - n14) < 0 ? - n14 : n14;
                }
                arrf[i] = arrs[n14 + n10] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodX(int n, int n2, int n3, double[] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrd.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n2; n10 < 0; n10 += n5) {
            }
            while (n10 >= this.ny) {
                n10 = (n10 = n5 - n10) < 0 ? - n10 : n10;
            }
            n10 *= this.nx;
            int n13 = n - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n4) {
                }
                while (n14 >= this.nx) {
                    n14 = (n14 = n4 - n14) < 0 ? - n14 : n14;
                }
                arrd[i] = arrs[n14 + n10] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("X", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodY(int n, int n2, int n3, byte[] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrby.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            int n13 = n2 - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n5) {
                }
                while (n14 >= this.ny) {
                    n14 = (n14 = n5 - n14) < 0 ? - n14 : n14;
                }
                arrby[i] = (byte)(arrs[n10 + n14 * this.nx] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodY(int n, int n2, int n3, short[] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrs.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            int n13 = n2 - n12 / 2;
            short[] arrs2 = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n5) {
                }
                while (n14 >= this.ny) {
                    n14 = (n14 = n5 - n14) < 0 ? - n14 : n14;
                }
                arrs[i] = (short)(arrs2[n10 + n14 * this.nx] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodY(int n, int n2, int n3, float[] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrf.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            int n13 = n2 - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n5) {
                }
                while (n14 >= this.ny) {
                    n14 = (n14 = n5 - n14) < 0 ? - n14 : n14;
                }
                arrf[i] = arrs[n10 + n14 * this.nx] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodY(int n, int n2, int n3, double[] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrd.length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            int n13 = n2 - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n14;
                for (n14 = n13 + i; n14 < 0; n14 += n5) {
                }
                while (n14 >= this.ny) {
                    n14 = (n14 = n5 - n14) < 0 ? - n14 : n14;
                }
                arrd[i] = arrs[n10 + n14 * this.nx] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Y", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodZ(int n, int n2, int n3, byte[] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrby.length;
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n10 + n9 * this.nx;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                arrby[i] = (byte)(((short[])this.data[n15])[n13] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodZ(int n, int n2, int n3, short[] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrs.length;
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n10 + n9 * this.nx;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                arrs[i] = (short)(((short[])this.data[n15])[n13] & 65535);
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodZ(int n, int n2, int n3, float[] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrf.length;
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n10 + n9 * this.nx;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                arrf[i] = ((short[])this.data[n15])[n13] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodZ(int n, int n2, int n3, double[] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10;
            int n11 = n + n2 * this.nx;
            int n12 = arrd.length;
            for (n10 = n; n10 < 0; n10 += n4) {
            }
            while (n10 >= this.nx) {
                n10 = (n10 = n4 - n10) < 0 ? - n10 : n10;
            }
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n10 + n9 * this.nx;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                arrd[i] = ((short[])this.data[n15])[n13] & 65535;
            }
        }
        catch (Exception var12_10) {
            this.throw_get("Z", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodXY(int n, int n2, int n3, byte[][] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrby.length;
            int n12 = arrby[0].length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n - n11 / 2;
            int n14 = n2 - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n5) {
                }
                while (n15 >= this.ny) {
                    n15 = (n15 = n5 - n15) < 0 ? - n15 : n15;
                }
                n15 *= this.nx;
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrby[j][i] = (byte)(arrs[n16 + n15] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodXY(int n, int n2, int n3, short[][] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrs.length;
            int n12 = arrs[0].length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n - n11 / 2;
            int n14 = n2 - n12 / 2;
            short[] arrs2 = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n5) {
                }
                while (n15 >= this.ny) {
                    n15 = (n15 = n5 - n15) < 0 ? - n15 : n15;
                }
                n15 *= this.nx;
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrs[j][i] = (short)(arrs2[n16 + n15] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodXY(int n, int n2, int n3, float[][] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrf.length;
            int n12 = arrf[0].length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n - n11 / 2;
            int n14 = n2 - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n5) {
                }
                while (n15 >= this.ny) {
                    n15 = (n15 = n5 - n15) < 0 ? - n15 : n15;
                }
                n15 *= this.nx;
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrf[j][i] = arrs[n16 + n15] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodXY(int n, int n2, int n3, double[][] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrd.length;
            int n12 = arrd[0].length;
            for (n9 = n3; n9 < 0; n9 += n8) {
            }
            while (n9 >= this.nz) {
                n9 = (n9 = n8 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n - n11 / 2;
            int n14 = n2 - n12 / 2;
            short[] arrs = (short[])this.data[n9];
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n5) {
                }
                while (n15 >= this.ny) {
                    n15 = (n15 = n5 - n15) < 0 ? - n15 : n15;
                }
                n15 *= this.nx;
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrd[j][i] = arrs[n16 + n15] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XY", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodXZ(int n, int n2, int n3, byte[][] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrby.length;
            int n12 = arrby[0].length;
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            n9 *= this.nx;
            int n13 = n - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = n8 - n9;
                    n15 = n15 < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrby[j][i] = (byte)(((short[])this.data[n15])[n16 + n9] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodXZ(int n, int n2, int n3, short[][] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrs.length;
            int n12 = arrs[0].length;
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            n9 *= this.nx;
            int n13 = n - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = n8 - n9;
                    n15 = n15 < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrs[j][i] = (short)(((short[])this.data[n15])[n16 + n9] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodXZ(int n, int n2, int n3, float[][] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrf.length;
            int n12 = arrf[0].length;
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            n9 *= this.nx;
            int n13 = n - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = n8 - n9;
                    n15 = n15 < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrf[j][i] = ((short[])this.data[n15])[n16 + n9] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodXZ(int n, int n2, int n3, double[][] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrd.length;
            int n12 = arrd[0].length;
            for (n9 = n2; n9 < 0; n9 += n5) {
            }
            while (n9 >= this.ny) {
                n9 = (n9 = n5 - n9) < 0 ? - n9 : n9;
            }
            n9 *= this.nx;
            int n13 = n - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = n8 - n9;
                    n15 = n15 < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n4) {
                    }
                    while (n16 >= this.nx) {
                        n16 = (n16 = n4 - n16) < 0 ? - n16 : n16;
                    }
                    arrd[j][i] = ((short[])this.data[n15])[n16 + n9] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodYZ(int n, int n2, int n3, byte[][] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrby.length;
            int n12 = arrby[0].length;
            for (n9 = n; n9 < 0; n9 += n4) {
            }
            while (n9 >= this.nx) {
                n9 = (n9 = n4 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n2 - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n5) {
                    }
                    while (n16 >= this.ny) {
                        n16 = (n16 = n5 - n16) < 0 ? - n16 : n16;
                    }
                    arrby[j][i] = (byte)(((short[])this.data[n15])[n9 + n16 * this.nx] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodYZ(int n, int n2, int n3, short[][] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrs.length;
            int n12 = arrs[0].length;
            for (n9 = n; n9 < 0; n9 += n4) {
            }
            while (n9 >= this.nx) {
                n9 = (n9 = n4 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n2 - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n5) {
                    }
                    while (n16 >= this.ny) {
                        n16 = (n16 = n5 - n16) < 0 ? - n16 : n16;
                    }
                    arrs[j][i] = (short)(((short[])this.data[n15])[n9 + n16 * this.nx] & 65535);
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodYZ(int n, int n2, int n3, float[][] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrf.length;
            int n12 = arrf[0].length;
            for (n9 = n; n9 < 0; n9 += n4) {
            }
            while (n9 >= this.nx) {
                n9 = (n9 = n4 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n2 - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n5) {
                    }
                    while (n16 >= this.ny) {
                        n16 = (n16 = n5 - n16) < 0 ? - n16 : n16;
                    }
                    arrf[j][i] = ((short[])this.data[n15])[n9 + n16 * this.nx] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodYZ(int n, int n2, int n3, double[][] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9;
            int n10 = n + n2 * this.nx;
            int n11 = arrd.length;
            int n12 = arrd[0].length;
            for (n9 = n; n9 < 0; n9 += n4) {
            }
            while (n9 >= this.nx) {
                n9 = (n9 = n4 - n9) < 0 ? - n9 : n9;
            }
            int n13 = n2 - n11 / 2;
            int n14 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n15;
                for (n15 = n14 + i; n15 < 0; n15 += n8) {
                }
                while (n15 >= this.nz) {
                    n15 = (n15 = n8 - n15) < 0 ? - n15 : n15;
                }
                for (int j = 0; j < n11; ++j) {
                    int n16;
                    for (n16 = n13 + j; n16 < 0; n16 += n5) {
                    }
                    while (n16 >= this.ny) {
                        n16 = (n16 = n5 - n16) < 0 ? - n16 : n16;
                    }
                    arrd[j][i] = ((short[])this.data[n15])[n9 + n16 * this.nx] & 65535;
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("YZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void getNeighborhoodXYZ(int n, int n2, int n3, byte[][][] arrby, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9 = n + n2 * this.nx;
            int n10 = arrby.length;
            int n11 = arrby[0].length;
            int n12 = arrby[0][0].length;
            int n13 = n - n10 / 2;
            int n14 = n2 - n11 / 2;
            int n15 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n16;
                for (n16 = n15 + i; n16 < 0; n16 += n8) {
                }
                while (n16 >= this.nz) {
                    n16 = (n16 = n8 - n16) < 0 ? - n16 : n16;
                }
                short[] arrs = (short[])this.data[n16];
                for (int j = 0; j < n11; ++j) {
                    int n17;
                    for (n17 = n14 + j; n17 < 0; n17 += n5) {
                    }
                    while (n17 >= this.ny) {
                        n17 = (n17 = n5 - n17) < 0 ? - n17 : n17;
                    }
                    n17 *= this.nx;
                    for (int k = 0; k < n10; ++k) {
                        int n18;
                        for (n18 = n13 + k; n18 < 0; n18 += n4) {
                        }
                        while (n18 >= this.nx) {
                            n18 = (n18 = n4 - n18) < 0 ? - n18 : n18;
                        }
                        arrby[k][j][i] = (byte)(arrs[n18 + n17] & 65535);
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrby, n, n2, n3);
        }
    }

    public void getNeighborhoodXYZ(int n, int n2, int n3, short[][][] arrs, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9 = n + n2 * this.nx;
            int n10 = arrs.length;
            int n11 = arrs[0].length;
            int n12 = arrs[0][0].length;
            int n13 = n - n10 / 2;
            int n14 = n2 - n11 / 2;
            int n15 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n16;
                for (n16 = n15 + i; n16 < 0; n16 += n8) {
                }
                while (n16 >= this.nz) {
                    n16 = (n16 = n8 - n16) < 0 ? - n16 : n16;
                }
                short[] arrs2 = (short[])this.data[n16];
                for (int j = 0; j < n11; ++j) {
                    int n17;
                    for (n17 = n14 + j; n17 < 0; n17 += n5) {
                    }
                    while (n17 >= this.ny) {
                        n17 = (n17 = n5 - n17) < 0 ? - n17 : n17;
                    }
                    n17 *= this.nx;
                    for (int k = 0; k < n10; ++k) {
                        int n18;
                        for (n18 = n13 + k; n18 < 0; n18 += n4) {
                        }
                        while (n18 >= this.nx) {
                            n18 = (n18 = n4 - n18) < 0 ? - n18 : n18;
                        }
                        arrs[k][j][i] = (short)(arrs2[n18 + n17] & 65535);
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrs, n, n2, n3);
        }
    }

    public void getNeighborhoodXYZ(int n, int n2, int n3, float[][][] arrf, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9 = n + n2 * this.nx;
            int n10 = arrf.length;
            int n11 = arrf[0].length;
            int n12 = arrf[0][0].length;
            int n13 = n - n10 / 2;
            int n14 = n2 - n11 / 2;
            int n15 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n16;
                for (n16 = n15 + i; n16 < 0; n16 += n8) {
                }
                while (n16 >= this.nz) {
                    n16 = (n16 = n8 - n16) < 0 ? - n16 : n16;
                }
                short[] arrs = (short[])this.data[n16];
                for (int j = 0; j < n11; ++j) {
                    int n17;
                    for (n17 = n14 + j; n17 < 0; n17 += n5) {
                    }
                    while (n17 >= this.ny) {
                        n17 = (n17 = n5 - n17) < 0 ? - n17 : n17;
                    }
                    n17 *= this.nx;
                    for (int k = 0; k < n10; ++k) {
                        int n18;
                        for (n18 = n13 + k; n18 < 0; n18 += n4) {
                        }
                        while (n18 >= this.nx) {
                            n18 = (n18 = n4 - n18) < 0 ? - n18 : n18;
                        }
                        arrf[k][j][i] = arrs[n18 + n17] & 65535;
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrf, n, n2, n3);
        }
    }

    public void getNeighborhoodXYZ(int n, int n2, int n3, double[][][] arrd, byte by) {
        int n4;
        int n5;
        int n6 = by == 2 ? (this.nx <= 1 ? 1 : 2 * this.nx - 2) : (n4 = this.nx);
        int n7 = by == 2 ? (this.ny <= 1 ? 1 : 2 * this.ny - 2) : (n5 = this.ny);
        int n8 = by == 2 ? (this.nz <= 1 ? 1 : 2 * this.nz - 2) : this.nz;
        try {
            int n9 = n + n2 * this.nx;
            int n10 = arrd.length;
            int n11 = arrd[0].length;
            int n12 = arrd[0][0].length;
            int n13 = n - n10 / 2;
            int n14 = n2 - n11 / 2;
            int n15 = n3 - n12 / 2;
            for (int i = 0; i < n12; ++i) {
                int n16;
                for (n16 = n15 + i; n16 < 0; n16 += n8) {
                }
                while (n16 >= this.nz) {
                    n16 = (n16 = n8 - n16) < 0 ? - n16 : n16;
                }
                short[] arrs = (short[])this.data[n16];
                for (int j = 0; j < n11; ++j) {
                    int n17;
                    for (n17 = n14 + j; n17 < 0; n17 += n5) {
                    }
                    while (n17 >= this.ny) {
                        n17 = (n17 = n5 - n17) < 0 ? - n17 : n17;
                    }
                    n17 *= this.nx;
                    for (int k = 0; k < n10; ++k) {
                        int n18;
                        for (n18 = n13 + k; n18 < 0; n18 += n4) {
                        }
                        while (n18 >= this.nx) {
                            n18 = (n18 = n4 - n18) < 0 ? - n18 : n18;
                        }
                        arrd[k][j][i] = arrs[n18 + n17] & 65535;
                    }
                }
            }
        }
        catch (Exception var12_10) {
            this.throw_get("XYZ", "Mirror or periodic boundaray conditions", arrd, n, n2, n3);
        }
    }

    public void putBoundedX(int n, int n2, int n3, byte[] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrby.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[n5] = (short)(arrby[i] & 255);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedX(int n, int n2, int n3, short[] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrs.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs2[n5] = (short)(arrs[i] & 65535);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedX(int n, int n2, int n3, float[] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrf.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[n5] = (short)arrf[i];
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedX(int n, int n2, int n3, double[] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n + n4 + n2 * this.nx;
            int n6 = n3;
            int n7 = arrd.length;
            if (n + n7 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n + n7 >= this.nx ? this.nx - n : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[n5] = (short)arrd[i];
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("X", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void putBoundedY(int n, int n2, int n3, byte[] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrby.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[n5] = (short)(arrby[i] & 255);
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedY(int n, int n2, int n3, short[] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrs.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs2[n5] = (short)(arrs[i] & 65535);
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedY(int n, int n2, int n3, float[] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrf.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[n5] = (short)arrf[i];
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedY(int n, int n2, int n3, double[] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n + (n2 + n4) * this.nx;
            int n6 = n3;
            int n7 = arrd.length;
            if (n < 0) {
                return;
            }
            if (n2 + n7 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n8 = n2 + n7 >= this.ny ? this.ny - n2 : n7;
            short[] arrs = (short[])this.data[n3];
            for (int i = n4; i < n8; ++i) {
                arrs[n5] = (short)arrd[i];
                n5 += this.nx;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Y", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void putBoundedZ(int n, int n2, int n3, byte[] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrby.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                ((short[])this.data[n5])[n6] = (short)(arrby[i] & 255);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedZ(int n, int n2, int n3, short[] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrs.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                ((short[])this.data[n5])[n6] = (short)(arrs[i] & 65535);
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedZ(int n, int n2, int n3, float[] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrf.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                ((short[])this.data[n5])[n6] = (short)arrf[i];
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedZ(int n, int n2, int n3, double[] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n3 < 0 ? - n3 : 0;
            int n5 = n3 + n4;
            int n6 = n + n2 * this.nx;
            int n7 = arrd.length;
            if (n < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n7 < 0) {
                return;
            }
            int n8 = n3 + n7 >= this.nz ? this.nz - n3 : n7;
            for (int i = n4; i < n8; ++i) {
                ((short[])this.data[n5])[n6] = (short)arrd[i];
                ++n5;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("Z", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void putBoundedXY(int n, int n2, int n3, byte[][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs[n6] = (short)(arrby[j][i] & 255);
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedXY(int n, int n2, int n3, short[][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs2 = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs2[n6] = (short)(arrs[j][i] & 65535);
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedXY(int n, int n2, int n3, float[][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs[n6] = (short)arrf[j][i];
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedXY(int n, int n2, int n3, double[][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = 0;
            int n7 = n3;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 + n9 < 0) {
                return;
            }
            if (n3 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n2 + n9 >= this.ny ? this.ny - n2 : n9;
            short[] arrs = (short[])this.data[n3];
            for (int i = n5; i < n11; ++i) {
                n6 = n + n4 + (n2 + i) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    arrs[n6] = (short)arrd[j][i];
                    ++n6;
                }
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XY", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void putBoundedXZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)(arrby[j][i] & 255);
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedXZ(int n, int n2, int n3, short[][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)(arrs[j][i] & 65535);
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedXZ(int n, int n2, int n3, float[][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)arrf[j][i];
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedXZ(int n, int n2, int n3, double[][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            if (n + n8 < 0) {
                return;
            }
            if (n2 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n + n8 >= this.nx ? this.nx - n : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + n4 + n2 * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)arrd[j][i];
                    ++n7;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("YZ", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void putBoundedYZ(int n, int n2, int n3, byte[][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrby.length;
            int n9 = arrby[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)(arrby[j][i] & 255);
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedYZ(int n, int n2, int n3, short[][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrs.length;
            int n9 = arrs[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)(arrs[j][i] & 65535);
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedYZ(int n, int n2, int n3, float[][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrf.length;
            int n9 = arrf[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)arrf[j][i];
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedYZ(int n, int n2, int n3, double[][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n2 < 0 ? - n2 : 0;
            int n5 = n3 < 0 ? - n3 : 0;
            int n6 = n3 + n5;
            int n7 = 0;
            int n8 = arrd.length;
            int n9 = arrd[0].length;
            if (n < 0) {
                return;
            }
            if (n2 + n8 < 0) {
                return;
            }
            if (n3 + n9 < 0) {
                return;
            }
            int n10 = n2 + n8 >= this.ny ? this.ny - n2 : n8;
            int n11 = n3 + n9 >= this.nz ? this.nz - n3 : n9;
            for (int i = n5; i < n11; ++i) {
                n7 = n + (n2 + n4) * this.nx;
                for (int j = n4; j < n10; ++j) {
                    ((short[])this.data[n6])[n7] = (short)arrd[j][i];
                    n7 += this.nx;
                }
                ++n6;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XZ", "Bounded check", arrd, n, n2, n3);
        }
    }

    public void putBoundedXYZ(int n, int n2, int n3, byte[][][] arrby) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrby.length;
            int n10 = arrby[0].length;
            int n11 = arrby[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrs[n8] = (short)(arrby[k][j][i] & 255);
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "Bounded check", arrby, n, n2, n3);
        }
    }

    public void putBoundedXYZ(int n, int n2, int n3, short[][][] arrs) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrs.length;
            int n10 = arrs[0].length;
            int n11 = arrs[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs2 = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrs2[n8] = (short)(arrs[k][j][i] & 65535);
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "Bounded check", arrs, n, n2, n3);
        }
    }

    public void putBoundedXYZ(int n, int n2, int n3, float[][][] arrf) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrf.length;
            int n10 = arrf[0].length;
            int n11 = arrf[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrs[n8] = (short)arrf[k][j][i];
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "Bounded check", arrf, n, n2, n3);
        }
    }

    public void putBoundedXYZ(int n, int n2, int n3, double[][][] arrd) {
        try {
            if (n >= this.nx) {
                return;
            }
            if (n2 >= this.ny) {
                return;
            }
            if (n3 >= this.nz) {
                return;
            }
            int n4 = n < 0 ? - n : 0;
            int n5 = n2 < 0 ? - n2 : 0;
            int n6 = n3 < 0 ? - n3 : 0;
            int n7 = n3 + n6;
            int n8 = 0;
            int n9 = arrd.length;
            int n10 = arrd[0].length;
            int n11 = arrd[0][0].length;
            if (n + n9 < 0) {
                return;
            }
            if (n2 + n10 < 0) {
                return;
            }
            if (n3 + n11 < 0) {
                return;
            }
            int n12 = n + n9 >= this.nx ? this.nx - n : n9;
            int n13 = n2 + n10 >= this.ny ? this.ny - n2 : n10;
            int n14 = n3 + n11 >= this.nz ? this.nz - n3 : n11;
            for (int i = n6; i < n14; ++i) {
                short[] arrs = (short[])this.data[n7];
                for (int j = n5; j < n13; ++j) {
                    n8 = n + n4 + (n2 + j) * this.nx;
                    for (int k = n4; k < n12; ++k) {
                        arrs[n8] = (short)arrd[k][j][i];
                        ++n8;
                    }
                }
                ++n7;
            }
        }
        catch (Exception var5_6) {
            this.throw_put("XYZ", "Bounded check", arrd, n, n2, n3);
        }
    }
}

