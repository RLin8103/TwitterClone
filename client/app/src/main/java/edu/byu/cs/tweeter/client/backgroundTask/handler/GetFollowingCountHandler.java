package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowingCountHandler extends BackGroundHandler<FollowService.GetFollowingCountObserver> {

    public GetFollowingCountHandler(FollowService.GetFollowingCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
        observer.setFollowingCount(count);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get following count: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
