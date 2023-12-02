package bg.sofia.uni.fmi.mjt.itinerary.astar;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a node in a graph.
 */
public class Node {

    private String id;

    private int x;

    private int y;

    private final Set<PriceNode> neighbours;

    private PriceNode parent = null;

    private BigDecimal globalGoal = BigDecimal.valueOf(Double.MAX_VALUE);

    private BigDecimal localGoal = BigDecimal.valueOf(Double.MAX_VALUE);

    public Node(String id, int x, int y) {
        setId(id);
        setLocation(x, y);

        this.neighbours = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public Set<PriceNode> getNeighbours() {
        return neighbours;
    }

    public PriceNode getParent() {
        return parent;
    }

    public BigDecimal getGlobalGoal() {
        return globalGoal;
    }

    public BigDecimal getLocalGoal() {
        return localGoal;
    }

    public double getDistanceTo(Node other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setParent(Node parentNode, BigDecimal priceFromParent) {
        this.parent = new PriceNode(parentNode, priceFromParent);
    }

    public void setGlobalGoal(BigDecimal globalGoal) {
        this.globalGoal = globalGoal;
    }

    public void setLocalGoal(BigDecimal localGoal) {
        this.localGoal = localGoal;
    }
}