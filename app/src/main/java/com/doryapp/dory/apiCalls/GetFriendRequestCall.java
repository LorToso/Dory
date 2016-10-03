package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.FriendshipRequest;
import com.doryapp.backend.myApi.model.FriendshipRequestCollection;
import com.doryapp.dory.Api;

import java.io.IOException;
import java.util.List;


public class GetFriendRequestCall extends AuthedApiCall {

    private OnComplete<List<FriendshipRequest>> onComplete;
    private Context context;

    public GetFriendRequestCall(Context context, OnComplete<List<FriendshipRequest>> onComplete)
    {
        this.onComplete = onComplete;
        this.context = context;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        FriendshipRequestCollection collection = api.getFriendshipRequests().execute();
        List<FriendshipRequest> requests = collection == null ? null : collection.getItems();
        onComplete.execute(requests);
    }
}
