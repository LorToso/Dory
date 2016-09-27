package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.widget.Button;

import com.doryapp.backend.myApi.model.DoryUser;


public class UserDetailsViewWithAddButton extends UserDetailsView {
    Button addButton;

    public UserDetailsViewWithAddButton(Context context, DoryUser user) {
        super(context, user);
    }

    protected void setupUserDetailsView(Context context, DoryUser user)
    {
        super.setupUserDetailsView(context,user);
        setupAddButton(context);
    }

    private void setupAddButton(Context context) {
        addButton = new UserButton(context, user);
        addButton.setText("ADD");
        addView(addButton);
    }

    public void setButtonClickListener(OnClickListener listener)
    {
        addButton.setOnClickListener(listener);
    }
}
