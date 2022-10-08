package dao;

import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDaoTest {

    private Database db;
    private AuthToken authToken;
    private String authTokenString;
    private AuthTokenDao dao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        dao = new AuthTokenDao(db.getConnection());
        authTokenString = "my_Auth_Token";
        authToken = new AuthToken(authTokenString, "Squidward");
        dao.clear();
    }

    @AfterEach
    public void tearDown() { db.closeConnection(false); }

    @Test
    public void insertPass() throws DataAccessException {
        assertNull(dao.findAuthToken(authTokenString));
        dao.insertAuthToken(authToken);
        assertEquals("Squidward", dao.findAuthToken(authTokenString).getUserName());
    }

    @Test
    public void insertFail() throws DataAccessException {
        dao.insertAuthToken(authToken);
        assertThrows(DataAccessException.class, () -> dao.insertAuthToken(authToken));
    }

    @Test
    public void getUserNamePass() throws DataAccessException {
        dao.insertAuthToken(authToken);
        assertEquals("Squidward", dao.getUserName(authTokenString));
    }

    @Test
    public void getUserNameFail() {
        assertThrows(NullPointerException.class, () -> dao.getUserName(authTokenString));
    }

    @Test
    public void findPass() throws DataAccessException {
        dao.insertAuthToken(authToken);
        assertEquals(authToken, dao.findAuthToken(authTokenString));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.findAuthToken(authTokenString));
    }

    @Test
    public void clearPass() throws DataAccessException {
        dao.insertAuthToken(authToken);
        dao.clear();
        assertNull(dao.findAuthToken(authToken.getUserName()));
    }
}
