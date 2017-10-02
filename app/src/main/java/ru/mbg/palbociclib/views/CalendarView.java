package ru.mbg.palbociclib.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.calendar.CyclesCounterHolder;
import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.new_version.db.models.helpers.PatientModelHelper;
import ru.mbg.palbociclib.new_version.gui.activities.MonitoringActivity;
import ru.mbg.palbociclib.new_version.gui.activities.SetDoseActivity;
import ru.mbg.palbociclib.utils.DateUtils;
import ru.mbg.palbociclib.utils.Three;

public class CalendarView extends LinearLayout{

    @BindView(R.id.calendar_grid)
    protected GridView grid;
    @BindView(R.id.cycle_count)
    protected GridView mCycleCountView;

    private View mRootView;

    private Calendar currentDate = Calendar.getInstance();

    private Patient mPatient;

    private double mCycle = -1;

    private double mLast = 0;

    private List<Pair<Integer, Integer>> mCycles;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    private int endPos = 0;

    private boolean update = true;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.calendar, this, true);
        ButterKnife.bind(this, mRootView);
        updateCalendar();
        setListeners(mOnItemLongClickListener, mOnItemClickListener);
        mCycles = new ArrayList<>();
    }

    public void setListeners(AdapterView.OnItemLongClickListener onItemLongClickListener, AdapterView.OnItemClickListener onItemClickListener){
        mOnItemLongClickListener = onItemLongClickListener;
        mOnItemClickListener = onItemClickListener;
        if (mOnItemClickListener != null) {
            grid.setOnItemClickListener(mOnItemClickListener);
        }
        if (mOnItemLongClickListener != null) {
            grid.setOnItemLongClickListener(mOnItemLongClickListener);
        }
    }

    public void setCalendarData(Patient patient, Date nextMonth){
        if (nextMonth != null) {
            currentDate.setTime(nextMonth);
        }
        mPatient = patient;
        mCycle = patient.getCycleCount();
        updateCalendar();
    }

    public void setCalendarData(Patient patient, Date nextMonth, int cycleIncreaseSize){
        if (nextMonth != null) {
            currentDate.setTime(nextMonth);
        }
        mPatient = patient;
        update = false;
        //mCycle = patient.getCycleCount() + cycleIncreaseSize;
        updateCalendar();
    }

    List<Date> cells;

    int monthBeginningCell;

    public void updateCalendar() {
        try {
            cells = new ArrayList<>();
            Calendar calendar = (Calendar) currentDate.clone();

            // determine the cell for current month's beginning
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2;

            // move calendar backwards to the beginning of the week
            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

            // fill cells
            int lineStart = 0;
            int lineEnd = 5;
            int dayInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            Date cycleStartDate = DateUtils.getDate(mPatient.getCycleStartDate(), DateUtils.DEFAULT_DATE_PATTERN);

            while (cells.size() < 48) {
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (cells.size() > 1) {
                    if ((DateUtils.getMonth(cells.get(cells.size() - 2)) < DateUtils.getMonth(cells.get(cells.size() - 1))) || (DateUtils.getYear(cells.get(cells.size() - 2)) < DateUtils.getYear(cells.get(cells.size() - 1)))) {
                        endPos = cells.size() - 2;
                    }
                }
            }

            // update grid
            grid.setAdapter(new CalendarAdapter(getContext(), cells, mPatient));
            grid.post(new Runnable() {
                @Override
                public void run() {
                    setCyclesCounter();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class CalendarAdapter extends ArrayAdapter<Date> {

        private LayoutInflater inflater;
        private Patient mPatient;

        public CalendarAdapter(Context context, List<Date> days, Patient patient) {
            super(context, R.layout.control_calendar_day, days);
            try {
                mPatient = patient;
                inflater = LayoutInflater.from(context);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        boolean newCycle = false;

        @Override
        @NonNull
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            try {
                boolean isBackGroundSet = false;
                // day in question
                Date date = getItem(position);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getItem(position));
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // today
                Calendar todayByCalendar = Calendar.getInstance();
                Calendar todayByList = (Calendar) currentDate.clone();

                // inflate item if it does not exist yet
                if (view == null)
                    view = inflater.inflate(R.layout.control_calendar_day, parent, false);

                TextView textView = (TextView) ((ViewGroup)view).getChildAt(0);

                // if this day has an event, specify event image
                view.setBackgroundResource(0);


                // clear styling
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setTextColor(Color.BLACK);
                textView.setText(String.valueOf(date.getDate()));

                if (mPatient.getOaksDate() != null) {
                    for (final Three<Integer, Date, Integer> eventDate : mPatient.getOaksDate()) {

                        Calendar calendarC = Calendar.getInstance();
                        calendarC.setTime(eventDate.second);
                        int dayC = calendarC.get(Calendar.DAY_OF_MONTH);
                        int monthC = calendarC.get(Calendar.MONTH);
                        int yearC = calendarC.get(Calendar.YEAR);

                        if (dayC == day && monthC == month && yearC == year) {
                            if (month != todayByList.get(Calendar.MONTH) || year != todayByList.get(Calendar.YEAR)) {
                                continue;
                            } else {
                                switch (eventDate.first) {
                                    case PatientModelHelper.EventList.START_CYCLE_DATE: {
                                        mCycles.add(new Pair<Integer, Integer>(PatientModelHelper.EventList.START_CYCLE_DATE, position));
                                        mCycle = eventDate.third;
                                        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                        textView.setText("OAK");
                                        view.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openSetDoseScreen(position, eventDate.third, 1);
                                            }
                                        });
                                        break;
                                    }
                                    case PatientModelHelper.EventList.OAK_DATE: {
                                        if (eventDate.third > 2) {
                                            switch (position) {
                                                case 0:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle));
                                                    break;
                                                case 6:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                                    break;
                                                case 7:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle));
                                                    break;
                                                case 13:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                                    break;
                                                case 14:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle));
                                                    break;
                                                case 20:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                                    break;
                                                case 21:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle));
                                                    break;
                                                case 27:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                                    break;
                                                case 28:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle));
                                                    break;
                                                case 34:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                                    break;
                                                case 35:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle));
                                                    break;
                                                case 41:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                                    break;
                                                default:
                                                    textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.calendar_fill));
                                                    break;
                                            }
                                        } else {
                                            switch (position) {
                                                case 0:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                                    break;
                                                case 6:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right));
                                                    break;
                                                case 7:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                                    break;
                                                case 13:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right));
                                                    break;
                                                case 14:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                                    break;
                                                case 20:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right));
                                                    break;
                                                case 21:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                                    break;
                                                case 27:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right));
                                                    break;
                                                case 28:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                                    break;
                                                case 34:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right));
                                                    break;
                                                case 35:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_left));
                                                    break;
                                                case 41:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right));
                                                    break;
                                                default:
                                                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak));
                                                    break;
                                            }
                                        }

                                        if (eventDate.third < 3) {
                                            textView.setText("OAK");
                                        }
                                        view.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (eventDate.third < 3) {
                                                    openSetDoseScreen(position, eventDate.third, 14);
                                                }
                                            }
                                        });
                                        break;
                                    }
                                    case PatientModelHelper.EventList.CYCLE_DATE: {
                                        mCycle = eventDate.third;
                                        switch (position){
                                            case 0: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle)); break;
                                            case 6: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle)); break;
                                            case 7: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle)); break;
                                            case 13: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle)); break;
                                            case 14: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle)); break;
                                            case 20: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle)); break;
                                            case 21: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle)); break;
                                            case 27: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle)); break;
                                            case 28: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle)); break;
                                            case 34: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle)); break;
                                            case 35: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_left_cycle)); break;
                                            case 41: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle)); break;
                                            default:  textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.calendar_fill)); break;
                                        }
                                        break;
                                    }
                                    case PatientModelHelper.EventList.END_CYCLE_DATE: {
                                        mCycles.add(new Pair<Integer, Integer>(PatientModelHelper.EventList.END_CYCLE_DATE, position));
                                        mCycle = eventDate.third;
                                        isBackGroundSet = true;
                                        if (eventDate.third < 3) {
                                            textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.oak_right_21));
                                            textView.setText("OAK");
                                        } else {
                                            textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_right_cycle));
                                        }
                                        view.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (eventDate.third < 3) {
                                                    openSetDoseScreen(position, eventDate.third, 21);
                                                }
                                            }
                                        });
                                        break;
                                    }
                                    case PatientModelHelper.EventList.PAUSE_START_DATE:{
                                        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left));
                                        break;
                                    }
                                    case PatientModelHelper.EventList.PAUSE_DATE:{
                                        switch (position){
                                            case 0: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left)); break;
                                            case 6: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right)); break;
                                            case 7: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left)); break;
                                            case 13: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right)); break;
                                            case 14: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left)); break;
                                            case 20: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right)); break;
                                            case 21: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left)); break;
                                            case 27: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right)); break;
                                            case 28: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left)); break;
                                            case 34: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right)); break;
                                            case 35: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_left)); break;
                                            case 41: textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right)); break;
                                            default:  textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pause)); break;
                                        }
                                        break;
                                    }
                                    case PatientModelHelper.EventList.PAUSE_END_DATE:{
                                        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause_right));
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

                if (month != todayByList.get(Calendar.MONTH) || year != todayByList.get(Calendar.YEAR)) {
                    // if this day is outside current month, grey it out
                    textView.setText("");
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                } else if (day == todayByCalendar.get(Calendar.DAY_OF_MONTH)) {
                    // if it is today, set it to blue/bold
                    if (todayByCalendar.get(Calendar.MONTH) == todayByList.get(Calendar.MONTH)) {
                        textView.setTypeface(null, Typeface.BOLD);
                        if (!isBackGroundSet) {
                            view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.current_day_calendar));
                            if (todayByList.getTime().after(DateUtils.getDate(mPatient.getCycleStartDate(), DateUtils.DEFAULT_DATE_PATTERN))) {
                                textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_fill));
                            }
                        }
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    }
                }
                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        private void openSetDoseScreen(int position, int cycle, int count){
            Intent intent = new Intent(getContext(), MonitoringActivity.class);
//            intent.putExtra(SetDoseActivity.PATIENT_ID_ARG, mPatient.getId());
//            if (date != null) {
//                intent.putExtra(SetDoseActivity.DATE_STRING_ARG, DateUtils.format(date, DateUtils.DEFAULT_DATE_PATTERN));
//            }
            intent.putExtra(SetDoseActivity.CYCLE, cycle);
            intent.putExtra(SetDoseActivity.COUNT, count);
            getContext().startActivity(intent);
        }

    }

    private void setCyclesCounter(){
        List<Double> cycles = new ArrayList<>();

//        if (!update){
//            CyclesCounterHolder.instance().reset();
//        }

        if (monthBeginningCell == -1) {
            CyclesCounterHolder.instance().reset(0);
        } else {
            CyclesCounterHolder.instance().reset(0.1);
        }

        if (mCycles.size() == 2) {
            if (mCycles.get(0).first == PatientModelHelper.EventList.START_CYCLE_DATE) {
                int start = 0;
                int end = 0;
                int pos = mCycles.get(0).second;
                if (pos >= 0 && pos <= 6) {
                    start = 0;
                } else if (pos >= 7 && pos <= 13) {
                    start = 1;
                } else if (pos >= 14 && pos <= 20) {
                    start = 2;
                } else if (pos >= 21 && pos <= 27) {
                    start = 3;
                } else if (pos >= 28 && pos <= 34) {
                    start = 4;
                } else if (pos >= 35 && pos <= 41) {
                    start = 5;
                }
                pos = mCycles.get(1).second;
                if (pos >= 0 && pos <= 6) {
                    end = 0;
                } else if (pos >= 7 && pos <= 13) {
                    end = 1;
                } else if (pos >= 14 && pos <= 20) {
                    end = 2;
                } else if (pos >= 21 && pos <= 27) {
                    end = 3;
                } else if (pos >= 28 && pos <= 34) {
                    end = 4;
                } else if (pos >= 35 && pos <= 41) {
                    end = 5;
                }
                for (int i = 0; i < 6; i++){
                    if (i >= start/* && i <= end*/){
                        cycles.add(mCycle += 0.1);
                        mLast = cycles.get(i);
                    } else {
                        cycles.add(-1.0);
                    }
                }

            } else if (mCycles.get(0).first == PatientModelHelper.EventList.END_CYCLE_DATE) {
                int start = 0;
                int end = 0;
                int pos = endPos;
                if (pos >= 0 && pos <= 6) {
                    end = 0;
                } else if (pos >= 7 && pos <= 13) {
                    end = 1;
                } else if (pos >= 14 && pos <= 20) {
                    end = 2;
                } else if (pos >= 21 && pos <= 27) {
                    end = 3;
                } else if (pos >= 28 && pos <= 34) {
                    end = 4;
                } else if (pos >= 35 && pos <= 41) {
                    end = 5;
                }
                pos = mCycles.get(1).second;
                if (pos >= 0 && pos <= 6) {
                    start = 0;
                } else if (pos >= 7 && pos <= 13) {
                    start = 1;
                } else if (pos >= 14 && pos <= 20) {
                    start = 2;
                } else if (pos >= 21 && pos <= 27) {
                    start = 3;
                } else if (pos >= 28 && pos <= 34) {
                    start = 4;
                } else if (pos >= 35 && pos <= 41) {
                    start = 5;
                }
                for (int i = 0; i < 6; i++){
                    if (i < start){
                        cycles.add(CyclesCounterHolder.instance().increase());
                    } else if (i >= start && i <= end){
                        cycles.add(mCycle += 0.1);
                    } else {
                        cycles.add(-1.0);
                    }
                }

            }
        } else if (mCycles.size() == 1){
            if (mCycles.get(0).first == PatientModelHelper.EventList.START_CYCLE_DATE){
                int start = 0;
                int pos = mCycles.get(0).second;
                if (pos >= 0 && pos <= 6) {
                    start = 0;
                } else if (pos >= 7 && pos <= 13) {
                    start = 1;
                } else if (pos >= 14 && pos <= 20) {
                    start = 2;
                } else if (pos >= 21 && pos <= 27) {
                    start = 3;
                } else if (pos >= 28 && pos <= 34) {
                    start = 4;
                } else if (pos >= 35 && pos <= 41) {
                    start = 5;
                }

                int end = 0;
                if (endPos >= 0 && endPos <= 6) {
                    end = 0;
                } else if (endPos >= 7 && endPos <= 13) {
                    end = 1;
                } else if (endPos >= 14 && endPos <= 20) {
                    end = 2;
                } else if (endPos >= 21 && endPos <= 27) {
                    end = 3;
                } else if (endPos >= 28 && endPos <= 34) {
                    end = 4;
                } else if (endPos >= 35 && endPos <= 41) {
                    end = 5;
                }

                for (int i = 0; i < 6; i++){
                    if (i >=start && i <= end){
                        cycles.add(mCycle += 0.1);
                        mLast = cycles.get(i);
                    } else {
                        cycles.add(-1.0);
                    }
                }
            } else if (mCycles.get(0).first == PatientModelHelper.EventList.END_CYCLE_DATE){
                int end = 0;
                int pos = mCycles.get(0).second;
                if (pos >= 0 && pos <= 6) {
                    end = 0;
                } else if (pos >= 7 && pos <= 13) {
                    end = 1;
                } else if (pos >= 14 && pos <= 20) {
                    end = 2;
                } else if (pos >= 21 && pos <= 27) {
                    end = 3;
                } else if (pos >= 28 && pos <= 34) {
                    end = 4;
                } else if (pos >= 35 && pos <= 41) {
                    end = 5;
                }
                for (int i = 0; i < 6; i++){
                    if (i <= end){
                        cycles.add(mCycle += 0.1);
                        mLast = cycles.get(i);
                    } else {
                        cycles.add(-1.0);
                    }
                }
            }
        } else if (mCycles.size() == 3){
            if (mCycles.get(0).first == PatientModelHelper.EventList.START_CYCLE_DATE){

                int three = 0;
                int start = 0;
                int end = 0;
                int pos = mCycles.get(0).second;

                if (pos >= 0 && pos <= 6) {
                    three = 0;
                } else if (pos >= 7 && pos <= 13) {
                    three = 1;
                } else if (pos >= 14 && pos <= 20) {
                    three = 2;
                } else if (pos >= 21 && pos <= 27) {
                    three = 3;
                } else if (pos >= 28 && pos <= 34) {
                    three = 4;
                } else if (pos >= 35 && pos <= 41) {
                    three = 5;
                }

                pos = mCycles.get(1).second;
                if (pos >= 0 && pos <= 6) {
                    start = 0;
                } else if (pos >= 7 && pos <= 13) {
                    start = 1;
                } else if (pos >= 14 && pos <= 20) {
                    start = 2;
                } else if (pos >= 21 && pos <= 27) {
                    start = 3;
                } else if (pos >= 28 && pos <= 34) {
                    start = 4;
                } else if (pos >= 35 && pos <= 41) {
                    start = 5;
                }
                pos = mCycles.get(2).second;
                if (pos >= 0 && pos <= 6) {
                    end = 0;
                } else if (pos >= 7 && pos <= 13) {
                    end = 1;
                } else if (pos >= 14 && pos <= 20) {
                    end = 2;
                } else if (pos >= 21 && pos <= 27) {
                    end = 3;
                } else if (pos >= 28 && pos <= 34) {
                    end = 4;
                } else if (pos >= 35 && pos <= 41) {
                    end = 5;
                }

                for (int i = 0; i < 6; i++){
                    if (i >= start && i <= end) {
                        cycles.add(mCycle += 0.1);
                    } else if (i >= three){
                        cycles.add(mCycle += 0.1);
                    } else {
                        cycles.add(-1.0);
                    }
                }

            } else if (mCycles.get(0).first == PatientModelHelper.EventList.END_CYCLE_DATE){

                int zero = 0;
                int start = 0;
                int end = 0;


                int pos = mCycles.get(0).second;

                if (pos >= 0 && pos <= 6) {
                    zero = 0;
                } else if (pos >= 7 && pos <= 13) {
                    zero = 1;
                } else if (pos >= 14 && pos <= 20) {
                    zero = 2;
                } else if (pos >= 21 && pos <= 27) {
                    zero = 3;
                } else if (pos >= 28 && pos <= 34) {
                    zero = 4;
                } else if (pos >= 35 && pos <= 41) {
                    zero = 5;
                }

                pos = mCycles.get(1).second;
                if (pos >= 0 && pos <= 6) {
                    start = 0;
                } else if (pos >= 7 && pos <= 13) {
                    start = 1;
                } else if (pos >= 14 && pos <= 20) {
                    start = 2;
                } else if (pos >= 21 && pos <= 27) {
                    start = 3;
                } else if (pos >= 28 && pos <= 34) {
                    start = 4;
                } else if (pos >= 35 && pos <= 41) {
                    start = 5;
                }
                pos = mCycles.get(2).second;
                if (pos >= 0 && pos <= 6) {
                    end = 0;
                } else if (pos >= 7 && pos <= 13) {
                    end = 1;
                } else if (pos >= 14 && pos <= 20) {
                    end = 2;
                } else if (pos >= 21 && pos <= 27) {
                    end = 3;
                } else if (pos >= 28 && pos <= 34) {
                    end = 4;
                } else if (pos >= 35 && pos <= 41) {
                    end = 5;
                }

                for (int i = 0; i < 6; i++){
                    if (i >= start && i <= end) {
                        cycles.add(mCycle += 0.1);
                    } else if (i <= zero){
                        cycles.add(-1.0);
                    } else {
                        cycles.add(-1.0);
                    }
                }

            } else if (mCycles.get(0).first == PatientModelHelper.EventList.OAK_DATE){
                int zero = 0;
                int end = 0;
                int start = 0;


                int pos = mCycles.get(0).second;

                if (pos >= 0 && pos <= 6) {
                    zero = 0;
                } else if (pos >= 7 && pos <= 13) {
                    zero = 1;
                } else if (pos >= 14 && pos <= 20) {
                    zero = 2;
                } else if (pos >= 21 && pos <= 27) {
                    zero = 3;
                } else if (pos >= 28 && pos <= 34) {
                    zero = 4;
                } else if (pos >= 35 && pos <= 41) {
                    zero = 5;
                }

                pos = mCycles.get(1).second;
                if (pos >= 0 && pos <= 6) {
                    end = 0;
                } else if (pos >= 7 && pos <= 13) {
                    end = 1;
                } else if (pos >= 14 && pos <= 20) {
                    end = 2;
                } else if (pos >= 21 && pos <= 27) {
                    end = 3;
                } else if (pos >= 28 && pos <= 34) {
                    end = 4;
                } else if (pos >= 35 && pos <= 41) {
                    end = 5;
                }
                pos = mCycles.get(2).second;
                if (pos >= 0 && pos <= 6) {
                    start = 0;
                } else if (pos >= 7 && pos <= 13) {
                    start = 1;
                } else if (pos >= 14 && pos <= 20) {
                    start = 2;
                } else if (pos >= 21 && pos <= 27) {
                    start = 3;
                } else if (pos >= 28 && pos <= 34) {
                    start = 4;
                } else if (pos >= 35 && pos <= 41) {
                    start = 5;
                }

                for (int i = 0; i < 6; i++){
                    if (i >= start && i <= end) {
                        cycles.add(mCycle += 0.1);
                    } else if (i <= zero){
                        cycles.add(-1.0);
                    } else {
                        cycles.add(-1.0);
                    }
                }
            }
        }
        if (update) {
            CyclesCounterHolder.instance().setCycleCounter(mCycle);
        }
        mCycleCountView.setAdapter(new CycleCountAdapter(getContext(), cycles));
    }

    private class CycleCountAdapter extends ArrayAdapter<Double> {

        private LayoutInflater inflater;

        private List<Double> mCycles;

        public CycleCountAdapter(Context context, List<Double> cycles) {
            super(context, R.layout.control_cycle_count, cycles);
            inflater = LayoutInflater.from(context);
            mCycles = cycles;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            try {
                // inflate item if it does not exist yet
                if (view == null)
                    view = inflater.inflate(R.layout.control_cycle_count, parent, false);

                // if this day has an event, specify event image
                view.setBackgroundResource(0);

                // set text
                if (mCycles.get(position) != null && mCycles.get(position) > 0) {
                    String digit = String.valueOf(mCycles.get(position));
                    ((TextView) view).setText(String.format(Locale.ROOT, "%s ( %s )", digit.charAt(0), digit.charAt(2)));
                } else {
                    ((TextView) view).setText("");
                }
                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;

        }
    }

}
