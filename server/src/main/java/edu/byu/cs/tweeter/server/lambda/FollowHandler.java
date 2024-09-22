package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that handles follow requests. This function is invoked to establish a
 * following relationship between two users.
 */
public class FollowHandler implements RequestHandler<FollowRequest, FollowResponse> {

    /**
     * Handles a follow request by invoking the FollowService to follow a user.
     * @param request The request object containing the information for the follow operation.
     * @param context The context of the lambda function execution environment.
     * @return The response object indicating the success or failure of the follow operation.
     */
    @Override
    public FollowResponse handleRequest(FollowRequest request, Context context) {
        FollowService followService = new FollowService();
        return followService.follow(request);
    }
}
