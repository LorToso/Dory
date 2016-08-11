package com.doryapp.dory;

import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo on 01.08.2016.
 */
public class FBFriendFinder {
    UserListCallback userListCallback;

    public FBFriendFinder(UserListCallback userListCallback)
    {
        this.userListCallback = userListCallback;
    }

    public void FindFriends()
    {
        final GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray array, GraphResponse response) {
                        List<User> friends = new ArrayList<User>();
                        try {
                            for(int i=0; i <array.length(); i++)
                            {
                                JSONObject friend = (JSONObject) array.get(i);
                                friends.add(User.FromJSON(friend));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userListCallback.onUserListLoaded(friends);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "location,first_name,last_name,id,picture{url}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public abstract static class UserListCallback {
        public abstract void onUserListLoaded(List<User> users);
    }
}
