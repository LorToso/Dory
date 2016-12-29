package com.doryapp.dory;

import com.doryapp.backend.myApi.model.FriendshipStatus;

/**
 * Created by Lorenzo Toso on 29.12.2016.
 */

public enum FriendshipStatusEnum {
    SELF,
    NO_FRIEND,
    FRIEND,
    OWN_REQUEST_PENDING,
    OTHER_REQUEST_PENDING,
    UNKNOWN;

    public String getButtonText()
    {
        switch(this)
        {
            case SELF:
                return "You";
            case NO_FRIEND:
                return "Add";
            case FRIEND:
                return "Friend";
            case OWN_REQUEST_PENDING:
                return "Sent";
            case OTHER_REQUEST_PENDING:
                return "Accept";
        }
        return "Unknown";
    }
    public static FriendshipStatusEnum from(FriendshipStatus status)
    {
        return from(status.getFriendshipStatus());
    }

    public static FriendshipStatusEnum from(String value)
    {
        switch (value)
        {
            case "SELF":
                return SELF;
            case "NO_FRIEND":
                return NO_FRIEND;
            case "FRIEND":
                return FRIEND;
            case "REQUEST_SENT":
                return OWN_REQUEST_PENDING;
            case "REQUEST_PENDING":
                return OTHER_REQUEST_PENDING;
        }
        return UNKNOWN;
    }

}
