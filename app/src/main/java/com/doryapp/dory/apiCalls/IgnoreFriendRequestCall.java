package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 01.10.2016.
 */

public class IgnoreFriendRequestCall extends AuthedApiCall {

    private Context context;
    private String friendID;

    public IgnoreFriendRequestCall(Context context, String friendID)
    {
        this.context = context;
        this.friendID = friendID;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        api.acceptFriendRequest(friendID).execute();
    }
}
