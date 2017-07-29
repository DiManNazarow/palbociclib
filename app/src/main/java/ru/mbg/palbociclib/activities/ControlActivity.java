package ru.mbg.palbociclib.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.views.ControlMonitoringView;
import ru.mbg.palbociclib.views.ControlOptimizationView;
import ru.mbg.palbociclib.views.ControlStartView;

public class ControlActivity extends AppCompatActivity {

    public static final String CONTROL_TYPE = "control_view_type";

    public static final int START_VIEW_TYPE = 1;

    public static final int MONITORING_VIEW_TYPE = 2;

    public static final int OPTIMIZATION_VIEW_TYPE = 3;

    @BindView(R.id.start_view)
    protected ControlStartView mControlStartView;
    @BindView(R.id.monitor_view)
    protected ControlMonitoringView mControlMonitoringView;
    @BindView(R.id.optimization_view)
    protected ControlOptimizationView mControlOptimizationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);
        setNavigationButton();
        setTitle(R.string.control);
        openControlInfo(getIntent().getIntExtra(CONTROL_TYPE, -1));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setNavigationButton(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void openControlInfo(int type){
        switch (type){
            case START_VIEW_TYPE: {
                mControlStartView.openInfo();
                break;
            }
            case MONITORING_VIEW_TYPE: {
                mControlMonitoringView.openInfo();
                break;
            }
            case OPTIMIZATION_VIEW_TYPE: {
                mControlOptimizationView.openInfo();
                break;
            }
        }
    }


}
