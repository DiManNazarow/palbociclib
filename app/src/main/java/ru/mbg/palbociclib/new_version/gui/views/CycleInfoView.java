package ru.mbg.palbociclib.new_version.gui.views;

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

public class CycleInfoView extends LinearLayout {

    @BindView(R.id.cycle_text_view)
    protected TextView mCycleTextView;
    @BindView(R.id.cycle_date_text_view)
    protected TextView mDateTextView;
    @BindView(R.id.close_button)
    protected ImageButton mCloseButton;

    protected View mRootView;

    public CycleInfoView(Context context) {
        super(context);
        init();
    }

    public CycleInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CycleInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CycleInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_cycle_info_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    public void setCycleText(String text){
        mCycleTextView.setText(text);
    }

    public void setDateText(String text){
        mDateTextView.setText(text);
    }

    public void setCloseButtonClickListener(OnClickListener onClickListener){
        mCloseButton.setOnClickListener(onClickListener);
    }
}
