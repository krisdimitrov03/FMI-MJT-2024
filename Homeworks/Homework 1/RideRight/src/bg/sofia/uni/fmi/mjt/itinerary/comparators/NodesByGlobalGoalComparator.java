package bg.sofia.uni.fmi.mjt.itinerary.comparators;

import bg.sofia.uni.fmi.mjt.itinerary.astar.Node;

import java.math.BigDecimal;
import java.util.Comparator;

public class NodesByGlobalGoalComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o1.getGlobalGoal().compareTo(o2.getGlobalGoal());
    }
}
