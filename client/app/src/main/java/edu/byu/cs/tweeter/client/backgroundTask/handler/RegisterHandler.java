package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends BackGroundHandler<UserService.RegisterObserver> {

    public RegisterHandler(UserService.RegisterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message msg) {
        User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
        Cache.getInstance().setCurrUser(registeredUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        observer.userRegister(registeredUser);
    }

    @Override
    protected String getMessage(String message) {
        return "Failed to register: " + message;
    }

    @Override
    protected void finalizeHandling(Message msg) {
        // Do Nothing
    }
}
