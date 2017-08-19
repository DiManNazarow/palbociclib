package ru.mbg.palbociclib.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.utils.GuiUtils;
import ru.mbg.palbociclib.utils.Utils;

public class SetGradeView extends LinearLayout implements GradePickerView.OnGradeChangeListener{

    @BindView(R.id.grade_picker_view)
    protected GradePickerView mGradePickerView;
    @BindView(R.id.neutrophils_count_edit)
    protected EditText mNeutrophilisTextView;

    private View mRootView;

    private double mNeutrophilisCount = -1.0;

    private int mGrade = 1;

    public SetGradeView(Context context) {
        super(context);
        init();
    }

    public SetGradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SetGradeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SetGradeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view, this, true);
        ButterKnife.bind(this, mRootView);
        mGradePickerView.setGradeChangeListener(this);
    }

    @OnTextChanged(R.id.neutrophils_count_edit)
    protected void onNeutrophilisCountChange(CharSequence text){
        if (!Utils.isEmpty(text.toString())) {
            mNeutrophilisCount = Double.parseDouble(text.toString());
        } else {
            mNeutrophilisCount = -1.0;
        }
    }

    private boolean mGradeSetError = false;

    @Override
    public void onGradeChange(int grade) {
        if (mNeutrophilisCount < 0 && !mGradeSetError){
            mGradeSetError = true;
            GuiUtils.displayOkDialog(getContext(), R.string.error_miss_neutrophils_for_grade_title, R.string.error_miss_neutrophils_for_grade_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mGradeSetError = false;
                    mGradePickerView.setGradeSeek(0);
                }
            }, false);
        } else {
            mGrade = grade;
        }
    }

    public Double getNeutrophilisCount() {
        return mNeutrophilisCount;
    }

    public int getGrade() {
        return mGrade;
    }

}
