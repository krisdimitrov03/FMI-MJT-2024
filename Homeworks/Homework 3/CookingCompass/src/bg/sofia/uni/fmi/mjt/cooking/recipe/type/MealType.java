package bg.sofia.uni.fmi.mjt.cooking.recipe.type;

import java.util.Arrays;

public enum MealType {

    BREAKFAST("breakfast"),
    BRUNCH("brunch"),
    LUNCH("lunch"),
    DINNER("dinner"),
    LUNCH_AND_DINNER("lunch/dinner"),
    SNACK("snack"),
    TEATIME("teatime"),
    UNKNOWN("unknown");

    private final String value;

    MealType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static MealType of(String value) {
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
