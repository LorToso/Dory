package com.doryapp.dory.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.R;
import com.doryapp.dory.activities.AddFriendActivity;
import com.doryapp.dory.activities.CreateUserActivity;
import com.doryapp.dory.activities.MainActivity;
import com.doryapp.dory.activities.UserProfileActivity;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetFriendsByNicknameCall;
import com.doryapp.dory.apiCalls.GetFriendsCall;
import com.doryapp.dory.apiCalls.GetUsersCall;
import com.doryapp.dory.apiCalls.SendFriendRequestCall;
import com.doryapp.dory.extendedViews.UserButton;
import com.doryapp.dory.extendedViews.UserDetailsViewWithAddButton;
import com.doryapp.dory.extendedViews.UserPictureView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo Toso on 24.10.2016.
 */

public class FriendsFragment extends Fragment {

    List<DoryUser> displayedUsers = new ArrayList<>();
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            displayUsers();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        findAllUsers();
    }

    private void findAllUsers() {
        new GetFriendsCall(getActivity()).onComplete(new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                displayedUsers = users;
                notifyUIThread();
            }
        }).execute();
    }

    private void notifyUIThread() {
        mHandler.sendEmptyMessage(0);
    }

    private void findUsersWithFilter(String nickName) {
        new GetFriendsByNicknameCall(getActivity(), nickName).onComplete(new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                displayedUsers = users;
                notifyUIThread();
            }
        }).execute();
    }

    public void onClickSearch(View v) throws IOException {
        SearchView searchView  = (SearchView)v;
        CharSequence searchedName = searchView.getQuery();
        findUsersWithFilter(searchedName.toString());
    }

    public void onClickAddFriend(View v)
    {
        Intent startActivity = new Intent(getActivity(),AddFriendActivity.class);
        startActivityForResult(startActivity, 0);
    }

    private void displayUsers() {
        LinearLayout view = (LinearLayout)getActivity().findViewById(R.id.userListView);

        if(view.getChildCount() > 0)
            view.removeAllViews();

        View.OnClickListener SendFriendRequest = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserButton detailsView = (UserButton)view;
                sendFriendRequestTo(detailsView.getUser());
            }
        };
        View.OnClickListener ShowProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPictureView pictureView = (UserPictureView)view;
                showProfile(pictureView.getUser());
            }
        };

        for(DoryUser user : displayedUsers)
        {
            UserDetailsViewWithAddButton detailsView = new UserDetailsViewWithAddButton(getActivity(), user);
            detailsView.setButtonClickListener(SendFriendRequest);
            detailsView.setPictureClickListener(ShowProfile);
            view.addView(detailsView);
        }

    }

    private void showProfile(DoryUser user) {
        Intent startActivity = new Intent(getActivity(),UserProfileActivity.class);
        startActivity.putExtra("user", new Gson().toJson(user));

        startActivity(startActivity);
    }

    private void sendFriendRequestTo(DoryUser user) {
        new SendFriendRequestCall(getActivity(), user.getId()).execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(
                R.layout.fragment_friends, container, false);


        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.btnAddFriendFloating);

        if(button != null)
        {
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onClickAddFriend(v);
                }
            });
        }

        return view;
    }
}

