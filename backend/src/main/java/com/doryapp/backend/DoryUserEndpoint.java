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
        name = "doryUserApi",
        version = "v1",
        resource = "doryUser",
        namespace = @ApiNamespace(
                ownerDomain = "backend.doryapp.com",
                ownerName = "backend.doryapp.com",
                packagePath = ""
        )
)
public class DoryUserEndpoint {

    private static final Logger logger = Logger.getLogger(DoryUserEndpoint.class.getName());

    /**
     * This method gets the <code>DoryUser</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>DoryUser</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getDoryUser")
    public DoryUser getDoryUser(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getDoryUser method");
        return null;
    }

    /**
     * This inserts a new <code>DoryUser</code> object.
     *
     * @param doryUser The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertDoryUser")
    public DoryUser insertDoryUser(DoryUser doryUser) {
        // TODO: Implement this function
        logger.info("Calling insertDoryUser method");
        return doryUser;
    }
}