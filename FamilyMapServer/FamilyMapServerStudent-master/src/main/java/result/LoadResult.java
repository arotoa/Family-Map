package result;

/**
 * The result class of a load request
 */
public class LoadResult extends Result {


    /**
     * Constructor of LoadResult object
     * @param message Message of what happened
     * @param success Success or failure
     */
    public LoadResult(String message, boolean success) {
        super.setMessage(message);
        super.setSuccess(success);
    }

    public String getMessage() { return super.getMessage(); }

    public boolean isSuccess() { return super.isSuccess(); }
}
