package com.doryapp.dory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
    }
    public void onClickSearch(View v)
    {
        SearchView searchView  = (SearchView)v;
        CharSequence searchedName = searchView.getQuery();
        MyApi a; // GetApi
        List<DoryUser> matchingUsers = a.getUsersByNickName(searchedName.toString());
        displayUsers(matchingUsers);
    }

    private void displayUsers(List<DoryUser> users) {
        ListView view = (ListView)findViewById(R.id.listView);


    }
}
