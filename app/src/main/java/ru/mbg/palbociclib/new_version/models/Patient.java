package ru.mbg.palbociclib.new_version.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ru.mbg.palbociclib.utils.Three;

@Entity(tableName = Patient.TABLE_NAME)
public class Patient implements Serializable{

    public static final String SERIALIZABLE_NAME = "patient";

    public static final String TABLE_NAME = "patients";
    public static final String CYCLE_COUNT_COLUMN = "cycle_count";
    public static final String TOXIC_LEVEL_COUNT = "toxic_level";
    public static final String CYCLE_START_DATE_COLUMN = "cycle_start_date";
    public static final String DOSE_COLUMN = "dose_column";

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = CYCLE_COUNT_COLUMN)
    private int cycleCount;

    @ColumnInfo(name = TOXIC_LEVEL_COUNT)
    private int toxicLevel;

    @ColumnInfo(name = CYCLE_START_DATE_COLUMN)
    private String cycleStartDate;

    @ColumnInfo(name = DOSE_COLUMN)
    private int dose;

    @Ignore
    private List<Three<Integer, Date, Integer>> mOaksDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }

    public int getToxicLevel() {
        return toxicLevel;
    }

    public void setToxicLevel(int toxicLevel) {
        this.toxicLevel = toxicLevel;
    }

    public String getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(String cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public List<Three<Integer, Date, Integer>> getOaksDate() {
        return mOaksDate;
    }

    public void setOaksDate(List<Three<Integer, Date, Integer>> oaksDate) {
        mOaksDate = oaksDate;
    }
}
