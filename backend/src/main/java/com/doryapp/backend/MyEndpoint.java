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
import com.googlecode.objectify.cmd.LoadType;

import java.util.ArrayList;
import java.util.List;

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

    @ApiMethod(name = "getFriendships")
    public List<Friendship> getFriendships(@Named("id") Long id) {
        LoadType<Friendship> loader = ofy().load().type(Friendship.class);
        List<Friendship> friends1 = loader.filter("user1 ==", id).list();
        List<Friendship> friends2 = loader.filter("user2 ==", id).list();

        friends1.addAll(friends2);
        return friends1;
    }
    @ApiMethod(name = "getFriends")
    public List<DoryUser> getFriends(@Named("id") Long id) {
        List<Friendship> friendships = getFriendships(id);
        List<DoryUser> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
            Long idToLoad = id.equals(friendship.getUser1()) ? friendship.getUser2() : friendship.getUser1();

            DoryUser user = ofy().load().type(DoryUser.class).id(idToLoad).now();
            friends.add(user);
        }
        return friends;
    }

}
