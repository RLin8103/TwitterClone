package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class PostStatusIntegrationTest {

    private User currentUser;
    private Status newStatus;
    private AuthToken currentAuthToken;

    private ServerFacade serverFacade;

    private MainPresenter mainPresenterSpy;
    private MainPresenter.View viewSpy;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        // Basic Information
        currentUser = new User("Ray", "Lin", "@followed", null);
        newStatus = createRandomStatus(currentUser);
        currentAuthToken = new AuthToken("Random");

        serverFacade = getServerFacade();

        viewSpy = Mockito.mock(MainPresenter.View.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(viewSpy));

        // Mock viewSpy methods with Answers
        Mockito.doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(viewSpy).postStatus();

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private Status createRandomStatus(User user) {
        Random random = new Random();

        // Generate a random post message
        String post = "Random Post " + random.nextInt(1000);

        // Generate a random timestamp
        Long timestamp = System.currentTimeMillis() + random.nextInt(1000);

        // Generate random URLs and mentions (for simplicity, using fixed number of elements)
        List<String> urls = new ArrayList<>();
        urls.add("https://example.com/" + random.nextInt(100));

        List<String> mentions = new ArrayList<>();
        mentions.add("@user" + random.nextInt(100));

        return new Status(post, user, timestamp, urls, mentions);
    }

    private ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return serverFacade;
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void testLoadMoreStories_validRequest_correctResponse() throws InterruptedException, IOException, TweeterRemoteException {

        // A. Login a user.
        LoginRequest loginRequest = new LoginRequest("@followed", "040421ray");
        AuthenticateResponse loginResponse = serverFacade.login(loginRequest, UserService.LOGIN_URL_PATH);

        // B. Post a status from the user to the server by calling the "post status" operation on the relevant Presenter.
        Cache.getInstance().setCurrUserAuthToken(loginResponse.getAuthToken());
        mainPresenterSpy.postStatus(newStatus);
        awaitCountDownLatch();

        // C. Verify that the "Successfully Posted!" message was displayed to the user.
        Mockito.verify(viewSpy).displayInfoMessage("Posting Status...");
        Mockito.verify(viewSpy, Mockito.atLeastOnce()).postStatus();

        // D. Retrieve the user's story from the server to verify that the new status was correctly appended to
        // the user's story, and that all status details are correct.
        currentAuthToken = loginResponse.getAuthToken();
        StoryRequest storyRequest = new StoryRequest(currentAuthToken, "@followed", 10, null);
        StoryResponse storyResponse = serverFacade.getStory(storyRequest, StatusService.GETSTORY_URL_PATH);
        List<Status> stories = storyResponse.getStatuses();
        Status story = stories.get(0);

        Assertions.assertEquals(newStatus.getPost(), story.getPost());
        Assertions.assertEquals(newStatus.getMentions(), story.getMentions());
        Assertions.assertEquals(newStatus.getTimestamp(), story.getTimestamp());
        Assertions.assertEquals(newStatus.getUrls(), story.getUrls());
        Assertions.assertEquals(newStatus.getUser().getAlias(), story.getUser().getAlias());
    }
}
