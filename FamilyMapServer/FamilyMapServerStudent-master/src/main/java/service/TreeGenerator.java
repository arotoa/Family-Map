package service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import dao.*;
import model.*;

import java.io.*;
import java.sql.Connection;
import java.util.Random;

public class TreeGenerator {

    private String associatedUsername;
    private UniqueIDGenerator newID;
    private PersonDao personDao;
    private EventDao eventDao;
    private int finalGenerations;
    private Location[] locations;
    private String[] maleNames;
    private String[] femaleNames;
    private String[] surNames;
    private final String userFirstName;
    private final String userLastName;
    private int lastYear;

    public TreeGenerator(String userFirstName, String userLastName) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public Person generateTree(String associatedUsername, String gender, int generations, Connection conn)
                                throws DataAccessException, FileNotFoundException {
        //initialize dao objects
        newID = new UniqueIDGenerator(conn);
        personDao = new PersonDao(conn);
        eventDao = new EventDao(conn);

        //set variables
        this.associatedUsername = associatedUsername;
        finalGenerations = generations + 1;
        //set final year of approximate birth
        lastYear = 2000 - (generations * 40);

        //generate all names, and locations
        generateData();

        //recursively generate persons
        return generatePerson(gender, generations + 1);
    }

    private Person generatePerson(String gender, int generations) throws DataAccessException {
        Person mother = null;
        Person father = null;
        Person person;

        if (generations > 1) {
            //generate mother and father
            mother = generatePerson("f", generations - 1);
            father = generatePerson("m", generations - 1);

            //add marriage event
            addMarriage(mother, father, generations);
        }

        //set names for person
        String lastName = getRandSurName();
        String firstName;
        if (gender.equals("m")) {
            firstName = getRandMaleName();
        }
        else {
            firstName = getRandFemaleName();
        }

        //create new persons
        if (generations > 1) {
            //has father and mother
            person = new Person(newID.generateID(), associatedUsername, firstName, lastName, gender,
                    father.getPersonID(), mother.getPersonID(), null);
        }
        else {
            //does not have father and mother
            person = new Person(newID.generateID(), associatedUsername, firstName, lastName, gender,
                    null, null, null);
        }

        //dd events
        addBirth(person, generations);
        if (finalGenerations != generations) {
            addDeath(person, generations);
        }
        else {
            //set user first and last name
            person.setFirstName(userFirstName);
            person.setLastName(userLastName);
        }

        //insert person into database
        personDao.insertPerson(person);
        return person;
    }

    private void generateData() throws FileNotFoundException {
        Gson gson = new Gson();

        //read male name json file
        JsonReader jsonReader = new JsonReader(new FileReader("json/mnames.json"));
        MaleNames male = gson.fromJson(jsonReader, MaleNames.class);
        maleNames = male.getData();

        //read female name json file
        jsonReader = new JsonReader(new FileReader("json/fnames.json"));
        FemaleNames female = gson.fromJson(jsonReader, FemaleNames.class);
        femaleNames = female.getData();

        //read last name json file
        jsonReader = new JsonReader(new FileReader("json/snames.json"));
        Surnames sur = gson.fromJson(jsonReader, Surnames.class);
        surNames = sur.getData();

        //read location json file
        jsonReader = new JsonReader(new FileReader("json/locations.json"));
        Locations allLocations = gson.fromJson(jsonReader, Locations.class);
        locations = allLocations.getData();
    }

    private void addMarriage (Person mother, Person father, int generation) throws DataAccessException {
        //get random location and year
        Location marriageLocation = getRandLocation();
        int marriageYear = lastYear + ((generation * 31) + getRandNum());

        //create marriage event
        Event marriage = new Event(newID.generateID(), associatedUsername, mother.getPersonID(),
                                   marriageLocation.getLatitude(), marriageLocation.getLongitude(),
                                   marriageLocation.getCountry(), marriageLocation.getCity(),
                          "Marriage", marriageYear);

        //set spouse ID on person
        personDao.setSpouseID(mother.getPersonID(), father.getPersonID());
        personDao.setSpouseID(father.getPersonID(), mother.getPersonID());

        //insert event into database for mother
        eventDao.insertEvent(marriage);

        //insert event into database for father
        marriage.setEventID(newID.generateID());
        marriage.setPersonID(father.getPersonID());
        eventDao.insertEvent(marriage);
    }
    
    private void addBirth (Person person, int generation) throws DataAccessException {
        //get random location and year
        Location birthLocation = getRandLocation();
        int birthYear = lastYear + (((generation * 31) + 8) + getRandNum());

        //add new birth event into database
        Event birth = new Event(newID.generateID(), associatedUsername, person.getPersonID(),
                                birthLocation.getLatitude(), birthLocation.getLongitude(), birthLocation.getCountry(),
                                birthLocation.getCity(), "Birth", birthYear);
        eventDao.insertEvent(birth);
    }

    private void addDeath (Person person,  int generation) throws DataAccessException {
        //get random location and year
        Location deathLocation = getRandLocation();
        int deathYear = lastYear + (((generation * 31) + 90) + getRandNum());

        //add new death event into database
        Event death = new Event(newID.generateID(), associatedUsername, person.getPersonID(),
                                deathLocation.getLatitude(), deathLocation.getLongitude(), deathLocation.getCountry(),
                                deathLocation.getCity(), "Death", deathYear);
        eventDao.insertEvent(death);
    }

    private Location getRandLocation() {
        int rnd = new Random().nextInt(locations.length);
        return locations[rnd];
    }

    private String getRandMaleName() {
        int rnd = new Random().nextInt(maleNames.length);
        return maleNames[rnd];
    }

    private String getRandFemaleName() {
        int rnd = new Random().nextInt(femaleNames.length);
        return femaleNames[rnd];
    }

    private String getRandSurName() {
        int rnd = new Random().nextInt(surNames.length);
        return surNames[rnd];
    }

    private int getRandNum() {
        return new Random().nextInt(7);
    }
}
