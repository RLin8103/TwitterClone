package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends BackGroundHandler<FollowService.FollowersObserver> {

    public GetFollowersHandler(FollowService.FollowersObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        observer.addItems(followers, hasMorePages);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get followers: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
