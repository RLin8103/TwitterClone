package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends BackGroundHandler<StatusService.StoryObserver> {

    public GetStoryHandler(StatusService.StoryObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
        observer.addItems(statuses, hasMorePages);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get story: " + message;
    }


    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
