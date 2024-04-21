package bg.sofia.uni.fmi.mjt.space.mission;

import java.util.Arrays;
import java.util.Optional;

public enum MissionStatus {

    SUCCESS("Success"),
    FAILURE("Failure"),
    PARTIAL_FAILURE("Partial Failure"),
    PRELAUNCH_FAILURE("Prelaunch Failure");

    private final String value;

    MissionStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static Optional<MissionStatus> get(String value) {
        return Arrays.stream(values())
                .filter(v -> v.toString().equals(value))
                .findFirst();
    }
    
}