package edu.byu.cs.tweeter.client.serverfacade;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

public class ServerFacadeIntegrationTest {

    private ServerFacade serverFacade;
    private String serverUrl;

    @BeforeEach
    public void setup() {
        serverFacade = new ServerFacade();
        // This URL should point to your server. Adjust it as necessary for your environment.
        serverUrl = "";
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("TestUser", "password", "FirstName", "LastName", "imageBytes");
        AuthenticateResponse response = serverFacade.register(request, serverUrl + "/register");
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getUser());
        assertNotNull(response.getAuthToken());
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowersRequest request = new FollowersRequest(new AuthToken(), "alias", 10, null);
        FollowersResponse response = serverFacade.getFollowers(request, serverUrl + "/getfollowers");
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getFollowers());
    }

    @Test
    public void testGetFollowingCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowingCountRequest request = new FollowingCountRequest(new AuthToken(), "alias");
        FollowingCountResponse response = serverFacade.getFollowingCount(request, serverUrl + "/getfollowingcount");
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getCount() >= 0);
    }

    @Test
    public void testGetFollowersCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowersCountRequest request = new FollowersCountRequest(new AuthToken(), "alias");
        FollowersCountResponse response = serverFacade.getFollowersCount(request, serverUrl + "/getfollowerscount");
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getCount() >= 0);
    }

    // Additional tests as necessary...
}

