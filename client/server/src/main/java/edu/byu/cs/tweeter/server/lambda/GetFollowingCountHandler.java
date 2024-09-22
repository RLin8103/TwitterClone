package edu.byu.cs.tweeter.server.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that retrieves the count of following for a specific user.
 */
public class GetFollowingCountHandler implements RequestHandler<FollowingCountRequest, FollowingCountResponse> {
    /**
     * Handles the request to get the count of users that the specified user is following.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the following count response.
     */
    @Override
    public FollowingCountResponse handleRequest(FollowingCountRequest request, Context context) {
        FollowService followService = new FollowService();
        return followService.getFollowingCount(request);
    }
}
