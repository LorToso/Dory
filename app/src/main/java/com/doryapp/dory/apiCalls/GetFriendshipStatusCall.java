package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.FriendshipStatus;
import com.doryapp.dory.Api;

import java.io.IOException;

public class GetFriendshipStatusCall extends AuthedApiCall {


    private OnComplete<FriendshipStatus> onComplete;
    private Context context;
    private String friendID;

    public GetFriendshipStatusCall(Context context, String friendID, OnComplete<FriendshipStatus> onComplete)
    {
        this.onComplete = onComplete;
        this.context = context;
        this.friendID = friendID;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi myApi = Api.getAuthenticated(context,token);
        FriendshipStatus status = myApi.getFriendshipStatus(friendID).execute();
        onComplete.execute(status);
    }
}
