package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that checks if one user is following another.
 */
public class IsFollowerHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    /**
     * Handles the request to determine if the specified follower is following the followee.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the is follower response.
     */
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest request, Context context) {
        FollowService followService = new FollowService();
        return followService.isFollower(request);
    }
}
