package com.doryapp.dory;


import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class AuthentificationTests{

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    Activity mActivity;

    public AuthentificationTests() {
        //super(Activity.class);
    }

    @Before
    public void setUp() throws Exception {

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        //injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        //mActivity = getActivity();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }
    @Before
    public void before()
    {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser != null){
            mFirebaseAuth.signOut();
            mFirebaseUser = null;
        }
    }

    @Test
    public void testCanUserLogin()
    {
        Assert.assertNull(mFirebaseUser);
        Assert.assertNotNull(mFirebaseAuth);

        mFirebaseAuth.signInWithEmailAndPassword(TestUsers.TEST_USER_EMAIL, TestUsers.TEST_USER_PASSWORD);

        Assert.assertNotNull(mFirebaseUser);
        Assert.assertEquals(TestUsers.TEST_USER_EMAIL, mFirebaseUser.getEmail());
    }
}

