package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function to handle unfollowing another user.
 */
public class UnfollowHandler implements RequestHandler<UnfollowRequest, UnfollowResponse> {
    /**
     * Handles the request to unfollow another user.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the unfollow response.
     */
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest request, Context context) {
        FollowService followService = new FollowService();
        return followService.unfollow(request);
    }
}

