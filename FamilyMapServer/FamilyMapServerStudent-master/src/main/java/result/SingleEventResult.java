package result;

/**
 * The result class of an event id request
 */
public class SingleEventResult extends Result {

    /**
     * Event's ID
     */
    private String eventID;
    /**
     * Username of user to which this event belongs
     */
    private String associatedUsername;
    /**
     * ID of person to which this event belongs
     */
    private String personID;
    /**
     * Latitude of event’s location
     */
    private float latitude;
    /**
     * Longitude of event’s location
     */
    private float longitude;
    /**
     * Country in which event occurred
     */
    private String country;
    /**
     * City in which event occurred
     */
    private String city;
    /**
     * Type of event
     */
    private String eventType;
    /**
     * Year in which event occurred
     */
    private int year;

    /**
     * Constructor is request is successful
     * @param eventID Unique identifier for this event
     * @param associatedUsername Username of user to which this event belongs
     * @param personID ID of person to which this event belongs
     * @param latitude Latitude of event’s location
     * @param longitude Longitude of event’s location
     * @param country Country in which event occurred
     * @param city City in which event occurred
     * @param eventType Type of event
     * @param year Year in which event occurred
     * @param success Successful
     */
    public SingleEventResult(String eventID, String associatedUsername, String personID,
                             float latitude, float longitude, String country, String city,
                             String eventType, int year, boolean success) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        super.setSuccess(success);
    }

    /**
     * Constructor if request was failed
     * @param success Failure
     * @param message Message of why it failed
     */
    public SingleEventResult(boolean success, String message) {
        super.setSuccess(success);
        super.setMessage(message);
    }

    public String getPersonID() { return personID; }

    public boolean isSuccess() { return super.isSuccess(); }

    public String getMessage() { return super.getMessage(); }
}
