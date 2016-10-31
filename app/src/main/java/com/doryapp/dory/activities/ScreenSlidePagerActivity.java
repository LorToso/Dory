package com.doryapp.dory.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.Location;
import com.doryapp.dory.R;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.AuthedApiCall;
import com.doryapp.dory.apiCalls.CreateUserCall;
import com.doryapp.dory.apiCalls.DoesUserExistCall;
import com.doryapp.dory.apiCalls.NopCall;
import com.doryapp.dory.extendedViews.ScreenSlidePagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import xdroid.toaster.Toaster;


public class ScreenSlidePagerActivity extends Activity implements FirebaseUserProvider{

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        setupViewPager();
        setupFirebase();
        checkServerConnection();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void checkServerConnection() {
        new NopCall(this).onComplete(new ApiCall.OnComplete<Void>() {
            @Override
            public void execute(Void v) {
                Toaster.toast("Server connected");
            }
        }).onException(new ApiCall.OnException() {
            @Override
            public void handle(Exception ex) {
                Toaster.toast("SERVER CONNECTION EXCEPTION!");
                ex.printStackTrace();
            }
        }).execute();
    }

    private void setupViewPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        mFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
            }
        });


        if (mFirebaseUser == null)
            showLoginUI();

        if (mFirebaseUser != null)
            startCreateUserActivityIfNecessary();
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
        new DoesUserExistCall(this, mFirebaseUser.getUid()).onComplete(new AuthedApiCall.OnComplete<Boolean>() {
            @Override
            public void execute(Boolean userExists) {
                if (userExists)
                    return;
                Intent startActivity = new Intent(ScreenSlidePagerActivity.this, CreateUserActivity.class);
                startActivityForResult(startActivity, getResources().getInteger(R.integer.create_user_request_code));
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == getResources().getInteger(R.integer.create_user_request_code)) {
            handleCreateUserResult(resultCode, data);
        } else if (requestCode == getResources().getInteger(R.integer.login_request_code)) {
            handleLoginUserResult(resultCode, data);
        }
    }

    private void handleLoginUserResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
        {
            forceUserLogin();
            return;
        }

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        startCreateUserActivityIfNecessary();
        Toaster.toast("User: " + mFirebaseUser.getEmail());
    }

    private void forceUserLogin() {
        Toaster.toast("You have to log in!");
        showLoginUI();
    }

    private void handleCreateUserResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        Toast.makeText(this, "User will be created", Toast.LENGTH_LONG).show();

        DoryUser user = new DoryUser();
        user.setId(mFirebaseUser.getUid());
        user.setFirstName(data.getStringExtra("firstName"));
        user.setLastName(data.getStringExtra("lastName"));
        user.setNickName(data.getStringExtra("nickName"));
        Location loc = new Location();
        user.setLocation(loc);
        new CreateUserCall(this, user).execute();
    }

    public void onClickTabMe(View view) {
        mPager.setCurrentItem(0);
    }

    public void onClickTabMap(View view) {
        mPager.setCurrentItem(1);
    }

    public void onClickTabFriends(View view) {
        mPager.setCurrentItem(2);
    }

    public void onClickTabCode(View view) {
        mPager.setCurrentItem(3);
    }

    public void onClickTabOptions(View view) {
        mPager.setCurrentItem(4);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Dory Main Activity")
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public FirebaseUser getUser() {
        return mFirebaseUser;
    }
}