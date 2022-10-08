package result;

import model.Person;

/**
 * The result class of a person request
 */
public class AllPersonsResult extends Result {

    /**
     * All persons
     */
    private Person[] data;

    /**
     * Constructor of successful request
     * @param data All persons
     * @param success Success
     */
    public AllPersonsResult(Person[] data, boolean success) {
        this.data = data;
        super.setSuccess(success);
    }

    /**
     * Constructor of failed request
     * @param success Failure
     * @param message Message of why it failed
     */
    public AllPersonsResult(boolean success, String message) {
        super.setSuccess(success);
        super.setMessage(message);
    }

    public Person[] getData() { return data; }

    public boolean isSuccess() { return super.isSuccess(); }

    public String getMessage() { return super.getMessage(); }
}
