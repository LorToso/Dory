package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.BoxedBool;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 28.10.2016.
 */

/*
 * This call deletes the DoryUser of the user that is currently signed into Firebase in the application
 * It is not possible to delete a user that si not signed in. (Would need administrator rights or sth like that, that is not implemented yet)
 *
 */
public class DeleteOwnUserCall extends AuthedApiCall<Boolean> {

    private Context context;

    public DeleteOwnUserCall(Context context)
    {
        this.context = context;
    }

    @Override
    protected Boolean performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        BoxedBool result = api.deleteOwnUser().execute();
        return result.getValid();
    }
}
