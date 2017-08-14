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

public class GradeView extends LinearLayout {

    @BindView(R.id.grade_picker_view)
    protected GradePickerView mGradePickerView;
    @BindView(R.id.neutrophilis_count)
    protected TextView mNeutrophilisTextView;

    private View mRootView;

    public GradeView(Context context) {
        super(context);
        init();
    }

    public GradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GradeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    public void setNeutrophilisTextCount(String count){
        mNeutrophilisTextView.setText(count);
    }
}
