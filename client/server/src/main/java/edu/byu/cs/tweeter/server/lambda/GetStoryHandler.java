package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function to handle retrieving the story for a specific user.
 */
public class GetStoryHandler implements RequestHandler<StoryRequest, StoryResponse> {
    /**
     * Handles the request to get the story (statuses posted by the user).
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the story response.
     */
    @Override
    public StoryResponse handleRequest(StoryRequest request, Context context) {
        StatusService statusService = new StatusService();
        return statusService.getStory(request);
    }
}
