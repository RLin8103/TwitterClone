package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;

/**
 * A response for an {@link UnfollowRequest}.
 */
public class UnfollowResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param success the success of the request.
     */
    public UnfollowResponse(boolean success) {
        super(success);
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public UnfollowResponse(String message) {
        super(false, message);
    }
}
