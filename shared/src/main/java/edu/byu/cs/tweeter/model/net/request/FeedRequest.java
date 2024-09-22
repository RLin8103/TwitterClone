package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a user's feed.
 */
public class FeedRequest {

    private AuthToken authToken;
    private String userAlias;
    private int limit;
    private Status lastStatus;

    /**
     * Allows construction of the object from JSON. Private so it won't be called in normal code.
     */
    private FeedRequest() {}

    /**
     * Creates an instance.
     *
     * @param authToken the auth token of the user making the request.
     * @param userAlias the alias of the user whose feed is to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the last status that was returned in the previous request (null if
     *                   there was no previous request or if no statuses were returned in the
     *                   previous request).
     */
    public FeedRequest(AuthToken authToken, String userAlias, int limit, Status lastStatus) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    // Getter for authToken
    public AuthToken getAuthToken() {
        return authToken;
    }

    // Setter for authToken
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    // Getter for userAlias
    public String getUserAlias() {
        return userAlias;
    }

    // Setter for userAlias
    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    // Getter for limit
    public int getLimit() {
        return limit;
    }

    // Setter for limit
    public void setLimit(int limit) {
        this.limit = limit;
    }

    // Getter for lastStatus
    public Status getLastStatus() {
        return lastStatus;
    }

    // Setter for lastStatus
    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}

