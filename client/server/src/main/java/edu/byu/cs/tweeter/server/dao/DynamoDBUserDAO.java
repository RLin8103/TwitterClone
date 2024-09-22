package edu.byu.cs.tweeter.server.dao;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoDBUserDAO implements UserDAO {

    private final DynamoDbTable<UserBean> userTable;

    private static final String TableName = "users";
    DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();
    DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    public DynamoDBUserDAO() {
        userTable = enhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));
    }

    public User saveUser(String username, String password, String firstName, String lastName, String imageUrl) {

        // Encoded password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        UserBean userBean = new UserBean();
        userBean.setAlias(username);
        userBean.setPassword(hashedPassword);
        userBean.setFirstName(firstName);
        userBean.setLastName(lastName);
        userBean.setImageUrl(imageUrl);

        System.out.println(username);
        System.out.println(password);
        System.out.println(imageUrl);

        userTable.putItem(userBean);
        return new User(firstName, lastName, username, imageUrl);
    }

    public User login(String alias, String password) {
        UserBean userBean = userTable.getItem(Key.builder().partitionValue(alias).build());
        if (userBean == null) {
            System.out.println("User not found");
            return null;
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, userBean.getPassword())) {
                // Passwords match, proceed with login
                return new User(userBean.getFirstName(), userBean.getLastName(), userBean.getAlias(), userBean.getImageUrl());
            } else {
                // Passwords do not match
                System.out.println("Incorrect password");
                return null;
            }
        }
    }

    public User getUser(String alias) {
        UserBean userBean = userTable.getItem(Key.builder().partitionValue(alias).build());
        if (userBean != null) {
            System.out.println("Get User Success!");
            return new User(userBean.getFirstName(), userBean.getLastName(), userBean.getAlias(), userBean.getImageUrl());
        } else {
            System.out.println("Get User Failure!");
            return null;
        }
    }

    public void deleteUser(String alias) {
        try {
            userTable.deleteItem(Key.builder().partitionValue(alias).build());
        } catch (DynamoDbException e) {
            // Handle the exception
        }
    }

    public void updateUser(User user) {
        UserBean userBean = new UserBean();
        userBean.setAlias(user.getAlias());
        userBean.setFirstName(user.getFirstName());
        userBean.setLastName(user.getLastName());
        userBean.setImageUrl(user.getImageUrl());
        userTable.updateItem(userBean);
    }

    // Additional methods as needed
    public void addUserBatch(List<User> users) {
        List<UserBean> batchToWrite = new ArrayList<>();
        for (User u : users) {
            UserBean dto = new UserBean();
            dto.setFirstName(u.getFirstName());
            dto.setLastName(u.getLastName());
            dto.setAlias(u.getAlias());
            dto.setImageUrl(u.getImageUrl());
            batchToWrite.add(dto);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }

    private void writeChunkOfUserDTOs(List<UserBean> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        DynamoDbTable<UserBean> table = enhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));
        WriteBatch.Builder<UserBean> writeBuilder = WriteBatch.builder(UserBean.class).mappedTableResource(table);
        for (UserBean item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}

