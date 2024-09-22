package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that retrieves a list of followers for a specific user.
 */
public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {
    /**
     * Handles the request to get a list of followers of the user.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followers response.
     */
    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        FollowService followService = new FollowService();
        return followService.getFollowers(request);
    }
}
