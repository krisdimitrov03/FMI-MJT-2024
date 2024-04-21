package bg.sofia.uni.fmi.mjt.space.rocket;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RocketStatusTest {

    @Test
    void testGetForCorrectResult() {
        assertEquals(Optional.of(RocketStatus.STATUS_ACTIVE), RocketStatus.get("StatusActive"));
    }

    @Test
    void testGetWithIncorrectData() {
        assertEquals(Optional.empty(), RocketStatus.get("Wrong data"));
    }

}
