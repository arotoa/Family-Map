package cs240.familymapclient.mainactivity;

import org.junit.Test;

import static org.junit.Assert.*;

import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventsResult;
import result.AllPersonsResult;
import result.LoginResult;
import result.RegisterResult;

public class ServerProxyTest {

    private ServerProxy proxy;

    @Test
    public void registerPass() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        RegisterRequest request = new RegisterRequest("Username", "password", "email@gmail.com", "user", "name", "m");
        RegisterResult result = proxy.register(request);
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
        assertEquals("Username", result.getUsername());
        assertTrue(result.isSuccess());
    }

    @Test
    public void registerFail() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        RegisterRequest request = new RegisterRequest("user", "pass", "notanemail", "first", "last", "f");
        RegisterResult result = proxy.register(request);
        assertFalse(result.isSuccess());
    }

    @Test
    public void loginPass() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        RegisterRequest request = new RegisterRequest("Username", "password", "email@gmail.com", "user", "name", "m");
        String personID = proxy.register(request).getPersonID();
        LoginRequest loginRequest = new LoginRequest("Username", "password");
        LoginResult result = proxy.login(loginRequest);
        assertNotNull(result);
        assertEquals(personID, result.getPersonID());
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
        assertEquals("Username", result.getUsername());
    }

    @Test
    public void loginFail() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        LoginRequest loginRequest = new LoginRequest("Random", "pas");
        LoginResult result = proxy.login(loginRequest);
        assertFalse(result.isSuccess());
    }

    @Test
    public void retrievePeoplePass() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        RegisterRequest request = new RegisterRequest("Username", "password", "email@gmail.com", "user", "name", "m");
        RegisterResult result = proxy.register(request);
        AllPersonsResult allPeopleResult = proxy.getPersons(result.getAuthtoken());
        assertTrue(allPeopleResult.isSuccess());
        Person[] allPeople = allPeopleResult.getData();
        boolean isFound = false;
        for (Person p : allPeople) {
            if (p.getPersonID().equals(result.getPersonID())) {
                isFound = true;
                break;
            }
        }
        assertTrue(isFound);
    }

    @Test
    public void retrievePeopleFail() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        AllPersonsResult allPeopleResult = proxy.getPersons("notAnAuthtoken");
        assertFalse(allPeopleResult.isSuccess());
        assertNull(allPeopleResult.getData());
    }

    @Test
    public void retrieveEventsPass() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        RegisterRequest request = new RegisterRequest("Username", "password", "email@gmail.com", "user", "name", "m");
        RegisterResult result = proxy.register(request);
        AllEventsResult allEventsResult = proxy.getEvents(result.getAuthtoken());
        assertTrue(allEventsResult.isSuccess());
        assertEquals("Username", allEventsResult.getData()[0].getAssociatedUsername());
    }

    @Test
    public void retrieveEventsFail() {
        proxy = new ServerProxy("192.168.0.177", "8080");
        proxy.clear();
        AllEventsResult allEventsResult = proxy.getEvents("notAnAuthToken");
        assertFalse(allEventsResult.isSuccess());
        assertNull(allEventsResult.getData());

    }
}