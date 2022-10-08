package result;

/**
 * The result class of a clear request
 */
public class ClearResult extends Result {

    /**
     * Constructor of ClearResult object
     * @param message
     * @param success
     */
    public ClearResult(String message, boolean success) {
        super.setMessage(message);
        super.setSuccess(success);
    }

    public String getMessage() { return super.getMessage(); }

    public boolean isSuccess() { return super.isSuccess(); }
}
