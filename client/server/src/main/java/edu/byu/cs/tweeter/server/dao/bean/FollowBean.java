package edu.byu.cs.tweeter.server.dao.bean;

import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowBean {

    private String follower_handle;
    private String followerName;
    private String followee_handle;
    private String followeeName;
//    private User follower;
//    private User followee;
    private String followerJson; // JSON representation of the follower User
    private String followeeJson; // JSON representation of the followee User

    public FollowBean() {
        // Do Nothing
    }

    public FollowBean(String followerHandle, String followerName, String followeeHandle, String followeeName) {
        this.follower_handle = followerHandle;
        this.followerName = followerName;
        this.followee_handle = followeeHandle;
        this.followeeName = followeeName;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = "follows_index")
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "follows_index")
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFolloweeName() {
        return followeeName;
    }

    public void setFolloweeName(String followeeName) {
        this.followeeName = followeeName;
    }

    public String getFollowerJson() {
        return followerJson;
    }

    public void setFollowerJson(String followerJson) {
        this.followerJson = followerJson;
    }

    public String getFolloweeJson() {
        return followeeJson;
    }

    public void setFolloweeJson(String followeeJson) {
        this.followeeJson = followeeJson;
    }

    @DynamoDbIgnore
    public User getFollower() {
        return new Gson().fromJson(followerJson, User.class);
    }

    public void setFollower(User follower) {
        this.followerJson = new Gson().toJson(follower);
    }

    @DynamoDbIgnore
    public User getFollowee() {
        return new Gson().fromJson(followeeJson, User.class);
    }

    public void setFollowee(User followee) {
        this.followeeJson = new Gson().toJson(followee);
    }

}
