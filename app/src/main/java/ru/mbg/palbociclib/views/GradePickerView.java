package ru.mbg.palbociclib.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;

public class GradePickerView extends LinearLayout implements SeekBar.OnSeekBarChangeListener{

    private final int GRADE_COUNT = 3;

    private final int FIRST_GRADE = 1;

    private final int SECOND_GRADE = 2;

    private final int THIRD_GRADE = 3;

    private final int FOURTH_GRADE = 4;

    @BindView(R.id.first_grade)
    protected TextView mFirstGrade;
    @BindView(R.id.second_grade)
    protected TextView mSecondGrade;
    @BindView(R.id.third_grade)
    protected TextView mThirdGrade;
    @BindView(R.id.fourth_grade)
    protected TextView mFourthGrade;
    @BindView(R.id.grade_sick)
    protected SeekBar mGradeSeek;

    private View mRootView;

    public interface OnGradeChangeListener {
        void onGradeChange(int grade);
    }

    private OnGradeChangeListener mGradeChangeListener;

    public GradePickerView(Context context) {
        super(context);
        init();
    }

    public GradePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GradePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.grade_picker_view, this, true);
        ButterKnife.bind(this, mRootView);
        setupSeekBar();
        pickGradeText(FIRST_GRADE);
    }

    private void setupSeekBar(){
        mGradeSeek.setMax(GRADE_COUNT);
        mGradeSeek.setOnSeekBarChangeListener(this);
    }

    private void pickGradeText(int grade){
        switch (grade){
            case FIRST_GRADE: {
                mFirstGrade.setTypeface(Typeface.DEFAULT_BOLD);
                mSecondGrade.setTypeface(Typeface.DEFAULT);
                mThirdGrade.setTypeface(Typeface.DEFAULT);
                mFourthGrade.setTypeface(Typeface.DEFAULT);
                break;
            }
            case SECOND_GRADE: {
                mFirstGrade.setTypeface(Typeface.DEFAULT);
                mSecondGrade.setTypeface(Typeface.DEFAULT_BOLD);
                mThirdGrade.setTypeface(Typeface.DEFAULT);
                mFourthGrade.setTypeface(Typeface.DEFAULT);
                break;
            }
            case THIRD_GRADE: {
                mFirstGrade.setTypeface(Typeface.DEFAULT);
                mSecondGrade.setTypeface(Typeface.DEFAULT);
                mThirdGrade.setTypeface(Typeface.DEFAULT_BOLD);
                mFourthGrade.setTypeface(Typeface.DEFAULT);
                break;
            }
            case FOURTH_GRADE: {
                mFirstGrade.setTypeface(Typeface.DEFAULT);
                mSecondGrade.setTypeface(Typeface.DEFAULT);
                mThirdGrade.setTypeface(Typeface.DEFAULT);
                mFourthGrade.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        pickGradeText(progress + 1);
        if (fromUser){
            if (mGradeChangeListener != null){
                mGradeChangeListener.onGradeChange(progress);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setGradeChangeListener(OnGradeChangeListener gradeChangeListener) {
        mGradeChangeListener = gradeChangeListener;
    }
}
