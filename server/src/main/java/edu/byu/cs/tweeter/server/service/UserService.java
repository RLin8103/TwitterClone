package edu.byu.cs.tweeter.server.service;

import java.time.Instant;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDBDAOFactory;
import edu.byu.cs.tweeter.server.dao.S3DAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserService {

    private final UserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;
    private final S3DAO s3DAO;
    private DAOFactory dynamoDBDAOFactory;

    public UserService() {
        dynamoDBDAOFactory = new DynamoDBDAOFactory();
        userDAO = dynamoDBDAOFactory.getUserDAO();
        authTokenDAO = dynamoDBDAOFactory.getAuthTokenDAO();
        s3DAO = dynamoDBDAOFactory.getS3DAO();
    }

    /**
     * Handles user login.
     *
     * @param request the login request.
     * @return the authentication response.
     */
    public AuthenticateResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        AuthenticateResponse response = null;
        User user = userDAO.login(request.getUsername(), request.getPassword());
        AuthToken authToken = null;
        if (user != null) {
            authToken = authTokenDAO.saveAuthToken(request.getUsername());
            response = new AuthenticateResponse(user, authToken);
        } else {
            throw new RuntimeException("[Bad Request] Invalid Login");
        }
        return response;
    }

    /**
     * Handles user registration.
     *
     * @param request the registration request.
     * @return the authentication response.
     */
    public AuthenticateResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing password");
        } else if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing profile image");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        // Assume imageUrl is now the URL of the image in S3
        AuthenticateResponse response = null;
        String imageUrl = s3DAO.uploadImageToS3(request.getImage(), request.getUsername());
        User user = userDAO.saveUser(request.getUsername(), request.getPassword(), request.getFirstName(), request.getLastName(), imageUrl);
        AuthToken authToken = null;
        if (user != null) {
            authToken = authTokenDAO.saveAuthToken(request.getUsername());
            response = new AuthenticateResponse(user, authToken);
        } else {
            throw new RuntimeException("[Bad Request] Invalid Registration");
        }
        return response;
    }

    /**
     * Handles user logout.
     *
     * @param request the logout request.
     * @return the logout response.
     */
    public LogoutResponse logout(LogoutRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing AuthToken");
        }

        return new LogoutResponse(true);
    }

    /**
     * Retrieves a user's information.
     *
     * @param request the user request.
     * @return the user response.
     */
    public UserResponse getUser(UserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing user alias");
        }

        // Check if the AuthToken is expired
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime >= request.getAuthToken().getTimestamp()) {
            throw new RuntimeException("AuthToken is expired");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        // User foundUser = getFakeData().findUserByAlias(request.getAlias());
        User foundUser = userDAO.getUser(request.getAlias());
        if (foundUser == null) {
            throw new RuntimeException("[Bad Request] Invalid user");
        } else {
            return new UserResponse(foundUser);
        }
    }

//    /**
//     * Returns the dummy user to be returned by the login operation.
//     * This is written as a separate method to allow mocking of the dummy user.
//     *
//     * @return a dummy user.
//     */
//    User getDummyUser() {
//        return getFakeData().getFirstUser();
//    }
//
//    /**
//     * Returns the dummy auth token to be returned by the login operation.
//     * This is written as a separate method to allow mocking of the dummy auth token.
//     *
//     * @return a dummy auth token.
//     */
//    AuthToken getDummyAuthToken() {
//        return getFakeData().getAuthToken();
//    }
//
//    /**
//     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
//     * This is written as a separate method to allow mocking of the {@link FakeData}.
//     *
//     * @return a {@link FakeData} instance.
//     */
//    FakeData getFakeData() {
//        return FakeData.getInstance();
//    }

}
