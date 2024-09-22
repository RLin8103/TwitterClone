package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import java.util.List;

public class UpdateFeedMessageRequest {
    private Status status;
    private List<User> followers;

    public UpdateFeedMessageRequest(Status status, List<User> followers) {
        this.status = status;
        this.followers = followers;
    }

    // Getters and setters
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
}
