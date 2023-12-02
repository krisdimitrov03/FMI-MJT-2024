package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        double dX = other.longitude - this.longitude;
        double dY = other.latitude - this.latitude;

        return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
    }
}