package edu.byu.cs.tweeter.server.lambda.sqs;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedMessageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class PostUpdateFeedMessages implements RequestHandler<SQSEvent, Void> {

    private AmazonSQS sqsClient = AmazonSQSClientBuilder.defaultClient();
    private final String updateFeedQueueUrl = "https://sqs.us-east-1.amazonaws.com/135132195266/UpdateFeedQueue";
    private Gson gson = new Gson();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        FollowService followService = new FollowService();

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
//            System.out.println("Received message: " + msg.getBody());
//
//            // Deserialize the message body to PostStatusRequest object
//            PostStatusRequest statusRequest = gson.fromJson(msg.getBody(), PostStatusRequest.class);
//
//            // Use FollowService to get followers of the status author
//            FollowersRequest followersRequest = new FollowersRequest(statusRequest.getAuthToken(),
//                    statusRequest.getStatus().getUser().getAlias(),
//                    100, null);
//
//            FollowersResponse followersResponse = followService.getFollowers(followersRequest);
//
//            UpdateFeedMessageRequest updateMsgRequest = new UpdateFeedMessageRequest(statusRequest.getStatus(),
//                    followersResponse.getFollowers());
//            String messageBody = gson.toJson(updateMsgRequest);
//
//            SendMessageRequest sendMsgRequest = new SendMessageRequest()
//                    .withQueueUrl(updateFeedQueueUrl)
//                    .withMessageBody(messageBody);
//
//            sqsClient.sendMessage(sendMsgRequest);
            System.out.println("Received message: " + msg.getBody());

            PostStatusRequest statusRequest = gson.fromJson(msg.getBody(), PostStatusRequest.class);
            String lastFollowerAlias = null; // Initialize the lastFollowerAlias to null

            while (true) {
                FollowersRequest followersRequest = new FollowersRequest(
                        statusRequest.getAuthToken(),
                        statusRequest.getStatus().getUser().getAlias(),
                        100, // Fetch 100 followers at a time
                        lastFollowerAlias);

                FollowersResponse followersResponse = followService.getFollowers(followersRequest);

                System.out.println(followersResponse.getFollowers().size());

                if (followersResponse.getFollowers().isEmpty()) {
                    break; // Break the loop if no more followers are returned
                }

                UpdateFeedMessageRequest updateMsgRequest = new UpdateFeedMessageRequest(
                        statusRequest.getStatus(),
                        followersResponse.getFollowers());
                String messageBody = gson.toJson(updateMsgRequest);

                SendMessageRequest sendMsgRequest = new SendMessageRequest()
                        .withQueueUrl(updateFeedQueueUrl)
                        .withMessageBody(messageBody);

                sqsClient.sendMessage(sendMsgRequest);

                // Prepare for the next iteration
                lastFollowerAlias = followersResponse.getFollowers()
                        .get(followersResponse.getFollowers().size() - 1)
                        .getAlias();
                if (!followersResponse.getHasMorePages()) {
                    break; // Break the loop if there are no more pages of followers
                }
            }
        }
        return null;
    }
}
