package com.doryapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "friendshipApi",
        version = "v1",
        resource = "friendship",
        namespace = @ApiNamespace(
                ownerDomain = "backend.doryapp.com",
                ownerName = "backend.doryapp.com",
                packagePath = ""
        )
)
public class FriendshipEndpoint {

    private static final Logger logger = Logger.getLogger(FriendshipEndpoint.class.getName());

    /**
     * This method gets the <code>Friendship</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Friendship</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getFriendship")
    public Friendship getFriendship(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getFriendship method");
        return null;
    }

    /**
     * This inserts a new <code>Friendship</code> object.
     *
     * @param friendship The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertFriendship")
    public Friendship insertFriendship(Friendship friendship) {
        // TODO: Implement this function
        logger.info("Calling insertFriendship method");
        return friendship;
    }
}