package edu.byu.cs.tweeter.server.service;

import java.time.Instant;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDBDAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    private final FollowDAO followDAO;
    private DAOFactory dynamoDBDAOFactory;

    public FollowService() {
        dynamoDBDAOFactory = new DynamoDBDAOFactory();
        followDAO = dynamoDBDAOFactory.getFollowDAO();
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link DynamoDBFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = followDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }
    /**
     * Gets the count of followers for a specific user.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers count response.
     */
    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        // Validate the request
        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }

        // Logic to call the DAO and get the count of followers
        int count = followDAO.getFollowersCount(request.getTargetUserAlias());
        return new FollowersCountResponse(count);
    }

    /**
     * Retrieves the followers of the user specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers response.
     */
    public FollowersResponse getFollowers(FollowersRequest request) {
        // Validate the request
        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        // Logic to call the DAO and get the followers
        Pair<List<User>, Boolean> pair = followDAO.getFollowers(request.getTargetUserAlias(), request.getLimit(), request.getLastFollowerAlias());
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }

    /**
     * Gets the count of users that a specific user is following.
     *
     * @param request contains the data required to fulfill the request.
     * @return the following count response.
     */
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        // Validate the request
        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }

        // Logic to call the DAO and get the count of following
        int count = followDAO.getFollowingCount(request.getTargetUserAlias());
        return new FollowingCountResponse(count);
    }

    /**
     * Determines if one user is following another user.
     *
     * @param request contains the data required to fulfill the request.
     * @return the is follower response.
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        // Validate the request
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        // Logic to call the DAO and check if follower
        boolean isFollower = followDAO.isFollower(request.getFollowerAlias(), request.getFolloweeAlias());
        return new IsFollowerResponse(isFollower);
    }

    /**
     * Follows a user.
     *
     * @param request contains the data required to fulfill the request.
     * @return the follow response.
     */
    public FollowResponse follow(FollowRequest request) {
        // Validate the request
        if (request.getFollowee().getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        // Check if the AuthToken is expired
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime >= request.getAuthToken().getTimestamp()) {
            throw new RuntimeException("AuthToken is expired");
        }

        // Logic to call the DAO and follow a user
        boolean success = followDAO.follow(request.getCurrUser(), request.getFollowee());
        return new FollowResponse(success);
    }

    /**
     * Unfollows a user.
     *
     * @param request contains the data required to fulfill the request.
     * @return the unfollow response.
     */
    public UnfollowResponse unfollow(UnfollowRequest request) {
        // Validate the request
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        // Check if the AuthToken is expired
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime >= request.getAuthToken().getTimestamp()) {
            throw new RuntimeException("AuthToken is expired");
        }

        // Logic to call the DAO and unfollow a user
        boolean success = followDAO.unfollow(request.getCurrUser(), request.getFolloweeAlias());
        return new UnfollowResponse(success);
    }

    /**
     * Returns an instance of {@link DynamoDBFollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return followDAO;
    }
}
