package bg.sofia.uni.fmi.mjt.itinerary.comparators;

import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.util.Comparator;

public class JourneysByPriceComparator implements Comparator<Journey> {

    @Override
    public int compare(Journey o1, Journey o2) {
        return o1.getPriceWithTax().compareTo(o2.getPriceWithTax());
    }
}
