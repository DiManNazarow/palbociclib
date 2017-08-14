package ru.mbg.palbociclib.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.Required;

public class BackgroundTherapy extends RealmObject {
    private int type = BackgroundTherapyType.none.rawValue;
    @Required
    private Date date;

    @LinkingObjects("backgroundTherapy")
    private final RealmResults<Patient> mPatients = null;

    public Patient getPatient() {
        if (getPatients() == null){
            return null;
        }
        return getPatients().first();
    }

    public RealmResults<Patient> getPatients() {
        return mPatients;
    }

    public BackgroundTherapyType getType() {
        return BackgroundTherapyType.fromRawValue(type);
    }

    public void setType(BackgroundTherapyType type) {
        this.type = type.rawValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
