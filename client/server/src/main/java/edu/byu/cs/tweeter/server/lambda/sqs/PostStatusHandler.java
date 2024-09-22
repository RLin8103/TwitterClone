package edu.byu.cs.tweeter.server.lambda.sqs;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;


/**
 * An AWS lambda function to handle posting a new status to the user's story.
 */
public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {

    private final String queueUrl = "https://sqs.us-east-1.amazonaws.com/135132195266/PostStatusQueue";
    private final Gson gson = new Gson();

    /**
     * Handles the request to post a status update for the user.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the post status response.
     */
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        StatusService statusService = new StatusService();
        PostStatusResponse response = statusService.postStatus(request);

        if (response.isSuccess()) {
            String messageBody = gson.toJson(request);

            SendMessageRequest sendMsgRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(messageBody);

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
            SendMessageResult sendMsgResult = sqs.sendMessage(sendMsgRequest);

            System.out.println("Message sent to SQS with ID: " + sendMsgResult.getMessageId());
        }

        return response;
    }

}