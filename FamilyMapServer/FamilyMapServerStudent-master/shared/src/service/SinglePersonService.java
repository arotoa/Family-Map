package service;

import dao.*;
import model.AuthToken;
import model.Person;
import request.SinglePersonRequest;
import result.SinglePersonResult;

import java.sql.Connection;

/**
 * Receives and processes a person ID request
 */
public class SinglePersonService {

    /**
     * Find single person object with ID. The current user is determind by authtoken.
     * @param r PersonIDRequest object containing authtoken and personID
     * @return PersonIDResult object containing all data for person
     */
    public SinglePersonResult getPerson(SinglePersonRequest r) throws DataAccessException {
        //open database connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //find auth token in database
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        AuthToken authToken = authTokenDao.findAuthToken(r.getAuthToken());

        SinglePersonResult result;
        //auth token exists
        if (authToken != null) {
            //find person
            PersonDao personDao = new PersonDao(conn);
            Person person = personDao.getPerson(r.getPersonID());
            if (person == null) {
                return new SinglePersonResult(false, "Error: Person does not exist in the database");
            }

            //check if person is associated with user
            String username = authToken.getUserName();
            if (person.getAssociatedUsername().equals(username)) {
                result = new SinglePersonResult(person.getPersonID(), person.getAssociatedUsername(), person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID(), true);
            }
            else {
                result = new SinglePersonResult(false, "Error: Requested person does not belong to this user");
            }
        }
        //auth token does not exist
        else {
            result = new SinglePersonResult(false, "Error: Invalid auth token");
        }

        //close connection
        db.closeConnection(true);
        return result;
    }
}
