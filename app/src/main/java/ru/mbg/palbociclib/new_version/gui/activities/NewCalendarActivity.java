package ru.mbg.palbociclib.new_version.gui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.calendar.CalendarMonthAdapter;
import ru.mbg.palbociclib.new_version.models.Patient;
import ru.mbg.palbociclib.new_version.models.PatientModelHelper;
import ru.mbg.palbociclib.new_version.gui.views.CycleInfoView;
import ru.mbg.palbociclib.utils.DateUtils;

public class NewCalendarActivity extends AppCompatActivity {

    @BindView(R.id.cycle_info_view)
    protected CycleInfoView mCycleInfoView;
//    @BindView(R.id.month_picker_view)
//    protected MonthPickerView mMonthPickerView;
    @BindView(R.id.month_recycler_view)
    protected RecyclerView mMonthRecyclerView;
    @BindView(R.id.floatingActionButton)
    protected FrameLayout mMonitoringButton;

    private Patient mPatient;

    private Pair<Integer, Integer> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calendar);
        ButterKnife.bind(this);

        mPatient = (Patient)getIntent().getSerializableExtra(Patient.SERIALIZABLE_NAME);

        if (mPatient == null){
            mPatient = new Patient();
            mPatient.setCycleCount(1);
            mPatient.setCycleStartDate(DateUtils.getCurrentDateString());
        }

        mCycleInfoView.setTitleText(getString(R.string.new_version_calendar_title));

        mPatient.setOaksDate(PatientModelHelper.fillPatientActionDate(mPatient));

        info = PatientModelHelper.getDaysOfCycle(mPatient.getOaksDate());
        if (info.second == 0) {
            mCycleInfoView.setDayOfCycleText(getString(R.string.cycle_oak_card, mPatient.getCycleCount()));
        } else {
            mCycleInfoView.setDayOfCycleText(getString(R.string.cycle_day_of_oak, info.first, info.second));
        }

        mCycleInfoView.setDateText(getString(R.string.new_version_date_start, mPatient.getCycleStartDate()));
        mCycleInfoView.setCloseButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mMonthRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mMonthRecyclerView.setAdapter(new CalendarMonthAdapter(getApplicationContext(), mPatient));

//        mMonthPickerView.setDateItemPickListener(new MonthPickerView.OnDateItemPickListener() {
//            @Override
//            public void onDatePick(Date date) {
//                mMonthRecyclerView.setAdapter(new CalendarMonthAdapter(getApplicationContext(), date, mPatient));
//            }
//        });

        mMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MonitoringActivity.class);
                intent.putExtra(MonitoringActivity.CYCLE, info.second);
                startActivity(intent);
            }
        });

    }

    private void showPopup(){

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_layout, null);
        adb.setView(view);
        TextView mCancelButton = (TextView)view.findViewById(R.id.cancel_button);
        TextView mMonitoringButton = (TextView)view.findViewById(R.id.monitoring_button);
        final AlertDialog alertDialog = adb.create();
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MonitoringActivity.class);
                intent.putExtra(MonitoringActivity.CYCLE, info.second);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
}
