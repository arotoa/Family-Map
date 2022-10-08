package cs240.familymapclient.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs240.familymapclient.mainactivity.Model.Settings;
import model.Person;
import model.Event;

public class DataCache {

    private static DataCache instance;
    private Map<String, Person> people;
    private Map<String, Person> filteredPeople;
    private Map<String, Event> allEvents;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private List<String> mothersSide;
    private List<String> fathersSide;
    private String authToken;
    private String userName;
    private String personID;
    private Settings settings;
    private boolean settingsChanged;

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache() {
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        filteredPeople = new HashMap<>();
        mothersSide = new ArrayList<>();
        fathersSide = new ArrayList<>();
        settingsChanged = false;
    }

    public void setPeople(Person[] personArray) {
        for (Person person : personArray) {
            people.put(person.getPersonID(), person);
            filteredPeople.put(person.getPersonID(), person);
        }
    }

    public void setEvents(Event[] eventArray) {
        for (Event event : eventArray) {
            //put event into event list
            events.put(event.getEventID(), event);
            //if person doesn't exist in personEvents, add them to it
            if (!personEvents.containsKey(event.getPersonID())) {
                personEvents.put(event.getPersonID(), new ArrayList<>());
                personEvents.get(event.getPersonID()).add(event);
            }
            else {
                chronologicallyPlaceEvent(event);
            }
        }
        allEvents = events;
    }

    public void chronologicallyPlaceEvent(Event event) {
        Event newEvent;
        for (int i = 0; i < personEvents.get(event.getPersonID()).size(); i++) {
            newEvent = personEvents.get(event.getPersonID()).get(i);
            //check if event is newer
            if (newEvent.getYear() > event.getYear()) {
                personEvents.get(event.getPersonID()).add(i, event);
                break;
            }
            //check is event is the same year
            if (newEvent.getYear() == event.getYear()) {
                //event comes first alphabetically
                if (newEvent.getEventType().compareToIgnoreCase(event.getEventType()) >= 0) {
                    personEvents.get(event.getPersonID()).add(i, event);
                }
                else {
                    personEvents.get(event.getPersonID()).add(event);
                }
                break;
            }
            //oldest event
            if (personEvents.get(event.getPersonID()).size() == (i + 1)) {
                personEvents.get(event.getPersonID()).add(event);
                break;
            }
        }
    }

    public void filterEvents() {
        //if sides of family have not been split yet
        if (fathersSide.size() == 0) {
            filterSidesOfFamily();
        }

        //get ready for filtering
        Map<String, Event> filteredEvents = new HashMap<>();
        filteredPeople.clear();
        boolean addEvent;
        for (Map.Entry<String, Event> e : allEvents.entrySet()) {
            //reset addEvent to true
            addEvent = true;
            //check if part of fathers side
            if (!getFathersSide() && fathersSide.contains(e.getValue().getPersonID())) {
                addEvent = false;
            }
            //check if part of mothers side
            if (!getMothersSide() && mothersSide.contains(e.getValue().getPersonID())) {
                addEvent = false;
            }
            //check if a female event
            if (!getFemaleEvents() 
                    && getPerson(e.getValue().getPersonID()).getGender().toLowerCase().equals("f")) {
                addEvent = false;
            }
            //check if male event
            if (!getMaleEvents() 
                    && getPerson(e.getValue().getPersonID()).getGender().toLowerCase().equals("m")) {
                addEvent = false;
            }
            //add event to filtered events if applicable
            if (addEvent) {
                filteredEvents.put(e.getValue().getEventID(), e.getValue());
                //add who event belongs to in filtered people
                if (!filteredPeople.containsKey(e.getValue().getPersonID())) {
                    filteredPeople.put(e.getValue().getPersonID(), 
                                       getPerson(e.getValue().getPersonID()));
                }
            }
        }
        events = filteredEvents;
    }
    
    public void filterSidesOfFamily () {
        fathersSide.add(getPerson(personID).getFatherID());
        mothersSide.add(getPerson(personID).getMotherID());
        getFathersAncestors(getPerson(personID).getFatherID());
        getMothersAncestors(getPerson(personID).getMotherID());
    }
    
    private void getMothersAncestors(String ancestorID) {
        //if father of ancestor exists
        if (getPerson(ancestorID).getFatherID() != null) {
            getMothersAncestors(getPerson(ancestorID).getFatherID());
            mothersSide.add(getPerson(ancestorID).getFatherID());
        }
        //if mother of ancestor exists
        if (getPerson(ancestorID).getMotherID() != null) {
            getMothersAncestors(getPerson(ancestorID).getMotherID());
            mothersSide.add(getPerson(ancestorID).getMotherID());
        }
    }

    private void getFathersAncestors(String ancestorID) {
        //if father of ancestor exists
        if (getPerson(ancestorID).getFatherID() != null) {
            getFathersAncestors(getPerson(ancestorID).getFatherID());
            fathersSide.add(getPerson(ancestorID).getFatherID());
        }
        //if mother of ancestor exists
        if (getPerson(ancestorID).getMotherID() != null) {
            getFathersAncestors(getPerson(ancestorID).getMotherID());
            fathersSide.add(getPerson(ancestorID).getMotherID());
        }
    }

    public String getRelationship(Person person, Person wantedRelationshipPerson) {
        String gender = person.getGender();
        try {
            if (person.getMotherID().equals(wantedRelationshipPerson.getPersonID())) {
                return "Mother";
            } else if (person.getFatherID().equals(wantedRelationshipPerson.getPersonID())) {
                return "Father";
            } else if (person.getSpouseID().equals(wantedRelationshipPerson.getPersonID())) {
                return "Spouse";
            } else if (wantedRelationshipPerson.getFatherID().equals(person.getPersonID())) {
                if (gender.equals("m")) {
                    return "Son";
                } else {
                    return "Daughter";
                }
            } else if (wantedRelationshipPerson.getMotherID().equals(person.getPersonID())) {
                if (gender.equals("m")) {
                    return "Son";
                } else {
                    return "Daughter";
                }
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    //check if event or person is in the filtered events
    public boolean containsEvent(Event event) {
        return events.containsKey(event.getEventID());
    }
    public boolean containsPerson(String idOfPerson) {
        return filteredPeople.containsKey(idOfPerson);
    }

    public List<Event> searchEvents(String searchText) {
        List<Event> newEvents = new ArrayList<>();
        for (Map.Entry<String, Event> e : events.entrySet()) {
            if (e.getValue().getCountry().toLowerCase().contains(searchText.toLowerCase())
                    || e.getValue().getCity().toLowerCase().contains(searchText.toLowerCase())
                    || e.getValue().getEventType().toLowerCase().contains(searchText.toLowerCase())
                    || Integer.toString(e.getValue().getYear()).contains(searchText)) {
                newEvents.add(e.getValue());
            }
        }
        return newEvents;
    }

    public List<Person> searchPeople(String searchText) {
        List<Person> newPeople = new ArrayList<>();
        for (Map.Entry<String, Person> p : people.entrySet()) {
            if (p.getValue().getFirstName().toLowerCase().contains(searchText.toLowerCase())
                    || p.getValue().getLastName().toLowerCase().contains(searchText.toLowerCase())) {
                newPeople.add(p.getValue());
            }
        }
        return newPeople;
    }

    public void clearData() {
        instance = new DataCache();
    }

    //getters
    public Map<String, Event> getEvents() {
        return events;
    }
    public List<Event> getEventsOfPerson(String personID) {
        return personEvents.get(personID);
    }
    public Person getPerson(String personID) {
        return people.get(personID);
    }
    public String getPersonID() {
        return personID;
    }
    public boolean getLifeStory() {
        return settings.isLifeStoryLines();
    }
    public boolean getFamilyTree() {
        return settings.isFamilyTreeLines();
    }
    public boolean getSpouseLines() {
        return settings.isSpouseLines();
    }
    public boolean getFathersSide() {
        return settings.isFathersSide();
    }
    public boolean getMothersSide() {
        return settings.isMothersSide();
    }
    public boolean getMaleEvents() {
        return settings.isMaleEvents();
    }
    public boolean getFemaleEvents() {
        return settings.isFemaleEvents();
    }
    public Map<String, Person> getFilteredPeople() {
        return filteredPeople;
    }
    public boolean isSettingsChanged() {
        return settingsChanged;
    }

    //setters
    public void setSettings() {
        settings = new Settings();
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public void setLifeStory(boolean isOn) {
        settings.setLifeStoryLines(isOn);
    }
    public void setFamilyTree(boolean isOn) {
        settings.setFamilyTreeLines(isOn);
    }
    public void setSpouseLines(boolean isOn) {
        settings.setSpouseLines(isOn);
    }
    public void setFathersSide(boolean isOn) {
        settings.setFathersSide(isOn);
    }
    public void setMothersSide(boolean isOn) {
        settings.setMothersSide(isOn);
    }
    public void setMaleEvents(boolean isOn) {
        settings.setMaleEvents(isOn);
    }
    public void setFemaleEvents(boolean isOn) {
        settings.setFemaleEvents(isOn);
    }
    public void setSettingsChanged(boolean isChanged) {
        settingsChanged = isChanged;
    }
}
