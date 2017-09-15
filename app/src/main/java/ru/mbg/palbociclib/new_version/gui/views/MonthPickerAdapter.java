package ru.mbg.palbociclib.new_version.gui.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mbg.palbociclib.R;

public class MonthPickerAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;

    private List<Date> months;

    private String[] monthsRes;

    private Calendar calendar = Calendar.getInstance();

    public MonthPickerAdapter(@NonNull Context context, @NonNull List<Date> objects) {
        super(context, R.layout.calendar_spiner_item);
        inflater = LayoutInflater.from(context);
        months = objects;
        monthsRes = context.getResources().getStringArray(R.array.months);
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null)
            view = inflater.inflate(R.layout.calendar_spiner_item, parent, false);

        calendar.setTime(months.get(position));

        String item = String.format(Locale.getDefault(), "%s %d", monthsRes[calendar.get(Calendar.MONTH)], calendar.get(Calendar.YEAR));

        ((TextView)view).setText(item);

        return view;
    }

    @Override
    public String getItem(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(months.get(position));
        return String.format(Locale.getDefault(), "%s %d", monthsRes[calendar.get(Calendar.MONTH)], calendar.get(Calendar.YEAR));
    }

    public Date getDate(int position){
        return months.get(position);
    }

    @Override
    public int getCount(){
        return months.size();
    }
}
