package ru.mbg.palbociclib.new_version.gui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.App;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.utils.DateUtils;
import ru.mbg.palbociclib.utils.GuiUtils;
import ru.mbg.palbociclib.utils.Utils;


public class StartActivity extends AppCompatActivity {

    @BindView(R.id.date_edit_view)
    protected EditText mDateEditView;
    @BindView(R.id.first_button)
    protected Button mFirstCycleButton;
    @BindView(R.id.second_button)
    protected Button mSecondCycleButton;
    @BindView(R.id.third_button)
    protected Button mThirdCycleButton;
    @BindView(R.id.fourth_button)
    protected Button mFourthCycleButton;

    private Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        mPatient = new Patient();
        setListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDateEditView.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus"
                        , new Class[]{boolean.class});
                method.setAccessible(true);
                method.invoke(mDateEditView, false);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @OnClick(R.id.first_button)
    protected void onFirstCycleClick(){
        mPatient.setCycleCount(1);
        mFirstCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mSecondCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mThirdCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mFourthCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
    }

    @OnClick(R.id.second_button)
    protected void onSecondCycleClick(){
        mPatient.setCycleCount(2);
        mSecondCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mFirstCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mThirdCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mFourthCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
    }

    @OnClick(R.id.third_button)
    protected void onThirdCycleClick(){
        mPatient.setCycleCount(3);
        mThirdCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mFirstCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mSecondCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mFourthCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
    }

    @OnClick(R.id.fourth_button)
    protected void onFourthCycleClick(){
        mPatient.setCycleCount(4);
        mFourthCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mFirstCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mSecondCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        mThirdCycleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
    }

    @OnClick(R.id.next_button_view)
    protected void onNextButtonClick(){
        if (!check()){
            return;
        }

        Patient patient = ((App)getApplicationContext()).getAppDatabase().patientDao().findByDateAndCycle(mPatient.getCycleStartDate(), mPatient.getCycleCount());
        long id;
        if (patient == null){
            id = ((App)getApplicationContext()).getAppDatabase().patientDao().insert(mPatient);
        } else {
            id = patient.getId();
        }

        Intent intent = new Intent(getApplicationContext(), NewCalendarActivity.class);
        intent.putExtra(Patient.SERIALIZABLE_NAME, id);
        startActivity(intent);
    }

    private void setListener(){
        mDateEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateUtils.showDatePickerDialog(StartActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mDateEditView.setText(DateUtils.format(year, month, day));
                        mPatient.setCycleStartDate(DateUtils.format(year, month, day));
                    }
                });
            }
        });
    }

    private boolean check(){
        boolean check = true;
        if (mPatient.getCycleCount() == 0){
            check = false;
            GuiUtils.displayOkDialog(this, R.string.new_version_error, R.string.new_version_error_empty_cycle, null, true);
        }
        if (Utils.isEmpty(mPatient.getCycleStartDate())){
            check = false;
            GuiUtils.displayOkDialog(this, R.string.new_version_error, R.string.new_version_error_empty_date, null, true);
        }
        return check;
    }
}
