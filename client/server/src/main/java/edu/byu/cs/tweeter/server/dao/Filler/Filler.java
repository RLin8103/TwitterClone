package edu.byu.cs.tweeter.server.dao.Filler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDBDAOFactory;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class Filler {

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@followed";

    public static void main(String[] args) {
        try {
            System.out.println("Starting database filling process...");
            fillDatabase();
            System.out.println("Database has been filled successfully.");
        } catch (Exception e) {
            System.err.println("Error occurred while filling the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void fillDatabase() {

        DAOFactory factory = new DynamoDBDAOFactory();

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAO userDAO = factory.getUserDAO();
        FollowDAO followDAO = factory.getFollowDAO();

        List<User> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String fname = "Guy", lname = Integer.toString(i);
            String alias = "@guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User(fname, lname, alias, "https://example.com/" + alias + ".jpg");

            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(user);
        }

        // Call the DAOs for the database logic
//        if (users.size() > 0) {
//            userDAO.addUserBatch(users);
//        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }
}

