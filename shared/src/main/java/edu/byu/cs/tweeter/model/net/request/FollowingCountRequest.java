package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to get the following count of a user.
 */
public class FollowingCountRequest {

    private AuthToken authToken;
    private String targetUserAlias;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private FollowingCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param targetUserAlias the alias of the user whose following count is being queried.
     */
    public FollowingCountRequest(AuthToken authToken, String targetUserAlias) {
        this.authToken = authToken;
        this.targetUserAlias = targetUserAlias;
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
}

