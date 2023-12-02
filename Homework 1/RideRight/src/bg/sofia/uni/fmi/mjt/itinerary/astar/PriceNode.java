package bg.sofia.uni.fmi.mjt.itinerary.astar;

import java.math.BigDecimal;

/**
 * Represents a pair of node and the price of the path from the previous node to it.
 */
public record PriceNode(Node node, BigDecimal price) {
}
