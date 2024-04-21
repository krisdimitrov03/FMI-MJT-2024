package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.exception.LimitExceededException;
import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;
import bg.sofia.uni.fmi.mjt.cooking.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.cooking.http.requester.RecipeRequester;
import bg.sofia.uni.fmi.mjt.cooking.http.requester.RequesterAPI;
import bg.sofia.uni.fmi.mjt.cooking.http.response.HitDTO;
import bg.sofia.uni.fmi.mjt.cooking.http.response.ResponseDTO;
import bg.sofia.uni.fmi.mjt.cooking.http.uri.BuildableURI;
import bg.sofia.uni.fmi.mjt.cooking.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cooking.recipe.label.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.recipe.type.MealType;
import bg.sofia.uni.fmi.mjt.cooking.validation.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class CookingCompass implements CookingCompassAPI {

    private static final int PAGES_COUNT = 2;

    private final RequesterAPI requester;

    private final Gson gson;

    public CookingCompass() {
        this.gson = new GsonBuilder().serializeNulls().create();
        this.requester = new RecipeRequester();
    }

    public CookingCompass(HttpClient client) {
        this.gson = new GsonBuilder().serializeNulls().create();
        this.requester = new RecipeRequester(client);
    }

    @Override
    public Collection<Recipe> searchRecipes(String searchQuery, Collection<MealType> mealTypes,
                                            Collection<HealthLabel> healthLabels)
        throws RequestFailedException, LimitExceededException, UnauthorizedException {

        Validator.validateParameters(searchQuery, mealTypes, healthLabels);

        BuildableURI.URIBuilder builder = BuildableURI.newBuilder(RecipeRequester.INITIAL_URI);
        addParametersToURIBuilder(searchQuery, mealTypes, healthLabels, builder);
        URI currentUri = builder.build().toURI();

        return loadRecipePages(currentUri);

    }

    private void addParametersToURIBuilder(String searchQuery, Collection<MealType> mealTypes,
                                           Collection<HealthLabel> healthLabels, BuildableURI.URIBuilder builder) {

        if (Validator.validateSearchQuery(searchQuery)) {
            builder.addQueryParam(RecipeRequester.SEARCH_QUERY,
                URLEncoder.encode(searchQuery, StandardCharsets.US_ASCII));
        }

        if (Validator.validateCollectionParameter(mealTypes)) {
            mealTypes.stream()
                .map(MealType::toString)
                .forEach(m -> builder.addQueryParam(RecipeRequester.MEAL_TYPE, m));
        }

        if (Validator.validateCollectionParameter(healthLabels)) {
            healthLabels.stream()
                .map(HealthLabel::toString)
                .forEach(d -> builder.addQueryParam(RecipeRequester.DIET_LABEL, d));
        }

    }

    private Collection<Recipe> loadRecipePages(URI currentUri)
        throws RequestFailedException, LimitExceededException, UnauthorizedException {

        Collection<Recipe> recipes = new LinkedList<>();

        for (int i = 0; i < PAGES_COUNT; i++) {
            HttpResponse<String> response = requester.get(currentUri);

            Validator.validateResponse(response);

            ResponseDTO responseDTO = gson.fromJson(response.body(), ResponseDTO.class);

            Collection<Recipe> currentRecipes = Arrays.stream(responseDTO.hits())
                .map(HitDTO::recipe)
                .map(Recipe::of)
                .toList();

            recipes.addAll(currentRecipes);

            if (responseDTO.links().next().href() == null) {
                break;
            }

            currentUri = URI.create(responseDTO.links().next().href());
        }

        return recipes;

    }

}
