package bg.sofia.uni.fmi.mjt.space.rocket;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RocketTest {

    @Test
    void testFactoryMethodWithFullLine() {
        String line = "0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m";

        Rocket result = Rocket.of(line);

        assertEquals("0", result.id());
        assertEquals("Tsyklon-3", result.name());
        assertEquals(Optional.of("https://en.wikipedia.org/wiki/Tsyklon-3"), result.wiki());
        assertEquals(Optional.of(39.0), result.height());
    }

    @Test
    void testFactoryMethodWithPartiallyFullLine() {
        String line = "62,Atlas-E/F Burner,,";

        Rocket result = Rocket.of(line);

        assertEquals("62", result.id());
        assertEquals("Atlas-E/F Burner", result.name());
        assertEquals(Optional.empty(), result.wiki());
        assertEquals(Optional.empty(), result.height());
    }

}
