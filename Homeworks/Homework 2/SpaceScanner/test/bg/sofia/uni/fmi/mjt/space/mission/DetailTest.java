package bg.sofia.uni.fmi.mjt.space.mission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetailTest {

    @Test
    void testFactoryMethodForCorrectResult() {
        String detailLine = "Long March 5 | Tianwen-1";

        Detail detail = Detail.of(detailLine);

        assertEquals("Long March 5", detail.rocketName());
        assertEquals("Tianwen-1", detail.payload());
    }
    
}
