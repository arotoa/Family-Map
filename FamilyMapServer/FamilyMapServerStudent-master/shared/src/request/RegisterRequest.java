package request;

/**
 * Creates an register request
 */
public class RegisterRequest {

    /**
     * Unique username for user
     */
    private final String username;
    /**
     * User’s password
     */
    private final String password;
    /**
     * User’s email address
     */
    private final String email;
    /**
     * User’s first name
     */
    private final String firstName;
    /**
     * User’s last name
     */
    private final String lastName;
    /**
     * User’s gender
     */
    private final String gender;

    /**
     * Constructor of RegisterRequest object
     * @param username Unique username for user
     * @param password User’s password
     * @param email User’s email address
     * @param firstName User’s first name
     * @param lastName User’s last name
     * @param gender User’s gender
     */
    public RegisterRequest(String username, String password, String email,
                           String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getGender() { return gender; }
}
