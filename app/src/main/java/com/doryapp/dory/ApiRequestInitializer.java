package com.doryapp.dory;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 26.10.2016.
 */

public class ApiRequestInitializer implements HttpRequestInitializer {

    private final static int connectionTimeOutInSeconds = 5;
    private final static int readTimeOutInSeconds = 10;

    @Override
    public void initialize(HttpRequest request) throws IOException {
        request.setConnectTimeout(connectionTimeOutInSeconds * 1000);
        request.setReadTimeout(readTimeOutInSeconds * 1000);
    }
}
