package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.exception.LimitExceededException;
import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;
import bg.sofia.uni.fmi.mjt.cooking.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.cooking.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.MealType;

import java.util.Collection;

public interface CookingCompassAPI {

    /***
     * Gets information for recipes according to the three filters
     * @param searchQuery keywords to find recipes
     * @param mealTypes mealTypes the recipes should be proper for
     * @param healthLabels healthLabels the recipes should be proper for
     * @return collection of recipes
     * @throws RequestFailedException if there is problem with the request
     * @throws LimitExceededException if the allowed amount of queries is reached
     * @throws UnauthorizedException if the request is not authorized
     */
    Collection<Recipe> searchRecipes(String searchQuery, Collection<MealType> mealTypes,
                                     Collection<HealthLabel> healthLabels)
        throws RequestFailedException, LimitExceededException, UnauthorizedException;

}
