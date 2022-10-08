package dao;

import model.Event;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Event data access class. Interacts with the database.
 */
public class EventDao {

    /**
     * Connection to database
     */
    private final Connection conn;

    /**
     * Constructor of Event Dao
     * @param conn connection to database
     */
    public EventDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert an event into the database
     * @param event Event object
     */
    public void insertEvent (Event event) throws DataAccessException{
        String sql = "INSERT INTO Event (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * Load in new events into the event table in the database
     * @param events New events
     */
    public void loadEvents(Event[] events) throws DataAccessException {
        for (Event e : events) {
            insertEvent(e);
        }
    }

    /**
     * Get a specific event that occurred in a person's life
     * @param eventID ID associated with the event
     * @return Event object
     */
    public Event getEvent(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"), rs.getString("PersonID"),
                                  rs.getFloat("Latitude"), rs.getFloat("Longitude"), rs.getString("Country"),
                                  rs.getString("City"), rs.getString("EventType"), rs.getInt("Year"));
                return event;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }


        /**
         * Get all events of a user with ID.
         * @param username User's username
         * @return Array of all events
         */
    public Event[] getAllEventsOfUser (String username) throws DataAccessException {
        Set<Event> events = new HashSet<>();
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"), rs.getString("PersonID"),
                                  rs.getFloat("Latitude"), rs.getFloat("Longitude"), rs.getString("Country"),
                                  rs.getString("City"), rs.getString("EventType"), rs.getInt("Year"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
        return events.toArray(Event[]::new);
    }

    /**
     * Clear the event table in the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Event";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    public void clearAssociatedData(String username) throws DataAccessException {
        //clear all events associated with a user
        String sql = "DELETE FROM Event WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing events associated with username");
        }
    }
}
