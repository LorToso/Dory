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
public class GetFriendsByNicknameCall extends AuthedApiCall<List<DoryUser>> {

    private Context context;
    private String nickname;

    public GetFriendsByNicknameCall(Context context, String nickname)
    {
        this.context = context;
        this.nickname = nickname;
    }

    @Override
    protected List<DoryUser> performCall(String token) throws IOException {
        MyApi api = Api.getAuthenticated(context,token);
        DoryUserCollection collection = api.getFriendsByNickname(nickname).execute();
        List<DoryUser> friends = collection == null ? null : collection.getItems();
        return friends;
    }
}
