package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User, FollowersPresenter.View> {

    public interface View extends PagedPresenter.PagedView<User> {
    }

    private FollowService followService;

    public FollowersPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void serviceLoadMoreItems(User user) {
        followService.loadMoreFollowersItems(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new FollowerServiceObserver());
    }

    private class FollowerServiceObserver extends PagedServiceObserver implements FollowService.FollowersObserver {
        @Override
        protected String getExceptionMessage() {
            return "Failed to get followers because of exception: ";
        }
    }


}
