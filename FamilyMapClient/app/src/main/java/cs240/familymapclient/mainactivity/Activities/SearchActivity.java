package cs240.familymapclient.mainactivity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.R;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        //get search view
        DataCache dataCache = DataCache.getInstance();
        SearchView searchView = findViewById(R.id.search_field);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    //run everytime text changes
                    public boolean onQueryTextChange(String s) {
                        List<Person> people = new ArrayList<>();
                        List<model.Event> events = new ArrayList<>();
                        people = dataCache.searchPeople(s);
                        events = dataCache.searchEvents(s);
                        SearchAdapter adapter = new SearchAdapter(people, events);
                        recyclerView.setAdapter(adapter);
                        return false;
                    }
                }
        );
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Person> people;
        private final List<model.Event> events;

        SearchAdapter(List<Person> people, List<model.Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView eventDescription;

        private final int viewType;
        private Person person;
        private model.Event event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.personName);
                eventDescription = null;
            } else {
                name = itemView.findViewById(R.id.nameOfPerson);
                eventDescription = itemView.findViewById(R.id.event);
            }
        }

        private void bind(Person person) {
            Drawable genderIcon;
            if (person.getGender().toLowerCase().equals("m")) {
                genderIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            }
            else {
                genderIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            }
            ImageView genderImageView = (ImageView) itemView.findViewById(R.id.genderIcon);
            genderImageView.setImageDrawable(genderIcon);

            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
        }

        private void bind(model.Event event) {
            Drawable eventIcon;
            eventIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(30);
            ImageView genderImageView = itemView.findViewById(R.id.eventIcon);
            genderImageView.setImageDrawable(eventIcon);

            this.event = event;
            DataCache dataCache = DataCache.getInstance();
            Person person = dataCache.getPerson(event.getPersonID());
            name.setText(person.getFirstName() + " " + person.getLastName());
            String eventInfo = String.format("%s: %s, %s (%d)", event.getEventType(),
                    event.getCity(), event.getCountry(), event.getYear());
            eventDescription.setText(eventInfo);
        }

        @Override
        public void onClick(View view) {
            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                Intent intent = new Intent(getBaseContext(), PersonActivity.class);
                intent.putExtra(PersonActivity.TEXT_KEY, person.getPersonID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getBaseContext(), EventActivity.class);
                intent.putExtra(EventActivity.TEXT_KEY, event.getEventID());
                startActivity(intent);
            }
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

