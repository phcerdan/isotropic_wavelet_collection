/*
 * Decompiled with CFR 0_118.
 */
package steerabletools;

import additionaluserinterface.WalkBar;
import imageware.Builder;
import imageware.ImageWare;

public class StructureTensor {
    private ImageWare orientation;
    private ImageWare energy;
    private ImageWare coherency;
    private WalkBar walk;

    public StructureTensor(WalkBar walk) {
        this.walk = walk;
    }

    public void compute(ImageWare gradx, ImageWare grady, double sigma, double epsilon) {
        int nx = gradx.getWidth();
        int ny = gradx.getHeight();
        int nz = gradx.getSizeZ();
        if (this.walk != null) {
            this.walk.reset();
        }
        this.coherency = Builder.create(nx, ny, nz, 3);
        if (this.walk != null) {
            this.walk.progress("Structure Tensor", 10);
        }
        this.orientation = Builder.create(nx, ny, nz, 3);
        if (this.walk != null) {
            this.walk.progress("Structure Tensor", 20);
        }
        this.energy = Builder.create(nx, ny, nz, 3);
        if (this.walk != null) {
            this.walk.progress("Structure Tensor", 30);
        }
        ImageWare dxx = Builder.create(nx, ny, 1, 3);
        ImageWare dyy = Builder.create(nx, ny, 1, 3);
        ImageWare dxy = Builder.create(nx, ny, 1, 3);
        double xx = 0.0;
        double yy = 0.0;
        double xy = 0.0;
        for (int z = 0; z < nz; ++z) {
            if (this.walk != null) {
                this.walk.progress("Structure Tensor", 30.0 + (double)z * 70.0 / (double)nz);
            }
            gradx.getXY(0, 0, z, dxx);
            gradx.getXY(0, 0, z, dxy);
            grady.getXY(0, 0, z, dyy);
            dxy.multiply(dyy);
            dxx.multiply(dxx);
            dyy.multiply(dyy);
            dxx.smoothGaussian(sigma, sigma, 0.0);
            dyy.smoothGaussian(sigma, sigma, 0.0);
            dxy.smoothGaussian(sigma, sigma, 0.0);
            for (int y = 0; y < ny; ++y) {
                for (int x = 0; x < nx; ++x) {
                    xx = dxx.getPixel(x, y, 0);
                    yy = dyy.getPixel(x, y, 0);
                    xy = dxy.getPixel(x, y, 0);
                    this.coherency.putPixel(x, y, z, this.computeCoherency(xx, yy, xy, epsilon));
                    this.orientation.putPixel(x, y, z, this.computeOrientation(xx, yy, xy));
                    this.energy.putPixel(x, y, z, 0.5 * (xx + yy + Math.sqrt((yy - xx) * (yy - xx) + 4.0 * xy * xy)));
                }
            }
        }
        if (this.walk != null) {
            this.walk.finish("Structure Tensor");
        }
    }

    private double computeCoherency(double xx, double yy, double xy, double epsilon) {
        double coherency = Math.sqrt((yy - xx) * (yy - xx) + 4.0 * xy * xy) / (xx + yy + epsilon);
        return coherency;
    }

    private double computeOrientation(double xx, double yy, double xy) {
        return 0.5 * Math.atan2(2.0 * xy, yy - xx);
    }

    public ImageWare getOrientation() {
        return this.orientation;
    }

    public ImageWare getEnergy() {
        return this.energy;
    }

    public ImageWare getCoherency() {
        return this.coherency;
    }
}

