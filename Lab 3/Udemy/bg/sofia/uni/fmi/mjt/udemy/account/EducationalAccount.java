package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase {
    private int discountsUsed;

    public EducationalAccount(String username, double balance) {
        super(username, balance);
        setType(AccountType.EDUCATION);
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        super.buyCourse(course);

        double discount = 0;
        if (getCompletedCoursesCount() % 5 == 0 &&
                (getAverageGrade() > 4.50 || Math.abs(getAverageGrade() - 4.50) < 0.00001)) {
            discount = getType().getDiscount();
        }

        addToPurchased(course, discount);
    }
}
