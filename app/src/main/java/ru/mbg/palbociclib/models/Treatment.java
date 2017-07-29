package ru.mbg.palbociclib.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.Required;

public class Treatment extends RealmObject {
    /// Дата начала цикла
    @Required
    private Date startDate;
    /// Номер цикла
    private int cycleNumber = 0;
    /// Доза препарата в этом цикле
    private int dose = TreatmentDose.none.rawValue;
    /// Дата начала приостановки приема
    private Date pauseDate = null;
    /// Дата возобновления приема препарата
    private Date continueDate = null;
    /// Анализы
    private RealmList<Oak> oaks;

    public Patient getPatient() {
        if (getPatients() == null) {
            return null;
        }
        return getPatients().first();
    }

    @LinkingObjects("treatments")
    private final RealmResults<Patient> patients = null;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public TreatmentDose getDose() {
        return TreatmentDose.fromRawValue(dose);
    }

    public void setDose(TreatmentDose dose) {
        this.dose = dose.rawValue;
    }

    public Date getPauseDate() {
        return pauseDate;
    }

    public void setPauseDate(Date pauseDate) {
        this.pauseDate = pauseDate;
    }

    public Date getContinueDate() {
        return continueDate;
    }

    public void setContinueDate(Date continueDate) {
        this.continueDate = continueDate;
    }

    public RealmList<Oak> getOaks() {
        return oaks;
    }

    public void setOaks(RealmList<Oak> oaks) {
        this.oaks = oaks;
    }

    public RealmResults<Patient> getPatients() {
        return patients;
    }
}
