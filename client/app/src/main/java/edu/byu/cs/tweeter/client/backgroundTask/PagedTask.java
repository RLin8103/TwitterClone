package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedTask<T> extends AuthenticatedTask {
    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";
    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of statuses to return (i.e., page size).
     */
    private int limit;
    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private final T lastItem;

    /**
     * The items returned in the current page of results.
     */
    private List<T> items;
    /**
     * Indicates whether there are more pages of items that can be retrieved on subsequent calls.
     */
    private boolean hasMorePages;

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    protected int getLimit() {
        return limit;
    }

    public T getLastItem() {
        return lastItem;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    @Override
    protected final void runTask() throws IOException {
//        Pair<List<T>, Boolean> pageOfItems = getItems();
//        if (pageOfItems != null) {
//            items = pageOfItems.getFirst();
//            hasMorePages = pageOfItems.getSecond();
//        }
        retrieveItems();
    }

    protected abstract void retrieveItems();

    @Override
    protected final void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

}
