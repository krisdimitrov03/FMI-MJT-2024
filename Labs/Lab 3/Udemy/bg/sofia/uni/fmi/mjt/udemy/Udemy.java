package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform {
    private Account[] accounts;
    private Course[] courses;

    public Udemy(Account[] accounts, Course[] courses) {
        setAccounts(accounts);
        setCourses(courses);
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank");
        }

        boolean exists = false;
        int idx;

        for (idx = 0; idx < courses.length; idx++) {
            if (courses[idx].getName().equals(name)) {
                exists = true;
                break;
            }
            idx++;
        }

        if (!exists) {
            throw new CourseNotFoundException("Course not found");
        }

        return courses[idx];
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank() || !isKeyword(keyword)) {
            throw new IllegalArgumentException("Keyword is null, blank or is not keyword");
        }

        int length = 0;
        for (Course c : courses) {
            if (containsKeyWord(c, keyword)) {
                length++;
            }
        }

        Course[] result = new Course[length];
        int i = 0;

        for (Course c : courses) {
            if (containsKeyWord(c, keyword)) {
                result[i++] = c;
            }
        }

        return result;
    }

    private boolean isKeyword(String keyword) {
        for (char c : keyword.toCharArray()) {
            if (!Character.isUpperCase(c) && !Character.isLowerCase(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean containsKeyWord(Course course, String keyword) {
        return course.getName().contains(keyword) || course.getDescription().contains(keyword);
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is null");
        }

        int length = 0;
        for (Course c : courses) {
            if (c.getCategory() == category) {
                length++;
            }
        }

        Course[] result = new Course[length];
        int i = 0;

        for (Course c : courses) {
            if (c.getCategory() == category) {
                result[i++] = c;
            }
        }

        return result;
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank");
        }

        int idx = -1;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].getUsername().equals(name)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            throw new AccountNotFoundException("Account with such a name not found");
        }

        return accounts[idx];
    }

    @Override
    public Course getLongestCourse() {
        if (courses.length == 0) {
            return null;
        }

        int duration = -1, idx = -1;

        for (int i = 0; i < courses.length; i++) {
            int courseMinutes = courses[i].getTotalTime().hours() * 60;
            courseMinutes += courses[i].getTotalTime().minutes();

            if (courseMinutes > duration) {
                duration = courseMinutes;
                idx = i;
            }
        }

        return courses[idx];
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is null");
        }

        double minPrice = 0.0;
        int idx = 0;

        for (int i = 0; i < courses.length; i++) {
            if (courses[i].getCategory() == category) {
                if (courses[i].getPrice() < minPrice) {
                    minPrice = courses[i].getPrice();
                    idx = i;
                }
            }
        }

        return courses[idx];
    }

    protected void setCourses(Course[] courses) {
        int count = 0;

        for (Course r : courses) {
            if (r != null) {
                count++;
            }
        }

        this.courses = new Course[count];

        for (int i = 0; i < courses.length; i++) {
            if (courses[i] == null) {
                continue;
            }

            this.courses[i] = courses[i];
        }
    }

    protected void setAccounts(Account[] accounts) {
        int count = 0;

        for (Account r : accounts) {
            if (r != null) {
                count++;
            }
        }

        this.accounts = new Account[count];

        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                continue;
            }

            this.accounts[i] = accounts[i];
        }
    }
}
