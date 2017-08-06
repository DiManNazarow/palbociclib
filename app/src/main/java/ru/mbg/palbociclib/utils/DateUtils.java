package ru.mbg.palbociclib.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy";

    public static void showDatePickerDialog(Context context, @Nullable final DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        try {
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        } catch (Exception ignored) {

        }
        dialog.show();
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return format(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String format(long time, String pattern) {
        Date d = new Date(time);
        return new SimpleDateFormat(pattern).format(d);
    }

    public static String format(Date date, String pattern){
        return  new SimpleDateFormat(pattern).format(date);
    }

    public static String format(int year, int month, int dayOfMonth){
        if ((month + 1) < 10) {
            return String.format(Locale.getDefault(), "%s.0%s.%s", year, month + 1, dayOfMonth);
        } else {
            return String.format(Locale.getDefault(), "%s.%s.%s", year, month + 1, dayOfMonth);
        }
    }

}
