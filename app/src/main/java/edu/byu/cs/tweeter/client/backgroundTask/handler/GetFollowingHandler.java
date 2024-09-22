package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowingTask.
 */
public class GetFollowingHandler extends BackGroundHandler<FollowService.FollowingObserver> {

    public GetFollowingHandler(FollowService.FollowingObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.addItems(followees, hasMorePages);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get following: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
