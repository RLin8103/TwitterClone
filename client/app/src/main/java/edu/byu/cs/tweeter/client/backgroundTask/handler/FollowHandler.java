package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.FollowService;

public class FollowHandler extends BackGroundHandler<FollowService.FollowObserver> {

    public FollowHandler(FollowService.FollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.follow();
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to follow: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        observer.enableFollowButton();
    }
}
