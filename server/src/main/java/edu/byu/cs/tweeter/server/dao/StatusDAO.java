package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;


public interface StatusDAO {

    Pair<List<Status>, Boolean> getFeed(String targetUserAlias, int limit, Status lastStatus);

    Pair<List<Status>, Boolean> getStory(String targetUserAlias, int limit, Status lastStatus);

    boolean postStatus(Status newStatus);

    void updateFeed(List<User> follower, Status statusToUpdate);
}
