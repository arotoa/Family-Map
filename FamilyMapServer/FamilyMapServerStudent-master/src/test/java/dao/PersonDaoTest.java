package dao;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person person;
    private PersonDao dao;
    private static Person[] persons;

    @BeforeAll
    static void addPersons() {
        Person newPerson;
        persons = new Person[7];
        for (int i = 0; i < 7; i++) {
            newPerson = new Person(UUID.randomUUID().toString(), "biker", "Sarah", "Bolton", "f",
                    null, null, null);
            persons[i] = newPerson;
        }
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        person = new Person("person12345", "biker", "John",
                "doe", "M", null, null, null);
        Connection conn = db.getConnection();
        dao = new PersonDao(conn);
        dao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        assertNull(dao.getPerson(person.getPersonID()));
        dao.insertPerson(person);
        assertNotNull(dao.getPerson(person.getPersonID()));
    }

    @Test
    public void insertFail() throws DataAccessException {
        dao.insertPerson(person);
        assertThrows(DataAccessException.class, () -> dao.insertPerson(person));
    }

    @Test
    public void findPass() throws DataAccessException {
        dao.insertPerson(person);
        assertEquals(person, dao.getPerson(person.getPersonID()));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.getPerson(person.getPersonID()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        dao.insertPerson(person);
        dao.clear();
        assertNull(dao.getPerson(person.getPersonID()));
    }

    @Test
    public void setSpouseIDPass() throws DataAccessException {
        dao.insertPerson(person);
        assertNull(dao.getPerson("person12345").getSpouseID());
        dao.setSpouseID("person12345", "My_Beautiful_Spouse");
        assertEquals("My_Beautiful_Spouse", dao.getPerson("person12345").getSpouseID());
    }

    @Test
    public void loadPass() throws DataAccessException {
        assertEquals(0, dao.getAllPersons("biker").length);
        dao.loadPersons(persons);
        assertNotNull(dao.getAllPersons("biker")[1]);
    }

    @Test
    public void loadFail() throws DataAccessException {
        dao.insertPerson(person);
        Person[] allPersons = { person };
        assertThrows(DataAccessException.class, () -> dao.loadPersons(allPersons));
    }

    @Test
    public void getAllPersonsPass() throws DataAccessException {
        assertEquals(0, dao.getAllPersons("biker").length);
        dao.loadPersons(persons);
        assertEquals(7, dao.getAllPersons("biker").length);
    }

    @Test
    public void getAllPersonsFail() throws DataAccessException {
        dao.loadPersons(persons);
        assertEquals(0, dao.getAllPersons("WRONG_USERNAME").length);
    }

    @Test
    public void clearAssociatedDataPass() throws DataAccessException {
        dao.loadPersons(persons);
        assertEquals(7, dao.getAllPersons("biker").length);
        dao.clearAssociatedData("biker");
        assertEquals(0, dao.getAllPersons("biker").length);
    }

    @Test
    public void clearAssociatedDataFail() throws DataAccessException {
        dao.loadPersons(persons);
        assertEquals(0, dao.getAllPersons("WRONG").length);
    }
}
