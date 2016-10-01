/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.doryapp.backend;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;


/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  authenticators = {FirebaseAuthenticator.class},
  namespace = @ApiNamespace(
    ownerDomain = "backend.doryapp.com",
    ownerName = "backend.doryapp.com"
  )
)
public class MyEndpoint {

    static
    {
        ObjectifyService.register(Friendship.class);
        ObjectifyService.register(DoryUser.class);
    }

    public MyEndpoint() throws FileNotFoundException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("Dory-4ab9c906117c.json"))
                .setDatabaseUrl("https://dory-1469989006683.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
    }


    @ApiMethod(name = "getFriendships", path = "getFriendships")
    public List<Friendship> getFriendships(User user)
    {
        LoadType<Friendship> loader = ofy().load().type(Friendship.class);
        List<Friendship> friends1 = loader.filter("user1 ==", user.getId()).list();
        List<Friendship> friends2 = loader.filter("user2 ==", user.getId()).list();

        friends1.addAll(friends2);
        return friends1;
    }
    @ApiMethod(name = "getFriends", path = "friends")
    public List<DoryUser> getFriends(User user) {

        if(user == null)
            return null;

        List<Friendship> friendships = getFriendships(user);
        List<DoryUser> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
            String idToLoad = user.getId().equals(friendship.getUser1()) ? friendship.getUser2() : friendship.getUser1();

            DoryUser doryUser = ofy().load().type(DoryUser.class).id(idToLoad).now();
            friends.add(doryUser);
        }
        return friends;
    }
    @ApiMethod(name = "getUsersByNickName", path = "users")
    public List<DoryUser> getUsersByNickName(@Named("searchedName") String searchedName)
    {
        // TODO: Can be exploited to retrieve all email addresses?
        List<DoryUser> allUsers = ofy().load().type(DoryUser.class).list();
        List<DoryUser> foundUsers = new ArrayList<>();
        searchedName = searchedName.toLowerCase();

        for (DoryUser user : allUsers) {
            String nick = user.getNickName().toLowerCase();
            if (nick.contains(searchedName))
                foundUsers.add(user);
        }

        return foundUsers;
    }

    @ApiMethod(name = "test", path = "testMethod")
    public CharSequence test()
    {
        return "test";
    }

    // This method should be a POST-Type, but since that appears to be bugged (Throws HTTP 500) it is set as GET instead
    @ApiMethod(name = "sendFriendRequest", path = "sendRequest", httpMethod = ApiMethod.HttpMethod.GET)
    public void sendFriendRequest(@Named("friendId") String friendId, User user)
    {
        if(user == null)
            return;
        if(user.getId().equals(friendId))
            return;
        if(!doesUserExist(friendId).valid)
            return;

        Friendship friendship = new Friendship();
        friendship.setUser1(user.getId());
        friendship.setUser2(friendId);
        friendship.setTime(DateTime.now());

        FriendshipRequest req = new FriendshipRequest();
        req.setFriendship(friendship);
        ofy().save().entity(req).now();
    }

    @ApiMethod(name = "acceptFriendRequest", path = "acceptRequest")
    public void acceptFriendRequest(@Named("requestID") String requestID, User user)
    {
        FriendshipRequest request = ofy().load().type(FriendshipRequest.class).id(requestID).now();
        if(request == null)
            return;

        Friendship newFriendship = request.getFriendship();
        if(!newFriendship.getUser2().equals(user.getId()))
            return;

        if(doesUserExist(newFriendship.getUser1()).valid)
            safeUser(newFriendship);

        deleteUser(requestID);
    }

    private Key<Friendship> safeUser(Friendship newFriendship) {
        return ofy().save().entity(newFriendship).now();
    }

    private Void deleteUser(@Named("requestID") String requestID) {
        return ofy().delete().type(FriendshipRequest.class).id(requestID).now();
    }

    @ApiMethod(name = "ignoreFriendRequest", path = "ignoreRequest")
    public void ignoreFriendRequest(@Named("requestID") Long requestID, User user)
    {
        if(user == null)
            return;

        FriendshipRequest request = ofy().load().type(FriendshipRequest.class).id(requestID).now();

        if(request == null)
            return;

        if(     !request.getFriendship().getUser1().equals(user.getId()) &&
                !request.getFriendship().getUser2().equals(user.getId()))
            return;

        ofy().delete().type(FriendshipRequest.class).id(requestID).now();
    }

    @ApiMethod(name = "doesUserExist", httpMethod = ApiMethod.HttpMethod.GET)
    public BoxedBool doesUserExist(@Named("userID") String userId)
    {
        DoryUser user = ofy().load().type(DoryUser.class).id(userId).now();

        return new BoxedBool(user != null);
    }

    // This method should be a POST-Type, but since that appears to be bugged (Throws HTTP 500) it is set as GET instead
    @ApiMethod(name = "createUser", path = "createNewUser", httpMethod = ApiMethod.HttpMethod.GET)
    public BoxedBool createUser(@Named("nickName") String nickName, @Named("firstName") String firstName, @Named("lastName") String lastName, User authUser)//, Location currentCity)
    {
        if(authUser == null)
            return new BoxedBool(false);
            //authUser = new User("id","test@test,de");

        DoryUser user = new DoryUser();

        user.setId(authUser.getId());
        user.setNickName(nickName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailAddress(authUser.getEmail());
        //user.setLocation(currentCity);

        ofy().save().entity(user).now();

        return new BoxedBool(true);
    }

}
