package bg.sofia.uni.fmi.mjt.cooking.http.uri;

import bg.sofia.uni.fmi.mjt.cooking.http.requester.RecipeRequester;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuildableURITest {

    @Test
    void testBuilderAddQueryParamWithNullData() {
        assertThrows(IllegalArgumentException.class,
            () -> BuildableURI
                .newBuilder(RecipeRequester.INITIAL_URI)
                .addQueryParam(null, null),
            "Null arguments are not legal input data");
    }

    @Test
    void testBuilderAddQueryParamWithCorrectData() {
        BuildableURI.URIBuilder builder = BuildableURI.newBuilder(RecipeRequester.BASE_URI);

        builder.addQueryParam("type", "public");

        URI expected = URI.create(RecipeRequester.BASE_URI + "?type=public");

        assertEquals(expected, builder.build().toURI(),
            "URI should be built correctly in case of valid query parameters");
    }

}
