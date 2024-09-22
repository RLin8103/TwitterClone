package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that retrieves the count of followers for a specific user.
 */
public class GetFollowersCountHandler implements RequestHandler<FollowersCountRequest, FollowersCountResponse> {
    /**
     * Handles the request to get the count of followers of the user.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followers count response.
     */
    @Override
    public FollowersCountResponse handleRequest(FollowersCountRequest request, Context context) {
        FollowService followService = new FollowService();
        return followService.getFollowersCount(request);
    }
}