package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.StatusService;

public class PostStatusHandler extends BackGroundHandler<StatusService.PostStatusObserver> {

    public PostStatusHandler(StatusService.PostStatusObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.postStatus();
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to post status: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
