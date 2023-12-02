package bg.sofia.uni.fmi.mjt.itinerary.astar;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.comparators.NodesByGlobalGoalComparator;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.Set;

/**
 * Represents a graph with a set of nodes and a start node useful for A* algorithm
 */
public class Graph {

    private final Node start;

    private final Set<Node> nodes;

    public Graph(Node start, Collection<Node> nodes) {
        this.start = start;
        this.nodes = new LinkedHashSet<>(nodes);
    }

    /**
     * Finds the cheapestPath from start node to the targetNode
     *
     * @param targetId id of the target node
     * @return targetNode - the end of the path
     * @throws CityNotKnownException if targetId is not a valid city name
     */
    public Node solveAStar(String targetId) throws CityNotKnownException {
        Node targetNode = getNodeById(targetId);

        if (targetNode == null) {
            throw new CityNotKnownException("City not known");
        }

        start.setLocalGoal(BigDecimal.valueOf(0));
        start.setGlobalGoal(BigDecimal.valueOf(start.getDistanceTo(targetNode)));

        List<Node> openedNodes = new LinkedList<>();
        openedNodes.add(start);

        Comparator<Node> comparator = new NodesByGlobalGoalComparator();

        Node currentNode = start;

        while (!openedNodes.isEmpty()) {
            for (PriceNode nbr : currentNode.getNeighbours()) {
                BigDecimal currentLocalGoal = currentNode.getLocalGoal();
                BigDecimal bigDecimalDistance = BigDecimal.valueOf(currentNode.getDistanceTo(nbr.node()));
                BigDecimal price = nbr.price();

                BigDecimal potentialLocalGoal = currentLocalGoal.add(bigDecimalDistance).add(price);

                if (potentialLocalGoal.compareTo(nbr.node().getLocalGoal()) < 0) {
                    BigDecimal bigDecimalHeuristic = BigDecimal.valueOf(nbr.node().getDistanceTo(targetNode));
                    BigDecimal globalGoal = nbr.node().getLocalGoal().add(bigDecimalHeuristic);

                    nbr.node().setLocalGoal(potentialLocalGoal);
                    nbr.node().setGlobalGoal(globalGoal);
                    nbr.node().setParent(currentNode, nbr.price());

                    if (!openedNodes.contains(nbr.node())) {
                        openedNodes.add(nbr.node());
                    }
                }
            }

            openedNodes.remove(currentNode);
            openedNodes.sort(comparator);

            if (!openedNodes.isEmpty()) {
                currentNode = openedNodes.getFirst();
            }
        }

        return targetNode;
    }

    /**
     * Creates graph from journeys with start node
     *
     * @param start    the start node of the graph
     * @param journeys collection of all available journeys
     * @return instance of Graph
     */
    public static Graph createFromJourneys(City start, Collection<Journey> journeys) {
        Node startNode = new Node(start.name(), start.location().x(), start.location().y());

        SequencedMap<String, Node> nodes = new LinkedHashMap<>();
        nodes.put(start.name(), startNode);

        for (Journey journey : journeys) {
            Node from;
            Node to;

            if (nodes.containsKey(journey.from().name())) {
                from = nodes.get(journey.from().name());
            } else {
                from = new Node(journey.from().name(), journey.from().location().x(), journey.from().location().y());
                nodes.put(journey.from().name(), from);
            }

            if (nodes.containsKey(journey.to().name())) {
                to = nodes.get(journey.to().name());
            } else {
                to = new Node(journey.to().name(), journey.to().location().x(), journey.to().location().y());
                nodes.put(journey.to().name(), to);
            }

            from.getNeighbours().add(new PriceNode(to, journey.getPriceWithTax()));
        }

        return new Graph(startNode, nodes.values());
    }

    /**
     * Converts a path of nodes to a collection of journeys
     *
     * @param pathEnd           the end node of the path
     * @param availableJourneys all available journeys
     * @return a sequenced collection of journeys
     */
    public static SequencedCollection<Journey> convertPathToJourneys(Node pathEnd, Set<Journey> availableJourneys) {
        SequencedCollection<Journey> result = new LinkedHashSet<>();

        Node current = pathEnd;

        while (current.getParent() != null) {
            for (Journey journey : availableJourneys) {
                String fromCityName = journey.from().name();
                String toCityName = journey.to().name();

                boolean fromNameEquals = fromCityName.equals(current.getParent().node().getId());
                boolean toNameEquals = toCityName.equals(current.getId());
                boolean priceEquals = journey.getPriceWithTax().equals(current.getParent().price());

                boolean corresponds = fromNameEquals && toNameEquals && priceEquals;

                if (corresponds) {
                    result.add(journey);
                    break;
                }
            }

            current = current.getParent().node();
        }

        return result.reversed();
    }

    private Node getNodeById(String id) {
        for (Node node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }

        return null;
    }
}
