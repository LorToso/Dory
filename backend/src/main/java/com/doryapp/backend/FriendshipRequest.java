package com.doryapp.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Lorenzo Toso on 29.08.2016.
 */
@Entity
public class FriendshipRequest {
    @Id String id;
    // User1 is the requesting User
    // User2 is the user that has to accept
    Friendship friendship;

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
