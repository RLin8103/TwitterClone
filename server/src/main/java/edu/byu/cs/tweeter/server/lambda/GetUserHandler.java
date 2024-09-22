package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function to handle retrieving a user's profile.
 */
public class GetUserHandler implements RequestHandler<UserRequest, UserResponse> {
    /**
     * Handles the request to get the user's profile.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the user response.
     */
    @Override
    public UserResponse handleRequest(UserRequest request, Context context) {
        UserService userService = new UserService();
        return userService.getUser(request);
    }
}
