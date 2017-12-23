package ru.mbg.palbociclib.new_version.db.models.helpers;


import android.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.utils.DateUtils;
import ru.mbg.palbociclib.utils.Three;

public class PatientModelHelper {

    public static class EventList {

        public static final int START_CYCLE_DATE = 1;
        public static final int CYCLE_DATE = 2;
        public static final int END_CYCLE_DATE = 3;
        public static final int OAK_DATE = 4;
        public static final int PAUSE_START_DATE = 5;
        public static final int PAUSE_DATE = 6;
        public static final int PAUSE_END_DATE = 7;
        public static final int CYCLE_COUNTER = 8;
        public static final int EMPTY = 9;

    }

    public static List<Three<Integer, Date, Integer>> fillPatientActionDate(Patient patient){
        List<Three<Integer, Date, Integer>> actions = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        Date startCycleDate = DateUtils.getDate(patient.getCycleStartDate(), DateUtils.DEFAULT_DATE_PATTERN);
        calendar.setTime(startCycleDate);

        actions.add(new Three<Integer, Date, Integer>(EventList.START_CYCLE_DATE, calendar.getTime(), patient.getCycleCount()));

        for (int i = 1; i <= 20; i++){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (i != 14){
                actions.add(new Three<Integer, Date, Integer>(EventList.CYCLE_DATE, calendar.getTime(), patient.getCycleCount()));
            } else {
                actions.add(new Three<Integer, Date, Integer>(EventList.OAK_DATE, calendar.getTime(), patient.getCycleCount()));
            }
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        actions.add(new Three<Integer, Date, Integer>(EventList.END_CYCLE_DATE, calendar.getTime(), patient.getCycleCount()));

        for (int i = patient.getCycleCount(); i<12; i++){

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            actions.add(new Three<Integer, Date, Integer>(EventList.PAUSE_START_DATE, calendar.getTime(),  i + 1));

            for (int k = 1; k < 6; k++){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                actions.add(new Three<Integer, Date, Integer>(EventList.PAUSE_DATE, calendar.getTime(),  i + 1));
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            actions.add(new Three<Integer, Date, Integer>(EventList.PAUSE_END_DATE, calendar.getTime(),  i + 1));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            actions.add(new Three<Integer, Date, Integer>(EventList.START_CYCLE_DATE, calendar.getTime(), i + 1));

            for (int j = 1; j <= 20; j++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (j != 14) {
                    actions.add(new Three<Integer, Date, Integer>(EventList.CYCLE_DATE, calendar.getTime(), i + 1));
                } else {
                    actions.add(new Three<Integer, Date, Integer>(EventList.OAK_DATE, calendar.getTime(), i + 1));
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            actions.add(new Three<Integer, Date, Integer>(EventList.END_CYCLE_DATE, calendar.getTime(), i + 1));
        }

        return actions;
    }

    public static Pair<Integer, Integer> getDaysOfCycle(List<Three<Integer, Date, Integer>> actionDate){
        int counter = 0;
        int cycle = 0;
        Date today = new Date();
        if (actionDate == null || actionDate.isEmpty()){
            return new Pair<>(0, 0);
        }
        for (int i = actionDate.size() - 1; i>=0; i--){
            if (actionDate.get(i).second.before(today) || DateUtils.compareDate(actionDate.get(i).second, today)){
                if (actionDate.get(i).first != EventList.START_CYCLE_DATE){
                    counter++;
                    cycle = actionDate.get(i).third;
                } else {
                    if (DateUtils.compareDate(actionDate.get(i).second, today)){
                        return new Pair<>(1, actionDate.get(i).third);
                    } else {
                        break;
                    }
                }
            }
        }
        return new Pair<>(counter + 1, cycle);
    }

}
