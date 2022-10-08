package cs240.familymapclient.mainactivity.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import cs240.familymapclient.mainactivity.Fragments.LoginFragment;
import cs240.familymapclient.mainactivity.Fragments.MapFragment;
import cs240.familymapclient.mainactivity.R;

public class EventActivity extends AppCompatActivity implements MapFragment.Listener {

    public static final String TEXT_KEY = "EventID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create fragments
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
        if(fragment == null) {
            fragment = createMapFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fragmentFrameLayout, fragment)
                    .commit();
        } else {
            if(fragment instanceof LoginFragment) {
                ((MapFragment) fragment).registerListener(this);
            }
        }
    }

    private Fragment createMapFragment() {
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        bundle.putString(TEXT_KEY, intent.getStringExtra(TEXT_KEY));
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void notifyDone() {
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