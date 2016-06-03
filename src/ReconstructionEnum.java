/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum ReconstructionEnum {
    LOSSLESS("Lossless"),
    WAVE_REMOVAL("Wave Removal"),
    WAVE_REMOVAL_ADAPTIVE("Wave Removal Adaptive"),
    DESCREENING("Descreening"),
    AMP_PHASE("Amp and Phase"),
    PHASE_ONLY("Cosine of Phase only"),
    AMP_ONLY("Amplitude only"),
    PHASE_ONLY_STAB("Cosine of Phase only (noise suppressed)"),
    DENOISE("Denoising"),
    CONTRAST_ENHANCE("Contrast Enhance");
    
    private final String name;

    private ReconstructionEnum(String name, int n2, String string2) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}

