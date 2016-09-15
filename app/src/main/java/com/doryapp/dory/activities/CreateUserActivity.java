package com.doryapp.dory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doryapp.dory.R;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void onClickOk(View v)
    {
        TextView firstNameView = (TextView)findViewById(R.id.firstNameView);
        TextView lastNameView = (TextView)findViewById(R.id.lastNameView);
        TextView nickNameView = (TextView)findViewById(R.id.nickNameView);


        if(!isValidName(firstNameView.getText()))
        {
            Toast.makeText(this,"Invalid first name", Toast.LENGTH_LONG).show();
            return;
        }
        if(!isValidName(lastNameView.getText()))
        {
            Toast.makeText(this,"Invalid last name", Toast.LENGTH_LONG).show();
            return;
        }
        if(!isValidName(nickNameView.getText()))
        {
            Toast.makeText(this,"Invalid nick name", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("firstName", firstNameView.getText().toString());
        intent.putExtra("lastName", lastNameView.getText().toString());
        intent.putExtra("nickName", nickNameView.getText().toString());


        this.setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isValidName(CharSequence name) {
        return name != "";
    }
}
