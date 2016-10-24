package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.BoxedBool;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * This class is probably only used to test the connection to the server.
 */
public class NopCall extends SimpleApiCall {

    private Context context;

    public NopCall(Context context, String userId, OnComplete<Boolean> onComplete)
    {
        this.context = context;
    }

    @Override
    protected void performCall() throws IOException {
        MyApi api = Api.get(context);
        api.nop().execute();
    }
}
