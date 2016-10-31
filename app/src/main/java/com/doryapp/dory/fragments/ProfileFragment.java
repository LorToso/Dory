package com.doryapp.dory.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.Location;
import com.doryapp.dory.R;
import com.doryapp.dory.activities.FirebaseUserProvider;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetUserByIdCall;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Lorenzo Toso on 24.10.2016.
 */

public class ProfileFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_ownprofile, container, false);
    }
    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUserProvider userProvider = (FirebaseUserProvider)getActivity();
        FirebaseUser user = userProvider.getUser();
        if(user == null)
            return;
        // TODO this fragment should probably listen to the AuthentificationStateListener of Firebase and show users as soon as they log in
        new GetUserByIdCall(getActivity(), user.getUid()).onComplete(new ApiCall.OnComplete<DoryUser>() {
            @Override
            public void execute(DoryUser user) {
                showUser(user);
            }
        }).execute();
    }

    private void showUser(DoryUser user) {
        this.user = user;
        mHandler.sendEmptyMessage(0);
    }
}

