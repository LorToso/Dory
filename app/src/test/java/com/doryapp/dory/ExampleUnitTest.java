package com.doryapp.dory;

import com.doryapp.backend.doryUserApi.DoryUserApi;
import com.doryapp.backend.doryUserApi.model.DoryUser;
import com.doryapp.backend.friendshipApi.FriendshipApi;
import com.doryapp.backend.friendshipApi.model.Friendship;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper();

    @Before
    public void setUp() throws IOException {
        helper.setUp();
        DoryUserApi userApi = getDoryUserApi();

        for (Long id : TestUsers.TestUserIds) {
            if(DoesUserExist(userApi,TestUsers.get(id)))
                userApi.remove(id).execute();
        }
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void canInsertAndRemoveUser() throws IOException {
        DoryUserApi api = getDoryUserApi();

        DoryUser testUser = TestUsers.get(123L);

        Assert.assertFalse(DoesUserExist(api, testUser));

        api.insert(testUser).execute();
        Assert.assertTrue(DoesUserExist(api, testUser));

        DoryUser queryUser = api.get(testUser.getId()).execute();
        Assert.assertEquals(testUser,queryUser);

        api.remove(testUser.getId()).execute();
        Assert.assertFalse(DoesUserExist(api, testUser));
    }

    private boolean DoesUserExist(DoryUserApi api, DoryUser user) throws IOException {
        try {
            DoryUser queryUser = api.get(user.getId()).execute();
            Assert.assertFalse(queryUser == null);
            return true;
        }
        catch (GoogleJsonResponseException exception)
        {
            String message = exception.getMessage();
            if(message.startsWith("404"))
                return false;
        }
        Assert.fail("Unexpected response.");
        return false;
    }


    @Test
    public void canAddFriendship() throws IOException {


        DoryUser user1 = TestUsers.get(1L);
        DoryUser user2 = TestUsers.get(2L);

        SafeInsertUser(user1);
        SafeInsertUser(user2);

        FriendshipApi friendshipApi = getFriendshipApi();

        Friendship friendship = new Friendship();
        friendship.setUser1(user1.getId());
        friendship.setUser2(user2.getId());
        friendshipApi.insert(friendship);



    }

    private void SafeInsertUser(DoryUser user) throws IOException {
        DoryUserApi userApi = getDoryUserApi();

        Assert.assertFalse(DoesUserExist(userApi,user));
        userApi.insert(user).execute();
        Assert.assertTrue(DoesUserExist(userApi,user));
    }


    private static DoryUserApi getDoryUserApi() throws IOException {
        // Use a builder to help formulate the API request.
        DoryUserApi.Builder helloWorld = new DoryUserApi.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), null);
        helloWorld.setApplicationName("doryUserApi");
        // If running the Cloud Endpoint API locally then point the API stub there by un-commenting the
        // next line.
        helloWorld.setRootUrl("http://localhost:8080/_ah/api/");

        return helloWorld.build();
    }
    private static FriendshipApi getFriendshipApi() throws IOException {
        // Use a builder to help formulate the API request.
        FriendshipApi.Builder helloWorld = new FriendshipApi.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), null);
        helloWorld.setApplicationName("friendshipApi");
        // If running the Cloud Endpoint API locally then point the API stub there by un-commenting the
        // next line.
        helloWorld.setRootUrl("http://localhost:8080/_ah/api/");

        return helloWorld.build();
    }
}