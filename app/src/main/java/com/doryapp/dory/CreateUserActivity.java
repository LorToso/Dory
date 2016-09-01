package com.doryapp.dory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doryapp.backend.myApi.model.DoryUser;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void onClickOk(View v)
    {
        DoryUser newUser = new DoryUser();

        Intent intent = this.getIntent();
        // TODO: intent.putExtra("newUser", newUser);
        this.setResult(RESULT_OK, intent);
        finish();
    }
}
