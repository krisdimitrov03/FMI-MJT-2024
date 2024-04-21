package bg.sofia.uni.fmi.mjt.cooking.recipe.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealTypeTest {

    @Test
    void testFactoryMethodWithNullData() {
        assertEquals(MealType.UNKNOWN, MealType.of(null),
            "MealType should be unknown in case of null input data");
    }

    @Test
    void testFactoryMethodWithUnknownData() {
        assertEquals(MealType.UNKNOWN, MealType.of("mexico"),
            "MealType should be unknown in case of unknown input data");
    }

    @Test
    void testFactoryMethodWithCorrectData() {
        assertEquals(MealType.DINNER, MealType.of("dinner"),
            "MealType should be parsed correctly in case of valid input data");
    }

}
