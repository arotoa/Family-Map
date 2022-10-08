package request;

/**
 * Creates an Fill request
 */
public class FillRequest {

    /**
     * User's username
     */
    private final String username;
    /**
     * Number of generations wanted
     */
    private final String numOfGenerations;


    /**
     * Constructor with numOfGenerations defaulting to 4
     * @param username User's username
     */
    public FillRequest(String username) {
        this.username = username;
        numOfGenerations = "4";
    }

    /**
     * Constructor of FillRequest object
     * @param username User's username
     * @param numOfGenerations Number of generations
     */
    public FillRequest(String username, String numOfGenerations) {
        this.username = username;
        this.numOfGenerations = numOfGenerations;
    }

    public String getUsername() { return username; }

    public String getNumOfGenerations() { return numOfGenerations; }
}
