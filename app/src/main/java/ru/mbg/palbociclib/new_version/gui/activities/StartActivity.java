package ru.mbg.palbociclib.new_version.gui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.App;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.models.Patient;
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

    @BindView(R.id.date_create_text_view)
    protected TextView mDateCreateTextView;
    @BindView(R.id.next_button_view)
    protected Button mContinueButton;

    private Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        mPatient = new Patient();
        mPatient.setCycleStartDate(DateUtils.getCurrentDateString());
        setListener();
        colorDateCreate();
        writeCurrentDate();
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

    private void colorDateCreate(){
        String text = getString(R.string.new_version_date_of_app_create);
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.green)), 0, text.length() - 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mDateCreateTextView.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    private void writeCurrentDate(){
        mDateEditView.setText(DateUtils.getCurrentDateString());
    }

    @OnClick(R.id.first_button)
    protected void onFirstCycleClick(){
        mPatient.setCycleCount(1);
        mFirstCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_left_fill);
        mFirstCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mSecondCycleButton.setBackgroundResource(R.drawable.cycle_button);
        mSecondCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mThirdCycleButton.setBackgroundResource(R.drawable.cycle_button);
        mThirdCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mFourthCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_right);
        mFourthCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        makeContinueButtonEnable();
    }

    @OnClick(R.id.second_button)
    protected void onSecondCycleClick(){
        mPatient.setCycleCount(2);
        mFirstCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_left);
        mFirstCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mSecondCycleButton.setBackgroundResource(R.drawable.cycle_button_fill);
        mSecondCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mThirdCycleButton.setBackgroundResource(R.drawable.cycle_button);
        mThirdCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mFourthCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_right);
        mFourthCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        makeContinueButtonEnable();
    }

    @OnClick(R.id.third_button)
    protected void onThirdCycleClick(){
        mPatient.setCycleCount(3);
        mFirstCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_left);
        mFirstCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mSecondCycleButton.setBackgroundResource(R.drawable.cycle_button);
        mSecondCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mThirdCycleButton.setBackgroundResource(R.drawable.cycle_button_fill);
        mThirdCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mFourthCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_right);
        mFourthCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        makeContinueButtonEnable();
    }

    @OnClick(R.id.fourth_button)
    protected void onFourthCycleClick(){
        mPatient.setCycleCount(4);
        mFirstCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_left);
        mFirstCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mSecondCycleButton.setBackgroundResource(R.drawable.cycle_button);
        mSecondCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mThirdCycleButton.setBackgroundResource(R.drawable.cycle_button);
        mThirdCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mFourthCycleButton.setBackgroundResource(R.drawable.start_activity_cycle_button_right_fill);
        mFourthCycleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        makeContinueButtonEnable();
    }

    @OnClick(R.id.next_button_view)
    protected void onNextButtonClick(){
        if (!check()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), NewCalendarActivity.class);
        intent.putExtra(Patient.SERIALIZABLE_NAME, mPatient);
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
                        makeContinueButtonEnable();
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

    private void makeContinueButtonEnable(){
        if (mPatient.getCycleCount() != 0 && mPatient.getCycleStartDate() != null){
            mContinueButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            mContinueButton.setEnabled(true);
        } else {
            mContinueButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.greyed_out));
            mContinueButton.setEnabled(false);
        }
    }
}
