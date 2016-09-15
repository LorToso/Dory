package com.doryapp.dory.apiCalls;

import android.content.Context;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.DoryUserCollection;
import com.doryapp.dory.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo Toso on 15.09.2016.
 */
public class GetUsersCall extends SimpleApiCall {
    Context context;
    String nickName;
    OnComplete<List<DoryUser>> onComplete;

    public GetUsersCall(Context context, OnComplete<List<DoryUser>> onComplete) {
        this(context, onComplete, "");
    }
    public GetUsersCall(Context context, OnComplete<List<DoryUser>> onComplete, String nickName) {
        this.context = context;
        this.nickName = nickName;
        this.onComplete = onComplete;
    }

    @Override
    protected void performCall() throws IOException {
        MyApi api = Api.get(context);
        DoryUserCollection usersCollection = api.getUsersByNickName(nickName).execute();

        onComplete.execute(usersCollection == null ? new ArrayList<DoryUser>() : usersCollection.getItems());
    }
}
