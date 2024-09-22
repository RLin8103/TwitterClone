package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BackGroundHandler<UserService.UserObserver> {

    public GetUserHandler(UserService.UserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
        observer.startMainActivity(user);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to get user's profile: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
