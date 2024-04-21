package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.regex.RegEx;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;

public record Mission(String id, String company, String location, LocalDate date, Detail detail,
                      RocketStatus rocketStatus, Optional<Double> cost, MissionStatus missionStatus) {

    private static final int ID_INDEX = 1;

    private static final int COMPANY_INDEX = 2;

    private static final int LOCATION_INDEX = 3;

    private static final int DATE_INDEX = 4;

    private static final int DETAIL_INDEX = 5;

    private static final int ROCKET_STATUS_INDEX = 6;

    private static final int COST_INDEX = 7;

    private static final int MISSION_STATUS_INDEX = 8;

    public static Mission of(String line) {
        Matcher match = RegEx.getMissionMatcher(line);

        String id = match.group(ID_INDEX);
        String companyName = match.group(COMPANY_INDEX);
        String location = match.group(LOCATION_INDEX);

        LocalDate date = LocalDate.parse(match.group(DATE_INDEX),
                DateTimeFormatter.ofPattern("EEE MMM dd, uuuu", Locale.ENGLISH));

        Detail detail = Detail.of(match.group(DETAIL_INDEX));

        RocketStatus rocketStatus = RocketStatus.get(match.group(ROCKET_STATUS_INDEX)).get();

        String costString = match.group(COST_INDEX);
        Optional<Double> cost = costString == null || costString.isEmpty()
                ? Optional.empty()
                : Optional.of(Double.parseDouble(costString.trim().replace(",", "")));

        MissionStatus missionStatus = MissionStatus.get(match.group(MISSION_STATUS_INDEX)).get();

        return new Mission(id, companyName, location, date, detail,
                rocketStatus, cost, missionStatus);
    }

    public String getCountry() {
        return Arrays.stream(location().split(", "))
                .toList()
                .getLast();
    }

    public boolean isSuccessful() {
        return missionStatus.equals(MissionStatus.SUCCESS);
    }
    
}
