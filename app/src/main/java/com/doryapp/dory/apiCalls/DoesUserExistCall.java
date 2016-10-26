package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.BoxedBool;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 01.09.2016.
 */
public class DoesUserExistCall extends SimpleApiCall<java.lang.Boolean> {


    private String userId;
    private Context context;

    public DoesUserExistCall(Context context, String userId)
    {
        this.userId = userId;
        this.context = context;
    }

    @Override
    protected Boolean performCall() throws IOException {
        MyApi api = Api.get(context);
        BoxedBool bool = api.doesUserExist(userId).execute();
        return bool.getValid();
    }
}
