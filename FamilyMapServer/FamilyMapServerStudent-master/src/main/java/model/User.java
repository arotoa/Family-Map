package model;

import java.util.Objects;

/**
 * User class. Stores data from the database into user objects
 */
public class User {

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
     * Unique Person ID assigned to this user’s generated Person
     */
    private final String personID;

    /**
     * Constructor of User object
     * @param username Unique username for user
     * @param password User’s password
     * @param email User’s email address
     * @param firstName User’s first name
     * @param lastName User’s last name
     * @param gender User’s gender
     * @param personID Unique Person ID assigned to this user’s generated Person
     */
    public User(String username, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password)
                && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && Objects.equals(gender, user.gender) && Objects.equals(email, user.email)
                && Objects.equals(personID, user.personID);
    }
}
