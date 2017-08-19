package ru.mbg.palbociclib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;
import ru.mbg.palbociclib.AppError;
import ru.mbg.palbociclib.Constants;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.UserDefaultsSettings;
import ru.mbg.palbociclib.models.Appointment;
import ru.mbg.palbociclib.models.AppointmentState;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;
import ru.mbg.palbociclib.views.CustomButton;
import ru.mbg.palbociclib.views.LastOakView;
import ru.mbg.palbociclib.views.SetGradeView;
import ru.mbg.palbociclib.views.ItemView;
import ru.mbg.palbociclib.views.OakInfoView;
import ru.mbg.palbociclib.views.PagePickerView;

public class AppointmentFragmentNew extends AbsAppointmentFragment {

    private static final String PATIENT_ID = "patient_id";

    @BindView(R.id.page_picker_view)
    protected PagePickerView mPagePickerView;
    @BindView(R.id.grade_view)
    protected SetGradeView mSetGradeView;
    @BindView(R.id.oak_info_view)
    protected OakInfoView mOakInfoView;
    @BindView(R.id.calculate_dose_button)
    protected CustomButton mCalculateDoseButton;

    public static AppointmentFragmentNew newInstance(String patientID) {
        AppointmentFragmentNew fragment = new AppointmentFragmentNew();
        Bundle args = new Bundle();
        args.putString(PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected PatientModel getPatientModel() {
        if (getArguments() != null) {
            String patientID = getArguments().getString(PATIENT_ID);
            return new PatientModel(patientID, new UserDefaultsSettings(getContext()));
        } else {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.appointment_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_new, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPagePickerView.setFirstPickerText(R.string.set_greid);
        mPagePickerView.setSecondPickerText(R.string.find_by_oak);
        mPagePickerView.setOnPickerClickListener(new PagePickerView.OnPickerClickListener() {
            @Override
            public void onFirstPickerClick() {
                mSetGradeView.setVisibility(View.VISIBLE);
                mOakInfoView.setVisibility(View.GONE);
                mCalculateDoseButton.setEnabled(true);
            }

            @Override
            public void onSecondPickerClick() {
                mSetGradeView.setVisibility(View.GONE);
                mOakInfoView.setVisibility(View.VISIBLE);
                mCalculateDoseButton.setEnabled(mOakInfoView.isGradeCalculate());
            }
        });
        mOakInfoView.setGradeStateChangeListener(new OakInfoView.GradeStateChangeListener() {
            @Override
            public void gradeStateChange(boolean state) {
                mCalculateDoseButton.setEnabled(state);
            }
        });
        mCalculateDoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openFragment(RecommendedDoseFragment.newInstance(getArguments().getString(PATIENT_ID), fillAppointmentModel()));
            }
        });
    }

    private AppointmentModel fillAppointmentModel(){
        if (mSetGradeView.getVisibility() == View.VISIBLE){
            mAppointmentModel.grade = mSetGradeView.getGrade();
            mAppointmentModel.neutrophils = mSetGradeView.getNeutrophilisCount();
        }
        if (mOakInfoView.getVisibility() == View.VISIBLE){
            mAppointmentModel.grade = mOakInfoView.getGrade();
            mAppointmentModel.neutrophils = mOakInfoView.getNeutrophilsCount();
            mAppointmentModel.erythrocytes = mOakInfoView.getErythrocytesCount();
            mAppointmentModel.leukocytes = mOakInfoView.getLeukocytesCount();
            mAppointmentModel.platelets = mOakInfoView.getPlateletsCount();
            mAppointmentModel.hemoglobin = mOakInfoView.getHemoglobinCount();
        }
        return mAppointmentModel;
    }
}
