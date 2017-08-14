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


public class OakInfoView extends LinearLayout {

    @BindView(R.id.neutrophils_count_text)
    protected TextView mNeutrophilsCount;
    @BindView(R.id.leukocytes_count_text)
    protected TextView mLeukocytesCount;
    @BindView(R.id.platelets_count_text)
    protected TextView mPlateletsCount;
    @BindView(R.id.erythrocytes_count_text)
    protected TextView mErythrocytesCount;
    @BindView(R.id.hemoglobin_count_text)
    protected TextView mHemoglobinCount;

    private View mRootView;

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

    public void setNeutrophilsCount(String neutrophilsCount) {
        mNeutrophilsCount.setText(neutrophilsCount);
    }

    public void setLeukocytesCount(String leukocytesCount) {
        mLeukocytesCount.setText(leukocytesCount);
    }

    public void setPlateletsCount(String plateletsCount) {
        mPlateletsCount.setText(plateletsCount);
    }

    public void setErythrocytesCount(String erythrocytesCount) {
        mErythrocytesCount.setText(erythrocytesCount);
    }

    public void setHemoglobinCount(String hemoglobinCount) {
        mHemoglobinCount.setText(hemoglobinCount);
    }
}
