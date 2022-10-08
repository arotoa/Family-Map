import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import dao.DataAccessException;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.FillResult;
import result.SinglePersonResult;
import service.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

public class ServicesTest {

    @BeforeEach
    public void setUp() throws FileNotFoundException, DataAccessException {
        load();
    }

    @AfterEach
    public void breakDown() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void AllEventsServicePass() throws DataAccessException {
        LoginService loginService = new LoginService();
        String authToken = loginService.login(new LoginRequest("sheila", "parker")).getAuthtoken();
        AllEventsService allEventsService = new AllEventsService();
        assertEquals(16, allEventsService.getEvents(authToken).getData().length);
    }

    @Test
    public void AllEventsServiceFail() throws DataAccessException {
        AllEventsService allEventsService = new AllEventsService();
        assertFalse(allEventsService.getEvents("WRONG").isSuccess());
    }

    @Test
    public void ALlPersonsServicePass() throws DataAccessException {
        LoginService loginService = new LoginService();
        String authToken = loginService.login(new LoginRequest("sheila", "parker")).getAuthtoken();
        AllPersonsService allPersonsService = new AllPersonsService();
        assertEquals(8, allPersonsService.getPersons(authToken).getData().length);
    }

    @Test
    public void AllPersonsServiceFail() throws DataAccessException {
        AllPersonsService allPersonsService = new AllPersonsService();
        assertFalse(allPersonsService.getPersons("WRONG").isSuccess());
    }

    @Test
    public void ClearServicePass() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
        AllPersonsService allPersonsService = new AllPersonsService();
        LoginService loginService = new LoginService();
        assertNull(allPersonsService.getPersons(loginService.login(new LoginRequest("username", "password")).getAuthtoken()).getData());
    }

    @Test
    public void FillServicePass() throws DataAccessException, FileNotFoundException {
        FillService fillService = new FillService();
        FillResult fillResult = fillService.fill(new FillRequest("sheila", "2"));
        assertEquals("Successfully added 7 persons and 19 events to the database.", fillResult.getMessage());

    }

    @Test
    public void FillServiceFail() throws FileNotFoundException, DataAccessException {
        FillService fillService = new FillService();
        assertFalse(fillService.fill(new FillRequest("wrong")).isSuccess());
    }

    @Test
    public void LoadServicePass() throws FileNotFoundException, DataAccessException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("json/LoadData.json"));
        LoadRequest request = gson.fromJson(jsonReader, LoadRequest.class);
        LoadService loadService = new LoadService();
        assertEquals("Successfully added 2 users, 11 persons, and 19 events to the database.", loadService.load(request).getMessage());
    }

    @Test
    public void LoadServiceFail() throws FileNotFoundException, DataAccessException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("json/bad.json"));
        LoadRequest request = gson.fromJson(jsonReader, LoadRequest.class);
        LoadService loadService = new LoadService();
        assertFalse(loadService.load(request).isSuccess());
    }

    @Test
    public void LoginServicePass() throws DataAccessException {
        LoginService loginService = new LoginService();
        assertTrue(loginService.login(new LoginRequest("sheila", "parker")).isSuccess());
    }

    @Test
    public void LoginServiceFail() throws DataAccessException {
        LoginService loginService = new LoginService();
        assertFalse(loginService.login(new LoginRequest("wrong", "password")).isSuccess());
    }

    @Test
    public void RegisterServicePass() throws FileNotFoundException, DataAccessException {
        RegisterService registerService = new RegisterService();
        assertTrue(registerService.register(new RegisterRequest("user", "pass", "email@em.co", "first", "last", "m")).isSuccess());
    }

    @Test
    public void RegisterServiceFail() throws FileNotFoundException, DataAccessException {
        RegisterService registerService = new RegisterService();
        assertFalse(registerService.register(new RegisterRequest("user", "pass", "email", "first", "last", "m")).isSuccess());
    }

    @Test
    public void SingleEventServicePass() throws DataAccessException {
        SingleEventService singleEventService = new SingleEventService();
        LoginService loginService = new LoginService();
        String authToken = loginService.login(new LoginRequest("sheila", "parker")).getAuthtoken();
        assertEquals("Ken_Rodham", singleEventService.getEvent(new SingleEventRequest("BYU_graduation", authToken)).getPersonID());
    }

    @Test
    public void SingleEventServiceFail() throws DataAccessException {
        SingleEventService singleEventService = new SingleEventService();
        LoginService loginService = new LoginService();
        String authToken = loginService.login(new LoginRequest("patrick", "spencer")).getAuthtoken();
        assertFalse(singleEventService.getEvent(new SingleEventRequest("BYU_graduation", authToken)).isSuccess());
    }

    @Test
    public void SinglePersonServicePass() throws DataAccessException {
        SinglePersonService singlePersonService = new SinglePersonService();
        LoginService loginService = new LoginService();
        String authToken = loginService.login(new LoginRequest("sheila", "parker")).getAuthtoken();
        assertEquals("f", singlePersonService.getPerson(new SinglePersonRequest("Mrs_Jones", authToken)).getGender());
    }

    @Test
    public void SinglePersonServiceFail() throws DataAccessException {
        SinglePersonService singlePersonService = new SinglePersonService();
        LoginService loginService = new LoginService();
        String authToken = loginService.login(new LoginRequest("patrick", "spencer")).getAuthtoken();
        assertFalse(singlePersonService.getPerson(new SinglePersonRequest("Mrs_Jones", authToken)).isSuccess());
    }

    private void load() throws DataAccessException, FileNotFoundException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("json/LoadData.json"));
        LoadRequest request = gson.fromJson(jsonReader, LoadRequest.class);
        LoadService loadService = new LoadService();
        loadService.load(request);
    }
}
