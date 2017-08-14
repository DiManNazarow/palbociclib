package ru.mbg.palbociclib.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.UserDefaultsSettings;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.views.GradePickerView;
import ru.mbg.palbociclib.views.GradeView;
import ru.mbg.palbociclib.views.ItemView;
import ru.mbg.palbociclib.views.OakInfoView;
import ru.mbg.palbociclib.views.PagePickerView;
import ru.mbg.palbociclib.views.WrapContentViewPager;

public class AppointmentFragmentNew extends Fragment {

    private static final String PATIENT_ID = "patient_id";

    private PatientModel patientModel;

    @BindView(R.id.last_result_item)
    protected ItemView mLastResultItem;
    @BindView(R.id.page_picker_view)
    protected PagePickerView mPagePickerView;
    @BindView(R.id.grade_view)
    protected GradeView mGradeView;
    @BindView(R.id.oak_info_view)
    protected OakInfoView mOakInfoView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_new, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mLastResultItem.setTitleTextViewText(R.string.last_result);
        mPagePickerView.setFirstPickerText(R.string.set_greid);
        mPagePickerView.setSecondPickerText(R.string.find_by_oak);
        mPagePickerView.setOnPickerClickListener(new PagePickerView.OnPickerClickListener() {
            @Override
            public void onFirstPickerClick() {
                mGradeView.setVisibility(View.VISIBLE);
                mOakInfoView.setVisibility(View.GONE);
            }

            @Override
            public void onSecondPickerClick() {
                mGradeView.setVisibility(View.GONE);
                mOakInfoView.setVisibility(View.VISIBLE);
            }
        });
        setupGradeInfo();
    }

    private void setupGradeInfo(){
        Treatment treatment = patientModel.getPatient().getTreatments().last();
        if (treatment != null) {
            Oak oak = treatment.getOaks().last();
            if (oak != null && oak.getReadyDate() != null) {
                double netrophils = oak.getNeutrophils()/100;
                mGradeView.setNeutrophilisTextCount(getString(R.string.neutrophils_count, netrophils));
            }
        }
    }
}
