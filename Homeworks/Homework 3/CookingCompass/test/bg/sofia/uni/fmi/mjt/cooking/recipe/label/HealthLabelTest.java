package bg.sofia.uni.fmi.mjt.cooking.recipe.label;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthLabelTest {

    @Test
    void testFactoryMethodWithNullData() {
        assertEquals(HealthLabel.UNKNOWN, HealthLabel.of(null));
    }

    @Test
    void testFactoryMethodWithUnknownData() {
        assertEquals(HealthLabel.UNKNOWN, HealthLabel.of("bulgaria"));
    }

    @Test
    void testFactoryMethodWithCorrectData() {
        assertEquals(HealthLabel.EGG_FREE, HealthLabel.of("egg-free"));
    }

}
