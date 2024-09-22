package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowersCountHandler extends BackGroundHandler<FollowService.GetFollowersCountObserver> {

    public GetFollowersCountHandler(FollowService.GetFollowersCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
        observer.setFollowerCount(count);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get followers count: " + message;
    }


    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
