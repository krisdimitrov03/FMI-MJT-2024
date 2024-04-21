package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MJTSpaceScannerTest {

    private static final String missionsSource = """
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
            2,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
            3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Failure
            4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Atlas V 541 | Perseverance,StatusActive,"145.0 ",Success
            5,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Sat Jul 25, 2020","Long March 4B | Ziyuan-3 03, Apocalypse-10 & NJU-HKU 1",StatusActive,"64.68 ",Success
            6,Roscosmos,"Site 31/6, Baikonur Cosmodrome, Kazakhstan","Thu Jul 23, 2020",Soyuz 2.1a | Progress MS-15,StatusActive,"48.5 ",Partial Failure
            7,JAXA,"LC-101, Wenchang Satellite Launch Center, China","Thu Jul 23, 2020",Long March 5 | Tianwen-1,StatusActive,,Success
            8,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Jul 20, 2020",Falcon 9 Block 5 | ANASIS-II,StatusActive,"50.0 ",Success
            9,JAXA,"LA-Y1, Tanegashima Space Center, Japan","Sun Jul 19, 2020",H-IIA 202 | Hope Mars Mission,StatusActive,"90.0 ",Success
            28,VKS RF,"Site 43/4, Plesetsk Cosmodrome, Russia","Fri May 22, 2020",Soyuz 2.1b/Fregat-M | Cosmos 2546,StatusActive,,Prelaunch Failure""";

    private static final String rocketsSource = """
            ,Name,Wiki,Rocket Height
            0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
            1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
            2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m
            3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m
            4,Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),23.0 m
            5,H-IIA 202,https://en.wikipedia.org/wiki/Vector-H,18.3 m
            6,Vector-R,https://en.wikipedia.org/wiki/Vector-R,13.0 m
            7,Vega,https://en.wikipedia.org/wiki/Vega_(rocket),29.9 m
            16,Proton-M/Briz-M,https://en.wikipedia.org/wiki/Vostok-2M,
            62,Atlas V 541,,""";

    private SpaceScannerAPI spaceScanner;

    private List<Mission> allMissions;

    private List<Rocket> allRockets;

    @BeforeEach
    void setupTests() throws NoSuchAlgorithmException {
        Reader missionsReader = new StringReader(missionsSource);
        Reader rocketsReader = new StringReader(rocketsSource);

        SecretKey key = Rijndael.generateSecretKey();

        spaceScanner = new MJTSpaceScanner(missionsReader, rocketsReader, key);

        allMissions = Arrays.stream(missionsSource.split("\n"))
                .skip(1)
                .map(Mission::of)
                .toList();

        allRockets = Arrays.stream(rocketsSource.split("\n"))
                .skip(1)
                .map(Rocket::of)
                .toList();
    }

    @Test
    void testGetAllMissionsForCorrectResult() {
        assertIterableEquals(allMissions, spaceScanner.getAllMissions());
    }

    @Test
    void testGetAllMissionsWithNullData() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner.getAllMissions(null));
    }

    @Test
    void testGetAllMissionsWithCorrectMissionStatus() {
        List<Mission> expected = List.of(
                allMissions.get(0),
                allMissions.get(1),
                allMissions.get(2),
                allMissions.get(4),
                allMissions.get(5),
                allMissions.get(7),
                allMissions.get(8),
                allMissions.get(9));

        assertIterableEquals(expected, spaceScanner.getAllMissions(MissionStatus.SUCCESS));
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null,
                        LocalDate.of(2020, 8, 23)));

        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(
                        LocalDate.of(2020, 8, 23),
                        null));

        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null, null));
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsForCorrectResult() {
        LocalDate from = LocalDate.of(2020, 7, 1);
        LocalDate to = LocalDate.of(2020, 8, 31);

        assertEquals("SpaceX", spaceScanner.getCompanyWithMostSuccessfulMissions(from, to));
    }

    @Test
    void testGetMissionsPerCountryForCorrectResult() {
        Map<String, Collection<Mission>> expected = Map.of(
                "USA", List.of(allMissions.get(0), allMissions.get(2), allMissions.get(4), allMissions.get(8)),
                "China", List.of(allMissions.get(1), allMissions.get(5), allMissions.get(7)),
                "Kazakhstan", List.of(allMissions.get(3), allMissions.get(6)),
                "Japan", List.of(allMissions.get(9)),
                "Russia", List.of(allMissions.get(10))
        );

        assertEquals(expected, spaceScanner.getMissionsPerCountry());
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner.getTopNLeastExpensiveMissions(-1, null, null));
    }

    @Test
    void testGetTopNLeastExpensiveMissionsForCorrectResult() {
        List<Mission> expected = List.of(
                allMissions.get(1),
                allMissions.get(0),
                allMissions.get(8)
        );

        assertIterableEquals(expected,
                spaceScanner.getTopNLeastExpensiveMissions(3, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE));
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompanyForCorrectResult() {
        Map<String, String> expected = Map.of(
                "SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA",
                "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
                "Roscosmos", "Site 31/6, Baikonur Cosmodrome, Kazakhstan",
                "ULA", "SLC-41, Cape Canaveral AFS, Florida, USA",
                "JAXA", "LA-Y1, Tanegashima Space Center, Japan",
                "VKS RF", "Site 43/4, Plesetsk Cosmodrome, Russia"
        );

        assertEquals(expected, spaceScanner.getMostDesiredLocationForMissionsPerCompany());
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithIncorrectData() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(null, null));

        LocalDate from = LocalDate.of(2020, 8, 23);
        LocalDate to = LocalDate.of(2020, 8, 12);

        assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(from, to));
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyForCorrectResult() {
        LocalDate from = LocalDate.of(2020, 5, 1);
        LocalDate to = LocalDate.of(2020, 8, 31);

        Map<String, String> expected = Map.of(
                "SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA",
                "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
                "Roscosmos", "",
                "ULA", "SLC-41, Cape Canaveral AFS, Florida, USA",
                "JAXA", "LA-Y1, Tanegashima Space Center, Japan",
                "VKS RF", ""
        );

        assertEquals(expected, spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(from, to));
    }

    @Test
    void testGetAllRocketsForCorrectResult() {
        assertIterableEquals(allRockets, spaceScanner.getAllRockets());
    }

    @Test
    void testGetTopNTallestRocketsWithInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner.getTopNTallestRockets(-1));
    }

    @Test
    void testGetTopNTallestRocketsForCorrectResult() {
        List<Rocket> expected = List.of(
                allRockets.get(0),
                allRockets.get(1),
                allRockets.get(3)
        );

        assertIterableEquals(expected, spaceScanner.getTopNTallestRockets(3));
    }

    @Test
    void testWikiPageForRocketForCorrectResult() {
        Map<String, Optional<String>> expected = allRockets.stream()
                .collect(Collectors.toMap(Rocket::name, Rocket::wiki));

        assertEquals(expected, spaceScanner.getWikiPageForRocket());
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner
                .getWikiPagesForRocketsUsedInMostExpensiveMissions(-1, null, null));
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsForCorrectResult() {
        List<String> expected = List.of(
                "",
                "https://en.wikipedia.org/wiki/Vector-H",
                "https://en.wikipedia.org/wiki/Vostok-2M"
        );

        assertIterableEquals(expected, spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(3, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE));
    }

    @Test
    void testSaveMostReliableRocketWithIncorrectData() {
        LocalDate from = LocalDate.of(2020, 5, 1);
        LocalDate to = LocalDate.of(2020, 8, 31);

        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.saveMostReliableRocket(null, null, null));

        assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(1), to, from));
    }

}
