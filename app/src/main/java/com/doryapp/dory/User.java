package com.doryapp.dory;

import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class User {
    String firstName;
    String lastName;

    Location currentCity;

    URI fbProfilePicture;

    String fbID;


    public static void GetSelf(GetUserCallback userCallback)
    {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),new FacebookUsercreationCallback(userCallback));
        request.setParameters(FacebookUsercreationCallback.prepareParameters());
        request.executeAsync();
    }
    public static User FromJSON(JSONObject json)
    {
        User user = new User();

        try {
            ExtractUserInformation(json, user);
            ExtractPictureInformation(json, user);
            ExtractLocation(json, user);
        } catch (URISyntaxException | JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static abstract class GetUserCallback
    {
        public abstract void onUser(User user);
    }
    private static class FacebookUsercreationCallback implements GraphRequest.GraphJSONObjectCallback
    {
        private GetUserCallback userCallback;

        FacebookUsercreationCallback(GetUserCallback userCallback)
        {
            this.userCallback = userCallback;
        }

        static Bundle prepareParameters()
        {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, picture.type(normal), location{location}");
            return parameters;
        }
        @Override
        public void onCompleted(
                JSONObject object,
                GraphResponse response) {
            if(object == null)
                return;
            userCallback.onUser(User.FromJSON(object));
        }

    }

    private static void ExtractPictureInformation(JSONObject object, User user) throws JSONException, URISyntaxException {
        JSONObject picture = (JSONObject)object.get("picture");
        JSONObject pictureData = (JSONObject) picture.get("data");
        user.fbProfilePicture = new URI((String) pictureData.get("url"));
    }

    private static void ExtractUserInformation(JSONObject object, User user) throws JSONException {
        user.firstName = (String) object.get("first_name");
        user.lastName = (String) object.get("last_name");
        user.fbID = (String) object.get("id");
    }

    private static void ExtractLocation(JSONObject object, User user) throws JSONException {
        JSONObject location = (JSONObject)object.get("location");
        user.currentCity = Location.FromJSON(location);
    }
}
