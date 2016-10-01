package com.doryapp.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class FriendshipStatus {

    @Id String id;
    Status friendshipStatus;


    public FriendshipStatus(Status friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }


    public Status getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatus(Status friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    enum Status
    {
        SELF,
        NO_FRIEND,
        FRIEND,
        REQUEST_SENT,
        REQUEST_PENDING,
    }

}
