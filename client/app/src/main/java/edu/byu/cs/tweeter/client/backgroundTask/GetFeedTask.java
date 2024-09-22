package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetFeedTask";

    public static final String STATUSES_KEY = "statuses";
    public static final String MORE_PAGES_KEY = "more-pages";

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
    }

    @Override
    protected void retrieveItems() {
//        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(getLastItem(), getLimit());

        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            Status lastStatus = getLastItem();

            FeedRequest request = new FeedRequest(getAuthToken(), targetUserAlias, getLimit(), lastStatus);
            FeedResponse response = getServerFacade().getFeed(request, StatusService.GETFEED_URL_PATH);

            if (response.isSuccess()) {
                setItems(response.getStatuses());
                setHasMorePages(response.getHasMorePages());
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get feed", ex);
            sendExceptionMessage(ex);
        }
//        return null;
    }

}
