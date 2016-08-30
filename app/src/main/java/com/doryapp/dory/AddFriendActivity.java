package com.doryapp.dory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;

import java.io.IOException;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        // TODO Obtain token through Intent
    }
    public void onClickSearch(View v) throws IOException {
        SearchView searchView  = (SearchView)v;
        CharSequence searchedName = searchView.getQuery();
        MyApi a = Api.get(this); // GetApi
        List<DoryUser> matchingUsers = a.getUsersByNickName(searchedName.toString()).execute().getItems();
        displayUsers(matchingUsers);
    }

    private void displayUsers(List<DoryUser> users) {
        ListView view = (ListView)findViewById(R.id.listView);
        view.removeAllViews();


        View.OnClickListener OnUserClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetailsView detailsView = (UserDetailsView)view;
                sendFriendRequest(detailsView.getUser());
            }
        };

        for(DoryUser user : users)
        {
            UserDetailsViewWithAddButton detailsView = new UserDetailsViewWithAddButton(this, user);
            detailsView.setOnClickListener(OnUserClicked);
            view.addView(detailsView);
        }

    }

    private void sendFriendRequest(DoryUser user) {
        MyApi a = Api.get(this);
        //a.sendFriendRequest(user.getId());
    }
}
