package com.doryapp.dory;
import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.doryapp.dory.apiCalls.AuthedApiCall;
import com.doryapp.dory.apiCalls.DoesUserExistCall;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {//extends ActivityTestRule<MainActivity> {
    Context con;
    FirebaseAuth firebaseAuth;
    Resources resources;

    @Before
    public void before() {
        con = InstrumentationRegistry.getTargetContext();
        resources = con.getResources();
        firebaseAuth = FirebaseAuth.getInstance();
//        String url = resources.getString(R.string.server_url);
//        int i = 0;
    }

    @Test
    public void canCreateUser() {

        Assert.assertNotNull(firebaseAuth.getCurrentUser());

        new DoesUserExistCall(con, firebaseAuth.getCurrentUser().getUid()).onComplete(new AuthedApiCall.OnComplete<Boolean>() {
            @Override
            public void execute(Boolean param) {
                assertFalse(param);
            }
        }).executeSynchronously();
   }
}