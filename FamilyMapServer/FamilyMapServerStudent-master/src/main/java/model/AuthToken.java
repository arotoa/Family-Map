package model;

import java.util.Objects;

/**
 * Authtoken class. Stores data from the database into Authtoken objects
 */
public class AuthToken {

    /**
     * Authtoken associated with a user
     */
    private final String authtoken;
    /**
     * Username with which the authtoken is associated
     */
    private final String userName;

    /**
     * Contstuctor of AuthToken
     * @param authtoken Authtoken associated with User
     * @param userName User's username
     */
    public AuthToken(String authtoken, String userName) {
        this.authtoken = authtoken;
        this.userName = userName;
    }

    public String getAuthToken() {
        return authtoken;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(authtoken, authToken.authtoken) && Objects.equals(userName, authToken.userName);
    }
}
