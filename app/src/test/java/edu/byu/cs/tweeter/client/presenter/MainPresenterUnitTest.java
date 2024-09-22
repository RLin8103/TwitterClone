package edu.byu.cs.tweeter.client.presenter;

// import static junit.framework.TestCase.assertEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {

    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    // private UserService mockUserService;
    private Cache mockCache;
    private Status mockStatus;
    private AuthToken authToken;
    private String expectedPostValue;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        // Create mocks
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        // mockUserService = Mockito.mock(UserService.class);
        mockCache = Mockito.mock(Cache.class);
        mockStatus = Mockito.mock(Status.class);
        authToken = new AuthToken("AuthToken");
        expectedPostValue = "This is a test post";

        // Create spy
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        // Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService();
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
        // Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);
        Mockito.when(mainPresenterSpy.getCache()).thenReturn(mockCache);
        Mockito.when(mockStatus.getPost()).thenReturn(expectedPostValue);

        Cache.setInstance(mockCache);
        Cache.getInstance().setCurrUserAuthToken(authToken);
    }

    @Test
    public void testPostStatus_postStatusSuccessful() {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgument(2, StatusService.PostStatusObserver.class);
                observer.postStatus();
                Status status = invocation.getArgument(1, Status.class);
                // Compare the values of the ?
                // ????whatparameters
                assertEquals(expectedPostValue, status.getPost());
                AuthToken authToken = invocation.getArgument(0, AuthToken.class);
                assertEquals("AuthToken", authToken.getToken());
                return null;
            }
        };
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(mockStatus);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).postStatus();
    }

    @Test
    public void testPostStatus_postStatusFailedWithMessage() {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgument(2, StatusService.PostStatusObserver.class);
                observer.displayError("Failed to post status: the error message");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(mockStatus);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayMessage("Failed to post status: the error message");
    }

    @Test
    public void testPostStatus_postStatusFailedWithException() {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgument(2, StatusService.PostStatusObserver.class);
                observer.displayException(new Exception());
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(mockStatus);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayMessage("Failed to post status because of exception: java.lang.Exception");
    }

//    @Test
//    public void testLogout_logoutSuccessful() {
//        Answer<Void> answer = new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                UserService.LogoutObserver observer = invocation.getArgument(0, UserService.LogoutObserver.class);
//                observer.userLogout();
//                return null;
//            }
//        };
//        Mockito.doAnswer(answer).when(mockUserService).logout(mockCache.getCurrUserAuthToken(), Mockito.any());
//        mainPresenterSpy.logout(mockCache.getCurrUserAuthToken());
//
//        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
//
//        Mockito.verify(mockCache).clearCache();
//        Mockito.verify(mockView).clearInfoMessage();
//        Mockito.verify(mockView).logoutUser();
//    }

//    @Test
//    public void testLogout_logoutFailedWithMessage() {
//        Answer<Void> answer = new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                UserService.LogoutObserver observer = invocation.getArgument(0, UserService.LogoutObserver.class);
//                observer.displayError("the error message");
//                return null;
//            }
//        };
//
//        Mockito.doAnswer(answer).when(mockUserService).logout(mockCache.getCurrUserAuthToken(), Mockito.any());
//        mainPresenterSpy.logout(mockCache.getCurrUserAuthToken());
//
//        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
//
//        Mockito.verify(mockCache, Mockito.timeout(0)).clearCache();
//        Mockito.verify(mockView).clearInfoMessage();
//        Mockito.verify(mockView).displayMessage("Failed to logout: the error message");
//    }

//    @Test
//    public void testLogout_logoutFailedWithException() {
//        Answer<Void> answer = new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                UserService.LogoutObserver observer = invocation.getArgument(0, UserService.LogoutObserver.class);
//                observer.displayException(new Exception("the exception message"));
//                return null;
//            }
//        };
//
//        Mockito.doAnswer(answer).when(mockUserService).logout(mockCache.getCurrUserAuthToken(), Mockito.any());
//        mainPresenterSpy.logout(mockCache.getCurrUserAuthToken());
//
//        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
//
//        Mockito.verify(mockCache, Mockito.timeout(0)).clearCache();
//        Mockito.verify(mockView).clearInfoMessage();
//        Mockito.verify(mockView).displayMessage("Failed to logout because of exception: the exception message");
//    }

}
