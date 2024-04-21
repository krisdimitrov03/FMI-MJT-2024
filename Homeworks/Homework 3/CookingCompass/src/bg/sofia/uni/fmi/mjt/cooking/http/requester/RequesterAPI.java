package bg.sofia.uni.fmi.mjt.cooking.http.requester;

import bg.sofia.uni.fmi.mjt.cooking.exception.RequestFailedException;

import java.net.URI;
import java.net.http.HttpResponse;

public interface RequesterAPI {

    /***
     * Makes GET request to the specified URI
     * @param uri the URI to make GET request
     * @return response with string body
     * @throws RequestFailedException is the request cannot be sent
     */
    HttpResponse<String> get(URI uri) throws RequestFailedException;

}
