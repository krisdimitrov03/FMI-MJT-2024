package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {

    public static Detail of(String input) {
        String[] tokens = input.split(" [|] ");

        return new Detail(tokens[0], tokens[1]);
    }
    
}
