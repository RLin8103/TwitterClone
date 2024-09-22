package edu.byu.cs.tweeter.server.service;

import java.time.Instant;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDBDAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDBStatusDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    private final StatusDAO statusDAO;
    private DAOFactory dynamoDBDAOFactory;

    public StatusService() {
        dynamoDBDAOFactory = new DynamoDBDAOFactory();
        statusDAO = dynamoDBDAOFactory.getStatusDAO();
    }

    /**
     * Retrieves the statuses for the feed of the user specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the feed response.
     */
    public FeedResponse getFeed(FeedRequest request) {
        // TODO: Implement method to get the feed statuses
        // StatusDAO statusDAO = getStatusDAO();
        Pair<List<Status>, Boolean> feedResult = statusDAO.getFeed(request.getUserAlias(), request.getLimit(), request.getLastStatus());
        return new FeedResponse(feedResult.getFirst(), feedResult.getSecond());
    }

    /**
     * Retrieves the story (statuses posted by the user) specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the story response.
     */
    public StoryResponse getStory(StoryRequest request) {
        // TODO: Implement method to get the user's story statuses
        // StatusDAO statusDAO = getStatusDAO();
        Pair<List<Status>, Boolean> storyResult = statusDAO.getStory(request.getTargetUserAlias(), request.getLimit(), request.getLastStatus());
        return new StoryResponse(storyResult.getFirst(), storyResult.getSecond());
    }

    /**
     * Posts a new status as specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the post status response.
     */
    public PostStatusResponse postStatus(PostStatusRequest request) {
        // TODO: Implement method to post a new status
        // StatusDAO statusDAO = getStatusDAO();
        // Check if the AuthToken is expired
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime >= request.getAuthToken().getTimestamp()) {
            throw new RuntimeException("AuthToken is expired");
        }
        boolean postResult = statusDAO.postStatus(request.getStatus());
        return new PostStatusResponse(postResult);
    }

    /**
     * Updates the feed for a specific follower with a new status.
     * This method calls the StatusDAO to add the status to the feed of the given follower.
     *
     * @param followers the users whose feed is to be updated.
     * @param statusToUpdate the status to add to the follower's feed.
     */
    public void updateFeedForFollower(List<User> followers, Status statusToUpdate) {
        // Check if the input parameters are valid
        if (followers == null || statusToUpdate == null) {
            throw new IllegalArgumentException("Follower and status to update cannot be null");
        }

        // Call StatusDAO to update the feed
        statusDAO.updateFeed(followers, statusToUpdate);
    }

    /**
     * Returns an instance of {@link DynamoDBStatusDAO}. Allows mocking of the StatusDAO class
     * for testing purposes. All usages of StatusDAO should get their StatusDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StatusDAO getStatusDAO() {
        return statusDAO;
    }
}
