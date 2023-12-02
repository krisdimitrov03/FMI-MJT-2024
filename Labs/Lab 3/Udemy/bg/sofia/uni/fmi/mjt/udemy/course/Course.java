package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {
    private String name;
    private String description;
    private double price;
    private Resource[] content;
    private CourseDuration duration;
    private Category category;
    private boolean isPurchased;

    public Course(String name, String description, double price, Resource[] content, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        setNonNullables(content);
        this.category = category;
        this.duration = CourseDuration.of(content);
        this.isPurchased = false;
    }

    @Override
    public boolean isCompleted() {
        for (Resource r : content) {
            if (!r.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getCompletionPercentage() {
        int completedCount = 0;

        for (Resource r : content) {
            if (r.isCompleted()) {
                completedCount++;
            }
        }

        double percentage = (double) (completedCount * 100) / content.length;
        return percentage - (int) percentage < 0.5 ? (int) percentage : (int) percentage + 1;
    }

    @Override
    public void purchase() {
        isPurchased = true;
    }

    @Override
    public boolean isPurchased() {
        return isPurchased;
    }

    /**
     * Returns the name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the price of the course.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of the course.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the content of the course.
     */
    public Resource[] getContent() {
        return content;
    }

    /**
     * Returns the total duration of the course.
     */
    public CourseDuration getTotalTime() {
        return duration;
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws IllegalArgumentException  if resourceToComplete is null.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        if (resourceToComplete == null)
            throw new IllegalArgumentException("Resource is null");

        boolean exists = false;
        int idx = -1;

        for (int i = 0; i < content.length; i++) {
            if (content[i] == resourceToComplete) {
                exists = true;
                idx = i;
                break;
            }
        }

        if (!exists)
            throw new ResourceNotFoundException("Resource not found");

        content[idx].complete();
    }

    protected void setNonNullables(Resource[] content) {
        int count = 0;

        for (Resource r : content) {
            if (r != null) {
                count++;
            }
        }

        this.content = new Resource[count];

        for (int i = 0; i < content.length; i++) {
            if (content[i] == null) {
                continue;
            }

            this.content[i] = content[i];
        }
    }
}
