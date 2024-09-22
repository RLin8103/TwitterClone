package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDAO {

    Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias);

    int getFollowersCount(String targetUserAlias);

    Pair<List<User>, Boolean> getFollowers(String targetUserAlias, int limit, String lastFollowerAlias);

    int getFollowingCount(String targetUserAlias);

    boolean isFollower(String followerAlias, String followeeAlias);

    boolean follow(User currUser, User followee);

    boolean unfollow(User currUser, String followeeAlias);

    void addFollowersBatch(List<User> followers, String followTarget);
}
