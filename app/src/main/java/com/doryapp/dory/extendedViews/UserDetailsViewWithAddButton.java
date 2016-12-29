package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.backend.myApi.model.FriendshipStatus;
import com.doryapp.dory.FriendshipStatusEnum;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetFriendshipStatusCall;
import com.doryapp.dory.apiCalls.SendFriendRequestCall;

import xdroid.toaster.Toaster;


public class UserDetailsViewWithAddButton extends UserDetailsView {

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            addButton.setText(buttonText);
        }
    };

    private FriendshipStatusEnum friendshipStatus;
    private String buttonText = "";
    private Button addButton;


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
        setupClickListener();
    }

    private void setupClickListener() {
        if(friendshipStatus != FriendshipStatusEnum.NO_FRIEND)
            return;
        new SendFriendRequestCall(getContext(),user.getId()).onComplete(new ApiCall.OnComplete<Void>() {
            @Override
            public void execute(Void param) {
                Toaster.toast("Friend request sent to user " + user.getId());
            }
        }).execute();
    }

    private void updateButtonStatus()
    {
        new GetFriendshipStatusCall(getContext(), user.getId()).onComplete(new ApiCall.OnComplete<FriendshipStatus>() {
            @Override
            public void execute(FriendshipStatus param) {
                friendshipStatus = FriendshipStatusEnum.from(param);
                buttonText = friendshipStatus.getButtonText();
                mHandler.sendEmptyMessage(0);
            }
        }).execute();
    }
}
