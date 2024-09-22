package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to check if one user is following another.
 */
public class IsFollowerRequest {

    private AuthToken authToken;
    private String followerAlias;
    private String followeeAlias;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private IsFollowerRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param followerAlias the alias of the alleged follower.
     * @param followeeAlias the alias of the alleged followee.
     */
    public IsFollowerRequest(AuthToken authToken, String followerAlias, String followeeAlias) {
        this.authToken = authToken;
        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for followerAlias
    public String getFollowerAlias() {
        return followerAlias;
    }

    // Setter for followerAlias
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
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

