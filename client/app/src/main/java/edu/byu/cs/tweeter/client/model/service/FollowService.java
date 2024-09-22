package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {

    public static final String GETFOLLOWING_URL_PATH = "/getfollowing";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";
    public static final String ISFOLLOWER_URL_PATH = "/isfollower";
    public static final String GETFOLLOWINGCOUNT_URL_PATH = "/getfollowingcount";
    public static final String GETFOLLOWERS_URL_PATH = "/getfollowers";
    public static final String GETFOLLOWERSCOUNT_URL_PATH = "/getfollowerscount";
    public static final String FOLLOW_URL_PATH = "/follow";

    public interface FollowersObserver extends BaseService.BaseObserver {
        void addItems(List<User> followers, boolean hasMorePages);
    }

    public interface FollowingObserver extends BaseService.BaseObserver {
        void addItems(List<User> followees, boolean hasMorePages);
    }

    public interface IsFollowerObserver extends BaseService.BaseObserver {
        void isFollower(boolean isFollower);
    }

    public interface FollowObserver extends BaseService.BaseObserver {

        void displayMessage(String message);

        void follow();

        void enableFollowButton();
    }

    public interface UnFollowObserver extends BaseService.BaseObserver{

        void displayMessage(String message);

        void unFollow();

        void enableFollowButton();
    }

    public interface GetFollowersCountObserver extends BaseService.BaseObserver {
        void setFollowerCount(int count);
    }

    public interface GetFollowingCountObserver extends BaseService.BaseObserver{
        void setFollowingCount(int count);
    }

    public void loadMoreFollowersItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, FollowersObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        executeTask(getFollowersTask);
    }

    public void loadMoreFollowingItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, FollowingObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        executeTask(getFollowingTask);
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                currUser, selectedUser, new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }

    public void follow(AuthToken currUserAuthToken, User selectedUser, FollowObserver observer) {
        FollowTask followTask = new FollowTask(currUserAuthToken, selectedUser, new FollowHandler(observer));
        executeTask(followTask);
    }

    public void unFollow(AuthToken currUserAuthToken, User selectedUser, UnFollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken,
                selectedUser, new UnfollowHandler(observer));
        executeTask(unfollowTask);
    }

    public void getFollowersFollowingCount(AuthToken currUserAuthToken, User selectedUser, GetFollowersCountObserver getFollowersCountObserver, GetFollowingCountObserver getFollowingCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetFollowersCountHandler(getFollowersCountObserver));
        executeTask(followersCountTask);
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        executeTask(followingCountTask);
    }

}
