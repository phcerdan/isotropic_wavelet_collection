/*
 * Decompiled with CFR 0_114.
 *
 * Could not load the following classes:
 *  ij.IJ
 *  ij.ImagePlus
 */
package monogenicwavelettoolbox;

import ij.IJ;
import ij.ImagePlus;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import monogenicwavelettoolbox.FloatArray2D;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.FourierFilters;
import monogenicwavelettoolbox.ImageAdjuster;
import monogenicwavelettoolbox.MonogenicDecompositionTree2D;
import monogenicwavelettoolbox.PointComparable;
import monogenicwavelettoolbox.PropertiesEnum;
import monogenicwavelettoolbox.PropertyDoesNotExistException;
import monogenicwavelettoolbox.ReconstructionEnum;
import monogenicwavelettoolbox.RieszTupel;
import monogenicwavelettoolbox.RieszTupel2D;
import monogenicwavelettoolbox.RieszTupel3D;

public abstract class MonogenicDecompositionTree {
    protected int mNumberOfOctaves;
    protected int mNumberOfChannels;
    protected int mWaveletOrder = 3;
    protected ImagePlus mOriginalImage;
    protected FloatArrayGeneric mImage;
    protected RieszTupel mOriginalRieszTupel;
    protected Map<PointComparable, RieszTupel> mDecompostitionTree;
    protected int mImageDepth;
    protected int mImageHeight;
    protected int mImageWidth;
    protected boolean mDerivativesComputed;
    protected double mImageMeanValue;
    protected int mDimension;
    private static /* synthetic */ int[] $SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum;

    public MonogenicDecompositionTree(ImagePlus imp, int numberOfOctaves, int numberOfChannels, int orderOfWavelet) {
        this.mOriginalImage = imp;
        this.mImageDepth = this.mOriginalImage.getStackSize();
        this.mImageHeight = this.mOriginalImage.getHeight();
        this.mImageWidth = this.mOriginalImage.getWidth();
        this.mNumberOfOctaves = numberOfOctaves;
        this.mNumberOfChannels = numberOfChannels;
        this.mWaveletOrder = orderOfWavelet;
        this.mDecompostitionTree = new TreeMap<PointComparable, RieszTupel>();
    }

    protected void subtractMeanValue() {
        this.mImageMeanValue = this.mImage.getMeanValue();
        this.mImage.add(- this.mImageMeanValue);
    }

    public double getMeanValue() {
        return this.mImageMeanValue;
    }

    // PHC noes
    // Octaves are levels ; n, l
    // Channels are high_sub_bands; m, k
    // waveletFilter List depends on initial Size, and subbands.
    // For each level.
    //  Calculate fft of approx (approx = initial image at l = 0)
    //  For each band, new output.
    //    multiply fft_approx by decomposition multiplier.  2^[ (Dim/2) * (level + band/TotalBands)  ] 
    //    2^[ (Dim/2) * (level)] * 2^[ (Dim/2) * band/TotalBands)]
    //
    //    and multiply by  wavelet filter(band)
    //    (Also do the riesz stuff, but can be docoupled)
    //    inverse_fft, and store (real space image)
    //
    //  multiply fft_approx by low_pass_wavelet filter. Note: wavelet_filter(band == max_band) is equivalent to low filter.
    //  inverse_fft
    //  downsample(new_approx) // you do in the freq domain. no interpolation
    //  downsample(wavelet_filter) // You dont do this. Generate new filter at each level instead.
    //  downsample(riesz)// you dont do this.
    //
    //
    //
    protected void computeDecompositionTree() {
        IJ.showStatus((String)"Decomposing...");
        long startTime = System.currentTimeMillis();
        List<FloatArrayGeneric> rieszFilterList = FourierFilters.getRieszFilter(this.mImage, true);
        List<FloatArrayGeneric> waveletFilterList = FourierFilters.getWaveletFilter(this.mImage, true, this.mNumberOfChannels, this.mWaveletOrder);
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Filter creation for " + this.mImageHeight + "x" + this.mImageWidth + ", Elapsed time: " + elapsedTime + " ms");
        startTime = System.currentTimeMillis();
        FloatArrayGeneric approx = this.mImage.duplicate();
        int n = 0;
        while (n < this.mNumberOfOctaves) {
            IJ.showProgress((int)n, (int)this.mNumberOfOctaves);
            approx.fft();
            int m = 0;
            while (m < this.mNumberOfChannels) {
                Iterator<FloatArrayGeneric> image = approx.duplicate();
                image.multiply(this.getDecompositionMultiplier(n, m));
                Vector<FloatArrayGeneric> rieszTransformsVector = new Vector<FloatArrayGeneric>(3);
                for (FloatArrayGeneric rieszFilter : rieszFilterList) {
                    FloatArrayGeneric r = image.duplicate();
                    r.multiply(rieszFilter).multiply(waveletFilterList.get(m));
                    r.ifft();
                    rieszTransformsVector.add(r);
                }
                image.multiply(waveletFilterList.get(m));
                image.ifft();
                RieszTupel rt = this.mImage instanceof FloatArray2D ? new RieszTupel2D((FloatArray2D)((Object)image), rieszTransformsVector) : new RieszTupel3D((FloatArrayGeneric)((Object)image), rieszTransformsVector);
                this.mDecompostitionTree.put(new PointComparable(n, m), rt);
                ++m;
            }
            approx.multiply(waveletFilterList.get(this.mNumberOfChannels));
            approx.ifft();
            approx.downsampling();
            for (FloatArrayGeneric wf : waveletFilterList) {
                wf.downsampling();
            }
            for (FloatArrayGeneric rf : rieszFilterList) {
                rf.downsampling();
            }
            ++n;
        }
        RieszTupel rtApprox = this.mImage instanceof FloatArray2D ? new RieszTupel2D((FloatArray2D)approx) : new RieszTupel3D(approx);
        this.mDecompostitionTree.put(new PointComparable(this.mNumberOfOctaves, 0), rtApprox);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Decomposition for " + this.mImageHeight + "x" + this.mImageWidth + ", Elapsed time: " + elapsedTime + " ms");
        IJ.showStatus((String)"Decomposition done.");
    }

    public void computeDerivatives() {
        List<List<FloatArrayGeneric>> waveletFilterDerivativeList = FourierFilters.getWaveletFilterDerivative(this.mImage, true, this.mNumberOfChannels, this.mWaveletOrder);
        List<FloatArrayGeneric> rieszFilterList = FourierFilters.getRieszFilter(this.mImage, true);
        ArrayList<FloatArrayGeneric> approxList = new ArrayList<FloatArrayGeneric>();
        FloatArrayGeneric originalImage = this.mOriginalRieszTupel.getImage();
        int i = 0;
        while (i < rieszFilterList.size()) {
            approxList.add(originalImage.duplicate());
            ++i;
        }
        i = 0;
        while (i < this.mNumberOfOctaves) {
            for (FloatArrayGeneric approx : approxList) {
                approx.fft();
            }
            int j = 0;
            while (j < this.mNumberOfChannels) {
                RieszTupel rieszTupel = this.mDecompostitionTree.get(new PointComparable(i, j));
                Vector<FloatArrayGeneric> imageDerivatives = new Vector<FloatArrayGeneric>(3);
                Vector rieszDerivatives = new Vector(3);
                int k = 0;
                while (k < approxList.size()) {
                    List<FloatArrayGeneric> waveletFilterInOneDirectionList = waveletFilterDerivativeList.get(k);
                    FloatArrayGeneric filteredWavelet = ((FloatArrayGeneric)approxList.get(k)).duplicate();
                    filteredWavelet.multiply(waveletFilterInOneDirectionList.get(j));
                    imageDerivatives.add(filteredWavelet);
                    Vector<FloatArrayGeneric> rieszDerivativeInOneDirection = new Vector<FloatArrayGeneric>(3);
                    rieszDerivatives.add(rieszDerivativeInOneDirection);
                    for (FloatArrayGeneric rieszFilter : rieszFilterList) {
                        FloatArrayGeneric filteredRiesz = filteredWavelet.duplicate();
                        filteredRiesz.multiply(rieszFilter);
                        filteredRiesz.ifft();
                        rieszDerivativeInOneDirection.add(filteredRiesz);
                    }
                    filteredWavelet.ifft();
                    ++k;
                }
                rieszTupel.setImageDerivatives(imageDerivatives);
                rieszTupel.setRieszDerivatives(rieszDerivatives);
                ++j;
            }
            int k = 0;
            while (k < approxList.size()) {
                Iterator<FloatArrayGeneric> filter = waveletFilterDerivativeList.get(k).get(this.mNumberOfChannels);
                FloatArrayGeneric approx2 = (FloatArrayGeneric)approxList.get(k);
                approx2.multiply((FloatArrayGeneric)((Object)filter));
                ((FloatArrayGeneric)approxList.get(k)).ifft();
                ((FloatArrayGeneric)approxList.get(k)).downsampling();
                ++k;
            }
            for (List<FloatArrayGeneric> wfList : waveletFilterDerivativeList) {
                for (FloatArrayGeneric wf : wfList) {
                    wf.downsampling();
                }
            }
            for (FloatArrayGeneric rf : rieszFilterList) {
                rf.downsampling();
            }
            ++i;
        }
        this.mDerivativesComputed = true;
    }

    public FloatArrayGeneric getDecompositionItem(int octave, int channel, PropertiesEnum property, boolean fullSize) {
        try {
            if (octave == 0) {
                return this.mOriginalRieszTupel.getProperty(property);
            }
            if (property == PropertiesEnum.FREQUENCY && !this.mDerivativesComputed) {
                this.computeDerivatives();
            }
            if (fullSize && octave > 1 && property != PropertiesEnum.FREQUENCY) {
                FloatArrayGeneric image = this.mDecompostitionTree.get(new PointComparable(octave - 1, channel - 1)).getProperty(PropertiesEnum.ORIGINAL);
                image.fft();
                List<FloatArrayGeneric> waveletFilterList = FourierFilters.getWaveletFilter(image, true, this.mNumberOfChannels, this.mWaveletOrder);
                if (octave == this.mNumberOfOctaves + 1) {
                    image.multiply(waveletFilterList.get(this.mNumberOfChannels));
                } else {
                    image.multiply(waveletFilterList.get(channel - 1));
                }
                image.ifft();
                int n = octave - 1;
                while (n > 0) {
                    image.upsampling();
                    image.multiply(4.0);
                    image.fft();
                    waveletFilterList = FourierFilters.getWaveletFilter(image, true, 1, this.mWaveletOrder);
                    image.multiply(waveletFilterList.get(1));
                    image.ifft();
                    --n;
                }
                RieszTupel rieszTupel = RieszTupel.createRieszTupel(image);
                FloatArrayGeneric imageFullSize = rieszTupel.getProperty(property);
                return imageFullSize;
            }
            FloatArrayGeneric image = this.mDecompostitionTree.get(new PointComparable(octave - 1, channel - 1)).getProperty(property);
            return image;
        }
        catch (PropertyDoesNotExistException e) {
            return null;
        }
    }

    public void showDecompositionItem(int octave, int channel, PropertiesEnum property, boolean fullSize) {
        String title = "";
        if (octave == this.mNumberOfOctaves + 1) {
            channel = 1;
            title = "Approximation image, " + property.getName();
        } else {
            title = "Oct: " + octave + ", Chn: " + channel + ", " + property.getName();
        }
        if (fullSize && property == PropertiesEnum.FREQUENCY) {
            IJ.showMessage((String)"Error", (String)"Frequency is not available in full size");
            return;
        }
        int sizeMultiplikator = fullSize ? 1 : (int)Math.round(Math.pow(2.0, octave - 1));
        FloatArrayGeneric image = this.getDecompositionItem(octave, channel, property, fullSize);
        if (image != null) {
            ImagePlus imp = image.getImagePlusReal(title, this.mImageDepth / sizeMultiplikator, this.mImageHeight / sizeMultiplikator, this.mImageWidth / sizeMultiplikator);
            if (property == PropertiesEnum.ORIENTATION) {
                ImageAdjuster.monochromeToHue(imp);
            }
            imp.show();
        } else {
            IJ.showMessage((String)"Error", (String)"This property is not available at this scale.");
        }
    }

    // LOSSLESS("Lossless"), //1
    // WAVE_REMOVAL("Wave Removal"), //2
    // WAVE_REMOVAL_ADAPTIVE("Wave Removal Adaptive"),/3
    // DESCREENING("Descreening"), //4
    // AMP_PHASE("Amp and Phase"), // 5
    // PHASE_ONLY("Cosine of Phase only"), //6
    // AMP_ONLY("Amplitude only"), //7
    // PHASE_ONLY_STAB("Cosine of Phase only (noise suppressed)"), //8
    // DENOISE("Denoising"), // 9
    // CONTRAST_ENHANCE("Contrast Enhance"); // 10
    public FloatArrayGeneric getReconstruction(ReconstructionEnum method, double threshold, boolean useLowPass) {
        IJ.showStatus((String)"Reconstructing...");
        PointComparable maxAverageIndex = new PointComparable(-1, -1);
        FloatArrayGeneric approx = null;
        double[] maxAmpIndexArr = null;
        double[] maxAmpIndexArr2 = null;
        FloatArrayGeneric maxAmpIndex = null;
        FloatArrayGeneric maxAmpIndex2 = null;
        long startTime = System.currentTimeMillis();
        double sigma_g = 0.0;
        switch (MonogenicDecompositionTree.$SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum()[method.ordinal()]) {
            case 9: {
                sigma_g = this.mDecompostitionTree.get(new PointComparable(0, 0)).getAmplitude().getMeanValue() / (2.0 * Math.sqrt(0.6366197723675814));
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getImage();
                break;
            }
            case 1: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getImage();
                break;
            }
            case 2: 
            case 4: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getImage();
                double maxAverage = 0.0;
                int i = this.mNumberOfOctaves - 1;
                while (i >= 0) {
                    int j = this.mNumberOfChannels - 1;
                    while (j >= 0) {
                        FloatArrayGeneric amp = this.mDecompostitionTree.get(new PointComparable(i, j)).getAmplitude();
                        double average = amp.getMeanValue();
                        if (average > maxAverage) {
                            maxAverageIndex = new PointComparable(i, j);
                            maxAverage = average;
                        }
                        --j;
                    }
                    --i;
                }
                break;
            }
            case 3: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getImage();
                FloatArrayGeneric maxAmp = approx.zerosOfSameSize();
                maxAmpIndex = maxAmp.duplicate();
                maxAmpIndex2 = maxAmpIndex.duplicate();
                FloatArrayGeneric previousAmp = null;
                FloatArrayGeneric nextAmp = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves - 1, this.mNumberOfChannels - 1)).getAmplitude();
                int n = this.mNumberOfOctaves - 1;
                while (n >= 0) {
                    maxAmpIndex.expand();
                    maxAmpIndex2.expand();
                    maxAmp.expand();
                    maxAmpIndexArr = maxAmpIndex.getArray();
                    maxAmpIndexArr2 = maxAmpIndex2.getArray();
                    int m = this.mNumberOfChannels - 1;
                    while (m >= 0) {
                        nextAmp = null;
                        FloatArrayGeneric amp = this.mDecompostitionTree.get(new PointComparable(n, m)).getAmplitude();
                        if (m > 0) {
                            nextAmp = this.mDecompostitionTree.get(new PointComparable(n, m - 1)).getAmplitude();
                        } else if (n > 0) {
                            nextAmp = this.mDecompostitionTree.get(new PointComparable(n - 1, this.mNumberOfChannels - 1)).getAmplitude();
                        }
                        double[] ampArr = amp.getArray();
                        double[] maxAmpArr = maxAmp.getArray();
                        FloatArrayGeneric nextAmpDup = null;
                        if (previousAmp != null && nextAmp != null) {
                            nextAmpDup = nextAmp.duplicate();
                            if (!nextAmpDup.hasEqualDimension(maxAmpIndex)) {
                                nextAmpDup.downsamplingAverage();
                            }
                            if (!previousAmp.hasEqualDimension(maxAmpIndex)) {
                                previousAmp.expand();
                            }
                        }
                        int i = 0;
                        while (i < maxAmpArr.length) {
                            if (ampArr[i] > maxAmpArr[i]) {
                                maxAmpIndexArr[i] = n;
                                maxAmpIndexArr[i + 1] = m;
                                maxAmpArr[i] = ampArr[i];
                                if (previousAmp != null && nextAmpDup != null) {
                                    if (previousAmp.getArray()[i] < nextAmpDup.getArray()[i]) {
                                        if (m > 0) {
                                            maxAmpIndexArr2[i] = n;
                                            maxAmpIndexArr2[i + 1] = m - 1;
                                        } else {
                                            maxAmpIndexArr2[i] = n - 1;
                                            maxAmpIndexArr2[i + 1] = this.mNumberOfChannels - 1;
                                        }
                                    } else if (m < this.mNumberOfChannels - 1) {
                                        maxAmpIndexArr2[i] = n;
                                        maxAmpIndexArr2[i + 1] = m + 1;
                                    } else {
                                        maxAmpIndexArr2[i] = n + 1;
                                        maxAmpIndexArr2[i + 1] = m;
                                    }
                                }
                            }
                            i += 2;
                        }
                        previousAmp = amp;
                        --m;
                    }
                    --n;
                }
                break;
            }
            case 5: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getImage();
                break;
            }
            case 7: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getAmplitude();
                break;
            }
            case 6: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getPhase().cos();
                break;
            }
            case 8: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getPhase().cos();
                break;
            }
            default: {
                approx = this.mDecompostitionTree.get(new PointComparable(this.mNumberOfOctaves, 0)).getImage();
            }
        }
        if (!useLowPass) {
            approx.multiply(0.0);
        }
        int n = this.mNumberOfOctaves - 1;
        while (n >= 0) {
            IJ.showProgress((int)(this.mNumberOfOctaves - n - 1), (int)this.mNumberOfOctaves);
            approx.upsampling();
            if (this instanceof MonogenicDecompositionTree2D) {
                approx.multiply(4.0);
            } else {
                approx.multiply(8.0);
            }
            List<FloatArrayGeneric> waveletFilterList = FourierFilters.getWaveletFilter(approx, true, this.mNumberOfChannels, this.mWaveletOrder);
            approx.fft();
            approx.multiply(waveletFilterList.get(this.mNumberOfChannels));
            int m = this.mNumberOfChannels - 1;
            while (m >= 0) {
                PointComparable index = new PointComparable(n, m);
                FloatArrayGeneric detail = this.mDecompostitionTree.get(index).getImage();
                switch (MonogenicDecompositionTree.$SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum()[method.ordinal()]) {
                    case 1: {
                        break;
                    }
                    case 5: {
                        FloatArrayGeneric phase = this.mDecompostitionTree.get(new PointComparable(n, m)).getPhase();
                        FloatArrayGeneric amp = this.mDecompostitionTree.get(new PointComparable(n, m)).getAmplitude();
                        detail = phase.cos().multiply(amp);
                        break;
                    }
                    case 6: {
                        FloatArrayGeneric phaseOnly = this.mDecompostitionTree.get(new PointComparable(n, m)).getPhase();
                        detail = phaseOnly.cos();
                        break;
                    }
                    case 7: {
                        FloatArrayGeneric ampOnly;
                        detail = ampOnly = this.mDecompostitionTree.get(new PointComparable(n, m)).getAmplitude();
                        break;
                    }
                    case 2: {
                        if (index.compareTo(maxAverageIndex) != 0) break;
                        detail.multiply(0.0);
                        break;
                    }
                    case 3: {
                        double[] detailArr = detail.getArray();
                        FloatArrayGeneric maxAmpIndexDup = maxAmpIndex.duplicate();
                        FloatArrayGeneric maxAmpIndex2Dup = maxAmpIndex2.duplicate();
                        maxAmpIndexDup.downsampling(n);
                        maxAmpIndex2Dup.downsampling(n);
                        int i = 0;
                        while (i < maxAmpIndexDup.getArray().length) {
                            if (maxAmpIndexDup.getArray()[i] == (double)n && maxAmpIndexDup.getArray()[i + 1] == (double)m || maxAmpIndex2Dup.getArray()[i] == (double)n && maxAmpIndex2Dup.getArray()[i + 1] == (double)m) {
                                detailArr[i] = 0.0;
                                detailArr[i + 1] = 0.0;
                            }
                            i += 2;
                        }
                        break;
                    }
                    case 4: {
                        if (index.compareTo(maxAverageIndex) > 0) break;
                        detail.multiply(0.0);
                        break;
                    }
                    case 8: {
                        FloatArrayGeneric phaset = this.mDecompostitionTree.get(new PointComparable(n, m)).getPhase().cos();
                        FloatArrayGeneric ampt = this.mDecompostitionTree.get(new PointComparable(n, m)).getAmplitude();
                        double mu_r = ampt.getMeanValue();
                        double sigma_r = ampt.getStandardDeviation();
                        System.out.println("mu: " + mu_r);
                        System.out.println("sigma: " + sigma_r);
                        double thresh_r_t = mu_r + 2.0 * sigma_r;
                        System.out.println("thresh: " + thresh_r_t);
                        int l = 0;
                        while (l < ampt.getArray().length) {
                            ampt.getArray()[l] = ampt.getArray()[l] < thresh_r_t ? ampt.getArray()[l] / thresh_r_t : 1.0;
                            l += 2;
                        }
                        detail = ampt.multiply(phaset);
                        break;
                    }
                    case 9: {
                        FloatArrayGeneric phased = this.mDecompostitionTree.get(new PointComparable(n, m)).getPhase().cos();
                        FloatArrayGeneric ampd = this.mDecompostitionTree.get(new PointComparable(n, m)).getAmplitude();
                        double mu_d = 2.0 * sigma_g * Math.sqrt(0.6366197723675814);
                        double sigma_d = sigma_g * Math.sqrt(0.4535209105296745);
                        double thresh_d_t = mu_d + sigma_d;
                        if (n == 0) {
                            ampd.hardThreshold(thresh_d_t);
                        } else {
                            ampd.hardThreshold(ampd.getMedianReal());
                        }
                        detail = ampd.multiply(phased);
                        break;
                    }
                    case 10: {
                        FloatArrayGeneric ampc = this.mDecompostitionTree.get(new PointComparable(n, m)).getAmplitude();
                        double mu_c = ampc.getMeanValue();
                        double sigma_c = ampc.getStandardDeviation();
                        double thresh_r_c = mu_c + 2.0 * sigma_c;
                        int l = 0;
                        while (l < ampc.getArray().length) {
                            if (ampc.getArray()[l] >= thresh_r_c) {
                                ampc.getArray()[l] = ampc.getArray()[l] * 10.0;
                            }
                            l += 2;
                        }
                        break block10;
                    }
                }
                detail.fft();
                detail.multiply(waveletFilterList.get(m));
                if (method != ReconstructionEnum.PHASE_ONLY && method != ReconstructionEnum.PHASE_ONLY_STAB) {
                    detail.multiply(this.getReconstructionMultiplier(n, m));
                }
                approx.add(detail);
                --m;
            }
            approx.ifft();
            --n;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        IJ.showStatus((String)"Reconstruction done.");
        System.out.println("Reconstruction " + (Object)((Object)method) + " for" + this.mImageHeight + "x" + this.mImageWidth + ", Elapsed time: " + elapsedTime + " ms");
        double error = approx.duplicate().sub(this.mImage).lInftyNorm();
        System.out.println("l_\\infty error for " + this.mImageHeight + "x" + this.mImageWidth + ":" + error);
        switch (MonogenicDecompositionTree.$SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum()[method.ordinal()]) {
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 9: {
                approx.add(this.mImageMeanValue);
            }
        }
        return approx;
    }

    public void showReconstruction(ReconstructionEnum r) {
        this.showReconstruction(r, 0.0);
    }

    public void showReconstruction(ReconstructionEnum r, double threshold) {
        ImagePlus image = this.getReconstruction(r, threshold, true).getImagePlusReal(String.valueOf(this.mOriginalImage.getTitle()) + ", " + r.getName(), this.mImageDepth, this.mImageHeight, this.mImageWidth);
        image.resetDisplayRange();
        image.show();
    }

    public FloatArrayGeneric getImage() {
        return this.mImage.duplicate();
    }

    private double getReconstructionMultiplier(int octave, int channel) {
        return Math.pow(2.0, ((double)octave + (double)channel / (double)this.mNumberOfChannels) * (double)this.mDimension / 2.0);
        // return Math.pow(2.0, (octave + channel / this.mNumberOfChannels) * Dimension / 2.0);
    }

    private double getDecompositionMultiplier(int octave, int channel) {
        return Math.pow(2.0, (- (double)octave + (double)channel / (double)this.mNumberOfChannels) * (double)this.mDimension / 2.0);
        // return Math.pow(2.0, (- octave + channel / this.mNumberOfChannels) * Dimension / 2.0);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[ReconstructionEnum.values().length];
        try {
            arrn[ReconstructionEnum.AMP_ONLY.ordinal()] = 7;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[ReconstructionEnum.AMP_PHASE.ordinal()] = 5;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[ReconstructionEnum.CONTRAST_ENHANCE.ordinal()] = 10;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[ReconstructionEnum.DENOISE.ordinal()] = 9;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[ReconstructionEnum.DESCREENING.ordinal()] = 4;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[ReconstructionEnum.LOSSLESS.ordinal()] = 1;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[ReconstructionEnum.PHASE_ONLY.ordinal()] = 6;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[ReconstructionEnum.PHASE_ONLY_STAB.ordinal()] = 8;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[ReconstructionEnum.WAVE_REMOVAL.ordinal()] = 2;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[ReconstructionEnum.WAVE_REMOVAL_ADAPTIVE.ordinal()] = 3;
        }
        catch (NoSuchFieldError v10) {}
        $SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum = arrn;
        return $SWITCH_TABLE$monogenicwavelettoolbox$ReconstructionEnum;
    }
}

