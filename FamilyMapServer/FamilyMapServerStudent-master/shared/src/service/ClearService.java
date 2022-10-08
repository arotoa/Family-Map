package service;

import dao.*;
import result.ClearResult;

import java.sql.Connection;

/**
 * Receives and processes a clear request
 */
public class ClearService {

    /**
     * Delete all data from the database
     * @return Clear result object with message of what happened
     */
    public ClearResult clear() throws DataAccessException {
        //open database connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //create all daos
        UserDao userDao = new UserDao(conn);
        PersonDao personDao = new PersonDao(conn);
        EventDao eventDao = new EventDao(conn);
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);

        //clear all tables
        ClearResult result;
        try {
            userDao.clear();
            personDao.clear();
            eventDao.clear();
            authTokenDao.clear();

            db.closeConnection(true);
            result = new ClearResult("Clear succeeded", true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            result = new ClearResult("Error: Clear unsuccessful", false);
        }
        return result;
    }
}
