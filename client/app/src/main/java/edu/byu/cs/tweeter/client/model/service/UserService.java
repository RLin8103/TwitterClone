package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends BaseService {

    public static final String LOGIN_URL_PATH = "/login";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String LOGOUT_URL_PATH = "/logout" ;
    public static final String GETUSER_URL_PATH = "/getuser";

    public interface LoginObserver extends BaseService.BaseObserver {
        void userLogin(User loggedInUser);
    }

    public interface LogoutObserver extends BaseService.BaseObserver {
        void userLogout();
    }

    public interface RegisterObserver extends BaseService.BaseObserver {
        void userRegister(User registeredUser);
    }

    public interface UserObserver extends BaseService.BaseObserver {
        void startMainActivity(User user);
    }

    public void login(String alias, String password, LoginObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        executeTask(loginTask);
    }

    public void logout(AuthToken currUserAuthToken, LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new LogoutHandler(observer));
        executeTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(observer));
        executeTask(registerTask);
    }

    public void getUserProfile(String userAlias, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        // Toast.makeText(getContext(), "Getting user's profile...", Toast.LENGTH_LONG).show();
        executeTask(getUserTask);
    }

}
