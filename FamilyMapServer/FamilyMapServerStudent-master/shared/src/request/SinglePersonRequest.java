package request;

import model.AuthToken;

/**
 * Creates an person ID request
 */
public class SinglePersonRequest {

    /**
     * ID of person wanted
     */
    private final String personID;
    /**
     * Authtoken assoociated with the user
     */
    private final String authToken;

    /**
     * Constructor of PersonIDRequest object
     * @param personID ID of person wanted
     * @param authToken User's authtoken
     */
    public SinglePersonRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() { return personID; }

    public String getAuthToken() { return authToken; }
}
