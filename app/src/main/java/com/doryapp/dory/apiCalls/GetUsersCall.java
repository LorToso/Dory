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
public class GetUsersCall extends SimpleApiCall<List<DoryUser>> {
    Context context;
    String nickName;

    public GetUsersCall(Context context) {
        this(context, "");
    }
    public GetUsersCall(Context context, String nickName) {
        this.context = context;
        this.nickName = nickName;
    }

    @Override
    protected List<DoryUser> performCall() throws IOException {
        MyApi api = Api.get(context);
        DoryUserCollection usersCollection = api.getUsersByNickName(nickName).execute();

        return usersCollection == null ? new ArrayList<DoryUser>() : usersCollection.getItems();
    }
}
