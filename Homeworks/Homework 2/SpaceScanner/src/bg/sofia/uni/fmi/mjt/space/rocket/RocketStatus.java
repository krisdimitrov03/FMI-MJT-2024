package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Arrays;
import java.util.Optional;

public enum RocketStatus {

    STATUS_RETIRED("StatusRetired"),
    STATUS_ACTIVE("StatusActive");

    private final String value;

    RocketStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static Optional<RocketStatus> get(String value) {
        return Arrays.stream(values())
                .filter(v -> v.toString().equals(value))
                .findFirst();
    }

}