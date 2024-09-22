package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAO {

    public AuthToken saveAuthToken(String username);

    public AuthToken getAuthToken(String token);

    public void deleteAuthToken(String token);

    public void updateAuthToken(AuthToken authToken);
}
