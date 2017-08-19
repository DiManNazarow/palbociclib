package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;

public class LastOakView extends LinearLayout {

    @BindView(R.id.cycle_text_view)
    protected TextView mCycleTextView;
    @BindView(R.id.day_count_text_view)
    protected TextView mDayTextView;
    @BindView(R.id.dose_text_view)
    protected TextView mDoseTextView;

    @BindView(R.id.grade_text_view)
    protected TextView mGradeTextView;

    @BindView(R.id.neutrophils_count_text_view)
    protected TextView mNeutrophilsCountTextView;
    @BindView(R.id.leukocytes_count_text_view)
    protected TextView mLeukocytesCountTextView;
    @BindView(R.id.platelets_count_text_view)
    protected TextView mPlateletsCountTextView;
    @BindView(R.id.erythrocytes_count_text_view)
    protected TextView mErythrocytesCountTextView;
    @BindView(R.id.hemoglobin_count_text_view)
    protected TextView mHemoglobinCountTextView;

    private View mRootView;

    public LastOakView(Context context) {
        super(context);
        init();
    }

    public LastOakView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LastOakView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LastOakView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.last_oak_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    public void setCycleText(String text){
        mCycleTextView.setText(text);
    }

    public void setDayText(String text){
        mDayTextView.setText(text);
    }

    public void setDoseText(String text){
        mDoseTextView.setText(text);
    }

    public void setGradeText(String text){
        mGradeTextView.setText(text);
    }

    public void setNeutrophilsCountText(String text){
        mNeutrophilsCountTextView.setText(text);
    }

    public void setLeukocytesCountText(String text){
        mLeukocytesCountTextView.setText(text);
    }

    public void setPlateletsCountText(String text){
        mPlateletsCountTextView.setText(text);
    }

    public void setErythrocytesCountText(String text){
        mErythrocytesCountTextView.setText(text);
    }

    public void setHemoglobinCountText(String text){
        mHemoglobinCountTextView.setText(text);
    }
}
