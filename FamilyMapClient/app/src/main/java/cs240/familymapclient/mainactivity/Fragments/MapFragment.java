package cs240.familymapclient.mainactivity.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs240.familymapclient.mainactivity.Activities.EventActivity;
import cs240.familymapclient.mainactivity.Activities.PersonActivity;
import cs240.familymapclient.mainactivity.Activities.SearchActivity;
import cs240.familymapclient.mainactivity.Activities.SettingsActivity;
import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.R;
import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private MapFragment.Listener listener;
    private GoogleMap map;
    private View view;
    private String eventID;
    private DataCache dataCache;
    private List<Marker> markers;
    private boolean isMapCreated;
    private int widthOfLines;
    private List<Polyline> polyLines;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(MapFragment.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isMapCreated = false;
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        //create android icon
        Drawable androidIcon;
        androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                colorRes(R.color.android).sizeDp(40);
        ImageView androidImageView = (ImageView) view.findViewById(R.id.genderIcon);
        androidImageView.setImageDrawable(androidIcon);

        //initialize lists
        polyLines = new ArrayList<>();
        markers = new ArrayList<>();
        
        //check if bundle has eventID from other activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            eventID = bundle.getString(EventActivity.TEXT_KEY);
        }
        else {
            eventID = null;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (eventID == null) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.main_menu, menu);
            //search button
            MenuItem searchMenuItem = menu.findItem(R.id.search_button);
            searchMenuItem.setIcon(new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_search)
                    .colorRes(R.color.white)
                    .actionBarSize());
            //settings button
            MenuItem settingsMenuItem = menu.findItem(R.id.settings_button);
            settingsMenuItem.setIcon(new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.white)
                    .actionBarSize());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
        }
        else if (item.getItemId() == R.id.search_button) {
            Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
            startActivity(searchIntent);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onMapLoaded() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        populateMap();
        isMapCreated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        dataCache = DataCache.getInstance();
        if (dataCache.isSettingsChanged()) {
            deleteLines();
            deleteMarkers();
            if (isMapCreated) {
                populateMap();
            }
            dataCache.setSettingsChanged(false);
        }
    }

    private void populateMap(){
        dataCache = DataCache.getInstance();

        Map<String, Event> events = dataCache.getEvents();
        float latitude;
        float longitude;
        String eventType;
        LatLng place;
        int colorIndex = 0;
        float markerColor;
        Marker marker;

        //map to keep track of which color goes with which event type
        Map<String, Float> eventColors = new HashMap<>();
        //array of all colors
        float[] colors = {BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_MAGENTA,
                BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_ROSE,
                BitmapDescriptorFactory.HUE_VIOLET,   BitmapDescriptorFactory.HUE_YELLOW};

        //place markers for all events in list
        for (Map.Entry<String, Event> e : events.entrySet()) {

            latitude = e.getValue().getLatitude();
            longitude = e.getValue().getLongitude();
            eventType = e.getValue().getEventType();
            place = new LatLng(latitude, longitude);

            //check if color has already been used
            if (!eventColors.containsKey(eventType)) {
                if (colorIndex > 9) {
                    colorIndex = 0;
                }
                eventColors.put(eventType, colors[colorIndex]);
                colorIndex++;
            }

            //add maker
            markerColor = eventColors.get(eventType);
            marker = map.addMarker(new MarkerOptions().position(place).title(eventType).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
            marker.setTag(e.getValue());
            markers.add(marker);

            //if event was selected by event activity, display it
            if (eventID != null) {
                if (e.getValue().getEventID().equals(eventID)) {
                    map.moveCamera(CameraUpdateFactory.zoomTo(5));
                    map.animateCamera(CameraUpdateFactory.newLatLng(place));
                    showDetails(e.getValue());
                    createLines(e.getValue());
                }
            }
        }

        //marker listener
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            //show event details and lines
            public boolean onMarkerClick(@NonNull Marker marker) {
                Event event = (Event)marker.getTag();
                showDetails(event);
                deleteLines();
                createLines(event);
                return false;
            }
        });
    }

    private void showDetails(Event event) {
        Person person = dataCache.getPerson(event.getPersonID());

        //show event details
        String eventInfo = String.format("%s %s\n%s: %s, %s (%d)", person.getFirstName(),
                                         person.getLastName(), event.getEventType(),
                                         event.getCity(), event.getCountry(), event.getYear());
        TextView textView = (TextView)view.findViewById(R.id.newEventInfo);
        textView.setText(eventInfo);

        //draw gender icons
        Drawable genderIcon;
        if (person.getGender().toLowerCase().equals("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(55);
        }
        else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(55);
        }
        ImageView genderImageView = (ImageView) view.findViewById(R.id.genderIcon);
        genderImageView.setImageDrawable(genderIcon);

        //check if person was touched
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(PersonActivity.TEXT_KEY, event.getPersonID());
                startActivity(intent);
                return false;
            }
        });
    }

    private void deleteMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    private void deleteLines() {
        for (Polyline line : polyLines) {
            line.remove();
        }
        polyLines.clear();
    }

    private void createLines(Event event) {
        Person person = dataCache.getPerson(event.getPersonID());

        //filter spouse lines
        if (dataCache.getSpouseLines()) {
            //check if spouse exists
            if (person.getSpouseID() != null) {
                Event spouseEvent = getEarliestEvent(person.getSpouseID());
                //check if event exists with filter
                if (spouseEvent != null && dataCache.containsEvent(spouseEvent)) {
                    drawLine(event, spouseEvent, Color.RED, 7);
                }

            }
        }

        //filter family tree lines
        if (dataCache.getFamilyTree()) {
            widthOfLines = 1;
            createFamilyTreeLines(person, 1, event);
        }

        //filter life story lines
        if (dataCache.getLifeStory()) {
            List<Event> personEvents = dataCache.getEventsOfPerson(person.getPersonID());
            Event firstEvent = personEvents.get(0);
            Event secondEvent;
            for (int i = 0; i < (personEvents.size() - 1); i++) {
                secondEvent = personEvents.get(i + 1);
                drawLine(firstEvent, secondEvent, Color.GREEN, 7);
                firstEvent = secondEvent;
            }
        }
    }

    private void createFamilyTreeLines (Person person, int currWidth, Event childEvent) {
        //check if person is in filter
        if (dataCache.containsPerson(person.getPersonID())) {
            //check if father exists
            if (person.getFatherID() != null) {
                widthOfLines += 3;
                createFamilyTreeLines(dataCache.getPerson(person.getFatherID()),
                              currWidth + 3, getEarliestEvent(person.getFatherID()));
                Event fatherEvent = getEarliestEvent(person.getFatherID());
                if (fatherEvent != null
                        && childEvent != null
                        && dataCache.containsEvent(fatherEvent)
                        && dataCache.containsEvent(childEvent)) {
                    drawLine(childEvent, fatherEvent, Color.BLUE, widthOfLines - currWidth);
                }
            }

            //reset width of lines
            widthOfLines = currWidth;
            //check if mother exists
            if (person.getMotherID() != null) {
                widthOfLines += 3;
                createFamilyTreeLines(dataCache.getPerson(person.getMotherID()),
                        currWidth + 3, getEarliestEvent(person.getMotherID()));
                Event motherEvent = getEarliestEvent(person.getMotherID());
                if (motherEvent != null
                        && childEvent != null
                        && dataCache.containsEvent(motherEvent)
                        && dataCache.containsEvent(childEvent)) {
                    drawLine(childEvent, motherEvent, Color.BLUE, widthOfLines - currWidth);
                }
            }
        }
    }

    private Event getEarliestEvent(String personID) {
        List<Event> eventsOfPerson = dataCache.getEventsOfPerson(personID);
        if (eventsOfPerson.size() > 0) {
            return eventsOfPerson.get(0);
        }
        return null;
    }

    private void drawLine(Event startEvent, Event endEvent, float googleColor, float width) {
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint)
                .color((int) googleColor)
                .width(width);
        polyLines.add(map.addPolyline(options));
    }
}

