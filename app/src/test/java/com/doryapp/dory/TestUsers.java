package com.doryapp.dory;

import com.doryapp.backend.doryUserApi.model.DoryUser;

/**
 * Created by Lorenzo Toso on 14.08.2016.
 */
public class TestUsers {

    private final static String TEST_LAST_NAME = "lastname_";
    private final static String TEST_FIRST_NAME = "firstname_";

    public static DoryUser get(Long id)
    {
        DoryUser user = new DoryUser();
        user.setId(id);
        user.setFirstName(TEST_FIRST_NAME + id);
        user.setLastName(TEST_LAST_NAME + id);
        return user;
    }




}
