package com.doryapp.dory.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.R;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetUsersCall;
import com.doryapp.dory.extendedViews.UserDetailsViewWithAddButton;

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
        new GetUsersCall(this).onComplete(new ApiCall.OnComplete<List<DoryUser>>() {
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
        // TODO
        new GetUsersCall(this, nickName).onComplete( new ApiCall.OnComplete<List<DoryUser>>() {
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

    private void displayUsers() {
        LinearLayout view = (LinearLayout)findViewById(R.id.userListView);

        if(view.getChildCount() > 0)
            view.removeAllViews();

        for(DoryUser user : displayedUsers)
        {
            view.addView(new UserDetailsViewWithAddButton(this, user));
        }
    }


}
