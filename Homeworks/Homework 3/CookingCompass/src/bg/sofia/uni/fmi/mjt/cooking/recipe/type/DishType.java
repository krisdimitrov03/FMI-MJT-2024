package bg.sofia.uni.fmi.mjt.cooking.recipe.type;

import java.util.Arrays;

public enum DishType {

    ALCOHOL_COCKTAIL("alcohol cocktail"),
    BISCUITS_AND_COOKIES("biscuits and cookies"),
    BREAD("bread"),
    CEREALS("cereals"),
    CONDIMENTS_AND_SAUCES("condiments and sauces"),
    DESSERTS("desserts"),
    DRINKS("drinks"),
    EGG("egg"),
    ICE_CREAM_AND_CUSTARD("ice cream and custard"),
    MAIN_COURSE("main course"),
    PANCAKE("pancake"),
    PASTA("pasta"),
    PASTRY("pastry"),
    PIES_AND_TARTS("pies and tarts"),
    PIZZA("pizza"),
    PREPS("preps"),
    PRESERVE("preserve"),
    SALAD("salad"),
    SANDWICHES("sandwiches"),
    SEAFOOD("seafood"),
    SIDE_DISH("side dish"),
    SOUP("soup"),
    SPECIAL_OCCASIONS("special occasions"),
    STARTER("starter"),
    SWEETS("sweets"),
    UNKNOWN("unkonown");

    private final String value;

    DishType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static DishType of(String value) {
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
