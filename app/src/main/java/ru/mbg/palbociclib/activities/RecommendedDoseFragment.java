package ru.mbg.palbociclib.activities;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.AppError;
import ru.mbg.palbociclib.Constants;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.UserDefaultsSettings;
import ru.mbg.palbociclib.models.AppointmentState;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;
import ru.mbg.palbociclib.utils.DateUtils;
import ru.mbg.palbociclib.utils.GuiUtils;
import ru.mbg.palbociclib.views.GradeCountView;

public class RecommendedDoseFragment extends AbsAppointmentFragment {

    @BindView(R.id.grade_count_view)
    protected GradeCountView mGradeCountView;
    @BindView(R.id.dose_text_view1)
    protected TextView mDoseTextView;
    @BindView(R.id.next_date_oak)
    protected TextView mNextOakTextView;
    @BindView(R.id.next_reception_date)
    protected TextView mNextReceptionDateTextView;
    @BindView(R.id.confirm_appointment_button)
    protected Button mConfirmAppointmentButton;

    private AppointmentModel mAppointmentModel;

    private Date nextOakDate;

    private Date nextReceptionDate;

    private TreatmentDose mTreatmentDose;

    public static RecommendedDoseFragment newInstance(String patientID, AppointmentModel appointmentModel) {
        RecommendedDoseFragment fragment = new RecommendedDoseFragment();
        Bundle args = new Bundle();
        args.putString(PATIENT_ID, patientID);
        args.putSerializable(ARGUMENTS, appointmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppointmentModel = (AppointmentModel) getArguments().getSerializable(ARGUMENTS);
        nextOakDate = DateUtils.getCurrentDate();
        nextReceptionDate = DateUtils.getCurrentDate(1);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommended_dose, container, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Treatment currentTreatment = mPatientModel.getPatient().getTreatments().last();
        mDoseTextView.setText(currentTreatment.getDose().description());

        mGradeCountView.setGradeCount(mAppointmentModel.grade);
        mGradeCountView.setNeutrophilsCount((int)mAppointmentModel.neutrophils);

        mNextOakTextView.setText(DateUtils.getCurrentDateString());
        //mNextReceptionDateTextView.setText(DateUtils.getCurrentDateString());

        mNextOakTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.showDatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nextOakDate = DateUtils.getDate(year, month, dayOfMonth);
                        mNextOakTextView.setText(DateUtils.format(year, month, dayOfMonth));
                    }
                });
            }
        });
        mNextReceptionDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.showDatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nextReceptionDate = DateUtils.getDate(year, month, dayOfMonth);
                        mNextReceptionDateTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mNextReceptionDateTextView.setText(DateUtils.format(year, month, dayOfMonth));
                    }
                });
            }
        });
        mDoseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseTreatmentDoseDialog();
            }
        });
        mConfirmAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTreatmentDose == null){
                    showChooseTreatmentDoseDialog();
                } else {
                    oakAction();
                }
            }
        });
    }

    private void showChooseTreatmentDoseDialog(){
        GuiUtils.showSelectDialog(getContext(), getString(R.string.choose_treatment_dose), TreatmentDose.getAsStringList(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTreatmentDose = TreatmentDose.fromRawValue(which + 1);
                mDoseTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                mDoseTextView.setText(mTreatmentDose.description());
            }
        }, false);
    }

    private void oakAction() {
        try {
//            switch (mode) {
//                case saveOAK:
                    double leukocytes = mAppointmentModel.leukocytes;
                    double neutrophilis = mAppointmentModel.neutrophils / 100.0;
                    double platelets = mAppointmentModel.platelets;
                    double erythrocytes = mAppointmentModel.erythrocytes;
                    double hemoglobin = mAppointmentModel.hemoglobin;
                    //boolean fever = feverSwitch.isChecked();
                    mPatientModel.saveOAK((int)leukocytes, (int)neutrophilis, (int)platelets, (int)erythrocytes, (int)hemoglobin, false);
                    AppointmentState result = mPatientModel.appointment(nextReceptionDate, mTreatmentDose);
                    if (result == AppointmentState.done) {
                        ((MainActivity)getActivity()).openFragment(PatientCardFragment.newInstance(getArguments().getString(PATIENT_ID)));
                    } else {
                        GuiUtils.displayOkDialog(getContext(), R.string.error_error_save_appointment, R.string.assign_next_oak, null, true);
                    }
//                    break;
//                case assignOAK:
//                    if (setDate == null) {
//                        return;
//                    }
                    //mPatientModel.assignOAKAndAppointmentAt(nextOakDate, nextReceptionDate);
//                    getFragmentManager().popBackStack();
//                    break;
//                case assignContinueDate:
//                    if (setDate == null) {
//                        return;
//                    }
//                    mPatientModel.assignContinueTreatmentAt(setDate);
//                    getFragmentManager().popBackStack();
//                    break;
//                case reduceDose:
////                    if (selectedDose == null) {
////                        return;
////                    }
////                    mPatientModel.selectLowerDose(selectedDose);
//                    getFragmentManager().popBackStack();
//                    break;
//                case close:
//                    getFragmentManager().popBackStack();
//                    break;
//            }
        } catch (NumberFormatException | AppError appError) {
            Log.e(Constants.TAG, "Error in action", appError);
            GuiUtils.displayOkDialog(getContext(), R.string.error_error_save_appointment, R.string.error_wrong_aok_data, null, true);
        }
    }

    private enum Mode {
        saveOAK, assignOAK, assignContinueDate, reduceDose, close
    }

}
