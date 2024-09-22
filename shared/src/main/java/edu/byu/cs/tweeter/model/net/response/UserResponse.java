package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UserRequest;

/**
 * A response for a {@link UserRequest}.
 */
public class UserResponse extends Response {

    private User user;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param user the user whose profile was retrieved.
     */
    public UserResponse(User user) {
        super(true);
        this.user = user;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public UserResponse(String message) {
        super(false, message);
    }

    /**
     * Returns the user for the corresponding request.
     *
     * @return the user.
     */
    public User getUser() {
        return user;
    }
}
