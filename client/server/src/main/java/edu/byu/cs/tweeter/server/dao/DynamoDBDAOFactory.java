package edu.byu.cs.tweeter.server.dao;

public class DynamoDBDAOFactory implements DAOFactory {

    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return new DynamoDBAuthTokenDAO();
    }

    @Override
    public UserDAO getUserDAO() {
        return new DynamoDBUserDAO();
    }

    @Override
    public StatusDAO getStatusDAO() {
        return new DynamoDBStatusDAO();
    }

    @Override
    public FollowDAO getFollowDAO() {
        return new DynamoDBFollowDAO();
    }

    @Override
    public S3DAO getS3DAO() {
        return new DynamoDBS3DAO();
    }
}
