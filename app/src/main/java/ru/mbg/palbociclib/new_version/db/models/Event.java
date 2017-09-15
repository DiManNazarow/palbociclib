package ru.mbg.palbociclib.new_version.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.util.TableInfo;

import java.io.Serializable;

@Entity(tableName = Event.TABLE_NAME /*, foreignKeys = @ForeignKey(entity = Patient.class, parentColumns = "id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE)*/)
public class Event implements Serializable {

    public static final String TABLE_NAME = "events";

    public static final String DATE_COLUMN_NAME = "event_date";

    public static final String PATIENT_COLUMN_NAME = "patient_id";

    public static final String TOXIC_LEVEL_COUNT = "toxic_level";

    public static final String DOSE_COLUMN = "dose_column";

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DATE_COLUMN_NAME)
    private String eventDate;

    @ColumnInfo(name = PATIENT_COLUMN_NAME)
    private long patientId;

    @ColumnInfo(name = TOXIC_LEVEL_COUNT)
    private int toxicLevel;

    @ColumnInfo(name = DOSE_COLUMN)
    private int dose;

    @Ignore
    private int cycle;

    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public int getToxicLevel() {
        return toxicLevel;
    }

    public void setToxicLevel(int toxicLevel) {
        this.toxicLevel = toxicLevel;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
