package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status, FeedPresenter.View>{

    public interface View extends PagedView<Status> {
    }

    private StatusService statusService;

    public FeedPresenter(View view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void serviceLoadMoreItems(User user) {
        statusService.loadMoreFeeds(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new FeedPresenter.FeedServiceObserver());
    }

    private class FeedServiceObserver extends PagedServiceObserver implements StatusService.FeedObserver {
        @Override
        protected String getExceptionMessage() {
            return "Failed to get Feed because of exception: ";
        }
    }
}
