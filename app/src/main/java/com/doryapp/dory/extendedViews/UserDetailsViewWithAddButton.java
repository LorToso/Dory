package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.FriendshipStatus;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetFriendshipStatusCall;


public class UserDetailsViewWithAddButton extends UserDetailsView {
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            addButton.setText(buttonText);
        }
    };

    String buttonText = "";
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
        addView(addButton);
        updateButtonStatus();
    }

    private void updateButtonStatus()
    {
        new GetFriendshipStatusCall(getContext(), user.getId(), new ApiCall.OnComplete<FriendshipStatus>() {
            @Override
            public void execute(FriendshipStatus param) {
                switch (param.getFriendshipStatus())
                {
                    case "SELF":
                        buttonText = "You";
                        break;
                    case "NO_FRIEND":
                        buttonText = "Add";
                        break;
                    case "FRIEND":
                        buttonText = "Friend";
                        break;
                    case "REQUEST_SENT":
                        buttonText = "Sent";
                        break;
                    case "REQUEST_PENDING":
                        buttonText = "Accept";
                        break;
                }
                mHandler.sendEmptyMessage(0);
            }
        }).execute();
    }

    public void setButtonClickListener(OnClickListener listener)
    {
        addButton.setOnClickListener(listener);
    }
}
