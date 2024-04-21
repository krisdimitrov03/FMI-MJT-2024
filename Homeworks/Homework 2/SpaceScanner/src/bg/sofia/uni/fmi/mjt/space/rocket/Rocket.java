package bg.sofia.uni.fmi.mjt.space.rocket;

import bg.sofia.uni.fmi.mjt.space.regex.RegEx;

import java.util.Optional;
import java.util.regex.Matcher;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {

    private static final int ID_INDEX = 1;

    private static final int NAME_INDEX = 2;

    private static final int WIKI_INDEX = 3;

    private static final int HEIGHT_INDEX = 4;

    public static Rocket of(String line) {
        Matcher match = RegEx.getRocketMatcher(line);

        String id = match.group(ID_INDEX);
        String name = match.group(NAME_INDEX);
        Optional<String> wiki;
        Optional<Double> height;

        if (match.group(WIKI_INDEX) == null || match.group(WIKI_INDEX).isEmpty()) {
            wiki = Optional.empty();
        } else {
            wiki = Optional.of(match.group(WIKI_INDEX));
        }

        if (match.group(HEIGHT_INDEX) == null || match.group(HEIGHT_INDEX).isEmpty()) {
            height = Optional.empty();
        } else {
            height = Optional.of(Double.parseDouble(match.group(HEIGHT_INDEX).replace(" m", "")));
        }

        return new Rocket(id, name, wiki, height);
    }
    
}