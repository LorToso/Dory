package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.BoxedBool;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 05.09.2016.
 */
public class CreateUserCall extends AuthedApiCall<java.lang.Boolean> {

    private DoryUser user;
    private Context context;

    public CreateUserCall(Context context, DoryUser user)
    {
        this.user = user;
        this.context = context;
    }

    @Override
    protected java.lang.Boolean performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        BoxedBool result = api.createUser(user.getFirstName(),user.getLastName(),user.getNickName()).execute();
        return result.getValid();
    }
}