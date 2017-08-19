package ru.mbg.palbociclib.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.Required;

public class Oak extends RealmObject {
    /// Дата назначения ОАК
    @Required
    private Date assignmentDate;
    /// Дата получения резульататов
    private Date readyDate = null;
    /// Лейкоциты
    private long leukocytes = 0;
    /// Нейтрофилы (в процентах)
    private double neutrophils = 0.0;
    /// Тромбоциты
    private long platelets = 0;
    /// Эритроциты
    private long erythrocytes = 0;
    /// Гемоглобин
    private long hemoglobin = 0;
    /// Грейд
    private int grade = 0;
    /// Лихорадка
    private boolean isFever = false;

    public Patient getPatient() {
        if (getTreatments() == null) {
            return null;
        }
        Treatment treatment = getTreatments().first();
        if (treatment == null) {
            return null;
        }
        return treatment.getPatient();
    }

    @LinkingObjects("oaks")
    private final RealmResults<Treatment> treatments = null;

    public String description() {
        // TODO: Форматирование чисел
        double neutrophilis = getLeukocytes() * getNeutrophils();
        return "Нейтрофилы "+neutrophilis+"мл\nТромбоциты "+ getPlatelets() +"/мл";
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public long getLeukocytes() {
        return leukocytes;
    }

    public void setLeukocytes(long leukocytes) {
        this.leukocytes = leukocytes;
    }

    public double getNeutrophils() {
        return neutrophils;
    }

    public void setNeutrophils(double neutrophils) {
        this.neutrophils = neutrophils;
    }

    public long getPlatelets() {
        return platelets;
    }

    public void setPlatelets(long platelets) {
        this.platelets = platelets;
    }

    public long getErythrocytes() {
        return erythrocytes;
    }

    public void setErythrocytes(long erythrocytes) {
        this.erythrocytes = erythrocytes;
    }

    public long getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(long hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isFever() {
        return isFever;
    }

    public void setFever(boolean fever) {
        isFever = fever;
    }

    public RealmResults<Treatment> getTreatments() {
        return treatments;
    }
}
