package com.doryapp.dory;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.CharSequence;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.DoryUserCollection;
import com.doryapp.backend.myApi.model.FriendshipCollection;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
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


        if(mFirebaseUser == null)
            showLoginUI();

//(getResources().getString(R.string.firebase_url));
    }

    private void showLoginUI() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.EMAIL_PROVIDER/*,
                                AuthUI.GOOGLE_PROVIDER,
                                AuthUI.FACEBOOK_PROVIDER*/)
                        .build(),
                0);//RC_SIGN_IN);
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
        //mFirebaseAuth.createUserWithEmailAndPassword("testcreate@test.de", "password");
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

    public void onShowFriends(View v)
    {
        Task<GetTokenResult> task = mFirebaseUser.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            String token = task.getResult().getToken();


                            new AsyncTask<String, Void, List<DoryUser>>()
                            {
                                @Override
                                protected List<DoryUser> doInBackground(String... tokens) {
                                    MyApi api = Api.get(MainActivity.this);
                                    List<DoryUser> users = null;
                                    try {
                                        //CharSequence s = api.test().execute();
                                        //FriendshipCollection c = api.getFriendships("123").execute();
                                        DoryUserCollection c =api.getFriends("abc").execute();
                                        if(c == null)
                                            return null;
                                        users = c.getItems();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                    Toast.makeText(MainActivity.this,"worked!", Toast.LENGTH_LONG).show();
                                    return users;
                                }
                            }.execute(token);

                        } else {
                            Toast.makeText(MainActivity.this,"Could not find friends", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    public void onClickAddFriend(View v)
    {
        Intent startActivity = new Intent(this,AddFriendActivity.class);
        Long TOKEN = 0L; // TODO Obtain token or whole User?
        startActivity.putExtra("user", TOKEN);
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


    private void ChangePosition(final LatLng position) {
        //AddMarker(self);
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