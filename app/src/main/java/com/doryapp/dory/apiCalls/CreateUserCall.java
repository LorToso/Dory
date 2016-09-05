package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 05.09.2016.
 */
public class CreateUserCall extends AsyncApiCall{

    private DoryUser user;
    private Context context;

    public CreateUserCall(Context context, DoryUser user)
    {
        this.user = user;
        this.context = context;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        api.createUser(user.getFirstName(),user.getId(),user.getLastName(),user.getNickName(),user.getLocation());
    }
}