package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.FeedBean;
import edu.byu.cs.tweeter.server.dao.bean.FollowBean;
import edu.byu.cs.tweeter.server.dao.bean.StoryBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

/**
 * A DAO for accessing 'status' data from the database.
 */
public class DynamoDBStatusDAO implements StatusDAO {

    // private final FakeData fakeData;

    private static final String FeedTableName = "feeds";
    private static final String StoryTableName = "stories";

    // DynamoDB client
    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    public DynamoDBStatusDAO() {
        // fakeData = FakeData.getInstance();
    }

    public Pair<List<Status>, Boolean> getFeed(String targetUserAlias, int limit, Status lastStatus) {
        DynamoDbTable<FeedBean> table = enhancedClient.table(FeedTableName, TableSchema.fromBean(FeedBean.class));

        // Assuming a GSI (Global Secondary Index) on userAlias for the feed
        Key key = Key.builder().partitionValue(targetUserAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastStatus != null && lastStatus.getTimestamp() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("userAlias", AttributeValue.builder().s(targetUserAlias).build());
            startKey.put("timestamp", AttributeValue.builder().n(String.valueOf(lastStatus.getTimestamp())).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();
        Pair<List<Status>, Boolean> result = new Pair<>(new ArrayList<>(), false);

        PageIterable<FeedBean> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedBean> page) -> {
                    result.setSecond(page.lastEvaluatedKey() != null);
                    page.items().forEach(feedBean -> result.getFirst().add(feedBean.getStatus()));
                });

        return result;
    }

    public Pair<List<Status>, Boolean> getStory(String targetUserAlias, int limit, Status lastStatus) {
        // TODO: Implement method logic
        // For now, using FakeData
        // return fakeData.getPageOfStatus(lastStatus, limit);
        DynamoDbTable<StoryBean> table = enhancedClient.table(StoryTableName, TableSchema.fromBean(StoryBean.class));

        Key key = Key.builder().partitionValue(targetUserAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastStatus != null && lastStatus.getTimestamp() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("userAlias", AttributeValue.builder().s(targetUserAlias).build());
            startKey.put("timestamp", AttributeValue.builder().n(String.valueOf(lastStatus.getTimestamp())).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();
        Pair<List<Status>, Boolean> result = new Pair<>(new ArrayList<>(), false);

        PageIterable<StoryBean> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<StoryBean> page) -> {
                    result.setSecond(page.lastEvaluatedKey() != null);
                    page.items().forEach(storyBean -> result.getFirst().add(storyBean.getStatus()));
                    page.items().forEach(storyBean -> System.out.println(storyBean.getStatusJson()));
                });

        return result;
    }

    public boolean postStatus(Status newStatus) {
        // TODO: Implement method logic to save a new status
        DynamoDbTable<StoryBean> storyTable = enhancedClient.table(StoryTableName, TableSchema.fromBean(StoryBean.class));
        DynamoDbTable<FeedBean> feedTable = enhancedClient.table(FeedTableName, TableSchema.fromBean(FeedBean.class));
        DynamoDbTable<FollowBean> followTable = enhancedClient.table("follows", TableSchema.fromBean(FollowBean.class));


        StoryBean storyBean = new StoryBean();
        storyBean.setUserAlias(newStatus.getUser().getAlias());
        storyBean.setStatus(newStatus);
        storyBean.setTimestamp(newStatus.getTimestamp());

        System.out.println("StoryBean details:");
        System.out.println("User Alias: " + storyBean.getUserAlias());
        System.out.println("Status: " + newStatus); // Assuming Status class has a meaningful toString() implementation
        System.out.println("Timestamp: " + storyBean.getTimestamp());

        try {
            storyTable.putItem(storyBean);
            System.out.println("POST SUCCESS!");
            return true;
        } catch (Exception e) {
            System.out.println("POST FAILTURE!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the feeds of followers with the new status.
     * This method performs batch writes to DynamoDB for efficiency.
     *
     * @param followers The list of users (followers) whose feed needs to be updated.
     * @param statusToUpdate The status to add to each follower's feed.
     */
    @Override
    public void updateFeed(List<User> followers, Status statusToUpdate) {
        List<FeedBean> batchToWrite = new ArrayList<>();
        for (User follower : followers) {
            FeedBean feedBean = new FeedBean();
            feedBean.setStatus(statusToUpdate);
            feedBean.setTimestamp(statusToUpdate.getTimestamp());
            feedBean.setUserAlias(follower.getAlias());
            System.out.println(feedBean.getStatusJson());
            System.out.println(feedBean.getTimestamp());
            System.out.println(feedBean.getUserAlias());

            batchToWrite.add(feedBean);

            if (batchToWrite.size() == 25) {
                writeChunkOfFeedBeans(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        if (!batchToWrite.isEmpty()) {
            writeChunkOfFeedBeans(batchToWrite);
        }
    }

    private void writeChunkOfFeedBeans(List<FeedBean> feedBeans) {
        if(feedBeans.size() > 25) {
            throw new RuntimeException("Too many feed items to write");
        }

        DynamoDbTable<FeedBean> table = enhancedClient.table(FeedTableName, TableSchema.fromBean(FeedBean.class));
        WriteBatch.Builder<FeedBean> writeBuilder = WriteBatch.builder(FeedBean.class).mappedTableResource(table);
        for (FeedBean item : feedBeans) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // Retry unprocessed items
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFeedBeans(result.unprocessedPutItemsForTable(table));
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
