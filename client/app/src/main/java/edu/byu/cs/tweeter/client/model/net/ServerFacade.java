package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://a3o2tq1g1m.execute-api.us-east-1.amazonaws.com/FullDeployment";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Retrieves the feed for a specified user.
     *
     * @param request The feed request containing the user's details and pagination information.
     * @param urlPath The URL path for the feed operation.
     * @return The response containing a page of statuses.
     */
    public FeedResponse getFeed(FeedRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);
    }

    /**
     * Retrieves the count of followers for a specified user.
     *
     * @param request The followers count request containing the target user's alias.
     * @param urlPath The URL path for the followers count operation.
     * @return The response containing the count of followers.
     */
    public FollowersCountResponse getFollowersCount(FollowersCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowersCountResponse.class);
    }

    /**
     * Retrieves a page of followers for a specified user.
     *
     * @param request The followers request containing the user's details and pagination information.
     * @param urlPath The URL path for the followers operation.
     * @return The response containing a page of the user's followers.
     */
    public FollowersResponse getFollowers(FollowersRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);
    }

    /**
     * Retrieves the count of users that a specified user is following.
     *
     * @param request The following count request containing the target user's alias.
     * @param urlPath The URL path for the following count operation.
     * @return The response containing the count of following users.
     */
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingCountResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    /**
     * Sends a request for the current user to follow another user.
     *
     * @param request The follow request containing information about the followee.
     * @param urlPath The URL path for the follow operation.
     * @return The response indicating the success or failure of the follow operation.
     */
    public FollowResponse follow(FollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);
    }

    /**
     * Sends a request to check if one user is following another.
     *
     * @param request The is follower request containing information about the follower and followee.
     * @param urlPath The URL path for the is follower operation.
     * @return The response indicating whether the user is a follower.
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);
    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public AuthenticateResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, AuthenticateResponse.class);
    }

    /**
     * Logs out the current user and ends their session.
     *
     * @param request The logout request containing the auth token of the user to be logged out.
     * @param urlPath The URL path for the logout operation.
     * @return The response indicating the success or failure of the logout operation.
     */
    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);
    }

    /**
     * Posts a new status on behalf of the user.
     *
     * @param request The post status request containing the status details.
     * @param urlPath The URL path for the post status operation.
     * @return The response indicating the success or failure of posting the status.
     */
    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
    }

    /**
     * Performs a register and if successful, returns the registered in user and an auth token.
     *
     * @param request contains all information needed to perform a register.
     * @return the register response.
     */
    public AuthenticateResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, AuthenticateResponse.class);
    }

    /**
     * Retrieves a page of statuses from a user's story.
     *
     * @param request The story request containing the user's details and pagination information.
     * @param urlPath The URL path for the story operation.
     * @return The response containing a page of statuses from the user's story.
     */
    public StoryResponse getStory(StoryRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);
    }

    /**
     * Sends a request for the current user to unfollow another user.
     *
     * @param request The unfollow request containing information about the followee.
     * @param urlPath The URL path for the unfollow operation.
     * @return The response indicating the success or failure of the unfollow operation.
     */
    public UnfollowResponse unfollow(UnfollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, UnfollowResponse.class);
    }

    /**
     * Retrieves the profile for a specified user.
     *
     * @param request The user request containing the user's alias.
     * @param urlPath The URL path for retrieving user profile.
     * @return The response containing the user profile or an error message.
     */
    public UserResponse getUser(UserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, UserResponse.class);
    }

}
