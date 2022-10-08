package service;

import dao.*;
import model.AuthToken;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;

/**
 * Receives and processes a login request
 */
public class LoginService {

    /**
     * Logs the user in, and returns an authtoken
     * @param r LoginRequest object
     * @return Login result object containing all results
     */
    public LoginResult login(LoginRequest r) throws DataAccessException {
        //check if all values are populated
        if (r.getUsername() == null || r.getPassword() == null) {
            return new LoginResult(false, "Error: Missing value");
        }

        //open database connection
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();

        //validate user
        String userName = r.getUsername();
        UserDao dao = new UserDao(conn);
        AuthToken authToken = dao.validate(userName, r.getPassword());

        LoginResult result;
        //User exists, and no errors in password or username
        if (authToken != null) {
            result = new LoginResult(authToken.getAuthToken(), userName, dao.getID(userName), true);
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            authTokenDao.insertAuthToken(authToken);
        }
        //error in password or username
        else {
            result = new LoginResult(false, "Error: Wrong username or password");
        }

        //close connection
        db.closeConnection(true);
        return result;
    }

}
