package service;

import dao.*;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;

/**
 * Receives and processes a load request
 */
public class LoadService {

    /**
     * Clear all data from database, and loads new data into database
     * @param r LoadRequest object containing all data
     * @return LoadResult object
     */
    public LoadResult load(LoadRequest r) throws DataAccessException {
        //clear database
        ClearService clearService = new ClearService();
        clearService.clear();

        //open database connection
        Database db = new Database();
        Connection conn = db.getConnection();

        //load events, persons, and users into database
        try {
            UserDao userDao = new UserDao(conn);
            userDao.loadUsers(r.getUsers());
            PersonDao personDao = new PersonDao(conn);
            personDao.loadPersons(r.getPersons());
            EventDao eventDao = new EventDao(conn);
            eventDao.loadEvents(r.getEvents());
            db.closeConnection(true);
        }
        catch (Exception e) {
            db.closeConnection(false);
            return new LoadResult("Error: Invalid input", false);
        }

        //return result object
        String message = "Successfully added " + r.getUsers().length + " users, " + r.getPersons().length +
                         " persons, and " + r.getEvents().length + " events to the database.";
        return new LoadResult(message, true);
    }
}
