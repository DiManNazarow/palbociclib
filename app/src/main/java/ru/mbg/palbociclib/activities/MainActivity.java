package ru.mbg.palbociclib.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import ru.mbg.palbociclib.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String SELECTED_ITEM = "selected_item";

    private CalendarFragment calendarFragment;
    private PatientsFragment patientsFragment;
    private PreparatFragment preparatFragment;
    private ProfileFragment moreFragment;
    private FragmentManager manager;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();

        int selectedID = R.id.navigation_patients;
        if (savedInstanceState != null) {
            selectedID = savedInstanceState.getInt(SELECTED_ITEM, R.id.navigation_patients);
        }
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        showFragmentForID(selectedID);
        navigation.setSelectedItemId(selectedID);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM, navigation.getSelectedItemId());
    }

    public CalendarFragment getCalendarFragment() {
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance();
        }
        return calendarFragment;
    }

    public PatientsFragment getPatientsFragment() {
        if (patientsFragment == null) {
            patientsFragment = PatientsFragment.newInstance();
        }
        return patientsFragment;
    }

    public PreparatFragment getPreparatFragment() {
        if (preparatFragment == null) {
            preparatFragment = PreparatFragment.newInstance();
        }
        return preparatFragment;
    }

    public ProfileFragment getMoreFragment() {
        if (moreFragment == null) {
            moreFragment = ProfileFragment.newInstance();
        }
        return moreFragment;
    }

    private boolean showFragmentForID(int selectedID) {
        switch (selectedID) {
            case R.id.navigation_calendar:
                manager.beginTransaction().replace(R.id.content, getCalendarFragment()).commit();
                return true;
            case R.id.navigation_patients:
                manager.beginTransaction().replace(R.id.content, getPatientsFragment()).commit();
                return true;
            case R.id.navigation_preparat:
                manager.beginTransaction().replace(R.id.content, getPreparatFragment()).commit();
                return true;
            case R.id.navigation_more:
                manager.beginTransaction().replace(R.id.content, getMoreFragment()).commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return showFragmentForID(item.getItemId());
    }

    public void selectPrevMonth(View v) {
        getCalendarFragment().selectPrevMonth();
    }

    public void selectNextMonth(View v) {
        getCalendarFragment().selectNextMonth();
    }
}
