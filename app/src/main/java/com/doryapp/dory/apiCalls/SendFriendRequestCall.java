package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.dory.Api;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 01.09.2016.
 */
public class SendFriendRequestCall extends AuthedApiCall {

    private String otherUserId;
    private Context context;

    public SendFriendRequestCall(Context context, String otherUserId)
    {
        this.otherUserId = otherUserId;
        this.context = context;
    }

    @Override
    protected void performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        api.sendFriendRequest(otherUserId).execute();
    }
}
