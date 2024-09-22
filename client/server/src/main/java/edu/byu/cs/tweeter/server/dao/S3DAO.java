package edu.byu.cs.tweeter.server.dao;

public interface S3DAO {
    public String uploadImageToS3(String imageString, String alias);
}
