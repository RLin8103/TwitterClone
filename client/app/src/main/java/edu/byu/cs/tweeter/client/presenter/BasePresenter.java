package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.BaseService;
import edu.byu.cs.tweeter.client.model.service.UserService;

// BasePresenter class
public abstract class BasePresenter<V extends BasePresenter.BaseView> {

    // BaseView Interface
    public interface BaseView {
        /**
         * Display a message to the user.
         * @param message the message to display
         */
        void displayMessage(String message);
    }

    protected V view;
    protected UserService userService;

    public BasePresenter(V view) {
        this.view = view;
        userService = new UserService();
    }

    public abstract class BaseServiceObserver implements BaseService.BaseObserver {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage(getExceptionMessage() + ex);
        }

        protected abstract String getExceptionMessage();
    }
}

