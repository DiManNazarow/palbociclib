package ru.mbg.palbociclib.models;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Patient extends RealmObject {
    /// Идентификатор пациента
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    /// ФИО
    @Required
    private String name;
    private int menopause = Menopause.none.rawValue;
    /// Была гормональная терапия
    private boolean wasHormonalTherapy = false;
    /// Фоновая терапиия
    private BackgroundTherapy backgroundTherapy = null;

    /// Проведенные и назначенные приемы врачом
    private RealmList<Appointment> appointments;

    /// Циклы лечения
    private RealmList<Treatment> treatments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Menopause getMenopause() {
        return Menopause.fromRawValue(menopause);
    }

    public void setMenopause(Menopause menopause) {
        this.menopause = menopause.rawValue;
    }

    public boolean isWasHormonalTherapy() {
        return wasHormonalTherapy;
    }

    public void setWasHormonalTherapy(boolean wasHormonalTherapy) {
        this.wasHormonalTherapy = wasHormonalTherapy;
    }

    public BackgroundTherapy getBackgroundTherapy() {
        return backgroundTherapy;
    }

    public void setBackgroundTherapy(BackgroundTherapy backgroundTherapy) {
        this.backgroundTherapy = backgroundTherapy;
    }

    public RealmList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(RealmList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public RealmList<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(RealmList<Treatment> treatments) {
        this.treatments = treatments;
    }
}