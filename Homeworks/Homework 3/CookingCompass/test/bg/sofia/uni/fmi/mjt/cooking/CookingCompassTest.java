package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.exception.LimitExceededException;
import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;
import bg.sofia.uni.fmi.mjt.cooking.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.cooking.http.requester.RecipeRequester;
import bg.sofia.uni.fmi.mjt.cooking.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.DietLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.CuisineType;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.DishType;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.MealType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CookingCompassTest {

    private static final int LIMITS_EXCEEDED_CODE = 429;
    
    private static final int UNAUTHORIZED_CODE = 401;

    private static final int LIST_OF_ERRORS_CODE = 400;

    private static final int OK_CODE = 200;

    private static final String PAGE_1_URI = RecipeRequester.INITIAL_URI + "&q=chicken";

    private static final String PAGE_2_URI =
        "https://api.edamam.com/api/recipes/v2?q=chicken&app_key=aa43ac4cb2eff4449143fa28926c2392&field=cuisineType&field=mealType&field=dishType&field=ingredientLines&field=label&field=dietLabels&field=healthLabels&field=totalWeight&_cont=CHcVQBtNNQphDmgVQntAEX4BYldtAAIES2VJAGcVZV16AAYDUXlSCmpFNVEiAgQPRmxIBmsRagN0DVEEEDFCBTQUZVcgB1IVLnlSVSBMPkd5BgNK&type=public&app_id=59273708";

    private static final String CORRECT_BODY_PAGE_1 = """
        {
          "_links": {
            "next": {
              "href": "https://api.edamam.com/api/recipes/v2?q=chicken&app_key=aa43ac4cb2eff4449143fa28926c2392&field=cuisineType&field=mealType&field=dishType&field=ingredientLines&field=label&field=dietLabels&field=healthLabels&field=totalWeight&_cont=CHcVQBtNNQphDmgVQntAEX4BYldtAAIES2VJAGcVZV16AAYDUXlSCmpFNVEiAgQPRmxIBmsRagN0DVEEEDFCBTQUZVcgB1IVLnlSVSBMPkd5BgNK&type=public&app_id=59273708",
              "title": "Next page"
            }
          },
          "hits": [
            {
              "recipe": {
                "label": "Chicken Vesuvio",
                "dietLabels": [
                  "Low-Carb"
                ],
                "healthLabels": [
                  "Mediterranean",
                  "Dairy-Free",
                  "Gluten-Free"
                ],
                "ingredientLines": [
                  "1/2 cup olive oil",
                  "5 cloves garlic, peeled",
                  "2 large russet potatoes, peeled and cut into chunks",
                  "1 3-4 pound chicken, cut into 8 pieces (or 3 pound chicken legs)"
                ],
                "totalWeight": 2976.85,
                "cuisineType": [
                  "italian"
                ],
                "mealType": [
                  "lunch/dinner"
                ],
                "dishType": [
                  "main course"
                ]
              }
            },
            {
              "recipe": {
                "label": "Chicken Paprikash",
                "dietLabels": [
                  "Low-Carb"
                ],
                "healthLabels": [
                  "Egg-Free",
                  "Peanut-Free"
                ],
                "ingredientLines": [
                  "640 grams chicken - drumsticks and thighs ( 3 whole chicken legs cut apart)",
                  "1/2 teaspoon salt",
                  "1/4 teaspoon black pepper"
                ],
                "totalWeight": 1824.61,
                "cuisineType": [
                  "central europe"
                ],
                "mealType": [
                  "lunch/dinner"
                ],
                "dishType": [
                  "main course"
                ]
              }
              }
              ]
          }
        """;

    private static final String CORRECT_BODY_PAGE_2 = """
        {
          "_links": {
            "next": {
              "href": "https://api.edamam.com/api/recipes/v2?q=chicken&app_key=aa43ac4cb2eff4449143fa28926c2392&field=cuisineType&field=mealType&field=dishType&field=ingredientLines&field=label&field=dietLabels&field=healthLabels&field=totalWeight&_cont=CHcVQBtNNQphDmgVQntAEX4BYldtBgEOQ2NDBWIVYVNxBgMAUXlSBGoTMVxyBwAGSmxGVzcRNwNwUVAFFzdJUmITNwchBAAVLnlSVSBMPkd5AANK&type=public&app_id=59273708",
              "title": "Next page"
            }
          },
          "hits": [
            {
              "recipe": {
                "label": "Poached Chicken for Tacos",
                "dietLabels": [
                  "High-Protein",
                  "Low-Carb",
                  "Low-Sodium"
                ],
                "healthLabels": [
                  "Sugar-Conscious",
                  "Keto-Friendly",
                  "Paleo"
                ],
                "ingredientLines": [
                  "1 large whole chicken breast with bone and skin",
                  "Salted chicken broth to cover"
                ],
                "totalWeight": 408.0,
                "cuisineType": [
                  "mexican"
                ],
                "mealType": [
                  "lunch/dinner"
                ],
                "dishType": [
                  "sandwiches"
                ]
              }
            },
            {
              "recipe": {
                "label": "Fig & Balsamic Chicken",
                "dietLabels": [
                  "Low-Fat",
                  "Low-Sodium"
                ],
                "healthLabels": [
                  "Dairy-Free",
                  "Gluten-Free",
                  "Wheat-Free"
                ],
                "ingredientLines": [
                  "2 Shallots",
                  "2 teaspoons Fresh Rosemary",
                  "2 Chicken Breasts"
                ],
                "totalWeight": 879.62,
                "cuisineType": [
                  "french"
                ],
                "mealType": [
                  "lunch/dinner"
                ]
              }
            }
            ]
            }
        """;

    @Test
    void testSearchRecipesWithNullData() {
        CookingCompassAPI compass = new CookingCompass();

        assertThrows(IllegalArgumentException.class, () ->
                compass.searchRecipes(null, null, null),
            "Three null arguments are not legal input data");
    }

    @Test
    void testSearchRecipesForLimitExceededCase() throws IOException, InterruptedException {
        HttpClient client = mock();

        HttpResponse<String> response = mock();
        when(response.statusCode()).thenReturn(LIMITS_EXCEEDED_CODE);

        when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
            .thenReturn(response);

        var compass = new CookingCompass(client);

        assertThrows(LimitExceededException.class,
            () -> compass.searchRecipes("recipe", List.of(MealType.LUNCH), null),
            "Validation in case of exceeded limits is required");
    }

    @Test
    void testSearchRecipesForUnauthorizedCase() throws IOException, InterruptedException {
        HttpClient client = mock();

        HttpResponse<String> response = mock();
        when(response.statusCode()).thenReturn(UNAUTHORIZED_CODE);

        when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
            .thenReturn(response);

        var compass = new CookingCompass(client);

        assertThrows(UnauthorizedException.class,
            () -> compass.searchRecipes("recipe", List.of(MealType.LUNCH), null),
            "Validation in case of unauthorized request is required");
    }

    @Test
    void testSearchRecipesForRequestFailedCase() throws IOException, InterruptedException {
        HttpClient client = mock();

        HttpResponse<String> response = mock();
        when(response.statusCode()).thenReturn(LIST_OF_ERRORS_CODE);

        when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
            .thenReturn(response);

        var compass = new CookingCompass(client);

        assertThrows(RequestFailedException.class,
            () -> compass.searchRecipes("recipe", List.of(MealType.LUNCH), null),
            "Validation in case of failed request is required");
    }

    @Test
    void testSearchRecipesForCorrectResult()
        throws IOException, InterruptedException, RequestFailedException, UnauthorizedException,
        LimitExceededException {
        var compass = new CookingCompass(mockClient());

        assertEquals(seedExpectedRecipes(), compass.searchRecipes("chicken", null, null),
            "Fetching or parsing recipes is incorrect");
    }

    private HttpClient mockClient() throws IOException, InterruptedException {
        HttpClient client = mock();

        URI pageOneUri = URI.create(PAGE_1_URI);

        HttpRequest pageOneRequest = HttpRequest.newBuilder(pageOneUri)
            .header(RecipeRequester.ACCEPT, RecipeRequester.APP_JSON)
            .build();

        HttpResponse<String> pageOneResponse = mock();
        when(pageOneResponse.statusCode()).thenReturn(OK_CODE);
        when(pageOneResponse.body()).thenReturn(CORRECT_BODY_PAGE_1);

        URI pageTwoUri = URI.create(PAGE_2_URI);

        HttpRequest pageTwoRequest = HttpRequest.newBuilder(pageTwoUri)
            .header(RecipeRequester.ACCEPT, RecipeRequester.APP_JSON)
            .build();

        HttpResponse<String> pageTwoResponse = mock();
        when(pageTwoResponse.statusCode()).thenReturn(OK_CODE);
        when(pageTwoResponse.body()).thenReturn(CORRECT_BODY_PAGE_2);

        when(client.send(pageOneRequest, HttpResponse.BodyHandlers.ofString()))
            .thenReturn(pageOneResponse);

        when(client.send(pageTwoRequest, HttpResponse.BodyHandlers.ofString()))
            .thenReturn(pageTwoResponse);

        return client;
    }

    private Collection<Recipe> seedExpectedRecipes() {
        return List.of(
            new Recipe("Chicken Vesuvio",
                List.of(DietLabel.LOW_CARB),
                List.of(HealthLabel.MEDITERRANEAN, HealthLabel.DAIRY_FREE, HealthLabel.GLUTEN_FREE),
                2976.85,
                List.of(CuisineType.ITALIAN),
                List.of(MealType.LUNCH_AND_DINNER),
                List.of(DishType.MAIN_COURSE),
                List.of("1/2 cup olive oil",
                    "5 cloves garlic, peeled",
                    "2 large russet potatoes, peeled and cut into chunks",
                    "1 3-4 pound chicken, cut into 8 pieces (or 3 pound chicken legs)")),
            new Recipe("Chicken Paprikash",
                List.of(DietLabel.LOW_CARB),
                List.of(HealthLabel.EGG_FREE, HealthLabel.PEANUT_FREE),
                1824.61,
                List.of(CuisineType.CENTRAL_EUROPE),
                List.of(MealType.LUNCH_AND_DINNER),
                List.of(DishType.MAIN_COURSE),
                List.of("640 grams chicken - drumsticks and thighs ( 3 whole chicken legs cut apart)",
                    "1/2 teaspoon salt",
                    "1/4 teaspoon black pepper")),
            new Recipe("Poached Chicken for Tacos",
                List.of(DietLabel.HIGH_PROTEIN, DietLabel.LOW_CARB, DietLabel.LOW_SODIUM),
                List.of(HealthLabel.SUGAR_CONSCIOUS, HealthLabel.KETO_FRIENDLY, HealthLabel.PALEO),
                408.0,
                List.of(CuisineType.MEXICAN),
                List.of(MealType.LUNCH_AND_DINNER),
                List.of(DishType.SANDWICHES),
                List.of("1 large whole chicken breast with bone and skin",
                    "Salted chicken broth to cover")),
            new Recipe("Fig & Balsamic Chicken",
                List.of(DietLabel.LOW_FAT, DietLabel.LOW_SODIUM),
                List.of(HealthLabel.DAIRY_FREE, HealthLabel.GLUTEN_FREE, HealthLabel.WHEAT_FREE),
                879.62,
                List.of(CuisineType.FRENCH),
                List.of(MealType.LUNCH_AND_DINNER),
                new LinkedList<>(),
                List.of("2 Shallots",
                    "2 teaspoons Fresh Rosemary",
                    "2 Chicken Breasts"))
        );
    }

}
