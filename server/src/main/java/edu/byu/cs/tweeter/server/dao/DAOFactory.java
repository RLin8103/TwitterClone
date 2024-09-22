package edu.byu.cs.tweeter.server.dao;

public interface DAOFactory {
    AuthTokenDAO getAuthTokenDAO();
    UserDAO getUserDAO();
    StatusDAO getStatusDAO();
    FollowDAO getFollowDAO();
    S3DAO getS3DAO();
}
