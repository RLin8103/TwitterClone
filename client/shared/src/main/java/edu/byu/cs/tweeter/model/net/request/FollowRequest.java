package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to follow a user.
 */
public class FollowRequest {

    private User currUser;
    private AuthToken authToken;
    private User followee;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private FollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param followee the user to be followed.
     */
    public FollowRequest(User currUser, AuthToken authToken, User followee) {
        this.currUser = currUser;
        this.authToken = authToken;
        this.followee = followee;
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

    // Getter for followee
    public User getFollowee() {
        return followee;
    }

    // Setter for followee
    public void setFollowee(User followee) {
        this.followee = followee;
    }
}

