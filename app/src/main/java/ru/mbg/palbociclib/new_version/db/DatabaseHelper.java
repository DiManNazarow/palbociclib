package ru.mbg.palbociclib.new_version.db;

import android.content.Context;
import android.os.AsyncTask;

import java.io.Serializable;
import java.util.List;

import ru.mbg.palbociclib.App;
import ru.mbg.palbociclib.new_version.db.models.Event;
import ru.mbg.palbociclib.new_version.db.models.Patient;


public class DatabaseHelper {

    public static final String GET_BY_START_CYCLE_DATE_AND_CYCLE_COUNT_QUERY = "SELECT * FROM " + Patient.TABLE_NAME
            + " WHERE " + Patient.CYCLE_START_DATE_COLUMN + " = :date"
            + " AND " + Patient.CYCLE_COUNT_COLUMN + " = :cycle";

    public static final String GET_EVENTS_BY_PATENT_ID = "SELECT * FROM " + Event.TABLE_NAME
            + " WHERE " + Event.PATIENT_COLUMN_NAME + " = :patientId";

    public static final String GET_EVENT_BY_DATE_AND_PATIENT = "SELECT * FROM " + Event.TABLE_NAME
            + " WHERE " + Event.DATE_COLUMN_NAME + " = :date"
            + " AND " + Event.PATIENT_COLUMN_NAME + " = :patientId";

    public static final String GET_PATIENT_BY_ID = "SELECT * FROM " + Patient.TABLE_NAME
            + " WHERE id = :id";

    public static final String DELETE_EVENT = "DELETE FROM " + Event.TABLE_NAME
            + " WHERE " + Event.DATE_COLUMN_NAME + " = :date";

    public static final String DELETE_PATIENT_EVENT = "DELETE FROM " + Event.TABLE_NAME
            + " WHERE " + Event.PATIENT_COLUMN_NAME + " = :patientId";

    public static long insertPatient(final Context context, Patient patient, final ReadyCallback readyCallback){
        return ((App)context.getApplicationContext()).getAppDatabase().patientDao().insert(patient);
//        new AsyncTask<Patient, Void, Void>(){
//            @Override
//            protected Void doInBackground(Patient[] patients) {
//                ((App)context.getApplicationContext()).getAppDatabase().patientDao().insert(patients[0]);
//                if (readyCallback != null){
//                    readyCallback.onActionDone();
//                }
//                return null;
//            }
//        }.execute(patient);
    }

    public static void insertEvent(final Context context, Event event, final ReadyCallback readyCallback){
        ((App)context.getApplicationContext()).getAppDatabase().eventDao().insert(event);
//        new AsyncTask<Event, Void, Void>(){
//            @Override
//            protected Void doInBackground(Event[] patients) {
//                ((App)context.getApplicationContext()).getAppDatabase().eventDao().insert(patients);
//                if (readyCallback != null){
//                    readyCallback.onActionDone();
//                }
//                return null;
//            }
//        }.execute(event);
    }

    public static List<Event> getAllEvents(Context context){
        return ((App)context.getApplicationContext()).getAppDatabase().eventDao().findAll();
    }

    public static void deleteEvent(final Context context, final Event event){
        new AsyncTask<Event, Void, Void>(){
            @Override
            protected Void doInBackground(Event[] patients) {
                ((App)context.getApplicationContext()).getAppDatabase().eventDao().delete(event);
//                if (readyCallback != null){
//                    readyCallback.onActionDone();
//                }
                return null;
            }
        }.execute(event);
    }

    public static Patient findByDateAndCycle(final Context context, String date, int cycle){
        return ((App)context.getApplicationContext()).getAppDatabase().patientDao().findByDateAndCycle(date, cycle);
//        Arguments arguments = new Arguments(date, cycle);
//        try {
//            return new AsyncTask<Arguments, Void, Patient>(){
//                @Override
//                protected Patient doInBackground(Arguments... arguments) {
//                    return ((App)context.getApplicationContext()).getAppDatabase().patientDao().findByDateAndCycle(arguments[0].arg1, arguments[0].arg2);
//                }
//            }.execute(arguments).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

//    public static Event findEventByDate(final Context context, String date){
//        return ((App)context.getApplicationContext()).getAppDatabase().eventDao().findByDateAndPatient(date);
////        try {
//            return new AsyncTask<Arguments, Void, Event>(){
//                @Override
//                protected Event doInBackground(Arguments... arguments) {
//                    return ((App)context.getApplicationContext()).getAppDatabase().eventDao().findByDateAndPatient(arguments[0].arg1, arguments[0].arg2);
//                }
//            }.execute(arguments).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }


    private static class Arguments implements Serializable {
        public String arg1;
        public int arg2;

        Arguments(String arg1, int arg2) {
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    public interface ReadyCallback {
        void onActionDone();
    }

}
