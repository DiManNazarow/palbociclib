package ru.mbg.palbociclib.new_version.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mbg.palbociclib.new_version.calendar.viewholders.MonthViewHolder;
import ru.mbg.palbociclib.new_version.models.Patient;
import ru.mbg.palbociclib.utils.DateUtils;


public class CalendarMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MONTH_NAME_VIEW_TYPE = 1;
    private static final int MONTH_VIEW_TYPE = 2;

    private static final int ITEM_COUNT = 6;

    private Context mContext;

    private Patient mPatient;

    private Date nextMonth;

    private Date currentMonth;

    boolean dateStartBeforeToday = false;

    private List<MonthViewHolder> holders;

    public CalendarMonthAdapter(Context context, Patient patient) {
        mContext = context;
        mPatient = patient;
        //currentMonth = DateUtils.getCurrentDate();
        currentMonth = DateUtils.getDate(mPatient.getCycleStartDate(), DateUtils.DEFAULT_DATE_PATTERN);
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
        return new MonthViewHolder(parent.getContext(), parent);
//        switch (viewType){
//            case MONTH_NAME_VIEW_TYPE: return new MonthNameViewHolder(parent.getContext(), parent);
//            case MONTH_VIEW_TYPE: return new MonthViewHolder(parent.getContext(), parent);
//            default: return null;
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null){
            if (position == 0){
                ((MonthViewHolder)holder).setup(mPatient, currentMonth);
            } else {
                ((MonthViewHolder) holder).setup(mPatient, DateUtils.increaseMonth(currentMonth, position));
            }
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        switch (position){
//            case 0: return MONTH_VIEW_TYPE;
//            case 1: return MONTH_NAME_VIEW_TYPE;
//            case 2: return MONTH_VIEW_TYPE;
//            default: return -1;
//        }
//    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    private void createViewHolders(RecyclerView recyclerView){
        holders = new ArrayList<>();
        for (int i = 0; i<ITEM_COUNT; i++){
            holders.add(new MonthViewHolder(recyclerView.getContext(), recyclerView));
        }
    }

}
