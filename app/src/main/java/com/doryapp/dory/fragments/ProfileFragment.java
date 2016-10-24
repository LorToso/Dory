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
        ((TextView)getActivity().findViewById(R.id.nameView)).setText(user.getFirstName() + " " + user.getLastName());
        ((TextView)getActivity().findViewById(R.id.locationView)).setText(user.getLocation().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_ownprofile, container, false);

        return rootView;
    }
    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUserProvider userProvider = (FirebaseUserProvider)getActivity();
        FirebaseUser user = userProvider.getUser();
        new GetUserByIdCall(getActivity(), user.getUid(), new ApiCall.OnComplete<DoryUser>() {
            @Override
            public void execute(DoryUser user) {
                showUser(user);
            }
        });


    }

    private void showUser(DoryUser user) {
        this.user = user;
        mHandler.sendEmptyMessage(0);
    }
}

