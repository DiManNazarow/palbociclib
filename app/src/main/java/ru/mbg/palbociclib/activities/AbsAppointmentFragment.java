package ru.mbg.palbociclib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.views.ItemView;
import ru.mbg.palbociclib.views.LastOakView;

public abstract class AbsAppointmentFragment extends Fragment {

    protected static final String PATIENT_ID = "patient_id";

    protected static final String ARGUMENTS = "arguments";

    @BindView(R.id.last_oak_item)
    protected ItemView mLastResultItem;
    @BindView(R.id.last_oak_view)
    protected LastOakView mLastOakView;

    protected PatientModel mPatientModel;

    protected boolean isLastOakVisible = false;

    protected AppointmentModel mAppointmentModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPatientModel = getPatientModel();
        mAppointmentModel = new AppointmentModel();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupViews();
    }

    protected void setupViews(){
        mLastResultItem.setTitleTextViewText(R.string.last_result);
        mLastResultItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLastOakVisible){
                    mLastOakView.setVisibility(View.GONE);
                    isLastOakVisible = false;
                } else {
                    mLastOakView.setVisibility(View.VISIBLE);
                    isLastOakVisible = true;
                }
            }
        });
        setupLastOkaResult();
    }

    private void setupLastOkaResult(){
        RealmList<Treatment> treatments = mPatientModel.getPatient().getTreatments();
        if (treatments.size() > 0){
            Treatment treatment = treatments.last();
            Oak oakReady = treatment.getOaks().where().isNotNull("readyDate").findAll().last(null);
            if (oakReady != null) {
                mLastOakView.setCycleText(getString(R.string.cycle_oak_card, oakReady.getGrade()));
                DateTime nowDate = new DateTime(Calendar.getInstance());
                DateTime startDrugDate = new DateTime(treatment.getStartDate());
                mLastOakView.setDayText(getString(R.string.patient_drugs_day_count, Days.daysBetween(startDrugDate, nowDate).getDays()));
                mLastOakView.setDoseText(treatment.getDose().description());
                mLastOakView.setGradeText(getString(R.string.grade_count, oakReady.getGrade()));
                mLastOakView.setNeutrophilsCountText(String.valueOf(oakReady.getNeutrophils()));
                mLastOakView.setLeukocytesCountText(String.valueOf(oakReady.getLeukocytes()));
                mLastOakView.setPlateletsCountText(String.valueOf(oakReady.getPlatelets()));
                mLastOakView.setErythrocytesCountText(String.valueOf(oakReady.getErythrocytes()));
                mLastOakView.setHemoglobinCountText(String.valueOf(oakReady.getHemoglobin()));
            } else {
                mLastResultItem.setVisibility(View.GONE);
                mLastOakView.setVisibility(View.GONE);
            }
        }
    }

    protected abstract PatientModel getPatientModel();

    public static class AppointmentModel implements Serializable {

        public double leukocytes = -1;

        public double neutrophils = -1.0;

        public double platelets = -1;

        public double erythrocytes = -1;

        public double hemoglobin = -1;

        public int grade = -1;

    }

}
