package ru.mbg.palbociclib.new_version.gui.delegate;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import ru.mbg.palbociclib.App;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.db.models.Dose;
import ru.mbg.palbociclib.new_version.db.models.Event;
import ru.mbg.palbociclib.new_version.db.models.Patient;
import ru.mbg.palbociclib.utils.DateUtils;

public class SetDoseActivityDelegate {

    private Patient mPatient;

    private Event mEvent;

    public Patient getPatient() {
        return mPatient;
    }

    public void setPatient(Patient patient) {
        mPatient = patient;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }

    public String getTitle(Context context, String date){
        try {
            String[] months = context.getResources().getStringArray(R.array.months_data);
            int[] datePart = DateUtils.getCurrentDate(date);
            return String.format(Locale.getDefault(), "%d %s %d", datePart[0], months[datePart[1]], datePart[2]);
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public void setUi(Toolbar toolbar, View.OnClickListener onClickListener){
        toolbar.setTitleTextColor(ContextCompat.getColor(toolbar.getContext(), R.color.white));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    public void setGradeUi(Button continueButton, Button stopAppointmentButton){
        if (mEvent.getCycle() == 1){
            if (mEvent.getCount() == 1){
                if (mEvent.getToxicLevel() < 3){
                    stopAppointmentButton.setEnabled(false);
                    continueButton.setText(R.string.new_version_start_125);
                    continueButton.setEnabled(true);
                    mPatient.setDose(Dose.DOSE_125.getDose());
                    mEvent.setDose(Dose.DOSE_125.getDose());
                } else {
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(R.string.new_version_start_125);
                    continueButton.setEnabled(false);
                    mPatient.setDose(Dose.DOSE_125.getDose());
                    mEvent.setDose(Dose.DOSE_125.getDose());
                }
            } else if (mEvent.getCount() == 14){
                if (mEvent.getToxicLevel() >= 4){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    mEvent.setDose(mPatient.getDose());
                } else {
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    continueButton.setEnabled(true);
                    stopAppointmentButton.setEnabled(false);
                }
            } else if (mEvent.getCount() == 21){
                if (mEvent.getToxicLevel() >= 4){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    mEvent.setDose(mPatient.getDose());
                } else {
                    stopAppointmentButton.setEnabled(false);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                }
            }
        } else if (mEvent.getCycle() == 2){
            if (mEvent.getCount() == 1){
                if (mEvent.getToxicLevel() <= 2){
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    continueButton.setEnabled(true);
                    stopAppointmentButton.setEnabled(false);
                } else if (mEvent.getToxicLevel() == 3){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    mEvent.setDose(mPatient.getDose());
                } else if (mEvent.getToxicLevel() > 3){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    mEvent.setDose(mPatient.getDose());
                }
            } else if (mEvent.getCount() == 14){
                if (mEvent.getToxicLevel() >= 4){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    mEvent.setDose(mPatient.getDose());
                } else {
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    continueButton.setEnabled(true);
                    stopAppointmentButton.setEnabled(false);
                }
            } else if (mEvent.getCount() == 21){
                if (mEvent.getToxicLevel() < 4){
                    stopAppointmentButton.setEnabled(false);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    continueButton.setEnabled(true);
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                } else {
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    stopAppointmentButton.setEnabled(true);
                }
            }
        } else if (mEvent.getCycle() >= 3){
            if (mEvent.getCount() == 1){
                if (mEvent.getToxicLevel() <= 2){
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    continueButton.setEnabled(true);
                    stopAppointmentButton.setEnabled(false);
                } else if (mEvent.getToxicLevel() == 3){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    continueButton.setEnabled(true);
                    mEvent.setDose(mPatient.getDose());
                } else if (mEvent.getToxicLevel() > 3){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    continueButton.setEnabled(true);
                    mEvent.setDose(mPatient.getDose());
                }
            } else if (mEvent.getCount() == 14){
                if (mEvent.getToxicLevel() >= 4){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    continueButton.setEnabled(true);
                    mEvent.setDose(mPatient.getDose());
                } else {
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    stopAppointmentButton.setEnabled(false);
                }
            } else if (mEvent.getCount() == 21){
                if (mEvent.getToxicLevel() >= 4){
                    stopAppointmentButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).lover().description());
                    mPatient.setDose(Dose.getByDose(mPatient.getDose()).lover().getDose());
                    continueButton.setEnabled(true);
                    mEvent.setDose(mPatient.getDose());
                } else {
                    mPatient.setDose(mPatient.getDose());
                    mEvent.setDose(mPatient.getDose());
                    continueButton.setEnabled(true);
                    continueButton.setText(Dose.getByDose(mPatient.getDose()).description());
                    stopAppointmentButton.setEnabled(false);
                }
            }
        }
    }

    public Event fillEvent(Event event, String date, int cycle, int count){
        event.setEventDate(date);
        event.setCycle(cycle);
        event.setCount(count);
        event.setPatientId(mPatient.getId());
        event.setDose(mPatient.getDose());
        return event;
    }

    public void fillEvent(Event event, int cycle, int count){
        event.setCycle(cycle);
        event.setCount(count);
        event.setPatientId(mPatient.getId());
        event.setDose(mPatient.getDose());
    }

    public Patient fillPatient(Patient patient, String date, int cycle){
        patient.setCycleCount(cycle);
        patient.setDose(Dose.DOSE_125.getDose());
        patient.setToxicLevel(0);
        patient.setCycleStartDate(date);
        return patient;
    }

    public void fillPatient(Patient patient){
        if (patient.getDose() == 0){
            patient.setDose(Dose.DOSE_125.getDose());
        }
    }

    public void refreshPatient(Context context){
        mPatient = getPatientFromDB(context, mPatient.getId());
    }

    public Patient getPatientFromDB(Context context, long patientId) {
        return ((App) context).getAppDatabase().patientDao().findPatientById(patientId);
    }

    public Event getEventFromDB(Context context, String eventDate){
        return ((App)context).getAppDatabase().eventDao().findByDateAndPatient(eventDate, mPatient.getId());
    }

    public long insertEvent(Context context){
        return ((App)context).getAppDatabase().eventDao().insert(mEvent);
    }

    public void insetPatient(Context context){
        mPatient.setId(((App)context).getAppDatabase().patientDao().insert(mPatient));
    }

    public void updatePatient(Context context){
        ((App)context).getAppDatabase().patientDao().update(mPatient);
    }

    public void stopAppointment(Context context){
        ((App)context).getAppDatabase().eventDao().deleteWithId(mPatient.getId());
        ((App)context).getAppDatabase().patientDao().delete(mPatient);
    }

}
