package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.utils.Utils;


public class OakInfoView extends LinearLayout {

    @BindView(R.id.neutrophils_edit_text)
    protected EditText mNeutrophilsCountEditText;
    @BindView(R.id.leukocytes_edit_text)
    protected EditText mLeukocytesCountEditText;
    @BindView(R.id.platelets_edit_text)
    protected EditText mPlateletsCountEditText;
    @BindView(R.id.erythrocytes_edit_text)
    protected EditText mErythrocytesCountEditText;
    @BindView(R.id.hemoglobin_edit_text)
    protected EditText mHemoglobinCountEditText;
    @BindView(R.id.button)
    protected Button mKnowGradeButton;

    private View mRootView;

    private double mNeutrophilsCount = -1.0;

    private double mLeukocytesCount = -1.0;

    private double mPlateletsCount = -1.0;

    private double mErythrocytesCount = -1.0;

    private double mHemoglobinCount = -1.0;

    public OakInfoView(Context context) {
        super(context);
        init();
    }

    public OakInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OakInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OakInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.oak_info_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    @OnTextChanged(R.id.neutrophils_edit_text)
    protected void onNeutrophilsCountChange(CharSequence text){
        if (!Utils.isEmpty(text.toString())){
            mNeutrophilsCount = Double.parseDouble(text.toString());
        } else {
            mNeutrophilsCount = -1.0;
        }
    }

    @OnTextChanged(R.id.leukocytes_edit_text)
    protected void onLeukocytesCountChange(CharSequence text){
        if (!Utils.isEmpty(text.toString())){
            mLeukocytesCount = Double.parseDouble(text.toString());
        } else {
            mLeukocytesCount = -1.0;
        }
    }

    @OnTextChanged(R.id.platelets_edit_text)
    protected void onPlateletsCountChange(CharSequence text){
        if (!Utils.isEmpty(text.toString())){
            mPlateletsCount = Double.parseDouble(text.toString());
        } else {
            mPlateletsCount = -1.0;
        }
    }

    @OnTextChanged(R.id.erythrocytes_edit_text)
    protected void onErythrocytesCountChange(CharSequence text){
        if (!Utils.isEmpty(text.toString())){
            mErythrocytesCount = Double.parseDouble(text.toString());
        } else {
            mErythrocytesCount = -1.0;
        }
    }

    @OnTextChanged(R.id.hemoglobin_edit_text)
    protected void onHemoglobinCountChange(CharSequence text){
        if (!Utils.isEmpty(text.toString())){
            mHemoglobinCount = Double.parseDouble(text.toString());
        } else {
            mHemoglobinCount = -1.0;
        }
    }

    @OnClick(R.id.button)
    protected void onKnowGradeButtonClick(){
        int grade;
        double ln = mLeukocytesCount * ( mNeutrophilsCount / 100.0 );
        if (ln < 500) {
            grade = 4;
        } else if (ln <= 1000) {
            grade = 3;
        } else if (ln < 1500) {
            grade = 2;
        } else {
            grade = 1;
        }
        mKnowGradeButton.setText(String.valueOf(grade));
    }

    public double getNeutrophilsCount() {
        return mNeutrophilsCount;
    }

    public double getLeukocytesCount() {
        return mLeukocytesCount;
    }

    public double getPlateletsCount() {
        return mPlateletsCount;
    }

    public double getErythrocytesCount() {
        return mErythrocytesCount;
    }

    public double getHemoglobinCount() {
        return mHemoglobinCount;
    }
}
