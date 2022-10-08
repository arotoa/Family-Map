package dao;

import model.Person;

import java.sql.*;
import java.util.*;

/**
 * Person data access class. Interacts with the database.
 */
public class PersonDao {

    /**
     * Connection to database
     */
    private final Connection conn;

    /**
     * Constructor of Person Dao
     * @param conn connection to database
     */
    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert a new person into the database
     * @param person New person
     */
    public void insertPerson(Person person) throws DataAccessException {
        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
    }

    public void setSpouseID (String personID, String spouseID) throws DataAccessException {
        //set the id of a spouse
        String sql = "UPDATE Person SET spouseID = ? WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, spouseID);
            stmt.setString(2, personID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a spouseID into the database");
        }
    }

    /**
     * Get the person object from the database
     * @param personID ID of the person you want to get
     * @return Person object associated with the wanted person
     */
    public Person getPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                                    rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                                    rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * Get all family members of a user from the database
     * @param username User's username
     * @return Array of all persons
     */
    public Person[] getAllPersons(String username) throws DataAccessException {
        Set<Person> persons = new HashSet<>();
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                                    rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                                    rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
        Person[] personArray = new Person[persons.size()];
        int counter = 0;
        for (Person p : persons) {
            personArray[counter] = p;
            counter++;
        }
        return personArray;
    }

    /**
     * Load new persons into the database
     * @param persons New persons
     */
    public void loadPersons(Person[] persons) throws DataAccessException {
        for (Person p : persons) {
            insertPerson(p);
        }
    }

    /**
     * Clear the person table in the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Person";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

    public void clearAssociatedData(String username) throws DataAccessException {
        //clear all persons that are associated with a user
        String sql = "DELETE FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing events associated with username");
        }
    }
}
