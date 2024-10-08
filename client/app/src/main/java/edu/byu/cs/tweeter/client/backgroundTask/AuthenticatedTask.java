package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedTask extends BackGroundTask {

    /**
     * Auth token for logged-in user.
     * This user is the "follower" in the relationship.
     */
    private final AuthToken authToken;

    public AuthenticatedTask(Handler messageHandler, AuthToken authToken) {
        super(messageHandler);
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
