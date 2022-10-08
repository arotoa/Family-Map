package dao;

import model.AuthToken;

import java.sql.*;

/**
 * Authtoken data access class. Interacts with the database.
 */
public class AuthTokenDao {

    /**
     * Connection to database
     */
    private final Connection conn;

    /**
     * Constructor of Authtoken Dao
     * @param conn connection to database
     */
    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert an authtoken into the database
     * @param authToken
     */
    public void insertAuthToken(AuthToken authToken)  throws DataAccessException {
        String sql = "INSERT INTO Authtoken (authToken, username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUserName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an auth token into the database");
        }
    }

    /**
     * Get a user's username using an authtoken
     * @param authtoken User's authtoken
     * @return User's username
     */
    public String getUserName(String authtoken) throws DataAccessException {
        return findAuthToken(authtoken).getUserName();
    }

    /**
     * Clear authtoken table from SQL Database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Authtoken";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the authtoken table");
        }
    }


    public AuthToken findAuthToken (String token) throws DataAccessException {
        AuthToken authTokenObject;
        ResultSet rs;
        String sql = "SELECT * FROM Authtoken WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authTokenObject = new AuthToken(rs.getString("authToken"), rs.getString("username"));
                return authTokenObject;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an auth token in the database");
        }

    }
}
