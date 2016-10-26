package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.FriendshipRequest;
import com.doryapp.backend.myApi.model.FriendshipRequestCollection;
import com.doryapp.dory.Api;

import java.io.IOException;
import java.util.List;


public class GetFriendRequestCall extends AuthedApiCall<List<FriendshipRequest>> {

    private Context context;

    public GetFriendRequestCall(Context context)
    {
        this.context = context;
    }

    @Override
    protected List<FriendshipRequest> performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        FriendshipRequestCollection collection = api.getFriendshipRequests().execute();
        List<FriendshipRequest> requests = collection == null ? null : collection.getItems();
        return requests;
    }
}
