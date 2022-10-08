package cs240.familymapclient.mainactivity;


import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class ModelTest {
    private DataCache dataCache;
    private Event[] events;
    private Person[] people;
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;

    private void setUp() {
        Event event1 = new Event("eventOne", "user", "malePerson", 123, 456, "US", "Provo", "Birth", 1950);
        Event event2 = new Event("eventTwo", "user", "femalePerson", 789, 123, "Russia", "Salt Lake", "Death", 1980);
        Event event3 = new Event("eventThree", "user", "mom", 456, 789, "China", "Nephi", "Ran", 1949);
        Event event4 = new Event("eventFour", "user", "dad", 123, 456, "England", "American Fork", "Walked", 1953);
        Event event5 = new Event("eventFive", "user", "dad", 789, 123, "Egypt", "Draper", "Slept", 1951);
        Event event6 = new Event("eventSix", "user", "dad", 456, 789, "Mexico", "Sandy", "Talked", 1952);
        events = new Event[] {event1, event2, event3, event4, event5, event6};

        person1 = new Person("malePerson", "user", "Karl", "Name", "m", "dad", "mom", "personTwo");
        person2 = new Person("femalePerson", "user", "Jess", "Charles", "f", "dad", "mom", "personOne");
        person3 = new Person("dad", "user", "Jacob", "Spencer", "m", null, null, "mom");
        person4 = new Person("mom", "user", "Jennie", "Spencer", "f", null, null, "dad");
        people = new Person[] {person1, person2, person3, person4};

        dataCache = DataCache.getInstance();
        dataCache.clearData();
        dataCache.setEvents(events);
        dataCache.setPeople(people);
    }

    @Test
    public void calculateFamilyTest() {
        setUp();
        dataCache.setSettings();
        dataCache.setPersonID("femalePerson");
        String relationship = dataCache.getRelationship(person2, person3);
        assertEquals("Father", relationship);
        relationship = dataCache.getRelationship(person2, person4);
        assertEquals("Mother", relationship);
        relationship = dataCache.getRelationship(person4, person2);
        assertNull(relationship);
        relationship = dataCache.getRelationship(person2, person3);
        assertNotEquals("Mom", relationship);
    }

    @Test
    public void filterMaleEvents() {
        setUp();
        dataCache.setSettings();
        dataCache.setMaleEvents(false);
        dataCache.setPersonID("malePerson");
        dataCache.filterEvents();
        Map<String, Event> dataEvents = dataCache.getEvents();
        assertEquals(2, dataEvents.size());
        Event testEvent = dataEvents.get("eventOne");
        assertNull(testEvent);
        testEvent = dataEvents.get("eventTwo");
        assertEquals("Russia", testEvent.getCountry());
    }

    @Test
    public void filterFemaleEvents() {
        setUp();
        dataCache.setSettings();
        dataCache.setFemaleEvents(false);
        dataCache.setPersonID("femalePerson");
        dataCache.filterEvents();
        Map<String, Event> dataEvents = dataCache.getEvents();
        assertEquals(4, dataEvents.size());
        Event testEvent = dataEvents.get("eventTwo");
        assertNull(testEvent);
        testEvent = dataEvents.get("eventOne");
        assertEquals("US", testEvent.getCountry());
    }

    @Test
    public void filterMothersSide() {
        setUp();
        dataCache.setSettings();
        dataCache.setMothersSide(false);
        dataCache.setPersonID("femalePerson");
        dataCache.filterEvents();
        Map<String, Event> dataEvents = dataCache.getEvents();
        assertEquals(5, dataEvents.size());
        Event testEvent = dataEvents.get("eventThree");
        assertNull(testEvent);
        testEvent = dataEvents.get("eventFour");
        assertEquals("England", testEvent.getCountry());
    }

    @Test
    public void filterFathersSide() {
        setUp();
        dataCache.setSettings();
        dataCache.setFathersSide(false);
        dataCache.setPersonID("femalePerson");
        dataCache.filterEvents();
        Map<String, Event> dataEvents = dataCache.getEvents();
        assertEquals(3, dataEvents.size());
        Event testEvent = dataEvents.get("eventFour");
        assertNull(testEvent);
        testEvent = dataEvents.get("eventThree");
        assertEquals("China", testEvent.getCountry());
    }

    @Test
    public void sortEvents() {
        setUp();
        dataCache.setSettings();
        dataCache.setPersonID("femalePerson");
        List<Event> dadEvents = dataCache.getEventsOfPerson("dad");
        assertEquals(3, dadEvents.size());
        Event testEvent = dadEvents.get(0);
        assertEquals("Egypt", testEvent.getCountry());
        testEvent = dadEvents.get(1);
        assertEquals("Mexico", testEvent.getCountry());
        testEvent = dadEvents.get(2);
        assertEquals("England", testEvent.getCountry());
    }

    @Test
    public void searchForPeople() {
        setUp();
        dataCache.setSettings();
        dataCache.setPersonID("femalePerson");
        List<Person> searchedPeople = dataCache.searchPeople("J");
        assertEquals(3, searchedPeople.size());
        searchedPeople = dataCache.searchPeople("j");
        assertEquals(3, searchedPeople.size());
        searchedPeople = dataCache.searchPeople("karl");
        assertEquals(1, searchedPeople.size());
        searchedPeople = dataCache.searchPeople("wrongName");
        assertEquals(0, searchedPeople.size());
        searchedPeople = dataCache.searchPeople("SPenCeR");
        assertEquals(2, searchedPeople.size());
    }

    @Test
    public void searchForEvents() {
        setUp();
        dataCache.setSettings();
        dataCache.setPersonID("femalePerson");
        List<Event> searchedEvents = dataCache.searchEvents("1");
        assertEquals(6, searchedEvents.size());
        searchedEvents = dataCache.searchEvents("1980");
        Event testEvent = searchedEvents.get(0);
        assertEquals("Salt Lake", testEvent.getCity());
        searchedEvents = dataCache.searchEvents("uS");
        assertEquals(2, searchedEvents.size());
        searchedEvents = dataCache.searchEvents("slept");
        testEvent = searchedEvents.get(0);
        assertEquals("Draper", testEvent.getCity());
    }
}