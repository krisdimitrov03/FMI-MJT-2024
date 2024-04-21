package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MissionTest {

    public static final String line =
            "0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\"" +
                    ",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky" +
                    ",StatusActive,\"50.0 \",Success";

    @Test
    void testFactoryMethodWithCostAvailable() {
        Mission result = Mission.of(line);

        assertEquals("0", result.id());
        assertEquals("SpaceX", result.company());
        assertEquals("LC-39A, Kennedy Space Center, Florida, USA", result.location());
        assertEquals(LocalDate.of(2020, 8, 7), result.date());
        assertEquals("Falcon 9 Block 5", result.detail().rocketName());
        assertEquals("Starlink V1 L9 & BlackSky", result.detail().payload());
        assertEquals(RocketStatus.STATUS_ACTIVE, result.rocketStatus());
        assertEquals(Optional.of(50.0), result.cost());
        assertEquals(MissionStatus.SUCCESS, result.missionStatus());
    }

    @Test
    void testFactoryMethodWithoutCostAvailable() {
        String currentLine = "28,VKS RF,\"Site 43/4, Plesetsk Cosmodrome, Russia\",\"Fri May 22, 2020\",Soyuz 2.1b/Fregat-M | Cosmos 2546,StatusActive,,Success";

        Mission result = Mission.of(currentLine);

        assertEquals("28", result.id());
        assertEquals("VKS RF", result.company());
        assertEquals("Site 43/4, Plesetsk Cosmodrome, Russia", result.location());
        assertEquals(LocalDate.of(2020, 5, 22), result.date());
        assertEquals("Soyuz 2.1b/Fregat-M", result.detail().rocketName());
        assertEquals("Cosmos 2546", result.detail().payload());
        assertEquals(RocketStatus.STATUS_ACTIVE, result.rocketStatus());
        assertEquals(Optional.empty(), result.cost());
        assertEquals(MissionStatus.SUCCESS, result.missionStatus());
    }

    @Test
    void testGetCountryForCorrectResult() {
        Mission result = Mission.of(line);

        assertEquals("USA", result.getCountry());
    }

    @Test
    void testIsSuccessfulForCorrectResult() {
        Mission result = Mission.of(line);

        assertTrue(result.isSuccessful());
    }

}
