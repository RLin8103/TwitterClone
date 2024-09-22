package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.FollowService;

public class UnfollowHandler extends BackGroundHandler<FollowService.UnFollowObserver> {

    public UnfollowHandler(FollowService.UnFollowObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccess(Message msg) {
        observer.unFollow();
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to unfollow: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        observer.enableFollowButton();
    }
}
