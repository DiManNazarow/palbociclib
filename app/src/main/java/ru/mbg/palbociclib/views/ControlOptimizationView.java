package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import ru.mbg.palbociclib.R;

public class ControlOptimizationView extends InfoView {

    public ControlOptimizationView(Context context) {
        super(context);
    }

    public ControlOptimizationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlOptimizationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ControlOptimizationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_optimization_view;
    }
}
