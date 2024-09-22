package edu.byu.cs.tweeter.server.dao.bean;

import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.Status;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

// Partition Key: whoever the feed it is (receiver alias)
// Sort Key (timestamp)
@DynamoDbBean
public class FeedBean {

    private String userAlias;
    private Long timestamp;
    // private Status status;
    private String statusJson; // Store the JSON representation of the status

    public FeedBean() {
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

    // ... other getters and setters ...

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
