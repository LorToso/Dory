package com.doryapp.dory.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.FriendshipRequest;
import com.doryapp.backend.myApi.model.Location;
import com.doryapp.dory.R;
import com.doryapp.dory.apiCalls.AcceptFriendRequestCall;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.AuthedApiCall;
import com.doryapp.dory.apiCalls.CreateUserCall;
import com.doryapp.dory.apiCalls.DoesUserExistCall;
import com.doryapp.dory.apiCalls.GetFriendRequestCall;
import com.doryapp.dory.apiCalls.GetUserByIdCall;
import com.doryapp.dory.apiCalls.IgnoreFriendRequestCall;
import com.doryapp.dory.extendedViews.FriendRequestView;
import com.doryapp.dory.extendedViews.UserButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    List<DoryUser> displayedUsers = new ArrayList<>();
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            displayRequests();
        }
    };

    private void displayRequests() {

        final LinearLayout layout = (LinearLayout) findViewById(R.id.friendRequest);

        final View.OnClickListener acceptListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserButton btn = (UserButton) v;
                new AcceptFriendRequestCall(MainActivity.this, btn.getUser().getId()).execute();
            }
        };
        final View.OnClickListener ignoreListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserButton btn = (UserButton) v;
                new IgnoreFriendRequestCall(MainActivity.this, btn.getUser().getId()).execute();
            }
        };

        for (DoryUser user : displayedUsers) {
            FriendRequestView reqView = new FriendRequestView(MainActivity.this, user);
            reqView.setAcceptButtonListener(acceptListener);
            reqView.setIgnoreButtonListener(ignoreListener);
            layout.addView(reqView);
        }
    }

    private GoogleMap googleMap;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFirebase();
        setupGoogleMap();
        setupFriendRequestFields();
    }

    private void setupFriendRequestFields() {

        displayedUsers.clear();

        new GetFriendRequestCall(this, new ApiCall.OnComplete<List<FriendshipRequest>>() {
            @Override
            public void execute(List<FriendshipRequest> param) {
                for (FriendshipRequest request : param) {
                    String senderId = request.getFriendship().getUser1();
                    if(senderId.equals(mFirebaseUser.getUid()))
                        continue;

                    new GetUserByIdCall(MainActivity.this, senderId, new ApiCall.OnComplete<DoryUser>() {
                        @Override
                        public void execute(DoryUser user) {
                            displayedUsers.add(user);
                        }
                    }).execute();

                    mHandler.sendEmptyMessage(0);
                }
            }
        }).execute();
    }

    private void setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        mFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                applyTextChangesBasedOnLoggedInUser();
            }
        });


        if(mFirebaseUser == null)
            showLoginUI();

        if(mFirebaseUser != null)
            startCreateUserActivityIfNecessary();
    }

    private void applyTextChangesBasedOnLoggedInUser() {
        if(mFirebaseUser == null) {
            ChangeText("Not logged in","","");
            return;
        }
        ChangeText(mFirebaseUser.getDisplayName(),mFirebaseUser.getEmail(),mFirebaseUser.getUid());
    }

    private void showLoginUI() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.EMAIL_PROVIDER,
                                AuthUI.GOOGLE_PROVIDER,
                                AuthUI.FACEBOOK_PROVIDER)
                        .build(),
                getResources().getInteger(R.integer.login_request_code));//RC_SIGN_IN);
    }

    private void startCreateUserActivityIfNecessary() {
        new DoesUserExistCall(this, mFirebaseUser.getUid(), new AuthedApiCall.OnComplete<Boolean>() {
            @Override
            public void execute(Boolean userExists) {
                if(userExists)
                   return;
                Intent startActivity = new Intent(MainActivity.this,CreateUserActivity.class);
                startActivityForResult(startActivity, getResources().getInteger(R.integer.create_user_request_code));
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == getResources().getInteger(R.integer.create_user_request_code)) {
            handleCreateUserResult(resultCode, data);
        }
        else if(requestCode == getResources().getInteger(R.integer.login_request_code)) {
            handleLoginUserResult(resultCode, data);
        }
    }

    private void handleLoginUserResult(int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
            return;
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        startCreateUserActivityIfNecessary();
    }

    private void handleCreateUserResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        Toast.makeText(this,"User will be created", Toast.LENGTH_LONG).show();

        DoryUser user = new DoryUser();
        user.setId(mFirebaseUser.getUid());
        user.setFirstName(data.getStringExtra("firstName"));
        user.setLastName(data.getStringExtra("lastName"));
        user.setNickName(data.getStringExtra("nickName"));
        Location loc = new Location();
        user.setLocation(loc);
        new CreateUserCall(this, user).execute();
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

    public void onClickLogin(View v) {
        if(mFirebaseUser != null)
            return;
        showLoginUI();
    }
    public void onClickLogout(View v)
    {
        mFirebaseAuth.signOut();
        mFirebaseUser = mFirebaseAuth.getCurrentUser(); // Should be null
    }

    public void onClickAddFriend(View v)
    {
        if(mFirebaseUser == null)
            return;
        Intent startActivity = new Intent(this,AddFriendActivity.class);
        startActivity(startActivity);
    }


    private void AddMarker(final DoryUser user) {
        MarkerOptions marker = new MarkerOptions().title(user.getFirstName()).position(new LatLng(0,0));
        googleMap.addMarker(marker);
//        new DownloadImageTask(new DownloadImageTask.PostDownloadAction() {
//            @Override
//            void onDownloadCompleted(Bitmap result) {
//                MarkerOptions marker = new MarkerOptions().title(user.getFirstName()).position(user.getcurrentCity.currentCityPosition).icon(BitmapDescriptorFactory.fromBitmap(result));
//                googleMap.addMarker(marker);
//            }
//        }).execute(user.fbProfilePicture.toString());
    }


//    private void ChangePosition(final LatLng position) {
//        //AddMarker(self);
//        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(position, 10.0f);
//        googleMap.moveCamera(camera);
//    }

//    private void ChangePicture(URI fbProfilePicture) {
//        new DownloadImageTask(new DownloadImageTask.PostDownloadAction() {
//            @Override
//            void onDownloadCompleted(Bitmap result) {
//                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(result);
//            }
//        }).execute(fbProfilePicture.toString());
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
    }
}