package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status, StoryPresenter.View> {

    public interface View extends PagedPresenter.PagedView<Status> {
    }

    private StatusService statusService;

    public StoryPresenter(View view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void serviceLoadMoreItems(User user) {
        statusService.loadMoreStories(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new StoryPresenter.StoryServiceObserver());
    }

    private class StoryServiceObserver extends PagedServiceObserver implements StatusService.StoryObserver {
        @Override
        protected String getExceptionMessage() {
            return "Failed to get story because of exception: ";
        }

        @Override
        public List<Status> getStatuses() {
            return null;
        }

        @Override
        public boolean getHasMorePages() {
            return false;
        }

        @Override
        public Object getException() {
            return null;
        }
    }
}
