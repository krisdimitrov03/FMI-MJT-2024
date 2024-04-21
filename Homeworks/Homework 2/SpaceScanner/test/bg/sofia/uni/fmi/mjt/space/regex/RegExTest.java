package bg.sofia.uni.fmi.mjt.space.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegExTest {

    @Test
    void testGetRocketMatcherWithIncorrectLine() {
        assertThrows(IllegalArgumentException.class, () -> RegEx.getRocketMatcher("Incorrect line"));
    }

}
