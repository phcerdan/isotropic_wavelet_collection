/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum FeatureEnum {
    STABILIZED("stabilized"),
    CONTRAST_ENHANCE("constrast enhance");
    
    private final String name;

    private FeatureEnum(String name, int n2, String string2) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}

