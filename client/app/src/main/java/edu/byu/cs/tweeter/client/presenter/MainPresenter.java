package edu.byu.cs.tweeter.client.presenter;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.View> {

    public interface View extends BaseView {
        
        void logoutUser();

        void logoutToast();

        void postStatus();

        void logError(Exception ex);

        void setFollowing();

        void setFollow();

        void unFollow();

        void enableFollowButton();

        void follow();

        void setFollowerCount(int count);

        void setFollowingCount(int count);

        void displayInfoMessage(String message);
    }

    FollowService followService;
    StatusService statusService;
    Cache cache;

    public MainPresenter(View view) {
        super(view);
        this.followService = new FollowService();
        this.statusService = getStatusService();
        // this.userService = getUserService();
        this.cache = getCache();
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

//    protected UserService getUserService() {
//        if (userService == null) {
//            userService = new UserService();
//        }
//        return userService;
//    }

    protected Cache getCache() {
        if (cache == null) {
            cache = Cache.getInstance();
        }
        return cache;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        followService.isFollower(currUserAuthToken, currUser, selectedUser, new IsFollowerServiceObserver());
    }

    public void Follow(AuthToken currUserAuthToken, User selectedUser) {
        FollowServiceObserver observer = new FollowServiceObserver();
        followService.follow(currUserAuthToken, selectedUser, observer);
        observer.displayMessage("Adding " + selectedUser.getName() + "...");
    }

    public void unFollow(AuthToken currUserAuthToken, User selectedUser) {
        UnFollowServiceObserver observer = new UnFollowServiceObserver();
        followService.unFollow(currUserAuthToken, selectedUser, observer);
        observer.displayMessage("Removing " + selectedUser.getName() + "...");
    }

    public void getFollowersFollowingCount(AuthToken currUserAuthToken, User selectedUser) {
        followService.getFollowersFollowingCount(currUserAuthToken, selectedUser,
                new GetFollowersCountServiceObserver(),
                new GetFollowingCountServiceObserver());
    }

    public void postStatus(Status status) {
        PostStatusServiceObserver observer = new PostStatusServiceObserver();
        observer.displayInfoMessage("Posting Status...");
        try {
            getStatusService().postStatus(cache.getCurrUserAuthToken(), status, observer);
        } catch (Exception ex) {
            observer.logError(ex);
            observer.displayException(ex);
        }
    }

    public void logout(AuthToken currUserAuthToken) {
        userService.logout(currUserAuthToken, new LogoutServiceObserver());
    }

    private class PostStatusServiceObserver extends BaseServiceObserver implements StatusService.PostStatusObserver {

        @Override
        public void postStatus() {
            view.postStatus();
        }

        @Override
        public void displayInfoMessage(String message) {
            view.displayInfoMessage(message);
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to post status because of exception: ";
        }

        @Override
        public void logError(Exception ex) {
            view.logError(ex);
        }
    }

    private class LogoutServiceObserver extends BaseServiceObserver implements UserService.LogoutObserver {

        @Override
        public void userLogout() {
            view.logoutToast();
            view.logoutUser();
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to logout because of exception: ";
        }
    }

    private class IsFollowerServiceObserver extends BaseServiceObserver implements FollowService.IsFollowerObserver {

        @Override
        protected String getExceptionMessage() {
            return "Failed to determine following relationship because of exception: ";
        }

        @Override
        public void isFollower(boolean isFollower) {
            // If logged in user if a follower of the selected user, display the follow button as "following"
            if (isFollower) {
                view.setFollowing();
            } else {
                view.setFollow();
            }
        }
    }

    private class FollowServiceObserver extends BaseServiceObserver implements FollowService.FollowObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to follow because of exception: ";
        }

        @Override
        public void follow() {
            view.follow();
        }

        @Override
        public void enableFollowButton() {
            view.enableFollowButton();
        }
    }

    private class UnFollowServiceObserver extends BaseServiceObserver implements FollowService.UnFollowObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        protected String getExceptionMessage() {
            return "Failed to unfollow because of exception: ";
        }

        @Override
        public void unFollow() {
            view.unFollow();
        }

        @Override
        public void enableFollowButton() {
            view.enableFollowButton();
        }
    }

    private class GetFollowersCountServiceObserver extends BaseServiceObserver implements FollowService.GetFollowersCountObserver {

        @Override
        protected String getExceptionMessage() {
            return "Failed to get followers count because of exception: ";
        }

        @Override
        public void setFollowerCount(int count) {
            view.setFollowerCount(count);
        }
    }

    private class GetFollowingCountServiceObserver extends BaseServiceObserver implements FollowService.GetFollowingCountObserver {

        @Override
        protected String getExceptionMessage() {
            return "Failed to get following count because of exception: ";
        }

        @Override
        public void setFollowingCount(int count) {
            view.setFollowingCount(count);
        }
    }

}
