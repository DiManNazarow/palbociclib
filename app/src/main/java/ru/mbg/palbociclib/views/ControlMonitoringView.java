package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import ru.mbg.palbociclib.R;

public class ControlMonitoringView extends InfoView {

    @BindView(R.id.digit_text_median)
    protected TextView mDigitTextMedian;
    @BindView(R.id.digit_text_average)
    protected TextView mDigitTextAverage;

    public ControlMonitoringView(Context context) {
        super(context);
    }

    public ControlMonitoringView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlMonitoringView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ControlMonitoringView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_monitoring_view;
    }

    @Override
    protected void init(){
        super.init();
        setDigitText();
    }

    @SuppressWarnings("deprecation")
    private void setDigitText(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mDigitTextMedian.setText(Html.fromHtml("<b>15</b><br>дней", Html.FROM_HTML_MODE_LEGACY));
            mDigitTextAverage.setText(Html.fromHtml("<b>7</b><br>дней", Html.FROM_HTML_MODE_LEGACY));
        } else {
            mDigitTextMedian.setText(Html.fromHtml("<b>15</b><br>дней"));
            mDigitTextAverage.setText(Html.fromHtml("<b>7</b><br>дней"));
        }
    }
}
