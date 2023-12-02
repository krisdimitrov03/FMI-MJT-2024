package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) {

    public BigDecimal getPriceWithTax() {
        return (BigDecimal.valueOf(1).add(vehicleType.getGreenTax())).multiply(price);
    }
}
