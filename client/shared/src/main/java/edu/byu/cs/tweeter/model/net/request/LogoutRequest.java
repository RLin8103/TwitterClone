package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to log out a user.
 */
public class LogoutRequest {

    private AuthToken authToken;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private LogoutRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user who is logging out.
     */
    public LogoutRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}

