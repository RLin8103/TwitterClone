package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;

/**
 * A response for a {@link FollowersCountRequest}.
 */
public class FollowersCountResponse extends Response {

    private int count;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count the number of followers.
     */
    public FollowersCountResponse(int count) {
        super(true);
        this.count = count;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowersCountResponse(String message) {
        super(false, message);
    }

    /**
     * Returns the count of followers.
     *
     * @return the count.
     */
    public int getCount() {
        return count;
    }
}

