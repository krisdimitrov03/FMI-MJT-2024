package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {

    public CourseDuration {
        if (hours < 0 || hours > 24)
            throw new IllegalArgumentException("Hours must be between 0 and 24.");

        if (minutes < 0 || minutes > 60)
            throw new IllegalArgumentException("Minutes must be between 0 and 60.");
    }

    public static CourseDuration of(Resource[] content) {
        int minutes = 0;

        for (var resource : content)
            minutes += resource.getDuration().minutes();

        int hours = minutes / 60;
        minutes -= hours * 60;

        return new CourseDuration(hours, minutes);
    }
}
