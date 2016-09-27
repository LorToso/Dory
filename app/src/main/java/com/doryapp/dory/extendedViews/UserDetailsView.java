package com.doryapp.dory.extendedViews;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doryapp.backend.myApi.model.DoryUser;

import static com.doryapp.dory.R.mipmap.ic_no_profile_picture;


public class UserDetailsView extends LinearLayout{

    protected DoryUser user;
    private UserPictureView profileView;

    public UserDetailsView(Context context, DoryUser user) {
        super(context);
        this.user = user;
        setupUserDetailsView(context, user);
    }


    public DoryUser getUser() {
        return user;
    }

    protected void setupUserDetailsView(Context context, DoryUser user) {
        removeAllViews();

        setOrientation(LinearLayout.HORIZONTAL);

        ImageView profilePicture = setupProfilePictureView(context);
        addView(profilePicture);

        LinearLayout detailsLayout = setupDetailsView(context, user);
        addView(detailsLayout);
    }

    @NonNull
    private LinearLayout setupDetailsView(Context context, DoryUser user) {
        LinearLayout detailsLayout = new LinearLayout(context);
        detailsLayout.setOrientation(LinearLayout.VERTICAL);

        TextView nameView = setupNameView(context, user);
        TextView locationView = setupLocationView(context, user);
        detailsLayout.addView(nameView);
        detailsLayout.addView(locationView);
        return detailsLayout;
    }

    @NonNull
    private ImageView setupProfilePictureView(Context context) {
        profileView = new UserPictureView(context, user);
        return profileView;
    }

    @NonNull
    private TextView setupNameView(Context context, DoryUser user) {
        TextView nameView = new TextView(context);
        nameView.setText(user.getFirstName() + " " + user.getLastName());
        return nameView;
    }

    private TextView setupLocationView(Context context, DoryUser user) {
        TextView locationView = new TextView(context);
        locationView.setTextColor(Color.GRAY);

        if(user.getLocation() != null)
            locationView.setText(user.getLocation().getName() + ", " + user.getLocation().getCountry());
        else
            locationView.setText("LOCATION MISSING!"); // TODO this is a bit ugly, isnt it?
        return locationView;
    }

    public void setPictureClickListener(OnClickListener listener)
    {
        profileView.setOnClickListener(listener);
    }
    public ImageView getProfileView()
    {
        return profileView;
    }
}
