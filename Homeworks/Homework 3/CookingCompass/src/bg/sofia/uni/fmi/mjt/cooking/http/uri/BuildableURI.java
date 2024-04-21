package bg.sofia.uni.fmi.mjt.cooking.http.uri;

import bg.sofia.uni.fmi.mjt.cooking.validation.Validator;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildableURI {

    private final String uri;

    private BuildableURI(URIBuilder builder) {
        this.uri = builder.getUriString();
    }

    public static URIBuilder newBuilder(String baseUrl) {
        return new URIBuilder(baseUrl);
    }

    public URI toURI() {
        return URI.create(uri);
    }

    public static class URIBuilder {

        private static final String KEY_VALUE_DELIMITER = "=";
        private static final String PARAM_DELIMITER = "&";
        private static final String QUERY_DELIMITER = "?";
        private final String baseUrl;
        private final Set<Map.Entry<String, String>> queryParams;

        private URIBuilder(String baseUrl) {
            this.baseUrl = baseUrl;
            this.queryParams = new HashSet<>();
        }

        public void addQueryParam(String key, String value) {
            Validator.validateArgument(key, "key");
            Validator.validateArgument(value, "value");

            queryParams.add(Map.entry(key, value));
        }

        public BuildableURI build() {
            return new BuildableURI(this);
        }

        private String getUriString() {
            if (queryParams.isEmpty()) {
                return baseUrl;
            }

            String queryString = queryParams
                .stream()
                .map(e -> e.getKey() + KEY_VALUE_DELIMITER + encodeUrlValue(e.getValue()))
                .collect(Collectors.joining(PARAM_DELIMITER));

            String delimiter = baseUrl.contains(QUERY_DELIMITER) ? PARAM_DELIMITER : QUERY_DELIMITER;

            return baseUrl + delimiter + queryString;
        }

        private String encodeUrlValue(String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }

    }

}
