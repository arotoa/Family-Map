package dao;

import java.sql.*;
import java.util.UUID;

public class UniqueIDGenerator {

    private final Connection conn;
    private String id;
    ResultSet rs;

    public UniqueIDGenerator(Connection conn) { this.conn = conn; }

    public String generateID() throws DataAccessException {
        boolean isFound = false;
        do {
            //create random string
            id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);

            //check to see if string exists in all tables
            isFound = isFoundInTables();

        } while (isFound);
        return id;
    }

    private boolean isFoundInTables() throws DataAccessException {
        if (isFoundPersonTable()) { return true; }
        if (isFoundAuthTokenTable()) { return true; }
        return isFoundEventTable();
    }

    private boolean isFoundPersonTable() throws DataAccessException {
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered accessing the database");
        }
        return false;
    }

    private boolean isFoundAuthTokenTable() throws DataAccessException {
        String sql = "SELECT * FROM Authtoken WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered accessing the database");
        }
        return false;
    }

    private boolean isFoundEventTable() throws DataAccessException {
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered accessing the database");
        }
        return false;
    }
}
