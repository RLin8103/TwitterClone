package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;

/**
 * A response for a {@link FollowingCountRequest}.
 */
public class FollowingCountResponse extends Response {

    private int count;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count the number of users the specified user is following.
     */
    public FollowingCountResponse(int count) {
        super(true);
        this.count = count;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowingCountResponse(String message) {
        super(false, message);
    }

    /**
     * Returns the count of users the specified user is following.
     *
     * @return the count.
     */
    public int getCount() {
        return count;
    }
}
