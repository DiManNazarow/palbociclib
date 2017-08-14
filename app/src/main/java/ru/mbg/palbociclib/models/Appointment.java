package ru.mbg.palbociclib.models;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.Required;

public class Appointment extends RealmObject {
    @Required
    private Date date;
    private int state = AppointmentState.oakNeeded.rawValue;

    public Patient getPatient() {
        if (getPatients() == null) {
            return null;
        }
        return getPatients().first();
    }

    public Appointment(){
        date = Calendar.getInstance().getTime();
    }

    @LinkingObjects("appointments")
    private final RealmResults<Patient> patients = null;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmResults<Patient> getPatients() {
        return patients;
    }

    public AppointmentState getState() {
        return AppointmentState.fromRawValue(state);
    }

    public void setState(AppointmentState state) {
        this.state = state.rawValue;
    }
}
