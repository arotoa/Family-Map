package cs240.familymapclient.mainactivity.Model;


import model.Person;

public class RelatedPerson {
    private Person person;
    private String relation;

    public RelatedPerson(Person person, String relation) {
        this.person = person;
        this.relation = relation;
    }

    public Person getPerson() {
        return person;
    }

    public String getRelation() {
        return relation;
    }
}
