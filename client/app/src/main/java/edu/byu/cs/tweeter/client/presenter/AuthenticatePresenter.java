package edu.byu.cs.tweeter.client.presenter;

public class AuthenticatePresenter<V extends BasePresenter.BaseView>  extends BasePresenter {

    public AuthenticatePresenter(V view) {
        super(view);
    }

    protected void validateAliasAndPassword(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }
}
