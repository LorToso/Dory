package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.widget.Button;

import com.doryapp.backend.myApi.model.DoryUser;

/**
 * Created by Lorenzo Toso on 01.10.2016.
 */

public class FriendRequestView extends UserDetailsView {

    private Button acceptButton;
    private Button ignoreButton;

    public FriendRequestView(Context context, DoryUser user) {
        super(context, user);

        acceptButton = new UserButton(context, user);
        acceptButton.setText("Accept");
        addView(acceptButton);

        ignoreButton = new UserButton(context, user);
        ignoreButton.setText("Ignore");
        addView(ignoreButton);
    }

    public void setAcceptButtonListener(OnClickListener listener) {
        acceptButton.setOnClickListener(listener);
    }
    public void setIgnoreButtonListener(OnClickListener listener) {
        ignoreButton.setOnClickListener(listener);
    }
}
