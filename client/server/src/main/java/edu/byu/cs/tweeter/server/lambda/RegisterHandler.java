package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function to handle the registration of a new user.
 */
public class RegisterHandler implements RequestHandler<RegisterRequest, AuthenticateResponse> {
    /**
     * Handles the request to register a new user.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the register response.
     */
    @Override
    public AuthenticateResponse handleRequest(RegisterRequest request, Context context) {
        UserService userService = new UserService();
        return userService.register(request);
    }
}
