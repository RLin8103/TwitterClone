package edu.byu.cs.tweeter.model.net.request;

/**
 * Contains all the information needed to make a registration request.
 */
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String image; // Base-64 encoded image string

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private RegisterRequest() {}

    /**
     * Creates an instance of the registration request.
     *
     * @param firstName the first name of the user to be registered.
     * @param lastName  the last name of the user to be registered.
     * @param username  the username for the new account.
     * @param password  the password for the new account.
     * @param image     the base-64 encoded string of the user's profile image.
     */
    public RegisterRequest(String firstName, String lastName, String username, String password, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    // Getter and setter for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and setter for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
