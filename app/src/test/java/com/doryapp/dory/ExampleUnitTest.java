package com.doryapp.dory;

import com.doryapp.backend.doryUserApi.DoryUserApi;
import com.doryapp.backend.doryUserApi.model.DoryUser;
import com.doryapp.backend.friendshipApi.FriendshipApi;
import com.doryapp.backend.myApi.MyApi;
import com.doryapp.backend.myApi.model.Friendship;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class ExampleUnitTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper();

    @Before
    public void setUp() throws IOException {
        helper.setUp();
        resetTestData();

    }

    private void resetTestData() throws IOException {
        DoryUserApi userApi = getDoryUserApi();
        FriendshipApi friendApi = getFriendshipApi();
        MyApi api = getMyApi();

        for (Long id : TestUsers.TestUserIds) {
            DoryUser user = TestUsers.get(id);


            List<Friendship> friends = api.getFriendships("0").execute().getItems();
            if(DoesUserExist(userApi,user))
                userApi.remove(id).execute();

            deleteAllFriendships(friendApi, friends);
        }
    }

    private void deleteAllFriendships(FriendshipApi friendApi, List<Friendship> friends) throws IOException {
        for (Friendship friendship : friends) {
            friendApi.remove(friendship.getId()).execute();
        }
    }

    @After
    public void tearDown() throws IOException {
        resetTestData();
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
        MyApi myApi = getMyApi();

        com.doryapp.backend.friendshipApi.model.Friendship friendship = new com.doryapp.backend.friendshipApi.model.Friendship();
        friendship.setUser1(user1.getId());
        friendship.setUser2(user2.getId());
        friendshipApi.insert(friendship).execute();

        List<com.doryapp.backend.myApi.model.DoryUser> friendsOfUser1 = myApi.getFriends("0").execute().getItems();

        Assert.assertEquals(1,friendsOfUser1.size());
        AssertInconvertibleTypes(friendsOfUser1.get(0),user2);
    }

    private void AssertInconvertibleTypes(com.doryapp.backend.myApi.model.DoryUser user1, DoryUser user2) {
        Assert.assertEquals(user1.getFirstName(), user2.getFirstName());
        Assert.assertEquals(user1.getLastName(), user2.getLastName());
        Assert.assertEquals(user1.getId(), user2.getId());
        Assert.assertEquals(user1.getLocation(), user2.getLocation());
    }

    private void SafeInsertUser(DoryUser user) throws IOException {
        DoryUserApi userApi = getDoryUserApi();

        Assert.assertFalse(DoesUserExist(userApi,user));
        userApi.insert(user).execute();
        Assert.assertTrue(DoesUserExist(userApi,user));
    }


    private static DoryUserApi getDoryUserApi() throws IOException {
        DoryUserApi.Builder helloWorld = new DoryUserApi.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), null);
        helloWorld.setApplicationName("doryUserApi");
        helloWorld.setRootUrl("http://localhost:8080/_ah/api/");
        return helloWorld.build();
    }
    private static FriendshipApi getFriendshipApi() throws IOException {
        FriendshipApi.Builder helloWorld = new FriendshipApi.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), null);
        helloWorld.setApplicationName("friendshipApi");
        helloWorld.setRootUrl("http://localhost:8080/_ah/api/");
        return helloWorld.build();
    }
    private static MyApi getMyApi() throws IOException {
        MyApi.Builder helloWorld = new MyApi.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), null);
        helloWorld.setApplicationName("myApi");
        helloWorld.setRootUrl("http://localhost:8080/_ah/api/");
       return helloWorld.build();
    }
}