package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followers for a specified user.
 */
public class FollowersRequest {

    private AuthToken authToken;
    private String targetUserAlias;
    private int limit;
    private String lastFollowerAlias;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private FollowersRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param targetUserAlias the alias of the user whose followers are to be returned.
     * @param limit the maximum number of followers to return.
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous request (null if
     *                          there was no previous request or if no followers were returned in the
     *                          previous request).
     */
    public FollowersRequest(AuthToken authToken, String targetUserAlias, int limit, String lastFollowerAlias) {
        this.authToken = authToken;
        this.targetUserAlias = targetUserAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for targetUserAlias
    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    // Setter for targetUserAlias
    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUserAlias = targetUserAlias;
    }

    // Getter for limit
    public int getLimit() {
        return limit;
    }

    // Setter for limit
    public void setLimit(int limit) {
        this.limit = limit;
    }

    // Getter for lastFollowerAlias
    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    // Setter for lastFollowerAlias
    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }
}

