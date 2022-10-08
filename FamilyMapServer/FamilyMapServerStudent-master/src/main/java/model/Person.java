package model;

import java.util.Objects;

/**
 * Person class. Stores data from the database into person objects
 */
public class Person {

    /**
     * Unique identifier for this person
     */
    private final String personID;
    /**
     * Username of user to which this person belongs
     */
    private final String associatedUsername;
    /**
     * Person’s first name
     */
    private String firstName;
    /**
     * Person’s last name
     */
    private String lastName;
    /**
     * Person’s gender either "f" or "m"
     */
    private final String gender;
    /**
     * Person ID of person’s father, may be null
     */
    private final String fatherID;
    /**
     * Person ID of person’s mother, may be null
     */
    private final String motherID;
    /**
     * Person ID of person’s spouse, may be null
     */
    private String spouseID;

    /**
     * Constructor of Person object
     * @param personID Unique identifier for this person
     * @param associatedUsername Username of user to which this person belongs
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender either "f" or "m"
     * @param fatherID Person ID of person’s father, may be null
     * @param motherID Person ID of person’s mother, may be null
     * @param spouseID Person ID of person’s spouse, may be null
     */
    public Person(String personID, String associatedUsername, String firstName,
                  String lastName, String gender, String fatherID,
                  String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() { return personID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getGender() { return gender; }

    public String getFatherID() { return fatherID; }

    public String getMotherID() { return motherID; }

    public String getSpouseID() { return spouseID; }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) && Objects.equals(associatedUsername, person.associatedUsername)
                && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName)
                && Objects.equals(gender, person.gender) && Objects.equals(fatherID, person.fatherID)
                && Objects.equals(motherID, person.motherID) && Objects.equals(spouseID, person.spouseID);
    }
}
