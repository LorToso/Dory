package com.doryapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
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

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Friendship.class);
    }

    /**
     * Returns the {@link Friendship} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Friendship} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "friendship/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Friendship get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Friendship with ID: " + id);
        Friendship friendship = ofy().load().type(Friendship.class).id(id).now();
        if (friendship == null) {
            throw new NotFoundException("Could not find Friendship with ID: " + id);
        }
        return friendship;
    }

    /**
     * Inserts a new {@code Friendship}.
     */
    @ApiMethod(
            name = "insert",
            path = "friendship",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Friendship insert(Friendship friendship) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that friendship.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(friendship).now();
        logger.info("Created Friendship with ID: " + friendship.getId());

        return ofy().load().entity(friendship).now();
    }

    /**
     * Updates an existing {@code Friendship}.
     *
     * @param id         the ID of the entity to be updated
     * @param friendship the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Friendship}
     */
    @ApiMethod(
            name = "update",
            path = "friendship/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Friendship update(@Named("id") Long id, Friendship friendship) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(friendship).now();
        logger.info("Updated Friendship: " + friendship);
        return ofy().load().entity(friendship).now();
    }

    /**
     * Deletes the specified {@code Friendship}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Friendship}
     */
    @ApiMethod(
            name = "remove",
            path = "friendship/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Friendship.class).id(id).now();
        logger.info("Deleted Friendship with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "friendship",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Friendship> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Friendship> query = ofy().load().type(Friendship.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Friendship> queryIterator = query.iterator();
        List<Friendship> friendshipList = new ArrayList<Friendship>(limit);
        while (queryIterator.hasNext()) {
            friendshipList.add(queryIterator.next());
        }
        return CollectionResponse.<Friendship>builder().setItems(friendshipList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Friendship.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Friendship with ID: " + id);
        }
    }
}