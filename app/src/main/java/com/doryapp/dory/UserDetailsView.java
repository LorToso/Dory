package com.doryapp.dory;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doryapp.backend.myApi.model.DoryUser;

/**
 * Created by Lorenzo Toso on 29.08.2016.
 */
public class UserDetailsView extends LinearLayout{

    protected DoryUser user;

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

        setOrientation(LinearLayout.VERTICAL);

        ImageView profilePicture = setupProfilePictureView(context);
        addView(profilePicture);

        LinearLayout detailsLayout = setupDetailsView(context, user);
        addView(detailsLayout);
    }

    @NonNull
    private LinearLayout setupDetailsView(Context context, DoryUser user) {
        LinearLayout detailsLayout = new LinearLayout(context);
        detailsLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView nameView = setupNameView(context, user);
        setupLocationView(context, user);
        detailsLayout.addView(nameView);
        return detailsLayout;
    }

    @NonNull
    private ImageView setupProfilePictureView(Context context) {
        return new ImageView(context);
    }

    @NonNull
    private TextView setupNameView(Context context, DoryUser user) {
        TextView nameView = new TextView(context);
        nameView.setText(user.getFirstName() + " " + user.getLastName());
        return nameView;
    }

    private void setupLocationView(Context context, DoryUser user) {
        TextView locationView = new TextView(context);
        locationView.setTextColor(Color.GRAY);
        locationView.setText(user.getLocation().getName() + ", " + user.getLocation().getCountry());
    }

}
