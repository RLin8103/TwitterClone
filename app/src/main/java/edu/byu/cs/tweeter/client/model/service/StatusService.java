package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends BaseService {

    public static final String GETSTORY_URL_PATH = "/getstory";
    public static final String GETFEED_URL_PATH ="/getfeed";
    public static final String POSTSTATUS_URL_PATH = "/poststatus";

    // TODO: Do a generic PagedObserver
    public interface FeedObserver extends BaseService.BaseObserver {
        void addItems(List<Status> statuses, boolean hasMorePages);
    }

    public interface StoryObserver extends BaseService.BaseObserver {
        void addItems(List<Status> statuses, boolean hasMorePages);

        List<Status> getStatuses();

        boolean getHasMorePages();

        Object getException();
    }

    public interface PostStatusObserver extends BaseService.BaseObserver {

        void postStatus();

        void displayInfoMessage(String message);

        void logError(Exception ex);
    }

    public void loadMoreFeeds(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, FeedObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(observer));
        executeTask(getFeedTask);
    }

    public void loadMoreStories(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, StoryObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(observer));
        executeTask(getStoryTask);
    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, PostStatusObserver observer) {

        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                    newStatus, new PostStatusHandler(observer));
        executeTask(statusTask);
    }

}
