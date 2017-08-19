package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;

public class GradeCountView extends LinearLayout {

    @BindView(R.id.grade_count_text_view)
    protected TextView mGradeCountTextView;
    @BindView(R.id.neutrophils_count_text_view)
    protected TextView mNeutrophilsCountTextView;
    @BindView(R.id.edit_button)
    protected ImageButton mEditButton;

    private View mRootView;

    public GradeCountView(Context context) {
        super(context);
        init();
    }

    public GradeCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradeCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GradeCountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.grade_count_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    public void setGradeCount(int count){
        mGradeCountTextView.setText(getContext().getString(R.string.grade_count, count));
    }

    public void setNeutrophilsCount(int count){
        mNeutrophilsCountTextView.setText(getContext().getString(R.string.neutrophils_count_for_grade, count));
    }

    public void setEditButtonClickListener(OnClickListener listener){
        mEditButton.setOnClickListener(listener);
    }

}
