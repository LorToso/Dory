package com.doryapp.dory;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

import com.doryapp.backend.myApi.model.DoryUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo Toso on 29.08.2016.
 */
public class UserDetailsViewWithAddButton extends UserDetailsView {

    List<View.OnClickListener> clickListeners = new ArrayList<>();
    View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            for (OnClickListener clickListener : clickListeners) {
                clickListener.onClick(view);
            }
        }
    };
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
        addButton = new Button(context);
        addButton.setOnClickListener(listener);
        addButton.setText("ADD");
        addView(addButton);
    }

    public void setOnClickListener(OnClickListener listener)
    {
        clickListeners.clear();
        clickListeners.add(listener);
    }
    public void addOnClickListener(OnClickListener listener)
    {
        clickListeners.add(listener);
    }
}
