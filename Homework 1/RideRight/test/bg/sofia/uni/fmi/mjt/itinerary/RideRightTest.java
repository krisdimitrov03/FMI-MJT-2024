package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.SequencedCollection;

import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.BUS;
import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.PLANE;

public class RideRightTest {

    private City sofia;

    private City varna;

    private City burgas;

    private City kardzhali;

    private List<Journey> schedule;

    private ItineraryPlanner rideRight;

    @BeforeEach
    void setup() {
        sofia = new City("Sofia", new Location(0, 2000));
        varna = new City("Varna", new Location(9000, 3000));
        burgas = new City("Burgas", new Location(9000, 1000));
        kardzhali = new City("Kardzhali", new Location(3000, 0));
        City plovdiv = new City("Plovdiv", new Location(4000, 1000));
        City ruse = new City("Ruse", new Location(7000, 4000));
        City blagoevgrad = new City("Blagoevgrad", new Location(0, 1000));
        City tarnovo = new City("Tarnovo", new Location(5000, 3000));

        schedule = List.of(
            new Journey(BUS, sofia, blagoevgrad, new BigDecimal("20")),
            new Journey(BUS, blagoevgrad, sofia, new BigDecimal("20")),
            new Journey(BUS, sofia, plovdiv, new BigDecimal("90")),
            new Journey(BUS, plovdiv, sofia, new BigDecimal("90")),
            new Journey(BUS, plovdiv, kardzhali, new BigDecimal("50")),
            new Journey(BUS, kardzhali, plovdiv, new BigDecimal("50")),
            new Journey(BUS, plovdiv, burgas, new BigDecimal("90")),
            new Journey(BUS, burgas, plovdiv, new BigDecimal("90")),
            new Journey(BUS, burgas, varna, new BigDecimal("60")),
            new Journey(BUS, varna, burgas, new BigDecimal("60")),
            new Journey(BUS, sofia, tarnovo, new BigDecimal("150")),
            new Journey(BUS, tarnovo, sofia, new BigDecimal("150")),
            new Journey(BUS, plovdiv, tarnovo, new BigDecimal("40")),
            new Journey(BUS, tarnovo, plovdiv, new BigDecimal("40")),
            new Journey(BUS, tarnovo, ruse, new BigDecimal("70")),
            new Journey(BUS, ruse, tarnovo, new BigDecimal("70")),
            new Journey(BUS, varna, ruse, new BigDecimal("70")),
            new Journey(BUS, ruse, varna, new BigDecimal("70")),
            new Journey(PLANE, varna, burgas, new BigDecimal("200")),
            new Journey(PLANE, burgas, varna, new BigDecimal("200")),
            new Journey(PLANE, burgas, sofia, new BigDecimal("150")),
            new Journey(PLANE, sofia, burgas, new BigDecimal("250")),
            new Journey(PLANE, varna, sofia, new BigDecimal("290")),
            new Journey(PLANE, sofia, varna, new BigDecimal("300"))
        );

        rideRight = new RideRight(schedule);
    }

    @Test
    void findCheapestPathShouldThrowIfCityNotKnown() {
        City from = new City("manhatan", new Location(200, 34));

        assertThrows(CityNotKnownException.class, () -> rideRight.findCheapestPath(from, sofia, true));
        assertThrows(CityNotKnownException.class, () -> rideRight.findCheapestPath(kardzhali, from, true));
    }

    @Test
    void findCheapestPathShouldThrowIfNoPathFound() {
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(varna, kardzhali, false));
    }

    @Test
    void findCheapestPathShouldReturnCorrectResultWhenMultiple()
        throws CityNotKnownException, NoPathToDestinationException {
        SequencedCollection<Journey> expected = List.of(schedule.get(9), schedule.get(7), schedule.get(4));

        assertIterableEquals(expected, rideRight.findCheapestPath(varna, kardzhali, true));
    }

    @Test
    void findCheapestPathShouldReturnCorrectResultWhenSingle()
        throws CityNotKnownException, NoPathToDestinationException {
        SequencedCollection<Journey> expected = List.of(schedule.get(9));

        assertIterableEquals(expected, rideRight.findCheapestPath(varna, burgas, false));
    }
}
