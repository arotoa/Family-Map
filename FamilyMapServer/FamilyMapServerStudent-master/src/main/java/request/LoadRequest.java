package request;

import model.Event;
import model.Person;
import model.User;

/**
 * Creates an load request
 */
public class LoadRequest {

    /**
     * Array of users to insert into database
     */
    private final User [] users;
    /**
     * Array of persons to insert into database
     */
    private final Person [] persons;
    /**
     * Array of events to insert into database
     */
    private final Event [] events;

    /**
     * Constructor of LoadRequest object
     * @param users Array of users
     * @param persons Array of persons
     * @param events Array of events
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() { return users; }

    public Person[] getPersons() { return persons; }

    public Event[] getEvents() { return events; }
}
