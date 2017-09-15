package ru.mbg.palbociclib.new_version.gui.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;


public class MonthPickerView extends LinearLayout {

    @BindView(R.id.month_spinner)
    protected Spinner mMonthSpinner;

    protected View mRootView;

    private OnDateItemPickListener mDateItemPickListener;

    public MonthPickerView(Context context) {
        super(context);
        init();
    }

    public MonthPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MonthPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_month_picker, this, true);
        ButterKnife.bind(this, mRootView);
        setup();
    }

    public void setup(){
        Calendar calendar = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<>();
        calendar.add(Calendar.MONTH, -4);
        for (int i = 0; i < 10; i++){
            dates.add(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
        }
        mMonthSpinner.setAdapter(new MonthPickerAdapter(getContext(), dates));
        mMonthSpinner.setSelection(4);
        mMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Date date = ((MonthPickerAdapter)adapterView.getAdapter()).getDate(i);
                if (mDateItemPickListener != null){
                    mDateItemPickListener.onDatePick(date);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setDateItemPickListener(OnDateItemPickListener dateItemPickListener) {
        mDateItemPickListener = dateItemPickListener;
    }

    public interface OnDateItemPickListener{
        void onDatePick(Date date);
    }

}
