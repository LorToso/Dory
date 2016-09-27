package com.doryapp.dory.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.R;
import com.google.gson.Gson;

public class UserProfileActivity extends AppCompatActivity {

    DoryUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent startIntent = getIntent();
        String userJson = startIntent.getStringExtra("user");

        if(userJson == null)
            return;

        user = new Gson().fromJson(userJson, DoryUser.class);
        int i = 0;
    }
}
