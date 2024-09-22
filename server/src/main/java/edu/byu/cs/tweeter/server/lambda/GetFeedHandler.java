package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that retrieves the feed for a specific user.
 */
public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {
    /**
     * Handles the feed request by fetching the statuses posted to the user's feed.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the feed response.
     */
    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        StatusService statusService = new StatusService();
        return statusService.getFeed(request);
    }
}


