package model;

import java.util.Objects;

/**
 * Event class. Stores data from the database into Event objects
 */
public class Event {

    /**
     * Event's ID
     */
    private String eventID;
    /**
     * Username of user to which this event belongs
     */
    private final String associatedUsername;
    /**
     * ID of person to which this event belongs
     */
    private String personID;
    /**
     * Latitude of event’s location
     */
    private final float latitude;
    /**
     * Longitude of event’s location
     */
    private final float longitude;
    /**
     * Country in which event occurred
     */
    private final String country;
    /**
     * City in which event occurred
     */
    private final String city;
    /**
     * Type of event
     */
    private final String eventType;
    /**
     * Year in which event occurred
     */
    private final int year;

    /**
     * Constructor of an Event object
     * @param eventID Unique identifier for this event
     * @param associatedUsername Username of user to which this event belongs
     * @param personID ID of person to which this event belongs
     * @param latitude Latitude of event’s location
     * @param longitude Longitude of event’s location
     * @param country Country in which event occurred
     * @param city City in which event occurred
     * @param eventType Type of event
     * @param year Year in which event occurred
     */
    public Event(String eventID, String associatedUsername, String personID,
                 float latitude, float longitude, String country, String city,
                 String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public void setEventID(String eventID) { this.eventID = eventID; }

    public void setPersonID(String personID) { this.personID = personID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) && Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) && Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) && Objects.equals(year, event.year);
    }

}
