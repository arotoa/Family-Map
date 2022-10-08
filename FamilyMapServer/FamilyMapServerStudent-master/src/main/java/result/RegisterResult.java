package result;

/**
 * The result class of a register request
 */
public class RegisterResult extends Result {

    /**
     * AuthToken as a string
     */
    private String authtoken;
    /**
     * Username of user
     */
    private String username;
    /**
     * ID associated with the user
     */
    private String personID;

    /**
     * Constructor if register request is accepted
     * @param authtoken Authtoken of user
     * @param username Username of user
     * @param personID ID associated with user
     * @param success Successfully added
     */
    public RegisterResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        super.setSuccess(success);
    }

    /**
     * Constructor if register request is not accepted
     * @param success Not successfully added
     * @param message Error message
     */
    public RegisterResult(boolean success, String message) {
        super.setSuccess(success);
        super.setMessage(message);
    }

    public String getAuthtoken() { return authtoken; }

    public String getUsername() { return username; }

    public String getPersonID() { return personID; }

    public boolean isSuccess() { return super.isSuccess(); }

    public String getMessage() { return super.getMessage(); }
}
