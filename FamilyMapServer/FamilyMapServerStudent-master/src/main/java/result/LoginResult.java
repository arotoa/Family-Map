package result;

/**
 * The result class of a login request
 */
public class LoginResult extends Result {

    /**
     * New authtoken associated with user
     */
    private String authtoken;
    /**
     * User's username
     */
    private String username;
    /**
     * User's personID
     */
    private String personID;

    /**
     * Constructor of successful request
     * @param authtoken New authtoken associated with user
     * @param username User's username
     * @param personID User's personID
     * @param success Success or failure
     */
    public LoginResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        super.setSuccess(success);
    }

    /**
     * Constructor if request was failed
     * @param success Failure
     * @param message Message of why it failed
     */
    public LoginResult(boolean success, String message) {
        super.setSuccess(success);
        super.setMessage(message);
    }

    public String getAuthtoken() { return authtoken; }

    public String getUsername() { return username; }

    public String getPersonID() { return personID; }

    public boolean isSuccess() { return super.isSuccess(); }

    public String getMessage() { return super.getMessage(); }
}
