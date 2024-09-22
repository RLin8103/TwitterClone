package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to fetch a user's profile.
 */
public class UserRequest {

    private AuthToken authToken;
    private String alias;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private UserRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param alias the alias (or handle) of the user whose profile is being retrieved.
     */
    public UserRequest(AuthToken authToken, String alias) {
        this.authToken = authToken;
        this.alias = alias;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for alias
    public String getAlias() {
        return alias;
    }

    // Setter for alias
    public void setAlias(String alias) {
        this.alias = alias;
    }
}
