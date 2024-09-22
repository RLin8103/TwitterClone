package edu.byu.cs.tweeter.server.lambda.sqs;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedMessageRequest;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeeds implements RequestHandler<SQSEvent, Void> {

    private Gson gson = new Gson();
    private StatusService statusService = new StatusService();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println("Received message: " + msg.getBody());

            // Deserialize the message
            UpdateFeedMessageRequest updateFeedRequest = gson.fromJson(msg.getBody(), UpdateFeedMessageRequest.class);

            // Extract the status and the list of followers
            Status statusToUpdate = updateFeedRequest.getStatus();
            List<User> followers = updateFeedRequest.getFollowers();

            statusService.updateFeedForFollower(followers, statusToUpdate);
        }
        return null;
    }
}
