package bg.sofia.uni.fmi.mjt.cooking.recipe.label;

import java.util.Arrays;

public enum DietLabel {

    BALANCED("balanced"),
    HIGH_FIBER("high-fiber"),
    HIGH_PROTEIN("high-protein"),
    LOW_CARB("low-carb"),
    LOW_FAT("low-fat"),
    LOW_SODIUM("low-sodium"),
    UNKNOWN("unknown");

    private final String value;

    DietLabel(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static DietLabel of(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        return Arrays.stream(values())
            .filter(v -> v.value.equals(value.toLowerCase()) ||
                v.value.replace("-", " ").equals(value.toLowerCase()))
            .findFirst()
            .orElse(UNKNOWN);
    }

}
