package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User, FollowingPresenter.View> {

    public interface View extends PagedPresenter.PagedView<User> {
    }

    private FollowService followService;

    public FollowingPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void serviceLoadMoreItems(User user) {
        followService.loadMoreFollowingItems(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new FollowingServiceObserver());
    }

    private class FollowingServiceObserver extends PagedServiceObserver implements FollowService.FollowingObserver {
        @Override
        protected String getExceptionMessage() {
            return "Failed to get following because of exception: ";
        }
    }
}
