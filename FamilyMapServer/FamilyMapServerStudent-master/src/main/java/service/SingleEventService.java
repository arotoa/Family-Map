package service;

import dao.*;
import model.AuthToken;
import model.Event;
import request.SingleEventRequest;
import result.SingleEventResult;

import java.sql.Connection;

/**
 * Receives and processes an event ID request
 */
public class SingleEventService {

    /**
     * Find single event object with ID. The current user is determind by authtoken.
     * @param r eventIDRequest object containing authtoken and eventID
     * @return eventIDResult object containing all data for event
     */
    public SingleEventResult getEvent(SingleEventRequest r) throws DataAccessException {
        //open connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //find auth token in database
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        AuthToken authToken = authTokenDao.findAuthToken(r.getAuthToken());

        SingleEventResult result;
        //auth token is valid
        if (authToken != null) {
            //find event
            EventDao eventDao = new EventDao(conn);
            Event event = eventDao.getEvent(r.getEventId());
            if (event == null) {
                return new SingleEventResult(false, "Error: Event id does not exist in the database");
            }

            //check if event is associated with user
            String userName = authToken.getUserName();
            if (event.getAssociatedUsername().equals(userName)) {
                result = new SingleEventResult(event.getEventID(), userName, event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear(), true);
            }
            else {
                result = new SingleEventResult(false, "Error: Requested event does not belong to this user");
            }
        }
        //auth is not valid
        else {
            result = new SingleEventResult(false, "Error: Invalid auth token");
        }

        //close connection
        db.closeConnection(true);
        return result;
    }
}
