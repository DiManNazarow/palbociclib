package ru.mbg.palbociclib;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import ru.mbg.palbociclib.helpers.DateHelper;

import static org.junit.Assert.*;


public class DateHelperTests {
    @Test
    public void weeksFromCurrentDate() {
        DateHelper helper = new DateHelper();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1);
        Date startDate = calendar.getTime();
        helper.mockDate = startDate;

        Date current = helper.currentDate();

        assertEquals(1, helper.weeksFrom(current));

        helper.mockDate = DateHelper.advancingDays(startDate, 6);
        assertEquals(1, helper.weeksFrom(current));

        helper.mockDate = DateHelper.advancingDays(startDate, 7);
        assertEquals(2, helper.weeksFrom(current));

        helper.mockDate = DateHelper.advancingDays(startDate, 14);
        assertEquals(3, helper.weeksFrom(current));
    }

    @Test
    public void daysToDate() {
        DateHelper helper = new DateHelper();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1);
        Date startDate = calendar.getTime();
        helper.mockDate = startDate;

        Date current = helper.currentDate();

        assertEquals(0, helper.daysTo(current));
        assertEquals(1, helper.daysTo(DateHelper.advancingDays(current, 1)));
        assertEquals(14, helper.daysTo(DateHelper.advancingDays(current, 14)));
    }
}