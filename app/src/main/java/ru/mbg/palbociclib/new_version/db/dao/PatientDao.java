package ru.mbg.palbociclib.new_version.db.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.mbg.palbociclib.new_version.db.DatabaseHelper;
import ru.mbg.palbociclib.new_version.db.models.Patient;


@Dao
public interface PatientDao {

    @Query("SELECT * FROM " + Patient.TABLE_NAME)
    List<Patient> getAll();

    @Query(DatabaseHelper.GET_BY_START_CYCLE_DATE_AND_CYCLE_COUNT_QUERY)
    Patient findByDateAndCycle(String date, int cycle);

    @Query(DatabaseHelper.GET_PATIENT_BY_ID)
    Patient findPatientById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Patient patient);

    @Delete
    void delete(Patient patient);

    @Update
    void update(Patient patient);
}
