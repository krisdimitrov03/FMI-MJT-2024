package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.astar.Graph;
import bg.sofia.uni.fmi.mjt.itinerary.astar.Node;
import bg.sofia.uni.fmi.mjt.itinerary.comparators.JourneysByPriceComparator;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.Set;

public class RideRight implements ItineraryPlanner {

    private final Set<Journey> schedule;

    public RideRight(List<Journey> schedule) {
        this.schedule = new HashSet<>();
        this.schedule.addAll(schedule);
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        if (isCityUnknown(start) || isCityUnknown(destination)) {
            throw new CityNotKnownException("City not known");
        }

        if (!allowTransfer) {
            return findNoTransferPath(start, destination);
        }

        Graph graph = Graph.createFromJourneys(start, schedule);
        Node targetNode = graph.solveAStar(destination.name());

        return Graph.convertPathToJourneys(targetNode, new LinkedHashSet<>(schedule));
    }

    private SequencedCollection<Journey> findNoTransferPath(City start, City destination)
        throws NoPathToDestinationException {
        List<Journey> scheduleCopy = new LinkedList<>(schedule);
        scheduleCopy.sort(new JourneysByPriceComparator());

        Journey result = findJourney(start, destination, scheduleCopy);

        if (result == null) {
            throw new NoPathToDestinationException(
                "No direct path from " + start.name() + " to " + destination.name() + " found");
        }

        SequencedCollection<Journey> singletonPath = new LinkedHashSet<>();
        singletonPath.add(result);

        return singletonPath;
    }

    private Journey findJourney(City start, City dest, List<Journey> schedule) {
        for (Journey j : schedule) {
            if (j.from().equals(start) && j.to().equals(dest)) {
                return j;
            }
        }

        return null;
    }

    private boolean isCityUnknown(City city) {
        for (Journey journey : schedule) {
            if (journey.from().equals(city) || journey.to().equals(city)) {
                return false;
            }
        }

        return true;
    }
}
