package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.DoryUserCollection;
import com.doryapp.dory.Api;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lorenzo Toso on 02.09.2016.
 */
public class GetFriendsCall extends AuthedApiCall {

    private OnComplete<List<DoryUser>> onComplete;
    private Context context;

    public GetFriendsCall(Context context, OnComplete<List<DoryUser>> onComplete)
    {
        this.onComplete = onComplete;
        this.context = context;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        DoryUserCollection collection = api.getFriends().execute();
        List<DoryUser> friends = collection == null ? null : collection.getItems();
        onComplete.execute(friends);
    }
}
