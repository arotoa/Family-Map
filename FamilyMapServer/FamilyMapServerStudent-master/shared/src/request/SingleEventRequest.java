package request;

import model.AuthToken;

/**
 * Creates an Event ID request
 */
public class SingleEventRequest {

    /**
     * ID of event beign requested
     */
    private final String eventId;
    /**
     * User's authtoken
     */
    private final String authToken;

    /**
     * Constructor of object
     * @param eventId ID of event beign requested
     * @param authToken User's authtoken
     */
    public SingleEventRequest(String eventId, String authToken) {
        this.eventId = eventId;
        this.authToken = authToken;
    }

    public String getEventId() { return eventId; }

    public String getAuthToken() { return authToken; }
}
