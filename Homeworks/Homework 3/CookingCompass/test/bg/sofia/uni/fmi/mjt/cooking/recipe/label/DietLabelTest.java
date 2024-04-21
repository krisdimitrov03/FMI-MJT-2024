package bg.sofia.uni.fmi.mjt.cooking.recipe.label;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DietLabelTest {

    @Test
    void testFactoryMethodWithNullData() {
        assertEquals(DietLabel.UNKNOWN, DietLabel.of(null),
            "DietLabel should be unknown in case of null input data");
    }

    @Test
    void testFactoryMethodWithUnknownData() {
        assertEquals(DietLabel.UNKNOWN, DietLabel.of("not-balanced"),
            "DietLabel should be unknown in case of unknown input data");
    }

    @Test
    void testFactoryMethodWithCorrectData() {
        assertEquals(DietLabel.HIGH_PROTEIN, DietLabel.of("high-protein"),
            "DietLabel should be parsed correctly in case of valid input data");
    }

}
