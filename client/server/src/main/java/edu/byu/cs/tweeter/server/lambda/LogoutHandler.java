package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user out of the application.
 */
public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    /**
     * Handles the request to log out the user.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the logout response.
     */
    @Override
    public LogoutResponse handleRequest(LogoutRequest request, Context context) {
        UserService userService = new UserService();
        return userService.logout(request);
    }
}
