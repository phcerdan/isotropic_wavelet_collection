/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum PropertiesEnum {
    ORIGINAL("Real part"),
    R1("Imag. part 1"),
    R2("Imag. part 2"),
    R3("Imag. part 3"),
    AMPLITUDE("Amplitude"),
    ATTENUATION("Attenutation"),
    PHASE("Phase"),
    COS_PHASE("Cosine of Phase"),
    ORIENTATION("Orientation"),
    FREQUENCY("Frequency");
    
    private final String name;

    private PropertiesEnum(String name, int n2, String string2) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}

