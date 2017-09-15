package ru.mbg.palbociclib.new_version.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.mbg.palbociclib.new_version.db.DatabaseHelper;
import ru.mbg.palbociclib.new_version.db.models.Event;

@Dao
public interface EventDao {

    @Query("SELECT * FROM "  + Event.TABLE_NAME)
    List<Event> findAll();

    @Query(DatabaseHelper.GET_EVENTS_BY_PATENT_ID)
    List<Event> findAllByPatientId(int patientId);

    @Query(DatabaseHelper.GET_EVENT_BY_DATE_AND_PATIENT)
    Event findByDateAndPatient(String date, long patientId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Event event);

    @Delete
    void delete(Event event);

    @Update
    void update(Event event);

    @Query(DatabaseHelper.DELETE_PATIENT_EVENT)
    void deleteWithId(long patientId);

}
