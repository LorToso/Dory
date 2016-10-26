package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.FriendshipRequest;
import com.doryapp.backend.myApi.model.FriendshipStatus;
import com.doryapp.dory.Api;

import java.io.IOException;
import java.util.List;

public class GetFriendshipStatusCall extends AuthedApiCall<FriendshipStatus> {

    private Context context;
    private String friendID;

    public GetFriendshipStatusCall(Context context, String friendID)
    {
        this.context = context;
        this.friendID = friendID;
    }

    @Override
    protected FriendshipStatus performCall(String token) throws IOException {
        MyApi myApi = Api.getAuthenticated(context,token);
        return myApi.getFriendshipStatus(friendID).execute();
    }
}
