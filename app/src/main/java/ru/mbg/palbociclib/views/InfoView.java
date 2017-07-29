package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;

/**
 * Created by Дмитрий on 29.07.2017.
 */

public abstract class InfoView extends LinearLayout {

    @BindView(R.id.container)
    protected View mContainerView;

    protected boolean isInfoVisible;

    public InfoView(Context context) {
        super(context);
        init();
    }

    public InfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init(){
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        ButterKnife.bind(this, this);
        isInfoVisible = false;
    }

    protected abstract int getLayoutId();

    @OnClick(R.id.title)
    protected void onTitleClick(){
        if (isInfoVisible){
            mContainerView.setVisibility(GONE);
            isInfoVisible = false;
        } else {
            mContainerView.setVisibility(VISIBLE);
            isInfoVisible = true;
        }
    }

    public void openInfo(){
        mContainerView.setVisibility(VISIBLE);
        isInfoVisible = true;
    }
}
