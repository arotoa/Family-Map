package dao;

import model.AuthToken;
import model.User;

import java.sql.*;

/**
 * User data access class. Interacts with the database.
 */
public class UserDao {

    /**
     * Connection to database
     */
    private final Connection conn;

    /**
     * Constructor of User Dao
     * @param conn connection to database
     */
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert a new user into the database
     * @param user New user
     */
    public void insertUser(User user) throws DataAccessException{
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }

    }

    /**
     * Login/validate the user, and create an authtoken
     * @param username Unique username for user
     * @param password User's password
     * @return New authtoken object
     */
    public AuthToken validate(String username, String password) throws DataAccessException {
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    UniqueIDGenerator newNumber = new UniqueIDGenerator(conn);
                    String id = newNumber.generateID();
                    return new AuthToken(id, username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
        return null;
    }

    /**
     * Find a user in the database with ID
     * @param id ID associated with user
     * @return User object
     */
    public User findUserByID (String id) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"),
                                rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                                rs.getString("personID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
    }

    public String getID (String username) throws DataAccessException {
        //get personID associated to user
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("personID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
        return null;
    }

    /**
     * Load new users into the database
     * @param users New persons
     */
    public void loadUsers(User[] users) throws DataAccessException {
        for (User u : users) {
            insertUser(u);
        }
    }

    /**
     * Clear the user table in the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

}
