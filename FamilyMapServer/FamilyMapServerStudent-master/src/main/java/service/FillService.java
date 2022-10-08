package service;

import dao.*;
import model.User;
import request.FillRequest;
import result.FillResult;

import java.io.FileNotFoundException;
import java.sql.Connection;

import static java.lang.Integer.parseInt;

/**
 * Receives and processes a fill request
 */
public class FillService {

    /**
     * Populates database with generated data for user and family members.
     * User needs to already be registered.
     * Generations can be specified.
     * @param r Fill request object containing needed data
     * @return FillResult object
     */
    public FillResult fill(FillRequest r) throws DataAccessException, FileNotFoundException {
        //test if valid generations parameter
        if (!r.getNumOfGenerations().matches("^[0-9]*$")){
            return new FillResult("Error: Invalid input for generations", false);
        }

        //open connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //find username in database
        UserDao userDao = new UserDao(conn);
        User user = userDao.findUserByID(userDao.getID(r.getUsername()));
        if (user == null) {
            db.closeConnection(true);
            return new FillResult("Error: Username not found", false);
        }

        //clear associated data with username
        EventDao eventDao = new EventDao(conn);
        eventDao.clearAssociatedData(r.getUsername());
        PersonDao personDao = new PersonDao(conn);
        personDao.clearAssociatedData(r.getUsername());

        //generate new family tree
        TreeGenerator newTree = new TreeGenerator(user.getFirstName(), user.getLastName());
        int generations = parseInt(r.getNumOfGenerations());
        newTree.generateTree(r.getUsername(), user.getGender(), generations, conn);

        //find how many people, and events added
        int personsAdded = personDao.getAllPersons(r.getUsername()).length;
        int eventsAdded = eventDao.getAllEventsOfUser(r.getUsername()).length;

        //create result object
        String message = "Successfully added " + personsAdded + " persons and " + eventsAdded + " events to the database.";
        FillResult fillResult;
        fillResult = new FillResult(message, true);

        //close connection
        db.closeConnection(true);
        return fillResult;
    }
}
