package com.doryapp.dory;
import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.CreateUserCall;
import com.doryapp.dory.apiCalls.DeleteOwnUserCall;
import com.doryapp.dory.apiCalls.DoesUserExistCall;
import com.doryapp.dory.apiCalls.NopCall;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {
    private static Context con;
    private static FirebaseAuth firebaseAuth;
    private static Resources resources;

    @BeforeClass
    public static void beforeClass() {
        con = InstrumentationRegistry.getTargetContext();
        resources = con.getResources();
        firebaseAuth = FirebaseAuth.getInstance();

        logoutIfNecessary();
    }

    @Before
    public void before()
    {
        signInTestUser();
        boolean userExists = new DoesUserExistCall(con, firebaseAuth.getCurrentUser().getUid()).executeSynchronously();
        if(userExists)
            new DeleteOwnUserCall(con).executeSynchronously();
        logoutIfNecessary();
    }
    @After
    public void after(){
        logoutIfNecessary();
    }


    private static void logoutIfNecessary() {
        if(firebaseAuth.getCurrentUser() != null)
            firebaseAuth.signOut();
    }


    @Test
    public void canConnectToServer()
    {
        new NopCall(con).onException(new ApiCall.OnException() {
            @Override
            public void handle(Exception ex) {
                Assert.fail();
            }
        }).executeSynchronously();
    }

    private FirebaseUser signInTestUser()
    {
        Task<AuthResult> task = firebaseAuth.signInWithEmailAndPassword("test@test.de", "password");
        while(!task.isComplete());

        return task.getResult().getUser();
    }

    @Test
    public void canLogIntoFirebase()
    {
        FirebaseUser user = signInTestUser();
        Assert.assertNotNull(user);

        firebaseAuth.signOut();
        Assert.assertNull(firebaseAuth.getCurrentUser());

    }

    @Test
    public void canCreateUser() {
        FirebaseUser user = signInTestUser();

        boolean userExists = new DoesUserExistCall(con, user.getUid()).executeSynchronously();
        Assert.assertFalse(userExists);

        DoryUser doryUser = new DoryUser();
        doryUser.setFirstName("firstName");
        doryUser.setLastName("lastName");
        doryUser.setNickName("nickName");
        doryUser.setEmailAddress(user.getEmail());
        doryUser.setId(user.getUid());

        new CreateUserCall(con, doryUser).executeSynchronously();

        userExists = new DoesUserExistCall(con, user.getUid()).executeSynchronously();
        Assert.assertTrue(userExists);

        boolean userWasDeleted = new DeleteOwnUserCall(con).executeSynchronously();
        Assert.assertTrue(userWasDeleted);

        userExists = new DoesUserExistCall(con, user.getUid()).executeSynchronously();
        Assert.assertFalse(userExists);
   }
    // TODO test all methods of MyEndpoint
    // TODO Create according apiCalls

}