package ru.mbg.palbociclib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsDictionary {
    private final Map<Date, List<PatientProvider.EventRow>> eventsByDate;
    private final List<PatientProvider.EventRow> allEvents;

    public EventsDictionary() {
        eventsByDate = new HashMap<>();
        allEvents = new ArrayList<>();
    }

    public void addEvent(PatientProvider.EventRow event) {
        List<PatientProvider.EventRow> events = eventsByDate.get(event.date);
        if (events != null) {
            events.add(event);
        } else {
            ArrayList<PatientProvider.EventRow> e = new ArrayList<>();
            e.add(event);
            eventsByDate.put(event.date, e);
        }
        allEvents.add(event);
    }

    public List<PatientProvider.EventRow> getAllEvents() {
        return allEvents;
    }

    public List<PatientProvider.EventRow> getEventsFor(Date date) {
        return eventsByDate.get(date);
    }

    public void sort() {
        // eventsByDate не сортируем, так как непосредственного обращения к датам в нем нет,
        // а внутри дат сортировать нет смысла, там даты и так равны
        Collections.sort(allEvents);
    }

    public int size() {
        return allEvents.size();
    }

    public Date getFirstDate() {
        if (allEvents.size() > 0) {
            return allEvents.get(0).date;
        } else {
            return null;
        }
    }
}
