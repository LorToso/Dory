package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 03.10.2016.
 */

public class GetUserByIdCall extends SimpleApiCall {

    private Context context;
    private String userId;
    private OnComplete<DoryUser> onComplete;

    public GetUserByIdCall(Context context, String userId, OnComplete<DoryUser> onComplete) {
        this.context = context;
        this.userId = userId;
        this.onComplete = onComplete;
    }

    @Override
    protected void performCall() throws IOException {
        MyApi api = Api.get(context);
        DoryUser user = api.getUserById(userId).execute();
        onComplete.execute(user);
    }
}
