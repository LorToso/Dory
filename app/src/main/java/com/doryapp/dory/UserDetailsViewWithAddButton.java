package com.doryapp.dory;

import android.content.Context;
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


    public UserDetailsViewWithAddButton(Context context, DoryUser user) {
        super(context, user);
    }

    protected void setupUserDetailsView(Context context, DoryUser user)
    {
        super.setupUserDetailsView(context,user);
        setupAddButton(context);
    }

    private void setupAddButton(Context context) {
        Button btn = new Button(context);
        btn.setOnClickListener(listener);
        btn.setText("ADD");
        addView(btn);
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
