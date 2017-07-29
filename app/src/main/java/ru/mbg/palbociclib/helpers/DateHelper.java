package ru.mbg.palbociclib.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {
    public Date mockDate;

    public static final DateHelper instance = new DateHelper();

    public Date currentDate() {
        if (mockDate != null) {
            return mockDate;
        }

        return strippingTime(new Date());
    }

    public static Date advancingDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date strippingTime(Date date) {
        final long LENGTH_OF_DAY = 24*60*60*1000;
        Calendar datetime = Calendar.getInstance();
        datetime.setTime(date);
        long millis = datetime.getTimeInMillis();
        long offset = datetime.getTimeZone().getOffset(millis);
        millis = millis - ((millis + offset) % LENGTH_OF_DAY);
        datetime.setTimeInMillis(millis);
        return datetime.getTime();
    }

    public int weeksFrom(Date date) {
        int days = -daysTo(date);
        return days / 7 + 1;
    }

    public int daysTo(Date date) {
        long diff = date.getTime() - currentDate().getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public int daysFrom(Date fromDate, Date toDate) {
        long diff = toDate.getTime() - fromDate.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public boolean isToday(Date date) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate());
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
