package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;

/**
 * A response for an {@link IsFollowerRequest}.
 */
public class IsFollowerResponse extends Response {

    private boolean isFollower;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param isFollower the result of the check - true if the user is a follower, false otherwise.
     */
    public IsFollowerResponse(boolean isFollower) {
        super(true);
        this.isFollower = isFollower;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public IsFollowerResponse(String message) {
        super(false, message);
        this.isFollower = false;
    }

    /**
     * Returns whether the user is a follower.
     *
     * @return the isFollower status.
     */
    public boolean isFollower() {
        return isFollower;
    }
}
