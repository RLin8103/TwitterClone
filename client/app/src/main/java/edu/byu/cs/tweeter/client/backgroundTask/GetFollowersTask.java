package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollower);
    }

    @Override
    protected void retrieveItems() {
//        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFollowerAlias = getLastItem() == null ? null : getLastItem().getAlias();

            FollowersRequest request = new FollowersRequest(getAuthToken(), targetUserAlias, getLimit(), lastFollowerAlias);
            FollowersResponse response = getServerFacade().getFollowers(request, FollowService.GETFOLLOWERS_URL_PATH);

            if (response.isSuccess()) {
                setItems(response.getFollowers());
                setHasMorePages(response.getHasMorePages());
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers", ex);
            sendExceptionMessage(ex);
        }
//        return null;
    }
}
