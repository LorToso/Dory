package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

import com.doryapp.backend.myApi.model.DoryUser;

public class UserButton extends Button {

    private DoryUser user;


    public UserButton(Context context, DoryUser user) {
        super(context);
        this.user = user;
    }

    public UserButton(Context context) {
        super(context);
    }

    public UserButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DoryUser getUser() {
        return user;
    }
}
