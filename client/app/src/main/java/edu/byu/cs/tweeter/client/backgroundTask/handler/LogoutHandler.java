package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class LogoutHandler extends BackGroundHandler<UserService.LogoutObserver> {

    public LogoutHandler(UserService.LogoutObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        observer.userLogout();
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to logout: " + message;
    }


    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
