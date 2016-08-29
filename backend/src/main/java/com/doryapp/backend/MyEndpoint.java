/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.doryapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import com.googlecode.objectify.cmd.LoadType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.googlecode.objectify.ObjectifyService.ofy;

/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.doryapp.com",
    ownerName = "backend.doryapp.com",
    packagePath=""
  )
)
public class MyEndpoint {


    public MyEndpoint() throws FileNotFoundException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("Dory-4ab9c906117c.json"))
                .setDatabaseUrl("https://dory-1469989006683.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
    }


    private Long UidToId(String uid) {

        FirebaseAuth.getInstance().verifyIdToken(uid).addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
            @Override
            public void onSuccess(FirebaseToken firebaseToken) {
                String s = firebaseToken.getEmail();
                int i = 0;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String s = e.getMessage();
                int i = 0;
            }
        });


        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Task<FirebaseToken> tokenTask = FirebaseAuth.getInstance().verifyIdToken("8EBVMmCX1TdXsLrYyUabrX6b2Aw2");
        //String customToken = FirebaseAuth.getInstance().createCustomToken("123");
//                .addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
//                    @Override
//                    public void onSuccess(FirebaseToken decodedToken) {
//                        String uid = decodedToken.getUid();
//                        // ...
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        String reason = e.getMessage();
//                        //
//                    }
//                });

        FirebaseToken token;
        try {
            token = Tasks.await(tokenTask);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return 1L;


//        DoryUser user = ofy().load().type(DoryUser.class).filter("emailAddress == ", token.getEmail()).first().now();
//        return user.getId();
    }

    @ApiMethod(name = "getFriendships")
    public List<Friendship> getFriendships(@Named("uid") String uid)
    {

    //    Long id = UidToId(uid);
        Long id = 0L;
        if(id == 0L)
            return null;

        LoadType<Friendship> loader = ofy().load().type(Friendship.class);
        List<Friendship> friends1 = loader.filter("user1 ==", id).list();
        List<Friendship> friends2 = loader.filter("user2 ==", id).list();

        friends1.addAll(friends2);
        return friends1;
    }
    @ApiMethod(name = "getFriends")
    public List<DoryUser> getFriends(@Named("uid") String uid) {
        Long id = UidToId(uid);
        if(id == 0L)
            return null;

        List<Friendship> friendships = getFriendships(uid);
        List<DoryUser> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
            Long idToLoad = id.equals(friendship.getUser1()) ? friendship.getUser2() : friendship.getUser1();

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

        // TODO: Lookup how .filter works
        for (DoryUser user : allUsers) {
            if (user.getNickName().contains(searchedName))
                foundUsers.add(user);
        }

        return foundUsers;
    }
    public void sendFriendRequest(@Named("friendId") Long friendId)
    {
        Friendship friendship = new Friendship();
        friendship.setUser1(0L); // TODO get own Id through authentification
        friendship.setUser2(friendId);
        friendship.setTime(DateTime.now());

        FriendshipRequest req = new FriendshipRequest();
        req.setFriendship(friendship);
        ofy().save().entity(req).now();
    }

    public void acceptFriendRequest(@Named("requestID") Long requestID)
    {
        FriendshipRequest request = ofy().load().type(FriendshipRequest.class).id(requestID).now();
        if(request == null)
            return;

        Friendship newFriendship = request.getFriendship();
        if(newFriendship.getUser2() != 0L) // TODO get own Id thorugh authentification
            return;

        // TODO check if User1 is a valid user (Might have been deleted)
        ofy().save().entity(newFriendship).now();
        ofy().delete().type(FriendshipRequest.class).id(requestID).now();
    }
    public void ignoreFriendRequest(@Named("requestID") Long requestID)
    {
        FriendshipRequest request = ofy().load().type(FriendshipRequest.class).id(requestID).now();
        if(request == null)
            return;

        ofy().delete().type(FriendshipRequest.class).id(requestID).now();
    }


}
