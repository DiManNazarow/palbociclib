package ru.mbg.palbociclib.new_version.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.mbg.palbociclib.new_version.db.dao.EventDao;
import ru.mbg.palbociclib.new_version.db.dao.PatientDao;
import ru.mbg.palbociclib.new_version.db.models.Event;
import ru.mbg.palbociclib.new_version.db.models.Patient;

@Database(entities = {Patient.class, Event.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PatientDao patientDao();

    public abstract EventDao eventDao();

}
