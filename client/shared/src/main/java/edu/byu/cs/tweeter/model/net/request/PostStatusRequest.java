package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to post a new status.
 */
public class PostStatusRequest {

    private AuthToken authToken;
    private Status status;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user posting the status.
     * @param status the status to be posted, containing all relevant details including the user's identity.
     */
    public PostStatusRequest(AuthToken authToken, Status status) {
        this.authToken = authToken;
        this.status = status;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for status
    public Status getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(Status status) {
        this.status = status;
    }
}

