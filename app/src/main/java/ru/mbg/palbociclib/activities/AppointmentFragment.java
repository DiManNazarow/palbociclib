package ru.mbg.palbociclib.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.mbg.palbociclib.AppError;
import ru.mbg.palbociclib.models.AppointmentState;
import ru.mbg.palbociclib.Constants;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.UserDefaultsSettings;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;


public class AppointmentFragment extends Fragment {

    private static final String PATIENT_ID = "patient_id";

    private PatientModel patientModel;
    private Mode mode = Mode.saveOAK;
    private Date setDate;
    private TreatmentDose selectedDose;

    private TextView leukocytesTextView;
    private TextView neutrophilisTextView;
    private TextView plateletsTextView;
    private Switch feverSwitch;

    private TextView assignNextOakTextView;
    private View dateSelector;
    private TextView dateSelectorValueTextView;
    private TextView dateSelectorTextView;
    private View selectLowerDose;
    private Button addButton;

    public AppointmentFragment() {
    }

    public static AppointmentFragment newInstance(String patientID) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String patientID = getArguments().getString(PATIENT_ID);
            patientModel = new PatientModel(patientID, new UserDefaultsSettings(getContext()));
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.appointment_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        leukocytesTextView = (TextView) view.findViewById(R.id.leukocytes);
        neutrophilisTextView = (TextView) view.findViewById(R.id.neutrophilis);
        plateletsTextView = (TextView) view.findViewById(R.id.platelets);
        feverSwitch = (Switch) view.findViewById(R.id.fever);

        assignNextOakTextView = (TextView) view.findViewById(R.id.assign_next_oak);
        dateSelector = view.findViewById(R.id.date_selector);
        dateSelectorTextView = (TextView) view.findViewById(R.id.date_selector_text);
        dateSelectorValueTextView = (TextView) view.findViewById(R.id.date_selector_value);
        selectLowerDose = view.findViewById(R.id.select_lower_dose);

        addButton = (Button) view.findViewById(R.id.add_button);

        Treatment treatment = patientModel.getPatient().getTreatments().last();
        if (treatment != null) {
            Oak oak = treatment.getOaks().last();
            if (oak != null && oak.getReadyDate() != null) {
                leukocytesTextView.setText(String.valueOf(oak.getLeukocytes()));
                neutrophilisTextView.setText(String.valueOf(oak.getNeutrophils()));
                plateletsTextView.setText(String.valueOf(oak.getPlatelets()));
                feverSwitch.setChecked(oak.isFever());

                leukocytesTextView.setEnabled(false);
                neutrophilisTextView.setEnabled(false);
                plateletsTextView.setEnabled(false);
                feverSwitch.setEnabled(false);
            }
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                addButton.setEnabled(leukocytesTextView.getText().length() > 0 && neutrophilisTextView.getText().length() > 0 && plateletsTextView.getText().length() > 0);
            }
        };

        leukocytesTextView.addTextChangedListener(watcher);
        neutrophilisTextView.addTextChangedListener(watcher);
        plateletsTextView.addTextChangedListener(watcher);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                setDate = cal.getTime();
                final DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                dateSelectorValueTextView.setText(dateFormat.format(setDate));
                addButton.setEnabled(true);
            }
        };

        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateHelper.instance.currentDate());
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(DateHelper.advancingDays(DateHelper.instance.currentDate(), 1).getTime());
                datePickerDialog.getDatePicker().setMaxDate(DateHelper.advancingDays(DateHelper.instance.currentDate(), 30).getTime());
                datePickerDialog.show();
            }
        });

        CompoundButton.OnCheckedChangeListener radiolistener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    return;
                }
                switch (compoundButton.getId()) {
                    case R.id.dose75:
                        selectedDose = TreatmentDose.dose75;
                        break;
                    case R.id.dose100:
                        selectedDose = TreatmentDose.dose100;
                        break;
                    case R.id.dose125:
                        selectedDose = TreatmentDose.dose125;
                        break;
                }

                addButton.setEnabled(true);
            }
        };

        ((RadioButton) view.findViewById(R.id.dose75)).setOnCheckedChangeListener(radiolistener);
        ((RadioButton) view.findViewById(R.id.dose100)).setOnCheckedChangeListener(radiolistener);
        ((RadioButton) view.findViewById(R.id.dose125)).setOnCheckedChangeListener(radiolistener);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oakAction();
            }
        });

        updateForm(patientModel.getLastAppointmentState());

        return view;
    }

    private void updateForm(AppointmentState result) {
        assignNextOakTextView.setVisibility(View.GONE);
        dateSelector.setVisibility(View.GONE);
        selectLowerDose.setVisibility(View.GONE);

        switch (result) {
            case oakNeeded:
                mode = Mode.saveOAK;
                addButton.setText("Сохранить результаты");
                addButton.setEnabled(false);
                break;
            case done:
                getFragmentManager().popBackStack();
                break;
            case assignNextAppointment:
                mode = Mode.assignOAK;
                assignNextOakTextView.setVisibility(View.VISIBLE);
                assignNextOakTextView.setText(R.string.assign_next_oak);
                dateSelector.setVisibility(View.VISIBLE);
                dateSelectorTextView.setText(R.string.next_treatment_date);
                addButton.setText("Назначить ОАК");
                addButton.setEnabled(false);
                break;
            case assignContinueTreatment:
                mode = Mode.assignContinueDate;
                assignNextOakTextView.setVisibility(View.VISIBLE);
                assignNextOakTextView.setText(R.string.assign_continue_date);
                dateSelector.setVisibility(View.VISIBLE);
                dateSelectorTextView.setText(R.string.continue_date);
                addButton.setText("Назначить дату");
                addButton.setEnabled(false);
                break;
            case maybeLowerDose:
                mode = Mode.reduceDose;
                selectLowerDose.setVisibility(View.VISIBLE);
                addButton.setText("Назначить новую дозу");
                addButton.setEnabled(false);
                break;
            case doseWasReduced:
                mode = Mode.close;
                assignNextOakTextView.setVisibility(View.VISIBLE);
                assignNextOakTextView.setText(R.string.dose_was_reduced);
                addButton.setText("Ок");
                addButton.setEnabled(true);
                break;
        }
    }

    private void oakAction() {
        try {
            switch (mode) {
                case saveOAK:
                    int leukocytes = Integer.parseInt(leukocytesTextView.getText().toString());
                    double neutrophilis = Double.parseDouble(neutrophilisTextView.getText().toString()) / 100.0;
                    int platelets = Integer.parseInt(plateletsTextView.getText().toString());
                    boolean fever = feverSwitch.isChecked();
                    patientModel.saveOAK(leukocytes, neutrophilis, platelets, fever);
                   // patientModel = new PatientModel(getArguments().getString(PATIENT_ID), new UserDefaultsSettings(getContext()));
                    AppointmentState result = patientModel.appointment();
                    if (result == AppointmentState.done) {
                        getFragmentManager().popBackStack();
                    } else {
                        updateForm(result);
                    }
                    break;
                case assignOAK:
                    if (setDate == null) {
                        return;
                    }
                    patientModel.assignOAKAndAppointmentAt(setDate);
                    getFragmentManager().popBackStack();
                    break;
                case assignContinueDate:
                    if (setDate == null) {
                        return;
                    }
                    patientModel.assignContinueTreatmentAt(setDate);
                    getFragmentManager().popBackStack();
                    break;
                case reduceDose:
                    if (selectedDose == null) {
                        return;
                    }
                    patientModel.selectLowerDose(selectedDose);
                    getFragmentManager().popBackStack();
                    break;
                case close:
                    getFragmentManager().popBackStack();
                    break;
            }
        } catch (NumberFormatException | AppError appError) {
            Log.e(Constants.TAG, "Error in action", appError);
            Toast.makeText(getContext(), "Ошибка сохранения информации", Toast.LENGTH_LONG).show();
        }
    }

    private enum Mode {
        saveOAK, assignOAK, assignContinueDate, reduceDose, close
    }
}
