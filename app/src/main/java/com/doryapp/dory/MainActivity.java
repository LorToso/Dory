package com.doryapp.dory;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doryapp.backend.myApi.model.DoryUser;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Console;
import java.net.URI;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private DoryUser self;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFirebase();
        setupGoogleMap();
    }

    private void setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        mFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser == null) {
                    ChangeText("Not logged in","","");
                    return;
                }
                ChangeText(mFirebaseUser.getDisplayName(),mFirebaseUser.getEmail(),mFirebaseUser.getUid());
            }
        });
//(getResources().getString(R.string.firebase_url));
    }

    private void setupGoogleMap() {
        FragmentManager man = getFragmentManager();
        MapFragment f = (MapFragment) man.findFragmentById(R.id.mapfragment);
        f.getMapAsync(this);
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
//        AccessToken token = AccessToken.getCurrentAccessToken();
//
//        if (token == null) {
//            ChangeText("Not logged in.", "", "");
//        } else {
//            User.GetSelf(new User.GetUserCallback() {
//                @Override
//                public void onUser(User user) {
//                    self = user;
//                    ChangeText(
//                            user.firstName + " " + user.lastName,
//                            user.currentCity.currentCityName + ", " + user.currentCity.currentCityCountry,
//                            user.currentCity.currentCityPosition.toString());
//                    ChangePicture(user.fbProfilePicture);
//                    ChangePosition(user.currentCity.currentCityPosition);
//                }
//            });
//        }
    }
    public void onClickRegister(View v) {
        mFirebaseAuth.createUserWithEmailAndPassword("testcreate@test.de", "password");
    }
    public void onClickLogin(View v) {
        mFirebaseAuth.signInWithEmailAndPassword("test@test.de", "password");
    }

    public void onShowFriends(View v)
    {
//        new FBFriendFinder(new FBFriendFinder.UserListCallback()
//        {
//            @Override
//            public void onUserListLoaded(List<User> users) {
//                if(users.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "No friends use this app", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "Friends found!", Toast.LENGTH_SHORT).show();
//                    for (User user : users) {
//                        AddMarker(user);
//                    }
//                }
//
//            }
//        }).FindFriends();
    }

    private void AddMarker(final DoryUser user) {
//        new DownloadImageTask(new DownloadImageTask.PostDownloadAction() {
//            @Override
//            void onDownloadCompleted(Bitmap result) {
//                MarkerOptions marker = new MarkerOptions().title(user.getFirstName()).position(user.getcurrentCity.currentCityPosition).icon(BitmapDescriptorFactory.fromBitmap(result));
//                googleMap.addMarker(marker);
//            }
//        }).execute(user.fbProfilePicture.toString());
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