package ru.mbg.palbociclib.new_version.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.calendar.viewholders.ColumnNameViewHolder;
import ru.mbg.palbociclib.new_version.calendar.viewholders.MonthNameViewHolder;
import ru.mbg.palbociclib.new_version.calendar.viewholders.MonthViewHolder;
import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.utils.DateUtils;


public class CalendarMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MONTH_NAME_VIEW_TYPE = 1;
    private static final int MONTH_VIEW_TYPE = 2;

    private static final int ITEM_COUNT = 3;

    private Context mContext;

    private Patient mPatient;

    private Date nextMonth;

    private Date currentMonth;

    public CalendarMonthAdapter(Context context, Patient patient) {
        mContext = context;
        mPatient = patient;
        currentMonth = DateUtils.getCurrentDate();
        nextMonth = DateUtils.increaseMonth(currentMonth);
    }

    public CalendarMonthAdapter(Context context, Date currentMonth, Patient patient) {
        mContext = context;
        mPatient = patient;
        this.currentMonth = currentMonth;
        nextMonth = DateUtils.increaseMonth(currentMonth);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case MONTH_NAME_VIEW_TYPE: return new MonthNameViewHolder(parent.getContext(), parent);
            case MONTH_VIEW_TYPE: return new MonthViewHolder(parent.getContext(), parent);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null){
            switch (position){
                case 0:{
                    ((MonthViewHolder)holder).setup(mPatient, currentMonth);
                    break;
                }
                case 1:{
                    String[] months = mContext.getResources().getStringArray(R.array.months);
                    int month = DateUtils.getMonth(currentMonth);
                    if (month == 11){
                        month = -1;
                    }
                    ((MonthNameViewHolder)holder).setMonthName(months[month + 1]);
                    break;
                }
                case 2:{
                    ((MonthViewHolder)holder).setup(mPatient, nextMonth, 1);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0: return MONTH_VIEW_TYPE;
            case 1: return MONTH_NAME_VIEW_TYPE;
            case 2: return MONTH_VIEW_TYPE;
            default: return -1;
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

}
