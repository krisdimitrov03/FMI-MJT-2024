package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class BusinessAccount extends AccountBase {
    private Category[] allowedCategories;

    public BusinessAccount(String username, double balance, Category[] allowedCategories) {
        super(username, balance);
        setType(AccountType.BUSINESS);
        this.allowedCategories = allowedCategories;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        super.buyCourse(course);

        boolean isAllowed = false;
        for (Category c : allowedCategories) {
            if(course.getCategory() == c) {
                isAllowed = true;
                break;
            }
        }

        if(!isAllowed) {
            throw new IllegalArgumentException("Course is not allowed for this account");
        }

        addToPurchased(course, getType().getDiscount());
    }
}
