package com.doryapp.dory;

import com.doryapp.backend.doryUserApi.DoryUserApi;
import com.doryapp.backend.doryUserApi.model.DoryUser;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper();

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    // Run this test twice to prove we're not leaking any state across tests.
    private void doTest() throws IOException {
        DoryUserApi api = getApiServiceHandle();

        final long id = 123L;

        AssertUserDoesNotExist(api, id);

        DoryUser user = new DoryUser();
        user.setId(id);
        user.setFirstName("test1");
        user.setLastName("test2");
        api.insert(user).execute();

        DoryUser queryUser = api.get(id).execute();
        Assert.assertEquals(user,queryUser);

        api.remove(id).execute();

        AssertUserDoesNotExist(api, id);
    }

    private void AssertUserDoesNotExist(DoryUserApi api, Long id) throws IOException {
        try {
            DoryUser user = api.get(id).execute();
            Assert.fail();
        }
        catch (GoogleJsonResponseException exception)
        {
            String message = exception.getMessage();
            Assert.assertTrue(message.startsWith("404"));
        }
    }

    @Test
    public void testInsert1() throws IOException {
        doTest();
    }

    @Test
    public void testInsert2() throws IOException {
        doTest();
    }


    private static DoryUserApi getApiServiceHandle() throws IOException {
        // Use a builder to help formulate the API request.
        DoryUserApi.Builder helloWorld = new DoryUserApi.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), null);
        helloWorld.setApplicationName("doryUserApi");
        // If running the Cloud Endpoint API locally then point the API stub there by un-commenting the
        // next line.
        helloWorld.setRootUrl("http://localhost:8080/_ah/api/");

        return helloWorld.build();
    }
}