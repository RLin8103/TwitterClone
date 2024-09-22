package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;

/**
 * A response for a {@link PostStatusRequest}.
 */
public class PostStatusResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param success the success of the request.
     */
    public PostStatusResponse(boolean success) {
        super(success);
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostStatusResponse(String message) {
        super(false, message);
    }
}
