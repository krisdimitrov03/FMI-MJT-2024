package bg.sofia.uni.fmi.mjt.cooking.recipe;

import bg.sofia.uni.fmi.mjt.cooking.http.response.RecipeDTO;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.DietLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.CuisineType;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.DishType;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.MealType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecipeTest {

    @Test
    void testFactoryMethodWithNullData() {
        assertThrows(IllegalArgumentException.class, () -> Recipe.of(null),
            "Null argument is not legal input data");
    }

    @Test
    void testFactoryMethodWithCorrectData() {
        RecipeDTO dto = new RecipeDTO("Chicken Vesuvio",
            new String[] {"Low-Carb"},
            new String[] {"Mediterranean", "Dairy-Free", "Gluten-Free"},
            2976.85,
            new String[] {"italian"},
            new String[] {"lunch/dinner"},
            new String[] {"main course"},
            new String[] {"1/2 cup olive oil",
                "5 cloves garlic, peeled",
                "2 large russet potatoes, peeled and cut into chunks",
                "1 3-4 pound chicken, cut into 8 pieces (or 3 pound chicken legs)"});

        Recipe expected = new Recipe("Chicken Vesuvio",
            List.of(DietLabel.LOW_CARB),
            List.of(HealthLabel.MEDITERRANEAN, HealthLabel.DAIRY_FREE, HealthLabel.GLUTEN_FREE),
            2976.85,
            List.of(CuisineType.ITALIAN),
            List.of(MealType.LUNCH_AND_DINNER),
            List.of(DishType.MAIN_COURSE),
            List.of("1/2 cup olive oil", "5 cloves garlic, peeled",
                "2 large russet potatoes, peeled and cut into chunks",
                "1 3-4 pound chicken, cut into 8 pieces (or 3 pound chicken legs)"));

        assertEquals(expected, Recipe.of(dto),
            "Factory method should create recipe in case of valid input data");
    }

}
