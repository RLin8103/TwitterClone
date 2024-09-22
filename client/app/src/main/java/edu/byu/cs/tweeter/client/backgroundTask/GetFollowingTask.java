package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowingTask";

    public static final String FOLLOWEES_KEY = "followees";
    public static final String MORE_PAGES_KEY = "more-pages";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollowee);
    }

    @Override
    protected void retrieveItems() {
        // Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());

        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias();

            FollowingRequest request = new FollowingRequest(getAuthToken(), targetUserAlias, getLimit(), lastFolloweeAlias);
            FollowingResponse response = getServerFacade().getFollowees(request, FollowService.GETFOLLOWING_URL_PATH);

            if (response.isSuccess()) {
                setItems(response.getFollowees());
                setHasMorePages(response.getHasMorePages());
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }

//        return null;
    }
}
