package service;

import dao.*;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Locale;

/**
 * Receives and processes a register request
 */
public class RegisterService {

    /**
     * Creates a new user, generates 4 generations of ancestor data for the new user,
     * logs the user in
     * @param r Register request object from handler
     * @return  Register result object
     */
    public RegisterResult register(RegisterRequest r) throws DataAccessException, FileNotFoundException {
        //check if all values are populated
        if (r.getEmail() == null || r.getGender() == null || r.getUsername() == null || r.getFirstName() == null
                || r.getLastName() == null || r.getPassword() == null) {
            return new RegisterResult(false, "Error: Missing value");
        }

        //open connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //check if username is already taken
        RegisterResult result;
        UserDao dao = new UserDao(conn);
        if (dao.getID(r.getUsername()) != null) {
            result = new RegisterResult(false, "Error: Username already taken");
            db.closeConnection(true);
        }
        else {
            //Check input is correct
            if (!r.getGender().equalsIgnoreCase("m") && !r.getGender().equalsIgnoreCase("f")) {
                db.closeConnection(true);
                return new RegisterResult(false, "Error: Invalid input for gender");
            }
            if (!r.getEmail().matches("^(.+)@(.+)$")) {
                db.closeConnection(true);
                return new RegisterResult(false, "Error: Invalid email input");
            }

            //generate new family tree with 4 generations
            TreeGenerator newTree = new TreeGenerator(r.getFirstName(), r.getLastName());
            String personID = newTree.generateTree(r.getUsername(), r.getGender(), 4, conn).getPersonID();

            //insert user into database
            User user = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(),
                                 r.getLastName(), r.getGender().toLowerCase(Locale.ROOT), personID);
            dao.insertUser(user);

            //close connection
            db.closeConnection(true);

            //log the new user in
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest(r.getUsername(), r.getPassword());
            LoginResult loginResult = loginService.login(loginRequest);

            //send result object
            if (loginResult.isSuccess()) {
                result = new RegisterResult(loginResult.getAuthtoken(), r.getUsername(), personID, true);
            }
            else {
                result = new RegisterResult(false, "Error while logging in");
            }
        }
        return result;
    }
}
