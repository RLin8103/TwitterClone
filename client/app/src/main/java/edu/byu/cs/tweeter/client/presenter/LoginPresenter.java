package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticatePresenter<LoginPresenter.View> {

    public interface View extends BaseView {
        void userLogin(User loggedInUser);
    }

    public LoginPresenter(View view) {
        super(view);
    }

    public void validateLogin(String alias, String password) {
        validateAliasAndPassword(alias, password);
    }

    public void login(String alias, String password) {
        // Send the login request.
        userService.login(alias, password, new LoginServiceObserver());
    }

    private class LoginServiceObserver extends BaseServiceObserver implements UserService.LoginObserver {

        @Override
        public void userLogin(User loggedInUser) {
            ((LoginPresenter.View)view).userLogin(loggedInUser);
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to login because of exception: ";
        }
    }

}
