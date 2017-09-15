package ru.mbg.palbociclib.new_version.calendar.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.views.CalendarView;

public class MonthViewHolder extends RecyclerView.ViewHolder {

    public MonthViewHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_month_view_holder, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    public void setListeners(AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemLongClickListener onItemLongClickListener){
        ((CalendarView)itemView).setListeners(onItemLongClickListener, onItemClickListener);
    }

    public void setup(Patient patient, Date nextMonth){
        ((CalendarView)itemView).setCalendarData(patient, nextMonth);
    }

    public void setup(Patient patient, Date nextMonth, int cycleIncreaseSize){
        ((CalendarView)itemView).setCalendarData(patient, nextMonth, cycleIncreaseSize);
    }
}
