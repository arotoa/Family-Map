package result;

/**
 * The result class of a Person ID request
 */
public class SinglePersonResult extends Result {

    /**
     * Unique identifier for this person
     */
    private String personID;
    /**
     * Username of user to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person’s first name
     */
    private String firstName;
    /**
     * Person’s last name
     */
    private String lastName;
    /**
     * Person’s gender either "f" or "m"
     */
    private String gender;
    /**
     * Person ID of person’s father, may be null
     */
    private String fatherID;
    /**
     * Person ID of person’s mother, may be null
     */
    private String motherID;
    /**
     * Person ID of person’s spouse, may be null
     */
    private String spouseID;

    /**
     * Constructor of successful request
     * @param personID Unique identifier for this person
     * @param associatedUsername Username of user to which this person belongs
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender either "f" or "m"
     * @param fatherID Person ID of person’s father, may be null
     * @param motherID Person ID of person’s mother, may be null
     * @param spouseID Person ID of person’s spouse, may be null
     * @param success success
     */
    public SinglePersonResult(String personID, String associatedUsername, String firstName,
                              String lastName, String gender, String fatherID,
                              String motherID, String spouseID, boolean success) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        super.setSuccess(success);
    }

    /**
     * Constructor of LoadResult object
     * @param message Message of what happened
     * @param success Success or failure
     */
    public SinglePersonResult(boolean success, String message) {
        super.setSuccess(success);
        super.setMessage(message);
    }

    public String getPersonID() { return personID; }

    public String getGender() { return gender; }

    public boolean getSuccess() { return super.isSuccess(); }

    public String getMessage() { return super.getMessage(); }
}
