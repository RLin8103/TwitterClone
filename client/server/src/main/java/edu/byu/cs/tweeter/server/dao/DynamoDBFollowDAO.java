package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.FollowBean;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoDBFollowDAO implements FollowDAO {

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    // DynamoDB client
    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the user whose followees are to be returned
     * @param limit the number of followees to be returned in one page
     * @param lastFolloweeAlias the alias of the last followee in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followees.
     */
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        // TODO: Generates dummy data. Replace with a real implementation.
        DynamoDbTable<FollowBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));

        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastFolloweeAlias != null && !lastFolloweeAlias.isEmpty()) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();
        Pair<List<User>, Boolean> result = new Pair<>(new ArrayList<>(), false);
        PageIterable<FollowBean> pages = null;

        try {
            pages = table.query(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    result.setSecond(page.lastEvaluatedKey() != null);
                    page.items().forEach(followBean -> result.getFirst().add(followBean.getFollowee()));
                });

        return result;
    }

    /**
     * Gets the number of users who follow the specified user.
     *
     * @param targetUserAlias the alias of the user whose followers are being counted.
     * @return the number of followers.
     */
    public int getFollowersCount(String targetUserAlias) {
//        DynamoDbTable<FollowBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
//        Key key = Key.builder().partitionValue(targetUserAlias).build();
//
//        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key))
//                .build();
//
//        int count = 0;
//        for (Page<FollowBean> page : table.query(request)) {
//            count += page.items().size();
//        }
//        return count;
        DynamoDbTable<UserBean> usersTable = enhancedClient.table("users", TableSchema.fromBean(UserBean.class));
        UserBean userBean = usersTable.getItem(Key.builder().partitionValue(targetUserAlias).build());
        return userBean != null ? userBean.getFollowersCount() : 0;
    }

    /**
     * Retrieves the followers of the specified user.
     *
     * @param targetUserAlias the alias of the user whose followers to retrieve.
     * @param limit the maximum number of followers to return.
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous request.
     * @return a pair containing the list of followers and a boolean indicating if there are more followers.
     */
    public Pair<List<User>, Boolean> getFollowers(String targetUserAlias, int limit, String lastFollowerAlias) {
        // Placeholder for database access logic to get followers
        // For now, it simply returns a portion of the fake data for demonstration purposes
        // User targetUser = getFakeData().findUserByAlias(targetUserAlias);
        // User lastFollower = getFakeData().findUserByAlias(lastFollowerAlias);
        // return getFakeData().getPageOfUsers(lastFollower, limit, targetUser); // should interact with the actual data source

        DynamoDbIndex<FollowBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastFollowerAlias != null && !lastFollowerAlias.isEmpty()) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();
        Pair<List<User>, Boolean> result = new Pair<>(new ArrayList<>(), false);

        SdkIterable<Page<FollowBean>> sdkIterable = index.query(request);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    result.setSecond(page.lastEvaluatedKey() != null);
                    page.items().forEach(followBean -> result.getFirst().add(followBean.getFollower()));
                    page.items().forEach(followBean -> System.out.println(followBean.getFollower()));
                });

        return result;
    }

    /**
     * Gets the number of users that the specified user is following.
     *
     * @param targetUserAlias the alias of the user whose followee count to retrieve.
     * @return the number of followees.
     */
    public int getFollowingCount(String targetUserAlias) {
//        DynamoDbIndex<FollowBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
//        Key key = Key.builder().partitionValue(targetUserAlias).build();
//
//        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key))
//                .build();
//
//        int count = 0;
//        for (Page<FollowBean> page : index.query(request)) {
//            count += page.items().size();
//        }
//        return count;
        DynamoDbTable<UserBean> usersTable = enhancedClient.table("users", TableSchema.fromBean(UserBean.class));
        UserBean userBean = usersTable.getItem(Key.builder().partitionValue(targetUserAlias).build());
        return userBean != null ? userBean.getFolloweesCount() : 0;
    }

    /**
     * Determines if a user with followerAlias is following a user with followeeAlias.
     *
     * @param followerAlias the alias of the user who may be following the other user.
     * @param followeeAlias the alias of the user who may be followed by the other user.
     * @return true if follower is following followee, false otherwise.
     */
    public boolean isFollower(String followerAlias, String followeeAlias) {
        // Placeholder for database access logic to determine if one user follows another
        // For now, it simply returns a random boolean for demonstration purposes
        // return new java.util.Random().nextBoolean(); // should interact with the actual data source

//        DynamoDbIndex<FollowBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
//        Key key = Key.builder()
//                .partitionValue(followerAlias)
//                .sortValue(followeeAlias)
//                .build();
//
//        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key))
//                .build();
//
//        // Check if the query returns any items
//        SdkIterable<Page<FollowBean>> results = index.query(request);
//        for (Page<FollowBean> page : results) {
//            if (!page.items().isEmpty()) {
//                return true;
//            }
//        }
//        return false;

        DynamoDbTable<FollowBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();

        // Try to get the item using the key
        FollowBean followBean = null;
        try {
            followBean = table.getItem(r -> r.key(key));
        } catch (Exception e) {
            System.err.println("Unable to get follow relationship: " + e.getMessage());
            e.printStackTrace();
        }

        // If the followBean is not null, then the follower is following the followee
        return followBean != null;
    }

    /**
     * Follows a user with the specified alias.
     *
     * @param followee the user to be followed.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean follow(User currUser, User followee) {
        DynamoDbTable<FollowBean> followsTable = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));

        DynamoDbTable<UserBean> usersTable = enhancedClient.table("users", TableSchema.fromBean(UserBean.class));

        FollowBean followBean = new FollowBean();
        followBean.setFollower_handle(currUser.getAlias());
        followBean.setFollowee_handle(followee.getAlias());
        followBean.setFollowerName(currUser.getName());
        followBean.setFolloweeName(followee.getName());
        followBean.setFollower(currUser);
        followBean.setFollowee(followee);

        // Print details for verification
        System.out.println("FollowBean details:");
        System.out.println("Follower Handle: " + followBean.getFollower_handle());
        System.out.println("Followee Handle: " + followBean.getFollowee_handle());
        System.out.println("Follower Name: " + followBean.getFollowerName());
        System.out.println("Followee Name: " + followBean.getFolloweeName());

        try {
            // Add follow relationship
            followsTable.putItem(followBean);

            // Update follower count for followee
            UserBean followeeBean = usersTable.getItem(r -> r.key(k -> k.partitionValue(followee.getAlias())));
            followeeBean.setFollowersCount(followeeBean.getFollowersCount() + 1);
            usersTable.updateItem(followeeBean);

            // Update following count for current user
            UserBean currUserBean = usersTable.getItem(r -> r.key(k -> k.partitionValue(currUser.getAlias())));
            currUserBean.setFolloweesCount(currUserBean.getFolloweesCount() + 1);
            usersTable.updateItem(currUserBean);

            System.out.println("Follow Success!");

            return true;
        } catch (Exception e) {
            // Log exception and handle error
            System.out.println("Follow Failed!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Unfollows a user with the specified alias.
     *
     * @param followeeAlias the alias of the user to be unfollowed.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean unfollow(User currUser, String followeeAlias) {

        // TODO: Get Current user
        String followerAlias = currUser.getAlias();
        DynamoDbTable<FollowBean> followsTable = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
        DynamoDbTable<UserBean> usersTable = enhancedClient.table("users", TableSchema.fromBean(UserBean.class));

        Key key = Key.builder()
                .partitionValue(currUser.getAlias())
                .sortValue(followeeAlias)
                .build();

        try {
            // Remove follow relationship
            followsTable.deleteItem(key);

            // Update follower count for followee
            UserBean followeeBean = usersTable.getItem(r -> r.key(k -> k.partitionValue(followeeAlias)));
            followeeBean.setFollowersCount(followeeBean.getFollowersCount() - 1);
            usersTable.updateItem(followeeBean);

            // Update following count for current user
            UserBean currUserBean = usersTable.getItem(r -> r.key(k -> k.partitionValue(currUser.getAlias())));
            currUserBean.setFolloweesCount(currUserBean.getFolloweesCount() - 1);
            usersTable.updateItem(currUserBean);

            return true;
        } catch (Exception e) {
            // Log exception and handle error
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addFollowersBatch(List<User> followers, String followTarget) {
        List<FollowBean> batchToWrite = new ArrayList<>();
        User followed = new User("Ray", "Lin", "@followed", "https://ruilinfirstbucket.s3.us-east-1.amazonaws.com/@followed");
        for (User follower : followers) {
            FollowBean followBean = new FollowBean();
            followBean.setFollower_handle(follower.getAlias());
            followBean.setFollowee_handle(followed.getAlias());
            followBean.setFollower(follower);
            followBean.setFollowee(followed);
            followBean.setFollowerName(follower.getName());
            followBean.setFolloweeName(followed.getName());
            // Other attributes of FollowBean can be set here if necessary
            batchToWrite.add(followBean);

            if (batchToWrite.size() == 25) {
                writeChunkOfFollowBeans(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // Write any remaining items in the batch
        if (!batchToWrite.isEmpty()) {
            writeChunkOfFollowBeans(batchToWrite);
        }
    }

    private void writeChunkOfFollowBeans(List<FollowBean> followBeans) {
        if (followBeans.size() > 25) {
            throw new RuntimeException("Too many follow relationships to write in a single batch");
        }

        DynamoDbTable<FollowBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
        WriteBatch.Builder<FollowBean> writeBuilder = WriteBatch.builder(FollowBean.class)
                .mappedTableResource(table);
        for (FollowBean followBean : followBeans) {
            writeBuilder.addPutItem(builder -> builder.item(followBean));
        }

        BatchWriteItemEnhancedRequest batchWriteRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build())
                .build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteRequest);

            // Retry unprocessed items
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFollowBeans(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
