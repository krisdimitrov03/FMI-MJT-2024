package bg.sofia.uni.fmi.mjt.cooking.recipe.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CuisineTypeTest {

    @Test
    void testFactoryMethodWithNullData() {
        assertEquals(CuisineType.UNKNOWN, CuisineType.of(null),
            "CuisineType should be unknown in case of null input data");
    }

    @Test
    void testFactoryMethodWithUnknownData() {
        assertEquals(CuisineType.UNKNOWN, CuisineType.of("mexico"),
            "CuisineType should be unknown in case of unknown input data");
    }

    @Test
    void testFactoryMethodWithCorrectData() {
        assertEquals(CuisineType.ASIAN, CuisineType.of("asian"),
            "CuisineType should be parsed correctly in case of valid input data");
    }

}
