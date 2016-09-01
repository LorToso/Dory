package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.BoxedBool;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 01.09.2016.
 */
public class DoesUserExistCall extends AsyncApiCall {


    private String userId;
    private OnComplete<Boolean> onComplete;
    private Context context;

    public DoesUserExistCall(Context context, String userId, OnComplete<Boolean> onComplete)
    {
        this.userId = userId;
        this.onComplete = onComplete;
        this.context = context;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        BoxedBool bool = api.doesUserExist(userId).execute();
        onComplete.execute(bool.getValid());
    }
}
