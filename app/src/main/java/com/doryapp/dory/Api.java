package com.doryapp.dory;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;

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
}
