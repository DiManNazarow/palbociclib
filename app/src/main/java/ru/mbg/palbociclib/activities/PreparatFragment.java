package ru.mbg.palbociclib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.views.ItemView;

public class PreparatFragment extends Fragment {

    @BindView(R.id.first_control_item)
    protected ItemView mFirstControlItem;
    @BindView(R.id.second_control_item)
    protected ItemView mSecondControlItem;
    @BindView(R.id.third_control_item)
    protected ItemView mThirdControlItem;
    @BindView(R.id.instruction_item)
    protected ItemView mInstructionItem;

    public PreparatFragment() {}

    public static PreparatFragment newInstance() {
        return new PreparatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preparat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setTitles();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_preparat);
    }

    @OnClick(R.id.first_control_item)
    protected void onFirstControlClick(){
        openControlByType(ControlActivity.START_VIEW_TYPE);
    }

    @OnClick(R.id.second_control_item)
    protected void onSecondControlClick(){
        openControlByType(ControlActivity.MONITORING_VIEW_TYPE);
    }

    @OnClick(R.id.third_control_item)
    protected void onThirdControlClick(){
        openControlByType(ControlActivity.OPTIMIZATION_VIEW_TYPE);
    }

    @OnClick(R.id.instruction_item)
    protected void onInstructionClick(){
        openInstruction();
    }

    private void openInstruction(){
        Intent intent = new Intent(getActivity(), ShortInstructionActivity.class);
        startActivity(intent);
    }

    private void openControlByType(int controlType){
        Intent intent = new Intent(getActivity(), ControlActivity.class);
        intent.putExtra(ControlActivity.CONTROL_TYPE, controlType);
        startActivity(intent);
    }

    private void setTitles(){
        mFirstControlItem.setTitleTextViewText(R.string.control_start);
        mSecondControlItem.setTitleTextViewText(R.string.control_monitoring);
        mThirdControlItem.setTitleTextViewText(R.string.control_optimization);
        mInstructionItem.setTitleTextViewText(R.string.instruction);
    }
}
