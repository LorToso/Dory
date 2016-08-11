package com.doryapp.dory;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private CallbackManager callbackManager;
    private GoogleMap googleMap;
    private User self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        AppEventsLogger.activateApp(this);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        SetupFacebookLoginButton();
        SetupGoogleMap();
    }

    private void SetupGoogleMap() {
        FragmentManager man = getFragmentManager();
        MapFragment f = (MapFragment) man.findFragmentById(R.id.mapfragment);
        f.getMapAsync(this);
    }

    private void SetupFacebookLoginButton() {
        callbackManager = CallbackManager.Factory.create();


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_location"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ChangeText("Facebook login succeeded", "", "");

            }

            @Override
            public void onCancel() {

                ChangeText("Facebook login canceled", "", "");
            }

            @Override
            public void onError(FacebookException error) {

                ChangeText("Facebook login error:", error.toString(), "");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onResume() {
        super.onResume();
    }

    private void ChangeText(String text1, String text2, String text3) {
        ((TextView) findViewById(R.id.TextView1)).setText(text1);
        ((TextView) findViewById(R.id.TextView2)).setText(text2);
        ((TextView) findViewById(R.id.TextView3)).setText(text3);
    }

    public void onButtonClick(View v) {
        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token == null) {
            ChangeText("Not logged in.", "", "");
        } else {
            User.GetSelf(new User.GetUserCallback() {
                @Override
                public void onUser(User user) {
                    self = user;
                    ChangeText(
                            user.firstName + " " + user.lastName,
                            user.currentCity.currentCityName + ", " + user.currentCity.currentCityCountry,
                            user.currentCity.currentCityPosition.toString());
                    ChangePicture(user.fbProfilePicture);
                    ChangePosition(user.currentCity.currentCityPosition);
                }
            });
        }
    }
    public void onShowFriends(View v)
    {
        new FBFriendFinder(new FBFriendFinder.UserListCallback()
        {
            @Override
            public void onUserListLoaded(List<User> users) {
                if(users.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No friends use this app", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Friends found!", Toast.LENGTH_SHORT).show();
                    for (User user : users) {
                        AddMarker(user);
                    }
                }

            }
        }).FindFriends();
    }

    private void AddMarker(final User user) {
        new DownloadImageTask(new DownloadImageTask.PostDownloadAction() {
            @Override
            void onDownloadCompleted(Bitmap result) {
                MarkerOptions marker = new MarkerOptions().title(user.firstName).position(user.currentCity.currentCityPosition).icon(BitmapDescriptorFactory.fromBitmap(result));
                googleMap.addMarker(marker);
            }
        }).execute(user.fbProfilePicture.toString());
    }


    private void ChangePosition(final LatLng position) {
        AddMarker(self);
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(position, 10.0f);
        googleMap.moveCamera(camera);
    }

    private void ChangePicture(URI fbProfilePicture) {
        new DownloadImageTask(new DownloadImageTask.PostDownloadAction() {
            @Override
            void onDownloadCompleted(Bitmap result) {
                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(result);
            }
        }).execute(fbProfilePicture.toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
    }
}