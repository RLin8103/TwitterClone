package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {

    User saveUser(String username, String password, String firstName, String lastName, String imageUrl);

    User login(String alias, String password);

    User getUser(String alias);

    void deleteUser(String alias);

    void updateUser(User user);

    void addUserBatch(List<User> users);
}
