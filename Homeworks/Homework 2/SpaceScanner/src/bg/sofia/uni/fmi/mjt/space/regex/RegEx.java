package bg.sofia.uni.fmi.mjt.space.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx {

    private static final String MISSION_PATTERN =
            "^(\\d+),(.+),\"(.+)\",\"([a-zA-Z ,0-9]+)\",\"?([^\"]+)\"?,([a-zA-Z]+),\"?([0-9., ]+)?\"?,([a-zA-Z ]+)$";

    private static final String ROCKET_PATTERN = "^(\\d+),\"?([^\"]+)\"?,([^,]+)?,([0-9.]+ m)?$";

    private static Matcher getMatcher(String input, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher match = pattern.matcher(input);

        if (!match.find()) {
            throw new IllegalArgumentException("No match");
        }

        return match;
    }

    public static Matcher getRocketMatcher(String line) {
        return getMatcher(line, ROCKET_PATTERN);
    }

    public static Matcher getMissionMatcher(String line) {
        return getMatcher(line, MISSION_PATTERN);
    }

}
