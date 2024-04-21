package bg.sofia.uni.fmi.mjt.cooking.validation;

import bg.sofia.uni.fmi.mjt.cooking.exception.LimitExceededException;
import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;
import bg.sofia.uni.fmi.mjt.cooking.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.MealType;

import java.net.http.HttpResponse;
import java.util.Collection;

public class Validator {

    public static final String INVALID_ARGUMENTS_MESSAGE =
            "Invalid arguments. At least one of the arguments should not be null or empty";

    public static final String LIMITS_EXCEEDED_MESSAGE = "Usage limits are exceeded";

    public static final String INCORRECT_REQUEST_PARAMS_MESSAGE = "Request parameters incorrect";

    public static final String NULL_ARGUMENT_MESSAGE = "Argument was null -> ";

    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized request";

    public static void validateResponse(HttpResponse<String> response)
            throws RequestFailedException, LimitExceededException, UnauthorizedException {
        if (response.statusCode() == 429) {
            throw new LimitExceededException(LIMITS_EXCEEDED_MESSAGE);
        }

        if (response.statusCode() == 401) {
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        if (response.statusCode() != 200) {
            throw new RequestFailedException(INCORRECT_REQUEST_PARAMS_MESSAGE);
        }
    }

    public static boolean validateSearchQuery(String query) {
        return query != null && !query.isEmpty();
    }

    public static <T extends Enum<T>> boolean validateCollectionParameter(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static void validateParameters(String searchQuery, Collection<MealType> mealTypes,
                                          Collection<HealthLabel> healthLabels) {

        if (!validateSearchQuery(searchQuery) && !validateCollectionParameter(mealTypes)
                && !validateCollectionParameter(healthLabels)) {
            throw new IllegalArgumentException(INVALID_ARGUMENTS_MESSAGE);
        }

    }

    public static <T> void validateArgument(T arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_MESSAGE + argName);
        }
    }

}
