package result;

/**
 * The result class of a fill request
 */
public class FillResult extends Result {


    /**
     * Constructor of FillResult object
     * @param message Message of what happened
     * @param success Success or failure
     */
    public FillResult(String message, boolean success) {
        super.setMessage(message);
        super.setSuccess(success);
    }

    public String getMessage() { return super.getMessage(); }

    public boolean isSuccess() { return super.isSuccess(); }
}
