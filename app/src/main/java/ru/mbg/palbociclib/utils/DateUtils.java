package ru.mbg.palbociclib.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.mbg.palbociclib.R;

public class DateUtils {

    public static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy";

    public static void showDatePickerDialog(Context context, @Nullable final DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
//        try {
//            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//        } catch (Exception ignored) {
//
//        }
        dialog.show();
    }

    public static void showDatePickerDialog(Context context, @NonNull String title, @Nullable final DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(title);
        setDatePickerHeaderBackgroundColor(dialog, ContextCompat.getColor(context, R.color.green));
        try {
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        } catch (Exception ignored) {

        }
        dialog.show();
    }

    private static void setDatePickerHeaderBackgroundColor(DatePickerDialog dpd, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mDatePickerField;
                mDatePickerField = DatePickerDialog.class.getDeclaredField("mDatePicker");
                mDatePickerField.setAccessible(true);
                final DatePicker mDatePicker = (DatePicker) mDatePickerField.get(dpd);

                int headerId = Resources.getSystem().getIdentifier("date_picker_header", "id", "android");
                final View header = mDatePicker.findViewById(headerId);
                View v = mDatePicker.getRootView();
                header.setBackgroundColor(color);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCurrentDateString(){
        Calendar calendar = Calendar.getInstance();
        return format(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static int getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Date increaseMonth(Date current){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date getCurrentDate(Date fromDate, int offset){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(fromDate);
        calendar1.add(Calendar.DAY_OF_MONTH, offset);
        return calendar1.getTime();
    }

    public static int[] getCurrentDate(String date){
        int[] dateParts = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date, DEFAULT_DATE_PATTERN));
        dateParts[0] = calendar.get(Calendar.DAY_OF_MONTH);
        dateParts[1] = calendar.get(Calendar.MONTH);
        dateParts[2] = calendar.get(Calendar.YEAR);
        return dateParts;
    }

    public static Date getCurrentDate(int offset){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return calendar.getTime();
    }

    public static Date getDate(String date, String pattern){
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDate(int year, int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    public static String format(long time, String pattern) {
        Date d = new Date(time);
        return new SimpleDateFormat(pattern).format(d);
    }

    public static String format(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }

    public static boolean compareDate(Date first, Date second){
        Calendar firstC = Calendar.getInstance();
        firstC.setTime(first);
        Calendar secondD = Calendar.getInstance();
        secondD.setTime(second);
        int day1 = firstC.get(Calendar.DAY_OF_MONTH);
        int month1 = firstC.get(Calendar.MONTH);
        int year1 = firstC.get(Calendar.YEAR);
        int day2 = secondD.get(Calendar.DAY_OF_MONTH);
        int month2 = secondD.get(Calendar.MONTH);
        int year2 = secondD.get(Calendar.YEAR);
        return day1 == day2 && month1 == month2 && year1 == year2;
    }

    public static String format(int year, int month, int dayOfMonth){
        if ((month + 1) < 10) {
            return String.format(Locale.getDefault(), "%s.0%s.%s", dayOfMonth, month + 1, year);
        } else {
            return String.format(Locale.getDefault(), "%s.%s.%s", dayOfMonth, month + 1, year);
        }
    }

}
