package result;

import model.Event;

/**
 * The result class of a clear request
 */
public class AllEventsResult extends Result {

    /**
     * All events
     */
    private Event[] data;

    /**
     * Constructor of successful request
     * @param data All events
     * @param success Success
     */
    public AllEventsResult(Event[] data, boolean success) {
        this.data = data;
        super.setSuccess(success);
    }

    /**
     * Constructor of failed request
     * @param success Failure
     * @param message Message of why it failed
     */
    public AllEventsResult(boolean success, String message) {
        super.setSuccess(success);
        super.setMessage(message);
    }

    public Event[] getData() { return data; }

    public boolean isSuccess() { return super.isSuccess(); }

    public String getMessage() { return super.getMessage(); }
}
