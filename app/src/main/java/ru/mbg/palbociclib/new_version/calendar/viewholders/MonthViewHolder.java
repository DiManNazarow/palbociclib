package ru.mbg.palbociclib.new_version.calendar.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.models.Patient;
import ru.mbg.palbociclib.utils.DateUtils;
import ru.mbg.palbociclib.new_version.gui.views.CalendarView;

public class MonthViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.month_text_view)
    protected TextView mMonthNameTextView;
    @BindView(R.id.calendar_view_1)
    protected CalendarView mCalendarView;

    public MonthViewHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_month_view_holder, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    public void setListeners(AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemLongClickListener onItemLongClickListener){
        ((CalendarView)itemView).setListeners(onItemLongClickListener, onItemClickListener);
    }

    public void setup(Patient patient, Date nextMonth){
        mCalendarView.setCalendarData(patient, nextMonth);
        String[] months = itemView.getContext().getResources().getStringArray(R.array.months);
        int month = DateUtils.getMonth(nextMonth);
        mMonthNameTextView.setText(months[month]);

    }

    public void setup(Patient patient, Date nextMonth, int cycleIncreaseSize){
        mCalendarView.setCalendarData(patient, nextMonth, cycleIncreaseSize);
        String[] months = itemView.getContext().getResources().getStringArray(R.array.months);
        int month = DateUtils.getMonth(nextMonth);
        mMonthNameTextView.setText(months[month]);
    }
}
