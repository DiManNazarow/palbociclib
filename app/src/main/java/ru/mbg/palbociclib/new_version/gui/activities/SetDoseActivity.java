package ru.mbg.palbociclib.new_version.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.App;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.db.models.Event;
import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.new_version.gui.delegate.SetDoseActivityDelegate;
import ru.mbg.palbociclib.views.SetDoseButton;


public class SetDoseActivity extends AppCompatActivity {

    public static final String PATIENT_ID_ARG = "id";
    public static final String DATE_STRING_ARG = "date_string_arg";
    public static final String CYCLE = "cycle";
    public static final String COUNT = "count";

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.one)
    protected Button mFirstButton;
    @BindView(R.id.two)
    protected Button mSecondButton;
    @BindView(R.id.three)
    protected Button mThirdButton;
    @BindView(R.id.four)
    protected Button mFourthButton;
    @BindView(R.id.continue_button)
    protected SetDoseButton mContinueButton;
    @BindView(R.id.stop_appointment)
    protected SetDoseButton mStopAppointmentButton;

    @BindView(R.id.info_text_view)
    protected TextView mInfoView;

    private boolean isInfoShow = false;

    private SetDoseActivityDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dose);
        ButterKnife.bind(this);


        String date = getIntent().getStringExtra(DATE_STRING_ARG);
        int cycle = getIntent().getIntExtra(CYCLE, -1);
        int count = getIntent().getIntExtra(COUNT, -1);
        long patientId = getIntent().getLongExtra(PATIENT_ID_ARG, -1);

        mDelegate = new SetDoseActivityDelegate();

        Patient patient = mDelegate.getPatientFromDB(getApplicationContext(), patientId);
        mDelegate.setPatient(patient);

        if (patient == null){
            patient = mDelegate.fillPatient(new Patient(), date, cycle);
            mDelegate.setPatient(patient);
            mDelegate.insetPatient(getApplicationContext());
        } else {
            mDelegate.fillPatient(patient);
        }

        Event event = mDelegate.getEventFromDB(getApplicationContext(), date);

        if (event == null){
            event = mDelegate.fillEvent(new Event(), date, cycle, count);
            mDelegate.setEvent(event);
        } else {
            switch (event.getToxicLevel()){
                case 1: mFirstButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange)); break;
                case 2: mSecondButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange)); break;
                case 3: mThirdButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange)); break;
                case 4: mFourthButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange)); break;
            }
            mDelegate.fillEvent(event, cycle, count);
            mDelegate.setEvent(event);
        }

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mDelegate.getTitle(getApplicationContext(), date));
        }

        mDelegate.setUi(mToolbar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (event.getToxicLevel() == 0){
            mContinueButton.setEnabled(false);
        } else {
            mDelegate.setGradeUi(mContinueButton, mStopAppointmentButton);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                if (isInfoShow){
                    isInfoShow = false;
                    mInfoView.setVisibility(View.GONE);
                } else {
                    isInfoShow = true;
                    mInfoView.setVisibility(View.VISIBLE);
                }
            default:
                break;
        }
        return true;
    }

    @OnClick(R.id.one)
    protected void onFirstCycleClick(){
        mDelegate.refreshPatient(getApplicationContext());
        mDelegate.getEvent().setToxicLevel(1);
        mFirstButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mSecondButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mThirdButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mFourthButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mDelegate.setGradeUi(mContinueButton, mStopAppointmentButton);
}

    @OnClick(R.id.two)
    protected void onSecondCycleClick(){
        mDelegate.refreshPatient(getApplicationContext());
        mDelegate.getEvent().setToxicLevel(2);
        mFirstButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mSecondButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mThirdButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mFourthButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mDelegate.setGradeUi(mContinueButton, mStopAppointmentButton);
    }

    @OnClick(R.id.three)
    protected void onThirdCycleClick(){
        mDelegate.refreshPatient(getApplicationContext());
        mDelegate.getEvent().setToxicLevel(3);
        mFirstButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mSecondButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mThirdButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mFourthButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mDelegate.setGradeUi(mContinueButton, mStopAppointmentButton);
    }

    @OnClick(R.id.four)
    protected void onFourthCycleClick(){
        mDelegate.refreshPatient(getApplicationContext());
        mDelegate.getEvent().setToxicLevel(4);
        mFirstButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mSecondButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mThirdButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
        mFourthButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mDelegate.setGradeUi(mContinueButton, mStopAppointmentButton);
    }

    @OnClick(R.id.continue_button)
    protected void onContinueClick(){
        mDelegate.insertEvent(getApplicationContext());
        mDelegate.updatePatient(getApplicationContext());
        finish();
    }

    @OnClick(R.id.stop_appointment)
    protected void onStopAppointmentClick(){
        mDelegate.stopAppointment(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        finish();
    }

}
