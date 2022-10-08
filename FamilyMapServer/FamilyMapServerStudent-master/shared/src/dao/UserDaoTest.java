package dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private Database db;
    private User user;
    private UserDao dao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        user = new User("johndoe8439", "abcd123", "mynameisjohnathan@hotmail.com", "Johnathan", "Doe", "M", "12kgn93");
        Connection conn = db.getConnection();
        dao = new UserDao(conn);
        dao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        assertNull(dao.findUserByID(user.getPersonID()));
        dao.insertUser(user);
        assertNotNull(dao.findUserByID(user.getPersonID()));
    }

    @Test
    public void insertFail() throws DataAccessException {
        dao.insertUser(user);
        assertThrows(DataAccessException.class, () -> dao.insertUser(user));
    }

    @Test
    public void findPass() throws DataAccessException {
        dao.insertUser(user);
        assertEquals(user, dao.findUserByID(user.getPersonID()));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.findUserByID(user.getPersonID()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        dao.insertUser(user);
        dao.clear();
        assertNull(dao.findUserByID(user.getPersonID()));
    }

    @Test
    public void validatePass() throws DataAccessException {
        dao.insertUser(user);
        assertNotNull(dao.validate("johndoe8439", "abcd123"));

    }

    @Test
    public void validateFail() throws DataAccessException {
        dao.insertUser(user);
        assertNull(dao.validate("not_right", "junk"));
    }

    @Test
    public void getIDPass() throws DataAccessException {
        dao.insertUser(user);
        assertEquals("12kgn93", dao.getID("johndoe8439"));
    }

    @Test
    public void getIDFail() throws DataAccessException {
        dao.insertUser(user);
        assertNull(dao.getID("Amazing_username"));
    }

    @Test
    public void loadPass() throws DataAccessException {
        User user2 = new User("newUser", "pass", "myEmail", "new", "user", "m", "9876");
        User[] allUsers = { user, user2 };
        dao.loadUsers(allUsers);
        assertNotNull(dao.findUserByID("9876"));
        assertNotNull(dao.findUserByID("12kgn93"));
    }

    @Test
    public void loadFail() throws DataAccessException {
        dao.insertUser(user);
        User[] users ={ user };
        assertThrows(DataAccessException.class, () -> dao.loadUsers(users));
    }
}
