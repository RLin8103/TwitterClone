package edu.byu.cs.tweeter.server.dao.bean;

import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.Status;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

// Partition Key: whoever the story it is (receiver alias)
// Sort Key (timestamp)
@DynamoDbBean
public class StoryBean {
    private String userAlias;
    private Long timestamp;
    // private Status status;
    private String statusJson;

    public StoryBean() {
        // Default constructor
    }

    @DynamoDbPartitionKey
    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatusJson() {
        return statusJson;
    }

    public void setStatusJson(String statusJson) {
        this.statusJson = statusJson;
    }

    @DynamoDbIgnore
    public Status getStatus() {
        return new Gson().fromJson(statusJson, Status.class);
    }

    public void setStatus(Status status) {
        this.statusJson = new Gson().toJson(status);
    }
}
