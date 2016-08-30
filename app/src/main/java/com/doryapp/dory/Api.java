package com.doryapp.dory;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 30.08.2016.
 */
public class Api {

    public static MyApi get(Context context)
    {
        MyApi.Builder builder = new MyApi.Builder(AppConstants.HTTP_TRANSPORT, AppConstants.JSON_FACTORY,null);
        builder.setRootUrl(context.getString(R.string.server_url));
        builder.setApplicationName("DoryApp");
        return builder.build();
    }
    public static MyApi getAuthenticated(Context context, final String token)
    {
        HttpRequestInitializer initializer = new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.setHeaders(new HttpHeaders().setAuthorization(token));
            }
        };

        MyApi.Builder builder = new MyApi.Builder(AppConstants.HTTP_TRANSPORT, AppConstants.JSON_FACTORY, initializer);
        builder.setRootUrl(context.getString(R.string.server_url));
        builder.setApplicationName("DoryApp");
        return builder.build();
    }
}
