package com.doryapp.dory.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.Location;
import com.doryapp.dory.R;
import com.doryapp.dory.activities.FirebaseUserProvider;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetUserByIdCall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Lorenzo Toso on 24.10.2016.
 */

public class ProfileFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    DoryUser user;
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            showUserMainThread(user);
        }
    };

    private void showUserMainThread(DoryUser user) {
        if(user == null)
            return;
        ((TextView)getActivity().findViewById(R.id.nameView)).setText(user.getFirstName() + " " + user.getLastName());
        Location location = user.getLocation();
        ((TextView)getActivity().findViewById(R.id.locationView)).setText(location != null ? user.getLocation().toString() : "NO LOCATION SET");
        ((TextView)getActivity().findViewById(R.id.emailView)).setText(location != null ? user.getEmailAddress() : "NO EMAIL SET");
        getActivity().findViewById(R.id.pictureView).setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_no_profile_picture));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_ownprofile, container, false);
    }

    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUserProvider userProvider = (FirebaseUserProvider)getActivity();
        FirebaseUser user = userProvider.getUser();

        showUser(user);
    }

    private void showUser(FirebaseUser user) {
        if(user == null)
            showEmptyUser();
        else {
            new GetUserByIdCall(getActivity(), user.getUid()).onComplete(new ApiCall.OnComplete<DoryUser>() {
                @Override
                public void execute(DoryUser user) {
                    showUser(user);
                }
            }).execute();
        }
    }

    private void showEmptyUser() {
        DoryUser emptyUser = new DoryUser();
        emptyUser.setNickName("NO USER");
        emptyUser.setFirstName("NO USER");
        emptyUser.setLastName("NO USER");
        emptyUser.setEmailAddress("NO USER");
        emptyUser.setId("0");

        showUserMainThread(emptyUser);
    }

    private void showUser(DoryUser user) {
        this.user = user;
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        showUser(user);
    }
}

