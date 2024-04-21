package bg.sofia.uni.fmi.mjt.cooking.recipe;

import bg.sofia.uni.fmi.mjt.cooking.http.response.RecipeDTO;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.DietLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.CuisineType;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.DishType;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.MealType;
import bg.sofia.uni.fmi.mjt.cooking.validation.Validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public record Recipe(String label, Collection<DietLabel> dietLabels, Collection<HealthLabel> healthLabels,
                     double totalWeight,
                     Collection<CuisineType> cuisineTypes, Collection<MealType> mealTypes,
                     Collection<DishType> dishTypes,
                     Collection<String> ingredientLines) {

    public static Recipe of(RecipeDTO data) {
        Validator.validateArgument(data, "data");

        var dietLabels = Arrays.stream(data.dietLabels()).map(DietLabel::of).toList();

        var healthLabels = Arrays.stream(data.healthLabels()).map(HealthLabel::of).toList();

        var cuisineTypes = Arrays.stream(data.cuisineType()).map(CuisineType::of).toList();

        var mealTypes = Arrays.stream(data.mealType()).map(MealType::of).toList();

        Collection<DishType> dishTypes = new LinkedList<>();

        if(data.dishType() != null) {
            dishTypes.addAll(Arrays.stream(data.dishType()).map(DishType::of).toList());
        }

        var ingredientLines = Arrays.stream(data.ingredientLines()).toList();

        return new Recipe(data.label(), dietLabels, healthLabels, data.totalWeight(), cuisineTypes, mealTypes,
                dishTypes, ingredientLines);
    }

}
