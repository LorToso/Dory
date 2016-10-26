package com.doryapp.dory;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

import java.io.IOException;

public class Api {


    public static MyApi get(Context context) {
        return get(context.getString(R.string.server_url));
    }

    public static MyApi getAuthenticated(Context context, String token) {
        return getAuthenticated(context.getString(R.string.server_url), token);
    }

    public static MyApi get(String serverURL) {
        return finalizeBuilder(new ApiRequestInitializer(), serverURL);
    }

    public static MyApi getAuthenticated(String serverURL, String token) {
        return finalizeBuilder(new AuthentificatedApiRequestInitializer(token), serverURL);
    }



    private static MyApi finalizeBuilder(HttpRequestInitializer initializer, String serverURL) {
        MyApi.Builder builder = new MyApi.Builder(AppConstants.HTTP_TRANSPORT, AppConstants.JSON_FACTORY, initializer);
        builder.setRootUrl(serverURL);
        builder.setApplicationName("DoryApp");
        return builder.build();
    }


}
