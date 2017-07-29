package ru.mbg.palbociclib;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.Appointment;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Patient;
import ru.mbg.palbociclib.models.Treatment;

public class PatientProvider {
    private Realm realm;
    private DateHelper dateHelper;

    public PatientProvider() {
        realm = Realm.getDefaultInstance();
        dateHelper = DateHelper.instance;
    }

    public PatientProvider(Realm realm, DateHelper dateHelper) {
        this.realm = realm;
        this.dateHelper = dateHelper;
    }

    public EventsDictionary getEventsFor(Date month) {
        Date startDate, endDate;
        Calendar cal = Calendar.getInstance();
        if (month != null) {
            cal.setTime(month);
            cal.set(Calendar.DAY_OF_MONTH, 1); // Первое число месяца, 00:00
            startDate = cal.getTime();
            cal.add(Calendar.MONTH, 1); // Последнее число месяца, 00:00
            endDate = cal.getTime();
        } else {
            startDate = dateHelper.currentDate();
            cal.set(2100, 1, 1);
            endDate = cal.getTime();
        }

        EventsDictionary events = new EventsDictionary();

        // Анализы
        for (Oak oak : realm.where(Oak.class).between("assignmentDate", startDate, endDate).findAll()) {
            events.addEvent(new EventRow(oak.getAssignmentDate(), oak.getPatient().getName(), State.assignedOak));
        }

        // Приемы пациента
        for (Appointment appointment : realm.where(Appointment.class).between("date", startDate, endDate).findAll()) {
            events.addEvent(new EventRow(appointment.getDate(), appointment.getPatient().getName(), State.assignedAppointment));
        }

        // Циклы лечения
        for (Treatment treatment : realm.where(Treatment.class).notEqualTo("cycleNumber",0).between("startDate", startDate, endDate).findAll()) {
            events.addEvent(new EventRow(treatment.getStartDate(), treatment.getPatient().getName(), State.nextTreatment, treatment.getCycleNumber()));
        }

        // Следующие циклы лечения
        for (Patient patient : realm.where(Patient.class).findAll()) {
            if (patient.getTreatments().size() < 2) {
                continue;
            }
            Treatment currentTreatment = patient.getTreatments().last();
            Date nextTreatmentDate = DateHelper.advancingDays(currentTreatment.getStartDate(), 29);
            if (nextTreatmentDate.getTime() >= startDate.getTime() && nextTreatmentDate.getTime() <= endDate.getTime()) {
                int nextTreatmentNumber = currentTreatment.getCycleNumber() + 1;
                events.addEvent(new EventRow(nextTreatmentDate, currentTreatment.getPatient().getName(), State.nextTreatment, nextTreatmentNumber));
            }
        }

        // Приостановки приема лекарств
        for (Treatment treatment : realm.where(Treatment.class).notEqualTo("cycleNumber",0).between("pauseDate", startDate, endDate).findAll()) {
            events.addEvent(new EventRow(treatment.getPauseDate(), treatment.getPatient().getName(), State.pauseTreatment));
        }

        // Возобновления приема лекарств
        for (Treatment treatment : realm.where(Treatment.class).notEqualTo("cycleNumber",0).between("continueDate", startDate, endDate).findAll()) {
            events.addEvent(new EventRow(treatment.getContinueDate(), treatment.getPatient().getName(), State.continueTreatment));
        }

        events.sort();

        return events;
    }

    public class EventRow implements Comparable<EventRow> {
        public final Date date;
        public final String name;
        public final State state;
        public final int cycleNumber;

        EventRow(Date date, String name, State state) {
            this(date, name, state, 0);
        }

        EventRow(Date date, String name, State state, int cycleNumber) {
            this.date = date;
            this.name = name;
            this.state = state;
            this.cycleNumber = cycleNumber;
        }

        @Override
        public int compareTo(@NonNull EventRow eventRow) {
            return this.date.compareTo(eventRow.date);
        }

        public String description() {
            switch (state) {
                case assignedOak:
                    return "Сдача ОАК";
                case assignedAppointment:
                    return "Назначен прием";
                case nextTreatment:
                    return "Начало " + cycleNumber + "-го цикла";
                case pauseTreatment:
                    return "Начало паузы";
                case continueTreatment:
                    return "Прекращение приема";
            }
            return null;
        }

        public int getImageColor() {
            switch (cycleNumber) {
                case 1:
                    return R.color.cycle1_color;
                case 2:
                    return R.color.cycle2_color;
                default:
                    return R.color.cycle_default_color;
            }
        }
    }

    public enum State {
        assignedOak,
        assignedAppointment,
        nextTreatment,
        pauseTreatment,
        continueTreatment;

        public int getImageResource() {
            switch (this) {
                case assignedOak:
                    return R.drawable.raindrop;
                case assignedAppointment:
                    return R.drawable.raindrop;
                case nextTreatment:
                    return R.drawable.cycle;
                case pauseTreatment:
                    return R.drawable.pause;
                case continueTreatment:
                    return R.drawable.pause;
            }
            return 0;
        }

        public int getCardColor() {
            switch (this) {
                case assignedOak:
                    return R.color.state_oak_color;
                case assignedAppointment:
                    return R.color.state_appointment_color;
                case nextTreatment:
                    return R.color.state_treatment_color;
                case pauseTreatment:
                    return R.color.state_pause_color;
                case continueTreatment:
                    return R.color.state_continue_color;
            }
            return 0;
        }
    }
}
