package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T, V extends PagedPresenter.PagedView<T>> extends BasePresenter<V> {

    // PagedView and PagedPresenter
    public interface PagedView<U> extends BaseView {
        /**
         * Transition to the main activity.
         * @param user the user for the main activity
         */
        void startMainActivity(User user);

        /**
         * Set the loading footer's visibility.
         * @param b visibility status
         */
        void setLoadingFooter(boolean b);

        /**
         * Add a list of items to the view.
         * @param items the items to add
         */
        void addItems(List<U> items);
    }

    protected static final int PAGE_SIZE = 10;
    protected boolean hasMorePages;
    protected boolean isLoading;
    protected T lastItem;

    public PagedPresenter(V view) {
        super(view);
    }

    /**
     * Check if more pages are available for pagination.
     * @return true if more pages are available, false otherwise
     */
    public boolean hasMorePages() {
        return hasMorePages;
    }

    /**
     * Check if the presenter is currently loading items.
     * @return true if currently loading, false otherwise
     */
    public boolean isLoading() {
        return isLoading;
    }

    public void getUserProfile(String userAlias) {
        userService.getUserProfile(userAlias, new UserServiceObserver());
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            serviceLoadMoreItems(user);
        }
    }

    protected abstract void serviceLoadMoreItems(User user);

    // ... Other shared methods, properties, and observers for pagination ...
    protected class UserServiceObserver extends BasePresenter.BaseServiceObserver implements UserService.UserObserver {

        @Override
        public void startMainActivity(User user) {
            view.startMainActivity(user);
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to get user's profile because of exception: ";
        }
    }

    protected abstract class PagedServiceObserver extends BaseServiceObserver {

        public void addItems(List<T> items, boolean hasMorePages) {
            setLoadingFooterFalse();
            PagedPresenter.this.hasMorePages = hasMorePages;
            if (lastItem != null) {
                lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            }
            view.addItems(items);
        }

        @Override
        public void displayError(String message) {
            setLoadingFooterFalse();
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            setLoadingFooterFalse();
            view.displayMessage(getExceptionMessage() + ex);
        }

        public void setLoadingFooterFalse() {
            view.setLoadingFooter(false);
            isLoading = false;
        }

        protected abstract String getExceptionMessage();
    }
}
