package edu.byu.cs.tweeter.client.presenter;

import android.widget.ImageView;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthenticatePresenter<RegisterPresenter.View> {

    public interface View extends BaseView {

        void userRegister(User registeredUser);
    }


    public RegisterPresenter(View view) {
        super(view);
    }

    // Do this in the Presenter
    public void validateRegistration(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }

        validateAliasAndPassword(alias, password);

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        // Send register request.
        userService.register(firstName, lastName, alias, password, imageBytesBase64, new RegisterServiceObserver());
    }

    private class RegisterServiceObserver extends BaseServiceObserver implements UserService.RegisterObserver {

        @Override
        public void userRegister(User registeredUser) {
            ((RegisterPresenter.View)view).userRegister(registeredUser);
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to register because of exception: ";
        }
    }

}
