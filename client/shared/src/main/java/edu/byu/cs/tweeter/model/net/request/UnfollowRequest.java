package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to unfollow a user.
 */
public class UnfollowRequest {

    private User currUser;
    private AuthToken authToken;
    private String followeeAlias;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private UnfollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param followeeAlias the alias of the user to be unfollowed.
     */
    public UnfollowRequest(User currUser, AuthToken authToken, String followeeAlias) {
        this.currUser = currUser;
        this.authToken = authToken;
        this.followeeAlias = followeeAlias;
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for followeeAlias
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    // Setter for followeeAlias
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
