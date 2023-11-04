package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public abstract class AccountBase implements Account {
    private static final int MAX_PURCHASED_COURSES_LENGTH = 100;
    private String username;
    private double balance;
    private AccountType type;
    private Course[] purchasedCourses;
    private int size;
    private double averageGrade;

    public AccountBase(String username, double balance) {
        this.username = username;
        this.balance = balance;
        this.purchasedCourses = new Course[MAX_PURCHASED_COURSES_LENGTH];
        this.averageGrade = 0;
        this.size = 0;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative number");

        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (course == null) {
            return;
        }

        if (balance < (1.00 - getType().getDiscount()) * course.getPrice()) {
            throw new InsufficientBalanceException("Not enough funds");
        }

        boolean isPurchased = false;

        for (int i=0; i<size; i++) {
            if(purchasedCourses[i] == course) {
                isPurchased = true;
                break;
            }
        }

        if (course.isPurchased() && isPurchased) {
            throw new CourseAlreadyPurchasedException("Course already purchased for this account");
        }

        if (size == purchasedCourses.length) {
            throw new MaxCourseCapacityReachedException("Purchased courses limit reached");
        }
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if (course == null) {
            throw new IllegalArgumentException("Course is null");
        }

        if (resourcesToComplete == null) {
            throw new IllegalArgumentException("Resource is null");
        }

        if (!isCoursePurchasedByThisAccount(course)) {
            throw new CourseNotPurchasedException("Course not purchased for this account");
        }

        for (Resource r : resourcesToComplete) {
            course.completeResource(r);
        }
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (course == null) {
            throw new IllegalArgumentException("Course is null");
        }

        if (grade < 2.00 || grade > 6.00) {
            throw new IllegalArgumentException("Grade must be between 2.00 and 6.00");
        }

        if (!isCoursePurchasedByThisAccount(course)) {
            throw new CourseNotPurchasedException("Course not purchased for this account");
        }

        if (!course.isCompleted()) {
            throw new CourseNotCompletedException("Course contains resource which is not completed");
        }

        if (averageGrade == 0) {
            averageGrade += grade;
        } else {
            averageGrade = (averageGrade + grade) / 2;
        }
    }

    @Override
    public Course getLeastCompletedCourse() {
        if (size == 0) {
            return null;
        }

        int idx = 0, currentPercentage = purchasedCourses[0].getCompletionPercentage();

        for (int i = 1; i < size; i++) {
            int perc = purchasedCourses[i].getCompletionPercentage();
            if (perc < currentPercentage) {
                idx = i;
                currentPercentage = perc;
            }
        }

        return purchasedCourses[idx];
    }

    protected AccountType getType() {
        return this.type;
    }

    protected void setType(AccountType type) {
        this.type = type;
    }

    private boolean isCoursePurchasedByThisAccount(Course course) {
        for (Course c : purchasedCourses) {
            if (c == course) {
                return true;
            }
        }

        return false;
    }

    protected void addToPurchased(Course course, double discount) {
        course.purchase();
        balance -= (1.00 - discount) * course.getPrice();
        purchasedCourses[size++] = course;
    }

    protected int getSize() {
        return size;
    }

    protected double getAverageGrade() {
        return averageGrade;
    }

    protected int getCompletedCoursesCount() {
        int result = 0;

        for (int i = 0; i < size; i++) {
            if (purchasedCourses[i].isCompleted()) {
                result++;
            }
        }

        return result;
    }
}
