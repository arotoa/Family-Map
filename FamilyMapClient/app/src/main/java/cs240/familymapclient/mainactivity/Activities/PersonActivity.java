package cs240.familymapclient.mainactivity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.R;
import cs240.familymapclient.mainactivity.Model.RelatedPerson;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    public static final String TEXT_KEY = "PersonID";

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Iconify.with(new FontAwesomeModule());

        //find person for activity
        Intent intent = getIntent();
        DataCache dataCache = DataCache.getInstance();
        person = dataCache.getPerson(intent.getStringExtra(TEXT_KEY));

        //set name and gender fields in app
        TextView textView = findViewById(R.id.firstName);
        textView.setText(person.getFirstName());
        textView = findViewById(R.id.lastName);
        textView.setText(person.getLastName());
        textView = findViewById(R.id.gender);
        if (person.getGender().toLowerCase().equals("m")) {
            textView.setText("Male");
        }
        else {
            textView.setText("Female");
        }

        //get events that belong to person
        List<Event> relatedEvents = new ArrayList<>();
        if (dataCache.containsPerson(person.getPersonID())) {
            //check filters
            if (dataCache.getMaleEvents() && person.getGender().toLowerCase().equals("m")) {
                relatedEvents = dataCache.getEventsOfPerson(person.getPersonID());
            }
            if (dataCache.getFemaleEvents() && person.getGender().toLowerCase().equals("f")) {
                relatedEvents = dataCache.getEventsOfPerson(person.getPersonID());
            }
        }

        //create list of related people to person
        List<RelatedPerson> relatedPeople = new ArrayList<>();
        if (person.getSpouseID() != null) {
            relatedPeople.add(new RelatedPerson(dataCache.getPerson(person.getSpouseID()), "Spouse"));
        }
        if (person.getFatherID() != null) {
            relatedPeople.add(new RelatedPerson(dataCache.getPerson(person.getFatherID()), "Father"));
        }
        if (person.getMotherID() != null) {
            relatedPeople.add(new RelatedPerson(dataCache.getPerson(person.getMotherID()), "Mother"));
        }

        //check all people if son or daughter of person
        Map<String, Person> people = dataCache.getFilteredPeople();
        for (Map.Entry<String, Person> p : people.entrySet()) {
            if (p.getValue().getFatherID() != null) {
                if (person.getGender().toLowerCase().equals("m")) {
                    if (p.getValue().getFatherID().equals(person.getPersonID())) {
                        if (p.getValue().getGender().toLowerCase().equals("m")) {
                            relatedPeople.add(new RelatedPerson(p.getValue(), "Son"));
                        } else {
                            relatedPeople.add(new RelatedPerson(p.getValue(), "Daughter"));
                        }
                    }
                } else {
                    if (p.getValue().getMotherID().equals(person.getPersonID())) {
                        if (p.getValue().getGender().toLowerCase().equals("m")) {
                            relatedPeople.add(new RelatedPerson(p.getValue(), "Son"));
                        } else {
                            relatedPeople.add(new RelatedPerson(p.getValue(), "Daughter"));
                        }
                    }
                }
            }
        }

        //create expandable list
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(relatedEvents, relatedPeople));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Event> events;
        private final List<RelatedPerson> people;

        public ExpandableListAdapter(List<Event> events, List<RelatedPerson> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return events.size();
                case PERSON_GROUP_POSITION:
                    return people.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // Not used
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // Not used
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.event_group_title);
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.person_group_title);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            //show event and name details
            TextView textView = eventItemView.findViewById(R.id.event);
            String eventInfo = String.format("%s: %s, %s (%d)",
                    events.get(childPosition).getEventType(), events.get(childPosition).getCity(),
                    events.get(childPosition).getCountry(), events.get(childPosition).getYear());
            textView.setText(eventInfo);
            textView = eventItemView.findViewById(R.id.nameOfPerson);
            DataCache dataCache = DataCache.getInstance();
            Person newPerson = dataCache.getPerson(events.get(childPosition).getPersonID());
            textView.setText(String.format("%s %s", newPerson.getFirstName(), newPerson.getLastName()));

            //draw event icon
            Drawable eventIcon;
            eventIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(30);
            ImageView genderImageView = eventItemView.findViewById(R.id.eventIcon);
            genderImageView.setImageDrawable(eventIcon);

            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), EventActivity.class);
                    intent.putExtra(EventActivity.TEXT_KEY, events.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePersonView(View personItemView, final int childPosition) {
            //show person details
            TextView textView = personItemView.findViewById(R.id.personName);
            Person relatedPerson = people.get(childPosition).getPerson();
            String relation = people.get(childPosition).getRelation();
            String nameOfPerson = relatedPerson.getFirstName() + " " + relatedPerson.getLastName();
            textView.setText(nameOfPerson);
            textView = personItemView.findViewById(R.id.relation);
            textView.setText(relation);

            //create genderIcon
            Drawable genderIcon;
            if (relatedPerson.getGender().toLowerCase().equals("m")) {
                genderIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            }
            else {
                genderIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            }
            ImageView genderImageView = (ImageView) personItemView.findViewById(R.id.genderIcon);
            genderImageView.setImageDrawable(genderIcon);

            personItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), PersonActivity.class);
                    intent.putExtra(PersonActivity.TEXT_KEY, relatedPerson.getPersonID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

}