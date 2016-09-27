package com.doryapp.dory.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.R;
import com.doryapp.dory.extendedViews.UserButton;
import com.doryapp.dory.extendedViews.UserDetailsViewWithAddButton;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetUsersCall;
import com.doryapp.dory.apiCalls.SendFriendRequestCall;
import com.doryapp.dory.extendedViews.UserPictureView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    List<DoryUser> displayedUsers = new ArrayList<>();
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            displayUsers();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
    }
    @Override
    public void onStart()
    {
        super.onStart();
        findAllUsers();
    }

    private void findAllUsers() {
        new GetUsersCall(this, new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                displayedUsers = users;
                notifyUIThread();
            }
        }).execute();
    }

    private void notifyUIThread() {
        Message m = new Message();
        m.setTarget(mHandler);
        m.sendToTarget();
    }

    private void findUsersWithFilter(String nickName) {
        new GetUsersCall(this, new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                displayedUsers = users;
                notifyUIThread();
            }
        }, nickName).execute();
    }

    public void onClickSearch(View v) throws IOException {
        SearchView searchView  = (SearchView)v;
        CharSequence searchedName = searchView.getQuery();
        findUsersWithFilter(searchedName.toString());
    }

    private void displayUsers() {
        LinearLayout view = (LinearLayout)findViewById(R.id.userListView);

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
            UserDetailsViewWithAddButton detailsView = new UserDetailsViewWithAddButton(this, user);
            detailsView.setButtonClickListener(SendFriendRequest);
            detailsView.setPictureClickListener(ShowProfile);
            view.addView(detailsView);
        }

    }

    private void showProfile(DoryUser user) {
        Intent startActivity = new Intent(this,UserProfileActivity.class);
        startActivity.putExtra("user", new Gson().toJson(user));

        startActivity(startActivity);
    }

    private void sendFriendRequestTo(DoryUser user) {
        new SendFriendRequestCall(this, user.getId()).execute();
    }
}
