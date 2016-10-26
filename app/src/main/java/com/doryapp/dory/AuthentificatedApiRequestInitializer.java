package com.doryapp.dory;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 26.10.2016.
 */

public class AuthentificatedApiRequestInitializer extends ApiRequestInitializer {

    private final String authentificationToken;

    public AuthentificatedApiRequestInitializer(String authentificationToken) {
        this.authentificationToken = authentificationToken;
    }


    @Override
    public void initialize(HttpRequest request) throws IOException {
        super.initialize(request);
        request.setHeaders(new HttpHeaders().setAuthorization(authentificationToken));
    }
}
