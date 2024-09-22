package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends BackGroundHandler<StatusService.FeedObserver> {

    // private StatusService.FeedObserver observer;

    public GetFeedHandler(StatusService.FeedObserver observer) {
        super(observer);
    }

    // TODO: Can put GetFeedHandler and GetStoryHandler as a same class
    @Override
    protected void handleSuccess(Message msg) {
        List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
        observer.addItems(statuses, hasMorePages);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get feed: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do nothing
    }
}
