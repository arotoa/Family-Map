package request;

/**
 * Creates an login request
 */
public class LoginRequest {

    /**
     * User's username
     */
    private final String username;
    /**
     * User's password
     */
    private final String password;

    /**
     * Constructor of LoginRequest object
     * @param username User's username
     * @param password User's password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }
}
