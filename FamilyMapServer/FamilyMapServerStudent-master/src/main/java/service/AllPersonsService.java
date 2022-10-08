package service;

import dao.*;
import model.AuthToken;
import result.AllPersonsResult;

import java.sql.Connection;

/**
 * Receives and processes a person request
 */
public class AllPersonsService {

    /**
     * Get all family members for current user, which is determined by authtoken
     * @param authToken Authtoken associated with user
     * @return PersonResult object with all data
     */
    public AllPersonsResult getPersons(String authToken) throws DataAccessException {
        //open database connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //find auth token in database
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        AuthToken newAuthToken = authTokenDao.findAuthToken(authToken);

        //create result depending on validation
        AllPersonsResult result;
        if (newAuthToken != null) {
            PersonDao personDao = new PersonDao(conn);
            result = new AllPersonsResult(personDao.getAllPersons(newAuthToken.getUserName()), true);
        }
        else {
            result = new AllPersonsResult(false, "Error: Invalid auth token");
        }

        //close connection
        db.closeConnection(true);
        return result;
    }
}
