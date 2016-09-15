package com.doryapp.dory.activities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.R;
import com.doryapp.dory.UserDetailsView;
import com.doryapp.dory.UserDetailsViewWithAddButton;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetFriendsCall;
import com.doryapp.dory.apiCalls.GetUsersCall;
import com.doryapp.dory.apiCalls.SendFriendRequestCall;

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

    private void filterBy(CharSequence searchedName) {
        List<DoryUser> newList = new ArrayList<>();
        for (DoryUser user : displayedUsers) {
            if(user.getNickName().contains(searchedName))
                newList.add(user);
        }
        displayedUsers = newList;
    }

    private void displayUsers() {
        LinearLayout view = (LinearLayout)findViewById(R.id.userListView);

        if(view.getChildCount() > 0)
            view.removeAllViews();

        View.OnClickListener OnUserClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetailsView detailsView = (UserDetailsView)view;
                sendFriendRequestTo(detailsView.getUser());
            }
        };

        for(DoryUser user : displayedUsers)
        {
            UserDetailsViewWithAddButton detailsView = new UserDetailsViewWithAddButton(this, user);
            detailsView.setOnClickListener(OnUserClicked);
            view.addView(detailsView);
        }

    }

    private void sendFriendRequestTo(DoryUser user) {
        new SendFriendRequestCall(this, user.getId()).execute();
    }
}
