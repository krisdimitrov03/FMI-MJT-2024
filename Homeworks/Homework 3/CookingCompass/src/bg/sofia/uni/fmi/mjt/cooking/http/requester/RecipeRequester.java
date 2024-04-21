package bg.sofia.uni.fmi.mjt.cooking.http.requester;

import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;
import bg.sofia.uni.fmi.mjt.cooking.validation.Validator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RecipeRequester implements RequesterAPI {

    private static final String APP_ID = "59273708";

    private static final String APP_KEY = "aa43ac4cb2eff4449143fa28926c2392";

    private static final String QUERY_URI = "?type=public"
        + "&app_id=" + APP_ID + "&app_key=" + APP_KEY
        + "&field=label&field=dietLabels&field=healthLabels&field=totalWeight"
        + "&field=cuisineType&field=mealType&field=dishType&field=ingredientLines";

    public static final String BASE_URI = "https://api.edamam.com/api/recipes/v2";

    public static final String INITIAL_URI = BASE_URI + QUERY_URI;

    public static final String SEARCH_QUERY = "q";

    public static final String MEAL_TYPE = "mealType";

    public static final String DIET_LABEL = "diet";

    public static final String ACCEPT = "Accept";

    public static final String APP_JSON = "application/json";

    public static final String REQUEST_NOT_SENT_MESSAGE = "Request could not be sent";

    private final HttpClient client;

    public RecipeRequester() {
        this.client = HttpClient.newHttpClient();
    }

    public RecipeRequester(HttpClient client) {
        this.client = client;
    }

    @Override
    public HttpResponse<String> get(URI uri) throws RequestFailedException {
        Validator.validateArgument(uri, "uri");

        try {
            var request = HttpRequest
                .newBuilder(uri)
                .header(ACCEPT, APP_JSON)
                .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RequestFailedException(REQUEST_NOT_SENT_MESSAGE);
        }
    }
    
}
