package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 03.10.2016.
 */

public class GetUserByIdCall extends SimpleApiCall<DoryUser> {

    private Context context;
    private String userId;

    public GetUserByIdCall(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    @Override
    protected DoryUser performCall() throws IOException {
        MyApi api = Api.get(context);
        return api.getUserById(userId).execute();
    }
}
