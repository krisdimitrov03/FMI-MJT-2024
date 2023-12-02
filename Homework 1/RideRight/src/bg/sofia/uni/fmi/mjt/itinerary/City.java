package bg.sofia.uni.fmi.mjt.itinerary;

public record City(String name, Location location) implements Comparable<City> {

    @Override
    public int compareTo(City o) {
        return this.name().compareTo(o.name());
    }
}
