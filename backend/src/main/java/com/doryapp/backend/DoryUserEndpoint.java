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
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
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

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(DoryUser.class);
    }

    /**
     * Returns the {@link DoryUser} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code DoryUser} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "doryUser/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public DoryUser get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting DoryUser with ID: " + id);
        DoryUser doryUser = ofy().load().type(DoryUser.class).id(id).now();
        if (doryUser == null) {
            throw new NotFoundException("Could not find DoryUser with ID: " + id);
        }
        return doryUser;
    }

    /**
     * Inserts a new {@code DoryUser}.
     */
    @ApiMethod(
            name = "insert",
            path = "doryUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public DoryUser insert(DoryUser doryUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that doryUser.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(doryUser).now();
        logger.info("Created DoryUser with ID: " + doryUser.getId());

        return ofy().load().entity(doryUser).now();
    }

    /**
     * Updates an existing {@code DoryUser}.
     *
     * @param id       the ID of the entity to be updated
     * @param doryUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code DoryUser}
     */
    @ApiMethod(
            name = "update",
            path = "doryUser/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public DoryUser update(@Named("id") Long id, DoryUser doryUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(doryUser).now();
        logger.info("Updated DoryUser: " + doryUser);
        return ofy().load().entity(doryUser).now();
    }

    /**
     * Deletes the specified {@code DoryUser}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code DoryUser}
     */
    @ApiMethod(
            name = "remove",
            path = "doryUser/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(DoryUser.class).id(id).now();
        logger.info("Deleted DoryUser with ID: " + id);
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
            path = "doryUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<DoryUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<DoryUser> query = ofy().load().type(DoryUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<DoryUser> queryIterator = query.iterator();
        List<DoryUser> doryUserList = new ArrayList<DoryUser>(limit);
        while (queryIterator.hasNext()) {
            doryUserList.add(queryIterator.next());
        }
        return CollectionResponse.<DoryUser>builder().setItems(doryUserList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(DoryUser.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find DoryUser with ID: " + id);
        }
    }
}