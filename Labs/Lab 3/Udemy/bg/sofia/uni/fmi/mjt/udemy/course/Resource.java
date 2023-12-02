package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {
    private String name;
    private ResourceDuration duration;
    private boolean isCompleted;
    public Resource(String name, ResourceDuration duration) {
        this.name = name;
        this.duration = duration;
        this.isCompleted = false;
    }

    public String getName() {
        return name;
    }

    public ResourceDuration getDuration() {
        return duration;
    }

    public void complete() {
        isCompleted = true;
    }

    @Override
    public boolean isCompleted() {
        return this.isCompleted;
    }

    @Override
    public int getCompletionPercentage() {
        return isCompleted() ? 100 : 0;
    }
}
