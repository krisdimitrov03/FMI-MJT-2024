package bg.sofia.uni.fmi.mjt.cooking.http.requester;

import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeRequesterTest {

    private static final URI incorrectRequestURI = URI.create(RecipeRequester.BASE_URI);

    private static final URI correctRequestURI = URI.create(RecipeRequester.INITIAL_URI + "&q=chicken");

    private static final String incorrectResponseBody = "{\"status\":\"error\",\"message\":\"Unauthorized\"}";

    private static final String correctResponseBody = """
        {
           "_links": {
             "next": {
               "href": "https://api.edamam.com/api/recipes/v2?q=chicken&app_key=aa43ac4cb2eff4449143fa28926c2392&field=label&field=dietLabels&field=healthLabels&field=totalWeight&field=cuisineType&field=mealType&field=dishType&field=ingredientLines&_cont=CHcVQBtNNQphDmgVQntAEX4BYldtAAIES2VJAGcVZV16AAYDUXlSCmpFNVEiAgQPRmxIBmsRagN0DVEEEDFCBTQUZVcgB1IVLnlSVSBMPkd5BgNK&type=public&app_id=59273708",
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
                   "Gluten-Free",
                   "Wheat-Free",
                   "Egg-Free",
                   "Peanut-Free",
                   "Tree-Nut-Free",
                   "Soy-Free",
                   "Fish-Free",
                   "Shellfish-Free",
                   "Pork-Free",
                   "Red-Meat-Free",
                   "Crustacean-Free",
                   "Celery-Free",
                   "Mustard-Free",
                   "Sesame-Free",
                   "Lupine-Free",
                   "Mollusk-Free",
                   "Kosher"
                 ],
                 "ingredientLines": [
                   "1/2 cup olive oil",
                   "5 cloves garlic, peeled",
                   "2 large russet potatoes, peeled and cut into chunks",
                   "1 3-4 pound chicken, cut into 8 pieces (or 3 pound chicken legs)",
                   "3/4 cup white wine",
                   "3/4 cup chicken stock",
                   "3 tablespoons chopped parsley",
                   "1 tablespoon dried oregano",
                   "Salt and pepper",
                   "1 cup frozen peas, thawed"
                 ],
                 "totalWeight": 2976.850615011728,
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
                   "Peanut-Free",
                   "Tree-Nut-Free",
                   "Soy-Free",
                   "Fish-Free",
                   "Shellfish-Free",
                   "Pork-Free",
                   "Red-Meat-Free",
                   "Crustacean-Free",
                   "Celery-Free",
                   "Mustard-Free",
                   "Sesame-Free",
                   "Lupine-Free",
                   "Mollusk-Free",
                   "Alcohol-Free",
                   "Sulfite-Free"
                 ],
                 "ingredientLines": [
                   "640 grams chicken - drumsticks and thighs ( 3 whole chicken legs cut apart)",
                   "1/2 teaspoon salt",
                   "1/4 teaspoon black pepper",
                   "1 tablespoon butter – cultured unsalted (or olive oil)",
                   "240 grams onion sliced thin (1 large onion)",
                   "70 grams Anaheim pepper chopped (1 large pepper)",
                   "25 grams paprika (about 1/4 cup)",
                   "1 cup chicken stock",
                   "1/2 teaspoon salt",
                   "1/2 cup sour cream",
                   "1 tablespoon flour – all-purpose"
                 ],
                 "totalWeight": 1824.6125000003276,
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

    @Test
    void testGetWithNullData() {
        var requester = new RecipeRequester();

        assertThrows(IllegalArgumentException.class,
            () -> requester.get(null),
            "Null argument is not legal input data");
    }

    @Test
    void testGetWithIncorrectRequestURI() throws RequestFailedException, IOException, InterruptedException {
        HttpClient client = mock();

        HttpResponse<String> incorrectResponse = mock();
        when(incorrectResponse.statusCode()).thenReturn(401);
        when(incorrectResponse.body()).thenReturn(incorrectResponseBody);

        var incorrectRequest = HttpRequest
            .newBuilder(incorrectRequestURI)
            .header(RecipeRequester.ACCEPT, RecipeRequester.APP_JSON)
            .build();

        when(client.send(incorrectRequest, HttpResponse.BodyHandlers.ofString()))
            .thenReturn(incorrectResponse);

        var requester = new RecipeRequester(client);

        var response = requester.get(incorrectRequestURI);

        assertEquals(401, response.statusCode(),
            "Status code should be 401 if request is not authorized");
        assertEquals(incorrectResponseBody, response.body(),
            "Body should be proper for unauthorized request");
    }

    @Test
    void testGetWithCorrectRequestURI() throws RequestFailedException, IOException, InterruptedException {
        HttpClient client = mock();

        var correctRequest = HttpRequest
            .newBuilder(correctRequestURI)
            .header(RecipeRequester.ACCEPT, RecipeRequester.APP_JSON)
            .build();

        HttpResponse<String> correctResponse = mock();
        when(correctResponse.statusCode()).thenReturn(200);
        when(correctResponse.body()).thenReturn(correctResponseBody);

        when(client.send(correctRequest, HttpResponse.BodyHandlers.ofString()))
            .thenReturn(correctResponse);

        var requester = new RecipeRequester(client);

        var response = requester.get(correctRequestURI);

        assertEquals(200, response.statusCode(),
            "Status code should be 200 in case of valid request");
        assertEquals(correctResponseBody, response.body(),
            "Body should be proper for valid request");
    }

}
