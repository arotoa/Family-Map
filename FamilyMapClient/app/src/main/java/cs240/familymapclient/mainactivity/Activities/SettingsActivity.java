package cs240.familymapclient.mainactivity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableRow;

import cs240.familymapclient.mainactivity.Activities.MainActivity;
import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DataCache dataCache = DataCache.getInstance();

        Switch lifeStorySwitch = (Switch) findViewById(R.id.lifeStorySwitch);
        Switch familyTreeSwitch = (Switch) findViewById(R.id.familyTreeSwitch);
        Switch spouseSwitch = (Switch) findViewById(R.id.spouseSwitch);
        Switch fatherSideSwitch = (Switch) findViewById(R.id.fatherSideSwitch);
        Switch motherSideSwitch = (Switch) findViewById(R.id.motherSideSwitch);
        Switch maleEventSwitch = (Switch) findViewById(R.id.maleEventSwitch);
        Switch femaleEventSwitch = (Switch) findViewById(R.id.femaleEventSwitch);

        lifeStorySwitch.setChecked(dataCache.getLifeStory());
        familyTreeSwitch.setChecked(dataCache.getFamilyTree());
        spouseSwitch.setChecked(dataCache.getSpouseLines());
        fatherSideSwitch.setChecked(dataCache.getFathersSide());
        motherSideSwitch.setChecked(dataCache.getMothersSide());
        maleEventSwitch.setChecked(dataCache.getMaleEvents());
        femaleEventSwitch.setChecked(dataCache.getFemaleEvents());

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setLifeStory(b);
                dataCache.setSettingsChanged(true);
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setFamilyTree(b);
                dataCache.setSettingsChanged(true);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setSpouseLines(b);
                dataCache.setSettingsChanged(true);
            }
        });

        fatherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setFathersSide(b);
                dataCache.filterEvents();
                dataCache.setSettingsChanged(true);
            }
        });

        motherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setMothersSide(b);
                dataCache.filterEvents();
                dataCache.setSettingsChanged(true);
            }
        });

        maleEventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setMaleEvents(b);
                dataCache.filterEvents();
                dataCache.setSettingsChanged(true);
            }
        });

        femaleEventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataCache.setFemaleEvents(b);
                dataCache.filterEvents();
                dataCache.setSettingsChanged(true);
            }
        });

        TableRow logoutView = findViewById(R.id.logoutRow);
        logoutView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dataCache.clearData();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        });
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