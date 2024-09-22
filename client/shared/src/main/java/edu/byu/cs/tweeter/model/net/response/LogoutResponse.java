package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;

/**
 * A response for a {@link LogoutRequest}.
 */
public class LogoutResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param success the success of the request.
     */
    public LogoutResponse(boolean success) {
        super(success);
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public LogoutResponse(String message) {
        super(false, message);
    }
}
