package bg.sofia.uni.fmi.mjt.space.mission;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissionStatusTest {

    @Test
    void testGetForCorrectResult() {
        assertEquals(Optional.of(MissionStatus.SUCCESS), MissionStatus.get("Success"));
    }

    @Test
    void testGetWithIncorrectData() {
        assertEquals(Optional.empty(), MissionStatus.get("Wrong data"));
    }

}
