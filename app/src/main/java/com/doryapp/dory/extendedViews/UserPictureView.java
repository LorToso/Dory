package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.doryapp.backend.myApi.model.DoryUser;

import static com.doryapp.dory.R.mipmap.ic_no_profile_picture;


public class UserPictureView extends ImageView {

    DoryUser user;


    public UserPictureView(Context context, DoryUser user)
    {
        super(context);
        this.user = user;
        this.setBackgroundResource(ic_no_profile_picture);
    }

    public UserPictureView(Context context) {
        super(context);
    }

    public UserPictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserPictureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public DoryUser getUser() {
        return user;
    }

    // TODO this view should automatically user setImage() and load the users image from the URI provided in the DoryUser-object this is created with

}
