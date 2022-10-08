package dao;

import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventDaoTest {

    private Database db;
    private Event event;
    private EventDao dao;
    private static Event[] events;

    @BeforeAll
    static void addEvents() {
        Event newEvent;
        events = new Event[7];
        for (int i = 0; i < 7; i++) {
            newEvent = new Event(UUID.randomUUID().toString(), "user", "ID", 12, 78, "USA", "SLC", "Party", 2090);
            events[i] = newEvent;
        }
    }


    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        event = new Event("eventID", "username", "personID", 12, 56, "USA", "Provo", "College", 1990);
        dao = new EventDao(db.getConnection());
        dao.clear();
    }

    @AfterEach
    public void tearDown() { db.closeConnection(false); }

    @Test
    public void insertPass() throws DataAccessException {
        assertNull(dao.getEvent("eventID"));
        dao.insertEvent(event);
        assertNotNull(dao.getEvent("eventID"));
    }

    @Test
    public void insertFail() throws DataAccessException {
        dao.insertEvent(event);
        assertThrows(DataAccessException.class, () -> dao.insertEvent(event));
    }

    @Test
    public void loadPass() throws DataAccessException {
        Event event2 = new Event("event", "username", "IDOfPerson", 567, 89, "Argentina", "Buenos Aires", "Mish", 2020);
        Event[] allEvents = { event, event2 };
        dao.loadEvents(allEvents);
        assertNotNull(dao.getEvent("event"));
        assertNotNull(dao.getEvent("eventID"));
    }

    @Test
    public void loadFail() throws DataAccessException {
        dao.insertEvent(event);
        Event[] allEvents ={ event };
        assertThrows(DataAccessException.class, () -> dao.loadEvents(allEvents));
    }

    @Test
    public void findPass() throws DataAccessException {
        dao.insertEvent(event);
        assertEquals(12, dao.getEvent("eventID").getLatitude());
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.getEvent("eventID"));
    }

    @Test
    public void getAllEventsPass() throws DataAccessException {
        assertEquals(0, dao.getAllEventsOfUser("user").length);
        dao.loadEvents(events);
        assertEquals(7, dao.getAllEventsOfUser("user").length);
    }

    @Test
    public void getAllEventsFail() throws DataAccessException {
        dao.loadEvents(events);
        assertEquals(0, dao.getAllEventsOfUser("WRONG_USERNAME").length);
    }

    @Test
    public void clearPass() throws DataAccessException {
        dao.insertEvent(event);
        dao.clear();
        assertNull(dao.getEvent("eventID"));

    }

    @Test
    public void clearAssociatedDataPass() throws DataAccessException {
        dao.loadEvents(events);
        assertEquals(7, dao.getAllEventsOfUser("user").length);
        dao.clearAssociatedData("user");
        assertEquals(0, dao.getAllEventsOfUser("user").length);
    }

    @Test
    public void clearAssociatedDataFail() throws DataAccessException {
        dao.loadEvents(events);
        assertEquals(0, dao.getAllEventsOfUser("WRONG").length);
    }
}
