package service;

import dao.*;
import model.AuthToken;
import result.AllEventsResult;

import java.sql.Connection;

/**
 * Receives and processes an event request
 */
public class AllEventsService {

    /**
     * Get all events for all family members for current user, which is determined by authtoken
     * @param authToken Authtoken associated with user
     * @return EventResult object with all data
     */
    public AllEventsResult getEvents (String authToken) throws DataAccessException {
        //open database connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //find authToken in database
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        AuthToken newAuthToken = authTokenDao.findAuthToken(authToken);

        //send result back depending on validation
        AllEventsResult result;
        if (newAuthToken != null) {
            EventDao eventDao = new EventDao(conn);
            result = new AllEventsResult(eventDao.getAllEventsOfUser(newAuthToken.getUserName()), true);
        }
        else {
            result = new AllEventsResult(false, "Error: Invalid auth token");
        }

        //close connection
        db.closeConnection(true);
        return result;
    }
}
