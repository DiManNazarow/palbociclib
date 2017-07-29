package ru.mbg.palbociclib.activities;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.mbg.palbociclib.Constants;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.Settings;
import ru.mbg.palbociclib.UserDefaultsSettings;
import ru.mbg.palbociclib.helpers.DateHelper;


public class ProfileFragment extends Fragment {
    Settings settings;
    DateFormat format;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new UserDefaultsSettings(getContext());
        format = SimpleDateFormat.getDateInstance(java.text.DateFormat.SHORT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // Настройка срока изготовления ОАК

        final EditText oakReadyEditText = (EditText) v.findViewById(R.id.oak_ready_value);
        oakReadyEditText.setText(String.valueOf(settings.getOakReadyDays()));

        oakReadyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                settings.setOakReadyDays(Integer.parseInt(editable.toString()));
            }
        });

        // Настройка времени для отладки

        final TextView mockDateTextView = (TextView) v.findViewById(R.id.mock_date_value);
        if (DateHelper.instance.mockDate == null) {
            mockDateTextView.setText(R.string.mockdate_system);
        } else {
            mockDateTextView.setText(format.format(DateHelper.instance.mockDate));
        }

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                DateHelper.instance.mockDate = cal.getTime();
                mockDateTextView.setText(format.format(DateHelper.instance.mockDate));
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putLong(Constants.MOCK_DATE_KEY, DateHelper.instance.mockDate.getTime()).apply();
            }
        };

        v.findViewById(R.id.mock_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateHelper.instance.currentDate());
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(DateHelper.advancingDays(DateHelper.instance.currentDate(), -30).getTime());
                datePickerDialog.getDatePicker().setMaxDate(DateHelper.advancingDays(DateHelper.instance.currentDate(), 30).getTime());
                datePickerDialog.show();
            }
        });

        v.findViewById(R.id.reset_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateHelper.instance.mockDate = null;
                mockDateTextView.setText(R.string.mockdate_system);
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove(Constants.MOCK_DATE_KEY).apply();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_more);
    }
}
