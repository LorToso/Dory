package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.DoryUserCollection;
import com.doryapp.backend.myApi.model.FriendshipRequest;
import com.doryapp.backend.myApi.model.FriendshipRequestCollection;
import com.doryapp.dory.Api;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lorenzo Toso on 24.12.2016.
 */

public class GetRequestingUsersCall extends AuthedApiCall<List<DoryUser>> {

    private Context context;

    public GetRequestingUsersCall(Context context)
    {
        this.context = context;
    }

    @Override
    protected List<DoryUser> performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        DoryUserCollection collection = api.getRequestingUsers().execute();
        List<DoryUser> users = collection == null ? null : collection.getItems();
        return users;
    }
}
