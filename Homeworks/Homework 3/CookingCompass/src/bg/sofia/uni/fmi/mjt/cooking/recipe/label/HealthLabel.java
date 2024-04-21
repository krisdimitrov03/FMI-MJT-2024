package bg.sofia.uni.fmi.mjt.cooking.recipe.label;

import java.util.Arrays;

public enum HealthLabel {

    ALCOHOL_COCKTAIL("alcohol-cocktail"),
    ALCOHOL_FREE("alcohol-free"),
    CELERY_FREE("celery-free"),
    CRUSTACEAN_FREE("crustacean-free"),
    DAIRY_FREE("dairy-free"),
    DASH("dash"),
    EGG_FREE("egg-free"),
    FISH_FREE("fish-free"),
    FODMAP_FREE("fodmap-free"),
    GLUTEN_FREE("gluten-free"),
    IMMUNO_SUPPORTIVE("immuno-supportive"),
    KETO_FRIENDLY("keto-friendly"),
    KIDNEY_FRIENDLY("kidney-friendly"),
    KOSHER("kosher"),
    LOW_FAT_ABS("low-fat-abs"),
    LOW_POTASSIUM("low-potassium"),
    LOW_SUGAR("low-sugar"),
    LUPINE_FREE("lupine-free"),
    MEDITERRANEAN("mediterranean"),
    MOLLUSK_FREE("mollusk-free"),
    MUSTARD_FREE("mustard-free"),
    NO_OIL_ADDED("no-oil-added"),
    PALEO("paleo"),
    PEANUT_FREE("peanut-free"),
    PESCATARIAN("pescatarian"),
    PORK_FREE("pork-free"),
    RED_MEAT_FREE("red-meat-free"),
    SESAME_FREE("sesame-free"),
    SHELLFISH_FREE("shellfish-free"),
    SOY_FREE("soy-free"),
    SUGAR_CONSCIOUS("sugar-conscious"),
    SULFITE_FREE("sulfite-free"),
    TREE_NUT_FREE("tree-nut-free"),
    VEGAN("vegan"),
    VEGETARIAN("vegetarian"),
    WHEAT_FREE("wheat-free"),
    UNKNOWN("unknown");

    private final String value;

    HealthLabel(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static HealthLabel of(String value) {
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
