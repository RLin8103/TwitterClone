package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected AuthenticateResponse runAuthenticationTask() throws IOException, TweeterRemoteException {
//        User loggedInUser = getFakeData().getFirstUser();
//        AuthToken authToken = getFakeData().getAuthToken();

        LoginRequest request = new LoginRequest(username, password);
        AuthenticateResponse response = getServerFacade().login(request, UserService.LOGIN_URL_PATH);

        return response;
    }

}
