package bg.sofia.uni.fmi.mjt.cooking.recipe.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DishTypeTest {

    @Test
    void testFactoryMethodWithNullData() {
        assertEquals(DishType.UNKNOWN, DishType.of(null),
            "DishType should be unknown in case of null input data");
    }

    @Test
    void testFactoryMethodWithUnknownData() {
        assertEquals(DishType.UNKNOWN, DishType.of("mexico"),
            "DishType should be unknown in case of unknown input data");
    }

    @Test
    void testFactoryMethodWithCorrectData() {
        assertEquals(DishType.DESSERTS, DishType.of("desserts"),
            "DishType should be parsed correctly in case of valid input data");
    }

}
