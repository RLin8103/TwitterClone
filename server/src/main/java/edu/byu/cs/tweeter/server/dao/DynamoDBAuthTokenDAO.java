package edu.byu.cs.tweeter.server.dao;

import java.util.UUID;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.bean.AuthTokenBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoDBAuthTokenDAO implements AuthTokenDAO {

    private final DynamoDbTable<AuthTokenBean> authTokenTable;
    private static final String TableName = "authtokens";

    private DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();
    private DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    public DynamoDBAuthTokenDAO() {
        authTokenTable = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenBean.class));
    }

    public AuthToken saveAuthToken(String username) {
        String token = UUID.randomUUID().toString();
        long expirationDuration = 3; // Expiration duration in minutes
        long expireTime = Instant.now().plus(expirationDuration, ChronoUnit.MINUTES).toEpochMilli();

        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(token);
        authTokenBean.setUserAlias(username);
        authTokenBean.setExpireTime(expireTime);

        try {
            authTokenTable.putItem(authTokenBean);
            return new AuthToken(token, expireTime);
        } catch (DynamoDbException e) {
            // Handle the exception
            // You may want to log this exception or throw a custom exception
        }
        return null;
    }

    public AuthToken getAuthToken(String token) {
        try {
            AuthTokenBean authTokenBean = authTokenTable.getItem(Key.builder().partitionValue(token).build());
            if (authTokenBean != null) {
                return new AuthToken(authTokenBean.getToken(), authTokenBean.getExpireTime());
            }
        } catch (DynamoDbException e) {
            // Handle the exception
        }
        return null;
    }

    public void deleteAuthToken(String token) {
        try {
            authTokenTable.deleteItem(Key.builder().partitionValue(token).build());
        } catch (DynamoDbException e) {
            // Handle the exception
        }
    }

    public void updateAuthToken(AuthToken authToken) {
        // Assuming you only update expireTime
        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(authToken.getToken());
        authTokenBean.setExpireTime(authToken.getTimestamp());
        try {
            authTokenTable.updateItem(authTokenBean);
        } catch (DynamoDbException e) {
            // Handle the exception
        }
    }
}
