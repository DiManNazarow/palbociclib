package ru.mbg.palbociclib;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import ru.mbg.palbociclib.helpers.DateHelper;

import static org.junit.Assert.assertEquals;


public class DateAdvanceTests {
    @Test
    public void dateAdvanceZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1);
        Date sut = DateHelper.advancingDays(calendar.getTime(), 0);

        calendar.setTime(sut);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void dateAdvanceTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1);
        Date sut = DateHelper.advancingDays(calendar.getTime(), 1);

        calendar.setTime(sut);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void dateAdvanceWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1);
        Date sut = DateHelper.advancingDays(calendar.getTime(), 7);

        calendar.setTime(sut);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(8, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void dateAdvanceChangeMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 0, 25);
        Date sut = DateHelper.advancingDays(calendar.getTime(), 7);

        calendar.setTime(sut);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}